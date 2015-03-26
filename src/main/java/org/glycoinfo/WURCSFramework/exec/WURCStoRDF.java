package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSExporterRDF;

public class WURCStoRDF {

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		String t_strFilePath = "C:\\GlycoCTList\\";

		Date day = new Date();
		long start = day.getTime();

		Boolean t_bPrefix = true;

		String t_strFilePathR = t_strFilePath + "20150310\\20150310result-GlyTouCan_GlycoCTmfWURCS.txt";
		File file = new File(t_strFilePathR);

		if(!file.isFile()) throw new Exception();
		TreeMap<String, String> t_mapIndexToWURCS = openString(t_strFilePathR);
		try{
			// Open print writer
			PrintWriter pwRDF = openTextFileW(t_strFilePath+"20150310\\WURCS-RDF.ttl");
			PrintWriter pwMSRDF = openTextFileW(t_strFilePath+"20150310\\WURCS-MS-RDF.ttl");

			// Prefix list
			WURCSExporterRDF t_oExport = new WURCSExporterRDF();
			String t_strPrefixList = t_oExport.getWURCSPrefix();
			pwRDF.println(t_strPrefixList);
			pwMSRDF.println(t_strPrefixList);

			// Generate RDF strings
			for(String key : t_mapIndexToWURCS.keySet()) {
				WURCSImporter t_oImport = new WURCSImporter();
				WURCSArray t_oWURCS = t_oImport.extractWURCSArray(t_mapIndexToWURCS.get(key));
				String t_strAccessionNumber = key;
				System.out.println(key);

				pwRDF.println(   t_oExport.getWURCSGlycanTripleTTL(t_strAccessionNumber, t_oWURCS) );
				pwMSRDF.println( t_oExport.getWURCSMonosaccharideTripleTTL(t_oWURCS.getUniqueRESs()) );
			}
			System.out.println("Fin...");
			day = new Date();
			long end = day.getTime();
			System.out.println("time:" + (end - start) + " m sec.");

		}catch(IOException e){
			System.out.println(e);
		}

	}

	/** Open text file for write */
	public static PrintWriter openTextFileW( String fileName ) throws Exception {
		String charSet = "utf-8";
		boolean append = false;
		boolean autoFlush = true;
		return new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File(fileName), append ), charSet ) ), autoFlush );

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
