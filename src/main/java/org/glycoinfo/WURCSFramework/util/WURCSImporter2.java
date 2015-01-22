package org.glycoinfo.WURCSFramework.util;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class WURCSImporter2 {

	public WURCSArray WURCSsepalator(String a_strWURCS) {

		java.lang.System.out.println(a_strWURCS);
		String version =         "";
		int numUniqueRES =        0;
		int numRES =              0;
		int numLIN =              0;
		String strUniqueRESs =   "";
		String strResSequenses = "";
		String strLINs =         "";

		//String strExp = "WURCS=(.+)\\/(\\d+),(\\d+),(\\d+)\\/(.+)\\]/([0-9-]+)/(.+)";
		String strExp = "WURCS=(.+)/(\\d+),(\\d+),(\\d+)/(.+)]/([0-9-]+)/(.+)";

		Matcher wurcsMatch = Pattern.compile(strExp).matcher(a_strWURCS);
		//WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/1-2-3-4-2-5-4-2-6-7/a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\c6_g1-c3\c6
		//		group(0)	WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/1-2-3-4-2-5-4-2-6-7/a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\c6_g1-c3\c6
		//		group(1)	2.0
		//		group(2)	7
		//		group(3)	10
		//		group(4)	9
		//		group(5)	[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5
		//		group(6)	1-2-3-4-2-5-4-2-6-7
		//		group(7)	a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\c6_g1-c3\c6
		int t_iVersion = 1;
		int t_iNumuRES = 2;
		int t_iNumRES = 3;
		int t_iNumLIN = 4;
		int t_iUniqueRESs = 5;
		int t_iResSequenses = 6;
		int t_iLINs = 7;
//		if ( !wurcsMatch.matches() )
//			return null;
		//if ( !wurcsMatch.find() )
		//return null;
		if(wurcsMatch.find()) {
			version =                              wurcsMatch.group(t_iVersion);
			numUniqueRES =        Integer.parseInt(wurcsMatch.group(t_iNumuRES));
			numRES =              Integer.parseInt(wurcsMatch.group(t_iNumRES));
			numLIN =              Integer.parseInt(wurcsMatch.group(t_iNumLIN));
			strUniqueRESs = "]" +                  wurcsMatch.group(t_iUniqueRESs);
			strResSequenses =                      wurcsMatch.group(t_iResSequenses);
			strLINs =                              wurcsMatch.group(t_iLINs);
		}

		WURCSArray wurcsContainer = new WURCSArray(version, numUniqueRES, numRES, numLIN);

		// generate a Unique RES
		// [x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5
		// replace "["
		String strRep = "([\\[])";
		strUniqueRESs = strUniqueRESs.replaceAll(strRep,"");

		// split "]"
		String[] t_aURESs = strUniqueRESs.split("]");
		int t_iURESID = 1; // count of uniqueRES
		for(String t_strURES : t_aURESs) {
			if (t_strURES.length() > 0) {
//				wurcsContainer = this.extractUniqueRES(t_strURES, wurcsContainer, t_iURESID);
				wurcsContainer.addUniqueRES(this.extractUniqueRES(t_strURES, t_iURESID));
				t_iURESID++;
			}
		}
		// generate a RES sequences and a LIN
		if(wurcsContainer.getRESCount() > 0) {
			// generate RESsequences
			// 1-2-3-4-2-5-4-2-6-7
			// split "-"
			String[] RESSequenceArray = strResSequenses.split("-");
			int i = 1; // count of RES
			for(String s : RESSequenceArray) {
				if (s.length() > 0) {
					String expRESseq = "([0-9-]+)";
					if(s.matches(expRESseq)) wurcsContainer = this.extractRESSeqs(s, wurcsContainer, i);
					i++;
				}
			}

			// generate a LIN
			// a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\c6_g1-c3\c6
			// split "_"
			String[] LINArray = strLINs.split("_");
			for(String s : LINArray) {
				//String expLIN = "([a-zA-Z0-9\\&&[^*]]+)-([a-zA-Z0-9\\&&[^*]]+)*(.*)";
				//String expLIN = "([a-zA-Z0-9|&&[^*]]+)-([a-zA-Z0-9|&&[^*]]+)*(.*)";
				//if(s.matches(expLIN)) wurcsContainer = extractLINs(s, wurcsContainer);
				wurcsContainer = this.extractLINs(s, wurcsContainer);
			}
		}
		return wurcsContainer;
	}

	public UniqueRES extractUniqueRES(String a_strURES, int a_iURESID) {
		// input: 12122h-1b_1-5_2*NCC/3=O
		// Split SkeletonCode and MODs
		String[] t_aSplitURES = a_strURES.split("_");

		// t_aSplitRES[0] = 12122h-1b  // basetype
		// t_aSplitRES[1] = 1-5        // MOD 1
		// t_aSplitRES[2] = 2*NCC/3=O  // MOD 2

		String[] t_aSplitSC = t_aSplitURES[0].split("-");

		// SkeletonCode and anomeric information
		String t_strSkeletonCode   = t_aSplitSC[0];
		int    t_iAnomericPosition =        0;
		char   t_cAnomericSymbol   =       ' ';
		if ( t_aSplitSC.length > 1 ) {
			String strExp = "(\\?|[0-9]+)([abx])";
			Matcher matchAnomerInfo = Pattern.compile(strExp).matcher(t_aSplitSC[1]);
			if(matchAnomerInfo.find()) {
				t_iAnomericPosition = (matchAnomerInfo.group(1).equals("?") ? -1 : Integer.parseInt(matchAnomerInfo.group(1))) ;
				t_cAnomericSymbol = matchAnomerInfo.group(2).toCharArray()[0];
			}
		}
		UniqueRES t_oURES = new UniqueRES(a_iURESID, t_strSkeletonCode, t_iAnomericPosition, t_cAnomericSymbol );

		// MODs
		for ( int i=1; i<t_aSplitURES.length; i++ ) {
			String t_strMOD = t_aSplitURES[i];
			MOD t_oMOD = this.extractMOD(t_strMOD);
			t_oURES.addMOD(t_oMOD);
		}
		return t_oURES;
	}

	public MOD extractMOD( String a_strMOD ) {
		String t_strMAP = "";
		String t_strLIPs = a_strMOD;
		if ( a_strMOD.indexOf("*") != -1 ) {
			t_strMAP = a_strMOD.substring( a_strMOD.indexOf("*") );
			t_strLIPs = a_strMOD.substring(0, a_strMOD.indexOf("*") );
		}
		MOD t_oMOD = new MOD(t_strMAP);

		// LIP ^:
		for ( String t_strLIP : t_strLIPs.split("-") ) {
			if ( t_strLIP.contains("|") ) {
				FuzzyLIP t_oFLIP = this.extractFLIP(t_strLIP);
				t_oMOD.addFuzzyLIP(t_oFLIP);
			} else {
				LIP t_oLIP = this.extractLIP(t_strLIP);
				t_oMOD.addLIP(t_oLIP);
			}
		}
		return t_oMOD;
	}

	public FuzzyLIP extractFLIP( String a_strFuzzyLIP ) {
		LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();
		// separate Alternative GLIP "|"
		for ( String t_strLIP : a_strFuzzyLIP.split("\\|") ) {
			t_aLIPs.addLast( this.extractLIP(t_strLIP) );
		}
		return new FuzzyLIP(t_aLIPs);

	}

	public LIP extractLIP( String a_strLIP ) {
		String prob = "(%(.+)%)?";
//		String nodeIndex = "(\\?|[a-zA-Z]+)";
		String SCPosition = "(\\?|[0-9]+)";
		String direction = "([nudtezx])?";
		String MAPPosition = "(\\?|[0-9]+)?";
//		String strExp = prob+ nodeIndex+SCPosition+direction+MAPPosition +prob;
		String strExp = prob+ SCPosition+direction+MAPPosition +prob;
		Matcher size = Pattern.compile(strExp).matcher(a_strLIP);
		// %.21%a2u1%.5%
		//	group(0)	%.21%a2u1%.5%
		//	group(1)	%.21%
		//	group(2)	.21
		//	group(3)	2
		//	group(4)	u
		//	group(5)	1
		//	group(6)	%.5%
		//	group(7)	.5

		// TODO: Error message for not match
		if ( !size.find() ) System.exit(0);

		String t_strSCPos     = size.group(3);
		String t_strDirection = size.group(4);
		String t_strMAPPos    = size.group(5);

		int t_iSCPos = 0;
		char t_cDirection = ' ';
		int t_iMAPPos = 0;

		// TODO: Error message for not number
		if ( t_strSCPos.equals("?") ) t_strSCPos = "-1";
		if (! this.isNumber(t_strSCPos) ) System.exit(0);

		t_iSCPos = Integer.parseInt(t_strSCPos);

		if ( t_strDirection != null )
			t_cDirection = t_strDirection.charAt(0);

		if (t_strMAPPos != null ) {
			// TODO: Error message for not number
			if ( t_strMAPPos.equals("?") ) t_strMAPPos = "-1";

			if (! this.isNumber(t_strMAPPos) ) System.exit(0);
			t_iMAPPos = Integer.parseInt(t_strMAPPos);
		}

		LIP t_oLIP = new LIP(t_iSCPos, t_cDirection, t_iMAPPos);

		// Probabilities
		String t_strBackboneProb = size.group(2);
		String t_strModificationProb = size.group(7);
		if ( t_strBackboneProb != null ) {
			System.out.println(t_strBackboneProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strBackboneProb);
			t_oLIP.setBackboneProbabilityLower(t_aProbs[0]);
			t_oLIP.setBackboneProbabilityUpper( ( t_aProbs[0] == t_aProbs[1] )? t_aProbs[0] : t_aProbs[1] );
		}

		if ( t_strModificationProb != null ) {
			System.out.println(t_strModificationProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strModificationProb);
			t_oLIP.setModificationProbabilityLower(t_aProbs[0]);
			t_oLIP.setModificationProbabilityUpper( ( t_aProbs[0] == t_aProbs[1] )? t_aProbs[0] : t_aProbs[1] );
		}

		return t_oLIP;

	}

	private double[] extractProbabilities( String a_strProb ) {
		// t_aProbs[0] : lower probability
		// t_aProbs[1] : Upper probability
		double[] t_adProbs = {1.0, 1.0};

		// TODO: Error message for not double
		String[] t_asProbs = a_strProb.split(":");
		if ( t_asProbs[0].matches("[^\\?\\.0-9]") ) System.exit(0);
		// Lower
		if ( t_asProbs[0].equals("?") ) t_asProbs[0] = "-1";
		t_adProbs[0] = Double.parseDouble(t_asProbs[0]);
		t_adProbs[1] = Double.parseDouble(t_asProbs[0]);
		// Upper
		if ( t_asProbs.length > 1 ) {
			if ( t_asProbs[1].matches("[^\\?\\.0-9]") ) System.exit(0);
			if ( t_asProbs[1].equals("?") ) t_asProbs[1] = "-1";
			t_adProbs[1] = Double.parseDouble(t_asProbs[1]);
		}

		return t_adProbs;
	}

	private String[] splitString(String a_strString, String strSplit){
		String[] t_aSplitString = {"",""};
		if (a_strString.contains(strSplit)){
			int index = a_strString.indexOf(strSplit);
			if (t_aSplitString.length > 1) {
				t_aSplitString[0] = a_strString.substring(0, index);
				t_aSplitString[1] = a_strString.substring(index);
			}
		}
		else {
			t_aSplitString[0] = a_strString;
			t_aSplitString[1] = "";
		}
		return t_aSplitString;
	}


	public WURCSArray extractRESSeqs(String a_strRESseqs, WURCSArray a_objWURCS, int iRESCount) {

		String strExp = "([0-9]+)";
		Matcher size = Pattern.compile(strExp).matcher(a_strRESseqs);
		if (size.find()){
																							// int to string
				RES a_objRES = new RES(Integer.parseInt(size.group(1)), convertResidueID(iRESCount));

			a_objWURCS.addRESs(a_objRES);
		}
		return a_objWURCS;
	}

	public WURCSArray extractLINs(String a_strLIN, WURCSArray a_objWURCS) {
		//	a1n1|c1n1-b1n1|c1n1*S*~n-100
		//	b1n1|c1n1													GLIP
		//	b1n1|c1n1
		// <GLIP>-<GLIP><MAP><REP>
		// {%.31%b1n1%.3%}-{%.21%c1n1%.5%}*S*~n-100
		//		{%.31%1d1%.3%}-{%.21%2u1%.5%}*S*		<-- GLIP-GLIP<MAP>
		//		n-100										<-- <REP>

		String LINsString = "";
		// REPEAR Section
		String[] a_strRepArray = a_strLIN.split("~");
		// Repeart split "~"
		// <LIP>-<LIP><MAP><REP>
		// {%.31%1d1%.3%}-{%.21%2u1%.5%}*NCCC/3=O~n-100
		//		{%.31%1d1%.3%}-{%.21%2u1%.5%}*NCCC/3=O		<-- a_strRepArray[0]
		//		n-100										<-- a_strRepArray[1]
		if (a_strRepArray.length > 1) {
			LINsString = a_strRepArray[0];
		}
		else {
			LINsString = a_strLIN;
		}

		// MAP Section
		String LINs = "";
		String t_strMAP = "";
		String[] LIPsMAP = this.splitString(LINsString, "*");
		// MAP split "*"
		//	{%.31%1d1%.3%}-{%.21%2u1%.5%}	<-- LIPsMAP[0]
		//	*NCCC/3=O						<-- LIPsMAP[1]
		if (LIPsMAP.length > 1 ) {
			LINs = LIPsMAP[0];
//			t_objLIN = new LIN(LIPsMAP[0], LIPsMAP[1]);
			t_strMAP = LIPsMAP[1];
			//a_objmod = new MOD(LIPsMAP[1]);
		}
/*		else {
			LINs = LINsString;
			t_objLIN = new LIN(LINs, "");
			//a_objmod = new MOD("");
		}
*/		LIN t_objLIN = new LIN(t_strMAP);

		// set Repeating unit
		// not  "~"    -> min =  0; max = 0
		// ~5          -> min =  5; max = 5
		// ~5-10       -> min =  5; max = 10
		// ~10-5       -> min =  5; max = 10  <--- sort
		// ~1-n       -> min =  1; max = -1  <---  n => -1
		// ~n          -> min = -1; max = -1
		// ~n-m is ~n 　-> min = -1; max = -1
		// ~n-m is ~n 　-> min = -1; max = -1
		if (a_strRepArray.length > 1) {
//			LINsString = a_strRepArray[0];

			int t_iMin = 0;
			int t_iMax = 0;

			String[] rep = a_strRepArray[1].split(":");
			if (rep.length > 1) {

				t_iMin = isNumber(rep[0]) ? Integer.parseInt(rep[0]) : -1;
				t_iMax = isNumber(rep[1]) ? Integer.parseInt(rep[1]) : -1;

				//TODO: write log Max -> Min; Min -> Max
				int temp_iMin = -1;
				if (t_iMin > t_iMax && t_iMax != -1) {
					temp_iMin = t_iMax;
					t_iMax = t_iMin;
					t_iMin = temp_iMin;
				}


				t_objLIN.setMinRepeatCount(t_iMin);
				t_objLIN.setMaxRepeatCount(t_iMax);
				t_objLIN.setRepeatingUnit(true);
//				java.lang.System.out.println("rep[true]:" + t_objLIN.isRepeatingUnit());
//				java.lang.System.out.println("rep[0]:" + rep[0]);
//				java.lang.System.out.println("rep[1]:" + rep[1]);
			}
			else {
//				java.lang.System.out.println("rep[true]:" + t_objLIN.isRepeatingUnit());
				t_iMin = isNumber(rep[0]) ? Integer.parseInt(rep[0]) : -1;
				t_objLIN.setMaxRepeatCount(t_iMin);
				t_objLIN.setMinRepeatCount(t_iMin);
				t_objLIN.setRepeatingUnit(true);
			}
		}


		// LIN separation
		String[] a_strLIPArray = LINs.split("-");
		// LIP List
		// {%.31%1d1%.3%}-{%.21%2u1%.5%}
		//	{%.31%1d1%.3%}				<---	a_strLIPArray[0]
		//	{%.21%2u1%.5%}				<---	a_strLIPArray[1]

		// generation for LINs
		for (String a_strGLIP : a_strLIPArray ) {

			GLIP t_oGLIP;
			if ( a_strGLIP.contains("|") ) {
			FuzzyGLIP t_oFuzzyGLIP = this.extractFuzzyGLIP(a_strGLIP);
				t_objLIN.addFuzzyGLIP(t_oFuzzyGLIP);
			} else {
				t_oGLIP = this.extractGLIP(a_strGLIP);
				t_objLIN.addGLIP(t_oGLIP);
			}

		}

		a_objWURCS.addLIN(t_objLIN);

		return a_objWURCS;
	}

	private GLIP extractGLIP(String a_strGLIP) {
		String prob = "(%(\\?|\\.[0-9]+)%)?";
		String nodeIndex = "(\\?|[a-zA-Z]+)";
		String SCPosition = "(\\?|[0-9]+)";
		String direction = "([nudtezx])?";
		String MAPPosition = "(\\?|[0-9]+)?";
		String strExp = prob+ nodeIndex+SCPosition+direction+MAPPosition +prob;
//		String strExp = "^([%]*)([.]*)([0-9?]*)([%]*)([a-zA-Z?\\\\]+)([0-9?\\\\]+)([a-zA-Z?\\\\]*)([0-9?\\\\]*)([%]*)([.]*)([0-9?]*)([%]*)";
		Matcher size = Pattern.compile(strExp).matcher(a_strGLIP);
		// %.21%a2u1%.5%
		//	group(0)	%.21%a2u1%.5%
		//	group(1)	%.21%
		//	group(2)	.21
		//	group(3)	a
		//	group(4)	2
		//	group(5)	u
		//	group(6)	1
		//	group(7)	%.5%
		//	group(8)	.5

		int t_iRESIndexGroup  = 3;
		int t_iSCPosGroup     = 4;
		int t_iDirectionGroup = 5;
		int t_iMAPPosGroup    = 6;
		String t_strRESIndex  = "";
		String t_strSCPos     = "";
		String t_strDirection = "";
		String t_strMAPPos = "";

		int t_iSC_Position = 0;
		char t_cDirection = ' ';
		int t_iModStarPositionOnMAP = 0;

		// TODO: Error message for not match
		if ( !size.find() )
			return null;

		t_strRESIndex  = size.group(t_iRESIndexGroup);
		t_strSCPos     = size.group(t_iSCPosGroup);
		t_strDirection = size.group(t_iDirectionGroup);
		t_strMAPPos    = size.group(t_iMAPPosGroup);

		// TODO: Error message for not number
		t_strSCPos = t_strSCPos.equals("?") ? "-1" : t_strSCPos ;

		if (! this.isNumber(t_strSCPos) )
			return null;

		t_iSC_Position = Integer.parseInt(t_strSCPos);

		if ( t_strDirection != null && t_strDirection.length() > 0)
			t_cDirection = t_strDirection.charAt(0);

		if (t_strMAPPos != null ) {
			// TODO: Error message for not number
			t_strMAPPos = t_strMAPPos.equals("?") ? "-1" : t_strMAPPos ;

			if (! this.isNumber(t_strMAPPos) )
				return null;
			t_iModStarPositionOnMAP = Integer.parseInt(t_strMAPPos);
		}

		GLIP a_oGLIP = new GLIP(t_strRESIndex, t_iSC_Position, t_cDirection, t_iModStarPositionOnMAP);

		return a_oGLIP;
	}

	private FuzzyGLIP extractFuzzyGLIP(String a_strFuzzyGLIP) {
		// set Alternative character in fuzzyGLIP
		// and repleace {,} to ""
		String strAlternative = "";
		if ( a_strFuzzyGLIP.contains("}") ) {
			strAlternative = "}";
			a_strFuzzyGLIP = a_strFuzzyGLIP.replace("}", "");
		}else if ( a_strFuzzyGLIP.contains("{") ) {
			strAlternative = "{";
			a_strFuzzyGLIP = a_strFuzzyGLIP.replace("{", "");
		}

		LinkedList<GLIP> t_aGLIP = new LinkedList<GLIP>();
		// separate Alternative GLIP "|"
		String[] GLIPList = a_strFuzzyGLIP.split("\\|");
		for ( String GLIP : GLIPList ) {
			t_aGLIP.addLast( this.extractGLIP(GLIP) );
		}
		return new FuzzyGLIP(t_aGLIP, strAlternative);
	}

	private String convertResidueID(int a_iResID) {
		//   1 ->  a
		//   2 ->  b
		//  26 ->  z
		//  27 ->  A
		//  28 ->  B
		//  52 ->  Z
		//  53 -> aa
		//  54 -> ab
		// 104 -> aZ
		// 105 -> ba

		String t_strRes = "";

		int t_iRemainder = a_iResID;
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
	 * Is the character a number(integer)?
	 * @param a_strNumber is a string
	 * @return if integer return true;
	 */
	public boolean isNumber(String a_strNumber) {
		try {
			Integer.parseInt(a_strNumber);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}

}
