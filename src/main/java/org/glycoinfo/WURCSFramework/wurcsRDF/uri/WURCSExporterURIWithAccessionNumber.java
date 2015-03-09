package org.glycoinfo.WURCSFramework.wurcsRDF.uri;

import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;

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

	/**
	 * Get RES URI from RES
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/RES/[RESindex]
	 */
	public String getRESURI(RES a_oRES){
		return this.getRESURI(a_oRES.getRESIndex());
	}

	/**
	 * Get RES URI from RES index
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/RES/[RESindex]
	 */
	public String getRESURI(String a_strRESIndex){
		return this.concatenateURIWithAccessionNumber( "RES", a_strRESIndex );
	}

	/**
	 * Get LIN URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/LIN/[LIN]
	 */
	public String getLINURI(LIN a_oLIN){
		return this.concatenateURIWithAccessionNumber( "LIN", this.m_oExport.getLINString(a_oLIN) );
	}

	/**
	 * Get GLIPS URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/GLIPS/[GLIPS]
	 */
	public String getGLIPSURI(GLIPs a_oGLIPs){
		return this.concatenateURIWithAccessionNumber( "GLIPS", this.m_oExport.getGLIPsString(a_oGLIPs) );
	}

	/**
	 * Get GLIP URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/GLIP/[GLIP]
	 */
	public String getGLIPURI(GLIP a_oGLIP){
		return this.concatenateURIWithAccessionNumber( "GLIP", this.m_oExport.getGLIPString(a_oGLIP) );
	}

	protected String concatenateURIWithAccessionNumber(String a_oClass, String a_strObject) {
		return this.brackets( this.m_strBaseURIwithAccessionNumber+"/"+a_oClass+"/"+WURCSStringUtils.getURLString( a_strObject ) );
	}


}
