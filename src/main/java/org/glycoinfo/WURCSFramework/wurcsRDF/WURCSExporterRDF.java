package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSAnobase;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcsRDF.uri.WURCSExporterURI;
import org.glycoinfo.WURCSFramework.wurcsRDF.uri.WURCSExporterURIWithAccessionNumber;

/**
 *
 * @author issaku yamada
 * @author MasaakiMatsubara
 * 
 * TODO: same residue in a row - loops and depth
 * @author nobu aoki
 *
 */
public class WURCSExporterRDF {

//	private String m_strAccessionNumber;
	private String m_strWURCS_RDF;
	private String m_strWURCS_monosaccharide_RDF;

	public void setWURCSrdfTriple(String a_strAccessionNumber, WURCSArray a_oWURCS, Boolean a_bPrefix){
		this.m_strWURCS_monosaccharide_RDF = this.getWURCSMonosaccharideTripleTTL(a_oWURCS.getUniqueRESs());
		this.m_strWURCS_RDF = this.getWURCSGlycanTripleTTL(a_strAccessionNumber, a_oWURCS);
	}

	public String getWURCSPrefix(){
		return PrefixList.getPrefixList();
	}

	public String getWURCS_RDF(){
		return m_strWURCS_RDF;
	}

	public String getWURCS_monosaccharide_RDF(){
		return m_strWURCS_monosaccharide_RDF;
	}

	public String getWURCSMonosaccharideTripleTTL(LinkedList<UniqueRES> a_aUniqueRESs) {

		StringBuilder t_sbMonosaccharide = new StringBuilder();

		WURCSExporterURI t_oExport = new WURCSExporterURI();

		TreeMap<String, UniqueRES> t_mapURItoMS         = new TreeMap<String, UniqueRES>();
		TreeMap<String, UniqueRES> t_mapURItoBasetype   = new TreeMap<String, UniqueRES>();
		TreeMap<String, UniqueRES> t_mapURItoAnobase    = new TreeMap<String, UniqueRES>();
		TreeMap<String, TreeSet<String>> t_mapMSSubsums      = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, TreeSet<String>> t_mapAnobaseSubsums = new TreeMap<String, TreeSet<String>>();
//		TreeMap<String, String> t_mapBasetypeURItoAnobaseURI = new TreeMap<String, String>();

		TreeMap<String, MOD> t_mapURItoMOD = new TreeMap<String, MOD>();

		// Monosaccharide triple
		t_sbMonosaccharide.append("# monosaccharide\n");
		for (UniqueRES t_oURES : a_aUniqueRESs) {

			String t_strMSURI = t_oExport.getMonosaccharideURI(t_oURES);
			t_mapURItoMS.put(t_strMSURI, t_oURES);
			t_sbMonosaccharide.append(t_strMSURI+"\n");
//			t_sbMonosaccharide.append("\t"+PredicateList.A_MS.getAPredicate()); // a  wurcs:monosaccharide
//			t_sbMonosaccharide.append(" ;\n");

			// RING information for pyranose, furanose
			for (MOD mod : t_oURES.getMODs()) {
				if ( mod.getListOfLIPs().size() != 2 ) continue;
				boolean isAnomRing = false;
				for (LIPs lips : mod.getListOfLIPs() ) {
					if ( lips.getLIPs().size() != 1 ) continue;
					if ( lips.getLIPs().getFirst().getBackbonePosition() != t_oURES.getAnomericPosition()  ) continue;
					isAnomRing = true;
					break;
				}
				if ( !isAnomRing ) continue;

//				t_sbMonosaccharide.append("\t"+PredicateList.HAS_RING.getTriple( t_oExport.getMODURI(mod) ));
//				t_sbMonosaccharide.append(" ;\n");
			}

//			System.out.println("basetype:" + WURCSBasetype.getBasetype( uRes ));
//			System.out.println("getURLString.Basetype:" + WURCSStringUtils.getURLString(WURCSBasetype.getBasetype( uRes )));
			// getBiologicalMonosacccharide
//			t_sbMonosaccharide.append("\twurcs:has_BiologicalMonosacccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/BiologicalMonosacccharide/"
//					+ WURCSStringUtils.getURLString(this.getBiologicalMonosacccharide( uRes )) + "> ;\n");

			// For SC
			String t_strSkeletonCode =  t_oURES.getSkeletonCode();
			t_sbMonosaccharide.append("\t"+PredicateList.HAS_SC.getTriple( t_oExport.getSkeletonCodeURI(t_strSkeletonCode) ));
			t_sbMonosaccharide.append(" ;\n");

			if ( t_oURES.getAnomericPosition() != 0 ) {
				// For anomeric position
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_ANOM_POS.getTripleLiteral( t_oURES.getAnomericPosition() ));
				t_sbMonosaccharide.append(" ;\n");

				// For anomeric symbol
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_ANOM_SYMBOL.getTripleLiteral( t_oURES.getAnomericSymbol() ));
				t_sbMonosaccharide.append(" ;\n");
			}

			// For MODs
			for (MOD mod : t_oURES.getMODs()) {
				String t_strMODURI = t_oExport.getMODURI(mod);
				t_mapURItoMOD.put(t_strMODURI, mod);

				t_sbMonosaccharide.append("\t"+PredicateList.HAS_MOD.getTriple(t_strMODURI));
				t_sbMonosaccharide.append(" ;\n");
			}

			// For basetype
			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
			String t_strBasetypeURI = t_oExport.getBasetypeURI(t_oBasetype);
			t_mapURItoBasetype.put(t_strBasetypeURI, t_oBasetype); // Store basetype
			t_sbMonosaccharide.append("\t"+PredicateList.HAS_BASETYPE.getTriple(t_strBasetypeURI));
			t_sbMonosaccharide.append(" ;\n");

			// For anobase
			UniqueRES t_oAnobase = WURCSMonosaccharideIntegrator.convertAnobase(t_oURES);
			String t_strAnobaseURI = t_oExport.getAnobaseURI(t_oAnobase);
			t_mapURItoAnobase.put(t_strAnobaseURI, t_oAnobase);
			t_sbMonosaccharide.append("\t"+PredicateList.HAS_ANOBASE.getTriple(t_strAnobaseURI));
			t_sbMonosaccharide.append(" .\n\n"); // End of monosaccharide

			// For monosaccharide subsumption
			if ( !t_mapMSSubsums.containsKey(t_strMSURI) )
				t_mapMSSubsums.put(t_strMSURI, new TreeSet<String>());
			t_mapMSSubsums.get(t_strMSURI).add(t_strMSURI);

			UniqueRES t_oSupersum = t_oURES;
			String t_strSupersumURI = t_strMSURI;
			String t_strSupersumURIold = "";
			while( true ) {
				t_strSupersumURIold = t_strSupersumURI;
				t_oSupersum = WURCSMonosaccharideIntegrator.supersumes(t_oSupersum);
				t_strSupersumURI = t_oExport.getMonosaccharideURI(t_oSupersum);
				if ( t_strSupersumURIold.equals(t_strSupersumURI) ) break;

				t_mapURItoMS.put(t_strSupersumURI, t_oSupersum);

				if ( !t_mapMSSubsums.containsKey(t_strSupersumURI) )
					t_mapMSSubsums.put(t_strSupersumURI, new TreeSet<String>());
				t_mapMSSubsums.get(t_strSupersumURI).add(t_strMSURI);
			}

