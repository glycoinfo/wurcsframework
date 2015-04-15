package org.glycoinfo.WURCSFramework.wurcsRDF.uri;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class WURCSExporterURI {

	protected final WURCSExporter m_oExport = new WURCSExporter();
	protected final String m_strGlycoInfoGlycanURI = "http://rdf.glycoinfo.org/glycan";
	private String m_strBaseURI = this.m_strGlycoInfoGlycanURI+"/wurcs/2.0";

	/**
	 * Get GlycoInfo glycan URI
	 * @return http://rdf.glycoinfo.org/glycan/
	 */
	public String getGlycoInfoGlycanURI(){
		return this.m_strGlycoInfoGlycanURI;
	}

	/**
	 * Get WURCS URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/[WURCS]
	 */
	public String getWURCSURI(WURCSArray a_oWURCS) {
		String t_strWURCS = WURCSStringUtils.getURLString( this.m_oExport.getWURCSString(a_oWURCS) );
		return this.brackets( this.m_strBaseURI+"/"+t_strWURCS );
	}

	/**
	 * Get monosaccharide URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/[UniqueRES]
	 */
	public String getMonosaccharideURI(UniqueRES a_oMonosaccharide) {
		return this.concatenateURI( "monosaccharide", this.m_oExport.getUniqueRESString(a_oMonosaccharide) );
	}

	/**
	 * Get MOD URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/[MOD]
	 */
	public String getMODURI(MOD a_oMOD) {
		return this.concatenateURI( "MOD", this.m_oExport.getMODString(a_oMOD) );
	}

	/**
	 * Get basetype URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/basetype/[basetype]
	 */
	public String getBasetypeURI(UniqueRES a_oBasetype) {
		return this.concatenateURI( "basetype", this.m_oExport.getUniqueRESString(a_oBasetype) );
	}

	/**
	 * Get anobase URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/anobase/[anobase]
	 */
	public String getAnobaseURI(UniqueRES a_oAnobase) {
		return this.concatenateURI( "anobase", this.m_oExport.getUniqueRESString(a_oAnobase) );
	}

	/**
	 * Get SkeletonCode URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/[SkeletonCode]
	 */
	public String getSkeletonCodeURI(String a_strSkeletonCode) {
		return this.concatenateURI( "SkeletonCode", a_strSkeletonCode );
	}

	/**
	 * Get LIPs URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIPS/[LIPS]
	 */
	public String getLIPSURI(LIPs a_oLIPs) {
		return this.concatenateURI( "LIPS", this.m_oExport.getLIPsString(a_oLIPs) );
	}

	/**
	 * Get LIP URI
	 * @return http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/[LIP]
	 */
	public String getLIPURI(LIP a_oLIP) {
		return this.concatenateURI( "LIP", this.m_oExport.getLIPString(a_oLIP) );
	}

	/**
	 * Get RES URI from RES
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/RES/[RESindex]
	 */
	public String getRESURI(RES a_oRES){
		return this.getRESURI(a_oRES.getRESIndex());
	}

	public String getRESURI(UniqueRES a_uRES){
		return this.getRESURI(this.m_oExport.getUniqueRESString(a_uRES));
	}
	
	public String getRESURI(UniqueRES a_uRES, String a_strRESIndex){
		return this.getRESURI(this.m_oExport.getUniqueRESString(a_uRES) + a_strRESIndex);
	}

	/**
	 * Get RES URI from RES index
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/RES/[RESindex]
	 */
	public String getRESURI(String a_strRESIndex){
		return this.concatenateURI( "RES", a_strRESIndex );
	}
	
	public String getRESURI(UniqueRES ures, int backbonePosition) {
		return this.getRESURI(this.m_oExport.getUniqueRESString(ures) + backbonePosition);
	}

	/**
	 * Get LIN URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/LIN/[LIN]
	 */
	public String getLINURI(LIN a_oLIN){
		return this.concatenateURI( "LIN", this.m_oExport.getLINString(a_oLIN) );
	}

	/**
	 * Get GLIPS URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/GLIPS/[GLIPS]
	 */
	public String getGLIPSURI(GLIPs a_oGLIPs){
		return this.concatenateURI( "GLIPS", this.m_oExport.getGLIPsString(a_oGLIPs) );
	}

	/**
	 * Get GLIP URI
	 * @return http://rdf.glycoinfo.org/glycan/[AccessionNumber]/wurcs/2.0/GLIP/[GLIP]
	 */
	public String getGLIPURI(GLIP a_oGLIP){
		return this.concatenateURI( "GLIP", this.m_oExport.getGLIPString(a_oGLIP) );
	}

	protected String concatenateURI(String a_oClass, String a_strObject) {
		return this.brackets( this.m_strBaseURI+"/"+a_oClass+"/"+WURCSStringUtils.getURLString( a_strObject ) );
	}

	protected String brackets(String a_strURI) {
		return "<"+a_strURI+">";
	}
	

	public String getLINURI(LinkedList<UniqueRES> t_aRESs) {
		String RESid = "";
		for (UniqueRES res : t_aRESs) {
			if (!RESid.isEmpty())
				RESid += "_";
			RESid += this.m_oExport.getUniqueRESString(res) ;
		}
		return this.concatenateURI("LIN", RESid);
	}

	public String getLINURI(LinkedList<UniqueRES> t_aRESs, int scpos) {
		String RESid = "";
		for (UniqueRES res : t_aRESs) {
			if (!RESid.isEmpty())
				RESid += "_";
			RESid += this.m_oExport.getUniqueRESString(res) ;
		}
		return this.concatenateURI("LIN", RESid + "%" + scpos);
	}

	
	public String getGLIPSURI(LinkedList<UniqueRES> t_aRESs, GLIPs t_oGLIPs) {
		String RESid = "";
		for (UniqueRES res : t_aRESs) {
			if (!RESid.isEmpty())
				RESid += "_";
			RESid += this.m_oExport.getUniqueRESString(res) ;
		}
		// this should only the position.
		return this.concatenateURI("GLIPS", RESid + "_" + this.m_oExport.getGLIPsString(t_oGLIPs) );
	}
	
	public String getGLIPSURI(LinkedList<UniqueRES> t_aRESs) {
		String RESid = "";
		for (UniqueRES res : t_aRESs) {
			if (!RESid.isEmpty())
				RESid += "_";
			RESid += this.m_oExport.getUniqueRESString(res) ;
		}
		return this.concatenateURI("GLIPS", RESid );
	}
}
