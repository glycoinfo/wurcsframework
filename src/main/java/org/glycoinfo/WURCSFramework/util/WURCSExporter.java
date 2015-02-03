package org.glycoinfo.WURCSFramework.util;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.comparator.GLIPComparator;
import org.glycoinfo.WURCSFramework.util.comparator.GLIPsComparator;
import org.glycoinfo.WURCSFramework.util.comparator.LINComparator;
import org.glycoinfo.WURCSFramework.util.comparator.LIPComparator;
import org.glycoinfo.WURCSFramework.util.comparator.LIPsComparator;
import org.glycoinfo.WURCSFramework.util.comparator.MODComparator;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

/**
 * Class for export string of WURCS contained sorting lists
 * @author MasaakiMatsubara
 *
 */
public class WURCSExporter {

	private LINComparator       m_oLINComp   = new LINComparator();
	private GLIPComparator      m_oGLIPComp  = new GLIPComparator();
	private GLIPsComparator     m_oGLIPsComp = new GLIPsComparator();
	private MODComparator       m_oMODComp   = new MODComparator();
	private LIPComparator       m_oLIPComp   = new LIPComparator();
	private LIPsComparator      m_oLIPsComp  = new LIPsComparator();

	/**
	 * Convert WURCSArray to WURCS string
	 * @param a_objWURCS WURCSArray object
	 */
	public String getWURCSString(WURCSArray a_objWURCS) {
		String t_strWURCS = "WURCS=";

		// Version
		t_strWURCS += a_objWURCS.getVersion();
		t_strWURCS += "/";

		// Unit count
		t_strWURCS
			+= a_objWURCS.getUniqueRESCount() +","+ a_objWURCS.getRESCount() +","+ a_objWURCS.getLINCount();
		t_strWURCS += "/";

		// Unique RESs
		for ( UniqueRES t_oURES : a_objWURCS.getUniqueRESs() ) {
			t_strWURCS += "[" +this.getUniqueRESString(t_oURES)+ "]";
		}
		t_strWURCS += "/";

		// RES
		String t_strRESs = "";
		for ( RES t_oRES : a_objWURCS.getRESs() ) {
			if (! t_strRESs.equals("") ) t_strRESs += "-";
			t_strRESs += t_oRES.getUniqueRESID();
		}
		t_strWURCS += t_strRESs;
		t_strWURCS += "/";

		// LIN
		String t_strLINs = "";
		LinkedList<LIN> t_aLINs = a_objWURCS.getLINs();
		Collections.sort( t_aLINs, this.m_oLINComp );
		for ( LIN t_oLIN : t_aLINs ) {
			if (! t_strLINs.equals("") ) t_strLINs += "_";
			t_strLINs += this.getLINString(t_oLIN);
		}
		t_strWURCS += t_strLINs;

//		System.out.println(t_strWURCS);
		return t_strWURCS;
	}

	/**
	 * Get string of UniqueRES
	 * @param a_oURES
	 * @return String of UniqueRES
	 */
	public String getUniqueRESString( UniqueRES a_oURES ) {

		// SkeletonCode in UniqueRES
		String t_strURES = a_oURES.getSkeletonCode();

		// Anomeric position and symbol in UniqueRES
		if ( a_oURES.getAnomericPosition() != 0 ) {
			String t_strAnomPos = ""+a_oURES.getAnomericPosition();
			if ( t_strAnomPos.equals("-1") ) t_strAnomPos = "?";
			t_strURES += "-" + t_strAnomPos + a_oURES.getAnomericSymbol();
		}

		// MODs in UniqueRES
		LinkedList<MOD> t_aMODs = a_oURES.getMODs();
		Collections.sort( t_aMODs, this.m_oMODComp );
		for ( MOD t_oMOD : t_aMODs )
			t_strURES += "_" + this.getMODString(t_oMOD);

		return t_strURES;
	}

	/**
	 * Get string of MOD
	 * @param a_oMOD
	 * @return String of MOD
	 */
/*
	public String getMODString( MOD a_oMOD ) {
		String t_strMOD = "";

		// LIP
		LinkedList<LIP> t_aLIPs = a_oMOD.getLIPs();
		Collections.sort( t_aLIPs, this.m_oLIPComp );
		for ( LIP t_oLIP : t_aLIPs ) {
			if (! t_strMOD.equals("") ) t_strMOD += "-";
			t_strMOD += this.getLIPString(t_oLIP);
		}

		// FuzzyGLIP in LIN
		LinkedList<FuzzyLIP> t_aFLIPs = a_oMOD.getFuzzyLIPs();
		Collections.sort( t_aFLIPs, this.m_oFLIPComp );
		for ( FuzzyLIP t_oFLIP : t_aFLIPs ) {
			if (! t_strMOD.equals("") ) t_strMOD += "-";
			t_strMOD += this.getFuzzyLIPString(t_oFLIP);
		}

		// MAP in MOD
		t_strMOD += a_oMOD.getMAPCode();
//		t_strMOD += a_oMOD.getMODString();
		return t_strMOD;
	}
*/
	public String getMODString( MOD a_oMOD ) {
		String t_strMOD = "";

		// List of GLIP
		LinkedList<LIPs> t_aListOfLIPs = a_oMOD.getListOfLIPs();
		Collections.sort( t_aListOfLIPs, this.m_oLIPsComp );
		for ( LIPs t_oLIPs : t_aListOfLIPs ) {
			if (! t_strMOD.equals("") ) t_strMOD += "-";
			t_strMOD += this.getLIPsString(t_oLIPs);
		}

		// MAP in MOD
		t_strMOD += a_oMOD.getMAPCode();
//		t_strMOD += a_oMOD.getMODString();
		return t_strMOD;
	}

