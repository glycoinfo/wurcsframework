package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class URI {
	
	//String a_strAccessionNumberURI = "<http://rdf.glycoinfo.org/glycan/" + a_strAccessionNumber;
	private static String m_strGlycoInfoGlycanURI = "http://rdf.glycoinfo.org/glycan/";
	
	/**
	 * 
	 * @return http://rdf.glycoinfo.org/glycan/
	 */
	public static String getGlycoInfoGlycanURI(){
		return m_strGlycoInfoGlycanURI;
	}
}
