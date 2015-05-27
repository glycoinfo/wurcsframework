package org.glycoinfo.WURCSFramework.util.rdf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;

//TODO: 
public class WURCSSPARQLUtils_TBD {
	
	
	
	// ESM old
	public static String m_strSearchtypeESuperM = "ESuperM";
	// SSM old
	public static String m_strSearchtypeFSuperM = "FSuperM";
	// FSM old
	public static String m_strSearchtypeFSubM = "FSubM";
	
	public static String m_strSearchtypeFSubMsume = "FSubMsume";
	
	public static String m_strSearchtypeExactStructureSearch = "ExactStructureSearch";
	
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
			m_strError += "#        not support this WURCS String with Probability (%) in LIP\n";
			m_bsupport = false;
		}
//		if (a_strWURCS.contains("~")) {
//			m_strError += "#        not support this WURCS String with repeat unit (~)\n";
//			m_bsupport = false;
//		}
		if (m_bsupport == false) {
			m_strHED += "#        CAUTION:\n";
			m_strHED += m_strError;
			m_strHED += "#        contact: info.glyco@gmail.com\n";
		}
		m_strHED += "#        version 2015.03.26.01:24 (JAPAN)\n";
		m_strHED += "#                modified Blazegraph \n";
		
		m_strHED += "# Query Structure:\n";
		m_strHED += "# " + a_strWURCS + "\n";
		
		for (String t_str : t_aOption) {
			m_strHED += "#      option: " + t_str + "\n";
		}
		m_strHED += "# Warranty: Use at your own risk.\n";
		m_strHED += "# ******************************************************\n\n";
		
		return m_strHED;
	}
	
	public static String getSelectRESs(WURCSArray a_oWURCSArray, LinkedList<String> a_aOption) {
		StringBuilder  sb = new StringBuilder();
		boolean m_bBlazegraph = false;
		if (a_aOption.contains("blazegraph")) {
			m_bBlazegraph = true;
		}

		if (!m_bBlazegraph) {
			sb.append(WURCSSPARQLUtils_TBD.getSelectRESs(a_oWURCSArray.getRESs()));
		
			// for monosaccharide graph
			for (String m_str : a_aOption){
				if (m_str.startsWith("FROM")) { 
					sb.append(m_str.replace("FROM","  FROM ").replace(">","/ms> ") + "\n");
				}
			}
			// for glycan graph
			for (String m_str : a_aOption){
				if (m_str.startsWith("FROM")) { 
					sb.append(m_str.replace("FROM","  FROM ") + "\n");
				}
			}
		}		
		// check search option
		String t_strSearchOption = "FSubM";
		for (String m_str : a_aOption){
			if (m_str.startsWith(m_strSearchtypeFSubMsume)) { 
				t_strSearchOption = m_strSearchtypeFSubMsume;
			}
		}
		

		
		//with subquery
		if (!m_bBlazegraph) {
			sb.append("  WHERE { \n");
		}
		
		sb.append(WURCSSPARQLUtils_TBD.getHasUniqueRES(a_oWURCSArray.getUniqueRESs()));
		
		sb.append(WURCSSPARQLUtils_TBD.getRES2uRES(a_oWURCSArray.getRESs()));
		
		sb.append(WURCSSPARQLUtils_TBD.getuREStoMS(a_oWURCSArray.getUniqueRESs()));
		
		
		
		if (t_strSearchOption.equals("FSubM")) {
			sb.append(WURCSSPARQLUtils_TBD.getMStoSubano(a_oWURCSArray.getUniqueRESs()));
		}
		else {
			sb.append(WURCSSPARQLUtils_TBD.getMStoSubsumes(a_oWURCSArray.getUniqueRESs()));
		}
		
		
		
//		sb.append(WURCSSPARQLUtils_TBD.getuniqueRES(a_oWURCSArray.getUniqueRESs()));

		//with subquery
		if (!m_bBlazegraph) {
			sb.append(" }\n");
		}

		return sb.toString();
	}
	
	public static String getuREStoMS(LinkedList<UniqueRES> a_aURESs) {
		StringBuilder  sb = new StringBuilder();
		//  ?RESa wurcs:is_uniqueRES ?uRES1 .
//		sb.append("# RES\n");
		for (UniqueRES m_aURES : a_aURESs) {
			sb.append("  ?uRES" + m_aURES.getUniqueRESID() + " wurcs:is_monosaccharide ?MS" + m_aURES.getUniqueRESID() + " .\n");
		}
		return sb.toString();
	}	
	
	public static String getHasUniqueRES(LinkedList<UniqueRES> a_aURESs) {
		StringBuilder  sb = new StringBuilder();
//		sb.append("# uniqueRES\n");
		sb.append("  ?gseq wurcs:has_uniqueRES ");
		
		int m_iRES = 1;
		for (UniqueRES uRES : a_aURESs) {
			String m_strEnd = ",";
			if (a_aURESs.size() == m_iRES){
				m_strEnd = ".";
			}
			sb.append("  ?uRES" + uRES.getUniqueRESID() + m_strEnd);
			m_iRES++;
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static String getSelectRESs(LinkedList<RES> a_aRESs) {
		StringBuilder  sb = new StringBuilder();
		sb.append("  SELECT");
		for (RES RES : a_aRESs) {
			sb.append(" ?RES" + RES.getRESIndex());
		}
		sb.append("\n");
		
		return sb.toString();
	}
	
	public static String getGLIPS2GLIP(String a_strSearchOption, LinkedList<LIN> a_aLINs){
		StringBuilder  sb = new StringBuilder();
		WURCSExporter export = new WURCSExporter();

		if (a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM)
				|| a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM)
				) {
			//		sb.append("# LIN1\n");
			int m_iLINGLIPS = 0;
			for (LIN a_oLIN : a_aLINs){
				
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
					
					// ESuperM
					if (a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeESuperM )) {
						// isFuzzy ?
						if (a_oGLIPS.getGLIPs().size() == 1) {
							sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy false .\n");
						}
						else {
							sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy true .\n");
						}
					}
					
					// FSuperM
					if (a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSuperM )) {
						// isFuzzy ?
						if (a_oGLIPS.getGLIPs().size() == 1) {
//							sb.append("#  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy false .\n");
						}
						else {
//							sb.append("#  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy true .\n");
						}
					}
					
					
					
					
				} // end for <GLIPS>
			}
		}
		else if (a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSubM)|| a_strSearchOption.equals(WURCSSPARQLUtils_TBD.m_strSearchtypeFSubMsume)) {
//			sb.append("# LIN1\n");
//			int i_pRES = 1;
			int m_iLINGLIPS = 0;
			for (LIN a_oLIN : a_aLINs){
				
				m_iLINGLIPS++;

				int m_iGLIPS = 0;
				for (GLIPs a_oGLIPS : a_oLIN.getListOfGLIPs()) {
					m_iGLIPS++;
					sb.append("# LIN" + m_iLINGLIPS + ": GLIPS" + m_iGLIPS + "\n");

					
					sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
							+ " wurcs:has_GLIP ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");
					

					if (a_oGLIPS.getGLIPs().size() == 1) {
						for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
						sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)));	
						sb.append(" wurcs:has_SC_position " + a_oGLIP.getBackbonePosition() + " .\n");
						sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)));	
						sb.append(" wurcs:has_RES ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(a_oGLIP.getRESIndex()) + " .\n");
						}
					}
					else {
						
						sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
								+ " wurcs:has_RES ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");
						
						sb.append("  ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
								+ " wurcs:has_SC_position ?Pos" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");

						// GLIP
						int i_glips = 1;
						String m_strEndString = " || ";
						sb.append("  FILTER (");
						for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){

							if (a_oGLIPS.getGLIPs().size() == i_glips ){ 
								m_strEndString = " || ( ?Pos" +  WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " = -1  && ?RES" +  WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " = ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(a_oGLIP.getRESIndex()) + "))\n"; 
							}
							
							sb.append("  ( ?Pos" +  WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " = " + a_oGLIP.getBackbonePosition() );
							sb.append("  && ?RES" +  WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " = ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(a_oGLIP.getRESIndex()) + " ) " + m_strEndString );
									
