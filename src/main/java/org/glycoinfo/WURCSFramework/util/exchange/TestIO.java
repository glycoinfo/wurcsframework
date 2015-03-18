package org.glycoinfo.WURCSFramework.util.exchange;

import org.glycoinfo.WURCSFramework.graph.WURCSException;
import org.glycoinfo.WURCSFramework.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public class TestIO {
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		String input = "WURCS=2.0/7,10,10/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2%?%*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-<2-3-4-2-5>-4-2-6-7/" +
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4%.1:.9%-f1_g2-h1_h4-i1%?%_d1-c3|c6_g1-c3|c6_b1-f4~n:100";

		// GlycomeDB: 12241
		input = "WURCS=2.0/3,12,16/[1212h-1b_1-5][12112h-1b_1-5][1211h-1a_1-4]/<<1>-<2-2>-<2-2-2-2>-<2-2-3-3>-2>/a4-b1_b3-d1_b6-c1_d3-h1_d6-e1_e3-f1_f3-g1_h3-l1_h6-i1_i6-j1_j3-k1_a1-a4~n_a1-l3~n_b1-b3~n_d1-d3~n_h1-h3~n";
		// GlycomeDB: 35232
		input = "WURCS=2.0/13,14,13/[aUd21122h][211221h-1a_1-5][211221h-1a_1-5_4*OPO/3O/3=O][22122a-1a_1-5][22122h-1a_1-5_2*N][12122h-1b_1-5][12112m-1b_1-5_2*NCC/3=O_4*N][11122a-1b_1-5_2*NCC/3=O_3*NCC/3=O][22112h-1a_1-5_2*NCC/3=O][12122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][11122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][112eEH-1b_1-5_2*OCC/3=O_3*OCC/3=O_6*N][22112a-1a_1-5_2*N]/1-2-3-4-5-6-5-7-8-9-10-11-12-13/a5-b1_b3-c1_b4-f1_c2-d1_c7-e1_f4-g1_f6-n1_g6-h1_h3-i1_i4-j1_j4-k1_k4-l1_l4-m1";
		WURCSImporter t_objImporter = new WURCSImporter();
		try {
			// Import WURCS without error messages
			WURCSArray t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("WURCS=")));
			WURCSArrayToGraph t_objToGraph = new WURCSArrayToGraph();
			t_objToGraph.start(t_objWURCS);
			WURCSGraph t_objGraph = t_objToGraph.getGraph();
			WURCSGraphToArray t_objToArray = new WURCSGraphToArray();
			t_objToArray.start(t_objGraph);
			t_objWURCS = t_objToArray.getWURCSArray();

			WURCSExporter export = new WURCSExporter();
			String WURCSString = export.getWURCSString(t_objWURCS);
			System.out.println(input);
			if (! input.equals(WURCSString) ) {
				System.out.println("Change WURCS string in importer and exporter");
				System.out.println(WURCSString);
			}

		} catch (WURCSFormatException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		} catch (WURCSException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		}

	}
}