			// For anobase subsumption
			if ( !t_mapAnobaseSubsums.containsKey(t_strAnobaseURI) )
				t_mapAnobaseSubsums.put(t_strAnobaseURI, new TreeSet<String>());
			t_mapAnobaseSubsums.get(t_strAnobaseURI).add(t_strAnobaseURI);

			// Make anobase subsums
			t_oSupersum = t_oAnobase;
			t_strSupersumURI = t_strAnobaseURI;
			t_strSupersumURIold = "";
			while( true ) {
				t_strSupersumURIold = t_strSupersumURI;
				t_oSupersum = WURCSMonosaccharideIntegrator.supersumes(t_oSupersum);
				t_strSupersumURI = t_oExport.getAnobaseURI(t_oSupersum);
				if ( t_strSupersumURIold.equals(t_strSupersumURI) ) break;

				t_mapURItoAnobase.put(t_strSupersumURI, t_oSupersum);

				if ( !t_mapAnobaseSubsums.containsKey(t_strSupersumURI) )
					t_mapAnobaseSubsums.put(t_strSupersumURI, new TreeSet<String>());
				t_mapAnobaseSubsums.get(t_strSupersumURI).add(t_strAnobaseURI);
			}
		}

		// monosaccharide subsume triple
		t_sbMonosaccharide.append("# subsumes\n");
		for ( String t_strMSURI : t_mapURItoMS.keySet() ) {
			t_sbMonosaccharide.append( t_strMSURI+"\n" );
			t_sbMonosaccharide.append("\t"+PredicateList.A_MS.getAPredicate()); // a  wurcs:monosaccharide

			for ( String t_strSubsumURI : t_mapMSSubsums.get(t_strMSURI) ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.SUBSUMES.getTriple(t_strSubsumURI));
			}
			t_sbMonosaccharide.append(" .\n\n");
		}
		t_sbMonosaccharide.append("\n");


		// basetype triple
		t_sbMonosaccharide.append("# basetype\n");
		for ( String t_strBasetypeURI : t_mapURItoBasetype.keySet() ) {
			t_sbMonosaccharide.append( t_strBasetypeURI+"\n" );
			t_sbMonosaccharide.append("\t"+PredicateList.A_BASETYPE.getAPredicate()); // a  wurcs:basetype
			t_sbMonosaccharide.append(" ;\n");

			UniqueRES t_oBasetype = t_mapURItoBasetype.get(t_strBasetypeURI);
			String t_strBasetype = WURCSBasetype.getBasetype(t_oBasetype);
			t_sbMonosaccharide.append("\t"+PredicateList.A_BASETYPE.getTripleLiteral(t_strBasetype));
			t_sbMonosaccharide.append(" .\n");
		}
		t_sbMonosaccharide.append("\n");


		// anobase triple
		t_sbMonosaccharide.append("# anobase\n");
		for ( String t_strAnobaseURI : t_mapURItoAnobase.keySet() ) {
			t_sbMonosaccharide.append( t_strAnobaseURI+"\n" );
			t_sbMonosaccharide.append("\t"+PredicateList.A_ANOBASE.getAPredicate()); // a  wurcs:anobase
			t_sbMonosaccharide.append(" ;\n");

			UniqueRES t_oAnobase = t_mapURItoAnobase.get(t_strAnobaseURI);
			String t_strAnobase = WURCSAnobase.getAnobase(t_oAnobase);
			t_sbMonosaccharide.append("\t"+PredicateList.A_ANOBASE.getTripleLiteral(t_strAnobase));
			for ( String t_strSubsumURI : t_mapAnobaseSubsums.get(t_strAnobaseURI) ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.SUBSUMES.getTriple(t_strSubsumURI));
			}
			t_sbMonosaccharide.append(" .\n\n");
		}
