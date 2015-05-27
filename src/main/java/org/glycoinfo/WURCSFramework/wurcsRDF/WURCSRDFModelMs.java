package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSAnobase;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.WURCS;
import org.openrdf.model.vocabulary.RDF;

public class WURCSRDFModelMs extends WURCSRDFModel{
	
	@SuppressWarnings("unused")
	private WURCSRDFModelMs(){}
	
	public WURCSRDFModelMs(LinkedList<UniqueRES> a_aUniqueRESs){
		super();
		createWURCSMonosaccharideTripleModel(a_aUniqueRESs);
	}

	private WURCSRDFModelMs createWURCSMonosaccharideTripleModel(LinkedList<UniqueRES> a_aUniqueRESs) {

		TreeMap<String, UniqueRES> t_mapURItoMS         = new TreeMap<String, UniqueRES>();
		TreeMap<String, UniqueRES> t_mapURItoBasetype   = new TreeMap<String, UniqueRES>();
		TreeMap<String, UniqueRES> t_mapURItoAnobase    = new TreeMap<String, UniqueRES>();
		TreeMap<String, TreeSet<String>> t_mapMSSubsums      = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, TreeSet<String>> t_mapAnobaseSubsums = new TreeMap<String, TreeSet<String>>();

		TreeMap<String, MOD> t_mapURItoMOD = new TreeMap<String, MOD>();

		WURCSRDFModelMs model = this;
		WURCSExporter m_oExport = new WURCSExporter();
		
		// Monosaccharide triple
		for (UniqueRES t_oURES : a_aUniqueRESs) {

			String t_strMSURI = WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES));
			t_mapURItoMS.put(t_strMSURI, t_oURES);

			model.addTriple(createResourceTriple(
					WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
					RDF.TYPE.stringValue(),
					WURCS.A_MS));
			
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

