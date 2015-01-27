package org.glycoinfo.WURCSFramework.wurcsRDF;

//TODO: 
public class MODTriple {
/*
 # MOD
<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/2-5> 
	a	wurcs:MOD ;
	wurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/2> ;
	wurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/5> .

<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/2*NCC%2F3%3DO> 
	a	wurcs:MOD ;
		wurcs:has_MAP "*NCC%2F3%3DO"^^xsd:string ;
	wurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/2> .

 */
	// subject
	private String m_subject_uriMOD;

	// object
	private String m_object_uriLIP;
	private String m_object_strAnomericSymbol;
	private int m_object_strMAP;
	
	//TODO: 
	public MODTriple() {
	}
	
	
	
}
