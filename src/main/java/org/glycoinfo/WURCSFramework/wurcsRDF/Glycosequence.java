package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class Glycosequence {
	/*
	# Glycosequence
	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs>
		a glycan:glycosequence ;
		glycan:in_carbohydrate_format   glycan:carbohydrate_format_wurcs ;
		wurcs:has_uniqueRES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1> ;
		wurcs:has_uniqueRES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/2> ;
		wurcs:has_root_RES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/a> ;
		glycan:has_sequence "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/b1-a2|a4_b6-c2u1*Se*_c1-d2*S*~10-n"^^xsd:string ;
		wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/h5122h-2b_2-5> ;
		wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/22122h-1a_1-5> ;
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4> ;
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b6-c2u1*Se*> ;
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/c1-d2*S*%7E10-n> ;
		owl:sameAs	<http://rdf.glycoinfo.org/glycan/wurcs/WURCS%3D2.0%2F2%2C4%2C3%2F%5Bh5122h-2b_2-5%5D%5B22122h-1a_1-5%5D%2F1-2-1-1%2Fb1-a2%7Ca4_b6-c2u1*Se*_c1-d2*S*%7E10-n> .
	*/
	private String m_strUri = "<http://rdf.glycoinfo.org/glycan/";
	private String m_strUriWURCS2 = m_strUri + "wurcs/2.0/";

	// subject
	private String m_subject_uriGlycosequence;
	
	private LinkedList<String> m_object_uniqueRESs;
	
	public Glycosequence(String  a_strAccessionNumber, WURCSArray a_objWURCS) {
		this.m_subject_uriGlycosequence = m_strUri +  a_strAccessionNumber + "/wurcs>";
		
		
	}
	

	public String get_subject_uriGlycosequence() {
		return this.m_subject_uriGlycosequence;
	}
	
}
