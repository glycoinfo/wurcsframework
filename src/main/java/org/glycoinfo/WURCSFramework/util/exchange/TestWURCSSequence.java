package org.glycoinfo.WURCSFramework.util.exchange;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;

public class TestWURCSSequence {

	public static void main(String[] args) {

		String input;
		// GlycomeDB: 4385
		input = "WURCS=2.0/2,4,4/[22122h-1a_1-5][h5122h-2b_2-5]/1-1-2-2/a1-b1_b6-c2_c1-d2_c1-c2~n";
//		input = "WURCS=2.0/13,14,13/[aUd21122h][211221h-1a_1-5][211221h-1a_1-5_4*OPO/3O/3=O][22122a-1a_1-5][22122h-1a_1-5_2*N][12122h-1b_1-5][12112m-1b_1-5_2*NCC/3=O_4*N][11122a-1b_1-5_2*NCC/3=O_3*NCC/3=O][22112h-1a_1-5_2*NCC/3=O][12122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][11122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][112eEH-1b_1-5_2*OCC/3=O_3*OCC/3=O_6*N][22112a-1a_1-5_2*N]/1-2-3-4-5-6-5-7-8-9-10-11-12-13/a5-b1_b3-c1_b4-f1_c2-d1_c7-e1_f4-g1_f6-n1_g6-h1_h3-i1_i4-j1_j4-k1_k4-l1_l4-m1";

		System.out.println(input);
		System.out.println();
		WURCSImporter t_objImporter = new WURCSImporter();
		try {
			// Import WURCS without error messages
			WURCSArray t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("WURCS=")));

			WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
			t_oA2S.start(t_objWURCS);
			WURCSSequence t_oSeq = t_oA2S.getSequence();
			for ( GLIN t_oGLIN : t_oSeq.getGLINs() ) {
				System.out.println(t_oGLIN.getGLINString());
				if ( !t_oGLIN.getMAP().equals("") )
					System.out.println("\twurcs:has_MAP\t"+t_oGLIN.getMAP());

				for ( MS t_oMS : t_oGLIN.getDonorMSs() )
					System.out.println("\twurcs:has_donor_MS\t"+t_oMS.getString());
				for ( int t_iPos : t_oGLIN.getDonorPositions() )
					System.out.println("\twurcs:is_donor_position\t"+t_iPos);
				for ( MS t_oMS : t_oGLIN.getAcceptorMSs() )
					System.out.println("\twurcs:has_acceptor_MS\t"+t_oMS.getString());
				for ( int t_iPos : t_oGLIN.getAcceptorPositions() )
					System.out.println("\twurcs:is_acceptor_position\t"+t_iPos);

				if ( t_oGLIN.isRepeat() ) {
					System.out.println("\twurcs:has_rep_min\t"+t_oGLIN.getRepeatCountMin());
					System.out.println("\twurcs:has_rep_max\t"+t_oGLIN.getRepeatCountMax());
				}
			}
			System.out.println("\n");

			for ( GRES t_oGRES :t_oSeq.getGRESs() ) {
				System.out.println("GRES"+t_oGRES.getID());
				System.out.println("\tis_monosaccharide\t"+t_oGRES.getMS().getString());
				for ( GLIN t_oGLIN : t_oGRES.getDonorGLINs() ) {
					System.out.println( "\tis_donor_of\t"+  t_oGLIN.getGLINString() );
				}
				for ( GLIN t_oGLIN : t_oGRES.getAcceptorGLINs() ) {
					System.out.println( "\tis_acceptor_of\t"+  t_oGLIN.getGLINString() );
				}
			}


		} catch (WURCSFormatException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		}

	}

}
