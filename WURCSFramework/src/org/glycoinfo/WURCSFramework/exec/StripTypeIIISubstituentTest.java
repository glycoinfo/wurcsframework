package org.glycoinfo.WURCSFramework.exec;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorStripTypeIIIModification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class StripTypeIIISubstituentTest {

	public static void main(String[] args) {

		LinkedList<String> t_aWURCSList = new LinkedList<String>();
		t_aWURCSList.add( "WURCS=2.0/1,1,0/[a2122h-1b_1-5_2*NCC/3=O]/1/" );
		t_aWURCSList.add( "WURCS=2.0/7,12,14/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][axxxxh-1x_1-?_2*NCC/3=O][a2112h-1b_1-5][Aad21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-5-6-7-7-4-5-6-7/a4-b1_b4-c1_e4-f1_f3-g2_g8-h2_j4-k1_k3-l2_c?-d1_c?-i1_d?-e1_i?-j1_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O_a?|b?|c?|d?|e?|f?|g?|h?|i?|j?|k?|l?}*OCC/3=O" );
		t_aWURCSList.add( "WURCS=2.0/5,5,5/[a2211m-1b_1-5][a2112h-1b_1-5][a2211m-1a_1-5][o2h][a2122h-1b_1-5]/1-2-3-4-5/a4-b1_b2-c1_b3-d2*OPO*/3O/3=O_b4-e1_a1-e4~n" );
		t_aWURCSList.add( "WURCS=2.0/4,4,4/[a2211m-1b_1-5_2*OCC/3=O_3*OPO/3O/3=O][a2122h-1a_1-5_4*OCC/3=O][a2211m-1a_1-5][a2122h-1b_1-5]/1-2-3-4/a4-b1_b2-c1*OPO*/3O/3=O_b3-d1_a1-d4~n" );

		WURCSVisitorStripTypeIIIModification t_oStripper = new WURCSVisitorStripTypeIIIModification();
		for ( String t_strWURCS : t_aWURCSList ) {
			System.out.println( "WURCS: "+t_strWURCS );
			try {
				WURCSFactory t_oFactoryIn = new WURCSFactory(t_strWURCS);
				WURCSGraph t_oGraph = t_oFactoryIn.getGraph();

				t_oStripper.start(t_oGraph);
				WURCSGraph t_oStripped = t_oStripper.getStrippedGraph();
				WURCSFactory t_oFactoryOut = new WURCSFactory(t_oStripped);
				String t_strStripped = t_oFactoryOut.getWURCS();
				System.out.println( "Stripped: "+t_strStripped );

			} catch (WURCSException e) {
				e.printStackTrace();
				continue;
			}

		}

	}

}
