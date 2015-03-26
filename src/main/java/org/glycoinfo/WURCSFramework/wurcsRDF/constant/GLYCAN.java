package org.glycoinfo.WURCSFramework.wurcsRDF.constant;

//import com.hp.hpl.jena.rdf.model.Property;
//import com.hp.hpl.jena.rdf.model.Resource;

public class GLYCAN{
	public static final String PREFIX = "glycan";
	public static final String NS = "http://purl.jp/bio/12/glyco/glycan#";
	public static String getPrefix() {return PREFIX;}
	public static String getURI() {return NS;}
	public static String getURI(String val) {return NS+val;}
//	public static final Resource NAMESPACE = m_model.createResource( NS );
//	public static final Resource FULL_LANG = m_model.getResource( getURI() );
	public static final String BASE_URL = "http://rdf.glycoinfo.org/glycan";
	
	// Property
	public static final String HAS_GSEQ			= getURI("has_glycosequence");
	public static final String HAS_SEQ			= getURI("has_sequence");
	public static final String IN_CARB_FORMAT	= getURI("in_carbohydrate_format");

	// Object
	public static final String A_SACCHARIDE		= getURI("saccharide");
	public static final String A_GSEQ			= getURI("glycosequence");
	public static final String FORMAT_WURCS		= getURI("carbohydrate_format_wurcs");

}
