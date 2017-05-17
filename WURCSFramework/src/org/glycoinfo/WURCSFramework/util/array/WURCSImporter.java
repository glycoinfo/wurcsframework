package org.glycoinfo.WURCSFramework.util.array;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;

/**
 * Importer class for WURCSArray from WURCS string
 * @author ShinichiroTsuchiya
 * @author IssakuYamada
 * @author MasaakiMatsubara
 *
 */
public class WURCSImporter {

	/**
	 * @see #extractWURCSArray(String, boolean)
	 */
	public WURCSArray extractWURCSArray(String a_strWURCS) throws WURCSFormatException {
		return this.extractWURCSArray(a_strWURCS, true);
	}

	/**
	 * Extract WURCSArray from the string
	 * @param a_strWURCS String of WURCS
	 * @param a_bCheckStrict Flag for strict checking format
	 * @return WURCSArray
	 * @throws WURCSFormatException
	 */
	public WURCSArray extractWURCSArray(String a_strWURCS, boolean a_bCheckStrict) throws WURCSFormatException {

//		java.lang.System.out.println(a_strWURCS);
		String t_strVersion =      "";
		int t_numUniqueRES =        0;
		int t_numRES =              0;
		int t_numLIN =              0;
		boolean t_bIsComposition =  false;
		String t_strUniqueRESs =   "";
		String t_strRESSequence = "";
		String t_strLINs =         "";

		//String strExp = "WURCS=(.+)\\/(\\d+),(\\d+),(\\d+)\\/(.+)\\]/([0-9-]+)/(.+)";
		String strExp = "WURCS=(.+)/(\\d+),(\\d+),(\\d+)(\\+)?/\\[(.+)\\]/([\\d\\-<>]+)/(.*)";

		Matcher t_oMatcher = Pattern.compile(strExp).matcher(a_strWURCS);
		//WURCS=2.0/7,10,9/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-4-2-5-4-2-6-7/a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6
		//		group(0)	WURCS=2.0/7,10,9/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5]/1-2-3-4-2-5-4-2-6-7/a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6
		//		group(1)	2.0
		//		group(2)	7
		//		group(3)	10
		//		group(4)	9
		//		group(5)	""
		//		group(6)	a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5
		//		group(7)	1-2-3-4-2-5-4-2-6-7
		//		group(8)	a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6
		int t_iVersion      = 1;
		int t_iNumuRES      = 2;
		int t_iNumRES       = 3;
		int t_iNumLIN       = 4;
		int t_iComposition  = 5;
		int t_iUniqueRESs   = 6;
		int t_iResSequenses = 7;
		int t_iLINs         = 8;
//		if ( !wurcsMatch.matches() )
//			return null;
		//if ( !wurcsMatch.find() )
		//return null;
		if ( ! t_oMatcher.find() )
			throw new WURCSFormatException("Not match as WURCS.", a_strWURCS);

		t_strVersion     =                  t_oMatcher.group(t_iVersion);
		t_numUniqueRES   = Integer.parseInt(t_oMatcher.group(t_iNumuRES));
		t_numRES         = Integer.parseInt(t_oMatcher.group(t_iNumRES));
		t_numLIN         = Integer.parseInt(t_oMatcher.group(t_iNumLIN));
		t_bIsComposition =                 (t_oMatcher.group(t_iComposition) != null);
		t_strUniqueRESs  =                  t_oMatcher.group(t_iUniqueRESs);
		t_strRESSequence =                  t_oMatcher.group(t_iResSequenses);
		t_strLINs        =                  t_oMatcher.group(t_iLINs);

		WURCSArray t_objWURCSContainer = new WURCSArray(t_strVersion, t_numUniqueRES, t_numRES, t_numLIN, t_bIsComposition);

		// Extract UniqueRES list
		// a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O][a2112h-1b_1-5][a1221m-1a_1-5
		// Split by "]["
		String[] t_aURESs = t_strUniqueRESs.split("\\]\\[");
		HashSet<String> t_aURESsCheck = new HashSet<String>();
		int count = 0; // count of uniqueRES
		for(String t_strURES : t_aURESs) {
			if (t_strURES.length() == 0) continue;
			count++;
			if ( a_bCheckStrict && t_aURESsCheck.contains(t_strURES) ) {
				throw new WURCSFormatException("Duplicated UniqueRES is found.", t_strURES);
			}
			t_aURESsCheck.add(t_strURES);
			UniqueRES t_oURES = this.extractUniqueRES(t_strURES, count);
			t_objWURCSContainer.addUniqueRES(t_oURES);
		}

		if ( a_bCheckStrict && t_numUniqueRES != count )
			throw new WURCSFormatException("Number of UniqueRES is not correct.", t_numUniqueRES+" vs "+count);

		// Extract RES sequence
		// 1-2-3-4-2-5-4-2-6-7
		TreeSet<Integer> t_aUsedURESID = new TreeSet<Integer>();
		count = 1; // count RES
		int t_iRep = 0;
		LinkedList<Integer> t_aRepIDs = new LinkedList<Integer>();
		for ( int i=0; i< t_strRESSequence.length(); i++ ) {
			char t_cSeq = t_strRESSequence.charAt(i);
			if ( t_cSeq == '-' ) count++;
			if ( t_cSeq == '<' ) {
				t_iRep++;
				t_aRepIDs.addLast(t_iRep);
			}
			if ( t_cSeq == '>' ) {
				t_aRepIDs.removeLast();
			}
			if ( !Character.isDigit(t_cSeq) ) continue;

			// Read string of IndexID
			String t_strRES = "";
			char t_cSeqNext = t_cSeq;
			while ( Character.isDigit(t_cSeqNext) ) {
				t_strRES += t_cSeqNext;
				i++;
				if ( i > t_strRESSequence.length()-1 ) break;
				t_cSeqNext = t_strRESSequence.charAt(i);
			}
			i--;
			int t_iRESID = Integer.parseInt(t_strRES);
			t_aUsedURESID.add(t_iRESID);
			// UniqueRES ID
			if ( t_iRESID > t_numUniqueRES )
				throw new WURCSFormatException("Exceeded UniqueRES ID is found in RES sequence.", t_iRESID+" in "+t_strRESSequence);

			RES t_objRES = new RES( t_iRESID, WURCSDataConverter.convertRESIDToIndex(count) );
			if ( !t_aRepIDs.isEmpty() )
				t_objRES.setRepeatIDs(t_aRepIDs);

			t_objWURCSContainer.addRES(t_objRES);
		}
		if ( t_aUsedURESID.size() < t_numUniqueRES )
			throw new WURCSFormatException("Some UniqueRES is not used in RES sequence.", t_aUsedURESID.toString());

/*
		// Split by "-"
		String[] t_aRESSequence = t_strRESSequence.split("\\-");
		count = 1; // count RES
		for(String t_strRES : t_aRESSequence) {
			if (t_strRES.length() == 0) continue;
			if (! t_strRES.matches("\\d+") )
				throw new WURCSFormatException("UniqueRES ID in RES sequence must be numeric: "+t_strRESSequence);

			count++;
			RES t_objRES = new RES( Integer.parseInt(t_strRES), WURCSDataConverter.convertRESIDToIndex(count) );
			t_objWURCSContainer.addRES(t_objRES);
		}
*/
		if ( a_bCheckStrict && t_numRES != count )
			throw new WURCSFormatException("Number of RES is not correct.", t_numRES+" vs "+count);

		if ( t_strLINs == null || t_strLINs.equals("") )
			return t_objWURCSContainer;

		// Extract LIN list
		// a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6
		// Split by "_"
		String[] t_aLINs = t_strLINs.split("_");

		count = 0; // count LIN
		for(String t_strLIN : t_aLINs) {
			count++;
			t_objWURCSContainer.addLIN( this.extractLIN(t_strLIN) );
//			t_objWURCSContainer = this.extractLINs(t_strLIN, t_objWURCSContainer);
		}

		if ( a_bCheckStrict && t_numLIN != count )
			throw new WURCSFormatException("Number of LIN is not correct.", t_numLIN+" vs "+count);

		return t_objWURCSContainer;
	}

