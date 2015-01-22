package org.glycoinfo.WURCSFramework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class ReadWURCSExample {

	public static void main(String[] args) throws Exception {
		
		//for demonstrate string
		//WURCS=2.0/6,5/[hxh][x2122h+1:x|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O]1+3-2,2+1-2*ONCCOP^XO*/7O/7=O|2+4,3+1|3+4,4+1|4+4,5+1|5+4,6+1
		//String input = "WURCS=2.0/6,5/[hxh][x2122h+1:x|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O]1+3-2,2+1-2*ONCCOP^XO*/7O/7=O|2+?,3+1|3+4,4+1|4+4,5+1|5+4,6+1";
		//String input = "WURCS=2.0/4,4/[22122h+1:a|1,5|2*NCC/3=O][22112h+1:a|1,5|2*NCC/3=O][12112a+1:b|1,5][21122a+1:a|1,5]1+3,2+1|2+3,3+1|2+4,4+1|1+1,4+3~n";
		//String input = "WURCS=2.0/5,4/[h2112h|2*NCC/3=O][12112h+1:b|1,5][a6d21122h+2:a|2,6|5*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][12112h+1:b|1,5]1+?,2+?|1+6,4+1|2+3,3+2|5+1,(4+3)\\(4+4)";
		//String input = "WURCS=2.0/5,5/[12211m+1:a|1,5][12211m+1:a|1,5][12211m+1:a|1,5][22122h+1:a|1,5|2*NCC/3=O][2121h+1:b|1,5]1+3,2+1|2+2,3+1|3+2,4+1|1+1,4+3~n|1+4,%.7%5+1";
		String input = "WURCS=2.0/9,8/[hxh][12122h+1:b|1,5|2*NCC/3=O][12122h+1:b|1,5|2*NCC/3=O][11122h+1:b|1,5][21122h+1:a|1,5][21122h+1:a|1,5][21122h+1:a|1,5][21122h+1:a|1,5][21122h+1:a|1,5]1+3:1-2,2+1:1-2*ONCCOP^XO*/7O/7=O|2+4,3+1|3+4,4+1|4+3,5+1|4+6,7+1|5+2,6+1|7+3,8+1|7+6,9+1";
		
		//String input = "src/org/glycoinfo/WURCSFramework/testresource/20141218result-GlycomeDB_GlycoCTmfWURCS.txt";
		
		if(input == null || input.equals("")) throw new Exception();
		
		TreeMap<Integer, String> wurcsIndex = new TreeMap<Integer, String>();
		WURCSImporter ws = new WURCSImporter();
		File file = new File(input);
		
		if(file.isFile()) {
			wurcsIndex = openString(input);
			for(Integer key : wurcsIndex.keySet()) {
				WURCSArray wurcs = ws.WURCSsepalator(wurcsIndex.get(key));
			}
		}else if(input.indexOf("WURCS") != -1) {
			ws.WURCSsepalator(input.substring(input.indexOf("W")));
		}else {
			throw new Exception("This file is not found");
		}
	}
	
	//input WURCS string file
	public static TreeMap<Integer, String> openString(String a_strFile) throws Exception {
		try {
			return readWURCS(new BufferedReader(new FileReader(a_strFile)));
    	}catch (IOException e) {
    		throw new Exception();
    	}
	}
	
	public static TreeMap<Integer, String> readWURCS(BufferedReader a_bfFile) throws IOException {
		String line = "";
		TreeMap<Integer, String> wret = new TreeMap<Integer, String>();
		wret.clear();
		int ct = 0;
		
		while((line = a_bfFile.readLine()) != null) {
			line.trim();
			if(line.indexOf("WURCS") != -1) {
				wret.put(ct, line.substring(line.indexOf("W")));
				ct++;
			}
		}
		a_bfFile.close();
		
		return wret;
	}
	
}
