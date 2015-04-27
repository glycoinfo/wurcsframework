package org.glycoinfo.WURCSFramework.util.exchange;

import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSSequenceExporterRDFModel;

public class TestWURCSSequence {

	public static void main(String[] args) {

		String input;
		// GlycomeDB: 4385
		input = "WURCS=2.0/2,4,4/[22122h-1a_1-5][h5122h-2b_2-5]/1-1-2-2/a1-b1_b6-c2_c1-d2_c1-c2~n";
		input = "WURCS=2.0/13,14,13/[aUd21122h][211221h-1a_1-5][211221h-1a_1-5_4*OPO/3O/3=O][22122a-1a_1-5][22122h-1a_1-5_2*N][12122h-1b_1-5][12112m-1b_1-5_2*NCC/3=O_4*N][11122a-1b_1-5_2*NCC/3=O_3*NCC/3=O][22112h-1a_1-5_2*NCC/3=O][12122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][11122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][112eEH-1b_1-5_2*OCC/3=O_3*OCC/3=O_6*N][22112a-1a_1-5_2*N]/1-2-3-4-5-6-5-7-8-9-10-11-12-13/a5-b1_b3-c1_b4-f1_c2-d1_c7-e1_f4-g1_f6-n1_g6-h1_h3-i1_i4-j1_j4-k1_k4-l1_l4-m1";
		input = "WURCS=2.0/2,6,7/[22122h-1a_1-5][22112h-1a_1-5]/1-1-1-1-1-2/a1-c4_a4-b1_b4-c1_c6-d1_d4-e1_e6-f1_b1-b4~n";
		input = "WURCS=2.0/5,5,5/[h222h][12112h-1b_1-5_2*NCC/3=O_6*OPO/3O/3=O][22112h-1a_1-5_2*NCC/3=O_6*OPO/3O/3=O][22112m-1a_1-5_2*NCC/3=O_4*N][12122h-1b_1-5]/1-2-3-4-5/a5-b1_b3-c1_c4-d1_d3-e1_a1-e6*OP^XO*/3O/3=O~n";
	//	input = "WURCS=2.0/2,4,5/[12122h-1b_1-5][hxh]/1-1-1-2/a1-c2_a2-b1_b2-c1_c6-d1*OP^XO*/3O/3=O_b1-b2~n";

		WURCSImporter t_objImporter = new WURCSImporter();
		try {
			// Import WURCS without error messages
			WURCSArray t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("WURCS=")));

			WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
			t_oA2S.start(t_objWURCS);
			WURCSSequence t_oSeq = t_oA2S.getSequence();

			WURCSSequenceExporterRDFModel t_oExporter = new WURCSSequenceExporterRDFModel("GXXXXXXX", t_oSeq);
			System.out.println( t_oExporter.get_RDF("TURTLE") );


		} catch (WURCSFormatException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		}

	}

}
