package org.glycoinfo.WURCSFramework.wurcsRDF;

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

		TreeMap<String, UniqueRES> t_mapURItoBasetype   = new TreeMap<String, UniqueRES>();
		TreeMap<String, UniqueRES> t_mapURItoAnobase    = new TreeMap<String, UniqueRES>();
		TreeMap<String, TreeSet<String>> t_mapAnobaseSubsums = new TreeMap<String, TreeSet<String>>();
//		TreeMap<String, String> t_mapBasetypeURItoAnobaseURI = new TreeMap<String, String>();

		TreeMap<String, MOD> t_mapURItoMOD = new TreeMap<String, MOD>();

		// Monosaccharide triple
		t_sbMonosaccharide.append("# monosaccharide\n");
		for (UniqueRES t_oURES : a_aUniqueRESs) {

			t_sbMonosaccharide.append(t_oExport.getMonosaccharideURI(t_oURES)+"\n");
			t_sbMonosaccharide.append("\t"+PredicateList.A_MS.getAPredicate()); // a  wurcs:monosaccharide
			t_sbMonosaccharide.append(" ;\n");

			// RING information for pyranose, furanose
			for (MOD mod : t_oURES.getMODs()) {
				if ( mod.getListOfLIPs().size() != 2 ) continue;
				boolean isAnomRing = false;
				for (LIPs lips : mod.getListOfLIPs() ) {
					if ( lips.getLIPs().size() != 1 ) continue;
					if ( lips.getLIPs().getFirst().getBackbonePosition() != 1 ) continue;
					isAnomRing = true;
					break;
				}
				if ( !isAnomRing ) continue;

				t_sbMonosaccharide.append("\t"+PredicateList.HAS_RING.getTriple( t_oExport.getMODURI(mod) ));
				t_sbMonosaccharide.append(" ;\n");
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

//			t_mapBasetypeURItoAnobaseURI.put(t_strBasetypeURI, t_strAnobaseURI);
			// Subsums itself
			if ( !t_mapAnobaseSubsums.containsKey(t_strAnobaseURI) )
				t_mapAnobaseSubsums.put(t_strAnobaseURI, new TreeSet<String>());
			t_mapAnobaseSubsums.get(t_strAnobaseURI).add(t_strAnobaseURI);

			// Make anobase subsums
			UniqueRES t_oSupersum = t_oAnobase;
			String t_strSupersumURI = t_strAnobaseURI;
			String t_strSupersumURIold = "";
			while( true ) {
				t_strSupersumURIold = t_strSupersumURI;
				t_oSupersum = WURCSMonosaccharideIntegrator.convertSupersumAnobase(t_oSupersum);
				t_strSupersumURI = t_oExport.getAnobaseURI(t_oSupersum);
				if ( t_strSupersumURIold.equals(t_strSupersumURI) ) break;

				t_mapURItoAnobase.put(t_strSupersumURI, t_oSupersum);

				if ( !t_mapAnobaseSubsums.containsKey(t_strSupersumURI) )
					t_mapAnobaseSubsums.put(t_strSupersumURI, new TreeSet<String>());
				t_mapAnobaseSubsums.get(t_strSupersumURI).add(t_strAnobaseURI);
			}
		}


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

			t_sbMonosaccharide.append(" .\n\n"); // End of LIP
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

		for (UniqueRES t_oURES : a_oWURCS.getUniqueRESs()) {
			// For unique RES ID
			t_sbGlycan.append("\t"+PredicateList.HAS_URES.getTriple( t_oExportURI.getUniqueRESURI(t_oURES) ));
			t_sbGlycan.append(" ;\n");

			// For monosaccharide of unique RES
			t_sbGlycan.append("\t"+PredicateList.HAS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oURES) ));
			t_sbGlycan.append(" ;\n");
			// For basetype of unique RES
			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
			t_sbGlycan.append("\t"+PredicateList.HAS_BASETYPE.getTriple( t_oExportURI.getBasetypeURI(t_oBasetype) ));
			t_sbGlycan.append(" ;\n");
		}

		for ( LIN t_oLIN :a_oWURCS.getLINs() ) {
			// For LIN
			t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_oLIN) ));
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
		t_sbGlycan.append("# UniqueRES\n");
		for ( UniqueRES t_oURES : a_oWURCS.getUniqueRESs() ) {
			t_sbGlycan.append(t_oExportURI.getUniqueRESURI(t_oURES)+"\n");
			t_sbGlycan.append("\t"+PredicateList.A_URES.getAPredicate()); // a wurcs:uniqueRES
			t_sbGlycan.append(" ;\n");

			// For monosaccharide of unique RES
			t_sbGlycan.append("\t"+PredicateList.IS_MS.getTriple( t_oExportURI.getMonosaccharideURI(t_oURES) ));
			t_sbGlycan.append(" .\n\n");
		}


		// RES triple
		if ( !a_oWURCS.getRESs().isEmpty() )
			t_sbGlycan.append("# RES\n");
		for ( RES t_oRES : a_oWURCS.getRESs() ) {
			t_sbGlycan.append(t_oExportURI.getRESURI(t_oRES)+"\n");
			t_sbGlycan.append("\t"+PredicateList.A_RES.getAPredicate()); // a wurcs:RES
			t_sbGlycan.append(" ;\n");

			// For uniqueRES of this RES
			t_sbGlycan.append("\t"+PredicateList.IS_URES.getTriple( t_oExportURI.getUniqueRESURI( t_oRES.getUniqueRESID() ) ));

			// For LIN contained this RES
			for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
				if ( !t_oLIN.containRES(t_oRES) ) continue;
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_LIN.getTriple( t_oExportURI.getLINURI(t_oLIN) ));
			}

			t_sbGlycan.append(" .\n\n");
		}


		// LIN triple
		LinkedList<GLIPs> t_aGLIPs = new LinkedList<GLIPs>();
		if ( !a_oWURCS.getLINs().isEmpty() )
			t_sbGlycan.append("# LIN\n");
		for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
			t_sbGlycan.append(t_oExportURI.getLINURI(t_oLIN)+"\n");
			t_sbGlycan.append("\t"+PredicateList.A_LIN.getAPredicate()); // a wurcs:LIN

			// For MAP
			if ( !t_oLIN.getMAPCode().isEmpty() ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_MAP.getTripleLiteral(t_oLIN.getMAPCode()));
			}

			// For repeating
			t_sbGlycan.append(" ;\n");
			t_sbGlycan.append("\t"+PredicateList.IS_REP.getTripleLiteral(t_oLIN.isRepeatingUnit())  );
			if ( t_oLIN.isRepeatingUnit() ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_REP_MIN.getTripleLiteral(t_oLIN.getMinRepeatCount()) );
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_REP_MAX.getTripleLiteral(t_oLIN.getMaxRepeatCount()) );
			}

			// For GLIPS
			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {
				t_aGLIPs.add(t_oGLIPs);
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_GLIPS.getTriple( t_oExportURI.getGLIPSURI(t_oGLIPs) ));
			}
			t_sbGlycan.append(" .\n\n");
		}


		// GLIPS triple
		LinkedList<GLIP> t_aGLIP = new LinkedList<GLIP>();
		if ( !t_aGLIPs.isEmpty() )
			t_sbGlycan.append("# GLIPS\n");
		for ( GLIPs t_oGLIPs : t_aGLIPs ) {
			t_sbGlycan.append(t_oExportURI.getGLIPSURI(t_oGLIPs)+"\n");
			t_sbGlycan.append("\t"+PredicateList.A_GLIPS.getAPredicate()); // a wurcs:GLIPS
			t_sbGlycan.append(" ;\n");

			// Is fuzzy
			t_sbGlycan.append("\t"+PredicateList.IS_FUZZY.getTripleLiteral( t_oGLIPs.isFuzzy() ));

			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				t_aGLIP.add(t_oGLIP);
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_GLIP.getTriple( t_oExportURI.getGLIPURI(t_oGLIP) ));
			}
			t_sbGlycan.append(" .\n\n");
		}


		// GLIP triple
		if ( !t_aGLIP.isEmpty() )
			t_sbGlycan.append("# GLIP\n");
		for ( GLIP t_oGLIP : t_aGLIP ) {
			t_sbGlycan.append(t_oExportURI.getGLIPURI(t_oGLIP)+"\n");
			t_sbGlycan.append("\t"+PredicateList.A_GLIP.getAPredicate()); // a wurcs:GLIP
			t_sbGlycan.append(" ;\n");

			// For RES index
			t_sbGlycan.append("\t"+PredicateList.HAS_RES.getTriple( t_oExportURI.getRESURI( t_oGLIP.getRESIndex() ) ));

			// For probabilities
			if ( t_oGLIP.getBackboneProbabilityLower() != 1.0 ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_LOW.getTripleLiteral(t_oGLIP.getBackboneProbabilityLower()));
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_B_PROB_UP.getTripleLiteral(t_oGLIP.getBackboneProbabilityUpper()));
			}
			if ( t_oGLIP.getModificationProbabilityLower() != 1.0 ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_LOW.getTripleLiteral(t_oGLIP.getModificationProbabilityLower()));
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_M_PROB_UP.getTripleLiteral(t_oGLIP.getModificationProbabilityUpper()));
			}
			t_sbGlycan.append(" ;\n");

			// For SC position
			t_sbGlycan.append("\t"+PredicateList.HAS_SC_POS.getTripleLiteral( t_oGLIP.getBackbonePosition() ));
			// For direction
			if ( t_oGLIP.getBackboneDirection() != ' ' ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_DIRECTION.getTripleLiteral( t_oGLIP.getBackboneDirection() ));
			}
			// For MAP position (TODO: MAP position -> star index)
			if ( t_oGLIP.getModificationPosition() != 0 ) {
				t_sbGlycan.append(" ;\n");
				t_sbGlycan.append("\t"+PredicateList.HAS_STAR_INDEX.getTripleLiteral( t_oGLIP.getModificationPosition() ));
			}
			t_sbGlycan.append(" .\n\n");
		}

		return t_sbGlycan.toString();
	}

}
