package org.glycoinfo.WURCSFramework.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.property.chemical.WURCSGraphChemicalCompositionCalculator;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;
import org.junit.Test;

public class TestChemicalCompositionCalculator {

	@Test
	public void testCalculationWURCS() throws WURCSException {
		String t_strWURCS = "";
		WURCSFactory t_oFactory = new WURCSFactory(t_strWURCS);
		WURCSGraph t_oGraph = t_oFactory.getGraph();
		WURCSGraphChemicalCompositionCalculator t_oGraphCCC = new WURCSGraphChemicalCompositionCalculator(t_oGraph);
		t_oGraphCCC.start();

		fail("まだ実装されていません");
	}

	@Test
	public void testCalculationWURCSToCompositionString() throws WURCSException {
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
		t_aWURCSList.add("WURCS=2.0/3,14,0+/[uxxxxh_?*][uxxxxh_2*NCC/3=O][uxxxxh]/1-2-2-2-2-2-2-3-3-3-3-3-3-3/");
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

		LinkedList<String> t_aCompositions = new LinkedList<String>();
		for ( String t_strWURCS : t_aWURCSList ) {
			WURCSFactory t_oFactory = new WURCSFactory(t_strWURCS);
			WURCSGraph t_oGraph = t_oFactory.getGraph();
			WURCSGraphChemicalCompositionCalculator t_oGraphCCC = new WURCSGraphChemicalCompositionCalculator(t_oGraph);
			t_oGraphCCC.start();

			t_aCompositions.add( t_oGraphCCC.getComposition() );
			System.err.println( t_oGraphCCC.getComposition()+"\t"+t_strWURCS );
		}


	}
}
