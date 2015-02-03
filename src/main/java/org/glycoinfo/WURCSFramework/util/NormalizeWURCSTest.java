package org.glycoinfo.WURCSFramework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public class NormalizeWURCSTest {

	public static void main(String[] args) {

		String t_strWURCSFile = "20150129result-GlyTouCan_GlycoCTmfWURCS.txt";
		String input = "src/org/glycoinfo/WURCSFramework/testresource/" + t_strWURCSFile;

		TreeMap<String, String> t_mapWURCSIndex = new TreeMap<String, String>();
		WURCSImporter t_objImporter = new WURCSImporter();

		if(! new File(input).isFile() ) {
			System.err.println("File not found");
			System.exit(0);
		}

		try {
			t_mapWURCSIndex = readWURCS(new BufferedReader(new FileReader(input)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			WURCSExporter t_oExporter = new WURCSExporter();
			int changeCount = 0;
			for(String key : t_mapWURCSIndex.keySet()) {
				String t_strOldWURCS = t_mapWURCSIndex.get(key);
				WURCSArray t_oWURCS = t_objImporter.extractWURCSArray(t_mapWURCSIndex.get(key));
				String t_strNewWURCS = t_oExporter.getWURCSString(t_oWURCS);
				if ( ! t_strOldWURCS.equals(t_strNewWURCS) ) {
					System.out.println(key+":\n\t"+t_strOldWURCS+"\n\t"+t_strNewWURCS);
					changeCount++;
				}
			}
			System.out.println("Change count: "+changeCount);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
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
