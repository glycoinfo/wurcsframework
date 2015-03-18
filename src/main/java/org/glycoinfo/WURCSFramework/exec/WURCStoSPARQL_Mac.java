package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.rdf.WURCSSPARQLUtils_TBD;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSPARQL_TBD;
//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;

public class WURCStoSPARQL_Mac {

	public static void main(String[] args) throws Exception {
		
		//for demonstrate string
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" + 
				"1-2-3-4-2-5-4-2-6-7/" + 
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";

		//input = "src/org/glycoinfo/WURCSFramework/testresource/20150121result-GlycomeDB_GlycoCTmfWURCS.txt";
		String file_WURCS = "20150302result-GlyTouCan_GlycoCTmfWURCS.txt";
		input = "src/org/glycoinfo/WURCSFramework/testresource/" + file_WURCS;
		
//		// G00030MO
		input = "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4";
		
		//G00012MO
		input = "WURCS=2.0/4,4,3/[u2122h][12112h-1b_1-5][22112h-1a_1-5][12112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1";
		
		String m_strSearchOption = WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM;
//		m_strSearchOption        = WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM; // SSM
		m_strSearchOption        = WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM;   // FSM
//		m_strSearchOption        = WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch;  // Exact Structure Search

		String m_strFromGraph = "FROM <http://rdf.glycoinfo.org/wurcs>";
		String m_strCount = "glycan";
		

		WURCSSPARQL_TBD sql = new WURCSSPARQL_TBD();

		int i_SPARQLtestID = -1;
		switch (i_SPARQLtestID) {
		case 1 :
			input = "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1";
			break;
		case 2 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1";
			break;
		case 3 :
			input = "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1";
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
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4-{b1|c1";
			break;
		case 8 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a4|a6-{b1|c1";
			break;
		case 9 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a6-{b1|c1";
			break;
		case 10 :
			input = "WURCS=2.0/2,3,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2-1/a3|a4|a6-{b1|c1";
			break;
		}
		// TODO:

		
		for (String a_str : args) {
			if(a_str.indexOf("WURCS=") != -1) {
				input = a_str;
			}
			if(a_str.indexOf(WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM) != -1) {
				m_strSearchOption = WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM;
			}
			if(a_str.indexOf(WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM) != -1) {
				m_strSearchOption = WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM;
			}
			if(a_str.indexOf(WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM) != -1) {
				m_strSearchOption = WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM;
			}
			if(a_str.indexOf(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch) != -1) {
				m_strSearchOption = WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch;
			}
			
			if (a_str.indexOf("FROM") != -1) {
				m_strFromGraph = a_str;
			}
			if (a_str.indexOf("count") != -1) {
				m_strCount = a_str;
			}
			//m_strCount

		}

		
		// set search option
		LinkedList<String> t_aOption = new LinkedList<String>();
//		t_aOption.add("exact");
		t_aOption.add("uri");
		t_aOption.add("wurcs");
		t_aOption.add("#  LIMIT 100");
		t_aOption.add(m_strFromGraph);
		t_aOption.add(m_strCount);
		t_aOption.add(m_strSearchOption);
//		t_aOption.add("TEST_ID:" + String.valueOf(i_SPARQLtestID));
		// Search Type
		// ESM		Exact Substructure match
		// SSM		Superstructure match
		// FSM		FuzzySubstructure match
		// PM		Partial match (For fuzzy query)

		
		if(input == null || input.equals("")) throw new Exception();
		
		TreeMap<String, String> wurcsIndex = new TreeMap<String, String>();
		
//		WURCSrdfSPARQL sql = new WURCSrdfSPARQL();
		
		Boolean t_bPrefix = true;
				
		File file = new File(input);
		
		if(file.isFile()) {
			wurcsIndex = openString(input);
			try{
				
				StringBuilder sb = new StringBuilder();
					for(String key : wurcsIndex.keySet()) {

						java.lang.System.out.println(key);
						
						if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM)) {
							sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM));
							//System.out.println(sql_e.getSPARQL(input.substring(input.indexOf("W")), t_aOption));
						}
						else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM)) {
							sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM));
						}
						else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM)) {
							sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM));
						}
						else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch)) {
							sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch));
						}
					}				
//				File savefile = new File("../../../testdata/WURCS-RDF-SPARQL.sql");
				
				try{
//					savefile.createNewFile();
				}catch(Exception e){
				    System.out.println(e);
				}

//				if(savefile.isFile()) {
//					FileWriter filewriter = new FileWriter(savefile);
//					filewriter.write(sb.toString());
//					filewriter.close();
//				}
				
				
			}catch(Exception e){
				System.out.println(e);
			}
		}else if(input.indexOf("WURCS") != -1) {
//			WURCSArray wurcs = ws.extractWURCSArray(input.substring(input.indexOf("W")));

			StringBuilder sb = new StringBuilder(); // 1 min 15 sec
			
			if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM)) {
				sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM));
				System.out.println(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM));
			}
			else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM)) {
				sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM));
				System.out.println(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM));
			}			
			else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM)) {
				sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM));
				System.out.println(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM));
			}
			else if (m_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch)) {
				sb.append(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch));
				System.out.println(sql.getSPARQL(input.substring(input.indexOf("W")), t_aOption, WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch));
			}


			try{
//				File savefile = new File("../../../testdata/WURCS-RDF-SPARQL.sql");
//				try{
//					savefile.createNewFile();
//				}catch(IOException e){
//				    System.out.println(e);
//				}
//				if(savefile.isFile()) {
//					FileWriter filewriter = new FileWriter(savefile);
//					filewriter.write(sb.toString());
//					filewriter.close();
//				}
			}catch(Exception e){
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
