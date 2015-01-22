package org.glycoinfo.WURCSFramework.wurcs;

public class RES {

	private int  m_iUniqueRESID;
	private String m_strRESIndex;


	public RES(int  a_iUniqueRESID, String a_strRESIndex) {
		this.m_iUniqueRESID = a_iUniqueRESID;
		this.m_strRESIndex = a_strRESIndex;
	}

	public int getUniqueRESID() {
		return this.m_iUniqueRESID;
	}

	public String getRESIndex() {
		return this.m_strRESIndex;
	}

}
