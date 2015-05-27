package org.glycoinfo.WURCSFramework.exec;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequenceExporterSPARQL;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;

public class WURCStoSPARQL_for_php {

	public static void main(String[] args) {

		String t_strWURCS = "";
		t_strWURCS = "WURCS=2.0/2,2,1/[aUd1122h][22122a-1a_1-5]/1-2/a4-b1";
		t_strWURCS = "WURCS=2.0/3,4,3/[h2122h][12122h-1b_1-5][12122h-1b_1-5_2*NCC/3=O]/1-2-2-3/a3-b1_b3-c1_c4-d1";
		t_strWURCS = "WURCS=2.0/2,2,1/[h2122h][12122h-1b_1-5]/1-2/a2-b1";
		t_strWURCS = "WURCS=2.0/2,2,1/[x2122h-1x_1-5][12122a-1b_1-5_6*=O_6*OC]/1-2/a2-b1";
		t_strWURCS = "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4";
//		input = "WURCS=2.0/4,4,3/[u2122h][12112h-1b_1-5][22112h-1a_1-5][12112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1";

		WURCSSequenceExporterSPARQL t_oExport = new WURCSSequenceExporterSPARQL();

		for (int i=0; i<args.length; ++i) {
			if ("-w".equals(args[i])) {
				t_strWURCS = args[++i];
			}
			else if ("-c".equals(args[i])) {
				String t_strOption = args[++i];
				if (t_strOption.equals("true")) t_oExport.setCountOption(true); // True for result
//				if (t_strOption.equals("false")) t_oExport.setCountOption(false); // True for result
			} else if ("-tg".equals(args[i])) {
				t_oExport.addTergetGraphURI(args[++i]); // Add your terget graph
			} else if ("-mg".equals(args[i])) {
				t_oExport.setMSGraphURI(args[++i]); // Add your terget graph
			}
		}
		
		try {
			WURCSImporter t_oImport = new WURCSImporter();
			WURCSArray t_oWURCS = t_oImport.extractWURCSArray(t_strWURCS);
			WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
			t_oA2S.start(t_oWURCS);
			WURCSSequence t_oSeq = t_oA2S.getSequence();

			t_oExport.start(t_oSeq);
			String t_strSPARQL = t_oExport.getQuery();

			System.out.println(t_strSPARQL);

		} catch (WURCSFormatException e) {
			e.printStackTrace();
		}
	}
}
