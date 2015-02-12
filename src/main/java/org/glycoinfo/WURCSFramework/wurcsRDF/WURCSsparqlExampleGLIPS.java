package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSrdfSPARQLGLIPS;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSrdfSPARQLGLIPS_ESM;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSrdfSPARQLGLIPS_SSM_TBD;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;

public class WURCSsparqlExampleGLIPS {

	public static void main(String[] args) throws Exception {
		
		//for demonstrate string
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" + 
				"1-2-3-4-2-5-4-2-6-7/" + 
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";

		//input = "src/org/glycoinfo/WURCSFramework/testresource/20150121result-GlycomeDB_GlycoCTmfWURCS.txt";
		String file_WURCS = "20150123result-GlyTouCan_GlycoCTmfWURCS.txt";
		input = "src/org/glycoinfo/WURCSFramework/testresource/" + file_WURCS;
		
		// GlyTouCan:G97690MS
		//input = "62	WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2-b1_b6-c2_c1-d2";
		
//		input = "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2|a4-b1_b6-c2_c1-d2~1-100";
		input = "WURCS=2.0/2,4,3/[h5122h-2b_2-5_2*NCC/3=O][22122h-1a_1-5]/1-2-1-1/a2-{c4|b1_b6-c2u1*Se*_c1-d2*S*~10:n";
		
		//954	WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1
		//input = "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1";
		
		// G00054MO
		input = "WURCS=2.0/4,4,3/[12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4/a3-b1_a4-c1_c3-d2";
		
		// GlycoRDF - Structures No.5 Bi-antennary with two unknown types of SLewis
		//input = "WURCS=2.0/6,17,16/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O][11221m-1a_1-5]/1-1-2-3-1-4-1-4-5-6-3-1-4-1-4-5-6/a4-b1_b4-c1_c3-d1_c6-k1_d2-e1_e4-f1_f4-g1_g4-j1_h3-i2_k2-l1_l4-m1_m4-n1_n4-q1_o3-p2_h1-g3|g4_o1-n3|n4";
		
		// GlycoRDF - Structures No.2 Sialic acid linkage
//		input = "WURCS=2.0/5,12,11/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-1-2-3-1-4-5-1-3-1-4-5/a4-b1_b4-c1_c3-d1_c4-h1_c6-i1_d2-e1_e4-f1_f6-g2_i2-j1_j4-k1_l2-k3|k6";
		
		// GlycoRDF - Structures No.3 3)Positioning of third GlcNAc (tri-antennary)
//		input = "WURCS=2.0/3,9,8/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-1-2-3-1-1-3-1-1/""a4-b1_b4-c1_c3-d1_c4-f1_c6-g1_d2-e1_g2-h1_i1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6";
		
		// GlycoRDF - Structures No.4 4)Tetra-antennary structure with unknown positioning of third and fourth GlcNAc with Gal 
		//input = "WURCS=2.0/5,12,11/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][x2112h-1x_1-5]/1-1-2-3-1-4-3-1-4-1-5-1/a4-b1_b4-c1_c3-d1_c6-g1_d2-e1_e4-f1_g2-h1_h4-i1_j1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6|i4|i6_l1-a4|a6|b4|b6|c4|c6|d4|d6|e4|e6|f4|f6|g4|g6|h4|h6|i4|i6_j?-k1";
			
		// fazzy sample
		//input = "WURCS=2.0/4,4,3/[12122h-1b_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4/a3|a4-b1_a2|a3|a4-c1_c3-d2~n";
		
		// G19577NS
		input = "WURCS=2.0/5,7,7/[x2122h-1x_1-5_2*NCC/3=O][11221m-1a_1-5][12112h-1b_1-5][12122h-1b_1-5_2*NCC/3=O][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-3-5/a3-b1_a4-c1_c3-d1_d3-e1_d4-f1_f3-g2_d1-f3~n";
		
		
		// FuzzyGLIP test sample
		// glytoucan.org: G09117NC
//		input = "WURCS=2.0/4,8,7/[12122h-1b_1-5_2*NCC/3=O][22112m-1a_1-5][11122h-1b_1-5][21122h-1a_1-5]/1-2-1-3-4-4-2-1/a3-b1_a4-c1_a6-g1_c4-d1_d3-e1_d6-f1_h1-e2|e4|e6";

//		// ESM, SSM, FSM, PM
//		String m_strSearchOption = "ESM";
//		WURCSrdfSPARQLGLIPS_ESM sql = new WURCSrdfSPARQLGLIPS_ESM();
		String m_strSearchOption = "SSM";
		WURCSrdfSPARQLGLIPS_SSM_TBD sql = new WURCSrdfSPARQLGLIPS_SSM_TBD();
//		String m_strSearchOption = "FSM or PM";
//		WURCSrdfSPARQLGLIPS sql = new WURCSrdfSPARQLGLIPS();

		int i_SPARQLtestID =10;
		
		switch (i_SPARQLtestID) {
		case 1 :
			input = "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1";
			break;
		case 2 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1";
			break;
		case 3 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1";
			break;
		case 4 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a3|a6";
			break;
		case 5 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a4|a6";
			break;
		case 6 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a3|a4|a6";
			break;
		case 7 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4-b1|c1";
			break;
		case 8 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a4|a6-b1|c1";
			break;
		case 9 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a6-b1|c1";
			break;
		case 10 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4|a6-b1|c1";
			break;
		}
		// TODO:
		
		
		// set search option
		LinkedList<String> t_aOption = new LinkedList<String>();
//		t_aOption.add("exact");
		t_aOption.add("uri");
		t_aOption.add("wurcs");
		t_aOption.add("LIMIT 100");
		t_aOption.add("FROM <http://www.glycoinfo.org/graph/wurcs/0.4.1>");
//		t_aOption.add("accession_number");
		t_aOption.add(m_strSearchOption);
		t_aOption.add("TEST_ID:" + String.valueOf(i_SPARQLtestID));
		// Search Type
		// ESM		Exact Substructure match
		// SSM		Superstructure match
		// FSM		FuzzySubstructure match
		// PM		Partial match (For fuzzy query)

		
		if(input == null || input.equals("")) throw new Exception();
		
		TreeMap<String, String> wurcsIndex = new TreeMap<String, String>();
//		WURCSImporter ws = new WURCSImporter();
		WURCSImporter ws = new WURCSImporter();
		
//		WURCSrdfSPARQL sql = new WURCSrdfSPARQL();
		
		Boolean t_bPrefix = true;
				
		File file = new File(input);
		
		if(file.isFile()) {
			wurcsIndex = openString(input);
			try{
				
				StringBuilder sb = new StringBuilder();
					for(String key : wurcsIndex.keySet()) {

						WURCSArray wurcs = ws.extractWURCSArray(wurcsIndex.get(key));
						String AccessionNumber = key;
						java.lang.System.out.println(key);						
						sb.append(sql.getSPARQL(wurcsIndex.get(key), t_aOption));
					}				
				File savefile = new File("../../../testdata/WURCS-RDF-SPARQL.sql");
				
				try{
					savefile.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}

				if(savefile.isFile()) {
					FileWriter filewriter = new FileWriter(savefile);
					filewriter.write(sb.toString());
					filewriter.close();
				}				
			}catch(IOException e){
				System.out.println(e);
			}
		}else if(input.indexOf("WURCS") != -1) {
			WURCSArray wurcs = ws.extractWURCSArray(input.substring(input.indexOf("W")));
			String AccessionNumber = "GxxxxxMS";
			StringBuilder sb = new StringBuilder(); // 1 min 15 sec
			sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption));
		
			try{
				File savefile = new File("../../../testdata/WURCS-RDF-SPARQL.sql");
				try{
					savefile.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}
				if(savefile.isFile()) {
					FileWriter filewriter = new FileWriter(savefile);
					filewriter.write(sb.toString());
					filewriter.close();
				}
			}catch(IOException e){
				System.out.println(e);
			}			
		}else {
			throw new Exception("This file is not found");
		}
	}

	//input WURCS string file
	public static TreeMap<String, String> openString(String a_strFile) throws Exception {
		try {
			return readWURCS(new BufferedReader(new FileReader(a_strFile)));
    	}catch (IOException e) {
    		throw new Exception();
    	}
	}
	
	public static TreeMap<String, String> readWURCS(BufferedReader a_bfFile) throws IOException {
		String line = "";
		TreeMap<String, String> wret = new TreeMap<String, String>();
		wret.clear();
		
		while((line = a_bfFile.readLine()) != null) {
			line.trim();
			if(line.indexOf("WURCS") != -1) {
				
				String[] IDandWURCS  = line.split("\t");
				if (IDandWURCS.length == 2) {
					wret.put(IDandWURCS[0].trim(), IDandWURCS[1]);
				}
			}
		}
		a_bfFile.close();
		
		return wret;
	}
}
