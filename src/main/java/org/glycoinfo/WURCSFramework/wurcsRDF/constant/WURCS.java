package org.glycoinfo.WURCSFramework.wurcsRDF.constant;

//import com.hp.hpl.jena.rdf.model.Property;
//import com.hp.hpl.jena.rdf.model.Resource;

public class WURCS{

//	private static Model m_model = ModelFactory.createDefaultModel();
	public static final String PREFIX = "wurcs";
	public static final String NS = "http://www.glycoinfo.org/glyco/owl/wurcs#";
	public static final String VERSION = "/wurcs/2.0";
	public static String getPrefix() {return PREFIX;}
	public static String getURI() {return NS;}
	public static String getURI(String val) {return NS+val;}
//	public static final Resource NAMESPACE = m_model.createResource( NS );
//	public static final Resource FULL_LANG = m_model.getResource( getURI() );
	public static final String BASE_URL = GLYCAN.BASE_URL+"/"+VERSION;

	// Property
	public static final String NUM_URES			= getURI("uniqueRES_count");
	public static final String NUM_RES			= getURI("RES_count");
	public static final String NUM_LIN			= getURI("LIN_count");
	public static final String IS_MS			= getURI("is_monosaccharide");
	public static final String IS_URES			= getURI("is_uniqueRES");
	public static final String IS_FUZZY			= getURI("is_fuzzy");
	public static final String IS_REP			= getURI("is_repeat");
	public static final String HAS_MS			= getURI("has_monosaccharide");
	public static final String HAS_LIN			= getURI("has_LIN");
	public static final String HAS_RING			= getURI("has_ring");
	public static final String HAS_SC			= getURI("has_SC");
	public static final String HAS_SC_POS		= getURI("has_SC_position");
	public static final String HAS_ANOM_POS		= getURI("has_anomeric_position");
	public static final String HAS_ANOM_SYMBOL	= getURI("has_anomeric_symbol");
	public static final String HAS_DIRECTION	= getURI("has_direction");
	public static final String HAS_STAR_INDEX	= getURI("has_MAP_position");
	public static final String HAS_BASETYPE		= getURI("has_basetype");
	public static final String HAS_ANOBASE		= getURI("has_anobase");
	public static final String HAS_MOD			= getURI("has_MOD");
	public static final String HAS_MAP			= getURI("has_MAP");
	public static final String HAS_LIPS			= getURI("has_LIPS");
	public static final String HAS_LIP			= getURI("has_LIP");
	public static final String HAS_GLIPS		= getURI("has_GLIPS");
	public static final String HAS_GLIP			= getURI("has_GLIP");
	public static final String HAS_RES			= getURI("has_RES");
	public static final String HAS_URES			= getURI("has_uniqueRES");
	public static final String HAS_ROOT_RES		= getURI("has_root_RES");
	public static final String HAS_REP_MIN		= getURI("has_rep_min");
	public static final String HAS_REP_MAX		= getURI("has_rep_max");
	public static final String HAS_B_PROB_UP	= getURI("has_backbone_prob_upper");
	public static final String HAS_B_PROB_LOW	= getURI("has_backbone_prob_lower");
	public static final String HAS_M_PROB_UP	= getURI("has_modification_prob_upper");
	public static final String HAS_M_PROB_LOW	= getURI("has_modification_prob_lower");
	public static final String SUBSUMES			= getURI("subsumes");