	/**
	 * Extract UniqueRES from the string
	 * @see #extractMS(String)
	 * @param a_strURES String of UniqueRES
	 * @param a_iURESID Index number of UniqueRES in the list
	 * @return UniqueRES
	 * @throws WURCSFormatException
	 */
	public UniqueRES extractUniqueRES(String a_strURES, int a_iURESID) throws WURCSFormatException {
		return new UniqueRES(a_iURESID, this.extractMS(a_strURES) );
	}

	/**
	 * Extract MS from the string
	 * @param a_strMS String of MS
	 * @return UniqueRES
	 * @throws WURCSFormatException
	 */
	public MS extractMS( String a_strMS ) throws WURCSFormatException {
		// input: a2122h-1b_1-5_2*NCC/3=O
		// Split SkeletonCode and MODs
		String[] t_aSplitMS = a_strMS.split("_");

		// t_aSplitRES[0] = a2122h-1b  // basetype
		// t_aSplitRES[1] = 1-5        // MOD 1
		// t_aSplitRES[2] = 2*NCC/3=O  // MOD 2

		String[] t_aSplitSC = t_aSplitMS[0].split("-");

		// SkeletonCode and anomeric information
		String t_strSkeletonCode   = t_aSplitSC[0];
		int    t_iAnomericPosition = 0;
		char   t_cAnomericSymbol   = 'o';
		// if Anomeric information is exist
		if ( t_aSplitSC.length > 1 ) {
			String strExp = "(\\?|[0-9]+)([abudxo])";
			Matcher matchAnomerInfo = Pattern.compile(strExp).matcher(t_aSplitSC[1]);

			if (! matchAnomerInfo.find() )
				throw new WURCSFormatException("Error in extract anomeric information.", a_strMS);

			t_iAnomericPosition = (matchAnomerInfo.group(1).equals("?") ? -1 : Integer.parseInt(matchAnomerInfo.group(1))) ;
			t_cAnomericSymbol = matchAnomerInfo.group(2).toCharArray()[0];
		}
		MS t_oMS = new MS(t_strSkeletonCode, t_iAnomericPosition, t_cAnomericSymbol );

		// MODs
		for ( int i=1; i<t_aSplitMS.length; i++ ) {
			String t_strMOD = t_aSplitMS[i];
			MOD t_oMOD = this.extractMOD(t_strMOD);
			t_oMS.addMOD(t_oMOD);
		}
		return t_oMS;
	}

