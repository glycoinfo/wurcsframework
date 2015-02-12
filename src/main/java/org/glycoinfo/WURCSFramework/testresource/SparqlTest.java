package org.glycoinfo.WURCSFramework.testresource;

import org.glycoinfo.WURCSFramework.util.rdf.SearchSparql;
import org.glycoinfo.WURCSFramework.util.rdf.SearchSparqlBean;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSrdfSPARQLGLIPS_ESM;

//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;

public class SparqlTest {

	public static void main(String[] args) throws Exception {

		// for demonstrate string
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/"
				+ "1-2-3-4-2-5-4-2-6-7/"
				+ "a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";

		// input =
		// "src/org/glycoinfo/WURCSFramework/testresource/20150121result-GlycomeDB_GlycoCTmfWURCS.txt";
		String file_WURCS = "20150123result-GlyTouCan_GlycoCTmfWURCS.txt";
		input = "src/org/glycoinfo/WURCSFramework/testresource/" + file_WURCS;

		// GlyTouCan:G97690MS
		// input =
		// "62	WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2-b1_b6-c2_c1-d2";

		// input =
		// "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2|a4-b1_b6-c2_c1-d2~1-100";
		input = "WURCS=2.0/2,4,3/[h5122h-2b_2-5_2*NCC/3=O][22122h-1a_1-5]/1-2-1-1/a2-{c4|b1_b6-c2u1*Se*_c1-d2*S*~10:n";

		// 954
		// WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1
		// input =
		// "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1";

		// G00054MO
		input = "WURCS=2.0/4,4,3/[12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4/a3-b1_a4-c1_c3-d2";

		// GlycoRDF - Structures No.5 Bi-antennary with two unknown types of
		// SLewis
		// input =
		// "WURCS=2.0/6,17,16/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O][11221m-1a_1-5]/1-1-2-3-1-4-1-4-5-6-3-1-4-1-4-5-6/a4-b1_b4-c1_c3-d1_c6-k1_d2-e1_e4-f1_f4-g1_g4-j1_h3-i2_k2-l1_l4-m1_m4-n1_n4-q1_o3-p2_h1-g3|g4_o1-n3|n4";

		// GlycoRDF - Structures No.2 Sialic acid linkage
		// input =
		// "WURCS=2.0/5,12,11/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-1-2-3-1-4-5-1-3-1-4-5/a4-b1_b4-c1_c3-d1_c4-h1_c6-i1_d2-e1_e4-f1_f6-g2_i2-j1_j4-k1_l2-k3|k6";

		// GlycoRDF - Structures No.3 3)Positioning of third GlcNAc
		// (tri-antennary)
		// input =
		// "WURCS=2.0/3,9,8/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-1-2-3-1-1-3-1-1/""a4-b1_b4-c1_c3-d1_c4-f1_c6-g1_d2-e1_g2-h1_i1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6";

		// GlycoRDF - Structures No.4 4)Tetra-antennary structure with unknown
		// positioning of third and fourth GlcNAc with Gal
		// input =
		// "WURCS=2.0/5,12,11/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][x2112h-1x_1-5]/1-1-2-3-1-4-3-1-4-1-5-1/a4-b1_b4-c1_c3-d1_c6-g1_d2-e1_e4-f1_g2-h1_h4-i1_j1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6|i4|i6_l1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6|i4|i6_j?-k1";

		// fazzy sample
		// input =
		// "WURCS=2.0/4,4,3/[12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4/a3|a4-b1_a2|a3|a4-c1_c3-d2~n";

		// G19577NS
		// input =
		// "WURCS=2.0/5,7,7/[x2122h-1x_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][12122h-1b_1-5_2*NCC/3=O][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-3-5/a3-b1_a4-c1_c3-d1_d3-e1_d4-f1_f3-g2_d1-f3~n";

		// FuzzyGLIP test sample
		// glytoucan.org: G09117NC
		// input =
		// "WURCS=2.0/4,8,7/[12122h-1b_1-5_2*NCC/3=O][22112m-1a_1-5][11122h-1b_1-5][21122h-1a_1-5]/1-2-1-3-4-4-2-1/a3-b1_a4-c1_a6-g1_c4-d1_d3-e1_d6-f1_h1-e2|e4|e6";

		// ESM, SSM, FSM, PM
		String m_strSearchOption = "ESM";
		WURCSrdfSPARQLGLIPS_ESM sql = new WURCSrdfSPARQLGLIPS_ESM();
		// String m_strSearchOption = "SSM";
		// WURCSrdfSPARQLGLIPS_SSM sql = new WURCSrdfSPARQLGLIPS_SSM();
		// String m_strSearchOption = "FSM or PM";
		// WURCSrdfSPARQLGLIPS sql = new WURCSrdfSPARQLGLIPS();

		int i_SPARQLtestID = 4;

		switch (i_SPARQLtestID) {
		case 1:
			input = "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1";
			break;
		case 2:
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1";
			break;
		case 3:
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1";
			break;
		case 4:
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a3|a6";
			break;
		case 5:
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a4|a6";
			break;
		case 6:
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a3|a4|a6";
			break;
		case 7:
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4-b1|c1";
			break;
		case 8:
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a4|a6-b1|c1";
			break;
		case 9:
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a6-b1|c1";
			break;
		case 10:
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4|a6-b1|c1";
			break;
		}
		// TODO:

		SearchSparql sparql = new SearchSparqlBean();
		sparql.setGlycoSequenceVariable("gseq");
		String output = sparql.getExactWhere(input);
		System.out.println(output);
	}

}
