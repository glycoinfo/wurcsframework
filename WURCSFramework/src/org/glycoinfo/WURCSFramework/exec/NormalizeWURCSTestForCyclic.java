package org.glycoinfo.WURCSFramework.exec;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;

public class NormalizeWURCSTestForCyclic {

	public static void main(String[] args) {
		LinkedList<String> t_aWURCSs = new LinkedList<String>();
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5_3-6][a2122h-1a_1-5]/1-2-1-2-1-1/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5][a2122h-1a_1-5_3-6]/1-2-1-2-2-2/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5_3-6][a2122h-1a_1-5]/1-2-1-1-1-2/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5][a2122h-1a_1-5_3-6]/1-2-2-2-1-2/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5_3-6][a2122h-1a_1-5]/1-1-1-2-1-2/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/2,6,6/[a2122h-1a_1-5_3-6][a2122h-1a_1-5]/1-1-2-1-2-1/a1-f4_a4-b1_b4-c1_c4-d1_d4-e1_e4-f1");
		t_aWURCSs.addLast("WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");

		try {
			for ( String t_strWURCS : t_aWURCSs ) {
				System.out.println("Input:  "+t_strWURCS);
				WURCSFactory t_oFactory = new WURCSFactory(t_strWURCS);
				t_strWURCS = t_oFactory.getWURCS();
				System.out.println("Output: "+t_strWURCS);
			}
		} catch (WURCSException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.err.println( e.getErrorMessage() );
		}

	}


}
