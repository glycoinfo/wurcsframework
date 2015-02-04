package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class RESTriple {
/*
# RES
<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/a>
	a	wurcs:RES ;
	wurcs:is_uniqueRES	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1> ;
	wurcs:has_LIN	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4> .

<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/b>
	a	wurcs:RES ;
	wurcs:is_uniqueRES	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/2> ;
	wurcs:has_LIN	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4> ;
	wurcs:has_LIN	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b6-c2u1*Se*> .
*/

	// object
	private LinkedList<LinkedList<String>> m_Objects = new LinkedList<LinkedList<String>>();
	
	
	//TODO: 
	public RESTriple(String  a_strAccessionNumber, WURCSArray a_objWURCS) {
		
		for (RES a_aRES : a_objWURCS.getRESs()) {
			LinkedList<String> m_obj = new LinkedList<String>();
			// subject RES: http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/a
			m_obj.add(URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/RES/" + a_aRES.getRESIndex());
			// object uniqueRES: http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1
			m_obj.add(URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/uniqueRES/" + a_aRES.getUniqueRESID());
			
			LinkedList<String> m_lins = this.getHasLINs(a_strAccessionNumber, a_aRES.getRESIndex(), a_objWURCS);
			for (String m_str : m_lins) {
				// object LIN: http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4
				if (!m_obj.contains(m_str)) { m_obj.add(m_str); }
			}
			this.m_Objects.add(m_obj);
		}
	}
	
	//TODO: 
	/**
	 * Extract LIN information 
	 * @param a_strAccessionNumber
	 * @param a_strRESIndex
	 * @param a_objWURCS
	 * @return URIs of LIN contains RESIndex
	 */
	private LinkedList<String> getHasLINs(String a_strAccessionNumber, String a_strRESIndex ,WURCSArray a_objWURCS){
		LinkedList<String> m_LINs = new LinkedList<String>();
		WURCSExporter export = new WURCSExporter();
		for (LIN lin : a_objWURCS.getLINs()) {
			for (GLIPs glips : lin.getListOfGLIPs()) {
				
				for (GLIP glip : glips.getGLIPs()) {
				
					if (glip.getRESIndex().equals(a_strRESIndex)) { 
						m_LINs.add(URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/LIN/" + WURCSStringUtils.getURLString(export.getLINString(lin))); 
					}
					if (glip.getRESIndex().equals(a_strRESIndex)) { 
						m_LINs.add(URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/LIN/" + WURCSStringUtils.getURLString(export.getLINString(lin))); 
					}
				}
			}
		}
		return m_LINs;
	}
	
	
	
	
	//TODO: 
	public String getRESTriple(Boolean a_bPrefix) {
		StringBuilder t_sbRES = new StringBuilder();
		t_sbRES.append("# RES\n");
		for (LinkedList<String> m_data : this.m_Objects) {
			
			int i = 1;
			for (String s : m_data) {
				String endString = ";";
				if (m_data.size() == i) endString = ".";
				
				if (i == 1) {
					// Subject
					t_sbRES.append("<" + s + "> \n");
					t_sbRES.append("\ta " + Predicate.getPredicateString("wurcs", "RES", a_bPrefix) + " ;\n");
					
				}
				else if (i == 2) {
					//wurcs:is_uniqueRES
					t_sbRES.append("\t" + Predicate.getPredicateString("wurcs", "is_uniqueRES", a_bPrefix) + " <" + s + "> " + endString + "\n");
				}
				else {
				// wurcs:has_LIN
					t_sbRES.append("\t" + Predicate.getPredicateString("wurcs", "has_LIN", a_bPrefix) + " <" + s + "> " + endString + "\n");
				}
				i++;
			}
			t_sbRES.append("\n");
		}
		return t_sbRES.toString();
	}
}
