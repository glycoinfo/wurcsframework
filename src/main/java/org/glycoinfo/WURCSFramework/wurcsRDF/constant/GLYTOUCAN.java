package org.glycoinfo.WURCSFramework.wurcsRDF.constant;

//import com.hp.hpl.jena.rdf.model.Property;
//import com.hp.hpl.jena.rdf.model.Resource;

public class GLYTOUCAN{
	public static final String PREFIX = "glytoucan";
	public static final String NS = "http://www.glytoucan.org/glyco/owl/glytoucan#";
	public static String getPREFIX() {return PREFIX;}
	public static String getURI() {return NS;}
	public static String getURI(String val) {return NS+val;}
//	public static final Resource NAMESPACE = m_model.createResource( NS );
//	public static final Resource FULL_LANG = m_model.getResource( getURI() );
	
//	public static final Property maxCardinality = m_model.createProperty( "http://www.glytoucan.org/glyco/owl/glytoucan#has_sequence" );
	
//	public static final Resource Thing = m_model.createResource( "http://www.glytoucan.org/glyco/owl/glytoucan#carbohydrate_format_wurcs" );

}
