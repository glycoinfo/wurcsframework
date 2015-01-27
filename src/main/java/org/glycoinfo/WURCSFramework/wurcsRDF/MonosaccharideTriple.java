package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class MonosaccharideTriple {
/*
 # monosaccharide
<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/h5122h-2b_2-5_2*NCC%2F3%3DO>
	a	wurcs:monosaccharide ; 
	wurcs:has_SC	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/h5122h> ;
	wurcs:has_anomeric_symbol	"b"^^xsd:string ;
	wurcs:has_anomeric_position	"2"^^xsd:integer ;
	wurcs:has_MOD	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/2-5> ;
	wurcs:has_MOD	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/2*NCC%2F3%3DO> . 
 */

	// subject
	private String m_subject_uriMonosaccharide;

	// object
	private String m_object_uriSkeletonCode;
	private String m_object_strAnomericSymbol;
	private int m_object_iAnomericPosition;
	private String m_object_uriMOD;
	
	//TODO: 
	public MonosaccharideTriple() {
	}
	
	
	
}