//		t_sbMonosaccharide.append("\n");


		// MOD triple
		TreeMap<String, LIPs> t_mapURItoLIPS = new TreeMap<String, LIPs>();
		if ( !t_mapURItoMOD.isEmpty() )
			t_sbMonosaccharide.append("# MOD\n");
		for (String t_strMODURI : t_mapURItoMOD.keySet()) {
			t_sbMonosaccharide.append(t_strMODURI+"\n");
			t_sbMonosaccharide.append("\t"+PredicateList.A_MOD.getAPredicate() ); // a  wurcs:MOD

			MOD mod = t_mapURItoMOD.get(t_strMODURI);
			// For MAP
			if ( !mod.getMAPCode().isEmpty() ) {
				t_sbMonosaccharide.append(" ;\n");
				String t_strMAP = mod.getMAPCode();
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_MAP.getTripleLiteral(t_strMAP));
			}

			// For LIPs
			for ( LIPs lips : mod.getListOfLIPs() ) {
				String t_strLIPSURI = t_oExport.getLIPSURI(lips);
				t_mapURItoLIPS.put(t_strLIPSURI, lips);

				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_LIPS.getTriple(t_strLIPSURI));
			}
			 t_sbMonosaccharide.append(" .\n\n"); // End of MOD
		}


		// LIPS triple
		TreeMap<String, LIP> t_mapURItoLIP = new TreeMap<String, LIP>();
		if ( !t_mapURItoLIPS.isEmpty() )
			t_sbMonosaccharide.append("# LIPS\n");
		for ( String t_strLIPSURI : t_mapURItoLIPS.keySet() ) {
			t_sbMonosaccharide.append(t_strLIPSURI+"\n");
			t_sbMonosaccharide.append("\t"+PredicateList.A_LIPS.getAPredicate()); // a  wurcs:LIPS

			LIPs lips = t_mapURItoLIPS.get(t_strLIPSURI);
			// For fuzzyness
			t_sbMonosaccharide.append(" ;\n");
			t_sbMonosaccharide.append("\t"+PredicateList.IS_FUZZY.getTripleLiteral(lips.isFuzzy()));

			for ( LIP lip : lips.getLIPs() ) {
				String t_strLIPURI = t_oExport.getLIPURI(lip);
				t_mapURItoLIP.put(t_strLIPURI, lip);

				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_LIP.getTriple(t_strLIPURI));
			}
			 t_sbMonosaccharide.append(" .\n\n"); // End of LIPS
		}


		// LIP triple
		if ( !t_mapURItoLIP.isEmpty() )
			t_sbMonosaccharide.append("# LIP\n");
		for ( String t_strLIPURI : t_mapURItoLIP.keySet() ) {
			t_sbMonosaccharide.append(t_strLIPURI+"\n");
			t_sbMonosaccharide.append("\t"+PredicateList.A_LIP.getAPredicate()); // a  wurcs:LIP

			LIP t_oLIP = t_mapURItoLIP.get(t_strLIPURI);

			// For probabilities
			if ( t_oLIP.getBackboneProbabilityLower() != 1.0 ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_B_PROB_LOW.getTripleLiteral(t_oLIP.getBackboneProbabilityLower()));
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_B_PROB_UP.getTripleLiteral(t_oLIP.getBackboneProbabilityUpper()));
			}
			if ( t_oLIP.getModificationProbabilityLower() != 1.0 ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_M_PROB_LOW.getTripleLiteral(t_oLIP.getModificationProbabilityLower()));
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_M_PROB_UP.getTripleLiteral(t_oLIP.getModificationProbabilityUpper()));
			}
			t_sbMonosaccharide.append(" ;\n");

			// For SC position
			int t_iSCPos = t_oLIP.getBackbonePosition();
			t_sbMonosaccharide.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral(t_iSCPos) );
			// For direction
			if ( t_oLIP.getBackboneDirection() != ' ' ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_DIRECTION.getTripleLiteral( t_oLIP.getBackboneDirection() ));
			}
			// For MAP position (TODO: MAP position -> star index)
			if ( t_oLIP.getModificationPosition() != 0 ) {
				t_sbMonosaccharide.append(" ;\n");
				t_sbMonosaccharide.append("\t"+PredicateList.HAS_STAR_INDEX.getTripleLiteral( t_oLIP.getModificationPosition() ));
			}

			t_sbMonosaccharide.append(" .\n"); // End of LIP
		}

		return t_sbMonosaccharide.toString();
	}


	// TODO:
	public String getWURCSGlycanTripleTTL(String a_strAccessionNumber, WURCSArray a_oWURCS)
	{
		StringBuilder t_sbGlycan= new StringBuilder();

		WURCSExporter export = new WURCSExporter();

		WURCSExporterURIWithAccessionNumber t_oExportURI = new WURCSExporterURIWithAccessionNumber(a_strAccessionNumber);

		// ##
		t_sbGlycan.append("##\n");

		// # WURCS
		String t_strWURCSString = export.getWURCSString(a_oWURCS);
		t_sbGlycan.append("# " + t_strWURCSString + "\n");

		// # AccessionNumber
		t_sbGlycan.append("# " + a_strAccessionNumber + "\n");
		t_sbGlycan.append("\n");

/*
		// Saccharide triple
		t_sbGlycan.append("# Saccharide\n");
		t_sbGlycan.append(t_oExportURI.getSaccharideURI()+"\n");
		t_sbGlycan.append("\t"+PredicateList.A_SACCHARIDE.getAPredicate()); // a glycan:saccharide
		t_sbGlycan.append(" ;\n");

		String t_strGSeq = t_oExportURI.getGlycosequenceURI();
		t_sbGlycan.append("\t"+PredicateList.HAS_GSEQ.getTriple(t_strGSeq));
		t_sbGlycan.append(" .\n\n");


		// Glycosequence triple
		t_sbGlycan.append("# Glycosequence\n");
		t_sbGlycan.append(t_strGSeq+"\n");
		t_sbGlycan.append("\t"+PredicateList.A_GSEQ.getAPredicate()); // a glycan:glycosequence
		t_sbGlycan.append(" ;\n");

		// For uniqueRES count
		t_sbGlycan.append("\t"+PredicateList.NUM_URES.getTripleLiteral(a_oWURCS.getUniqueRESCount()));
		t_sbGlycan.append(" ;\n");
		// For RES count
		t_sbGlycan.append("\t"+PredicateList.NUM_RES.getTripleLiteral(a_oWURCS.getRESCount()));
		t_sbGlycan.append(" ;\n");
		// For LIN count
		t_sbGlycan.append("\t"+PredicateList.NUM_LIN.getTripleLiteral(a_oWURCS.getLINCount()));
		t_sbGlycan.append(" ;\n");

		// For root RES
		t_sbGlycan.append("\t"+PredicateList.HAS_ROOT_RES.getTriple( t_oExportURI.getRESURI("a") ));
		t_sbGlycan.append(" ;\n");

//		for (UniqueRES t_oURES : a_oWURCS.getUniqueRESs()) {
			// For unique RES ID
//			t_sbGlycan.append("\t"+PredicateList.HAS_URES.getTriple( t_oExportURI.getUniqueRESURI(t_oURES) ));
//			t_sbGlycan.append(" ;\n");

			// For monosaccharide of unique RES
//			t_sbGlycan.append("\t"+PredicateList.HAS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oURES) ));
//			t_sbGlycan.append(" ;\n");
			// For basetype of unique RES
//			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
//			t_sbGlycan.append("\t"+PredicateList.HAS_BASETYPE.getTriple( t_oExportURI.getBasetypeURI(t_oBasetype) ));
//			t_sbGlycan.append(" ;\n");
//		}

		for ( LIN t_oLIN :a_oWURCS.getLINs() ) {
			// For LIN
			LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
			for ( RES t_oRES : a_oWURCS.getRESs() ) {
				if ( !t_oLIN.containRES(t_oRES) ) continue;
				t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
			}
			t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_aRESs) ));
			t_sbGlycan.append(" ;\n");
		}

		// For WURCS sequence
		t_sbGlycan.append("\t"+PredicateList.HAS_SEQ.getTripleLiteral( t_strWURCSString ));
		t_sbGlycan.append(" ;\n");

		// For format
		t_sbGlycan.append("\t"+PredicateList.IN_CARB_FORMAT.getTriple( PredicateList.FORMAT_WURCS.getPredicateWithPrefix() ));
		t_sbGlycan.append(" ;\n");

		// Same as
		t_sbGlycan.append("\t"+PredicateList.SAMEAS.getTriple( t_oExportURI.getWURCSURI(a_oWURCS) ));
		t_sbGlycan.append(" .\n\n");


		// UniqueRES triple
//		t_sbGlycan.append("# UniqueRES\n");
//		for ( UniqueRES t_oURES : a_oWURCS.getUniqueRESs() ) {
//			t_sbGlycan.append(t_oExportURI.getUniqueRESURI(t_oURES)+"\n");
//			t_sbGlycan.append("\t"+PredicateList.A_URES.getAPredicate()); // a wurcs:uniqueRES
//			t_sbGlycan.append(" ;\n");
//
//			// For monosaccharide of unique RES
//			t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oURES) ));
//			t_sbGlycan.append(" .\n\n");
//		}
*/

//		// LIN triple
//		LinkedList<GLIPs> t_aGLIPs = new LinkedList<GLIPs>();
//		if ( !a_oWURCS.getLINs().isEmpty() )
//			t_sbGlycan.append("# LIN\n");
//		for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
////			t_sbGlycan.append(t_oExportURI.getLINURI(t_oLIN)+"\n");
//
//// make LIN related to RES
//			LinkedList<UniqueRES> t_aURESs = new LinkedList<UniqueRES>();
//			LinkedList<RES> t_aRESs = new LinkedList<RES>();
//			int resInLin = 0;
//
//			for ( RES t_oRES : a_oWURCS.getRESs() ) {
//				if ( !t_oLIN.containRES(t_oRES) ) continue;
//				resInLin++;
//
//				t_aURESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//				t_aRESs.add(t_oRES);
//			}
////			t_sbGlycan.append(t_oExportURI.getLINURI(t_aRESs)+"\n");
//
//			GLIP glip = t_oLIN.getFirstGLIP();
////			t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( glip.getBackbonePosition() ));
//			t_sbGlycan.append(t_oExportURI.getLINURI(t_aURESs, glip.getBackbonePosition())+"\n");
//
//			t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( glip.getBackbonePosition() ));
//			
//			for ( RES t_oRES : t_aRESs ) {
////				if ( !t_oLIN.getLastGLIP().getRESIndex().equals(t_oRES.getRESIndex())) continue;
//				
//				UniqueRES uniqueRES = a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1);
//				// first case
//
//				boolean scSet = false;		
//				for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
//					LIN t_LIN = iter.next();
//					LinkedList<UniqueRES> t_URESs = new LinkedList<UniqueRES>();
//					
//					if (t_LIN.getLastGLIP().getRESIndex().equals(t_oRES.getRESIndex())) {
//						glip = t_LIN.getFirstGLIP();
//						t_sbGlycan.append(" ;\n");
//
//						t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI(uniqueRES, glip.getBackbonePosition())));
//						t_sbGlycan.append(" .\n");
//						t_sbGlycan.append("## RES \n");
//
//						t_sbGlycan.append(t_oExportURI.getRESURI(uniqueRES, glip.getBackbonePosition())+"\n");
//						t_sbGlycan.append(" ;\n");
//						scSet = true;
//						
//						for ( RES t_RES : a_oWURCS.getRESs() ) {
//							if ( !t_LIN.containRES(t_RES) ) continue;
//							resInLin++;
//
//							t_URESs.add(a_oWURCS.getUniqueRESs().get(t_RES.getUniqueRESID() -1));
//						}//			for ( RES t_oRES : t_aRESs ) {

//						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_URESs, glip.getBackbonePosition())));
//						t_sbGlycan.append(" ;\n");
//						t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI( uniqueRES )));
//					}
//					
//					if (!scSet && t_LIN.getFirstGLIP().getRESIndex().equals(t_oRES.getRESIndex())) {
//						glip = t_LIN.getFirstGLIP();
//						t_sbGlycan.append(" ;\n");
//						t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI(uniqueRES, glip.getBackbonePosition())));
//						t_sbGlycan.append(" .\n");
//						t_sbGlycan.append("## RES \n");
//						t_sbGlycan.append(t_oExportURI.getRESURI(uniqueRES)+"\n");
//						t_sbGlycan.append(" ;\n");
//						for ( RES t_RES : a_oWURCS.getRESs() ) {
//							if ( !t_LIN.containRES(t_RES) ) continue;
//							resInLin++;
//
//							t_URESs.add(a_oWURCS.getUniqueRESs().get(t_RES.getUniqueRESID() -1));
//						}
//						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_URESs, glip.getBackbonePosition())));
//						t_sbGlycan.append(" ;\n");
//						t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI( uniqueRES )));
//					}
//					
//				}
//				
//			}
//			
//			if (resInLin == 1) {
//				t_sbGlycan.append(";\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
//			}
//				
//
//			
//				
//				
//				// For monosaccharide
////				t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oRES) ));
////				t_sbGlycan.append(" .\n\n");
//
//				
//				
////				resInLin is count of this is in each lin, if only 1 lin is attached to this lin, then it is an end.
//				t_sbGlycan.append(" .\n\n");
//			
			
			
// LIN to RES
			
//			t_sbGlycan.append("\t"+PredicateList.A_LIN.getAPredicate()); // a wurcs:LIN
//			GLIP glip = t_oLIN.getFirstGLIP();

			
//			t_sbGlycan.append(" ;\n");
//			t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber

			// For MAP
//			if ( !t_oLIN.getMAPCode().isEmpty() ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_MAP.getTripleLiteral(t_oLIN.getMAPCode()));
//			}

			// For repeating
//			t_sbGlycan.append(" ;\n");
//			t_sbGlycan.append("\t"+PredicateList.IS_REP.getTripleLiteral(t_oLIN.isRepeatingUnit())  );
//			if ( t_oLIN.isRepeatingUnit() ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_REP_MIN.getTripleLiteral(t_oLIN.getMinRepeatCount()) );
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_REP_MAX.getTripleLiteral(t_oLIN.getMaxRepeatCount()) );
//			}
			

			// For GLIPS