	/**
	 * Get string of list of LIP
	 * @param a_oLIPs LIPs (list of LIP)
	 * @return String of list of LIP
	 */
	public String getLIPsString( LIPs a_oLIPs ) {
		String t_strLIPs = "";

		// LIP in FuzzyLIP
		LinkedList<LIP> t_aLIPs = a_oLIPs.getLIPs();
		Collections.sort( t_aLIPs, this.m_oLIPComp );
		for ( LIP t_oLIP : t_aLIPs ) {
			if (! t_strLIPs.equals("") ) t_strLIPs += "|";
			t_strLIPs += this.getLIPString(t_oLIP);
		}

		return t_strLIPs;
	}

	/**
	 * Get string of LIN
	 * @param a_oLIN
	 * @return String of LIN
	 */
/*
	public String getLINString( LIN a_oLIN ) {
		String t_strLIN = "";

		// GLIP in LIN
		LinkedList<GLIP> t_aGLIPs = a_oLIN.getGLIPs();
		Collections.sort( t_aGLIPs, this.m_oGLIPComp );
		for ( GLIP t_oGLIP : t_aGLIPs ) {
			if (! t_strLIN.equals("") ) t_strLIN += "-";
			t_strLIN += this.getGLIPString(t_oGLIP);
		}

		// FuzzyGLIP in LIN
		LinkedList<FuzzyGLIP> t_aFGLIPs = a_oLIN.getFuzzyGLIPs();
		Collections.sort( t_aFGLIPs, this.m_oFGLIPComp );
		for ( FuzzyGLIP t_oFGLIP : t_aFGLIPs ) {
			if (! t_strLIN.equals("") ) t_strLIN += "-";
			t_strLIN += this.getFuzzyGLIPString(t_oFGLIP);
		}

		// MAP in LIN
		t_strLIN += a_oLIN.getMAPCode();

		// Repeat
		if ( a_oLIN.isRepeatingUnit() ) {
			t_strLIN += "~";
			int t_iMinRep = a_oLIN.getMinRepeatCount();
			int t_iMaxRep = a_oLIN.getMaxRepeatCount();
			t_strLIN += (t_iMinRep == -1)? "n" : ""+t_iMinRep;
			if ( t_iMinRep != t_iMaxRep )
				t_strLIN += (t_iMaxRep == -1)? ":n" : ":"+t_iMaxRep;
		}

		return t_strLIN;
	}
*/
	public String getLINString( LIN a_oLIN ) {
		String t_strLIN = "";

		// List of GLIP
		LinkedList<GLIPs> t_aGLIPs = a_oLIN.getListOfGLIPs();
		Collections.sort( t_aGLIPs, this.m_oGLIPsComp );
		for ( GLIPs t_oGLIPs : t_aGLIPs ) {
			if (! t_strLIN.equals("") ) t_strLIN += "-";
			t_strLIN += this.getGLIPsString(t_oGLIPs);
		}

		// MAP in LIN
		t_strLIN += a_oLIN.getMAPCode();

		// Repeat
		if ( a_oLIN.isRepeatingUnit() ) {
			t_strLIN += "~";
			int t_iMinRep = a_oLIN.getMinRepeatCount();
			int t_iMaxRep = a_oLIN.getMaxRepeatCount();
			t_strLIN += (t_iMinRep == -1)? "n" : ""+t_iMinRep;
			if ( t_iMinRep != t_iMaxRep )
				t_strLIN += (t_iMaxRep == -1)? ":n" : ":"+t_iMaxRep;
		}

		return t_strLIN;
	}


	/**
	 * Get string of list of GLIP
	 * @param a_oGLIPs GLIPs (list of GLIP)
	 * @return String of GLIPs
	 */
	public String getGLIPsString( GLIPs a_oGLIPs ) {
		String t_strGLIPs = "";

		// List of GLIPs
		LinkedList<GLIP> t_aGLIPs = a_oGLIPs.getGLIPs();
		Collections.sort( t_aGLIPs, this.m_oGLIPComp );
		for ( GLIP t_oGLIP : t_aGLIPs ) {
			if (! t_strGLIPs.equals("") ) t_strGLIPs += "|";
			t_strGLIPs += this.getGLIPString(t_oGLIP);
		}

		// Alternative
		if ( a_oGLIPs.getAlternativeType() == null )
			return t_strGLIPs;

		if ( a_oGLIPs.getAlternativeType().equals("{") )
			t_strGLIPs = "{" + t_strGLIPs;
		if ( a_oGLIPs.getAlternativeType().equals("}") )
			t_strGLIPs += "}";

		return t_strGLIPs;
	}

