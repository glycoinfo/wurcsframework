package org.glycoinfo.WURCSFramework.util;

import java.io.BufferedReader;
import java.util.TreeMap;

public class WURCSListReader extends FileIOUtils {

	//input WURCS string file
	public static TreeMap<String, String> readFromFile(String a_strFilePath) throws Exception {
		String line = null;
		BufferedReader br  = FileIOUtils.openTextFileR( a_strFilePath );

		TreeMap<String, String> t_mapIDToWURCS = new TreeMap<String, String>();
		while ( (line = br.readLine())!=null ) {
			line.trim();
			if(line.indexOf("WURCS") != -1) {

				String[] IDandWURCS  = line.split("\t");
				if (IDandWURCS.length == 2) {
					t_mapIDToWURCS.put(IDandWURCS[0].trim(), IDandWURCS[1]);
				}
			}
		}
		br.close();

		return t_mapIDToWURCS;

	}

}
