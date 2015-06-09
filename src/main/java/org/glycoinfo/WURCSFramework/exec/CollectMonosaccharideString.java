package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToGraph;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSGraphExporterUniqueMonosaccharides;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class CollectMonosaccharideString {

	public static void main(String[] args) {
		String t_strDir = "C:\\GlycoCTList\\20150318\\";
		String t_strWURCSFile = "20150318result-GlyTouCan_GlycoCTmfWURCS.txt";
//		String input = "src/org/glycoinfo/WURCSFramework/testresource/" + t_strWURCSFile;
		String input  = t_strDir + t_strWURCSFile;
		String output = t_strDir + "20150318result-GlyTouCan_MSList.txt";

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
			PrintWriter pw = openTextFileW(output);
			TreeSet<String> t_aUniqueOrigWURCS = new TreeSet<String>();
			TreeSet<String> t_aUniqueMS = new TreeSet<String>();

			for(String key : t_mapWURCSIndex.keySet()) {
				String t_strOrigWURCS = t_mapWURCSIndex.get(key);
				if ( t_aUniqueOrigWURCS.contains(t_strOrigWURCS) ) continue;
				t_aUniqueOrigWURCS.add(t_strOrigWURCS);

				System.err.println(key+":");

				// Extract WURCS
				WURCSArray t_oWURCS = t_objImporter.extractWURCSArray(t_mapWURCSIndex.get(key));

				// To Graph
				WURCSArrayToGraph t_oA2G = new WURCSArrayToGraph();
				t_oA2G.start(t_oWURCS);
				WURCSGraph t_oGraph = t_oA2G.getGraph();

				WURCSGraphExporterUniqueMonosaccharides t_oExportMS = new WURCSGraphExporterUniqueMonosaccharides();
				t_oExportMS.start(t_oGraph);
				for ( String t_strUniqueMS : t_oExportMS.getMSStrings() ) {
					if ( t_aUniqueMS.contains(t_strUniqueMS) ) continue;
					t_aUniqueMS.add(t_strUniqueMS);
				}
			}
			for ( String t_strMS : t_aUniqueMS ) {
				pw.println(t_strMS);
			}

		} catch (WURCSFormatException e) {
			e.printStackTrace();
		} catch (WURCSException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
