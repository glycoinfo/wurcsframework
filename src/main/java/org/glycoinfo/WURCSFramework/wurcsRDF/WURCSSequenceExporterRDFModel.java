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
		t_oModel.addTriple(createResourceTriple(
				WURCSTripleURL_TBD.GLYCAN_AC.get(a_strAccessionNumber, null),
				GLYCAN.HAS_GSEQ,
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null)));


		// Glycosequence triple

		// For GRES
		for ( GRES t_oGRES : a_oSeq.getGRESs() ) {
			t_oModel.addTriple(createResourceTriple(
					WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
					WURCSSEQ.HAS_GRES,
					WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID())));
		}

		// For WURCS sequence
		t_oModel.addTriple(createLiteralTriple(
				WURCSTripleURL_TBD.GSEQ_AC.get(a_strAccessionNumber, null),
				GLYCAN.HAS_SEQ,
				a_oSeq.getWURCS()));

		// GRES triple
		for ( GRES t_oGRES : a_oSeq.getGRESs() ) {

			String t_strGRESURL = WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID());

			t_oModel.addTriple(createResourceTriple(
				t_strGRESURL, WURCSSEQ.IS_MS, WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oGRES.getMS().getString() )
			));

			// For GLIN of donor side
			for ( GLIN t_oGLIN : t_oGRES.getDonorGLINs() ) {
				t_oModel.addTriple(createResourceTriple(
					t_strGRESURL, WURCSSEQ.IS_DONOR, WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString() )
				));
			}
			// For GLIN of acceptor side
			for ( GLIN t_oGLIN : t_oGRES.getAcceptorGLINs() ) {
				t_oModel.addTriple(createResourceTriple(
					t_strGRESURL, WURCSSEQ.IS_ACCEPTOR, WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString() )
				));
			}
		}

		// GLIN triple
		for ( GLIN t_oGLIN : a_oSeq.getGLINs() ) {

			String t_strGLINURL = WURCSTripleURL_TBD.GLIN.get(a_strAccessionNumber, t_oGLIN.getGLINString());

			// For donor monosaccharide
			for ( MS t_oMS : t_oGLIN.getDonorMSs() ) {
				t_oModel.addTriple(createResourceTriple(
						t_strGLINURL, WURCSSEQ.HAS_D_MS, WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oMS.getString() )
				));
			}
			// For donor position
			for ( Integer t_iPos : t_oGLIN.getDonorPositions() ) {
				t_oModel.addTriple(createLiteralTriple(
						t_strGLINURL, WURCSSEQ.HAS_D_POS, t_iPos));
			}
			// For acceptor monosaccharide
			for ( MS t_oMS : t_oGLIN.getAcceptorMSs() ) {
				t_oModel.addTriple(createResourceTriple(
						t_strGLINURL, WURCSSEQ.HAS_A_MS, WURCSTripleURL_TBD.MS.get(a_strAccessionNumber, t_oMS.getString() )
				));
			}
			// For acceptor position
			for ( Integer t_iPos : t_oGLIN.getAcceptorPositions() ) {
				t_oModel.addTriple(createLiteralTriple(
						t_strGLINURL, WURCSSEQ.HAS_A_POS, t_iPos
				));
			}

			// For MAP
			t_oModel.addTriple(createLiteralTriple(
					t_strGLINURL, WURCSSEQ.HAS_MAP, t_oGLIN.getMAP()
			));

		}

		return t_oModel;
	}

}
