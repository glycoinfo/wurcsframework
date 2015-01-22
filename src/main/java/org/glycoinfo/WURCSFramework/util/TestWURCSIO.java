package org.glycoinfo.WURCSFramework.util;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class TestWURCSIO {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2%?%*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6_b1-f4~n:100";

		WURCSImporter2 t_objImporter = new WURCSImporter2();
		WURCSArray t_objWURCS = t_objImporter.WURCSsepalator(input.substring(input.indexOf("W")));

		WURCSExporter export = new WURCSExporter();
		String WURCSString = export.getWURCSString(t_objWURCS);



	}

}
