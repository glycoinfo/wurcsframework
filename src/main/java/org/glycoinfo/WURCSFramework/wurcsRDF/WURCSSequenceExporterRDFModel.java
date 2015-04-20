package org.glycoinfo.WURCSFramework.wurcsRDF;

import org.glycoinfo.WURCSFramework.wurcs.sequence.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.GLYCAN;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.WURCSSEQ;


public class WURCSSequenceExporterRDFModel extends WURCSRDFModel {

	@SuppressWarnings("unused")
	private WURCSSequenceExporterRDFModel(){}

	public WURCSSequenceExporterRDFModel(String a_strAccessionNumber, WURCSSequence a_oWURCS){
		super(false);
		this.addPrefix(PrefixList.GLYCAN.getPrefix(), PrefixList.GLYCAN.getPrefixURI());
		this.addPrefix(PrefixList.WURCS.getPrefix(), PrefixList.WURCS.getPrefixURI());
		start(a_strAccessionNumber, a_oWURCS);
	}

	private WURCSSequenceExporterRDFModel start(String a_strAccessionNumber, WURCSSequence a_oSeq) {

		WURCSSequenceExporterRDFModel t_oModel = this;
		// Saccharide triple
/*		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GLYCAN_AC.get(a_strAccessionNumber, null),
				RDF.TYPE.stringValue(),
				GLYCAN.A_SACCHARIDE));
*/
		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GLYCAN_AC.get(a_strAccessionNumber, null),
				GLYCAN.HAS_GSEQ,
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null)));


		// Glycosequence triple
/*
		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
				RDF.TYPE.stringValue(),
				GLYCAN.A_GSEQ));

		// For uniqueRES count
		t_oModel.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_URES,
				a_oWURCS.getUniqueRESCount()));

		// For RES count
		t_oModel.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_RES,
				a_oWURCS.getRESCount()));

		// For LIN count
		t_oModel.addTriple(createLiteralTriple(
				WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
				WURCS.NUM_LIN,
				a_oWURCS.getLINCount()));
*/

		// For GRES
		for ( GRES t_oGRES : a_oSeq.getGRESs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
					WURCSSEQ.HAS_GRES,
					WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID())));
		}

/*
		for (UniqueRES t_oURES : a_oWURCS.getUniqueRESs()) {
			// For unique RES ID
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_URES,
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID())));

			// For monosaccharide of unique RES
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_MS,
					WURCSTripleURL.MS.get(a_strAccessionNumber, export.getUniqueRESString(t_oURES))));

			// For basetype of unique RES
			UniqueRES t_oBasetype = WURCSMonosaccharideIntegrator.convertBasetype(t_oURES);
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_BASETYPE,
					WURCSTripleURL.BASE_TYPE.get(a_strAccessionNumber, export.getUniqueRESString(t_oBasetype))));
		}

		for ( LIN t_oLIN :a_oWURCS.getLINs() ) {
			// For LIN
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.GLY_WURCS.get(a_strAccessionNumber, null),
					WURCS.HAS_LIN,
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN))));
		}
*/
		// For WURCS sequence
		t_oModel.addTriple(createLiteralTriple(
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
				GLYCAN.HAS_SEQ,
				a_oSeq.getWURCS()));
/*
		// For format
		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
				GLYCAN.IN_CARB_FORMAT,
				GLYCAN.FORMAT_WURCS));

		// Same as
		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
				OWL.SAMEAS.stringValue(),
				WURCSTripleURL_TBD.WURCS_URL.get(a_strAccessionNumber,  a_oSeq.getWURCS() )));

		// UniqueRES triple
		for ( UniqueRES t_oURES : a_oWURCS.getUniqueRESs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID()),
					RDF.TYPE.stringValue(),
					WURCS.A_URES));

			// For monosaccharide of unique RES
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oURES.getUniqueRESID()),
					WURCS.IS_MS,
					WURCSTripleURL.MS.get(a_strAccessionNumber, export.getUniqueRESString(t_oURES))));
		}

		// RES triple
		for ( RES t_oRES : a_oWURCS.getRESs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
					RDF.TYPE.stringValue(),
					WURCS.A_RES));

			// For uniqueRES of this RES
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
					WURCS.IS_URES,
					WURCSTripleURL.UNIQ_RES.get(a_strAccessionNumber, t_oRES.getUniqueRESID())));
			// For LIN contained this RES
			for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
				if ( !t_oLIN.containRES(t_oRES) ) continue;
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL.RES.get(a_strAccessionNumber, t_oRES.getRESIndex()),
						WURCS.HAS_LIN,
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN))));
			}
		}
*/
		// GRES triple
		for ( GRES t_oGRES : a_oSeq.getGRESs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID()),
					WURCSSEQ.IS_MS,
					WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oGRES.getMS().getString() )));

			// For GLIN of donor side
			for ( GLIN t_oGLIN : t_oGRES.getDonorGLINs() ) {
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID()),
						WURCSSEQ.IS_DONOR,
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString() )));
			}
			// For GLIN of acceptor side
			for ( GLIN t_oGLIN : t_oGRES.getAcceptorGLINs() ) {
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID()),
						WURCSSEQ.IS_ACCEPTOR,
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString() )));
			}
		}
/*
		// LIN triple
		LinkedList<GLIPs> t_aGLIPs = new LinkedList<GLIPs>();
		for ( LIN t_oLIN : a_oWURCS.getLINs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
					RDF.TYPE.stringValue(),
					WURCS.A_LIN));

			// For MAP
			if ( !t_oLIN.getMAPCode().isEmpty() ) {
				t_oModel.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_MAP,
						t_oLIN.getMAPCode()));
			}

			// For repeating
			t_oModel.addTriple(createLiteralTriple(
					WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
					WURCS.IS_REP,
					t_oLIN.isRepeatingUnit()));
			if ( t_oLIN.isRepeatingUnit() ) {
				t_oModel.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_REP_MIN,
						t_oLIN.getMinRepeatCount()));
				t_oModel.addTriple(createLiteralTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_REP_MAX,
						t_oLIN.getMaxRepeatCount()));
			}

			// For GLIPS
			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {
				t_aGLIPs.add(t_oGLIPs);
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL.LIN.get(a_strAccessionNumber, export.getLINString(t_oLIN)),
						WURCS.HAS_GLIPS,
						WURCSTripleURL.GLIPS.get(a_strAccessionNumber, export.getGLIPsString(t_oGLIPs))));
			}
		}
*/
		// GLIN triple
		for ( GLIN t_oGLIN : a_oSeq.getGLINs() ) {
			// For donor monosaccharide
			for ( MS t_oMS : t_oGLIN.getDonorMSs() ) {
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString()),
						WURCSSEQ.HAS_D_MS,
						WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oMS.getString() )));
			}
			// For donor position
			for ( Integer t_iPos : t_oGLIN.getDonorPositions() ) {
				t_oModel.addTriple(createLiteralTriple(
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString()),
						WURCSSEQ.HAS_D_POS,
						t_iPos));
			}
			// For acceptor monosaccharide
			for ( MS t_oMS : t_oGLIN.getAcceptorMSs() ) {
				t_oModel.addTriple(createResourceTriple(
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString()),
						WURCSSEQ.HAS_A_MS,
						WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oMS.getString() )));
			}
			// For acceptor position
			for ( Integer t_iPos : t_oGLIN.getAcceptorPositions() ) {
				t_oModel.addTriple(createLiteralTriple(
						WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString()),
						WURCSSEQ.HAS_A_POS,
						t_iPos));
			}
		}

		return t_oModel;
	}

}
