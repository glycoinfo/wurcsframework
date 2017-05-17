package org.glycoinfo.WURCSFramework.exec;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.WURCSValidation;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;

public class WURCSFrameworkUsage {

	public static void main(String[] args) {

		String t_strWURCSInput = "WURCS=2.0/2,2,1/[a2122h-1b_1-5][a2112h-1b_1-5]/1-2/b1-a4"; // b-Lac: b-D-Gal(1-4)b-D-Glcp

		// Validate WURCS
		System.out.println("--Validate WURCS string");
		System.out.println("Input WURCS string:\t"+t_strWURCSInput);
		WURCSValidation t_oValidation = new WURCSValidation();
		t_oValidation.start(t_strWURCSInput);
		System.out.println("Errors:");
		for ( String t_oError : t_oValidation.getErrors() ) {
			System.out.println("\t"+t_oError);
		}
		System.out.println("Warnings:");
		for ( String t_oWarning : t_oValidation.getWarnings() ) {
			System.out.println("\t"+t_oWarning);
		}
		System.out.println();

		// Import and export WURCS string
		try {
			System.out.println("--Import and export WURCS string");
			System.out.println("Input WURCS string:\t"+t_strWURCSInput);
			WURCSFactory t_oFactory = new WURCSFactory(t_strWURCSInput);
			String t_strWURCSOutput = t_oFactory.getWURCS();
			System.out.println("Output WURCS string:\t"+t_strWURCSOutput);
		} catch (WURCSException e) {
			e.printStackTrace();
		}

		// Import WURCS string and get WURCS object
		try {
			WURCSFactory t_oFactory = new WURCSFactory(t_strWURCSInput);
			WURCSArray t_oArray = t_oFactory.getArray();
			WURCSGraph t_oGraph = t_oFactory.getGraph();
			WURCSSequence2 t_oSeq2 = t_oFactory.getSequence();
		} catch (WURCSException e) {
			e.printStackTrace();
		}

		t_strWURCSInput = "WURCS=2.0/6,11,10/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-4-2-5-4-2-6-2-5/a4-b1_a6-i1_b4-c1_c3-d1_c6-g1_d2-e1_e4-f1_g2-h1_j4-k1_j1-d4|d6|g4|g6}";
		// Use WURCS object
		try {
			System.out.println("--Test WURCS object");
			System.out.println("Input WURCS string:\t"+t_strWURCSInput);
			WURCSFactory t_oFactory = new WURCSFactory(t_strWURCSInput);
			WURCSArray t_oArray = t_oFactory.getArray();
			WURCSGraph t_oGraph = t_oFactory.getGraph();
			WURCSSequence2 t_oSeq2 = t_oFactory.getSequence();

			// Constract WURCSFactory using copied graph
			WURCSFactory t_oFactory2 = new WURCSFactory(t_oGraph.copy());
			String t_strWURCSCopy = t_oFactory2.getWURCS();
			System.out.println("Copied WURCS string:\t"+t_strWURCSCopy);
		} catch (WURCSException e) {
			e.printStackTrace();
		}

	}

}
