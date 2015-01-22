package org.glycoinfo.WURCSFramework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;


public class ReadWURCSExample2 {

	public static void main(String[] args) throws Exception {
		
		//for demonstrate string
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" + 
				"1-2-3-4-2-5-4-2-6-7/" + 
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";

		input = "src/org/glycoinfo/WURCSFramework/testresource/20150121result-GlycomeDB_GlycoCTmfWURCS.txt";
		
		//input = "WURCS=2.0/2,3,3/[u1122h][x1122h-?x_?-?]/1-2-2/a?-b1_b?-c1_b?-b1~n";
		
		// GlyTouCan:G97690MS
		//input = "62	WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2-b1_b6-c2_c1-d2";
		
//		input = "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2|a4-b1_b6-c2_c1-d2~1-100";
//		input = "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/a2|a4-b1_b6-c2u1*Se*_c1-d2*S*~10:n";
		
		//954	WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1
		//input = "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1";
		
		if(input == null || input.equals("")) throw new Exception();
		
		TreeMap<String, String> wurcsIndex = new TreeMap<String, String>();
		// using WURCSImporter2
		WURCSImporter2 ws = new WURCSImporter2();
		RDF rdf = new RDF();
		
		File file = new File(input);
		
		if(file.isFile()) {
			wurcsIndex = openString(input);
			try{
				
				StringBuilder sb = new StringBuilder(); // 1 min 15 sec
				//String t_strWURCSrdf = "";                // over 30 min
					for(String key : wurcsIndex.keySet()) {
						WURCSArray wurcs = ws.WURCSsepalator(wurcsIndex.get(key));
						String AccessionNumber = key;
						java.lang.System.out.println(key);
						String wurcsRDFxml = rdf.getRDFxml(AccessionNumber, wurcs);
						//java.lang.System.out.println("WURCS RDF:");
						//java.lang.System.out.println(wurcsRDFxml);
						//t_strWURCSrdf += wurcsRDFxml;
						sb.append(wurcsRDFxml);
					}
				
				File savefile = new File("../../../testdata/WURCS-RDF.ttl");
				
				if(file.isFile()) {
					//FileWriter filewriter = new FileWriter(savefile, true);
					FileWriter filewriter = new FileWriter(savefile);
					
					filewriter.write("@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n");
					filewriter.write("@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n");
					filewriter.write("@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
					filewriter.write("@prefix owl:   <http://www.w3.org/2002/07/owl#> .\n");
					filewriter.write("@prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .\n");
					filewriter.write("@prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .\n");
					filewriter.write("@prefix wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#> .\n");
					
					//filewriter.write(t_strWURCSrdf);
					filewriter.write(sb.toString());
					filewriter.close();
				}
			}catch(IOException e){
				System.out.println(e);
			}

		}else if(input.indexOf("WURCS") != -1) {
			WURCSArray wurcs = ws.WURCSsepalator(input.substring(input.indexOf("W")));
			String AccessionNumber = "GxxxxxMS";
			String wurcsRDFxml = rdf.getRDFxml(AccessionNumber, wurcs);
			//java.lang.System.out.println("WURCS RDF:");
			//java.lang.System.out.println(wurcsRDFxml);
			
			try{
				//File savefile = new File("WURCS-RDF.ttl.txt");
				File savefile = new File("../../../testdata/WURCS-RDF.ttl");
				if(savefile.isFile()) {
					FileWriter filewriter = new FileWriter(savefile);

					filewriter.write(wurcsRDFxml);

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