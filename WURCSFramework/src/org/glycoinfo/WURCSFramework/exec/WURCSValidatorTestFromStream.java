package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSValidator;

public class WURCSValidatorTestFromStream {
	public static void main(String[] args) {

		TreeMap<String, String> t_mapWURCSIndex = new TreeMap<String, String>();
		try {
			t_mapWURCSIndex = readWURCS(new BufferedReader(new InputStreamReader(System.in,"utf-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//
////		TreeSet<String> t_aUniqueOrigWURCS = new TreeSet<String>();
		String t_strWURCS;
		for(String key : t_mapWURCSIndex.keySet()) {
////			if ( t_aUniqueOrigWURCS.contains(t_mapWURCSIndex.get(key)) ) continue;
////			t_aUniqueOrigWURCS.add(t_mapWURCSIndex.get(key));
			t_strWURCS = t_mapWURCSIndex.get(key);
//		for ( String t_strWURCS : t_aWURCSList ) {
			WURCSValidator validator=new WURCSValidator();
			validator.start(t_strWURCS);
				if(validator.getTheNumberOfErrors()!=0||validator.getTheNumberOfWarnings()!=0) {
//				if(validator.getTheNumberOfErrors()!=0) {
				System.out.println(key+": "+t_strWURCS);
				if(validator.getTheNumberOfErrors()==0) System.out.println("outWURCS: "+validator.getStandardWURCS());
				System.out.println("the number of errors: "+validator.getTheNumberOfErrors());
				for(String er: validator.getErrors()) System.out.println("	Error:   "+er);
				for(String wa: validator.getWarnings()) System.out.println("	Warning: "+wa);
				System.out.println();
				}
			}
		}
//
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
