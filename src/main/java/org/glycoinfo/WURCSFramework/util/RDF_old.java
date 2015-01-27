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

//TODO: 
public class RDF_old {
	private String getURLString(String a_strUrl){
		
		try {
			a_strUrl = URLEncoder.encode(a_strUrl,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a_strUrl;
	}
	
	//TODO: use WURCSExporter
	public String[] getRDFxml(String AccessionNumber, WURCSArray a_oWURCS) {
		String t_strRdfXml = "";
		String t_strMonosaccharideRdfXml = "";		
		
		WURCSExporter export = new WURCSExporter();
		String t_strWURCSString = export.getWURCSString(a_oWURCS);
		t_strRdfXml += "#" + t_strWURCSString + "\n";
		t_strRdfXml += "#" + AccessionNumber + "\n";
		t_strRdfXml += "\n";
		t_strRdfXml += "# Saccharide\n";
		String a_strAccessionNumberURI = "<http://rdf.glycoinfo.org/glycan/" + AccessionNumber;
		t_strRdfXml += a_strAccessionNumberURI + ">\n";
		t_strRdfXml += "\ta	glycan:saccharide ;\n";
		t_strRdfXml += "\tglycan:has_glycosequence\t" + a_strAccessionNumberURI + "/wurcs> .\n";
		t_strRdfXml += "\n";
		t_strRdfXml += "# Glycosequence\n";
		t_strRdfXml += a_strAccessionNumberURI + "/wurcs>\n";
		t_strRdfXml += "\ta glycan:glycosequence ;\n";
		t_strRdfXml += "\tglycan:in_carbohydrate_format   glycan:carbohydrate_format_wurcs ;\n";

		for (UniqueRES uRES : a_oWURCS.getUniqueRESs()) {
		t_strRdfXml += "\twurcs:has_uniqueRES " + a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + uRES.getUniqueRESID() + "> ;\n";
		}
		t_strRdfXml += "\twurcs:has_root_RES " + a_strAccessionNumberURI + "/wurcs/2.0/RES/a> ;\n";
		t_strRdfXml += "\tglycan:has_sequence \"" + t_strWURCSString + "\"^^xsd:string ;\n";
		
		for (UniqueRES uRes : a_oWURCS.getUniqueRESs()) {
			t_strRdfXml += "\twurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/" 
					+ getURLString(export.getUniqueRESString(uRes)) + "> ;\n";
		}
		
		// uLIN
		String str_LIN = "";
		String str_uLIN = "";
		
		// has_LIN
		int i = 1;
		for (LIN lin : a_oWURCS.getLINs()) {
			t_strRdfXml += "\twurcs:has_LIN ";
			t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) + "> ;\n";
			str_LIN = a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) + "> .\n";
			String strExp = "([a-zA-Z?]+)([0-9?]+)-([a-zA-Z?]+)([0-9?]+)([.]*)";
			Matcher size = Pattern.compile(strExp).matcher(export.getLINString(lin));
			if (size.find()){
				String sizegroup2 = size.group(2);
				String sizegroup4 = size.group(4);

				if (WURCSNumberUtils.isInteger(sizegroup2) && WURCSNumberUtils.isInteger(sizegroup4)) {
					if (Integer.parseInt(sizegroup2) <= Integer.parseInt(sizegroup4)){
						str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								+ sizegroup2 + "-" + sizegroup4 + ">\n";
						str_uLIN += "\ta wurcs:uniqueLIN ;\n";
						str_uLIN += "\twurcs:has_LIN " + str_LIN;
					}
					else if (Integer.parseInt(sizegroup2) > Integer.parseInt(sizegroup4)){
						str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
								+ sizegroup4 + "-" + sizegroup2 + ">\n";
						str_uLIN += "\ta wurcs:uniqueLIN ;\n";
						str_uLIN += "\twurcs:has_LIN " + str_LIN;
					}
				}
				else if (!WURCSNumberUtils.isInteger(sizegroup2)){
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup4 + "-" + sizegroup2 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
				}
				else if (!WURCSNumberUtils.isInteger(sizegroup4)){
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup2 + "-" + sizegroup4 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
				}
				else {
					str_uLIN += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/uniqueLIN/" 
							+ sizegroup2 + "-" + sizegroup4 + ">\n";
					str_uLIN += "\ta wurcs:uniqueLIN ;\n";
					str_uLIN += "\twurcs:has_LIN " + str_LIN;
				}
			}
		}
		
		t_strRdfXml += "\towl:sameAs	<http://rdf.glycoinfo.org/glycan/wurcs/" + getURLString(t_strWURCSString) + "> .\n";

		
		if (str_uLIN.length() > 1) {
			t_strRdfXml += "\n";
		}
		
		t_strRdfXml += "\n";
		t_strRdfXml += "# uniqueRES\n";
		t_strMonosaccharideRdfXml += "# monosaccharide\n";
		
		i = 1;
		for (UniqueRES uRes : a_oWURCS.getUniqueRESs()) {
			String endString = ".";
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				break;
			}
			t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + i + ">\n";
			t_strRdfXml += "\ta	wurcs:uniqueRES ;\n";
			String t_strMS = "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/" 
					+ getURLString(export.getUniqueRESString(uRes)) + ">";
			
			t_strRdfXml += "\twurcs:is_monosaccharide	" + t_strMS + " .\n";
			
//			t_strMonosaccharideRdfXml += "# monosaccharide\n";
			t_strMonosaccharideRdfXml += t_strMS + "\n";
			
			t_strMonosaccharideRdfXml += "\ta	wurcs:monosaccharide ; \n";
			
			t_strMonosaccharideRdfXml += "\twurcs:has_SC	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/" 
					+ getURLString(uRes.getSkeletonCode()) + "> ;\n";
			t_strMonosaccharideRdfXml += "\twurcs:has_anomeric_symbol\t\"" + uRes.getAnomericSymbol() + "\"^^xsd:string ;\n";
			t_strMonosaccharideRdfXml += "\twurcs:has_anomeric_position\t\"" + uRes.getAnomericPosition() + "\"^^xsd:integer " + endString + "\n";
			int j = 1;
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				if (uRes.getMODs().size() == j) endString = ".";
				t_strMonosaccharideRdfXml += "\twurcs:has_MOD	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + getURLString(export.getMODString(mod)) + "> " + endString + "\n";
				j++;
			}
			t_strRdfXml += "\n";
			t_strMonosaccharideRdfXml += "\n";
			i++;
		}
		t_strRdfXml += "\n";	
		
		t_strMonosaccharideRdfXml += "# MOD\n";
		// MOD
		String endString = ";";
		
		for (UniqueRES uRes : a_oWURCS.getUniqueRESs()) {
			
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				
				t_strMonosaccharideRdfXml += "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + getURLString(export.getMODString(mod)) + "> \n";
				t_strMonosaccharideRdfXml += "\ta	wurcs:MOD ;\n";

//				if (mod.getLIPs().size() > 0) endString = ";";
				endString = (mod.getLIPs().size() > 0) ? ";": ".";
				if (mod.getMAPCode().length() > 0) {
					t_strMonosaccharideRdfXml += "\t	wurcs:has_MAP \"" + getURLString(mod.getMAPCode()) + "\"^^xsd:string " + endString + "\n";;
				}
				int j = 1;
				for (LIP lip: mod.getLIPs()) {
					endString = (mod.getLIPs().size() == j) ? "." : ";";
					t_strMonosaccharideRdfXml += "\twurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/" + getURLString(export.getLIPString(lip)) + "> " + endString + "\n";
					j++;
				}
				

				t_strMonosaccharideRdfXml += "\n";
				
			}
			t_strMonosaccharideRdfXml += "\n";
		}
		t_strMonosaccharideRdfXml += "\n";		
		
		
		
		t_strRdfXml += "# RES\n";
		i = 1;
		for (RES res : a_oWURCS.getRESs()) {
			t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/RES/" + res.getRESIndex() + ">\n";
			t_strRdfXml += "\ta	wurcs:RES ;\n";
			
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
			
			endString = ";";
			if (strLINs.size() == 0) endString = ".";
			t_strRdfXml += "\twurcs:is_uniqueRES\t" + a_strAccessionNumberURI + "/wurcs/2.0/uniqueRES/" + res.getUniqueRESID() + "> " + endString + "\n";
			
			i = 1;
			for (String s : strLINs) {
				endString = ";";
				if (strLINs.size() == i) endString = ".";
					t_strRdfXml += "\twurcs:has_LIN\t" + a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(s) + "> " + endString + "\n";
				i++;
			}
			t_strRdfXml += "\n";
		}
		
		t_strRdfXml += "# LIN\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			
			t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/LIN/" + getURLString(export.getLINString(lin)) +">\n";
			t_strRdfXml += "\ta	wurcs:LIN ;\n";
			
			// has_MAP
			if (lin.getMAPCode() != null)
				if ( lin.getMAPCode().length() > 0)
				t_strRdfXml += "\t	wurcs:has_MAP \"" + lin.getMAPCode() + "\"^^xsd:string ;\n";
			
			// repeat unit
			if (lin.isRepeatingUnit()) {
				t_strRdfXml += "\t	wurcs:is_repeat \"true\"^^xsd:boolean ;\n";
				t_strRdfXml += "\t	wurcs:max_repeat_count \"" + lin.getMaxRepeatCount() + "\"^^xsd:integer ;\n";
				t_strRdfXml += "\t	wurcs:min_repeat_count \"" + lin.getMinRepeatCount() + "\"^^xsd:integer ;\n";
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
				endString = ";";
				if (lin.getGLIPs().size() == i) {
					if (t_bFuzzyGLIP == false){
						endString = ".";
					}
				}
				
				t_strRdfXml += "\twurcs:has_GLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + "> " + endString + " \n";
				i++;
			}
			// fuzzyGLIP
			i = 1;
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
				endString = ";";
				if (lin.getFuzzyGLIPs().size() == i) {
					endString = ".";
				}
				t_strRdfXml += "\twurcs:has_fuzzyGLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/fuzzyGLIP/" + getURLString(export.getFuzzyGLIPString(fglip)) + "> " + endString + "\n";
				i++;
			}
			t_strRdfXml += "\n";
		}
		
		t_strRdfXml += "# GLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (GLIP glip : lin.getGLIPs()) {
				t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + ">  \n";
				t_strRdfXml += "\ta	wurcs:GLIP ;\n";
				t_strRdfXml += "\twurcs:has_RES\t" + a_strAccessionNumberURI + "/wurcs/2.0/RES/" + glip.getRESIndex() + "> ; \n";
				if (glip.getBackboneDirection() != ' ')
					t_strRdfXml += "\twurcs:has_direction\t\"" + glip.getBackboneDirection() + "\"^^xsd:string ; \n";
				if (glip.getModificationPosition() != 0)
					t_strRdfXml += "\twurcs:has_MAP_position\t\"" + glip.getModificationPosition() + "\"^^xsd:integer ; \n";
				t_strRdfXml += "\twurcs:has_SC_position\t\"" + glip.getBackbonePosition() + "\"^^xsd:integer .\n";
			}
			t_strRdfXml += "\n";
		}

		t_strRdfXml += "\n";

		t_strRdfXml += "# fuzzyGLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
