package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class WURCSSPARQL_TBD {
	
	public String getSPARQL(String  a_strWURCS, LinkedList<String> t_aOption, String a_SearchType){

		String strSPAQRL = "";
		boolean m_bwhereonly = false;
		boolean m_bBlazegraph = false;
		
		StringBuilder  sb = new StringBuilder();
//		sb.append("DEFINE sql:select-option \"order\"\n");
		WURCSImporter ws = new WURCSImporter();
		
		try {
			WURCSArray m_oWURCSArray = ws.extractWURCSArray(a_strWURCS);
			
			if (t_aOption.contains("virtuoso")) {
				sb.append("DEFINE sql:select-option \"order\"\n");
			}
			
			if (t_aOption.contains("whereonly")) {
				m_bwhereonly = true;
			}
			if (t_aOption.contains("blazegraph")) {
				m_bBlazegraph = true;
			}
			if (!m_bwhereonly) {
				sb.append(WURCSSPARQLUtils_TBD.getPrefix(t_aOption));
				sb.append(WURCSSPARQLUtils_TBD.getSelect(t_aOption));
				sb.append(WURCSSPARQLUtils_TBD.getGraph(m_oWURCSArray.getRESCount(), t_aOption));
				sb.append("WHERE {\n");
			}
	
			sb.append(WURCSSPARQLUtils_TBD.getGseq(t_aOption));
			
			if (!m_bBlazegraph) {
				sb.append(WURCSSPARQLUtils_TBD.getBind());
			}
			
			if (t_aOption.contains(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch)) {
				//   ?gseq glycan:has_sequence "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4"^^xsd:string .
				sb.append(WURCSSPARQLUtils_TBD.getHasWURCS(a_strWURCS));
			}
			else {
				sb.append(WURCSSPARQLUtils_TBD.getWURCS(t_aOption));
				
				//with subquery
				if (!m_bBlazegraph) {
					sb.append("{\n");
				}
				
				
				sb.append(WURCSSPARQLUtils_TBD.getSelectRESs(m_oWURCSArray, t_aOption));
//				sb.append(" SELECT * WHERE { \n");
//				sb.append(WURCSSPARQLUtils_TBD.getuniqueRES(m_oWURCSArray.getUniqueRESs()));
//				sb.append(WURCSSPARQLUtils_TBD.getRES2uRES(m_oWURCSArray.getRESs()));
//				sb.append(" }\n");
				
				//with subquery
				if (!m_bBlazegraph) {
					sb.append("}\n");
				}
	
				sb.append(WURCSSPARQLUtils_TBD.getRESFilter_TBD(m_oWURCSArray));
//				sb.append(WURCSSPARQLUtils_TBD.getLINFilter_TBD(m_oWURCSArray.getLINs()));
	
				
				sb.append(WURCSSPARQLUtils_TBD.getGseq2LIN(m_oWURCSArray.getLINs()));
				sb.append(WURCSSPARQLUtils_TBD.getLLIN2GLIPS(m_oWURCSArray.getLINs()));
				sb.append(WURCSSPARQLUtils_TBD.getGLIPS2GLIP(a_SearchType, m_oWURCSArray.getLINs()));
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
		String m_strOrder = "";
		
		if (!t_aOption.contains("count")) {
			
			if (!m_bBlazegraph) {
				m_strOrder = "  ORDER BY ?glycans \n";
			}
			else {
				m_strOrder = "  ORDER BY ?glycan \n";
			}
			
			for (String m_str : t_aOption){
				if (m_str.contains("LIMIT")) { m_strLimit = m_str; }
			}
		}
		else {
			m_strOrder = "#  ORDER BY ?glycans \n";
		}
		
		return WURCSSPARQLUtils_TBD.getHeadr(a_strWURCS, t_aOption) + strSPAQRL + (m_bwhereonly? "" : "\n" + m_strOrder + m_strLimit ) + "\n";
	}
}
