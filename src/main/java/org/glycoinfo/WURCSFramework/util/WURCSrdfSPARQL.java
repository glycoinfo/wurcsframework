package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;

//TODO: 
public class WURCSrdfSPARQL {
	
	private String m_strPrefix;
	
	public String getSPARQL(WURCSArray m_oWURCSArray){
		StringBuilder  sb = new StringBuilder();
		
		WURCSExporter export = new WURCSExporter();
		
		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
		sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
		sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n");
		sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
		sb.append("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n");
		sb.append("PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n");
		sb.append("PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n");
		sb.append("SELECT DISTINCT ?glycan2\n");
		sb.append("FROM <http://www.glycoinfo.org/graph/wurcs>\n");
		sb.append("WHERE {\n");
		sb.append("  ?glycan glycan:has_glycosequence ?gseq .\n");
		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"http://www.glycome-db.org/getSugarImage.action?type=cfg&id=\")) as ?glycan2)\n");

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

		for (UniqueRES uRES : m_oWURCSArray.getUniqueRESs()) {
			
			sb.append(" ?uRES" + uRES.getUniqueRESID() + " wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)) + "> .\n");
		}

		int m_iLIN = 1;
		sb.append("  ?gseq wurcs:has_LIN ");
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			String m_strEnd = ",";
			if (m_oWURCSArray.getLINs().size() == m_iLIN){
				m_strEnd = ".";
			}
			sb.append("?LIN" + export.getLINString(a_oLIN).replace("-", "") + " " + m_strEnd + " ");
			m_iLIN++;
			
		}
		sb.append("\n");
		
		
		//   ?LIN1 wurcs:has_GLIP ?GLIP1, ?GLIP2 .
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			sb.append("  ?LIN" + export.getLINString(a_oLIN).replace("-", "") + " wurcs:has_GLIP ");
			m_iLIN = 1;
			for (GLIP a_oGLIP : a_oLIN.getGLIPs()) {
				String m_strEnd = ",";
				if (a_oLIN.getGLIPs().size() == m_iLIN){
					m_strEnd = ".";
				}
				sb.append("  ?GLIP" + export.getGLIPString(a_oGLIP) + " "  + m_strEnd + " ");
				m_iLIN++;
			}
			sb.append(" \n");
		}
		sb.append(" \n");
		
		
		// extract RES index & ID
		LinkedList<String[]> RESs = new LinkedList<String[]>();
		
		for (RES RES : m_oWURCSArray.getRESs()) {
			String[] res = {"",""};
			res[0] = RES.getRESIndex();
			res[1] = String.valueOf(RES.getUniqueRESID());
			RESs.add(res);
		}
		
		int LINcount = 1;
//		sb.append("# LIN1\n");
		for (LIN a_oLIN : m_oWURCSArray.getLINs()){
			sb.append("# LIN" + LINcount + " \n");
			LINcount++;
			
			for (GLIP a_oGLIP : a_oLIN.getGLIPs()) {
				sb.append("  ?GLIP" + export.getGLIPString(a_oGLIP) + " wurcs:has_SC_position " + a_oGLIP.getBackbonePosition() + " .\n");
				
				sb.append("  ?GLIP" + export.getGLIPString(a_oGLIP) + " wurcs:has_RES ?RES" + a_oGLIP.getRESIndex() + " .\n");
				
				sb.append("  ?RES" + a_oGLIP.getRESIndex() + " wurcs:is_uniqueRES ?uRES");
				
				for (String[] s : RESs) {
					if (s[0].equals(a_oGLIP.getRESIndex())){
						sb.append(s[1] + " .\n");
					}
				}
				
			}
			
			
			
		}
//		  # LIN1
//		  ?GLIPa2 wurcs:has_SC_position 2 .
//		  ?GLIPa2 wurcs:has_RES ?RESa .
//		  ?RESa wurcs:is_uniqueRES ?uRES1 .
		
//		  ?GLIPb3 wurcs:has_SC_position 3 .
//		  ?GLIPb3 wurcs:has_RES ?RESb . 
//		  ?RESb wurcs:is_uniqueRES ?uRES2 .
						  
		

		
		
		
		
		
		sb.append("}");
		
		
		return sb.toString();
	}
	
	
/*
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> 
PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>
PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#> 

SELECT DISTINCT ?glycan2
FROM <http://www.glycoinfo.org/graph/wurcs>
WHERE {
  ?glycan glycan:has_glycosequence ?gseq .
  BIND( iri(replace(str(?glycan), "http://rdf.glycoinfo.org/glycan/", "http://www.glycome-db.org/getSugarImage.action?type=cfg&id=")) as ?glycan2)
  ?gseq wurcs:has_uniqueRES ?uRES1, ?uRES2, ?uRES3, ?uRES4 .
  ?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a6d21122h-2a_2-6_5*NCC%2F3%3DO> .
  ?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .
  ?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> .
  ?uRES4 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .
  
  ?gseq wurcs:has_LIN ?LIN1, ?LIN2, ?LIN3 .
  ?LIN1 wurcs:has_GLIP ?GLIP1, ?GLIP2 .
  ?LIN2 wurcs:has_GLIP ?GLIP3, ?GLIP4 .
  ?LIN3 wurcs:has_GLIP ?GLIP5, ?GLIP6 .

  # LIN1
  ?GLIP1 wurcs:has_SC_position 2 .
  ?GLIP1 wurcs:has_RES ?RESa .
  ?RESa wurcs:is_uniqueRES ?uRES1 .

  ?GLIP2 wurcs:has_SC_position 3 .
  ?GLIP2 wurcs:has_RES ?RESb . 
  ?RESb wurcs:is_uniqueRES ?uRES2 .

    # LIN2
  ?GLIP3 wurcs:has_SC_position 1 .
  ?GLIP3 wurcs:has_RES ?RESb . 
  ?RESb wurcs:is_uniqueRES ?uRES2 .

  ?GLIP4 wurcs:has_SC_position 4 .
  ?GLIP4 wurcs:has_RES ?RESc . 
  ?RESc wurcs:is_uniqueRES ?uRES3 .

  # LIN3
  ?GLIP5 wurcs:has_SC_position 1 .
  ?GLIP5 wurcs:has_RES ?RESd . 
  ?RESd wurcs:is_uniqueRES ?uRES4 .

  ?GLIP6 wurcs:has_SC_position 3 .
  ?GLIP6 wurcs:has_RES ?RESc . 
  ?RESc wurcs:is_uniqueRES ?uRES3 .
 } 

 */
	
	
	
	
	
	
	
	


}
