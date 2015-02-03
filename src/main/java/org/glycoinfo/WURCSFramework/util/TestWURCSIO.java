package org.glycoinfo.WURCSFramework.util;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public class TestWURCSIO {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		String input = "WURCS=2.0/7,10,10/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2%?%*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4%.1:.9%-f1_g2-h1_h4-i1%?%_d1-c3|c6_g1-c3|c6_b1-f4~n:100";

//		input = "WURCS=2.0/3,8,7/[12112h-1b_1-5][12122h-1b_1-5][12122h-1b_1-5_4-6*OC^XO*/3CO/6=O/3C]/1-2-2-2-2-2-3-1/a3-b1_b4-c1_c4-d1_d6-e1_e6-f1_f3-g1_g3-h1";
		WURCSImporter t_objImporter = new WURCSImporter();
		WURCSArray t_objWURCS;
		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("W")));
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
		}

		/**
		int[] ids = { 1, 2, 3, 26, 27, 28, 52, 53, 54, 104, 105, 106, 1000, 2000};
		for ( int id : ids ) {
			String newindex = WURCSDataConverter.convertRESIDToIndex(id);
			System.out.print(id + "->" + newindex);
			int newid = WURCSDataConverter.convertRESIndexToID(newindex);
			System.out.println("->" + newid);
		}
		String[] indexes = {"a", "b", "c", "x", "y", "z", "A", "B", "C", "X", "Y", "Z", "AA", "BB", "CC", "XX", "YY", "ZZ" };
		for ( String index : indexes ) {
			int newid = WURCSDataConverter.convertRESIndexToID(index);
			System.out.print(index + "->" + newid);
			String newindex = WURCSDataConverter.convertRESIDToIndex(newid);
			System.out.println("->" + newindex);
		}
		*/

	}

}
