package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSExporterRDF;


public class WURCStoRDF_Mac {

	public static void main(String[] args) throws Exception {
		
		Date day = new Date();
		long start = day.getTime();

		//for demonstrate string
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/" + 
				"1-2-3-4-2-5-4-2-6-7/" + 
			//	"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c3";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";

		//input = "src/org/glycoinfo/WURCSFramework/testresource/20150121result-GlycomeDB_GlycoCTmfWURCS.txt";
		String file_WURCS = "20150302result-GlyTouCan_GlycoCTmfWURCS.txt";
		
//		file_WURCS = "20150130_SPARQ-test1LWURCS.txt";
		
//		file_WURCS = "20150213-GlyTouCan_Motif.txt";
		
		
		input = "src"+ File.separator +"org"+ File.separator +"glycoinfo"+ File.separator +"WURCSFramework"+ File.separator +"testresource"+ File.separator  + file_WURCS;
		
//		G03690AY
//		input = "WURCS=2.0/2,2,2/[<0>-?b][a6d21121m-2a_2-6_5*NCC/3=O_7*NCC/3=O]/1-2/a3-b2_a1-b4~n";
//		G03693LO
		input = "WURCS=2.0/2,2,1/[u1122h][22112h-1a_1-5]/1-2/a2-b1";
				
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

		
//		System.out.println("input: " + input);
		
		
		if(input == null || input.equals("")) throw new Exception();
		
		TreeMap<String, String> wurcsIndex = new TreeMap<String, String>();
		// using WURCSImporter2
//		WURCSImporter ws = new WURCSImporter();
// WURCSImporterForGLIPs
		WURCSImporter ws = new WURCSImporter();
//		WURCSrdf rdf = new WURCSrdf();
		// WURCSrdfGLIPs
		WURCSExporterRDF rdf = new WURCSExporterRDF();
		Boolean t_bPrefix = true;
		
		
		
		File file = new File(input);
		
		if(file.isFile()) {
			wurcsIndex = openString(input);
			try{
				
				StringBuilder sb = new StringBuilder();
				StringBuilder sbMS = new StringBuilder();
					for(String key : wurcsIndex.keySet()) {
						WURCSArray wurcs = ws.extractWURCSArray(wurcsIndex.get(key));
						String AccessionNumber = key;
						java.lang.System.out.println(key);
						rdf.setWURCSrdfTriple(AccessionNumber, wurcs, t_bPrefix);
						sb.append(rdf.getWURCS_RDF());
						sbMS.append(rdf.getWURCS_monosaccharide_RDF());
					}
				
				File savefile = new File(".."+ File.separator +".."+ File.separator +".."+ File.separator +"testdata"+ File.separator +"WURCS-RDF.ttl");
				File savefileMS = new File(".."+ File.separator +".."+ File.separator +".."+ File.separator +"testdata"+ File.separator +"WURCS-MS_RDF.ttl");
				
				try{
					savefile.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}
				try{
					savefileMS.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}
				
				if(savefile.isFile()) {
					FileWriter filewriter = new FileWriter(savefile);
					filewriter.write(rdf.getWURCSPrefix());
					filewriter.write(sb.toString());
					filewriter.close();
				}
				if(savefileMS.isFile()) {
					FileWriter filewriterMS = new FileWriter(savefileMS);
					filewriterMS.write(rdf.getWURCSPrefix());
					filewriterMS.write(sbMS.toString());
					filewriterMS.close();
				}
				System.out.println("Fin...");
				day = new Date();
				long end = day.getTime();
				System.out.println("time:" + (end - start) + " m sec.");
				
			}catch(IOException e){
				System.out.println(e);
			}

		}else if(input.indexOf("WURCS") != -1) {
			WURCSArray wurcs = ws.extractWURCSArray(input.substring(input.indexOf("W")));
			String AccessionNumber = "GxxxxxMS";
//			rdf.setWURCSrdfTriple(AccessionNumber, wurcs);
			//java.lang.System.out.println("WURCS RDF:");
			//java.lang.System.out.println(wurcsRDFxml);
			StringBuilder sb = new StringBuilder(); // 1 min 15 sec
			StringBuilder sbMS = new StringBuilder(); // 1 min 15 sec
//			WURCSArray wurcs = ws.WURCSsepalator(wurcsIndex.get(key));
			

			rdf.setWURCSrdfTriple(AccessionNumber, wurcs, t_bPrefix);
			sb.append(rdf.getWURCS_RDF());
			sbMS.append(rdf.getWURCS_monosaccharide_RDF());

			
			try{
				//File savefile = new File("WURCS-RDF.ttl.txt");
				File savefile = new File(".."+ File.separator +".."+ File.separator +".."+ File.separator +"testdata"+ File.separator +"WURCS-RDF.ttl");
				File savefileMS = new File(".."+ File.separator +".."+ File.separator +".."+ File.separator +"testdata"+ File.separator +"WURCS-MS_RDF.ttl");

				try{
					savefile.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}
				try{
					savefileMS.createNewFile();
				}catch(IOException e){
				    System.out.println(e);
				}

				
				if(savefile.isFile()) {
					FileWriter filewriter = new FileWriter(savefile);
					filewriter.write(rdf.getWURCSPrefix());
					filewriter.write(sb.toString());
					filewriter.close();
				}
				if(savefileMS.isFile()) {
					FileWriter filewriterMS = new FileWriter(savefileMS);
					filewriterMS.write(rdf.getWURCSPrefix());
					filewriterMS.write(sbMS.toString());
					filewriterMS.close();
				}
				System.out.println("Fin...");
				day = new Date();
				long end = day.getTime();
				System.out.println("time:" + (end - start) + " m sec.");


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
