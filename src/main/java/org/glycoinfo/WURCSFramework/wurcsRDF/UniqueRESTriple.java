package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporterOld;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class UniqueRESTriple {
/*
	# uniqueRES
	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1>
		a	wurcs:uniqueRES ;
		wurcs:is_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/h5122h-2b_2-5_2*NCC%2F3%3DO> .

	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/2>
		a	wurcs:uniqueRES ;
		wurcs:is_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/22122h-1a_1-5> .
*/
	// subject
	private LinkedList<String[]> m_uriUniqueRES = new LinkedList<String[]>();

	// object
//	private String m_object_urimonosaccharide;

	//TODO: 
	public UniqueRESTriple(String  a_strAccessionNumber, LinkedList<UniqueRES> a_aURESs) {
		
		WURCSExporterOld export = new WURCSExporterOld();
		for (UniqueRES a_aURES : a_aURESs) {
			String[] m_sub_obj = {"", ""};
			m_sub_obj[0] = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs/2.0/uniqueRES/" + a_aURES.getUniqueRESID();
			m_sub_obj[1] = URI.getGlycoInfoGlycanURI() + "wurcs/2.0/monosaccharide/" + WURCSStringUtils.getURLString(export.getUniqueRESString(a_aURES));
			m_uriUniqueRES.add(m_sub_obj);
		}
	}
	
	//TODO: 
	private String get_UniqueRESTriple(String a_subject_uriUniqueRES, String a_object_urimonosaccharide, Boolean a_bPrefix) {
		StringBuilder sb = new StringBuilder();
//		sb.append("# uniqueRES\n");
		// subject
		sb.append("<" + a_subject_uriUniqueRES + ">\n");
		// a glycan:glycosequence ;
		sb.append("\ta " + Predicate.getPredicateString("wurcs", "uniqueRES", a_bPrefix) + " ;\n");
		
		// wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/22122h-1a_1-5> ;
		sb.append("\t" + Predicate.getPredicateString("wurcs", "is_monosaccharide", a_bPrefix) + " <" + a_object_urimonosaccharide + "> .\n");
		
		return sb.toString();
	}
	
	//TODO: 
	public String get_UniqueRESTriples(Boolean a_bPrefix){
		StringBuilder sb = new StringBuilder();
		sb.append("# uniqueRES\n");
		for (String[] a_aSubObj : this.m_uriUniqueRES) {
			sb.append(get_UniqueRESTriple(a_aSubObj[0], a_aSubObj[1], a_bPrefix));
		}
		return sb.toString();
	}
	
}
