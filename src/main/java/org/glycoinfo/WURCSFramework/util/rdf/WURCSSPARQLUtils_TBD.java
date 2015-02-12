package org.glycoinfo.WURCSFramework.util.rdf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class WURCSSPARQLUtils_TBD {
		
	
	/**
	 * TODO:
	 * @param a_objWURCS
	 * @param a_uRES
	 * @return RES FILTER in SPARQL Query
	 */
	public static String getRESFilter_TBD(LinkedList<RES> a_aRESs) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("# RES FILTER\n");
		
		for (RES t_oRes1 : a_aRESs) {
			LinkedList<RES> t_aRESs = new LinkedList<RES>();
			for (RES t_oRes2 : a_aRESs) {
				if (WURCSDataConverter.convertRESIndexToID(t_oRes1.getRESIndex()) < WURCSDataConverter.convertRESIndexToID(t_oRes2.getRESIndex()) ) {
						t_aRESs.add(t_oRes2);
				}
			}
			if (t_aRESs.size() > 0) {
				sb.append("  FILTER ( ");
				int m_iRES = 1;
				String endString = " &&";
				for (RES res : t_aRESs) {
					if (t_aRESs.size() == m_iRES) endString = " ";
						sb.append(" ?RES" + t_oRes1.getRESIndex() + " != ?RES" + res.getRESIndex());
					sb.append(endString);
					m_iRES++;
				}
				sb.append(" ) . \n");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @param a_objWURCS
	 * @return LIN FILTER in SPARQL Query
	 */
	public static String getLINFilter_TBD(LinkedList<LIN> a_aLINs) {
		WURCSExporter export = new WURCSExporter();

//		for (int i = 0; i < a_aLINs.size(); i++) {
//			System.out.println(export.getLINString(a_aLINs.get(i)) + "\n");
//		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("# LIN FILTER\n");
				
		for (int i = 0; i < a_aLINs.size(); i++) {
			LinkedList<LIN> t_aLINs = new LinkedList<LIN>();
			for (int j = 0; j < a_aLINs.size(); j++) {
				if (i < j) {
					t_aLINs.add(a_aLINs.get(j));
				}
			}
			if (t_aLINs.size() > 0) {
				sb.append("  FILTER ( ");
				int m_iLIN = 1;
				String endString = " &&";
				for (LIN lin : t_aLINs) {
					if (t_aLINs.size() == m_iLIN) endString = " ";
						sb.append(" ?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(a_aLINs.get(i))) + " != ?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(lin)));
					sb.append(endString);
					m_iLIN++;
				}
				sb.append(" ) . \n");
			}
		}
		return sb.toString();
	}
	
	
	public static String removeChar4SPARQL(String a_str){
		return a_str.replace("-", "").replace(" ", "").replace("|", "").replace("~", "Repeat").replace("%", "Pro").replaceAll("([a-zA-Z]\\?+)", "Question");
	}
	
	
}
