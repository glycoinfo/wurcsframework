package org.glycoinfo.WURCSFramework.wurcsRDF;

public class Saccharide {
//	subject		<http://rdf.glycoinfo.org/glycan/GxxxxxMS>
//	object	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs> 
	private String m_strUri = "<http://rdf.glycoinfo.org/glycan/";
	private String m_subject_uriSaccharide;
	private String m_object_uriGlycosequence;
	
	private String m_predivate_strSaccharide = "a glycan:saccharide";
	private String m_Predicate_strHas_Glycosequence = "glycan:has_glycosequence";
	private String m_strAccessionNumber;
	
	public Saccharide(String  a_strAccessionNumber) {
		this.m_strAccessionNumber = a_strAccessionNumber;
		this.m_subject_uriSaccharide = m_strUri +  a_strAccessionNumber + ">";
		this.m_object_uriGlycosequence = m_strUri + a_strAccessionNumber + "/wurcs>";
	}

	public String get_subject_uriSaccharide() {
		return this.m_subject_uriSaccharide;
	}
	
	public String get_object_uriGlycosequence() {
		return this.m_object_uriGlycosequence;
	}

	public String getAccessionNumber() {
		return this.m_strAccessionNumber;
	}
	
	public String getPredicate_saccharide() {
		return this.m_predivate_strSaccharide;
	}
	
	public String getPredicate_has_Glycosequence() {
		return this.m_Predicate_strHas_Glycosequence;
	}
	
	
	
	
}
