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

public class NormalizeWURCSTestFromFile {

	public static void main(String[] args) {

		String t_strDir = "E:\\GlycoCTList\\20160215\\";
		String t_strWURCSFile = "20160215result-GlyTouCan_WURCSList.txt";
//		String input = "src/org/glycoinfo/WURCSFramework/testresource/" + t_strWURCSFile;
		String input  = t_strDir + t_strWURCSFile;
		String output = t_strDir + "20160215result-GlyTouCan_WURCStoWURCS.txt";

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
			PrintWriter pw = openTextFileW(output);
			TreeSet<String> t_aUniqueOrigWURCS = new TreeSet<String>();
			TreeMap<String, TreeSet<String>> t_mapUniqueGraphWURCStoID = new TreeMap<String, TreeSet<String>>();
			TreeSet<String> t_aSortLIN = new TreeSet<String>();
			TreeSet<String> t_aInvert = new TreeSet<String>();
			TreeSet<String> t_aAnomLink = new TreeSet<String>();
			TreeSet<String> t_aCyclic = new TreeSet<String>();
			TreeSet<String> t_aOthers = new TreeSet<String>();
			int changeCount = 0;
			for(String key : t_mapWURCSIndex.keySet()) {
				if ( t_aUniqueOrigWURCS.contains(t_mapWURCSIndex.get(key)) ) continue;
				t_aUniqueOrigWURCS.add(t_mapWURCSIndex.get(key));

				System.err.println(key+":");
				String t_strOrigWURCS = t_mapWURCSIndex.get(key);

				// Extract WURCS
				WURCSArray t_oWURCS = t_objImporter.extractWURCSArray(t_mapWURCSIndex.get(key));

				// Export WURCS string
				String t_strSortLIN = t_oExporter.getWURCSString(t_oWURCS);
				if ( !t_strOrigWURCS.equals(t_strSortLIN) ) t_aSortLIN.add(key);

				// To Graph
				WURCSArrayToGraph t_oA2G = new WURCSArrayToGraph();
				t_oA2G.start(t_oWURCS);
				WURCSGraph t_oGraph = t_oA2G.getGraph();

				// Normalize
				WURCSGraphNormalizer t_oNorm = new WURCSGraphNormalizer();
				t_oNorm.start(t_oGraph);

				// For inverted backbone
				if ( t_oNorm.isInverted() ) t_aInvert.add(key);

				// To Array
				WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
				t_oG2A.start(t_oGraph);
				t_oWURCS = t_oG2A.getWURCSArray();

				// Mass calculate
/*				try {
					WURCSMassCalculator.calcMassWURCS(t_oWURCS);
				} catch (WURCSMassException e) {
					// TODO 自動生成された catch ブロック

					System.out.println(key+": "+e.getErrorMessage());
//					e.printStackTrace();
				}
*/
				// Export WURCS string again
				String t_strSortGraph = t_oExporter.getWURCSString(t_oWURCS);
				if ( !t_mapUniqueGraphWURCStoID.containsKey(t_strSortGraph) )
					t_mapUniqueGraphWURCStoID.put(t_strSortGraph, new TreeSet<String>());
				t_mapUniqueGraphWURCStoID.get(t_strSortGraph).add(key);

				// Check change strings
				if ( t_strOrigWURCS.equals(t_strSortGraph) ) continue;
				changeCount++;
				pw.print(key+":");
				if ( ! t_strOrigWURCS.equals(t_strSortLIN) ) pw.print(" LIN or MOD Sort :");
				if ( t_oNorm.isInverted() )                  pw.print(" Invert :");
				if ( t_oNorm.linkedAnomericPositions() )     pw.print(" Anomeric linkage :");
				if ( t_oNorm.hasCyclic() )                   pw.print(" Cyclic :");
				pw.println("\n\t"+t_strOrigWURCS);
				if ( ! t_strOrigWURCS.equals(t_strSortLIN) )
					pw.println("\t"+t_strSortLIN);
				if ( t_strSortLIN.equals(t_strSortGraph) ) continue;
				pw.println("\t"+t_strSortGraph);

				if ( t_oNorm.linkedAnomericPositions() ) t_aAnomLink.add(key);
				if ( t_oNorm.hasCyclic() ) t_aCyclic.add(key);
				if ( !t_oNorm.isInverted() && !t_oNorm.linkedAnomericPositions() && !t_oNorm.hasCyclic() )
					t_aOthers.add(key);
			}
			pw.println("\nTotal change count: "+changeCount);
			pw.println("Sort change count: "+t_aSortLIN.size());
			pw.println( join(t_aSortLIN) );
			pw.println("Invert count: "+t_aInvert.size());
			pw.println( join(t_aInvert) );
			pw.println("Anomeric bonding count: "+t_aAnomLink.size());
			pw.println( join(t_aAnomLink) );
			pw.println("Cyclic count: "+t_aCyclic.size());
			pw.println( join(t_aCyclic) );
			pw.println("Other count: "+t_aOthers.size());
			pw.println( join(t_aOthers) );
			// dup
			pw.println("Duplicate:");
			for ( String t_strWURCS : t_mapUniqueGraphWURCStoID.keySet() ) {
				if ( t_mapUniqueGraphWURCStoID.get(t_strWURCS).size() < 2 ) continue;
				pw.println( join(t_mapUniqueGraphWURCStoID.get(t_strWURCS)) );
				pw.println(t_strWURCS);
			}
		} catch (WURCSFormatException e) {
			e.printStackTrace();
		} catch (WURCSException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String join(Set<String> t_aList ) {
		String t_strJoin = "";
		int count = 0;
		for ( String t_strInv : t_aList ) {
			if ( !t_strJoin.equals("") ) t_strJoin += ",";
			if ( count != 0 && count % 20 == 0 ) t_strJoin += "\n";
			t_strJoin += t_strInv;
			count++;
		}
		return t_strJoin;
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
