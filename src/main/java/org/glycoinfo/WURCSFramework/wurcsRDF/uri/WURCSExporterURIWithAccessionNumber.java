package org.glycoinfo.WURCSFramework.wurcsRDF.uri;

import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;

public class WURCSExporterURIWithAccessionNumber extends WURCSExporterURI {

	private String m_strAccessionNumber = "";
	private String m_strBaseURIwithAccessionNumber;

	public WURCSExporterURIWithAccessionNumber(String a_strAccessionNumber) {
		this.m_strAccessionNumber = a_strAccessionNumber;
		this.m_strBaseURIwithAccessionNumber = this.m_strGlycoInfoGlycanURI+"/"+a_strAccessionNumber+"/wurcs/2.0";
	}

	/**
	 * Get saccharide URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]
	 */
	public String getSaccharideURI(){
		return this.brackets( this.m_strGlycoInfoGlycanURI+"/"+this.m_strAccessionNumber );
	}

	/**
	 * Get glycosequence URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0
	 */
	public String getGlycosequenceURI(){
		return this.brackets( this.m_strBaseURIwithAccessionNumber );
	}

	/**
	 * Get uniqueRES URI from UniqueRES
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/uniqueRES/[uniqueRESID]
	 */
	public String getUniqueRESURI(UniqueRES a_oURES){
		return this.getUniqueRESURI(a_oURES.getUniqueRESID());
	}

	/**
	 * Get uniqueRES URI from UniqueRES ID
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/uniqueRES/[uniqueRESID]
	 */
	public String getUniqueRESURI(int a_iURESID){
		return this.concatenateURIWithAccessionNumber( "uniqueRES", ""+a_iURESID );
	}


	protected String concatenateURIWithAccessionNumber(String a_oClass, String a_strObject) {
		return this.brackets( this.m_strBaseURIwithAccessionNumber+"/"+a_oClass+"/"+WURCSStringUtils.getURLString( a_strObject ) );
	}


}
