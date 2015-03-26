package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.GLYCAN;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.WURCS;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;

public class WURCSRDFModelGlycan extends WURCSRDFModel{
	
	@SuppressWarnings("unused")
	private WURCSRDFModelGlycan(){}
	
	public WURCSRDFModelGlycan(String a_strAccessionNumber, WURCSArray a_oWURCS){
		super();
		createWURCSGlycanTripleModel(a_strAccessionNumber, a_oWURCS);
	}

	private WURCSRDFModelGlycan createWURCSGlycanTripleModel(String a_strAccessionNumber, WURCSArray a_oWURCS){

		WURCSExporter export = new WURCSExporter();

		// # WURCS
		String t_strWURCSString = export.getWURCSString(a_oWURCS);

		WURCSRDFModelGlycan model = this;
		
		// Saccharide triple
		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLYAN.get(a_strAccessionNumber, null),
				RDF.TYPE.stringValue(),
				GLYCAN.A_SACCHARIDE));

		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLYAN.get(a_strAccessionNumber, null),
				GLYCAN.HAS_GSEQ,
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null)));

		// Glycosequence triple
		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				RDF.TYPE.stringValue(),
				GLYCAN.A_GSEQ));

		// For uniqueRES count
		model.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_URES,
				a_oWURCS.getUniqueRESCount()));

		// For RES count
		model.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_RES,
				a_oWURCS.getRESCount()));
		
		// For LIN count
		model.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_LIN,
				a_oWURCS.getLINCount()));
		
		// For root RES
		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.HAS_ROOT_RES,
				WURCSTripleURL.RES.get(a_strAccessionNumber, "a")));
		
		for (UniqueRES t_oURES : a_oWURCS.getUniqueRESs()) {
			// For unique RES ID
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_URES,
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID())));
			
			// For monosaccharide of unique RES
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_MS,
					WURCSTripleURL.MS.get(a_strAccessionNumber, export.getUniqueRESString(t_oURES))));
			
			// For basetype of unique RES
			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_BASETYPE,
					WURCSTripleURL.BASE_TYPE.get(a_strAccessionNumber, export.getUniqueRESString(t_oBasetype))));
		}

		for ( LIN t_oLIN :a_oWURCS.getLINs() ) {
			// For LIN
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_LIN,
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN))));
		}

		// For WURCS sequence
		model.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				GLYCAN.HAS_SEQ,
				t_strWURCSString));
		
		// For format
		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				GLYCAN.IN_CARB_FORMAT,
				GLYCAN.FORMAT_WURCS));
		
		// Same as
		model.addTriple(createResourceTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				OWL.SAMEAS.stringValue(),
				WURCSTripleURL.WURCS_URL.get(a_strAccessionNumber,  export.getWURCSString(a_oWURCS) )));

		// UniqueRES triple
		for ( UniqueRES t_oURES : a_oWURCS.getUniqueRESs() ) {
			model.addTriple(createResourceTriple(
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID()),
					RDF.TYPE.stringValue(),
					WURCS.A_URES));
			
			// For monosaccharide of unique RES
			model.addTriple(createResourceTriple(
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID()),
					WURCS.IS_MS,
					WURCSTripleURL.MS.get(a_strAccessionNumber, export.getUniqueRESString(t_oURES))));
		}

		// RES triple
		for ( RES t_oRES : a_oWURCS.getRESs() ) {
			model.addTriple(createResourceTriple(
					WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
					RDF.TYPE.stringValue(),
					WURCS.A_RES));
			
			// For uniqueRES of this RES
			model.addTriple(createResourceTriple(
					WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
					WURCS.IS_URES,
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oRES.getUniqueRESID())));
			// For LIN contained this RES
			for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
				if ( !t_oLIN.containRES(t_oRES) ) continue;
				model.addTriple(createResourceTriple(
						WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
						WURCS.HAS_LIN,
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN))));
			}
		}

		// LIN triple
		LinkedList<GLIPs> t_aGLIPs = new LinkedList<GLIPs>();
		for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
			model.addTriple(createResourceTriple(
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
					RDF.TYPE.stringValue(),
					WURCS.A_LIN));
			
			// For MAP
			if ( !t_oLIN.getMAPCode().isEmpty() ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_MAP,
						t_oLIN.getMAPCode()));
			}

			// For repeating
			model.addTriple(createLiteralTriple(
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
					WURCS.IS_REP,
					t_oLIN.isRepeatingUnit()));
			if ( t_oLIN.isRepeatingUnit() ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_REP_MIN,
						t_oLIN.getMinRepeatCount()));
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_REP_MAX,
						t_oLIN.getMaxRepeatCount()));
			}

			// For GLIPS
			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {
				t_aGLIPs.add(t_oGLIPs);
				model.addTriple(createResourceTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_GLIPS,
						WURCSTripleURL.GLIPS.get(a_strAccessionNumber, export.getGLIPsString(t_oGLIPs))));
			}
		}

		// GLIPS triple
		LinkedList<GLIP> t_aGLIP = new LinkedList<GLIP>();
		for ( GLIPs t_oGLIPs : t_aGLIPs ) {
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLIPS.get(a_strAccessionNumber, export.getGLIPsString(t_oGLIPs)),
					RDF.TYPE.stringValue(),
					WURCS.A_GLIPS));

			// Is fuzzy
			model.addTriple(createLiteralTriple(
					WURCSTripleURL.GLIPS.get(a_strAccessionNumber, export.getGLIPsString(t_oGLIPs)),
					WURCS.IS_FUZZY,
					t_oGLIPs.isFuzzy()));

			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				t_aGLIP.add(t_oGLIP);
				model.addTriple(createResourceTriple(
						WURCSTripleURL.GLIPS.get(a_strAccessionNumber, export.getGLIPsString(t_oGLIPs)),
						WURCS.HAS_GLIP,
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP))));
			}
		}

		// GLIP triple
		for ( GLIP t_oGLIP : t_aGLIP ) {
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
					RDF.TYPE.stringValue(),
					WURCS.A_GLIP));

			// For RES index
			model.addTriple(createResourceTriple(
					WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
					WURCS.HAS_RES,
					WURCSTripleURL.RES.get(a_strAccessionNumber, t_oGLIP.getRESIndex())));

			// For probabilities
			if ( t_oGLIP.getBackboneProbabilityLower() != 1.0 ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_B_PROB_LOW,
						t_oGLIP.getBackboneProbabilityLower()));
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_B_PROB_UP,
						t_oGLIP.getBackboneProbabilityUpper()));
			}
			if ( t_oGLIP.getModificationProbabilityLower() != 1.0 ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_M_PROB_LOW,
						t_oGLIP.getModificationProbabilityLower()));
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_M_PROB_UP,
						t_oGLIP.getModificationProbabilityUpper()));
			}

			// For SC position
			model.addTriple(createLiteralTriple(
					WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
					WURCS.HAS_SC_POS,
					t_oGLIP.getBackbonePosition()));
			// For direction
			if ( t_oGLIP.getBackboneDirection() != ' ' ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_DIRECTION,
						t_oGLIP.getBackboneDirection()));
			}
			// For MAP position (TODO: MAP position -> star index)
			if ( t_oGLIP.getModificationPosition() != 0 ) {
				model.addTriple(createLiteralTriple(
						WURCSTripleURL.GLIP.get(a_strAccessionNumber, export.getGLIPString(t_oGLIP)),
						WURCS.HAS_STAR_INDEX,
						t_oGLIP.getModificationPosition()));
			}
		}
		return model;
	}
}
