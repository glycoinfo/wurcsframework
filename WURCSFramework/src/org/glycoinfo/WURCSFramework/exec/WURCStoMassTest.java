package org.glycoinfo.WURCSFramework.exec;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;


public class WURCStoMassTest {

	public static void main(String[] args) {

		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-2-3-4-2-5-4-2-6-7/" +
				//"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\\c6_g1-c3\\c6";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c6*S*~10:100";

//		input = "WURCS=2.0/2,2,1/[aUd1122h][22122a-1a_1-5]/1-2/a4-b1";
//		input = "WURCS=2.0/3,4,3/[h2122h][12122h-1b_1-5][12122h-1b_1-5_2*NCC/3=O]/1-2-2-3/a3-b1_b3-c1_c4-d1";
//		input = "WURCS=2.0/2,2,1/[h2122h][12122h-1b_1-5]/1-2/a2-b1";
//		input = "WURCS=2.0/2,2,1/[a2122h-1x_1-5][a2122a-1b_1-5_6*=O_6*OC]/1-2/a2-b1";
//		input = "WURCS=2.0/2,7,0+/[uxxxxh_2*NCC/3=O][axxxxh-1x_1-5]/1-1-1-2-2-2-2/";

		LinkedList<String> t_aWURCSList = new LinkedList<String>();
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[o2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1x_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hU122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hO122h_2*=N]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[ha122h-2x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/2,2,1/[h2122h][12122h-1b_1-5]/1-2/a2-b1");
		t_aWURCSList.add("WURCS=2.0/3,4,3/[h2122h][12122h-1b_1-5][12122h-1b_1-5_2*NCC/3=O]/1-2-2-3/a3-b1_b3-c1_c4-d1");
// removed by muller this makes a error
//		t_aWURCSList.add("WURCS=2.0/3,14,0+/[uxxxxh_?*][uxxxxh_2*NCC/3=O][uxxxxh]/1-2-2-2-2-2-2-3-3-3-3-3-3-3/");
// end of muller
		t_aWURCSList.add("WURCS=2.0/3,3,2/[a2221m-1a_1-5][a2211m-1a_1-5][a2122h-1b_1-5_4n2-6n1*1OC^RO*2/3CO/6=O/3C_3*OC]/1-2-3/a2-b1_b3-c1");
		t_aWURCSList.add("WURCS=2.0/5,6,5/[a2112h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5][a2112h-1b_1-5_3*OSO/3=O/3=O]/1-2-3-4-2-5/a3-b1_a6-e1_b3-c1_c2-d1_e4-f1");
		// G00423RQ
		t_aWURCSList.add("WURCS=2.0/4,4,3/[a2121A-1x_1-5_2*OSO/3=O/3=O][a2122h-1a_1-5_2*NSO/3=O/3=O_6*OSO/3=O/3=O][a2121A-1a_1-5_2*OSO/3=O/3=O][a2122h-1a_1-5_2*NSO/3=O/3=O]/1-2-3-4/a4-b1_b4-c1_c4-d1");
		t_aWURCSList.add("WURCS=2.0/2,2,1/[a2121A-1x_1-5_2*OSO/3=O/3=O][a2122h-1a_1-5_2*NSO/3=O/3=O_6*OSO/3=O/3=O]/1-2/a4-b1");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2121A-1x_1-5_2*OSO/3=O/3=O]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2121h-1x_1-5_2*OSO/3=O/3=O]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1a_1-5_2*NSO/3=O/3=O_6*OSO/3=O/3=O]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1a_1-5_2*NSO/3=O/3=O]/1/");
		// G00026MO
		t_aWURCSList.add("WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
		// add test by muller 181206
				// G01753LJ
				t_aWURCSList.add("WURCS=2.0/2,2,1/[h2112h_2*NCC/3=O][a2112h-1a_1-5_2*NCC/3=O]/1-2/a6-b1");
		// end of muller

		try {
			for ( String t_strWURCS : t_aWURCSList ) {
				System.out.println(t_strWURCS);
				WURCSImporter t_objImporter = new WURCSImporter();
				WURCSArray t_objWURCS = t_objImporter.extractWURCSArray(t_strWURCS);
				// Calcurate mass from WURCS
				System.out.println(WURCSMassCalculator.calcMassWURCS(t_objWURCS));
			}
		} catch (WURCSFormatException e) {
			e.printStackTrace();
		} catch (WURCSMassException e) {
			e.printStackTrace();
		}

		LinkedList<String> testMAPs = new LinkedList<String>();
		testMAPs.add("*OPO/3O/3=O");
		testMAPs.add("*NCC/3=O");
		testMAPs.add("*O");
		testMAPs.add("*=N");
		testMAPs.add("*=O");
		testMAPs.add("*O*");
		testMAPs.add("O");
		testMAPs.add("HOH");
		testMAPs.add("*NSO/3=O/3=O");
		double testMass = 0;
		for ( String MAP : testMAPs ) {
			testMass = WURCSMassCalculator.getMassMAP(MAP).doubleValue();
			System.out.println(MAP + " : " + testMass);
		}
		testMass = WURCSMassCalculator.getMassSkeletonCode("u2122h").doubleValue();
		System.out.println("u2122h:"+testMass);
		testMass = WURCSMassCalculator.getMassSkeletonCode("a2122h").doubleValue();
		System.out.println("a2122h:"+testMass);
		System.out.println();
	}

}
