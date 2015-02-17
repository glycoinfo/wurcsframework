package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class RES {

	private int  m_iUniqueRESID;
	private String m_strRESIndex;
	private LinkedList<Integer> m_aRepeatIDs = new LinkedList<Integer>();


	public RES(int a_iUniqueRESID, String a_strRESIndex) {
		this.m_iUniqueRESID = a_iUniqueRESID;
		this.m_strRESIndex = a_strRESIndex;
	}

	public int getUniqueRESID() {
		return this.m_iUniqueRESID;
	}

	public String getRESIndex() {
		return this.m_strRESIndex;
	}

	public void addRepeatID(int a_iRepeatID) {
		this.m_aRepeatIDs.addLast(a_iRepeatID);
	}

	public void setRepeatIDs(LinkedList<Integer> a_aRepeatIDs) {
		this.m_aRepeatIDs.addAll(a_aRepeatIDs);
	}

	public LinkedList<Integer> getRepeatIDs() {
		return this.m_aRepeatIDs;
	}
}