//			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {  // TODO: need to process this for RES - so put RES afterwards and follow GLIPS method. 
//				t_aGLIPs.add(t_oGLIPs);
				
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( t_oExportURI.getGLIPSURI(t_oGLIPs) ));
				// GLIPS needs URES IN URI
//				t_aRESs = new LinkedList<UniqueRES>();
//				for ( RES t_oRES : a_oWURCS.getRESs() ) {
//					for (GLIP glip : t_oGLIPs.getGLIPs()) {

//						if ( t_oRES.getRESIndex().equals( glip.getRESIndex() ) ) {
//							t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//						}
//					}
//				}
//				t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( a_strAccessionNumber,  t_oExportURI.getGLIPSURI(t_aRESs)));
//				t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( t_oExportURI.getGLIPSURI(t_aRESs)));
//
//			}
//			t_sbGlycan.append(" .\n\n");
//		}

		// GLIPS triple
//		LinkedList<GLIP> t_aGLIP = new LinkedList<GLIP>();
//		if ( !t_aGLIPs.isEmpty() )
//			t_sbGlycan.append("# GLIPS\n");
//		for ( GLIPs t_oGLIPs : t_aGLIPs ) {
//
//			// GLIPS needs URES IN URI
////			t_sbGlycan.append(t_oExportURI.getGLIPSURI(t_oGLIPs)+"\n");
//			LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
//			for ( RES t_oRES : a_oWURCS.getRESs() ) {
//				for (GLIP glip : t_oGLIPs.getGLIPs()) {
//					if ( t_oRES.getRESIndex().equals( glip.getRESIndex() ) ) {
//						t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//					}
//				}
//			}
//			t_sbGlycan.append(t_oExportURI.getGLIPSURI(t_aRESs)+"\n");
//			
////			t_sbGlycan.append("\t"+PredicateList.A_GLIPS.getAPredicate()); // a wurcs:GLIPS
////			t_sbGlycan.append(" ;\n");
//
//			// Is fuzzy
////			t_sbGlycan.append("\t"+PredicateList.IS_FUZZY.getTripleLiteral( t_oGLIPs.isFuzzy() ));
//
//			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
//				t_aGLIP.add(t_oGLIP);
////				t_sbGlycan.append(" ;\n");
////				t_sbGlycan.append("\t"+PredicateList.HAS_GLIP.getTriple( t_oExportURI.getGLIPURI(t_oGLIP) ));
//				
//				// For RES index
//				
//				// RES needs URES
////				t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI( t_oGLIP.getRESIndex() ) ));
//				for (UniqueRES uniqueRES : t_aRESs) {
////					t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( a_strAccessionNumber, t_oExportURI.getRESURI(uniqueRES)));
//					t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI(uniqueRES)));
//					t_sbGlycan.append(" ;\n");
//				}
//
//				// has LIN  ***
//
//				// LIN triple
////				if ( !a_oWURCS.getLINs().isEmpty() )
////					t_sbGlycan.append("\n# GLIPS to LIN\n");
//				for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
//					// For each LIN, find the matching GLIPS
//					if (t_oLIN.getListOfGLIPs().contains(t_oGLIPs)) {
//						
//						t_aRESs = new LinkedList<UniqueRES>();
//						for ( RES t_oRES : a_oWURCS.getRESs() ) {
//							if ( !t_oLIN.containRES(t_oRES) ) continue;
//							t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//						}
////						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( a_strAccessionNumber, t_oExportURI.getLINURI(t_aRESs) ));
//						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_aRESs) ));
//						t_sbGlycan.append(" ;\n");
////						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_oLIN) ));
//					}
//				}
//				// LIN ***
//
//				// For probabilities
////				if ( t_oGLIP.getBackboneProbabilityLower() != 1.0 ) {
////					t_sbGlycan.append(" ;\n");
////					t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_LOW.getTripleLiteral(t_oGLIP.getBackboneProbabilityLower()));
////					t_sbGlycan.append(" ;\n");
////					t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_UP.getTripleLiteral(t_oGLIP.getBackboneProbabilityUpper()));
////				}
////				if ( t_oGLIP.getModificationProbabilityLower() != 1.0 ) {
////					t_sbGlycan.append(" ;\n");
////					t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_LOW.getTripleLiteral(t_oGLIP.getModificationProbabilityLower()));
////					t_sbGlycan.append(" ;\n");
////					t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_UP.getTripleLiteral(t_oGLIP.getModificationProbabilityUpper()));
////				}
////				t_sbGlycan.append(" ;\n");
//
//				// For SC position
//				t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( t_oGLIP.getBackbonePosition() ));
//				// For direction
////				if ( t_oGLIP.getBackboneDirection() != ' ' ) {
////					t_sbGlycan.append("\t"+PredicateList.HAS_DIRECTION.getTripleLiteral( t_oGLIP.getBackboneDirection() ));
////					t_sbGlycan.append(" ;\n");
////				}
////				// For MAP position (TODO: MAP position -> star index)
////				if ( t_oGLIP.getModificationPosition() != 0 ) {
////					t_sbGlycan.append(" ;\n");
////					t_sbGlycan.append("\t"+PredicateList.HAS_STAR_INDEX.getTripleLiteral( t_oGLIP.getModificationPosition() ));
////				}
////				t_sbGlycan.append(" .\n\n");
//
//				
//			}
//			t_sbGlycan.append(" .\n\n");
//		}

		TreeMap<String, RES> residues = new TreeMap<String, RES>();
		// set the lin for each res, res for each lin, and primary
		LinkedList<RES> t_UniqueRESs = new LinkedList<RES>();
		for ( RES t_oRES : a_oWURCS.getRESs() ) {
			// LINs = 1 means it's a root or leaf
			int resInLin = 0; boolean fuzzy = false;
			LinkedList<LIN> t_aLINs = new LinkedList<LIN>();
			GLIP glip = null;

			for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
				LIN t_oLIN = iter.next();

				if (!t_oLIN.containRES(t_oRES) ) continue;
				resInLin++;
				// if this res is not the parent, ignore.
				if (!t_oLIN.isNextLin(t_oRES)) continue;
				if (t_oLIN.getListOfGLIPs().getLast().isFuzzy()) { fuzzy  = true; continue;}
				for ( RES t_RES : a_oWURCS.getRESs() ) {
					// ignore if no relation
//						if (!t_oLIN.containRES(t_RES)) continue;
					
					if (t_oLIN.isPreviousLin(t_RES)) {
//						t_oRES.setScPosition("");
						
						glip = t_oLIN.getFirstGLIP();
						if (t_oLIN.getListOfGLIPs().getFirst().isFuzzy()) {
							glip = t_oLIN.getLastGLIP();
							
						}
						
						if (glip.getBackbonePosition() < 0) {
							t_RES.setScPosition("rep");
							t_oLIN.setScPosition("rep");
						} else { 
							t_RES.setScPosition(glip.getBackbonePosition()+"");
							t_oLIN.setScPosition(glip.getBackbonePosition()+"");
						}
						t_oLIN.setChildRES(t_RES);
						t_oLIN.setParentRES(t_oRES);
						t_aLINs.add(t_oLIN);
					}
				}
			}
			if (resInLin == 1 || t_oRES.getRESIndex().equals("a")) {
				t_oRES.setPrimaryId(a_strAccessionNumber);
			}
			
			t_oRES.setLINs(t_aLINs);
			System.out.println("putting res:" + t_oRES + "\n");
			residues.put(t_oRES.getRESIndex(), t_oRES);
		}
		
		for ( RES childREScheck : a_oWURCS.getRESs() ) {
			int resInLin = 0; 
			for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
				LIN t_oLIN = iter.next();
				if (!t_oLIN.containRES(childREScheck) ) continue;
				resInLin++;
				// if this lin is not the next one, continue
				if (!t_oLIN.isNextLin(childREScheck)) continue;

				if (t_oLIN.getListOfGLIPs().getLast().isFuzzy()) {
					// for fuzzy ones its reversed - current is child
					LinkedList<LIN> t_fLINs = new LinkedList<LIN>();
					
					// find the parent
					// from the GLIP in the LIN, using the RES Index.
					// get the RES from the hash. 
					
					//return this.m_aGLIPs.getLast().getGLIPs().getLast();
					System.out.println("t_oLIN of fuzzy:" + t_oLIN + "\n");
					System.out.println("RES of fuzzy:" + childREScheck + "\n");

					// want the parent residue, and overwrite the data with proper info.
					// e1-d2|d4
					
					// so it should be e-2-d2 and e-4-d4
					// with d2 and d4 as ending nodes.
					GLIP fglip = t_oLIN.getListOfGLIPs().getLast().getGLIPs().getFirst();
					System.out.println("fglip of fuzzy:" + fglip + "\n");
				
					// assuming we are on e, retrieve e and rewrite.
					RES parentRES = residues.get(fglip.getRESIndex());
					System.out.println("checking parent:" + parentRES + "<");
					
					
					childREScheck.setScPosition(fglip.getBackbonePosition()+ "");
					t_oLIN.setParentRES(parentRES);
					t_oLIN.setScPosition(fglip.getBackbonePosition()+"");
					// if the (fuzzy, so reversed) parent was a leaf, make the new child a proper leaf.
//					childREScheck.setPrimaryId(parentRES.getPrimaryId());
					t_oLIN.setChildRES(childREScheck);
					t_fLINs.add(t_oLIN);
					
					fglip = t_oLIN.getListOfGLIPs().getLast().getGLIPs().getLast();
					RES parentRES2 = residues.get(fglip.getRESIndex());
					System.out.println("checking parent:" + parentRES2 + "<");
					System.out.println("fglip2 of fuzzy:" + fglip + "position:>" + fglip.getBackbonePosition() + "\n");
					System.out.println("putting:" + childREScheck.getRESIndex()+childREScheck.getScPosition() + ":>" + childREScheck + "\n");

					LIN lin = new LIN("");
					RES newres = new RES(childREScheck.getUniqueRESID(), childREScheck.getRESIndex());
					
					newres.setScPosition(fglip.getBackbonePosition()+"");
					lin.setParentRES(parentRES);
					lin.setScPosition(fglip.getBackbonePosition()+"");

					newres.setPrimaryId(childREScheck.getPrimaryId());
					lin.setChildRES(newres);
					// put in a new node for the other ambiguous position.
					System.out.println("putting:" + newres.getRESIndex()+newres.getScPosition() + ":>" + newres + "\n");
					residues.put(newres.getRESIndex()+newres.getScPosition(), newres);
					t_fLINs.add(lin);

					if (null != parentRES ) {
						parentRES.setLINs(t_fLINs);
						residues.put(parentRES.getRESIndex(), parentRES);
					};
				}
			}
		}
		System.out.println("\n\nprocessing:\n\n");
		for (String key : residues.keySet()) {
			// lin by lin
			RES res = residues.get(key);
			UniqueRES ures = a_oWURCS.getUniqueRESs().get(res.getUniqueRESID() -1);

			t_sbGlycan.append(t_oExportURI.getRESURI(ures, res.getRESIndex()+res.getScPosition()) + "\n");
			t_sbGlycan.append("\t"+PredicateList.SUBSUMES.getTriple(t_oExportURI.getRESURI(ures)));
			
			System.out.println(key + "#RES:>" + res + "\n");

			if (null != res.getPrimaryId()) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(res.getPrimaryId())); // has_primary_id a_strAccessionNumber
			}

			for (Iterator<LIN> iter = res.getLINs().iterator(); iter.hasNext();) {
				t_sbGlycan.append(" ;\n");
				LIN t_oLIN = iter.next();
				RES childRES = t_oLIN.getChildRES();
				UniqueRES childURES = a_oWURCS.getUniqueRESs().get(childRES.getUniqueRESID() -1);
				
				if (t_oLIN.getScPosition().equals("-1")) {
					t_sbGlycan.append("\twurcs:rep " + t_oExportURI.getRESURI(childURES, childRES.getRESIndex()+childRES.getScPosition()) + "");
				} else {
					t_sbGlycan.append("\twurcs:" +t_oLIN.getScPosition()+" " + t_oExportURI.getRESURI(childURES, childRES.getRESIndex()+childRES.getScPosition()) + "");
				}
					
					
			}
			t_sbGlycan.append(" .\n");
		}
		
		