//				String strFuzzyGLIP = "";
				String t_strFrontAlt = "";
				String t_strBackAlt = "";
				boolean t_bAlternative = false;
				endString = ";";
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
				t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/fuzzyGLIP/" + getURLString(export.getFuzzyGLIPString(fglip)) + "> \n";
				t_strRdfXml += "\ta	wurcs:fuzzyGLIP ;\n";
				
				i = 1;
				for (GLIP glip : fglip.getFuzzyGLIPs()) {
					if (fglip.getFuzzyGLIPs().size() == i && t_bAlternative == false) {
						endString = ".";
					}
					i++;
					t_strRdfXml += "\twurcs:has_GLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + "> " + endString + " \n";
				}
				if (fglip.getAlternativeType().length() > 0) {
					t_strRdfXml += "\twurcs:has_alternative\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(fglip.getAlternativeType()) + "> . \n";
				}
			}
		}

		t_strRdfXml += "\n";
		
		t_strRdfXml += "# GLIP of fuzzyGLIP\n";
		for (LIN lin : a_oWURCS.getLINs()) {
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {				
				for (GLIP glip : fglip.getFuzzyGLIPs()) {
					t_strRdfXml += a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + getURLString(export.getGLIPString(glip)) + ">  \n";
					t_strRdfXml += "\ta	wurcs:GLIP ;\n";
					t_strRdfXml += "\t\twurcs:has_RES\t" + a_strAccessionNumberURI + "/wurcs/2.0/RES/" + glip.getRESIndex() + "> ; \n";
					if (glip.getBackboneDirection() != ' ')
						t_strRdfXml += "\twurcs:has_direction\t\"" + glip.getBackboneDirection() + "\"^^xsd:string ; \n";
					if (glip.getModificationPosition() != 0)
							t_strRdfXml += "\twurcs:has_MAP_position\t\"" + glip.getModificationPosition() + "\"^^xsd:integer ; \n";
					t_strRdfXml += "\t\twurcs:has_SC_position\t\"" + glip.getBackbonePosition() + "\"^^xsd:integer .\n";
				}
			}
		}

		t_strRdfXml += "\n";

		String[] rdfxmls = {t_strRdfXml, t_strMonosaccharideRdfXml };
		return  rdfxmls;
	}
/*
	private boolean isNumber(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}
*/
}
