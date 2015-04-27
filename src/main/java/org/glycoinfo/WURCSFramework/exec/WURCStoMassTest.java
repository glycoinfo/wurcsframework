package org.glycoinfo.WURCSFramework.exec;

import java.io.File;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;


public class WURCStoMassTest {

	public static void main(String[] args) {

		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
				//"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\\c6_g1-c3\\c6";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c6*S*~10:100";

		input = "WURCS=2.0/2,2,1/[aUd1122h][22122a-1a_1-5]/1-2/a4-b1";
		input = "WURCS=2.0/3,4,3/[h2122h][12122h-1b_1-5][12122h-1b_1-5_2*NCC/3=O]/1-2-2-3/a3-b1_b3-c1_c4-d1";
		input = "WURCS=2.0/2,2,1/[h2122h][12122h-1b_1-5]/1-2/a2-b1";
		input = "WURCS=2.0/2,2,1/[x2122h-1x_1-5][12122a-1b_1-5_6*=O_6*OC]/1-2/a2-b1";

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
		double mass;
		try {
			mass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
			System.out.println(mass);
		} catch (WURCSMassException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
