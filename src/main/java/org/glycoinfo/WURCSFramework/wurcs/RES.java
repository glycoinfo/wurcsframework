package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class RES {

	private int  m_iUniqueRESID;
	private String m_strRESIndex;
	private LinkedList<Integer> m_aRepeatIDs = new LinkedList<Integer>();
	private LinkedList<LIN> lins = new LinkedList<LIN>();
	private String scPosition = "";
	private String primaryId;
	
	private UniqueRES uRes;

	public LinkedList<LIN> getLINs() {
		return lins;
	}

	public void setLINs(LinkedList<LIN> lins) {
		this.lins = lins;
	}

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

	public String getScPosition() {
		return scPosition;
	}

	public void setScPosition(String scPosition) {
		this.scPosition = scPosition;
	}

	public UniqueRES getuRes() {
		return uRes;
	}

	@Override
	public String toString() {
		return "RES [m_iUniqueRESID=" + m_iUniqueRESID + ", m_strRESIndex="
				+ m_strRESIndex + ", m_aRepeatIDs=" + m_aRepeatIDs + ", lins="
				+ lins + ", scPosition=" + scPosition + ", primaryId="
				+ primaryId + ", uRes=" + uRes + "]";
	}

	public void setuRes(UniqueRES uniqueRES) {
		this.uRes = uniqueRES;
	}

	public String getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	
	
}
