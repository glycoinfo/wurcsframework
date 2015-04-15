package org.glycoinfo.WURCSFramework.wurcsRDF;

public enum PredicateList {

	SAMEAS         (PrefixList.OWL, "sameAs", ""),
	A_SACCHARIDE   (PrefixList.GLYCAN, "saccharide",             ""),
	A_GSEQ         (PrefixList.GLYCAN, "glycosequence",          ""),
	HAS_GSEQ       (PrefixList.GLYCAN, "has_glycosequence",      ""),
	HAS_SEQ        (PrefixList.GLYCAN, "has_sequence",           ""),
	IN_CARB_FORMAT (PrefixList.GLYCAN, "in_carbohydrate_format", ""),
	FORMAT_WURCS   (PrefixList.GLYCAN, "carbohydrate_format_wurcs", ""),
	HAS_PRIMARY_ID (PrefixList.GLYTOUCAN, "has_primary_id", ""),
	NUM_URES       (PrefixList.WURCS, "uniqueRES_count",       ""),
	NUM_RES        (PrefixList.WURCS, "RES_count",             ""),
	NUM_LIN        (PrefixList.WURCS, "LIN_count",             ""),
	A_URES         (PrefixList.WURCS, "uniqueRES",             ""),
	A_RES          (PrefixList.WURCS, "RES",                   ""),
	A_LIN          (PrefixList.WURCS, "LIN",                   ""),
	A_GLIPS        (PrefixList.WURCS, "GLIPS",                 ""),
	A_GLIP         (PrefixList.WURCS, "GLIP",                  ""),
	A_MS           (PrefixList.WURCS, "monosaccharide",        ""),
	A_MOD          (PrefixList.WURCS, "MOD",                   ""),
	A_BASETYPE     (PrefixList.WURCS, "basetype",              ""),
	A_ANOBASE      (PrefixList.WURCS, "anobase",               ""),
	A_LIPS         (PrefixList.WURCS, "LIPS",                  ""),
	A_LIP          (PrefixList.WURCS, "LIP",                   ""),
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

	private PrefixList m_enumPrefix;
	private String m_strPredicate;
	private String m_strAbbr;

	private PredicateList( PrefixList a_enumPrefixList , String a_strPredicate, String a_strAbbr ) {
		this.m_enumPrefix   = a_enumPrefixList;
		this.m_strPredicate = a_strPredicate;
		this.m_strAbbr      = a_strAbbr;
	}

	public PrefixList getPrefix() {
		return this.m_enumPrefix;
	}

	public String getPredicate() {
		return this.m_strPredicate;
	}

	public String getPredicateWithPrefix() {
		return this.m_enumPrefix.getPrefix() +":"+this.m_strPredicate;
	}

	public String getAbbr() {
		return this.m_strAbbr;
	}

	public String getAPredicate() {
		return "a\t"+this.getPredicateWithPrefix();
	}

	public String getTriple(String a_strObject) {
		return this.getPredicateWithPrefix()+"\t"+a_strObject;
	}

	public String getTriple(String id, String a_strObject) {
		return this.getPredicateWithPrefix() + id + "\t"+a_strObject;
	}

	public String getTripleLiteral(String a_strObject) {
		return this.getPredicateWithPrefix()+"\t\""+a_strObject+"\"^^xsd:string";
	}

	public String getTripleLiteral(int a_iObject) {
		return this.getPredicateWithPrefix()+"\t\""+a_iObject+"\"^^xsd:integer";
	}

	public String getTripleLiteral(char a_cObject) {
		return this.getPredicateWithPrefix()+"\t\""+a_cObject+"\"^^xsd:string";
	}

	public String getTripleLiteral(double a_dObject) {
		return this.getPredicateWithPrefix()+"\t\""+a_dObject+"\"^^xsd:double";
	}

	public String getTripleLiteral(boolean t_bObject) {
		return this.getPredicateWithPrefix()+"\t"+t_bObject;
	}

	// Subject, Object -> Predicate
	public static PredicateList forAbbr(String a_strAbbr) {
		for ( PredicateList pl : PredicateList.values() ) {
			if ( pl.m_strAbbr.equals(a_strAbbr) ) return pl;
		}
		return null;
	}

}