//							System.out.println("(a_oGLIPS.getGLIPs().size():\t" + a_oGLIPS.getGLIPs().size());
//							System.out.println("i_glips\t\t\t\t" + i_glips);
							i_glips++;
						}
					}
					
				} // end for <GLIPS>
			}
			sb.append("\n");
		}
		else  {
//			sb.append("# LIN1\n");
			int i_pRES = 1;
			int m_iLINGLIPS = 0;
			for (LIN a_oLIN : a_aLINs){
				
				m_iLINGLIPS++;
				// TODO: 
				// <GLIPS>
				
				int m_iGLIPS = 0;
				for (GLIPs a_oGLIPS : a_oLIN.getListOfGLIPs()) {
					m_iGLIPS++;
					sb.append("# LIN" + m_iLINGLIPS + ": GLIPS" + m_iGLIPS + "\n");
					
					
					
					sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) 
							+ " wurcs:has_GLIP ?GLIP" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " . \n");
					
					// GLIP
					sb.append("  ?GLIP");
					for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
						sb.append(WURCSSPARQLUtils_TBD.removeChar4SPARQL(WURCSSPARQLUtils_TBD.getGLIPSting(a_oGLIP)));
					}
					
					
					String m_strEndString = " || ";
					
//					boolean m_bSCfuzzy = false;
//					for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
//						if (a_oGLIP.getBackbonePosition() == -1){
//							m_bSCfuzzy = true;
//						}
//					}
					
					int i_glips = 1;
					String strposition = "?p" + i_pRES;
