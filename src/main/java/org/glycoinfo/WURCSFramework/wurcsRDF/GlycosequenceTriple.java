package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class GlycosequenceTriple {
	/*
	# Glycosequence
	<http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs>
		a glycan:glycosequence ;
		
		wurcs:has_uniqueRES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1> ;
		wurcs:has_uniqueRES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/2> ;
		
		wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/h5122h-2b_2-5> ;
		wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/22122h-1a_1-5> ;
		
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4> ;
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b6-c2u1*Se*> ;
		wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/c1-d2*S*%7E10-n> ;
		
		wurcs:has_root_RES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/a> ;
		
		glycan:has_sequence "WURCS=2.0/2,4,3/[h5122h-2b_2-5][22122h-1a_1-5]/1-2-1-1/b1-a2|a4_b6-c2u1*Se*_c1-d2*S*~10-n"^^xsd:string ;
		glycan:in_carbohydrate_format   glycan:carbohydrate_format_wurcs ;

		owl:sameAs	<http://rdf.glycoinfo.org/glycan/wurcs/WURCS%3D2.0%2F2%2C4%2C3%2F%5Bh5122h-2b_2-5%5D%5B22122h-1a_1-5%5D%2F1-2-1-1%2Fb1-a2%7Ca4_b6-c2u1*Se*_c1-d2*S*%7E10-n> .
	*/
	
	// subject
	private String m_subject_uriGlycosequence;
	
	// object
	// object of a
	private String m_object_a = "glycan:glycosequence";
	private LinkedList<String> m_object_uniqueRESIndexs = new LinkedList<String>();
	private LinkedList<String> m_object_monosaccharides = new LinkedList<String>();
	private LinkedList<String> m_object_LINs = new LinkedList<String>();
	private String m_object_uriHas_root_RES;
	private String m_object_strWURCS;
	private String m_object_In_carbohydrate_format = "glycan:carbohydrate_format_wurcs";
	private String m_object_sameAs;
	
	//TODO: 
	public GlycosequenceTriple(String  a_strAccessionNumber, WURCSArray a_objWURCS) {
		WURCSExporter export = new WURCSExporter();
		// subject
		this.m_subject_uriGlycosequence = URI.getGlycoInfoGlycanURI() + a_strAccessionNumber + "/wurcs";
		String m_strObjectURI =  URI.getGlycoInfoGlycanURI() + "wurcs/";
		String t_strWURCS2 = this.m_subject_uriGlycosequence + "/2.0/";
		// object of glycan:has_sequence
		this.m_object_strWURCS = export.getWURCSString(a_objWURCS);
		
		for (UniqueRES uRES : a_objWURCS.getUniqueRESs()){
			// object of wurcs:has_uniqueRES
			//this.m_object_uniqueRESIndexs.add(t_strWURCS2 + "uniqueRES/" + String.valueOf(uRES.getUniqueRESID()));
			this.m_object_uniqueRESIndexs.add(t_strWURCS2 + "uniqueRES/" + uRES.getUniqueRESID());
			
			// object of wurcs:has_monosaccharide
			this.m_object_monosaccharides.add(m_strObjectURI + "2.0/monosaccharide/" + WURCSStringUtils.getURLString(export.getUniqueRESString(uRES)));
			
//			System.out.println("uRES.getUniqueRESID(): " + uRES.getUniqueRESID());
			
			if (uRES.getUniqueRESID() == 1){
				// object of wurcs:has_root_RES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/RES/a> ;
				this.m_object_uriHas_root_RES = t_strWURCS2 + "RES/a";
			}
		}
		// object of wurcs:has_LIN
		for (LIN lin : a_objWURCS.getLINs()){
			this.m_object_LINs.add(t_strWURCS2 + "LIN/" + WURCSStringUtils.getURLString(export.getLINString(lin)));
		}
		// object of owl:sameAs
		this.m_object_sameAs = m_strObjectURI + WURCSStringUtils.getURLString(export.getWURCSString(a_objWURCS));
	}
	
	//TODO: 
	public String get_GlycosequenceTriple(WURCSArray a_objWURCS, Boolean a_bPrefix) {
		StringBuilder sb = new StringBuilder();
//		WURCSExporter export = new WURCSExporter();
		
		sb.append("# Glycosequence\n");
		// subject
		sb.append("<" + this.m_subject_uriGlycosequence + ">\n");
		// a glycan:glycosequence ;
		sb.append("\ta " + Predicate.getPredicateString("glycan", "glycosequence", a_bPrefix) + " ;\n");
		
		// wurcs:uniqueRES_count
		sb.append("\t" + Predicate.getPredicateString("wurcs", Predicate.getUniqueRES_count(), a_bPrefix) + " \"" + a_objWURCS.getUniqueRESCount() + "\"^^xsd:integer ;\n");
		// wurcs:RES_count
		sb.append("\t" + Predicate.getPredicateString("wurcs", Predicate.getRES_count(), a_bPrefix) + " \"" + a_objWURCS.getRESCount() + "\"^^xsd:integer ;\n");
		
		// wurcs:LIN_count
		sb.append("\t" + Predicate.getPredicateString("wurcs", Predicate.getLIN_count(), a_bPrefix) + " \"" + a_objWURCS.getLINCount() + "\"^^xsd:integer ;\n");

		// has_root_RES
		// this.m_object_uriHas_root_RES
		sb.append("\t" + Predicate.getPredicateString("wurcs", Predicate.getHas_root_RES(), a_bPrefix) + " <" + this.m_object_uriHas_root_RES + "> ;\n");
		
		
		
		for (String ures : m_object_uniqueRESIndexs) {
			// wurcs:has_uniqueRES <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/uniqueRES/1> ;
			sb.append("\t" + Predicate.getPredicateString("wurcs", "has_uniqueRES", a_bPrefix) + " <" + ures + "> ;\n");
		}
		
		for (String mss : m_object_monosaccharides) {
			// wurcs:has_monosaccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/22122h-1a_1-5> ;
			sb.append("\t" + Predicate.getPredicateString("wurcs", "has_monosaccharide", a_bPrefix) + " <" + mss + "> ;\n");
		}
				
		for (String lin : m_object_LINs) {
			// wurcs:has_LIN <http://rdf.glycoinfo.org/glycan/GxxxxxMS/wurcs/2.0/LIN/b1-a2%7Ca4> ;
			sb.append("\t" + Predicate.getPredicateString("wurcs", "has_LIN", a_bPrefix) + " <" + lin + "> ;\n");
		}		
		
		// glycan:has_sequence
		sb.append("\t" + Predicate.getPredicateString("glycan", Predicate.getHas_sequence(), a_bPrefix) + " \"" + this.m_object_strWURCS + "\"^^xsd:string ;\n");
		// glycan:in_carbohydrate_format 
		sb.append("\t" + Predicate.getPredicateString("glycan", Predicate.getIn_carbohydrate_format(), a_bPrefix) + " " + Predicate.getPredicateString("glycan", Predicate.getCarbohydrate_format_wurcs(), a_bPrefix) + " ;\n");
		// owl:sameAs
		sb.append("\t" + Predicate.getPredicateString("owl", "sameAs", a_bPrefix) + " <" + this.m_object_sameAs + "> .\n");
		
		return sb.toString();
	}
	
}