				model.addTriple(createResourceTriple(
						WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
						WURCS.HAS_RING,
						WURCSTripleURL.MOD.get(null, m_oExport.getMODString(mod))));
			}

			// For SC
			String t_strSkeletonCode =  t_oURES.getSkeletonCode();

			model.addTriple(createResourceTriple(
					WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
					WURCS.HAS_SC,
					WURCSTripleURL.SC.get(null, t_strSkeletonCode)));

			if ( t_oURES.getAnomericPosition() != 0 ) {
				// For anomeric position
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
						WURCS.HAS_ANOM_POS,
						t_oURES.getAnomericPosition()));

				// For anomeric symbol
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
						WURCS.HAS_ANOM_SYMBOL,
						t_oURES.getAnomericSymbol()));
			}

			// For MODs
			for (MOD mod : t_oURES.getMODs()) {
				String t_strMODURI = WURCSTripleURL.MOD.get(null, m_oExport.getMODString(mod));
				t_mapURItoMOD.put(t_strMODURI, mod);

				model.addTriple(createResourceTriple(
						WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
						WURCS.HAS_MOD,
						WURCSTripleURL.MOD.get(null, m_oExport.getMODString(mod))));
			}

			// For subsumes
			if ( !t_mapMSSubsums.containsKey(t_strMSURI) )
				t_mapMSSubsums.put(t_strMSURI, new TreeSet<String>());
			t_mapMSSubsums.get(t_strMSURI).add(t_strMSURI);

			UniqueRES t_oSupersum = t_oURES;
			String t_strSupersumURI = t_strMSURI;
			String t_strSupersumURIold = "";
			while( true ) {
				t_strSupersumURIold = t_strSupersumURI;
				t_oSupersum = WURCSMonosaccharideIntegrator.supersumes(t_oSupersum);
				t_strSupersumURI = WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oSupersum));
				if ( t_strSupersumURIold.equals(t_strSupersumURI) ) break;

				t_mapURItoMS.put(t_strSupersumURI, t_oSupersum);

				if ( !t_mapMSSubsums.containsKey(t_strSupersumURI) )
					t_mapMSSubsums.put(t_strSupersumURI, new TreeSet<String>());
				t_mapMSSubsums.get(t_strSupersumURI).add(t_strMSURI);
			}

			// For 
			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
			String t_strBasetypeURI = WURCSTripleURL.BASE_TYPE.get(null, m_oExport.getUniqueRESString(t_oBasetype));
			t_mapURItoBasetype.put(t_strBasetypeURI, t_oBasetype); // Store basetype

			model.addTriple(createResourceTriple(
					WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
					WURCS.HAS_BASETYPE,
					WURCSTripleURL.BASE_TYPE.get(null, m_oExport.getUniqueRESString(t_oBasetype))));

			// For anobase
			UniqueRES t_oAnobase = WURCSMonosaccharideIntegrator.convertAnobase(t_oURES);
			String t_strAnobaseURI = WURCSTripleURL.ANO_BASE.get(null, m_oExport.getUniqueRESString(t_oAnobase));
			t_mapURItoAnobase.put(t_strAnobaseURI, t_oAnobase);

			model.addTriple(createResourceTriple(
					WURCSTripleURL.MS.get(null, m_oExport.getUniqueRESString(t_oURES)),
					WURCS.HAS_ANOBASE,
					WURCSTripleURL.ANO_BASE.get(null, m_oExport.getUniqueRESString(t_oAnobase))));

			// Subsums itself
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
				t_strSupersumURI = WURCSTripleURL.ANO_BASE.get(null, m_oExport.getUniqueRESString(t_oSupersum));
				if ( t_strSupersumURIold.equals(t_strSupersumURI) ) break;

				t_mapURItoAnobase.put(t_strSupersumURI, t_oSupersum);

				if ( !t_mapAnobaseSubsums.containsKey(t_strSupersumURI) )
					t_mapAnobaseSubsums.put(t_strSupersumURI, new TreeSet<String>());
				t_mapAnobaseSubsums.get(t_strSupersumURI).add(t_strAnobaseURI);
			}
		}

		// monosaccharide subsume triple
		for ( String t_strMSURI : t_mapURItoMS.keySet() ) {
			model.addTriple(createResourceTriple(
					t_strMSURI,
					RDF.TYPE.toString(),
					WURCS.A_MS));

			for ( String t_strSubsumURI : t_mapMSSubsums.get(t_strMSURI) ) {
				model.addTriple(createResourceTriple(
						t_strMSURI,
						WURCS.SUBSUMES,
						t_strSubsumURI));
			}
		}

		// basetype triple
		for ( String t_strBasetypeURI : t_mapURItoBasetype.keySet() ) {
			model.addTriple(createResourceTriple(
					t_strBasetypeURI,
					RDF.TYPE.toString(),
					WURCS.A_BASETYPE));

			UniqueRES t_oBasetype = t_mapURItoBasetype.get(t_strBasetypeURI);
			String t_strBasetype = WURCSBasetype.getBasetype(t_oBasetype);

			model.addTriple(createLiteralTriple(
					t_strBasetypeURI,
					WURCS.A_BASETYPE,
					t_strBasetype));
		}

		// anobase triple
		for ( String t_strAnobaseURI : t_mapURItoAnobase.keySet() ) {
			model.addTriple(createResourceTriple(
					t_strAnobaseURI,
					RDF.TYPE.toString(),
					WURCS.A_ANOBASE));

			UniqueRES t_oAnobase = t_mapURItoAnobase.get(t_strAnobaseURI);
			String t_strAnobase = WURCSAnobase.getAnobase(t_oAnobase);

			model.addTriple(createLiteralTriple(
					t_strAnobaseURI,
					WURCS.A_ANOBASE,
					t_strAnobase));

			for ( String t_strSubsumURI : t_mapAnobaseSubsums.get(t_strAnobaseURI) ) {
				model.addTriple(createResourceTriple(
						t_strAnobaseURI,
						WURCS.SUBSUMES,
						t_strSubsumURI));
			}
		}

		// MOD triple
		TreeMap<String, LIPs> t_mapURItoLIPS = new TreeMap<String, LIPs>();
		for (String t_strMODURI : t_mapURItoMOD.keySet()) {
			model.addTriple(createResourceTriple(
					t_strMODURI,
					RDF.TYPE.toString(),
					WURCS.A_MOD));

			MOD mod = t_mapURItoMOD.get(t_strMODURI);
			// For MAP
			if ( !mod.getMAPCode().isEmpty() ) {
				String t_strMAP = mod.getMAPCode();

				model.addTriple(createLiteralTriple(
						t_strMODURI,
						WURCS.HAS_MAP,
						t_strMAP));
			}

			// For LIPs
			for ( LIPs lips : mod.getListOfLIPs() ) {
				String t_strLIPSURI = WURCSTripleURL.LIPS.get(null, m_oExport.getLIPsString(lips));
				t_mapURItoLIPS.put(t_strLIPSURI, lips);
				
				model.addTriple(createResourceTriple(
						t_strMODURI,
						WURCS.HAS_LIPS,
						t_strLIPSURI));
			}
		}

		// LIPS triple
		TreeMap<String, LIP> t_mapURItoLIP = new TreeMap<String, LIP>();
		for ( String t_strLIPSURI : t_mapURItoLIPS.keySet() ) {
			model.addTriple(createResourceTriple(
					t_strLIPSURI,
					RDF.TYPE.toString(),
					WURCS.A_LIPS));

			LIPs lips = t_mapURItoLIPS.get(t_strLIPSURI);
			// For fuzzyness
			model.addTriple(createLiteralTriple(
					t_strLIPSURI,
					WURCS.IS_FUZZY,
					lips.isFuzzy()));

			for ( LIP lip : lips.getLIPs() ) {
				String t_strLIPURI = WURCSTripleURL.LIP.get(null, m_oExport.getLIPString(lip));
				t_mapURItoLIP.put(t_strLIPURI, lip);

				model.addTriple(createResourceTriple(
						t_strLIPSURI,
						WURCS.HAS_LIP,
						t_strLIPURI));
			}
		}

		// LIP triple
		for ( String t_strLIPURI : t_mapURItoLIP.keySet() ) {
			model.addTriple(createResourceTriple(
					t_strLIPURI,
					RDF.TYPE.toString(),
					WURCS.A_LIP));

			LIP t_oLIP = t_mapURItoLIP.get(t_strLIPURI);

			// For probabilities
			if ( t_oLIP.getBackboneProbabilityLower() != 1.0 ) {
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_B_PROB_LOW,
						t_oLIP.getBackboneProbabilityLower()));
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_B_PROB_UP,
						t_oLIP.getBackboneProbabilityUpper()));
			}
			if ( t_oLIP.getModificationProbabilityLower() != 1.0 ) {
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_M_PROB_LOW,
						t_oLIP.getModificationProbabilityLower()));
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_M_PROB_UP,
						t_oLIP.getModificationProbabilityUpper()));
			}

			// For SC position
			int t_iSCPos = t_oLIP.getBackbonePosition();
			model.addTriple(createLiteralTriple(
					t_strLIPURI,
					WURCS.HAS_SC_POS,
					t_iSCPos));
			
			// For direction
			if ( t_oLIP.getBackboneDirection() != ' ' ) {
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_DIRECTION,
						t_oLIP.getBackboneDirection()));
			}
			// For MAP position (TODO: MAP position -> star index)
			if ( t_oLIP.getModificationPosition() != 0 ) {
				model.addTriple(createLiteralTriple(
						t_strLIPURI,
						WURCS.HAS_STAR_INDEX,
						t_oLIP.getModificationPosition()));
			}

		}
		return model;
	}
}