	/**
	 * Get string of FuzzyGLIP
	 * @param a_oFuzzyGLIP
	 * @return String of FuzzyGLIP
	 */
/*
	public String getFuzzyGLIPString( FuzzyGLIP a_oFuzzyGLIP ) {
		String t_strFGLIP = "";

		// GLIP in FuzzyGLIP
		LinkedList<GLIP> t_aGLIPs = a_oFuzzyGLIP.getGLIPs();
		Collections.sort( t_aGLIPs, this.m_oGLIPComp );
		for ( GLIP t_oGLIP : t_aGLIPs ) {
			if (! t_strFGLIP.equals("") ) t_strFGLIP += "|";
			t_strFGLIP += this.getGLIPString(t_oGLIP);
		}

		// Alternative
		if ( a_oFuzzyGLIP.getAlternativeType().equals("{") )
			t_strFGLIP = "{" + t_strFGLIP;
		if ( a_oFuzzyGLIP.getAlternativeType().equals("}") )
			t_strFGLIP += "}";

		return t_strFGLIP;
	}
*/
	/**
	 * Get string of FuzzyLIP
	 * @param a_oFuzzyLIP
	 * @return String of FuzzyLIP
	 */
/*
	public String getFuzzyLIPString( FuzzyLIP a_oFuzzyLIP ) {
		String t_strFLIP = "";

		// LIP in FuzzyLIP
		LinkedList<LIP> t_aLIPs = a_oFuzzyLIP.getLIPs();
		Collections.sort( t_aLIPs, this.m_oLIPComp );
		for ( LIP t_oLIP : t_aLIPs ) {
			if (! t_strFLIP.equals("") ) t_strFLIP += "|";
			t_strFLIP += this.getLIPString(t_oLIP);
		}

		return t_strFLIP;
	}
*/
	/**
	 * Get string of GLIP
	 * @param a_oGLIP
	 * @return String of GLIP
	 */
	public String getGLIPString( GLIP a_oGLIP ) {
		String t_strGLIP = a_oGLIP.getRESIndex();
		t_strGLIP += getLIPString( a_oGLIP.getBackbonePosition(), a_oGLIP.getBackboneDirection(), a_oGLIP.getModificationPosition() );

		// Probabilities
		String t_strBProb = this.getProbabilityString( a_oGLIP.getBackboneProbabilityLower(), a_oGLIP.getBackboneProbabilityUpper() );
		String t_strMProb = this.getProbabilityString( a_oGLIP.getModificationProbabilityLower(), a_oGLIP.getModificationProbabilityUpper() );
		t_strGLIP = t_strBProb + t_strGLIP + t_strMProb;

		return t_strGLIP;
	}

	/**
	 * Get string of LIP
	 * @param a_oLIP
	 * @return String of LIP
	 */
	public String getLIPString( LIP a_oLIP ) {
		String t_strLIP = "";
		t_strLIP = this.getLIPString(a_oLIP.getBackbonePosition(), a_oLIP.getBackboneDirection(), a_oLIP.getModificationPosition() );

		// Probabilities
		String t_strBProb = this.getProbabilityString( a_oLIP.getBackboneProbabilityLower(), a_oLIP.getBackboneProbabilityUpper() );
		String t_strMProb = this.getProbabilityString( a_oLIP.getModificationProbabilityLower(), a_oLIP.getModificationProbabilityUpper() );
		t_strLIP = t_strBProb + t_strLIP + t_strMProb;

		return t_strLIP;
	}

	/**
	 * Get string of LIP for parameters
	 * @param a_iSCPos
	 * @param a_cDirection
	 * @param a_iMAPPos
	 * @return String of LIP
	 */
	private String getLIPString( int a_iSCPos, char a_cDirection, int a_iMAPPos ) {
		String t_strLIP = "";
		String t_strSCPos = ""+a_iSCPos;
		if ( t_strSCPos.equals("-1") ) t_strSCPos = "?";
		t_strLIP += t_strSCPos;
		if ( a_cDirection != ' ' )
			t_strLIP += a_cDirection;
		if ( a_iMAPPos != 0 ) {
			String t_strMAPPos = ""+a_iMAPPos;
			if ( t_strMAPPos.equals("-1") ) t_strMAPPos = "?";
			t_strLIP += t_strMAPPos;
		}
		return t_strLIP;
	}

	/**
	 * Get string of probabilities of LIP or GLIP
	 * @param a_dLower
	 * @param a_dUpper
	 * @return String of probabilities
	 */
	private String getProbabilityString( double a_dLower, double a_dUpper ) {
		if ( a_dLower == 1.0 ) return "";

		String t_strProb = "";
		t_strProb = ( a_dLower < 0.0 )? "?" : NumberFormat.getNumberInstance().format( a_dLower ).substring(1);
		if ( a_dUpper != a_dLower ) {
			t_strProb += ":";
			t_strProb += ( a_dUpper < 0.0 )? "?" : NumberFormat.getNumberInstance().format( a_dUpper ).substring(1);
		}
		return "%"+t_strProb+"%";
	}
}
