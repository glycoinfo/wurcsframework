package org.glycoinfo.WURCSFramework.util.mass;

import java.io.File;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;


public class WURCStoMassTest {

	public static void main(String[] args) throws Exception {
		//for demonstrate string
		//WURCS=2.0/5,4/[22112h+1:a|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12112h+1:b|1,5][12122h+1:b|1,5|2*NCC/3=O][12112h+1:b|1,5]1+3,2+1|3+1,(2+3)\(2+4)|1+6,4+1|4+4,5+1
		//WURCS=2.0/2,2/[22122h+1:a|1,5|2*NCC/3=O][12122a+1:b|1,5]1+4,2+1|1+1,2+4~n
		//WURCS=2.0/7,6/[u2122h+?:x|?,?][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5]1+6,2+1|2+3,3+1|2+6,4+1|4+6,5+1|5+3,6+1|5+6,7+1
		//WURCS=2.0/6,5/[12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5]1+3,2+1|1+6,3+1|3+6,4+1|4+3,5+1|4+6,6+1
		//WURCS=2.0/9,8/[12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][11122h+1:b|1,5][21122h+1:a|1,5][12122h+1:b|1,5|2*NCC/3=O][12112h+1:b|1,5|2*NCC/3=O|3\4*OSO/3=O/3=O][21122h+1:a|1,5][12122h+1:b|1,5|2*NCC/3=O][12112h+1:b|1,5|2*NCC/3=O|3\4*OSO/3=O/3=O]1+4,2+1|2+4,3+1|3+3,4+1|4+2,5+1|5+4,6+1|3+6,7+1|7+3,8+1|8+4,9+1

		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
				//"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\\c6_g1-c3\\c6";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c6*S*~10-100";
		if(input == null || input.equals("")) throw new Exception();

		WURCSArray t_objWURCS = new WURCSArray("2.0", 0, 0, 0);
		WURCSImporter ws = new WURCSImporter();
		File file = new File(input);

		if(file.isFile()) {
			//File open
		}else if(input.indexOf("WURCS") != -1) {
			t_objWURCS = ws.WURCSsepalator(input.substring(input.indexOf("W")));
		}else {
			throw new Exception("File not found");
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
		double mass = WURCSMassCalculator.getMassWURCS(t_objWURCS);
		System.out.println(mass);

	}

}
