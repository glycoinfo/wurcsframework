package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;

//TODO: 
//public class FuzzyGLIPTriple extends FuzzyLIPTriple {
public class GLIPSTriple {
/*
 # fuzzyGLIP
<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/fuzzyGLIP/a2%7Ca4> 
	a	wurcs:fuzzyGLIP ;
	wurcs:has_GLIP	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/a2> ; 
	wurcs:has_GLIP	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/a4> .  
 */
	// subject
	private String m_subject_uriGLIPS;
	// object
	private LinkedList<String> m_object_uriGIP = new LinkedList<String>();
	private String m_object_strAlternative;
	private String m_object_strAlternativeURI;

/*
	// object
	private int m_object_iSCPosition;
	private String m_object_strDirection;
	private int m_object_iMAPPosition;
	private String m_object_strAlternative;
*/
	//TODO: 
	public GLIPSTriple(
			String a_strAccessionNumber
			, GLIPs a_fglip
//			, int a_object_iSCPosition
//			, String a_object_strDirection
//			, int a_object_iMAPPosition
//			, String a_object_strAlternative
			) {
//		super(
//				a_object_iSCPosition
//				, a_object_strDirection
//				, a_object_iMAPPosition
//				, a_object_strAlternative
//				);
		WURCSExporter export = new WURCSExporter();
		this.m_subject_uriGLIPS = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/GLIPS/" + WURCSStringUtils.getURLString(export.getGLIPsString(a_fglip));
		// GLIP
		for (GLIP glip : a_fglip.getGLIPs()) {
			this.m_object_uriGIP.add(URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/GLIP/" + WURCSStringUtils.getURLString(export.getGLIPString(glip)));
		}
		if (a_fglip.getAlternativeType().length() > 0) {
			this.m_object_strAlternative = a_fglip.getAlternativeType();
			this.m_object_strAlternativeURI = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/ALT/" + WURCSStringUtils.getURLString(a_fglip.getAlternativeType());
		}
	}
	
	//TODO: 
	public String getGLIPSTriple(Boolean a_bPrefix) {
		StringBuilder t_sb = new StringBuilder();
		//t_sb.append("# FuzzyGLIP\n");
		String endString = ";";
		
		// subject
		t_sb.append("<" + this.m_subject_uriGLIPS + ">\n");
		// a	wurcs:fuzzyGLIP ;
		t_sb.append("\ta wurcs:GLIPS ;\n");
		// wurcs:has_GLIP	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/a2> ;

		if (this.m_object_strAlternative != null && this.m_object_strAlternative != "") {
			t_sb.append("\t" + Predicate.getPredicateString("wurcs", "has_alternativeType", a_bPrefix) + " \"" + this.m_object_strAlternative + "\"^^xsd:string " + endString + "\n");
			t_sb.append("\t" + Predicate.getPredicateString("wurcs", "has_alternative", a_bPrefix) + " <" + this.m_object_strAlternativeURI + "> " + endString + "\n");
		}

		int i = 1;
		for (String str : this.m_object_uriGIP) {
			endString = ";";
			//endString = (this.m_object_uriGIP.size() != i) ? ";" : ".";
			t_sb.append("\t" + Predicate.getPredicateString("wurcs", "has_GLIP", a_bPrefix) + " <" + str + "> " + endString + "\n");
			i++;
		}
		
		// wurcs:isFuzzy "true"^^xsd:boolean .
		endString = ".";
		if (i == 2) {
			t_sb.append("\t" + Predicate.getPredicateString("wurcs", "isFuzzy", a_bPrefix) + " \"false\"^^xsd:boolean " + endString + "\n");
		}
		else {
			t_sb.append("\t" + Predicate.getPredicateString("wurcs", "isFuzzy", a_bPrefix) + " \"true\"^^xsd:boolean " + endString + "\n");
		}
		
		
		t_sb.append("\n");
		
		return t_sb.toString();
	}
	
}