	/**
	 * Extract MOD from the string
	 * @param a_strMOD String of MOD
	 * @return MOD
	 * @throws WURCSFormatException
	 */
	public MOD extractMOD( String a_strMOD ) throws WURCSFormatException {
		// Extract MAP
		String t_strMAP = "";
		String t_strLIPs = a_strMOD;
		if ( a_strMOD.contains("*") ) {
			t_strMAP  = a_strMOD.substring( a_strMOD.indexOf("*") );
			t_strLIPs = a_strMOD.substring(0, a_strMOD.indexOf("*") );
		}

		MOD t_oMOD = new MOD(t_strMAP);

		// LIPs and FuzzyLIPs
		for ( String t_strLIP : t_strLIPs.split("-") ) {
/*
			if ( t_strLIP.contains("|") ) {
				FuzzyLIP t_oFLIP = this.extractFuzzyLIP(t_strLIP);
				t_oMOD.addFuzzyLIP(t_oFLIP);
			} else {
				LIP t_oLIP = this.extractLIP(t_strLIP);
				t_oMOD.addLIP(t_oLIP);
			}
*/
			t_oMOD.addLIPs(this.extractLIPs(t_strLIP));
		}
		return t_oMOD;
	}

	/**
	 * Extract List of LIPs from the string
	 * @param a_strLIPs
	 * @return LIPs (List of LIP)
	 * @throws WURCSFormatException
	 */
	public LIPs extractLIPs(String a_strLIPs) throws WURCSFormatException {
		LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();
		// separate Alternative GLIP "|"
		for ( String t_strLIP : a_strLIPs.split("\\|") ) {
			t_aLIPs.addLast( this.extractLIP(t_strLIP) );
		}
		return new LIPs(t_aLIPs);
	}

	/**
	 * Extract fuzzy LIP from the string
	 * @param a_strFuzzyLIP String of FuzzyLIP
	 * @return FuzzyLIP
	 * @throws WURCSFormatException
	 */
/*
	public FuzzyLIP extractFuzzyLIP( String a_strFuzzyLIP ) throws WURCSFormatException {
		LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();
		// separate Alternative GLIP "|"
		for ( String t_strLIP : a_strFuzzyLIP.split("\\|") ) {
			t_aLIPs.addLast( this.extractLIP(t_strLIP) );
		}
		return new FuzzyLIP(t_aLIPs);

	}
*/