//		for ( RES t_oRES : a_oWURCS.getRESs() ) {
////			LinkedList<LIN> t_aLINs = new LinkedList<LIN>();
////			LinkedList<GLIP> t_aGLIPs = new LinkedList<GLIP>();
////			LinkedList<String> t_aRESCHECKs = new LinkedList<String>();
//			UniqueRES ures = a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1);
//			// LINs = 1 means it's a root or leaf
//						int resInLin = 0;
//			// go through each LIN to get lin data.
//			boolean fuzzy = false;
//			GLIP glip = null;
//			for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
//				LIN t_oLIN = iter.next();
//				
//				if (!t_oLIN.containRES(t_oRES) ) continue;
//				// count number of res in lin
//				resInLin++;
//				// normal logic
//				// having the unique res is necessary.
//
//
//				//return getFirstGLIP().getRESIndex().equals(a_oRES.getRESIndex());
//				if (t_oLIN.isNextLin(t_oRES)) {
//					// check fuzziness. (at e of e1-d2|d4)
//					// e already created above.
//					// e could have other linkages - process as normal
//					// create d2-e and d4-e
//					
//					// at d
//					// if next is fuzzy, ignore.
//					
//					// find the next RES of this lin
//					
//					// creates e1-d which is wrong.
//					if (t_oLIN.getListOfGLIPs().getLast().isFuzzy()) { fuzzy  = true; continue;}
//					for ( RES t_RES : a_oWURCS.getRESs() ) {
//						// ignore if no relation
//	//						if (!t_oLIN.containRES(t_RES)) continue;
//						
//						if (t_oLIN.isPreviousLin(t_RES)) {
//							UniqueRES tres = a_oWURCS.getUniqueRESs().get(t_RES.getUniqueRESID() -1);
//	
//							glip = t_oLIN.getFirstGLIP();
//							t_sbGlycan.append(t_oExportURI.getRESURI(ures, t_oRES.getRESIndex()) + "\n");
//							t_sbGlycan.append("\twurcs:" +glip.getBackbonePosition()+" " + t_oExportURI.getRESURI(tres, t_RES.getRESIndex()+glip.getBackbonePosition()) + "");
//							t_sbGlycan.append(".\n");
//						}
//					}
//				}
//			}
//			if (glip == null && !fuzzy) {
//				for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
//					LIN t_oLIN = iter.next();
//					if (!t_oLIN.containRES(t_oRES) ) continue;
//					// count number of res in lin
//					resInLin++;
//					// normal logic
//					// having the unique res is necessary.
//
//
//					//return getFirstGLIP().getRESIndex().equals(a_oRES.getRESIndex());
//					if (t_oLIN.isPreviousLin(t_oRES)) {//				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber

