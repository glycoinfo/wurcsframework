package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class GLIPTriple extends LIPTriple {
/*
 # GLIP
<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/GLIP/c2u1>  
	a	wurcs:GLIP ;
	wurcs:has_RES	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/c> ; 
	wurcs:has_direction	"u"^^xsd:string ; 
	wurcs:has_MAP_position	"1"^^xsd:integer ; 
	wurcs:has_SC_position	"2"^^xsd:integer .

 */
	// subject
	private String m_subject_uriGLIP;

	// object
	private String m_object_strRESIndex;
	private char m_object_strDirection;
	private int m_object_strMAPPosition;
	private int m_object_strSCPosition;
	private ProbabilityTriple m_object_objProbability;

	//TODO: 
	public GLIPTriple(
			String a_strAccessionNumber
			, String a_subject_strGLIP
			, String a_object_strRESIndex
			, int a_object_iSCPosition
			, char a_object_strDirection
			, int a_object_iMAPPosition
			//, ProbabilityTriple a_object_objProbability
			) {
		super(
				a_strAccessionNumber
				, a_subject_strGLIP
				, a_object_iSCPosition
				, a_object_strDirection
				, a_object_iMAPPosition
				//, a_object_objProbability
				);
		this.m_subject_uriGLIP = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/GLIP/" + a_subject_strGLIP;
		this.m_object_strRESIndex = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/RES/" + a_object_strRESIndex;
		this.m_object_strDirection = a_object_strDirection;
		this.m_object_strMAPPosition = a_object_iMAPPosition;
		this.m_object_strSCPosition = a_object_iSCPosition;
		//this.m_object_objProbability = a_object_objProbability;
		
	}

	//TODO: 
	public String getGLIPTriple(Boolean a_bPrefix) {
		StringBuilder t_sb = new StringBuilder();
		//t_sb.append("# GLIP\n");
		String endString = ";";
		
		// subject
		t_sb.append("<" + this.m_subject_uriGLIP + ">\n");
		t_sb.append("\ta wurcs:GLIP ;\n");
		// wurcs:has_RES	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/c> ; 
		t_sb.append("\t" + Predicate_TBD.getPredicateString("wurcs", "has_RES", a_bPrefix) + " <" + this.m_object_strRESIndex + "> " + endString + "\n");
		if (this.m_object_strDirection != ' ')
			t_sb.append("\t" + Predicate_TBD.getPredicateString("wurcs", "has_direction", a_bPrefix) + " \"" + this.m_object_strDirection + "\"^^xsd:string " + endString + "\n");
		
		if (this.m_object_strMAPPosition != 0)
			t_sb.append("\t" + Predicate_TBD.getPredicateString("wurcs", "has_MAP_position", a_bPrefix) + " \"" + this.m_object_strMAPPosition + "\"^^xsd:integer " + endString + "\n");
		endString = ".";
		t_sb.append("\t" + Predicate_TBD.getPredicateString("wurcs", "has_SC_position", a_bPrefix) + " \"" + this.m_object_strSCPosition + "\"^^xsd:integer " + endString + "\n");
		t_sb.append("\n");
		
		return t_sb.toString();
	}


}