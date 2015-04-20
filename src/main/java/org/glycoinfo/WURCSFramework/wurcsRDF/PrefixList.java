package org.glycoinfo.WURCSFramework.wurcsRDF;

public enum PrefixList {

	/*
	 * @prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
	 * @prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
	 * @prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
	 * @prefix owl:   <http://www.w3.org/2002/07/owl#> .
	 * @prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .
	 * @prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .
	 * @prefix wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#> .
	 * @prefix dcterms: <http://purl.org/dc/terms/> .
	 */
	RDFS     ("rdfs",      "http://www.w3.org/2000/01/rdf-schema#"),
	XSD      ("xsd",       "http://www.w3.org/2001/XMLSchema#"),
	RDF      ("rdf",       "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
	OWL      ("owl",       "http://www.w3.org/2002/07/owl#"),
	GLYCAN   ("glycan",    "http://purl.jp/bio/12/glyco/glycan#"),
	GLYTOUCAN("glytoucan", "http://www.glytoucan.org/glyco/owl/glytoucan#"),
	WURCS    ("wurcs",     "http://www.glycoinfo.org/glyco/owl/wurcs#"),
	DCTERM   ("dcterms",   "http://purl.org/dc/terms/");

	private String m_strPrefix;
	private String m_strIRI;

	private PrefixList(String a_strPrefix, String a_strPrefixURI) {
		this.m_strPrefix    = a_strPrefix;
		this.m_strIRI = a_strPrefixURI;
	}

	public String getPrefix() {
		return this.m_strPrefix;
	}

	public String getPrefixURI() {
		return this.m_strIRI;
	}

	/**
	 * Get prefix list for head of RDF
	 * @return List of prefix
	 */
	public static String getPrefixList() {
		StringBuilder sb = new StringBuilder();
		for ( PrefixList pl : PrefixList.values() ) {
			sb.append("@prefix "+pl.m_strPrefix+": <"+pl.m_strIRI+"> . \n");
		}
		return sb.toString();
	}
}
