package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;

public class WURCSDataConverter {
	//   ID <-> Index
	//    1 <->  a
	//    2 <->  b
	//   26 <->  z
	//   27 <->  A
	//   28 <->  B
	//   52 <->  Z
	//   53 <-> aa
	//   54 <-> ab
	//  104 <-> aZ
	//  105 <-> ba
	//  106 <-> bb
	// 1000 <-> sl
	// 2000 <-> Lx

	/**
	 * Convert RES ID number to RES Index string
	 * @param a_iRESID
	 * @return String of RES Index
	 */
	public static String convertRESIDToIndex(int a_iRESID) {
		String t_strRes = "";

		int t_iRemainder = a_iRESID;
		LinkedList<Integer> t_aQuotients = new LinkedList<Integer>();
		while(t_iRemainder > 0) {
			int t_iQuotient = t_iRemainder % 52;
			t_iQuotient = (t_iQuotient == 0)? 52 : t_iQuotient;
			t_aQuotients.addFirst(t_iQuotient);
			t_iRemainder = (int)Math.floor( (t_iRemainder - t_iQuotient) / 52 );
		}

		// int of alphabet
		int t_iLower = (int)'a' - 1;
		int t_iUpper = (int)'A' - 1;
		for (Integer q : t_aQuotients) {
			int alphabet = ( q > 26 )? t_iUpper + q - 26 : t_iLower + q;
			t_strRes += (char)alphabet;
		}
		return t_strRes;
	}

	/**
	 * Convert RES Index string to RES ID number
	 * @param a_iRESIndex
	 * @return RES ID number
	 */
	public static int convertRESIndexToID(String a_strRESIndex) {
		int t_iRESID = 0;
		for ( int i=0; i<a_strRESIndex.length(); i++ ) {
			char ch = a_strRESIndex.charAt(a_strRESIndex.length()-1-i);
			int q = (int)ch + 1;
			q += Character.isUpperCase(ch) ? 26 - (int)'A' : - (int)'a';
			t_iRESID += q * Math.pow(52, i);
		}
		return t_iRESID;
	}

	/**
	 * Convert LIP Direction character to number of priority order
	 * @param a_cDirection
	 * @return Number of priority order
	 */
	public static int convertDirectionToID(char a_cDirection) {
		return   a_cDirection == 'n' ? 0
			   : a_cDirection == 'u' ? 1
			   : a_cDirection == 'd' ? 2
			   : a_cDirection == 't' ? 3
			   : a_cDirection == 'e' ? 4
			   : a_cDirection == 'z' ? 5
			   : a_cDirection == 'x' ? 6 : 7;
	}

}
