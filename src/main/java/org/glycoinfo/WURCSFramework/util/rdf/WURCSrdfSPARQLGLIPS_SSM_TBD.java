package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

//TODO: 
public class WURCSrdfSPARQLGLIPS_SSM_TBD extends SearchVariables {
	
	public String getSPARQL(String  a_strWURCS, LinkedList<String> t_aOption){
		int m_iPosition = 1;
		String strSPAQRL = "";
		boolean m_bwhereonly = false;
		StringBuilder  sb = new StringBuilder();

		WURCSImporter ws = new WURCSImporter();
		
		try {
			WURCSArray m_oWURCSArray = ws.extractWURCSArray(a_strWURCS);
		WURCSExporter export = new WURCSExporter();
		
		if (t_aOption.contains("whereonly")) {
			m_bwhereonly = true;
		}
				
		if (!m_bwhereonly) {

			sb.append(WURCSSPARQLUtils_TBD.getPrefix());
			sb.append(WURCSSPARQLUtils_TBD.getSelect(t_aOption));
			sb.append(WURCSSPARQLUtils_TBD.getGraph(t_aOption));
			sb.append("WHERE {\n");
		}		

		
		
		sb.append("# SEQ\n");
		sb.append("  ?glycan glycan:has_glycosequence ?" + getGlycoSequenceUri() + " \n");
		
		sb.append("# FILTER\n");
		sb.append("#  FILTER regex (str(?" + getGlycoSequenceUri() + "), \"^http://rdf.glycoinfo.org/glycan/\") .\n");
		
		sb.append("# BIND\n");
		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glytoucan.org/glyspace/service/glycans/\")) as ?glycan2)\n");	// 
//		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glytoucan.org/glyspace/service/glycans/\")) as ?glycan2)\n");
		sb.append("  BIND( iri(concat(?glycan2, \"/image?style=extended&format=png&notation=cfg\"))as ?glycans )\n");
//		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glycome-db.org/getSugarImage.action?type=cfg&id=\")) as ?glycan2)\n");

		// has_uniqueRES Section
		//   ?" + getGlycoSequenceUri() + " wurcs:has_uniqueRES ?uRES1, ?uRES2, ?uRES3, ?uRES4 .
		
		if (t_aOption.contains("wurcs")) {
			sb.append("# WURCS\n");
			sb.append("  ?" + getGlycoSequenceUri() + " glycan:has_sequence ?wurcs .\n");
		}
		
		sb.append("# uniqueRES\n");
		sb.append("  ?" + getGlycoSequenceUri() + " wurcs:has_uniqueRES ");
		
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

		// RES FILER
		sb.append(WURCSSPARQLUtils_TBD.getRESFilter_TBD(m_oWURCSArray.getRESs()));
		
		// LIN FILTER getLINFilter_TBD
		sb.append(WURCSSPARQLUtils_TBD.getLINFilter_TBD(m_oWURCSArray.getLINs()));

		
		//  ?RESa wurcs:is_uniqueRES ?uRES1 .
		sb.append("# RES\n");
		for (RES m_aRES : m_oWURCSArray.getRESs()) {
			sb.append("  ?RES" + m_aRES.getRESIndex() + " wurcs:is_uniqueRES ?uRES" + m_aRES.getUniqueRESID() + " .\n");
		}
		
		
		// has_LIN Section
		//   ?" + getGlycoSequenceUri() + " wurcs:has_LIN ?LIN1, ?LIN2, ?LIN3 
		sb.append("# LIN\n");
		int m_iLIN = 1;
		sb.append("  ?" + getGlycoSequenceUri() + " wurcs:has_LIN ");
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			String m_strEnd = ",";
			if (m_oWURCSArray.getLINs().size() == m_iLIN){
				m_strEnd = ".";
			}
			sb.append("?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(a_oLIN)) + " " + m_strEnd + " ");
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
			
			sb.append("  ?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(a_oLIN)) + " wurcs:has_GLIPS ");
			
			LinkedList<String> m_aGLIPS = new LinkedList<String>();
			for (GLIPs a_oGLIPs : a_oLIN.getListOfGLIPs()) {
				m_aGLIPS.add("?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPs)));
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
				sb.append("  ?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(a_oLIN)) + "  wurcs:is_repeat true .\n");
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
				
				
				
				sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
						+ " wurcs:has_GLIP "); //?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");
				
				String endString = " , ";
				int i_GLIPS = 1;
				for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
					if (i_GLIPS == a_oGLIPS.getGLIPs().size()) {
						endString = " ";
					}
					sb.append(" ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(WURCSSPARQLUtils_TBD.getGLIPSting(a_oGLIP)) + endString);
					i_GLIPS++;
				}
				sb.append(" .\n");
				
				
				
				for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
					
					if (a_oGLIP.getBackbonePosition() != -1) {
						sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(WURCSSPARQLUtils_TBD.getGLIPSting(a_oGLIP)) + " wurcs:has_SC_position " + a_oGLIP.getBackbonePosition()  + " .\n");
					}
					
					sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(WURCSSPARQLUtils_TBD.getGLIPSting(a_oGLIP)) + " wurcs:has_RES ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(a_oGLIP.getRESIndex()) + " .\n");
					
				}
				
				// isFuzzy ?
				if (a_oGLIPS.getGLIPs().size() == 1) {
					sb.append("#  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy false .\n");
				}
				else {
					sb.append("#  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy true .\n");
				}
				
			} // end for <GLIPS>
		}
		
		if (!m_bwhereonly) {
		sb.append("}");
		}
		
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
		
		return WURCSSPARQLUtils_TBD.getHeadr(a_strWURCS, t_aOption) + strSPAQRL + (m_bwhereonly? "" : "\n  ORDER BY ?glycans \n  " + m_strLimit ) + "\n";
	}
}