//					if (m_bSCfuzzy = false) {
						sb.append(" wurcs:has_SC_position " + strposition + " \n");
						sb.append(" FILTER ( ");
						i_glips = 1;
						for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
							if (a_oGLIPS.getGLIPs().size() == i_glips ){ m_strEndString = " )\n"; }
							sb.append(" " + strposition + " = " + a_oGLIP.getBackbonePosition()  + m_strEndString);
							i_glips++;
						}
//					}
					i_pRES++;
					
					// GLIP
					sb.append("  ?GLIP");
					for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
						sb.append(WURCSSPARQLUtils_TBD.removeChar4SPARQL(WURCSSPARQLUtils_TBD.getGLIPSting(a_oGLIP)));
					}
					m_strEndString = " || ";
					String strRes = "?res" + i_pRES;
					sb.append(" wurcs:has_RES " + strRes + " \n");
					sb.append(" FILTER ( ");
					i_glips = 1;
					for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()){
						if (a_oGLIPS.getGLIPs().size() == i_glips ){ m_strEndString = " )\n"; }
						sb.append(" " + strRes + " = ?RES" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(a_oGLIP.getRESIndex()) + m_strEndString);
						i_glips++;
					}
					i_pRES++;
					
					// isFuzzy ?
					if (a_oGLIPS.getGLIPs().size() == 1) {
//						sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy false .\n");
					}
					else {
//						sb.append("  ?GLIPS" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getGLIPsString(a_oGLIPS)) + " wurcs:is_fuzzy true .\n");
					}
					
				} // end for <GLIPS>
			}
		}
		return sb.toString();
	}
	
	

	public static String getLLIN2GLIPS(LinkedList<LIN> a_aLINs) {
		StringBuilder  sb = new StringBuilder();
		WURCSExporter export = new WURCSExporter();
		// has_GLIPS Section
		
//		sb.append("# LIN\n");
//		int m_iLINhas_GLIPS = 0;
		for (LIN a_oLIN : a_aLINs){
			// GLIP List in LIN
			//   ?LIN1 wurcs:has_GLIPS ?GLIPS1, ?GLIPS2 . ?GLIPS3, ..., 
//			m_iLINhas_GLIPS++;
//			sb.append("# LIN" + m_iLINhas_GLIPS + "\n");
			
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
		
		return sb.toString();
	}
	
	
	public static String getGseq2LIN(LinkedList<LIN> a_aLINs) {
		StringBuilder  sb = new StringBuilder();
		
		if (a_aLINs.size() > 0) {
			WURCSExporter export = new WURCSExporter();
			// has_LIN Section
			//   ?gseq wurcs:has_LIN ?LIN1, ?LIN2, ?LIN3 
	//		sb.append("# LIN\n");
			int m_iLIN = 1;
			sb.append("  ?gseq wurcs:has_LIN ");
			for (LIN a_oLIN : a_aLINs){
				String m_strEnd = ",";
				if (a_aLINs.size() == m_iLIN){
					m_strEnd = ".";
				}
				sb.append("?LIN" + WURCSSPARQLUtils_TBD.removeChar4SPARQL(export.getLINString(a_oLIN)) + " " + m_strEnd + " ");
				m_iLIN++;
				
			}
		}
		sb.append("\n");
		return sb.toString();
	}	
	
	public static String getRES2uRES(LinkedList<RES> a_aRESs) {
		StringBuilder  sb = new StringBuilder();
		//  ?RESa wurcs:is_uniqueRES ?uRES1 .
//		sb.append("# RES\n");
		for (RES m_aRES : a_aRESs) {
			sb.append("  ?RES" + m_aRES.getRESIndex() + " wurcs:is_uniqueRES ?uRES" + m_aRES.getUniqueRESID() + " .\n");
		}
		return sb.toString();
	}	
	
	public static String getMStoSubano(LinkedList<UniqueRES> a_aURESs) {
		StringBuilder  sb = new StringBuilder();

		
		WURCSExporter export = new WURCSExporter();
		for (UniqueRES uRES : a_aURESs) {
			
			sb.append("  <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)) + "> wurcs:has_anobase ?anob" + uRES.getUniqueRESID() + " .\n");
			sb.append("  ?anob" + uRES.getUniqueRESID() + " wurcs:subsumes ?subano" + uRES.getUniqueRESID() + " .\n");
			sb.append("  ?MS" + uRES.getUniqueRESID() + " wurcs:has_anobase ?subano" + uRES.getUniqueRESID() + " .\n");
		}
		return sb.toString();
	}
	
	
	public static String getMStoSubsumes(LinkedList<UniqueRES> a_aURESs) {
		StringBuilder  sb = new StringBuilder();

		
		WURCSExporter export = new WURCSExporter();
		for (UniqueRES uRES : a_aURESs) {
			
			sb.append("  <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)) + "> wurcs:subsumes ?MS" + uRES.getUniqueRESID() + " .\n");
		}
		return sb.toString();
	}
	
	
	public static String getuniqueRES(LinkedList<UniqueRES> a_aURESs) {
		StringBuilder  sb = new StringBuilder();
//		sb.append("# uniqueRES\n");
		sb.append("  ?gseq wurcs:has_uniqueRES ");
		
		int m_iRES = 1;
		for (UniqueRES uRES : a_aURESs) {
			String m_strEnd = ",";
			if (a_aURESs.size() == m_iRES){
				m_strEnd = ".";
			}
			sb.append("  ?uRES" + uRES.getUniqueRESID() + m_strEnd);
			m_iRES++;
		}
		sb.append("\n");
		
		WURCSExporter export = new WURCSExporter();
		// is_monosaccharide Section
		//  ?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a6d21122h-2a_2-6_5*NCC%2F3%3DO> .
		for (UniqueRES uRES : a_aURESs) {
			
			sb.append("  ?uRES" + uRES.getUniqueRESID() + " wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)) + "> .\n");
		}
		return sb.toString();
	}

	public static String getHasWURCS(String a_strWURCS){
		StringBuilder  sb = new StringBuilder();
		sb.append("  ?gseq glycan:has_sequence \"" + a_strWURCS + "\"^^xsd:string .\n");
		return sb.toString();
	}
	
	public static String getWURCS(LinkedList<String> t_aOption){
		StringBuilder  sb = new StringBuilder();
		if (t_aOption.contains("wurcs")) {
//			sb.append("# WURCS\n");
			sb.append("  ?gseq glycan:has_sequence ?wurcs .\n");
		}
		return sb.toString();
	}

	
	
	public static String getBind(){
		StringBuilder  sb = new StringBuilder();
//		sb.append("# BIND\n");
		sb.append("  BIND( iri(replace(str(?glycan), \"http://rdf.glycoinfo.org/glycan/\", \"<img src=\\\"http://www.glytoucan.org/glyspace/service/glycans/\")) as ?glycan2)\n");

		sb.append("  BIND( iri(concat(?glycan2, \"/image?style=extended&format=png&notation=cfg\\\"/>\")) as ?glycans )\n");

		return sb.toString();
	}
	
	
	
	public static String getGseq(LinkedList<String> t_aOption){
		StringBuilder  sb = new StringBuilder();
//		sb.append("# SEQ\n");
		
		boolean m_bBlazegraph = false;
		if (t_aOption.contains("blazegraph")) {
			m_bBlazegraph = true;
		}

		if (!m_bBlazegraph) {
			sb.append("  ?glycan glycan:has_glycosequence ?gseq \n");
		}
		else {
			sb.append("  ?glycan glycan:has_glycosequence ?gseq .\n");
		}
		
		
		
		return sb.toString();
	}
	
	
	
	
	public static String getGLIPSting(GLIP a_oGLIP){
		return a_oGLIP.getRESIndex() + a_oGLIP.getBackbonePosition() + a_oGLIP.getBackboneDirection() ;
	}
	
	public static String getGraph(int t_RES_Count, LinkedList<String> t_aOption){
		StringBuilder  sb = new StringBuilder();
		boolean m_bBlazegraph = false;
		if (t_aOption.contains("blazegraph")) {
			m_bBlazegraph = true;
		}
		// select graph
		for (String m_str : t_aOption){
			if (m_str.startsWith("FROM")) {
				
				if (!m_bBlazegraph) {
					sb.append(m_str.replace("FROM","FROM ") + "\n");
				}
				else {
					
					int t_iREScount[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,33,34,36,37,40,54,60,103};
					
					for (int t_iCount : t_iREScount) {
						if (t_iCount >= t_RES_Count) {
//							sb.append(m_str.replace("FROM","FROM ").replace(">","/" + t_iCount + ">") + "\n");
//							sb.append(m_str.replace("FROM","FROM ").replace(">","/" + t_iCount + "/ms>") + "\n");
						}
					}
					sb.append("# " + m_str.replace("FROM","FROM ").replace(">","/>") + "\n");
					sb.append("# " + m_str.replace("FROM","FROM ").replace(">","/ms>") + "\n");

				}
			}
		}
		return sb.toString();
	}
	
	
	public static String getSelect(LinkedList<String> t_aOption) {
		StringBuilder  sb = new StringBuilder();
		
		boolean m_bBlazegraph = false;
		if (t_aOption.contains("blazegraph")) {
			m_bBlazegraph = true;
		}
		
//		sb.append("# SELECT\n");
		if (!t_aOption.contains("count")) {
			
			//with subquery
			if (!m_bBlazegraph) {
				sb.append("SELECT DISTINCT ?glycans\n");
			}
			else {
				sb.append("SELECT DISTINCT ?glycan\n");
			}
			
			if (!t_aOption.contains(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch)) {
				if (t_aOption.contains("wurcs")) {
					
					if (!m_bBlazegraph) {
						sb.append("  str ( ?wurcs ) AS ?WURCS\n");
					}
					else {
						sb.append("  ?wurcs\n");
					}
				}
			}
		}
		else {
			//with subquery
			if (!m_bBlazegraph) {
				sb.append("SELECT count (DISTINCT ?glycans) AS ?count\n");
			}
			else{
				sb.append("SELECT count (DISTINCT ?glycan) AS ?count\n");
			}
		}
		return sb.toString();
	}
	
	public static String getPrefix(LinkedList<String> t_aOption){
		StringBuilder  sb = new StringBuilder();
				
//		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
//		sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
//		sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n");
		
		if (t_aOption.contains(WURCSSPARQLUtils_TBD.m_strSearchtypeExactStructureSearch)) {
			sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
		}
		
		sb.append("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n");
//		sb.append("PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n");
		sb.append("PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n");
		
		return sb.toString();
	}
	

	
	
	
	/**
	 * TODO:
	 * @param a_objWURCS
	 * @param a_uRES
	 * @return RES FILTER in SPARQL Query
	 */
	public static String getRESFilter_TBD(WURCSArray a_oWURCSArray) {
		
		StringBuilder sb = new StringBuilder();
		LinkedList<String> t_aRESFILTERs = new LinkedList<String>();
		
		for (UniqueRES t_oURes : a_oWURCSArray.getUniqueRESs()) {
			for (RES t_oRes1 : a_oWURCSArray.getRESs()) {
				for (RES t_oRes2 : a_oWURCSArray.getRESs()) {
					if (WURCSDataConverter.convertRESIndexToID(t_oRes1.getRESIndex()) < WURCSDataConverter.convertRESIndexToID(t_oRes2.getRESIndex()) ) {
						if (t_oURes.getUniqueRESID() == t_oRes1.getUniqueRESID() && t_oURes.getUniqueRESID() == t_oRes2.getUniqueRESID()){
							if (t_oRes1.getRESIndex() != t_oRes2.getRESIndex()) {
								t_aRESFILTERs.add("?RES" + t_oRes1.getRESIndex() + " != ?RES" + t_oRes2.getRESIndex());
							}
						}
					}
				}
			}
		}
		
		if (t_aRESFILTERs.size() > 0) {
			sb.append("  FILTER (");
			int m_it_aRESFILTERs = 1;
			String endString = " && ";
			for (String t_StrResPair : t_aRESFILTERs) {
				if (t_aRESFILTERs.size() == m_it_aRESFILTERs) endString = " ";
					sb.append(t_StrResPair);
				sb.append(endString);
				m_it_aRESFILTERs++;
			}
			sb.append(") . \n");
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
//		sb.append("# LIN FILTER\n");
				
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
		return a_str.replace("-", "").replace(" ", "").replace("|", "").replace("~", "Repeat").replace("%", "Pro").replace("?", "q").replace(".", "d");
	}
	
	
}
