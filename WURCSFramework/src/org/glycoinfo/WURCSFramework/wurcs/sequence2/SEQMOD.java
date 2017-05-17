package org.glycoinfo.WURCSFramework.wurcs.sequence2;


public abstract class SEQMOD {

	private int m_iID;
	private String m_strMAP;

	public SEQMOD(int a_iID, String a_strMAP) {
		this.m_iID = a_iID;
		this.m_strMAP = a_strMAP;
	}

	public String getMAP() {
		return this.m_strMAP;
	}

	public int getID() {
		return this.m_iID;
	}
}
