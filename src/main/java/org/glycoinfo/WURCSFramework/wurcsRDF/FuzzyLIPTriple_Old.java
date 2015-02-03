package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class FuzzyLIPTriple_Old {
	
	// subject
	private String m_subject_uriFuzzyLIP;

	// object
	private int m_object_iSCPosition;
	private String m_object_strDirection;
	private int m_object_iMAPPosition;
	private String m_object_strAlternative;

	//TODO: 
	public FuzzyLIPTriple_Old(
			int a_object_iSCPosition
			, String a_object_strDirection
			, int a_object_iMAPPosition
			, String a_object_strAlternative
			) {
	}
	
	//TODO: 
	public void set_subject_uriFuzzyLIP(String a_subject_uriFuzzyLIP){
		this.m_subject_uriFuzzyLIP = a_subject_uriFuzzyLIP;
	}
	
}
