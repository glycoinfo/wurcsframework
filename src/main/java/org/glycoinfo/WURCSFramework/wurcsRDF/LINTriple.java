package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;

//TODO: 
public class LINTriple {
/*
 # LIN
<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/c1-d2*S*%7E10%3An>
	a	wurcs:LIN ;
		wurcs:has_MAP "*S*"^^xsd:string ;
		wurcs:is_repeat "true"^^xsd:boolean ;
		wurcs:max_repeat_count "-1"^^xsd:integer ;
		wurcs:min_repeat_count "10"^^xsd:integer ;
		wurcs:has_GLIP	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/c1> ; 
		wurcs:has_GLIP	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/d2> . 
 */
	// subject
	private String m_subject_uriLIN;

	// object
	private String m_object_uriGLIP;
	private String m_object_uriFuzzyGLIP;
	private String m_object_strMAP;
	private RepeatTriple m_object_Repeat;
	
	private LinkedList<LinkedList<String[]>> m_objects = new LinkedList<LinkedList<String[]>>();
	
	
	//TODO: 
	public LINTriple(String a_strAccessionNumber, LinkedList<LIN> a_aLINs) {
		WURCSExporter export = new WURCSExporter();
		
		for (LIN lin : a_aLINs) {
			LinkedList<String[]> m_obj = new LinkedList<String[]>();
			m_obj.add(new String[] {"subject", URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/LIN/" + WURCSStringUtils.getURLString(export.getLINString(lin))});
			m_obj.add(new String[] {"a", "a\twurcs:LIN"});
			
			// has_MAP
			if (lin.getMAPCode() != null)
				if ( lin.getMAPCode().length() > 0)
					m_obj.add(new String[] {"has_MAP", "wurcs:has_MAP \"" + lin.getMAPCode() + "\"^^xsd:string"});
			
			// repeat unit
			if (lin.isRepeatingUnit()) {
				m_obj.add(new String[] {"is_repeat", "wurcs:is_repeat \"true\"^^xsd:boolean"});
				m_obj.add(new String[] {"max_repeat_count", "wurcs:max_repeat_count \"" + lin.getMaxRepeatCount() + "\"^^xsd:integer"});
				m_obj.add(new String[] {"min_repeat_count", "wurcs:min_repeat_count \"" + lin.getMinRepeatCount() + "\"^^xsd:integer"});
			}
			else {
				m_obj.add(new String[] {"is_repeat", "wurcs:is_repeat \"false\"^^xsd:boolean"});
			}
			
			// GLIP
			for (GLIP glip : export. lin.getGLIPs()) {
				m_obj.add(new String[] {"has_GLIP", "wurcs:has_GLIP\t<" + URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/GLIP/" + WURCSStringUtils.getURLString(export.getGLIPString(glip)) + ">"});
			}
			
			// fuzzyGLIP
			for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
				m_obj.add(new String[] {"has_fuzzyGLIP", "wurcs:has_fuzzyGLIP\t<" + URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/fuzzyGLIP/" + WURCSStringUtils.getURLString(export.getFuzzyGLIPString(fglip)) + ">"});
			}
			this.m_objects.add(m_obj);
		}
	}
	
	//TODO: 
	public String getLINTriple(Boolean a_bPrefix) {
		StringBuilder t_sb = new StringBuilder();
		t_sb.append("# LIN\n");
		for (LinkedList<String[]> objects : this.m_objects) {
			int i = 1;
			for (String[] obj : objects) {
				String endString = ";";
				endString = (objects.size() != i) ? ";" : ".";

				if (obj[0].equals("subject")) {
					t_sb.append("<" + obj[1] + ">\n");
				}
				else if (obj[0].equals("a")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("has_MAP")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("is_repeat")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("max_repeat_count")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("min_repeat_count")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("has_GLIP")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				else if (obj[0].equals("has_fuzzyGLIP")) {
					t_sb.append("\t" + obj[1] + " " + endString + "\n");
				}
				i++;
			}
			t_sb.append("\n");
		}
		
		
		return t_sb.toString();
	}
	
	
	
}