	/*
	NUM_URES       (PrefixList.WURCS, "uniqueRES_count",       ""),
	NUM_RES        (PrefixList.WURCS, "RES_count",             ""),
	NUM_LIN        (PrefixList.WURCS, "LIN_count",             ""),
	IS_MS          (PrefixList.WURCS, "is_monosaccharide",     ""),
	IS_URES        (PrefixList.WURCS, "is_uniqueRES",          ""),
	HAS_MS         (PrefixList.WURCS, "has_monosaccharide",    ""),
	HAS_URES       (PrefixList.WURCS, "has_uniqueRES",         ""),
	HAS_ROOT_RES   (PrefixList.WURCS, "has_root_RES",          ""),
	HAS_LIN        (PrefixList.WURCS, "has_LIN",               ""),
	HAS_RING       (PrefixList.WURCS, "has_ring",              ""),
	HAS_SC         (PrefixList.WURCS, "has_SC",                ""),
	HAS_ANOM_POS   (PrefixList.WURCS, "has_anomeric_position", ""),
	HAS_ANOM_SYMBOL(PrefixList.WURCS, "has_anomeric_symbol",   ""),
	HAS_SC_POS     (PrefixList.WURCS, "has_SC_position",       ""),
	HAS_DIRECTION  (PrefixList.WURCS, "has_direction",         ""),
	HAS_STAR_INDEX (PrefixList.WURCS, "has_MAP_position",      ""), // TODO: "MAP_position" to "star_index"
	HAS_BASETYPE   (PrefixList.WURCS, "has_basetype",          ""),
	HAS_ANOBASE    (PrefixList.WURCS, "has_anobase",           ""),
	HAS_MOD        (PrefixList.WURCS, "has_MOD",               ""),
	HAS_MAP        (PrefixList.WURCS, "has_MAPS",              ""),
	HAS_LIPS       (PrefixList.WURCS, "has_LIPS",              ""),
	HAS_LIP        (PrefixList.WURCS, "has_LIP",               ""),
	HAS_GLIPS      (PrefixList.WURCS, "has_GLIPS",             ""),
	HAS_GLIP       (PrefixList.WURCS, "has_GLIP",              ""),
	HAS_RES        (PrefixList.WURCS, "has_RES",               ""),
	IS_FUZZY       (PrefixList.WURCS, "is_fuzzy",              ""),
	IS_REP         (PrefixList.WURCS, "is_repeat",             ""),
	HAS_REP_MIN    (PrefixList.WURCS, "has_rep_min",           ""),
	HAS_REP_MAX    (PrefixList.WURCS, "has_rep_max",           ""),
	HAS_B_PROB_UP  (PrefixList.WURCS, "has_backbone_prob_upper",     ""),
	HAS_B_PROB_LOW (PrefixList.WURCS, "has_backbone_prob_lower",     ""),
	HAS_M_PROB_UP  (PrefixList.WURCS, "has_modification_prob_upper", ""),
	HAS_M_PROB_LOW (PrefixList.WURCS, "has_modification_prob_lower", ""),
	SUBSUMES       (PrefixList.WURCS, "subsumes",              "");
	*/

	public static final String A_ANOBASE_Prop	= getURI("anobase");
	public static final String A_BASETYPE_Prop	= getURI("basetype");
	public static final String A_ANOBASE		= getURI("anobase");
	public static final String A_BASETYPE		= getURI("basetype");
	/*
	A_ANOBASE      (PrefixList.WURCS, "anobase",               ""),
	A_BASETYPE     (PrefixList.WURCS, "basetype",              ""),
	 */

	// Object
	public static final String A_URES	= getURI("uniqueRES");
	public static final String A_RES	= getURI("RES");
	public static final String A_LIN	= getURI("LIN");
	public static final String A_GLIPS	= getURI("GLIPS");
	public static final String A_GLIP	= getURI("GLIP");
	public static final String A_MS		= getURI("monosaccharide");
	public static final String A_MOD	= getURI("MOD");
	public static final String A_LIPS	= getURI("LIPS");
	public static final String A_LIP	= getURI("LIP");
	/*
	A_URES         (PrefixList.WURCS, "uniqueRES",             ""),
	A_RES          (PrefixList.WURCS, "RES",                   ""),
	A_LIN          (PrefixList.WURCS, "LIN",                   ""),
	A_GLIPS        (PrefixList.WURCS, "GLIPS",                 ""),
	A_GLIP         (PrefixList.WURCS, "GLIP",                  ""),
	A_MS           (PrefixList.WURCS, "monosaccharide",        ""),
	A_MOD          (PrefixList.WURCS, "MOD",                   ""),
	A_LIPS         (PrefixList.WURCS, "LIPS",                  ""),
	A_LIP          (PrefixList.WURCS, "LIP",                   ""),
	*/
}
