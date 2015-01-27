package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class SaccharideTriple {
/*	
	# Saccharide
	<http://rdf.glycoinfo.org/glycan/GxxxxxMS>
		a	glycan:saccharide ;
		glycan:has_glycosequence	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs> .
*/
	
	private String m_subject_uriSaccharide;
	private String m_Predicate_strHas_Glycosequence;
	private String m_object_uriGlycosequence;

	public SaccharideTriple(String  a_strAccessionNumber) {
		this.m_subject_uriSaccharide = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber;
		this.m_Predicate_strHas_Glycosequence = Predicate.getHas_glycosequence();
		this.m_object_uriGlycosequence = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs";
	}

	//TODO: 
	// http://www.easyrdf.org/converter
	public String get_SaccharideTtl(Boolean a_bPrefix) {
		StringBuilder sb = new StringBuilder();
		
			sb.append("# Saccharide\n");
			sb.append("<" + this.m_subject_uriSaccharide + ">\n");
			sb.append("\ta " + Predicate.getPredicateString("glycan", "saccharide", a_bPrefix) + " ;\n");
			sb.append("\t" + Predicate.getPredicateString("glycan", this.m_Predicate_strHas_Glycosequence, a_bPrefix) + " <" + this.m_object_uriGlycosequence + "> .\n");

		return sb.toString();
	}
	

	
	
	
}
