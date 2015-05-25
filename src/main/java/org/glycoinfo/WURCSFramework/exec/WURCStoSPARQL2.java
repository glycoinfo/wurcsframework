package org.glycoinfo.WURCSFramework.exec;

import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence2;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequence2ExporterSPARQL;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;

public class WURCStoSPARQL2 {
	public static void main(String[] args) {

		String input = "";
		input = "WURCS=2.0/2,2,1/[aUd1122h][22122a-1a_1-5]/1-2/a4-b1";
		input = "WURCS=2.0/3,4,3/[h2122h][12122h-1b_1-5][12122h-1b_1-5_2*NCC/3=O]/1-2-2-3/a3-b1_b3-c1_c4-d1";
		input = "WURCS=2.0/2,2,1/[h2122h][12122h-1b_1-5]/1-2/a2-b1";
		input = "WURCS=2.0/2,2,1/[x2122h-1x_1-5][12122a-1b_1-5_6*=O_6*OC]/1-2/a2-b1";
		input = "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4";
//		input = "WURCS=2.0/4,4,3/[u2122h][12112h-1b_1-5][22112h-1a_1-5][12112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1";
		input = "WURCS=2.0/4,4,3/[12122h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O][11221m-1a_1-5]/1-2-3-4/a3|a4-b1_a3|a4-d1_b3-c2";
		input = "WURCS=2.0/7,12,14/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][xxxxxh-1x_1-?_2*NCC/3=O][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-5-6-7-7-4-5-6-7/a4-b1_b4-c1_e4-f1_f3-g2_g8-h2_j4-k1_k3-l2_c?-d1_c?-i1_d?-e1_i?-j1_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O";

		try {
			WURCSImporter t_oImport = new WURCSImporter();
			WURCSArray t_oWURCS = t_oImport.extractWURCSArray(input);
			WURCSArrayToSequence2 t_oA2S = new WURCSArrayToSequence2();
			t_oA2S.start(t_oWURCS);
			WURCSSequence2 t_oSeq = t_oA2S.getSequence();

			WURCSSequence2ExporterSPARQL t_oExport = new WURCSSequence2ExporterSPARQL();

			// Set option for SPARQL query generator
			//t_oExport.setCountOption(true); // True for result count
			//t_oExport.addTergetGraphURI("<http://rdf.glycoinfo.org/wurcs/seq>"); // Add your terget graph
			//t_oExport.setMSGraphURI("<http://rdf.glycoinfo.org/wurcs/0.5.1/ms>"); // Set your monosaccharide graph

			t_oExport.start(t_oSeq);
			String t_strSPARQL = t_oExport.getQuery();

			System.out.println(t_strSPARQL);

		} catch (WURCSFormatException e) {
			e.printStackTrace();
		}

	}

}