//						glip = t_oLIN.getFirstGLIP();
//					}
//				}
//				t_sbGlycan.append(t_oExportURI.getRESURI(ures, t_oRES.getRESIndex()+glip.getBackbonePosition()) + "\n");
//				t_sbGlycan.append("\t"+PredicateList.SUBSUMES.getTriple(t_oExportURI.getRESURI(ures)) );		
//			}
//
//			if (resInLin == 1 && !fuzzy) {
//				t_sbGlycan.append(";\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
//			}
//			t_sbGlycan.append(".\n");
//			
//			// process fuzzy e1-d2|d4
//					}
//		
//		// e1-d2|d4)
//		t_sbGlycan.append("\n\n");
//		
//		for ( RES t_fRES : a_oWURCS.getRESs() ) {
//			// for each res
//			// check if only one lin
//			// check if fuzzy
//			// if fuzzy, get previous
//		
//			// if one lin add id
//			// LINs = 1 means it's a root or leaf
//			int resInLin = 0;
//
//			
//			for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
//				LIN t_fLIN = iter.next();
//				
//				if (!t_fLIN.containRES(t_fRES) ) continue;
//				// count number of res in lin
//				resInLin++;
//				GLIP childglip = t_fLIN.getFirstGLIP();
//				if (t_fLIN.isNextLin(t_fRES)) continue;
//				
//				// only want the e first.
//				if (t_fLIN.getListOfGLIPs().getLast().isFuzzy()) {
//					t_sbGlycan.append("## FUZZ\n");
//					System.out.println("FUZZY!>" + t_fLIN.getLastGLIP().getBackbonePosition());
//					UniqueRES parentUres = a_oWURCS.getUniqueRESs().get(t_fRES.getUniqueRESID() -1);
//					
//					// create d-e
//					// find d of this lin.
//					for ( RES t_RES : a_oWURCS.getRESs() ) {
//						if (t_fLIN.isNextLin(t_RES)) {
//							UniqueRES childUres = a_oWURCS.getUniqueRESs().get(t_RES.getUniqueRESID() -1);
//		
//							//return this.m_aGLIPs.getLast().getGLIPs().getLast();
//							GLIP glip = t_fLIN.getLastGLIP();
//							t_sbGlycan.append(t_oExportURI.getRESURI(parentUres, t_fRES.getRESIndex()+childglip.getBackboneDirection()) + "\n");
//							t_sbGlycan.append("\twurcs:" +glip.getBackbonePosition()+" " + t_oExportURI.getRESURI(childUres, t_RES.getRESIndex()+glip.getBackbonePosition()) + ".\n");
//							//return this.m_aGLIPs.getLast().getGLIPs().getLast();
//							glip = t_fLIN.getListOfGLIPs().getLast().getGLIPs().getFirst();
//							t_sbGlycan.append(t_oExportURI.getRESURI(parentUres, t_fRES.getRESIndex()+childglip.getBackboneDirection()) + "\n");
//							t_sbGlycan.append("\twurcs:" +glip.getBackbonePosition()+" " + t_oExportURI.getRESURI(childUres, t_RES.getRESIndex()+glip.getBackbonePosition()) + ".\n");
//						}
//					}
//				}
//			}
//		}
		
