package org.glycoinfo.WURCSFramework.wurcs.sequence;

import java.util.LinkedList;

/**
 * Class of GLIN for WURCS sequence
 * @author MasaakiMatsubara
 *
 */
public class GLIN {

	private String m_strGLINString;
	private String m_strMAP = "";
	private LinkedList<MS> m_aDonorMS    = new LinkedList<MS>();
	private LinkedList<MS> m_aAcceptorMS = new LinkedList<MS>();
	private LinkedList<Integer> m_aDonorPos    = new LinkedList<Integer>();
	private LinkedList<Integer> m_aAcceptorPos = new LinkedList<Integer>();
	private int m_nRepeatMin = 0;
	private int m_nRepeatMax = 0;

	public GLIN(String a_strMAP, String a_strGLINString) {
		this.m_strMAP        = a_strMAP;
		this.m_strGLINString = a_strGLINString;
	}

	public String getMAP() {
		return this.m_strMAP;
	}

	public String getGLINString() {
		return this.m_strGLINString;
	}

	public void addDonorMS(MS a_oDMS) {
		this.m_aDonorMS.addLast(a_oDMS);
	}

	public LinkedList<MS> getDonorMSs() {
		return this.m_aDonorMS;
	}

	public void addAcceptorMS(MS a_oAMS) {
		this.m_aAcceptorMS.addLast(a_oAMS);
	}

	public LinkedList<MS> getAcceptorMSs() {
		return this.m_aAcceptorMS;
	}

	public void addDonorPosition(Integer a_iDPos) {
		this.m_aDonorPos.add(a_iDPos);
	}

	public LinkedList<Integer> getDonorPositions() {
		return this.m_aDonorPos;
	}

	public void addAcceptorPosition(Integer a_iAPos) {
		this.m_aAcceptorPos.add(a_iAPos);
	}

	public LinkedList<Integer> getAcceptorPositions() {
		return this.m_aAcceptorPos;
	}

	public boolean isRepeat() {
		return (this.m_nRepeatMin != 0);
	}

	public void setRepeatCountMin(int a_nRepeatMin ) {
		this.m_nRepeatMin = a_nRepeatMin;
	}

	public int getRepeatCountMin() {
		return this.m_nRepeatMin;
	}

	public void setRepeatCountMax(int a_nRepeatMax ) {
		this.m_nRepeatMax = a_nRepeatMax;
	}

	public int getRepeatCountMax() {
		return this.m_nRepeatMax;
	}
}
