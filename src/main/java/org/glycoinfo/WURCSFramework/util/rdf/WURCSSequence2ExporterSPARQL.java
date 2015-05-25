package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.sequence2.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSTripleURL_TBD;

/**
 * Class for creation of WURCSDequence SPARQL query
 * @author MasaakiMatsubara
 *
 */
public class WURCSSequence2ExporterSPARQL extends SPARQLQueryGenerator {

	private LinkedList<MS>   m_aMS   = new LinkedList<MS>();
	private LinkedList<GRES> m_aGRES = new LinkedList<GRES>();

	private String m_strGraphURI    = "<http://rdf.glycoinfo.org/wurcs/seq/0.3>";
	private String m_strGraphURIPos = "<http://rdf.glycoinfo.org/wurcs/seq/0.3/pos>";
	private String m_strMSGraphURIDefault = "<http://rdf.glycoinfo.org/wurcs/0.5.1/ms>";

	private String m_strMSGraphURI  = this.m_strMSGraphURIDefault;

	private boolean m_bIsCount = false;

	private String m_strQuery = "";

	public void setCountOption(boolean a_bCount) {
		this.m_bIsCount = a_bCount;
	}

	public void setMSGraphURI(String a_strMSGraphURI) {
		this.m_strMSGraphURI = a_strMSGraphURI;
	}

	public String getQuery() {
		return this.m_strQuery;
	}

	public void start(WURCSSequence2 a_oSeq) {
		this.clear();

		String t_strQuery = "";

		// Header and prefix
		t_strQuery += this.getHeaderString(a_oSeq.getWURCS());

		// SELECT
		LinkedList<String> t_aSELECTVar = new LinkedList<String>();
		boolean t_bDistinct = false;
		if ( this.m_bIsCount ) {
			t_aSELECTVar.add("(count (DISTINCT ?glycan) AS ?count)");
		} else {
			t_aSELECTVar.add("?glycan");
			t_aSELECTVar.add("(str ( ?wurcs ) AS ?WURCS)");
			t_bDistinct = true;
		}
		t_strQuery += this.getSELECTSection(t_aSELECTVar, t_bDistinct);

		if ( this.getTergetGraphURIs().isEmpty() ) {
			this.addTergetGraphURI(this.m_strGraphURI);
			this.addTergetGraphURI(this.m_strGraphURIPos);
		}
		LinkedList<String> t_aNamedGraphURI = new LinkedList<String>();
		t_aNamedGraphURI.add(this.m_strMSGraphURI);

		// FROM
		t_strQuery += this.getFROMSection();
		// FROM NAMED
		t_strQuery += this.getFROMNAMEDSection(t_aNamedGraphURI);

		// WHERE
		t_strQuery += this.getWHERESection( this.getMainQuery(a_oSeq) );

		if ( !this.m_bIsCount )
			t_strQuery += this.getORDERBY("?glycan");

		this.m_strQuery = t_strQuery;
	}

	public String getMainQuery(WURCSSequence2 a_oSeq) {
		return getMainQuery(a_oSeq, true);
	}

	public String getMainQuery(WURCSSequence2 a_oSeq, boolean a_bComments) {

		String t_strMain = this.getSPO("?glycan", "glycan:has_glycosequence", "?gseq");
		t_strMain += this.getSPO("?gseq", "glycan:has_sequence", "?wurcs");

		for ( GLIN t_oGLIN : a_oSeq.getGLINs() ) {

			// GLIN variable string
			String t_strGLINVar = "?GLIN"+t_oGLIN.getID();
			if (a_bComments)
				t_strMain += "\n  # GLIN"+t_oGLIN.getID()+"\n";

			// For acceptor GRES
			for ( GRES t_oGRES : a_oSeq.getGRESs() ) {
				if ( !t_oGRES.getAcceptorGLINs().contains(t_oGLIN) ) continue;
				t_strMain += this.getGRESQuery(t_oGRES, a_bComments);
				int t_iAGRESID = this.m_aGRES.indexOf(t_oGRES)+1;
				t_strMain += this.getSPO("?GRES"+t_iAGRESID, "wurcs:is_acceptor_of", t_strGLINVar);
			}

			// For positions
			t_strMain += this.getGLINPositionQuery(t_oGLIN, t_strGLINVar);
			t_strMain += this.getSPO(t_strGLINVar, "wurcs:has_MAP", t_oGLIN.getMAP());

			// For donor GRES
			for ( GRES t_oGRES : a_oSeq.getGRESs() ) {
				if ( !t_oGRES.getDonorGLINs().contains(t_oGLIN) ) continue;
				t_strMain += this.getGRESQuery(t_oGRES, a_bComments);
				int t_iDGRESID = this.m_aGRES.indexOf(t_oGRES)+1;
				t_strMain += this.getSPO("?GRES"+t_iDGRESID, "wurcs:is_donor_of", t_strGLINVar);
			}
		}

		// For composition
		for ( GRES t_oGRES : a_oSeq.getGRESs() )
			t_strMain += this.getGRESQuery(t_oGRES, a_bComments);

		return t_strMain;

	}

