package org.glycoinfo.WURCSFramework.util.rdf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
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
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;
//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;

//TODO: 
public class WURCSrdfSPARQLGLIPS_SSM {
	
/*	
	// set search option
	LinkedList<String> t_aOption = new LinkedList<String>();
//	t_aOption.add("exact");
	t_aOption.add("uri");
	t_aOption.add("wurcs");
	t_aOption.add("LIMIT 100");
	t_aOption.add("FROM <http://www.glycoinfo.org/graph/wurcs/0.3>");
//	t_aOption.add("accession_number");
*/
	
	
	public String getSPARQL(String  a_strWURCS, LinkedList<String> t_aOption){
		String m_strHED = "";
		int m_iPosition = 1;
		String strSPAQRL = "";
		StringBuilder  sb = new StringBuilder();

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
		m_strHED += "#        version 2015.02.02 (JAPAN) \n";

		m_strHED += "# Query Structure:\n";
		m_strHED += "# " + a_strWURCS + "\n";
		
		for (String t_str : t_aOption) {
			m_strHED += "#      option: " + t_str + "\n";
		}
		m_strHED += "# Warranty: Use at your own risk.";
		m_strHED += "# ******************************************************\n\n";
		
		WURCSImporter ws = new WURCSImporter();
		
		try {
			WURCSArray m_oWURCSArray = ws.extractWURCSArray(a_strWURCS);
		WURCSExporter export = new WURCSExporter();
				
		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
		sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
		sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n");
		sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
		sb.append("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n");
		sb.append("PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n");
		sb.append("PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n");
		
		sb.append("# SELECT\n");
		sb.append("SELECT DISTINCT ?glycans\n");
		
		if (t_aOption.contains("wurcs")) {
			sb.append("  str ( ?wurcs ) AS ?WURCS\n");
		}
		
		// select graph
		for (String m_str : t_aOption){
			if (m_str.startsWith("FROM")) { 
				sb.append(m_str + "\n");
			}
		}
		
		sb.append("WHERE {\n");
		
		sb.append("# SEQ\n");
		sb.append("  ?glycan glycan:has_glycosequence ?gseq \n");
		
		sb.append("# FILTER\n");
		sb.append("  FILTER regex (str(?gseq), \"^http://rdf.glycoinfo.org/glycan/\") .\n");
		
		sb.append("# BIND\n");
		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glytoucan.org/glyspace/service/glycans/\")) as ?glycan2)\n");	// 
//		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glytoucan.org/glyspace/service/glycans/\")) as ?glycan2)\n");
		sb.append("  BIND( iri(concat(?glycan2, \"/image?style=extended&format=png&notation=cfg\"))as ?glycans )\n");
//		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glycome-db.org/getSugarImage.action?type=cfg&id=\")) as ?glycan2)\n");

		// has_uniqueRES Section
		//   ?gseq wurcs:has_uniqueRES ?uRES1, ?uRES2, ?uRES3, ?uRES4 .
		
		if (t_aOption.contains("wurcs")) {
			sb.append("# WURCS\n");
			sb.append("  ?gseq glycan:has_sequence ?wurcs .\n");
		}
		
		sb.append("# uniqueRES\n");
		sb.append("  ?gseq wurcs:has_uniqueRES ");
		
		int m_iRES = 1;
		for (UniqueRES uRES : m_oWURCSArray.getUniqueRESs()) {
			String m_strEnd = ",";
			if (m_oWURCSArray.getUniqueRESs().size() == m_iRES){
				m_strEnd = ".";
			}
			sb.append("  ?uRES" + uRES.getUniqueRESID() + m_strEnd);
			m_iRES++;
		}
		sb.append("\n");

		// is_monosaccharide Section
		//  ?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a6d21122h-2a_2-6_5*NCC%2F3%3DO> .
		for (UniqueRES uRES : m_oWURCSArray.getUniqueRESs()) {
			
			sb.append("  ?uRES" + uRES.getUniqueRESID() + " wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)) + "> .\n");
		}

		
		//  ?RESa wurcs:is_uniqueRES ?uRES1 .
		sb.append("# RES\n");
		for (RES m_aRES : m_oWURCSArray.getRESs()) {
			sb.append("  ?RES" + m_aRES.getRESIndex() + " wurcs:is_uniqueRES ?uRES" + m_aRES.getUniqueRESID() + " .\n");
		}
		
		// has_LIN Section
		//   ?gseq wurcs:has_LIN ?LIN1, ?LIN2, ?LIN3 
		sb.append("# LIN\n");
		int m_iLIN = 1;
		sb.append("  ?gseq wurcs:has_LIN ");
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			String m_strEnd = ",";
			if (m_oWURCSArray.getLINs().size() == m_iLIN){
				m_strEnd = ".";
			}
			sb.append("?LIN" + this.removeChar4SPARQL(export.getLINString(a_oLIN)) + " " + m_strEnd + " ");
			m_iLIN++;
			
		}
		sb.append("\n");
		
		// has_GLIPS Section
		
//		sb.append("# LIN\n");
		int m_iLINhas_GLIPS = 0;
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			// GLIP List in LIN
			//   ?LIN1 wurcs:has_GLIPS ?GLIPS1, ?GLIPS2 . ?GLIPS3, ..., 
			m_iLINhas_GLIPS++;
			sb.append("# LIN" + m_iLINhas_GLIPS + "\n");
			
			sb.append("  ?LIN" + this.removeChar4SPARQL(export.getLINString(a_oLIN)) + " wurcs:has_GLIPS ");
			
			LinkedList<String> m_aGLIPS = new LinkedList<String>();
			for (GLIPs a_oGLIPs : a_oLIN.getListOfGLIPs()) {
				m_aGLIPS.add("?GLIPS" + this.removeChar4SPARQL(export.getGLIPsString(a_oGLIPs)));
			}
			String m_strEnd = ",";
			int m_iGLIPS = 1;
			for (String strGLIPS : m_aGLIPS) {
				if (m_aGLIPS.size() == m_iGLIPS){
					m_strEnd = ".";
				}
				sb.append("  " + strGLIPS + " "  + m_strEnd + " ");
				m_iGLIPS++;
			}
			sb.append(" \n");
			
			
			// repeat unit
			if (a_oLIN.isRepeatingUnit()) {
				sb.append("  ?LIN" + this.removeChar4SPARQL(export.getLINString(a_oLIN)) + "  wurcs:is_repeat \"true\"^^xsd:boolean .\n");
			}
		}
		sb.append(" \n");
		
//		sb.append("# LIN1\n");
		int m_iLINGLIPS = 0;
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			
			m_iLINGLIPS++;
			// TODO: 
			// <GLIPS>
			int m_iGLIPS = 0;
			for (GLIPs a_oGLIPS : a_oLIN.getListOfGLIPs()) {
				m_iGLIPS++;
				sb.append("# LIN" + m_iLINGLIPS + ": GLIPS" + m_iGLIPS + "\n");
				
				
				
				sb.append("  ?GLIPS" + this.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
						+ " wurcs:has_GLIP ?GLIP" + this.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");
				
				for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
					sb.append("  ?GLIP" + this.removeChar4SPARQL(getGLIPSting(a_oGLIP)) + " wurcs:has_SC_position " + a_oGLIP.getBackbonePosition()  + " .\n");
					sb.append("  ?GLIP" + this.removeChar4SPARQL(getGLIPSting(a_oGLIP)) + " wurcs:has_RES ?RES" + removeChar4SPARQL(a_oGLIP.getRESIndex()) + " .\n");
					
				}
				
				// isFuzzy ?
				if (a_oGLIPS.getGLIPs().size() == 1) {
					sb.append("#  ?GLIPS" + this.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:isFuzzy \"false\"^^xsd:boolean .\n");
				}
				else {
					sb.append("#  ?GLIPS" + this.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:isFuzzy \"true\"^^xsd:boolean .\n");
				}
				
			} // end for <GLIPS>
		}
		
		sb.append("}");
		
		strSPAQRL = sb.toString();
		
		
		} catch (WURCSFormatException e) {
		// TODO 自動生成された catch ブロック
			System.err.println( e.getErrorMessage() );
		e.printStackTrace();
		}
		String m_strLimit = "";
		for (String m_str : t_aOption){
			if (m_str.startsWith("LIMIT")) { m_strLimit = m_str; }
		}
		
		return m_strHED + strSPAQRL + "\n  ORDER BY ?glycans \n  " + m_strLimit + "\n";
	}
	
	private String getGLIPSting(GLIP a_oGLIP){
		return a_oGLIP.getRESIndex() + a_oGLIP.getBackbonePosition() + a_oGLIP.getBackboneDirection() ;
	}
	
	
	
	private String removeChar4SPARQL(String a_str){
		return a_str.replace("-", "").replace(" ", "").replace("|", "").replace("~", "Repeat").replace("%", "Pro").replaceAll("([a-zA-Z]\\?+)", "Question");
	}
}
