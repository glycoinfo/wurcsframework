package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class LIPTriple {
	
	// subject
	private String m_subject_uriLIP;

	// object
	// object
	private String m_object_strRESIndex;
	private char m_object_strDirection;
	private int m_object_strMAPPosition;
	private int m_object_strSCPosition;
	private ProbabilityTriple m_object_objProbability;

	
	//TODO: 
	public LIPTriple(
			String a_strAccessionNumber
			, String a_subject_uriLIP
			, int a_object_iSCPosition
			, char a_object_strDirection
			, int a_object_iMAPPosition
			//, ProbabilityTriple a_object_objProbability
			) {
		this.m_subject_uriLIP = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/LIP/" + a_subject_uriLIP;
		this.m_object_strDirection = a_object_strDirection;
		this.m_object_strMAPPosition = a_object_iMAPPosition;
		this.m_object_strSCPosition = a_object_iSCPosition;
		//this.m_object_objProbability = a_object_objProbability;

	}
	
	
	
}
