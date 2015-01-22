package org.glycoinfo.WURCSFramework.util;



import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class RDF {
	private String getURLString(String a_strUrl){
		
		try {
			a_strUrl = URLEncoder.encode(a_strUrl,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a_strUrl;
	}
	
	//TODO: use WURCSExporter
	public String getRDFxml(String AccessionNumber, WURCSArray a_oWURCS) {
		String a_strRdfXml = "";

		
		WURCSExporter export = new WURCSExporter();
		String t_strWURCSString = export.getWURCSString(a_oWURCS);
		a_strRdfXml += "#" + t_strWURCSString + "\n";
		a_strRdfXml += "#" + AccessionNumber + "\n";
		a_strRdfXml += "\n";
		a_strRdfXml += "# Saccharide\n";
		String a_strAccessionNumberURI = "<http://rdf.glycoinfo.org/glycan/" + AccessionNumber;
		a_strRdfXml += a_strAccessionNumberURI + ">\n";
		a_strRdfXml += "\ta	glycan:saccharide ;\n";
		a_strRdfXml += "\tglycan:has_glycosequence\t" + a_strAccessionNumberURI + "/wurcs> .\n";
		a_strRdfXml += "\n";
		a_strRdfXml += "# Glycosequence\n";
		a_strRdfXml += a_strAccessionNumberURI + "/wurcs>\n";
		a_strRdfXml += "\ta glycan:glycosequence ;\n";
		a_strRdfXml += "\tglycan:in_carbohydrate_format   glycan:carbohydrate_format_wurcs ;\n";

		//for (int i = 1; i <= a_oWURCS.getUniqueRESCount(); i++) {
		for (UniqueRES uRES : a_oWURCS.getUniqueRESs()) {
		a_strRdfXml += "\twurcs:has_uniqueRES " + a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + uRES.getUniqueRESID() + "> ;\n";
		}
		a_strRdfXml += "\twurcs:has_root_RES " + a_strAccessionNumberURI + "/wurcs/2.0/RES/a> ;\n";
		//String strWURCS = t_strWURCSString; //.replace("\\", "|");;
		a_strRdfXml += "\tglycan:has_sequence \"" + t_strWURCSString + "\"^^xsd:string ;\n";
		
		//a_strRdfXml += "\towl:sameAs	<http://rdf.glycoinfo.org/glycan/wurcs/" + getURLString(a_oWURCS.getWURCS()) + "> ;\n";
		
		for (UniqueRES uRes : a_oWURCS.getUniqueRESs()) {
			a_strRdfXml += "\twurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/" 
					+ getURLString(export.getUniqueRESString(uRes)) + "> ;\n";
		}
		
		// uLIN
		String str_LIN = "";
		String str_uLIN = "";
		
		// has_LIN
		int i = 1;
		for (LIN lin : a_oWURCS.getLINs()) {
			a_strRdfXml += "\twurcs:has_LIN ";
			//String endString = ";";
			//if (a_oWURCS.getLINs().size() == i) endString = ".";
			a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) + "> ;\n";
			str_LIN = a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) + "> .\n";
			//String strExp = "^([a-zA-Z?\\\\]*)([0-9?\\\\]*)-([a-zA-Z?\\\\]*)([0-9?\\\\]*)([.]*)";
			String strExp = "([a-zA-Z?]+)([0-9?]+)-([a-zA-Z?]+)([0-9?]+)([.]*)";
			Matcher size = Pattern.compile(strExp).matcher(export.getLINString(lin));
			if (size.find()){
				//a_strRdfXml += size.group(2) + "-" + size.group(4) + "\n";
				String sizegroup2 = size.group(2);
				String sizegroup4 = size.group(4);

				if (isNumber(sizegroup2) && isNumber(sizegroup4)) {
					//a_strRdfXml += "\t\t\t(0)" + size.group(0) + "(1)" + size.group(1) + "(2)" + size.group(2) + "(3)" + size.group(3) + "(4)" + size.group(4) + "\n";
					
					if (Integer.parseInt(sizegroup2) <= Integer.parseInt(sizegroup4)){
						str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								+ sizegroup2 + "-" + sizegroup4 + ">\n";
						str_uLIN += "\ta wurcs:uniqueLIN ;\n";
						str_uLIN += "\twurcs:has_LIN " + str_LIN;
						//a_strRdfXml += "\twurcs:has_uniqueLIN <http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								//+ sizegroup2 + "-" + sizegroup4 + "> ;\n";
					}
					else if (Integer.parseInt(sizegroup2) > Integer.parseInt(sizegroup4)){
						str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								+ sizegroup4 + "-" + sizegroup2 + ">\n";
						str_uLIN += "\ta wurcs:uniqueLIN ;\n";
						str_uLIN += "\twurcs:has_LIN " + str_LIN;
						//a_strRdfXml += "\twurcs:has_uniqueLIN <http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								//+ sizegroup4 + "-" + sizegroup2 + "> ;\n";
					}
				}
				else if (!isNumber(sizegroup2)){
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup4 + "-" + sizegroup2 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
					//a_strRdfXml += "\twurcs:has_uniqueLIN <http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							//+ sizegroup4 + "-" + sizegroup2 + "> ;\n";
				}
				else if (!isNumber(sizegroup4)){
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup2 + "-" + sizegroup4 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
					//a_strRdfXml += "\twurcs:has_uniqueLIN <http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							//+ sizegroup2 + "-" + sizegroup4 + "> ;\n";
				}
				else {
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup2 + "-" + sizegroup4 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
					//a_strRdfXml += "\twurcs:has_uniqueLIN <http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							//+ sizegroup2 + "-" + sizegroup4 + "> ;\n";
				}
			}
			//i++;
		}
		
		a_strRdfXml += "\towl:sameAs	<http://rdf.glycoinfo.org/glycan/wurcs/" + getURLString(t_strWURCSString) + "> .\n";

		
		if (str_uLIN.length() > 1) {
			a_strRdfXml += "\n";
			//a_strRdfXml += str_uLIN;
		}
		
		a_strRdfXml += "\n";
		a_strRdfXml += "# uniqueRES\n";
		i = 1;
		for (UniqueRES uRes : a_oWURCS.getUniqueRESs()) {
			String endString = ".";
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				break;
			}
			a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + i + ">\n";
			a_strRdfXml += "\ta	wurcs:uniqueRES ;\n";
			a_strRdfXml += "\twurcs:is_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/" 
					+ getURLString(export.getUniqueRESString(uRes)) + "> ;\n";
			a_strRdfXml += "\twurcs:has_skeleton_code	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/" 
					+ getURLString(uRes.getSkeletonCode()) + "> ;\n";
			a_strRdfXml += "\twurcs:has_anomeric_symbol\t\"" + uRes.getAnomericSymbol() + "\"^^xsd:string ;\n";
			a_strRdfXml += "\twurcs:has_anomeric_position\t\"" + uRes.getAnomericPosition() + "\"^^xsd:integer " + endString + "\n";
			int j = 1;
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				if (uRes.getMODs().size() == j) endString = ".";
				a_strRdfXml += "\twurcs:has_MOD	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + getURLString(export.getMODString(mod)) + "> " + endString + "\n";
				j++;
			}
			a_strRdfXml += "\n";
			i++;
		}
		
		// MOD
		
		
		
		
		a_strRdfXml += "# RES\n";
		i = 1;
		for (RES res : a_oWURCS.getRESs()) {
			a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/RES/" + res.getRESIndex() + ">\n";
			a_strRdfXml += "\ta	wurcs:RES ;\n";
			
			LinkedList<String> strLINs = new LinkedList<String>();
			for (LIN strlin : a_oWURCS.getLINs()) {
				String strExp = "^([a-zA-Z?\\\\]*)([0-9?\\\\]*)-([a-zA-Z?\\\\]*)([0-9?\\\\]*)([.]*)";
				Matcher size = Pattern.compile(strExp).matcher(export.getLINString(strlin));
				if (size.find()){
					if (size.group(1).equals(res.getRESIndex()) || size.group(3).equals(res.getRESIndex())) {
						strLINs.addLast(export.getLINString(strlin));
					}
				}
			}
			
			String endString = ";";
			if (strLINs.size() == 0) endString = ".";
			a_strRdfXml += "\twurcs:is_uniqueRES\t" + a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + res.getUniqueRESID() + "> " + endString + "\n";
			
			i = 1;
			for (String s : strLINs) {
				endString = ";";
				if (strLINs.size() == i) endString = ".";
					a_strRdfXml += "\twurcs:has_LIN\t" + a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(s) + "> " + endString + "\n";
				i++;
			}
			a_strRdfXml += "\n";
		}
		
		a_strRdfXml += "# LIN\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			String t_strMAPcosde = "";
			if (lin.getMAPCode() != null)
				t_strMAPcosde = lin.getMAPCode();
			
			String t_strRepeating = "";
			if (lin.isRepeatingUnit()) {
				t_strRepeating += "~";
				if (lin.getMinRepeatCount() != 0){
					t_strRepeating += lin.getMinRepeatCount();
					if (lin.getMaxRepeatCount() != 0){
						t_strRepeating += "-";
					}
				}
				if (lin.getMaxRepeatCount() != 0){
					t_strRepeating += lin.getMaxRepeatCount();
				}
			}
			a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) +">\n";
			a_strRdfXml += "\ta	wurcs:LIN ;\n";
			
			// has_MAP
			if (lin.getMAPCode() != null)
				a_strRdfXml += "\t	wurcs:has_MAP \"" + lin.getMAPCode() + "\"^^xsd:string ;\n";
			
			// repeat unit
			if (lin.isRepeatingUnit()) {
				a_strRdfXml += "\t	wurcs:isRepeat \"true\"^^xsd:string ;\n";
				a_strRdfXml += "\t	wurcs:MaxRepeatCount \"" + lin.getMaxRepeatCount() + "\"^^xsd:integer ;\n";
				a_strRdfXml += "\t	wurcs:MinRepeatCount \"" + lin.getMinRepeatCount() + "\"^^xsd:integer ;\n";
//				System.out.println("isRepeatingUnit: " + lin.isRepeatingUnit());
			}
			else {
//				System.out.println("isRepeatingUnit: " + lin.isRepeatingUnit());
			}

			
			boolean t_bFuzzyGLIP = false;
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
				for (GLIP glip : fglip.getFuzzyGLIPs() ) {
					t_bFuzzyGLIP = true;
				}
			}
			
			// GLIP
			i = 1;
			for (GLIP glip : lin.getGLIPs()) {
				String endString = ";";
				if (lin.getGLIPs().size() == i) {
					if (t_bFuzzyGLIP == false){
						endString = ".";
					}
				}
				
				a_strRdfXml += "\twurcs:has_GLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + "> " + endString + " \n";
				i++;
			}
			// fuzzyGLIP
			i = 1;
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
				String endString = ";";
