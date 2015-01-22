package org.glycoinfo.WURCSFramework.util;

import java.text.NumberFormat;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

/**
 * Class for export string of WURCS
 * @author MasaakiMatsubara
 *
 */
public class WURCSExporter {

	private String m_strWURCS = "";

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
		for ( LIN t_oLIN : a_objWURCS.getLINs() ) {
			if (! t_strLINs.equals("") ) t_strLINs += "_";
			t_strLINs += this.getLINString(t_oLIN);
		}
		t_strWURCS += t_strLINs;

		System.out.println(t_strWURCS);
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
			t_strURES += "-" + a_oURES.getAnomericPosition() + a_oURES.getAnomericSymbol();
		}

		// MODs in UniqueRES
		for ( MOD t_oMOD : a_oURES.getMODs() ) {
			t_strURES += "_" + this.getMODString(t_oMOD);
		}
		return t_strURES;
	}

	/**
	 * Get string of MOD
	 * @param a_oMOD
	 * @return String of MOD
	 */
	public String getMODString( MOD a_oMOD ) {
		String t_strMOD = "";
		// LIP
		for ( LIP t_oLIP : a_oMOD.getLIPs() ) {
			if (! t_strMOD.equals("") ) t_strMOD += "-";
			t_strMOD += this.getLIPString(t_oLIP);
		}
		// FuzzyGLIP in LIN
		for ( FuzzyLIP t_oFLIP : a_oMOD.getFuzzyLIPs() ) {
			if (! t_strMOD.equals("") ) t_strMOD += "-";
			t_strMOD += this.getFuzzyLIPString(t_oFLIP);
		}

		// MAP in MOD
		t_strMOD += a_oMOD.getMAPCode();
//		t_strMOD += a_oMOD.getMODString();
		return t_strMOD;
	}

	/**
	 * Get string of LIN
	 * @param a_oLIN
	 * @return String of LIN
	 */
	public String getLINString( LIN a_oLIN ) {
		String t_strLIN = "";

		// GLIP in LIN
		for ( GLIP t_oGLIP : a_oLIN.getGLIPs() ) {
			if (! t_strLIN.equals("") ) t_strLIN += "-";
			t_strLIN += this.getGLIPString(t_oGLIP);
		}

		// FuzzyGLIP in LIN
		for ( FuzzyGLIP t_oFGLIP : a_oLIN.getFuzzyGLIPs() ) {
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

	/**
	 * Get string of FuzzyGLIP
	 * @param a_oFuzzyGLIP
	 * @return String of FuzzyGLIP
	 */
	public String getFuzzyGLIPString( FuzzyGLIP a_oFuzzyGLIP ) {
		String t_strFGLIP = "";

		// GLIP in FuzzyGLIP
		for ( GLIP t_oGLIP : a_oFuzzyGLIP.getFuzzyGLIPs() ) {
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

	/**
	 * Get string of FuzzyLIP
	 * @param a_oFuzzyLIP
	 * @return String of FuzzyLIP
	 */
	public String getFuzzyLIPString( FuzzyLIP a_oFuzzyLIP ) {
		String t_strFLIP = "";

		// LIP in FuzzyLIP
		for ( LIP t_oLIP : a_oFuzzyLIP.getFuzzyLIPs() ) {
			if (! t_strFLIP.equals("") ) t_strFLIP += "|";
			t_strFLIP += this.getLIPString(t_oLIP);
		}

		return t_strFLIP;
	}

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