	/**
	 * Extract LIP from the string
	 * @param a_strLIP String of LIP
	 * @return LIP
	 * @throws WURCSFormatException
	 */
	public LIP extractLIP( String a_strLIP ) throws WURCSFormatException {
		String SCPosition  = "(\\?|[0-9]+)";
		String direction   = "([nudtezx])?";   // Can omit
		String MAPPosition = "(\\?|[0-9]+)?";  // Can omit
		String prob        = "(%(.+)%)?";      // Can omit
		String strExp = "^"+prob+ SCPosition+direction+MAPPosition +prob+"$";
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

		if ( !size.find() )
			throw new WURCSFormatException("Not match as LIP.", a_strLIP);

		String t_strSCPos     = size.group(3);
		String t_strDirection = size.group(4);
		String t_strMAPPos    = size.group(5);

		int t_iSCPos = 0;
		char t_cDirection = ' ';
		int t_iMAPPos = 0;

		if ( t_strSCPos.equals("?") ) t_strSCPos = "-1";
		if (! WURCSNumberUtils.isInteger(t_strSCPos) )
			throw new WURCSFormatException("SkeletonCode position must be number in LIP.", a_strLIP);

		t_iSCPos = Integer.parseInt(t_strSCPos);

		if ( t_strDirection != null )
			t_cDirection = t_strDirection.charAt(0);

		if (t_strMAPPos != null ) {
			if ( t_strMAPPos.equals("?") ) t_strMAPPos = "-1";

			if (! WURCSNumberUtils.isInteger(t_strMAPPos) )
				throw new WURCSFormatException("MAP position must be number in LIP.", a_strLIP);
			t_iMAPPos = Integer.parseInt(t_strMAPPos);
		}

		LIP t_oLIP = new LIP(t_iSCPos, t_cDirection, t_iMAPPos);

		// Probabilities
		String t_strBackboneProb = size.group(2);
		String t_strModificationProb = size.group(7);
		if ( t_strBackboneProb != null ) {
//			System.out.println(t_strBackboneProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strBackboneProb);
			t_oLIP.setBackboneProbabilityLower( t_aProbs[0] );
			t_oLIP.setBackboneProbabilityUpper( t_aProbs[1] );
		}

		if ( t_strModificationProb != null ) {
//			System.out.println(t_strModificationProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strModificationProb);
			t_oLIP.setModificationProbabilityLower( t_aProbs[0] );
			t_oLIP.setModificationProbabilityUpper( t_aProbs[1] );
		}

		return t_oLIP;

	}

	/**
	 * Extract LIN from the string
	 * @param a_strLIN String of LIN
	 * @return LIN
	 * @throws WURCSFormatException
	 */
	public LIN extractLIN( String a_strLIN ) throws WURCSFormatException {
		String t_strLIN = a_strLIN;

		// Extract repeat section
		String t_strRep = "";
		if ( a_strLIN.contains("~") ) {
			t_strRep = a_strLIN.split("~")[1];
			t_strLIN = a_strLIN.split("~")[0];
		}

		// Extract MAP
		String t_strMAP = "";
		if ( t_strLIN.contains("*") ) {
			t_strMAP = t_strLIN.substring( t_strLIN.indexOf('*') );
			t_strLIN = t_strLIN.substring( 0, t_strLIN.indexOf('*') );
		}

		LIN t_objLIN = new LIN(t_strMAP);

		String[] t_aGLIPs = t_strLIN.split("\\-");
		for (String t_strGLIP : t_aGLIPs ) {
/*
			if ( t_strGLIP.contains("|") ) {
				FuzzyGLIP t_oFuzzyGLIP = this.extractFuzzyGLIP(t_strGLIP);
				t_objLIN.addFuzzyGLIP(t_oFuzzyGLIP);
			} else {
				GLIP t_oGLIP = this.extractGLIP(t_strGLIP);
				t_objLIN.addGLIP(t_oGLIP);
			}
*/
			t_objLIN.addGLIPs( this.extractGLIPs(t_strGLIP));

		}

		// If no repeat
		if ( t_strRep.equals("") ) return t_objLIN;

		int[] t_aRep = this.extractRepeat(t_strRep);
		t_objLIN.setMinRepeatCount(t_aRep[0]);
		t_objLIN.setMaxRepeatCount(t_aRep[1]);
		t_objLIN.setRepeatingUnit(true);

		return t_objLIN;
	}
	/**
	 * Extract list of GLIP from the string
	 * @param a_strGLIPs String of list of GLIP
	 * @return GLIPs (list of GLIP)
	 * @throws WURCSFormatException
	 */
	public GLIPs extractGLIPs(String a_strGLIPs) throws WURCSFormatException {
		// set Alternative character in GLIPs
		// and repleace {,} to ""
		String strAlternative = "";
		if ( a_strGLIPs.contains("}") ) {
			strAlternative = "}";
			a_strGLIPs = a_strGLIPs.replace("}", "");
		}else if ( a_strGLIPs.contains("{") ) {
			strAlternative = "{";
			a_strGLIPs = a_strGLIPs.replace("{", "");
		}

		LinkedList<GLIP> t_aGLIP = new LinkedList<GLIP>();
		// separate Alternative GLIP "|"
		String[] GLIPList = a_strGLIPs.split("\\|");
		for ( String GLIP : GLIPList ) {
			t_aGLIP.addLast( this.extractGLIP(GLIP) );
		}

		GLIPs t_oGLIPs = new GLIPs(t_aGLIP);
		if (! strAlternative.equals("") )
			t_oGLIPs.setAlternative(strAlternative);

		return t_oGLIPs;
	}