//				String strFuzzyGLIP = "";
//				for (GLIP glip : fglip.getFuzzyGLIPs() ) {
//					strFuzzyGLIP += "|" + getURLString(glip.getRESIndex() + export.getFuzzyGLIPString(fglip);
//					//a_strRdfXml += "\twurcs:has_FuzzyGLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/FuzzyGLIP/" + getURLString(glip.getRESIndex() + glip.getLIPString()) + "> \n";
//				}
				if (lin.getFuzzyGLIPs().size() == i) {
					endString = ".";
				}
				a_strRdfXml += "\twurcs:has_fuzzyGLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/fuzzyGLIP/" + getURLString(export.getFuzzyGLIPString(fglip)) + "> " + endString + "\n";
				i++;
			}
						
			a_strRdfXml += "\n";
		}
			
		
		a_strRdfXml += "# GLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (GLIP glip : lin.getGLIPs()) {
				a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + ">  \n";
				a_strRdfXml += "\ta	wurcs:GLIP ;\n";
				a_strRdfXml += "\twurcs:has_RES\t" + a_strAccessionNumberURI + "/wurcs/2.0/RES/" + glip.getRESIndex() + "> ; \n";
				if (glip.getBackboneDirection() != ' ')
					a_strRdfXml += "\twurcs:has_Direction\t\"" + glip.getBackboneDirection() + "\"^^xsd:string ; \n";
				if (glip.getModificationPosition() != 0)
					a_strRdfXml += "\twurcs:has_MAP_position\t\"" + glip.getModificationPosition() + "\"^^xsd:integer ; \n";
				a_strRdfXml += "\twurcs:has_SC_position\t\"" + glip.getBackbonePosition() + "\"^^xsd:integer .\n";
			}
			a_strRdfXml += "\n";
		}

		a_strRdfXml += "\n";

		a_strRdfXml += "# fuzzyGLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
				String strFuzzyGLIP = "";
				String t_strFrontAlt = "";
				String t_strBackAlt = "";
				boolean t_bAlternative = false;
				String endString = ";";
				if (fglip.getAlternativeType().length() > 0) {
					endString = ";";
					t_bAlternative = true;
					if (fglip.getAlternativeType() == "{") {
						t_strFrontAlt = "{";
					}
					else if (fglip.getAlternativeType() == "}") {
						t_strBackAlt = "}";
					}
				}

