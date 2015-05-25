package org.glycoinfo.WURCSFramework.wurcsRDF;

import org.glycoinfo.WURCSFramework.wurcs.sequence2.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.GLYCAN;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.WURCSSEQ;


public class WURCSSequence2ExporterRDFModel extends WURCSRDFModel {

	@SuppressWarnings("unused")
	private WURCSSequence2ExporterRDFModel(){}

	public WURCSSequence2ExporterRDFModel(String a_strAccessionNumber, WURCSSequence2 a_oWURCS){
		super(false);
		this.addPrefix(PrefixList.GLYCAN.getPrefix(), PrefixList.GLYCAN.getPrefixURI());
		this.addPrefix(PrefixList.WURCS.getPrefix(), PrefixList.WURCS.getPrefixURI());
		start(a_strAccessionNumber, a_oWURCS);
	}

	private WURCSSequence2ExporterRDFModel start(String a_strAccessionNumber, WURCSSequence2 a_oSeq) {

		WURCSSequence2ExporterRDFModel t_oModel = this;
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
					t_strGRESURL, WURCSSEQ.IS_DONOR, WURCSTripleURL_TBD.GLIN_AC.get(a_strAccessionNumber, t_oGLIN.getID() )
				));
			}
			// For GLIN of acceptor side
			for ( GLIN t_oGLIN : t_oGRES.getAcceptorGLINs() ) {
				t_oModel.addTriple(createResourceTriple(
					t_strGRESURL, WURCSSEQ.IS_ACCEPTOR, WURCSTripleURL_TBD.GLIN_AC.get(a_strAccessionNumber, t_oGLIN.getID() )
				));
			}
		}

		// GLIN triple
		for ( GLIN t_oGLIN : a_oSeq.getGLINs() ) {

			String t_strGLINURL = WURCSTripleURL_TBD.GLIN_AC.get(a_strAccessionNumber, t_oGLIN.getID());

			// For donor monosaccharide
			for ( GRES t_oGRES : t_oGLIN.getDonor() ) {
				t_oModel.addTriple(createResourceTriple(
						t_strGLINURL, WURCSSEQ.HAS_DONOR, WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID() )
				));
			}
			// For donor position
			for ( Integer t_iPos : t_oGLIN.getDonorPositions() ) {
				t_oModel.addTriple(createLiteralTriple(
						t_strGLINURL, WURCSSEQ.HAS_D_POS, t_iPos));
			}
			// For acceptor monosaccharide
			for ( GRES t_oGRES : t_oGLIN.getAcceptor() ) {
				t_oModel.addTriple(createResourceTriple(
						t_strGLINURL, WURCSSEQ.HAS_ACCEPTOR, WURCSTripleURL_TBD.GRES_AC.get(a_strAccessionNumber, t_oGRES.getID() )
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
