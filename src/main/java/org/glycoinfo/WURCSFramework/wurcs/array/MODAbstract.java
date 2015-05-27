package org.glycoinfo.WURCSFramework.wurcs.array;

public abstract class MODAbstract {

	/** String of modification (ALIN @ ver 1.0) */
	private String m_strMAP;
//	private String m_strMOD;

	public MODAbstract( String a_strMAP ){
		this.m_strMAP = a_strMAP;
	}
/*
	public MODAbstract(String a_strMOD, String a_strMAP ){
		this.m_strMOD = a_strMOD;
		this.m_strMAP = a_strMAP;
	}
*/
	public String getMAPCode() {
		return this.m_strMAP;
	}
/*
	public String getMODString() {
		return this.m_strMOD;
	}
*/

}
