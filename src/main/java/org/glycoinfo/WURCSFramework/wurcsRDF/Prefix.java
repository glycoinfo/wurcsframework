package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class Prefix {
	
	/* 
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix owl:   <http://www.w3.org/2002/07/owl#> .
@prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .
@prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .
@prefix wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#> .

	 */
	
	public static String m_strRdfs 		= "rdsf";
	public static String m_strXsd 			= "xsd";
	public static String m_strRdf 			= "rdf";
	public static String m_strOwl 			= "owl";
	public static String m_strGlycan 		= "glycan";
	public static String m_strGlyTouCan 	= "glytoucan";
	public static String m_strWURCS 		= "wurcs";
	public static String m_strDcterm 		= "dcterms";
	
	private static String m_strRdfsURI			= "http://www.w3.org/2000/01/rdf-schema#";
	private static String m_strXsdURI			= "http://www.w3.org/2001/XMLSchema#";
	private static String m_strRdfURI			= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static String m_strOwlURI			= "http://www.w3.org/2002/07/owl#";
	private static String m_strGlycanURI		= "http://purl.jp/bio/12/glyco/glycan#";
	private static String m_strGlyTouCanURI		= "http://www.glytoucan.org/glyco/owl/glytoucan#";
	private static String m_strWurcsURI			= "http://www.glycoinfo.org/glyco/owl/wurcs#";
	
	private static String m_strDctermURI = "http://purl.org/dc/terms/";
	
	//TODO:
	/**
	 * WURCS-RDF Prefix String
	 * @param a_strPrefix = rdsf, xsd, rdf, owl, glycan, glytoucan, wurcs
	 * @return prefixURI
	 */
	public static String getPrefix(String a_strPrefix, Boolean a_bPrefix){
		String m_strPrefix = null;
		
		switch (a_strPrefix) {
			case "rdfs":
				return m_strPrefix = (!a_bPrefix) ? m_strRdfsURI : a_strPrefix;
			case "xsd":
				return m_strPrefix = (!a_bPrefix) ? m_strXsdURI : a_strPrefix;
//				return m_strXsdURI;
			case "rdf":
				return m_strPrefix = (!a_bPrefix) ? m_strRdfURI : a_strPrefix;
//				return m_strRdfURI;
			case "owl":
				return m_strPrefix = (!a_bPrefix) ? m_strOwlURI : a_strPrefix;
//				return m_strOwlURI;
			case "glycan":
				return m_strPrefix = (!a_bPrefix) ? m_strGlycanURI : a_strPrefix;
//				return m_strGlycanURI;
			case "glytoucan":
				return m_strPrefix = (!a_bPrefix) ? m_strGlyTouCanURI : a_strPrefix;
//				return m_strGlyTouCanURI;
			case "wurcs":
				return m_strPrefix = (!a_bPrefix) ? m_strWurcsURI : a_strPrefix;
//				return m_strWurcsURI;
			case "dcterm":
				return m_strPrefix = (!a_bPrefix) ? m_strDctermURI : a_strPrefix;

		}
		
		return m_strPrefix;
	}
	
	// TODO:
	/**
	 * 
	 * @return
	 */
	public static String getPrefixs(){
		StringBuilder sb = new StringBuilder();
		sb.append("# WURCS-RDF ver 2015.02.10\n");
		// @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n
		sb.append("@prefix "); sb.append(m_strRdfs); sb.append(": <"); sb.append(m_strRdfsURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strXsd); sb.append(": <"); sb.append(m_strXsdURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strRdf); sb.append(": <"); sb.append(m_strRdfURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strOwl); sb.append(": <"); sb.append(m_strOwlURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strGlycan); sb.append(": <"); sb.append(m_strGlycanURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strGlyTouCan); sb.append(": <"); sb.append(m_strGlyTouCanURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strWURCS); sb.append(": <"); sb.append(m_strWurcsURI); sb.append("> . \n");
		sb.append("@prefix "); sb.append(m_strDcterm); sb.append(": <"); sb.append(m_strDctermURI); sb.append("> . \n");
		return sb.toString();
	}
	


}