	/**
	 * Extract FuzzyGLIP from the string
	 * @param a_strFuzzyGLIP String of FuzzyGLIP
	 * @return FuzzyGLIP
	 * @throws WURCSFormatException
	 */
/*
	public FuzzyGLIP extractFuzzyGLIP(String a_strFuzzyGLIP) throws WURCSFormatException {
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
*/

	/**
	 * Extract GLIP from the string
	 * @param a_strGLIP String of GLIP
	 * @return GLIP
	 * @throws WURCSFormatException
	 */
	public GLIP extractGLIP(String a_strGLIP) throws WURCSFormatException {
		String nodeIndex   = "(\\?|[a-zA-Z]+)";
		String SCPosition  = "(\\?|[0-9]+)";
		String direction   = "([nudtezx])?";    // Can omit
		String MAPPosition = "(\\?|[0-9]+)?";   // Can omit
		String prob        = "(%(.+)%)?";       // Can omit
		String strExp = "^"+prob+ nodeIndex+SCPosition+direction+MAPPosition +prob+"$";
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

		if ( !size.find() )
			throw new WURCSFormatException("Not match as GLIP.", a_strGLIP);

		String t_strRESIndex  = size.group(3);
		String t_strSCPos     = size.group(4);
		String t_strDirection = size.group(5);
		String t_strMAPPos    = size.group(6);

		int  t_iSC_Position          = 0;
		char t_cDirection            = ' ';
		int  t_iModStarPositionOnMAP = 0;

		// SkeletonCode position
		t_strSCPos = t_strSCPos.equals("?") ? "-1" : t_strSCPos ;
		if (! WURCSNumberUtils.isInteger(t_strSCPos) )
			throw new WURCSFormatException("SkeletonCode position must be number in GLIP.", a_strGLIP);

		t_iSC_Position = Integer.parseInt(t_strSCPos);

		// Direction
		if ( t_strDirection != null && t_strDirection.length() > 0)
			t_cDirection = t_strDirection.charAt(0);

		// MAP position
		if (t_strMAPPos != null ) {
			t_strMAPPos = t_strMAPPos.equals("?") ? "-1" : t_strMAPPos ;

			if (! WURCSNumberUtils.isInteger(t_strMAPPos) )
				throw new WURCSFormatException("MAP position must be number in GLIP.", a_strGLIP);
			t_iModStarPositionOnMAP = Integer.parseInt(t_strMAPPos);
		}

		GLIP t_oGLIP = new GLIP(t_strRESIndex, t_iSC_Position, t_cDirection, t_iModStarPositionOnMAP);

		// Probabilities
		String t_strBackboneProb = size.group(2);
		String t_strModificationProb = size.group(8);
		if ( t_strBackboneProb != null ) {
//			System.out.println(t_strBackboneProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strBackboneProb);
			t_oGLIP.setBackboneProbabilityLower( t_aProbs[0] );
			t_oGLIP.setBackboneProbabilityUpper( t_aProbs[1] );
		}

		if ( t_strModificationProb != null ) {
//			System.out.println(t_strModificationProb);
			// Extract Probabilities
			double[] t_aProbs = this.extractProbabilities(t_strModificationProb);
			t_oGLIP.setModificationProbabilityLower( t_aProbs[0] );
			t_oGLIP.setModificationProbabilityUpper( t_aProbs[1] );
		}

		return t_oGLIP;
	}

