package org.glycoinfo.WURCSFramework.util.rdf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIPOld;
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
		
	public static String getGLIPSting(GLIP a_oGLIP){
		return a_oGLIP.getRESIndex() + a_oGLIP.getBackbonePosition() + a_oGLIP.getBackboneDirection() ;
	}
	
	public static String getGraph(LinkedList<String> t_aOption){
		StringBuilder  sb = new StringBuilder();
		// select graph
		for (String m_str : t_aOption){
			if (m_str.startsWith("FROM")) { 
				sb.append(m_str + "\n");
			}
		}
		return sb.toString();
	}
	
	
	public static String getSelect(LinkedList<String> t_aOption) {
		StringBuilder  sb = new StringBuilder();
		sb.append("# SELECT\n");
		sb.append("SELECT DISTINCT ?glycans\n");
		
		if (t_aOption.contains("wurcs")) {
			sb.append("  str ( ?wurcs ) AS ?WURCS\n");
		}
		return sb.toString();
	}
	
	public static String getPrefix(){
		StringBuilder  sb = new StringBuilder();
				
//		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
//		sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
//		sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n");
//		sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
		sb.append("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n");
//		sb.append("PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n");
		sb.append("PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n");
		
		return sb.toString();
	}
	
	
	public static String getHeadr(String a_strWURCS, LinkedList<String> t_aOption){
		
		String m_strHED = "";
		
		// get date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String m_strDate = sdf.format(date);
		
		m_strHED += "# ******************************************************\n";
		m_strHED += "#    WURCSFramework: WURCS2SPARQL   " + m_strDate + "\n";
		boolean m_bsupport = true;
		String m_strError = "";
		if (a_strWURCS.contains("%")) {
			m_strError += "#        not support this WURCS String with Probability (%) \n";
			m_bsupport = false;
		}
		if (a_strWURCS.contains("~")) {
			m_strError += "#        not support this WURCS String with repeat unit (~)\n";
			m_bsupport = false;
		}
		if (m_bsupport == false) {
			m_strHED += "#        CAUTION:\n";
			m_strHED += m_strError;
			m_strHED += "#        contact: info.glyco@gmail.com\n";
		}
		m_strHED += "#        version 2015.02.16 (JAPAN) \n";

		m_strHED += "# Query Structure:\n";
		m_strHED += "# " + a_strWURCS + "\n";
		
		for (String t_str : t_aOption) {
			m_strHED += "#      option: " + t_str + "\n";
		}
		m_strHED += "# Warranty: Use at your own risk.\n";
		m_strHED += "# ******************************************************\n\n";
		
		return m_strHED;
	}
	
	
	
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
		return a_str.replace("-", "").replace(" ", "").replace("|", "").replace("~", "Repeat").replace("%", "Pro").replace("?", "q");
	}
	
	
}