//			
//
//		// RES triple
//		if ( !a_oWURCS.getRESs().isEmpty() )
//			t_sbGlycan.append("# RES\n");
//		LinkedList<RES> t_UniqueRESs = new LinkedList<RES>();
//		boolean rootdone = false;		
//		boolean firstglip = false;
//		for ( RES t_oRES : a_oWURCS.getRESs() ) {
//			UniqueRES ures = a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1);
//			
//			// first case
//			int resInLin = 0;
//			LinkedList<LIN> t_aLINs = new LinkedList<LIN>();
//			LinkedList<GLIP> t_aGLIPs = new LinkedList<GLIP>();
//			LinkedList<String> t_aRESCHECKs = new LinkedList<String>();
//			
//			// go through each LIN to get lin data.
//			for (Iterator<LIN> iter = a_oWURCS.getLINs().iterator(); iter.hasNext();) {
//				LIN t_oLIN = iter.next();
//				
//				// first if its not there, ignore. 
//				if (!t_oLIN.containRES(t_oRES) ) continue;
//
//				// count number of res in lin
//				resInLin++;
//
//				GLIP glip = t_oLIN.getFirstGLIP();
//				
//				if (t_oLIN.getListOfGLIPs().getLast().isFuzzy()) {
//					// if fuzzy, have to run this RES twice.
//					// one for each position.
//					glip = t_oLIN.getListOfGLIPs().getLast().getGLIPs().getFirst();
//
//					// check each res to get the sc position (d+3)
//					if (t_aRESCHECKs.contains(t_oRES.getRESIndex() +  glip.getBackbonePosition())) continue;
//					
//					if (t_oLIN.getLastGLIP().getRESIndex().equals(t_oRES.getRESIndex())) {
//						t_sbGlycan.append(t_oExportURI.getRESURI(ures, glip.getBackbonePosition())+"\n");
//						t_aRESCHECKs.add(t_oRES.getRESIndex() +  glip.getBackbonePosition());
//					}
//					
//					// For HAS_LIN of this RES
//					LIN lin = null;
//					boolean haslin = false;
//					
//					for (Iterator iterator = a_oWURCS.getLINs().iterator(); iterator.hasNext();) {
//						lin = (LIN) iterator.next();
//						LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
//						if ( !lin.containRES(t_oRES) ) continue;
//						t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//
//						if (lin.getListOfGLIPs().getLast().isFuzzy()) {
//							if (firstglip) {
//								glip = lin.getListOfGLIPs().getLast().getGLIPs().getLast();
//								firstglip = false;
//							} else {
//								glip = lin.getListOfGLIPs().getLast().getGLIPs().getFirst();
//								firstglip = true;
//							}
//						}
////									t_sbGlycan.append(";\n");
////									t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( glip.getBackbonePosition() ));
////									t_sbGlycan.append(t_oExportURI.getLINURI(t_aRESs, glip.getBackbonePosition())+"\n");
//
//						t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_aRESs, glip.getBackbonePosition())));
//						t_oRES.setScPosition(glip.getBackbonePosition()+"");
//
//						t_sbGlycan.append(" ;\n");
//						haslin = true;
//					}
//
//					
////					t_sbGlycan.append("\t"+PredicateList.A_RES.getAPredicate()); // a wurcs:RES
//					
////					if (t_oRES.getRESIndex().equals("a")) {
////						t_sbGlycan.append(" ;\n");
////						t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTriple(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
////					}
//
//					// For uniqueRES of this RES
////					t_sbGlycan.append("\t"+PredicateList.IS_URES.getTriple( t_oExportURI.getUniqueRESURI( t_oRES.getUniqueRESID() ) ));
//					t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI( a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID()-1)) ));
//					
//					// For monosaccharide
////					t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oRES) ));
////					t_sbGlycan.append(" .\n\n");
//
//					
//					
////					resInLin is count of this is in each lin, if only 1 lin is attached to this lin, then it is an end.
//					if (!haslin && resInLin == 1) {
//						t_sbGlycan.append(";\n");
//						t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
//					}
//						
//
//					t_sbGlycan.append(" .\n\n");					
//				}
//
//				if (t_aRESCHECKs.contains(t_oRES.getRESIndex() +  glip.getBackbonePosition())) continue;
//				t_aLINs.add(t_oLIN);
//
//				// normal lin, normal res, not first.
//				if (t_oLIN.isPreviousLin(t_oRES)) {
//					t_sbGlycan.append(t_oExportURI.getRESURI(ures, glip.getBackbonePosition())+"\n");
//					t_aRESCHECKs.add(t_oRES.getRESIndex() +  glip.getBackbonePosition());
//					t_oRES.setScPosition(glip.getBackbonePosition()+"");
//				}
//				
//				// first normal res.
//				if (!rootdone && t_oLIN.isNextLin(t_oRES)) {
//					t_oRES.setScPosition("root");
//					t_sbGlycan.append(t_oExportURI.getRESURI(ures)+"\n");
//					rootdone = true;
//				}
////				if (t_oLIN.getFirstGLIP().getRESIndex().equals(t_oRES.getRESIndex())) continue;
//				
////				t_sbGlycan.append(+"\n");
//			}
//			
//			// For LIN contained this RES
//			LIN lin = null;
////			boolean haslin = false;
//			
//			
//			for (Iterator iterator = t_aLINs.iterator(); iterator.hasNext();) {
//				lin = (LIN) iterator.next();
////				if (!lin.isNextLin(t_oRES)) continue;
//				LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
//				for ( RES t_oRESb : a_oWURCS.getRESs() ) {
//					if ( !lin.containRES(t_oRESb) ) continue;
//					t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRESb.getUniqueRESID() -1));
//				}
//				
//				
//				GLIP glip = lin.getFirstGLIP();
//				GLIP first = null;	
//
//				if (lin.getListOfGLIPs().getLast().isFuzzy()) {
//					if (firstglip) {
//						glip = lin.getListOfGLIPs().getLast().getGLIPs().getLast();
//						firstglip = false;
//					} else {
//						glip = lin.getListOfGLIPs().getLast().getGLIPs().getFirst();
//						firstglip = true;
//					}
//				}
////							t_sbGlycan.append(";\n");
////							t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( glip.getBackbonePosition() ));
////							t_sbGlycan.append(t_oExportURI.getLINURI(t_aRESs, glip.getBackbonePosition())+"\n");
//
//				t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_aRESs, glip.getBackbonePosition())));
//				t_sbGlycan.append(" ;\n");
////				haslin = true;
//			}
//
//			
////			t_sbGlycan.append("\t"+PredicateList.A_RES.getAPredicate()); // a wurcs:RES
//			
////			if (t_oRES.getRESIndex().equals("a")) {
////				t_sbGlycan.append(" ;\n");
////				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTriple(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
////			}
//
//			// For uniqueRES of this RES
////			t_sbGlycan.append("\t"+PredicateList.IS_URES.getTriple( t_oExportURI.getUniqueRESURI( t_oRES.getUniqueRESID() ) ));
//			t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI( a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID()-1)) ));
//			
//			// For monosaccharide
////			t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oRES) ));
////			t_sbGlycan.append(" .\n\n");
//
//			
//			
////			resInLin is count of this is in each lin, if only 1 lin is attached to this lin, then it is an end.
//			if (resInLin == 1) {
//				t_sbGlycan.append(";\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_PRIMARY_ID.getTripleLiteral(a_strAccessionNumber)); // has_primary_id a_strAccessionNumber
//			}
//				
//
//			t_sbGlycan.append(" .\n\n");
//
//			// process the other LIN, not the parent one.
//
//			lin = null;
//			for (Iterator iterator = t_aLINs.iterator(); iterator.hasNext();) {
//				lin = (LIN) iterator.next();
//				if (!lin.isNextLin(t_oRES)) continue;
//				LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
//				LinkedList<RES> t_RESs = new LinkedList<RES>();
//				
//				for ( RES t_oRESb : a_oWURCS.getRESs() ) {
//					if ( !lin.containRES(t_oRESb) ) continue;
//					t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRESb.getUniqueRESID() -1));
//
//					if (lin.getLastGLIP().getRESIndex().equals(t_oRESb.getRESIndex())) {
//						UniqueRES t_URES = a_oWURCS.getUniqueRESs().get(t_oRESb.getUniqueRESID() -1);
//						t_RESs.add(t_oRESb);
//					}
//				}
//				GLIP glip = lin.getFirstGLIP();
//	
//				if (lin.getListOfGLIPs().getLast().isFuzzy()) {
//					glip = lin.getListOfGLIPs().getLast().getGLIPs().getLast();
//				}
//
////				t_sbGlycan.append(";\n");
//
//				t_sbGlycan.append(t_oExportURI.getLINURI(t_aRESs, glip.getBackbonePosition())+"\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( glip.getBackbonePosition() ));
//				LinkedList<String> t_aRESCHECKs2 = new LinkedList<String>();
//
//				for (RES t_RES : t_RESs ) {
//
//					t_sbGlycan.append(" ;\n");
//					
//						UniqueRES t_URES = a_oWURCS.getUniqueRESs().get(t_RES.getUniqueRESID() -1);
//						glip = lin.getFirstGLIP();
//						if (lin.getListOfGLIPs().getLast().isFuzzy()) {
//							
//							if (firstglip) {
//								glip = lin.getListOfGLIPs().getLast().getGLIPs().getLast();
//								firstglip = false;
//							} else {
//								glip = lin.getListOfGLIPs().getLast().getGLIPs().getFirst();
//								firstglip = true;
//							}
//
//						}
//							t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI(t_URES, glip.getBackbonePosition())));
//							t_aRESCHECKs2.add(t_RES.getRESIndex()+ glip.getBackbonePosition());
////							scSet = true;
//						
////						if (!t_aRESCHECKs2.contains(ures) && t_oLIN.getFirstGLIP().getRESIndex().equals(t_oRES.getRESIndex())) {
////							glip = t_oLIN.getFirstGLIP();
////							t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI(t_URES, glip.getBackbonePosition())));
////							t_aRESCHECKs2.add(ures);
////						}
//					
//					
//					
//				}				
//				
//				t_sbGlycan.append(" .\n");
//
//			}
//			
//			
//			// has GLIPS
//			
////			for ( GLIPs t_oGLIPs : t_aGLIPs ) {
////				for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
//////					t_sbGlycan.append("\t"+PredicateList.HAS_GLIP.getTriple( t_oExportURI.getGLIPURI(t_oGLIP) ));
////					
////					if (t_oGLIP.getRESIndex().equals(t_oRES.getRESIndex())) {
////						t_sbGlycan.append(" ;\n");
////
////						// GLIPS needs URES IN URI
////						LinkedList<UniqueRES> t_aRESs = new LinkedList<UniqueRES>();
////						t_aRESs.add(a_oWURCS.getUniqueRESs().get(t_oRES.getUniqueRESID() -1));
//////						t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( a_strAccessionNumber,t_oExportURI.getGLIPSURI(t_aRESs)+"\n"));
////						t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( t_oExportURI.getGLIPSURI(t_aRESs)+"\n"));
////
////						
//////						t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( t_oExportURI.getGLIPSURI(t_oGLIPs) ));
////					}
////				}
////			}
//
//			
//		}



		// GLIP triple
