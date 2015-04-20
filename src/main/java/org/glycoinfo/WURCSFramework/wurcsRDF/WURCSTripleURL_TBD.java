package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.net.URL;

import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.GLYCAN;
import org.glycoinfo.WURCSFramework.wurcsRDF.constant.WURCS;

public enum WURCSTripleURL_TBD{
	// unused Accession Number
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/WURCS%3D2.0%2F1%2C1%2C0%2F%5B12221m-1a_1-5%5D%2F1%2F
	WURCS_URL()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5
	MS()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/monosaccharide/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/basetype/u2112h
	BASE_TYPE()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/basetype/"+urlenc(objectToString(val)) ;}},
	//  http://rdf.glycoinfo.org/glycan/wurcs/2.0/anobase/%3C0%3E-%3Fa
	ANO_BASE()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/anobase/"+urlenc(objectToString(val)) ;}},
	//  http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/1-5
	MOD()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/MOD/"+urlenc(objectToString(val)) ;}},
	//  http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/%3C0%3E
	SC()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/SkeletonCode/"+urlenc(objectToString(val)) ;}},

	// need Accession Number
	//	http://rdf.glycoinfo.org/glycan/G00012MO
	GLYCAN_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no ;}},
	//	http://rdf.glycoinfo.org/glycan/G00012MO/wurcs/2.0
	GSEQ_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/GLIP/a%3F	(a?)
	GLIP()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/GLIP/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/G01779GQ/wurcs/2.0/GLIP/a%3F	(a?)
	GLIP_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/GLIP/"+objectToString(val) ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/GLIPS/g3%7Cg6
	GLIPS()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/GLIPS/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/G00025LQ/wurcs/2.0/GLIPS/g3%7Cg6
  	GLIPS_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/GLIPS/"+objectToString(val) ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIN/h2-g3%7Cg6
	LIN()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/LIN/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/G00025LQ/wurcs/2.0/LIN/h2-g3%7Cg6
	LIN_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/LIN/"+objectToString(val) ;}},
	//	http://rdf.glycoinfo.org/glycan/wurcs/2.0/RES/d
	RES()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/RES/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/G00009BX/wurcs/2.0/RES/d
	RES_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/RES/"+objectToString(val) ;}},
	LIP()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/LIP/"+urlenc(objectToString(val)) ;}},
	LIPS()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/LIPS/"+urlenc(objectToString(val)) ;}},
	//	http://rdf.glycoinfo.org/glycan/G00009BX/wurcs/2.0/uniqueRES/2
	UNIQ_RES()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/uniqueRES/"+urlenc(objectToString(val)) ;}},

	// For WURCSSequence
	GRES_AC()	{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+"/"+ac_no+WURCS.VERSION+"/GRES/"+objectToString(val) ;}},
	GLIN()		{ public String get(String ac_no, Object val)	{ return GLYCAN.BASE_URL+WURCS.VERSION+"/GLIN/"+urlenc(objectToString(val)) ;}};

	 abstract public String get(String ac_no, Object value);

	 private static String urlenc(String str){
		 return  WURCSStringUtils.getURLString(str);
	 }

	 private static String objectToString(Object obj){
		if(obj instanceof  String){
			return (String)obj;
		}else if( obj instanceof Integer){
			return String.valueOf((Integer)obj);
		}else if( obj instanceof Double){
			return String.valueOf((Double)obj);
		}else if( obj instanceof Boolean){
			return String.valueOf((Boolean)obj);
		}else if( obj instanceof Character){
			return String.valueOf((Character)obj);
		}else if( obj instanceof URL){
			return ((URL)obj).toString();
		}
		return null;
	 }
 }
