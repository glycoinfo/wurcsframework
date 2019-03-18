package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToGraph;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSGraphToArray;
import org.glycoinfo.WURCSFramework.util.graph.WURCSGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSTestInOutDump {

	public static void main(String[] args) {

//		TreeMap<String, String> t_mapWURCSIndex = new TreeMap<String, String>();
		TreeMap<String, ArrayList<String>> t_mapWURCSIndex = new TreeMap<String, ArrayList<String>>();
		WURCSImporter t_objImporter = new WURCSImporter();


		try {
			t_mapWURCSIndex = readWURCS2(new BufferedReader(new InputStreamReader(System.in,"utf-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
//			WURCSExporter t_oExporter = new WURCSExporter();
//			TreeSet<String> t_aUniqueOrigWURCS = new TreeSet<String>();
//			TreeSet<String> t_aSortLIN = new TreeSet<String>();
//			System.err.print("before for loop\n");
			for(String key : t_mapWURCSIndex.keySet()) {
//				if ( t_aUniqueOrigWURCS.contains(t_mapWURCSIndex.get(key)) ) continue;
//				t_aUniqueOrigWURCS.add(t_mapWURCSIndex.get(key));

//				System.err.print(key+"\t");
				System.out.print(key+"\t");
				String t_strOrigWURCS1 = t_mapWURCSIndex.get(key).get(0);
				String t_strOrigWURCS2 = t_mapWURCSIndex.get(key).get(1);
//				System.err.print(t_mapWURCSIndex.get(key));
//				System.err.print(t_mapWURCSIndex.get(key).get(0));
//				System.err.print(t_mapWURCSIndex.get(key).get(1));
//				System.err.println();

				// Extract WURCS
				WURCSArray t_oWURCS1 = t_objImporter.extractWURCSArray(t_strOrigWURCS1);
				WURCSArray t_oWURCS2 = t_objImporter.extractWURCSArray(t_strOrigWURCS2);

				// Export WURCS string
//				String t_strSortLIN1 = t_oExporter.getWURCSString(t_oWURCS1);
//				String t_strSortLIN2 = t_oExporter.getWURCSString(t_oWURCS2);
//				if ( !t_strOrigWURCS.equals(t_strSortLIN) ) t_aSortLIN.add(key);

				// To Graph
				WURCSArrayToGraph t_oA2G1 = new WURCSArrayToGraph();
				WURCSArrayToGraph t_oA2G2 = new WURCSArrayToGraph();
				t_oA2G1.start(t_oWURCS1);
				t_oA2G2.start(t_oWURCS2);
				WURCSGraph t_oGraph1 = t_oA2G1.getGraph();
				WURCSGraph t_oGraph2 = t_oA2G2.getGraph();

				// Normalize
				WURCSGraphNormalizer t_oNorm = new WURCSGraphNormalizer();
				t_oNorm.start(t_oGraph1);
				t_oNorm.start(t_oGraph2);

				t_oGraph1.dump();
				System.out.print("\t");
				t_oGraph2.dump();
				System.out.println();
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
//	public static TreeMap<String, String> openString(String a_strFile) throws Exception {
//		try {
//			return readWURCS(new BufferedReader(new FileReader(a_strFile)));
//		}catch (IOException e) {
//			throw new Exception();
//		}
//	}

	public static TreeMap<String, ArrayList<String>> readWURCS2(BufferedReader a_bfFile) throws IOException {
		String line = "";
//		ArrayList<String> t_strWURCS = new ArrayList<String>();
		TreeMap<String, ArrayList<String>> wret = new TreeMap<String, ArrayList<String>>();
		wret.clear();

		while((line = a_bfFile.readLine()) != null) {
			line.trim();
			if(line.indexOf("WURCS") != -1) {
				String[] IDandWURCS  = line.split("\t");
//				System.err.println("readWURCS>"+IDandWURCS[1]+","+IDandWURCS[2]+"\n");
				if (IDandWURCS.length == 3) {
					ArrayList<String> t_strWURCS = new ArrayList<String>();
					t_strWURCS.add(IDandWURCS[1].trim());
					t_strWURCS.add(IDandWURCS[2].trim());
					wret.put(IDandWURCS[0].trim(), t_strWURCS);
//					System.err.print(wret.get(IDandWURCS[0]));
//					System.err.print(wret.get(IDandWURCS[0]).get(0));
//					System.err.print(wret.get(IDandWURCS[0]).get(1)+"\n");
					}
				}
			}
		a_bfFile.close();
		return wret;
	}

}
