package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

/**
 * Abstract class for creation SPARQL query
 * @author MasaakiMatsubara
 *
 */
public abstract class SPARQLQueryGenerator {

	private LinkedList<String> m_aTergetGraphURI = new LinkedList<String>();

	public void addTergetGraphURI(String a_strTergetGraphURI) {
		if ( this.m_aTergetGraphURI.contains(a_strTergetGraphURI) ) return;
		this.m_aTergetGraphURI.addLast(a_strTergetGraphURI);
	}

	public LinkedList<String> getTergetGraphURIs() {
		return this.m_aTergetGraphURI;
	}

	protected String getSELECTSection(LinkedList<String> a_aVar, boolean a_bDistinct) {
		String t_strSelect = "SELECT ";
		if ( a_bDistinct ) t_strSelect += "DISTINCT ";
		for ( String t_strVar : a_aVar ) {
			t_strSelect += t_strVar+" ";
		}
		t_strSelect += "\n";
		return t_strSelect;
	}

	protected String getCONSTRUCTSection(String a_strMainQuery) {
		String t_strConstruct = "CONSTRUCT {\n";
		t_strConstruct += a_strMainQuery+"\n";
		t_strConstruct += "}\n";
		return t_strConstruct;
	}

	protected String getFROMSection() {
		String t_strFrom = "";
		for ( String t_strGraph : this.m_aTergetGraphURI ) {
			t_strFrom += "FROM "+t_strGraph+"\n";
		}
		return t_strFrom;
	}

	protected String getFROMNAMEDSection(LinkedList<String> a_aTergetGraphURI) {
		String t_strFrom = "";
		for ( String t_strGraph : a_aTergetGraphURI ) {
			t_strFrom += "FROM NAMED "+t_strGraph+"\n";
		}
		return t_strFrom;
	}

	protected String getWHERESection(String a_strMainQuery) {
		String t_strWHERE = "WHERE {\n";
		t_strWHERE += a_strMainQuery;
		t_strWHERE += "}\n";
		return t_strWHERE;
	}

	protected String getORDERBY(String a_strVar) {
		String t_strOrderBy = "ORDER BY ";
		t_strOrderBy += a_strVar+"\n";
		return t_strOrderBy;
	}

	protected String getGSPO(String a_strGraph, String a_strSubject, String a_strPredicate, String a_strObject) {
		String t_strGSPO = "  GRAPH "+a_strGraph+" {\n";
		t_strGSPO += "  "+this.getSPO(a_strSubject, a_strPredicate, a_strObject);
		t_strGSPO += "  }\n";
		return t_strGSPO;
	}

	protected String getSPO(String a_strSubject, String a_strPredicate, String a_strObject) {
		String t_strSPO = "  "+a_strSubject+" "+a_strPredicate+" "+a_strObject+" .\n";
		return t_strSPO;
	}

	abstract public String getQuery();
}
