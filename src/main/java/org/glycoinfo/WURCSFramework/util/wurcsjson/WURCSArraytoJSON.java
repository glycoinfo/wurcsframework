package org.glycoinfo.WURCSFramework.util.wurcsjson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import org.json.simple.JSONArray;

/**
 * Class for test to convert Json string from WURCS
 * @author st
 *
 */
public class WURCSArraytoJSON {
	
	public static void main(String[] args) throws Exception{
		String file_directory = "";
		if(file_directory.equals("")) {
			//directory
			//file_directory = "/Users/st/git/wurcsframework/WURCSFramework/src/org/glycoinfo/WURCSFramework/testresource/";
			//file_directory += "20150302result-Glycoepitope_GlycoCTmfWURCS.txt";
			//file_directory += "20150302result-GlycomeDB_GlycoCTmfWURCS.txt";
		}
		//text
		//file_directory = "WURCS=2.0/8,19,18/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][11221m-1a_1-5][12112h-1b_1-5][a6d21122h-2a_2-6_5*NCCO/3=O][a6d21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-5-6-7-2-6-4-2-6-8-2-5-6-8-5/a4-b1_a6-s1_b4-c1_c3-d1_c6-k1_d2-e1_d4-i1_e3-f1_e4-g1_i4-j1_k2-l1_k6-o1_l4-m1_o3-p1_o4-q1_h2-g3|g6_n2-m3|m6_r2-q3|q6";
		//file_directory = "WURCS=2.0/4,5,5/[12122h-1b_1-5_2*NCC/3=O][22112h-1a_1-5][12211m-1a_1-5][22112m-1a_1-5_4*N]/1-2-3-3-4/a3-b1_b3-c1_c3-d1_d2-e1_a1-d3~n";
		
		//file_directory = "WURCS=2.0/6,11,10/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5][11221m-1a_1-5]/1-2-3-4-2-5-4-2-6-2-5/a4-b1_a6-i1_b4-c1_c3-d1_c6-g1_d2-e1_e4-f1_g2-h1_j4-k1_j1-d4|d6|g4|g6}";
		file_directory = "WURCS=2.0/5,5,5/[x1221m-1x_1-5][22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5_2*NCC/3=O][22112h-1a_1-5][22122h-1a_1-5]/1-2-3-4-5/a4-b1_b3-c1_b4-e1_c3-d1_d?-a1~n";
		
		//file_directory = "G00023MO	WURCS=2.0/3,4,3/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O_3*OCC^RC/4O/3=O][12122h-1b_1-5_2*NCC/3=O]/1-2-3-2/a4-b1_b4-c1_c4-d1";
		//file_directory = "WURCS=2.0/4,10,9/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][11221m-1a_1-5]/1-1-2-3-1-1-3-1-1-4/a4-b1_a6-j1_b4-c1_c3-d1_c6-g1_d2-e1_d4-f1_g2-h1_g6-i1";
		//file_directory = "00038	WURCS=2.0/2,3,2/[1d122m-1b_1-5][1d122m-1b_1-5_3*C]/1-1-2/a3-b1_b3-c1";
		//file_directory = "WURCS=2.0/3,4,3/[h5122h-2b_2-5][22122h-1a_1-5][22112h-1a_1-5]/1-2-3-3/a2-b1_b6-c1_c6-d1";
		//file_directory = "00582	WURCS=2.0/4,4,3/[a6d112dh-2a_2-6][211221h-1a_1-5][12122h-1b_1-5][211221h-1a_1-5_7*OPO/3O/3=O]/1-2-3-4/a4-b1_b3-c1_b4-d1";
		//file_directory = "WURCS=2.0/8,8,7/[x48h-1x_1-4_3*CO_3*O][33344m-1b_1-5][44334a-1a_1-5][34334a-1b_1-5][44334m-1a_1-5][4434h-1a_1-5_2*OC][34344a-1b_1-5][44334h-1a_1-5]/1-2-3-4-5-6-7-8/a3-b1_b2-c1_b3-d1_b4-e1_e3-f1_e4-g1_g2-h1";
		//file_directory = "15489	WURCS=2.0/2,3,3/[h2122h][22122h-1a_1-5]/1-2-2/a4-b1_b4-c1_b1-b4~3:7";
		//file_directory = "37356	WURCS=2.0/5,5,5/[22112h-1a_1-5][12211m-1a_1-5][21122h-1a_1-5][22d12m-1a_1-5][22122h-1a_1-5]/1-2-3-4-5/a3-b1_b4-c1_c3-d1_a4-%.3%e1_a1-c2~n";
		
		if(file_directory == null || file_directory.equals("")) throw new Exception();
		TreeMap<String, String> wurcsIndex = new TreeMap<String, String>();	
		File file = new File(file_directory);
		JSONArray ret = new JSONArray();
		
		if(file.isFile()) {
			wurcsIndex = openString(file_directory);
			for(String key : wurcsIndex.keySet()) {
				ret = new WURCSJsonExporter().generateWURCSJSON(wurcsIndex.get(key));
				//System.out.println(ja + "\n");
			}
		}else if(file_directory.indexOf("WURCS") != -1) {
			ret = new WURCSJsonExporter().generateWURCSJSON(file_directory);
			System.out.println(ret);
		}else {
			throw new Exception("This file is not found !");
		}
		
		//out put 
		//pass for test : /Users/st/Research/WURCS_Visualization 
	}
	
	/**
	 * 
	 * @param a_strFile
	 * @return
	 * @throws Exception
	 */
	public static TreeMap<String, String> openString(String a_strFile) throws Exception {
		try {
			return readWURCS(new BufferedReader(new FileReader(a_strFile)));
		}catch (IOException e) {
			throw new Exception();
		}
	}

	/**
	 * 
	 * @param a_bfFile
	 * @return
	 * @throws IOException
	 */
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