	private String getHeaderString(String a_strQueryStructure) {
		String t_strHeader = "";
		t_strHeader += "# ******************************************************\n";
		t_strHeader += "# Query Structure:\n";
		t_strHeader += "# "+a_strQueryStructure+"\n";
		t_strHeader += "# ******************************************************\n";
		t_strHeader += "\n";
		t_strHeader += "DEFINE sql:select-option \"order\"\n";
		t_strHeader += "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n";
		t_strHeader += "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n";

		return t_strHeader;
	}

	private String getGLINPositionQuery(GLIN a_oGLIN, String a_strGLINVar) {
		String t_strGLIN = "";

		if ( !a_oGLIN.getAcceptorPositions().isEmpty() ) {
			if ( a_oGLIN.getAcceptorPositions().size() == 1 ) {
				String t_strAPos = ""+a_oGLIN.getAcceptorPositions().getFirst();
				t_strGLIN += this.getSPO(a_strGLINVar, "wurcs:has_acceptor_position", t_strAPos);
			} else {
				String t_strPosVar = a_strGLINVar+"PosA";
				t_strGLIN += this.getSPO(a_strGLINVar, "wurcs:has_acceptor_position", t_strPosVar);
				t_strGLIN += this.getPositionVALUES( t_strPosVar, a_oGLIN.getAcceptorPositions());
			}
		}

		if ( !a_oGLIN.getDonorPositions().isEmpty() ) {
			if ( a_oGLIN.getDonorPositions().size() == 1 ) {
				String t_strAPos = ""+a_oGLIN.getDonorPositions().getFirst();
				t_strGLIN += this.getSPO(a_strGLINVar, "wurcs:has_donor_position", t_strAPos);
			} else {
				String t_strPosVar = a_strGLINVar+"PosD";
				t_strGLIN += this.getSPO(a_strGLINVar, "wurcs:has_donor_position", t_strPosVar);
				t_strGLIN += this.getPositionVALUES( t_strPosVar, a_oGLIN.getDonorPositions());
			}
		}

		return t_strGLIN;
	}

	private String getPositionVALUES(String a_strVar, LinkedList<Integer> a_aPositions) {
		String t_strPosition = "";
		t_strPosition += "  VALUES ( "+a_strVar+" ) { ";
		for ( int t_iPos : a_aPositions ) {
			t_strPosition += "(\""+t_iPos+"\"^^xsd:integer"+") ";
		}
		t_strPosition += "}\n";
		return t_strPosition;
	}

	private String getMSQuery(MS a_oMS) {
		if ( this.m_aMS.contains(a_oMS) ) return "";
		this.m_aMS.addLast(a_oMS);

		String t_strMSURI = WURCSTripleURL_TBD.MS.get("", a_oMS.getString());
		String t_strMS = this.getGSPO(this.m_strMSGraphURI, "<"+t_strMSURI+">", "wurcs:subsumes", "?MS"+this.m_aMS.size());
		return t_strMS;
	}

	private String getGRESQuery(GRES a_oGRES) {
		return getGRESQuery(a_oGRES, true);
	}

	private String getGRESQuery(GRES a_oGRES, boolean a_bComments) {
		if ( a_oGRES == null ) return "";
		if ( this.m_aGRES.contains(a_oGRES) ) return "";
		this.m_aGRES.addLast(a_oGRES);
		int t_iGRESID = this.m_aGRES.indexOf(a_oGRES)+1;
		String t_strGRES = "\n";
		if (a_bComments)
			t_strGRES += "  ## GRES"+t_iGRESID+"\n";
		// For gseq
//		t_strGRES += this.getSPO("?gseq", "wurcs:has_GES", "?GRES"+t_iGRESID);
		t_strGRES += this.getSPO("?gseq", "wurcs:has_GRES", "?GRES"+t_iGRESID);

		// For MS
		String t_strMS = this.getMSQuery(a_oGRES.getMS());
		int t_iMSID = this.m_aMS.indexOf(a_oGRES.getMS())+1;
		t_strGRES += this.getSPO("?GRES"+t_iGRESID, "wurcs:is_monosaccharide", "?MS"+t_iMSID);

		// Return if MS was already appeared
		if ( !t_strMS.equals("") ) return t_strGRES+t_strMS+"\n";

		// Filter GRES
		String t_strGRESFilter = "";
		for ( GRES t_oGRES : this.m_aGRES ) {
			if ( t_oGRES.equals(a_oGRES) ) continue;
			if ( !t_oGRES.getMS().equals(a_oGRES.getMS()) ) continue;

			if ( !t_strGRESFilter.equals("") ) t_strGRESFilter += " && ";
			t_strGRESFilter += "?GRES"+t_iGRESID+" != ?GRES"+(this.m_aGRES.indexOf(t_oGRES)+1);
		}
		t_strGRES += "  FILTER ( "+t_strGRESFilter+" )\n\n";

		return t_strGRES;
	}

	private void clear() {
		this.m_aGRES = new LinkedList<GRES>();
		this.m_aMS   = new LinkedList<MS>();

		this.m_strQuery = "";
	}
}
