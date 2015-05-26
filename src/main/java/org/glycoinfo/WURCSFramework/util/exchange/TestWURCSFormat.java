package org.glycoinfo.WURCSFramework.util.exchange;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorExpandRepeatingUnit;
import org.glycoinfo.WURCSFramework.util.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class TestWURCSFormat {
	public static void main(String[] args) {

		String input = "WURCS=2.0/7,10,10/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2%?%*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" +
				"1-<2-3-4-2-5>-4-2-6-7/" +
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4%.1:.9%-f1_g2-h1_h4-i1%?%_d1-c3|c6_g1-c3|c6_b1-f4~n:100";

		// GlycomeDB: 12241
		input = "WURCS=2.0/3,12,16/[1212h-1b_1-5][12112h-1b_1-5][1211h-1a_1-4]/1-2-2-2-2-2-2-2-2-3-3-2>/a4-b1_b3-d1_b6-c1_d3-h1_d6-e1_e3-f1_f3-g1_h3-l1_h6-i1_i6-j1_j3-k1_a1-a4~n_a1-l3~n_b1-b3~n_d1-d3~n_h1-h3~n";
		// GlycomeDB: 35232
		input = "WURCS=2.0/13,14,13/[aUd21122h][211221h-1a_1-5][211221h-1a_1-5_4*OPO/3O/3=O][22122a-1a_1-5][22122h-1a_1-5_2*N][12122h-1b_1-5][12112m-1b_1-5_2*NCC/3=O_4*N][11122a-1b_1-5_2*NCC/3=O_3*NCC/3=O][22112h-1a_1-5_2*NCC/3=O][12122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][11122A-1b_1-5_2*NCC/3=O_3*NCC/3=O_6*=O_6*N][112eEH-1b_1-5_2*OCC/3=O_3*OCC/3=O_6*N][22112a-1a_1-5_2*N]/1-2-3-4-5-6-5-7-8-9-10-11-12-13/a5-b1_b3-c1_b4-f1_c2-d1_c7-e1_f4-g1_f6-n1_g6-h1_h3-i1_i4-j1_j4-k1_k4-l1_l4-m1";
		// GlycomeDB: 29873
		input = "WURCS=2.0/7,14,13/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][22112h-1a_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-5-6-4-2-5-2-5-7-7/a4-b1_b4-c1_c3-d1_c6-h1_d2-e1_e4-f1_f3-g1_h2-i1_i4-j1_k4-l1_m8-n2_m2-l3|l6_k1-d4|d6|h4|h6}";
		// GlycomeDB: 4385
		input = "WURCS=2.0/2,4,4/[22122h-1a_1-5][h5122h-2b_2-5]/1-1-2-2/a1-b1_b6-c2_c1-d2_c1-c2~n";
		// GlycomeDB: none , GlyTouCan: G00997BI
		input = "WURCS=2.0/4,7,6/[h2112h][12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5]/1-2-3-4-2-3-4/a?-b1_a?-e1_b?-c1_b?-d1_e?-f1_e?-g1";
		// GlycomeDB: 26448
		input = "WURCS=2.0/2,2,2/[<0>-?b][a6d21121m-2a_2-6_5*NCC/3=O_7*NCC/3=O]/1-2/a3-b2_a1-b4~n";
//		input = "WURCS=2.0/2,5,4/[hxh][22122h-1a_1-5]/1-2-2-1-1/a3-b1_b2-c1_b6-e3*OP^XO*/3O/3=O_c6-d3*OP^XO*/3O/3=O";
		// GlycomeDB: 33783
//		input = "WURCS=2.0/2,4,5/[12122h-1b_1-5][hxh]/1-1-1-2/a1-c2_a2-b1_b2-c1_c6-d1*OP^XO*/3O/3=O_b1-b2~n";
		// GlycomeDB: 7018
		input = "WURCS=2.0/1,3,3/[2212h-1a_1-4]/1-1-1/a3-b5_b3-c5_b3-b5~n";
		// GlyTouCan: G07481OW:
		input = "WURCS=2.0/5,6,5/[hxh][h2122h][12112h-1b_1-5][12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5]/1-2-3-4-5-3/a3n2-b1n1*ONCCOP^XO*/7O/7=O_b4-c1_c3-d1_d3-e1_d4-f1";
		// GlyTouCan: G00605LS: Anomeric linkage :
		input = "WURCS=2.0/2,5,4/[22122h-1a_1-5][22112h-1a_1-5]/1-2-2-2-1/a1-b1_b6-c1_c6-d1_d6-e1";
		// GlyTouCan: G36259OZ: Anomeric linkage :
		input = "WURCS=2.0/2,2,1/[22122h-1a_1-5][22122h-1a_1-5_6*OPO/3O/3=O]/1-2/a1-b1";
		// GlyTouCan: G22652VR:
		input = "WURCS=2.0/5,5,5/[22122a-1a_1-5][112112h-1a_1-5_6*OC][12112h-1b_1-4_2*NCC/3=O][1222h-1b_1-4][hxh]/1-2-3-4-5/a3-b1_a4-c1_a6-e2*N*_c5-d1_a1-d2~n";
		// GlyTouCan: G09646QM:
		input = "WURCS=2.0/1,12,11/[h222h]/1-1-1-1-1-1-1-1-1-1-1-1/a5-b1*OP^XO*/3O/3=O_b5-c1*OP^XO*/3O/3=O_c5-d1*OP^XO*/3O/3=O_d5-e1*OP^XO*/3O/3=O_e5-f1*OP^XO*/3O/3=O_f5-g1*OP^XO*/3O/3=O_g5-h1*OP^XO*/3O/3=O_h5-i1*OP^XO*/3O/3=O_i5-j1*OP^XO*/3O/3=O_j5-k1*OP^XO*/3O/3=O_k5-l1*OP^XO*/3O/3=O";
		// GlyTouCan: G94434RV:
//		input = "WURCS=2.0/2,2,2/[<0>-?b][<0>-?a]/1-2/a7-b1_a1-b7~n";


		WURCSImporter t_objImporter = new WURCSImporter();
		try {
			// Import WURCS without error messages
			WURCSArray t_objWURCS = t_objImporter.extractWURCSArray(input.substring(input.indexOf("WURCS=")));
			WURCSArrayToGraph t_objToGraph = new WURCSArrayToGraph();
			t_objToGraph.start(t_objWURCS);
			WURCSGraph t_objGraph = t_objToGraph.getGraph();

			WURCSVisitorExpandRepeatingUnit t_oDevelRep = new WURCSVisitorExpandRepeatingUnit();
			t_oDevelRep.start(t_objGraph);

			for ( Backbone t_oB : t_objGraph.getRootBackbones() ) {
				System.out.println( t_objGraph.getBackbones().indexOf(t_oB)+":"+t_oB.getSkeletonCode());
			}
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
			// Mass calc
			System.out.println( WURCSMassCalculator.calcMassWURCS(t_objWURCS) );

		} catch (WURCSFormatException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		} catch (WURCSException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		} catch (WURCSMassException e) {
			System.err.println( e.getErrorMessage() );
			e.printStackTrace();
		}

	}
}
