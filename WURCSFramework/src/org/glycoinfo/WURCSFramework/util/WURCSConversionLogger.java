package org.glycoinfo.WURCSFramework.util;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class WURCSConversionLogger {

//	private String m_strFilePath;
//	private String m_strErrors = "";
//	private PrintWriter m_pwResults;

	private String m_strInputFileName = "";
	private int m_nTotal = 0;
	private int m_nSuccess = 0;

	private TreeMap<String, String> m_hashIDToMessage = new TreeMap<String, String>();
	private LinkedHashMap<String, LinkedList<String>> m_hashWURCSToIDs = new LinkedHashMap<String, LinkedList<String>>();
	private HashMap<String, LinkedList<String>>       m_hashErrors         = new HashMap<String, LinkedList<String>>();

//	public WURCSConversionLogger(String a_strFilePath) {
//		this.m_strFilePath = a_strFilePath;
//		this.m_pwResults = FileIOUtils.openTextFileW(this.m_strFilePath);
//	}

	public void setInputFileName(String a_strInputFile) {
		this.m_strInputFileName = a_strInputFile;
	}

	public void addWURCS(String a_strID, String a_strWURCS) {
		if ( !this.m_hashWURCSToIDs.containsKey(a_strWURCS) )
			this.m_hashWURCSToIDs.put(a_strWURCS, new LinkedList<String>() );
		// Check same ID
		if ( this.m_hashWURCSToIDs.get(a_strWURCS).contains(a_strID) ) return;
		this.m_hashWURCSToIDs.get(a_strWURCS).add(a_strID);
		this.m_nTotal++;
		this.m_nSuccess++;
	}

	public void addMessage(String a_strID, String a_strType, String a_strFullMessage) {
		String t_strErrorMessage = "";
		if ( !a_strType.equals("") ) {
			this.m_nTotal++;
			t_strErrorMessage = a_strType;
			if ( !a_strFullMessage.equals("") )
				t_strErrorMessage += "\n"+a_strFullMessage;

//			System.out.println(a_strType);

			if ( !this.m_hashErrors.containsKey(a_strType) )
				this.m_hashErrors.put(a_strType, new LinkedList<String>() );
			this.m_hashErrors.get(a_strType).add(a_strID);
		} else if ( !a_strFullMessage.equals("") ) {
			t_strErrorMessage += "Warning in GlycoCT.\n"+a_strFullMessage;
		}

		if ( t_strErrorMessage.equals("") ) return;
		this.m_hashIDToMessage.put(a_strID, t_strErrorMessage);
	}

	public void printLog(String a_strFilePath) throws Exception {
		PrintWriter t_pwResults = FileIOUtils.openTextFileW(a_strFilePath);

		// Print input filename
		if ( !this.m_strInputFileName.equals("") )
			t_pwResults.println("Read from "+this.m_strInputFileName+":\n");

		// Print message for each ID
		for ( String t_strID : this.m_hashIDToMessage.keySet() )
			t_pwResults.println( t_strID + "\t" + this.m_hashIDToMessage.get(t_strID) );

		// Total results
		t_pwResults.println( "\nTotal CT: " + this.m_nTotal );
		t_pwResults.println( "Successful conversion: " + this.m_nSuccess );
		if ( !this.m_hashErrors.isEmpty() ) {
			t_pwResults.println( "Errors: " );
			LinkedList<String> errors = new LinkedList<String>();
			for ( String err : this.m_hashErrors.keySet() ) errors.add(err);
			Collections.sort(errors);
			for ( String t_strErr : errors ) {
//				pw.println( "-" + err + ": " + hashErrors.get(err).size() );
				String t_strErrIDs = this.concatinateIDs( this.m_hashErrors.get(t_strErr) );
				int t_nErrIDs = this.m_hashErrors.get(t_strErr).size();
/*				String t_strErrIDs = "";
				int errCount = 0;
				LinkedList<String> errIDs = this.m_hashErrors.get(t_strErr);
				Collections.sort(errIDs);
				for ( String errID : errIDs ) {
					t_strErrIDs += errID;
					errCount++;
					if ( errCount == this.m_hashErrors.get(t_strErr).size() ) continue;
					t_strErrIDs += ",";
					if ( errCount % 20 == 0 )
						t_strErrIDs += "\n";
				}
*/				t_pwResults.println(t_strErrIDs+"\t"+t_strErr+" : "+t_nErrIDs);
			}
		}
		LinkedList<String> t_aDuplicateWURCS = new LinkedList<String>();
		for ( String strWURCS : this.m_hashWURCSToIDs.keySet() ) {
			if ( this.m_hashWURCSToIDs.get(strWURCS).size() == 1 ) continue;
			t_aDuplicateWURCS.add(strWURCS);
		}
		t_pwResults.println( "Duplicated structures: " + t_aDuplicateWURCS.size() );
		for ( String strWURCS : t_aDuplicateWURCS ) {
			String dupIDs = this.concatinateIDs( this.m_hashWURCSToIDs.get(strWURCS) );
/*			String dupIDs = "";
			int dupCount = 0;
			for ( String dupID : this.m_hashWURCSToIDs.get(strWURCS) ) {
				dupIDs += dupID;
				dupCount++;
				if ( dupCount == this.m_hashWURCSToIDs.get(strWURCS).size() ) continue;
				if ( dupIDs != "" ) dupIDs += ",";
			}
*/			t_pwResults.println(dupIDs+"\t"+strWURCS);
		}

		t_pwResults.close();
	}

	private String concatinateIDs( LinkedList<String> a_aIDs ) {
		String t_strIDs = "";
		int count = 0;
		for ( String ID : a_aIDs ) {
			t_strIDs += ID;
			count++;
			if ( count == a_aIDs.size() ) continue;
			t_strIDs += ",";
			if ( count % 20 == 0 )
				t_strIDs += "\n";
		}
		return t_strIDs;
	}
}
