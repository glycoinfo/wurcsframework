package org.glycoinfo.WURCSFramework.util.mass;

import java.io.File;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;


public class WURCStoMassTest {

	public static void main(String[] args) {

		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
				//"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\\c6_g1-c3\\c6";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c6*S*~10:100";

		WURCSArray t_objWURCS = new WURCSArray("2.0", 0, 0, 0);
		WURCSImporter t_objImporter = new WURCSImporter();
		File file = new File(input);

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("W")));
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		LinkedList<String> testMAPs = new LinkedList<String>();
		testMAPs.add("*OPO/3O/3=O");
		testMAPs.add("*NCC/3=O");
		testMAPs.add("*O");
		testMAPs.add("*O*");
		testMAPs.add("O");
		testMAPs.add("HOH");
		double testMass = 0;
		for ( String MAP : testMAPs ) {
			testMass = WURCSMassCalculator.getMassMAP(MAP);
			System.out.println(MAP + " : " + testMass);
		}
		testMass = WURCSMassCalculator.getMassSkeletonCode("u2122h");
		System.out.println(testMass);
		System.out.println();


		// Calcurate mass from WURCS
		double mass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		System.out.println(mass);

	}

}