//				for (GLIP glip : fglip.getFuzzyGLIPs() ) {
//					strFuzzyGLIP += "|" + getURLString(glip.getRESIndex() + glip.getLIPString());
//					//a_strRdfXml += "\twurcs:has_FuzzyGLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/FuzzyGLIP/" + getURLString(glip.getRESIndex() + glip.getLIPString()) + "> \n";
//				}
				a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/fuzzyGLIP/" + getURLString(export.getFuzzyGLIPString(fglip)) + "> \n";
				a_strRdfXml += "\ta	wurcs:fuzzyGLIP ;\n";
				
				i = 1;
				for (GLIP glip : fglip.getFuzzyGLIPs()) {
					if (fglip.getFuzzyGLIPs().size() == i && t_bAlternative == false) {
						endString = ".";
					}
					i++;
					a_strRdfXml += "\twurcs:has_GLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + "> " + endString + " \n";
				}
				
				
				
				
				if (fglip.getAlternativeType().length() > 0) {
					a_strRdfXml += "\twurcs:has_Alternative\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(fglip.getAlternativeType()) + "> . \n";
				}
			}
			//a_strRdfXml += "\n";
		}

		a_strRdfXml += "\n";
		
		a_strRdfXml += "# GLIP of fuzzyGLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
//				String strFuzzyGLIP = "";
//				for (GLIP glip : fglip.getFuzzyGLIPs() ) {
//					strFuzzyGLIP += "|" + getURLString(glip.getRESIndex() + glip.getLIPString());
//					//a_strRdfXml += "\twurcs:has_FuzzyGLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/FuzzyGLIP/" + getURLString(glip.getRESIndex() + glip.getLIPString()) + "> \n";
//				}
				//a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/FuzzyGLIP/" + getURLString(strFuzzyGLIP.substring(1)) + "> \n";
				//a_strRdfXml += "\ta	wurcs:FuzzyGLIP ;\n";
				
				for (GLIP glip : fglip.getFuzzyGLIPs()) {
					a_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + ">  \n";
					a_strRdfXml += "\ta	wurcs:GLIP ;\n";
					a_strRdfXml += "\t\twurcs:has_RES\t" + a_strAccessionNumberURI + "/wurcs/2.0/RES/" + glip.getRESIndex() + "> ; \n";
					if (glip.getBackboneDirection() != ' ')
						a_strRdfXml += "\twurcs:has_Direction\t\"" + glip.getBackboneDirection() + "\"^^xsd:string ; \n";
					if (glip.getModificationPosition() != 0)
							a_strRdfXml += "\twurcs:has_MAP_position\t\"" + glip.getModificationPosition() + "\"^^xsd:integer ; \n";
					a_strRdfXml += "\t\twurcs:has_SC_position\t\"" + glip.getBackbonePosition() + "\"^^xsd:integer .\n";
				}
			}
			//a_strRdfXml += "\n";
		}

		a_strRdfXml += "\n";

		
		
		
		//a_strRdfXml += "\n";
		
		
		return a_strRdfXml;
	}
	private boolean isNumber(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}
	
	
}