//		if ( !t_aGLIP.isEmpty() )
//			t_sbGlycan.append("# GLIP\n");
//		for ( GLIP t_oGLIP : t_aGLIP ) {
//			t_sbGlycan.append(t_oExportURI.getGLIPURI(t_oGLIP)+"\n");
//			t_sbGlycan.append("\t"+PredicateList.A_GLIP.getAPredicate()); // a wurcs:GLIP
//			t_sbGlycan.append(" ;\n");
//
//			// For RES index
//			t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI( t_oGLIP.getRESIndex() ) ));
//
//			// For probabilities
//			if ( t_oGLIP.getBackboneProbabilityLower() != 1.0 ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_LOW.getTripleLiteral(t_oGLIP.getBackboneProbabilityLower()));
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_UP.getTripleLiteral(t_oGLIP.getBackboneProbabilityUpper()));
//			}
//			if ( t_oGLIP.getModificationProbabilityLower() != 1.0 ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_LOW.getTripleLiteral(t_oGLIP.getModificationProbabilityLower()));
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_UP.getTripleLiteral(t_oGLIP.getModificationProbabilityUpper()));
//			}
//			t_sbGlycan.append(" ;\n");
//
//			// For SC position
//			t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( t_oGLIP.getBackbonePosition() ));
//			// For direction
//			if ( t_oGLIP.getBackboneDirection() != ' ' ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_DIRECTION.getTripleLiteral( t_oGLIP.getBackboneDirection() ));
//			}
//			// For MAP position (TODO: MAP position -> star index)
//			if ( t_oGLIP.getModificationPosition() != 0 ) {
//				t_sbGlycan.append(" ;\n");
//				t_sbGlycan.append("\t"+PredicateList.HAS_STAR_INDEX.getTripleLiteral( t_oGLIP.getModificationPosition() ));
//			}
//			t_sbGlycan.append(" .\n\n");
//		}

		return t_sbGlycan.toString();
	}

}