	/**
	 * Extract probabilities in LIP/GLIP from the string
	 * @param a_strProb String of probabilities in LIP/GLIP
	 * @return Values of min and max probabilities
	 * @throws WURCSFormatException
	 */
	private double[] extractProbabilities( String a_strProb ) throws WURCSFormatException {
		// t_adProbs[0] : lower probability
		// t_adProbs[1] : Upper probability
		double[] t_adProbs = {1.0, 1.0};

		String[] t_asProbs = a_strProb.split(":");
		if ( t_asProbs[0].matches("[^\\?\\.0-9]") )
			throw new WURCSFormatException("Not match as probability in LIP/GLIP.", a_strProb);

		// Lower
		if ( t_asProbs[0].equals("?") ) t_asProbs[0] = "-1";
//		System.out.println(t_asProbs[0]);
		if (! WURCSNumberUtils.isDouble(t_asProbs[0]) )
			throw new WURCSFormatException("probability must be double type number.", a_strProb);

		t_adProbs[0] = Double.parseDouble(t_asProbs[0]);
		t_adProbs[1] = t_adProbs[0];
		// Upper (if exist)
		if ( t_asProbs.length > 1 ) {
			if ( t_asProbs[1].matches("[^\\?\\.0-9]") )
				throw new WURCSFormatException("Not match as probability in LIP/GLIP.", a_strProb);

			if ( t_asProbs[1].equals("?") ) t_asProbs[1] = "-1";
			if (! WURCSNumberUtils.isDouble(t_asProbs[1]) )
				throw new WURCSFormatException("probability must be double type number.", a_strProb);
			t_adProbs[1] = Double.parseDouble(t_asProbs[1]);
		}

		return t_adProbs;
	}

	/**
	 * Extract repeat counts in LIN from the string
	 * @param a_strRepeat String of repeat counts in LIN
	 * @return Repeat count(s)
	 * @throws WURCSFormatException
	 */
	private int[] extractRepeat(String a_strRepeat) throws WURCSFormatException {
		// 5    : one parameter 5 repeat count
		// n    : one parameter unkown repeat count
		// 5-10 : two parameter 5 to 10 repeat count
		// n-10 : two parameter lower 10 repeat count
		// 5-n  : two parameter upper 5 repeat count
		// n-m  : not allow "m"
		// n-n  : not allow unkown to unkown, it must be one parameter "n"

		// t_aiReps[0] : Min repeat count
		// t_aiReps[1] : Max repeat count
		int[] t_aiReps = {1, 1};
		// Split min and max
		String[] t_asRep = a_strRepeat.split(":");

		if ( t_asRep[0].equals("n") ) t_asRep[0] = "-1";
		if (! WURCSNumberUtils.isInteger( t_asRep[0] ) )
			throw new WURCSFormatException("Repeat unit is must be a number or \"n\".", "~"+a_strRepeat);

		t_aiReps[0] = Integer.parseInt(t_asRep[0]);
		t_aiReps[1] = t_aiReps[0];

		// Repeat is only one parameter
		if (t_asRep.length == 1) return t_aiReps;


		if ( t_asRep[1].equals("n") ) t_asRep[1] = "-1";
		if (! WURCSNumberUtils.isInteger( t_asRep[1] ) )
			throw new WURCSFormatException("Repeat unit is must be number or \"n\".", "~"+a_strRepeat);

		t_aiReps[1] = Integer.parseInt(t_asRep[1]);

		//TODO: write log Max -> Min; Min -> Max
		int temp_iMin = -1;
		if (t_aiReps[0] > t_aiReps[1] && t_aiReps[1] != -1) {
			temp_iMin = t_aiReps[1];
			t_aiReps[1] = t_aiReps[0];
			t_aiReps[0] = temp_iMin;
		}

		return t_aiReps;
	}

}
