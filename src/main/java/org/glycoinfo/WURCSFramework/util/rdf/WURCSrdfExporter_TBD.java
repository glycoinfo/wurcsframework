package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIPOld;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
//import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdf;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSrdfGLIPs;

//TODO: 
public class WURCSrdfExporter_TBD {
	private String getURLString(String a_strUrl){
		
		try {
			a_strUrl = URLEncoder.encode(a_strUrl,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a_strUrl;
	}
	
	
	private String getRDFttl(Object a_obj){
		return "";
	}
	
	
	
	private String getMonosaccharideTriple(UniqueRES a_objURES){
		String t_strMonosaccharide = "";
		
	
		
		
		return t_strMonosaccharide;
	}
	
	
	
	
	
	//TODO: use WURCSExporter
	public String[] getRDFxml(String AccessionNumber, WURCSrdfGLIPs a_oWURCS) {
		String t_strRdfXml = a_oWURCS.getWURCS_RDF();
		String t_strMonosaccharideRdfXml = a_oWURCS.getWURCS_monosaccharide_RDF();		
		


		String[] rdfxmls = {t_strRdfXml, t_strMonosaccharideRdfXml };
		return  rdfxmls;
	}

}
