package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class LIN extends MODAbstract {

	//private LinkedList<GLIPInterface> m_aGLIPs = new LinkedList<GLIPInterface>();
	private LinkedList<GLIP> m_aGLIPs = new LinkedList<GLIP>();
	private LinkedList<FuzzyGLIP> m_aFuzzyGLIPs = new LinkedList<FuzzyGLIP>();

	private boolean m_bIsRepeatingUnit = false;
	private int m_strMinRepeatCount = 0;
	private int m_strMaxRepeatCount = 0;
/*
	public LIN(String a_strMOD, String a_strMAP) {
		super(a_strMOD, a_strMAP);
	}
*/
	public LIN(String a_strMAP) {
		super(a_strMAP);
	}

	public void addGLIP(GLIP a_oGLIP) {
	this.m_aGLIPs.addLast(a_oGLIP);
}

	public void addFuzzyGLIP(FuzzyGLIP a_oFuzzyGLIPs) {
	this.m_aFuzzyGLIPs.addLast(a_oFuzzyGLIPs);
}

//	public void addGLIP(GLIPInterface a_oLIP) {
//		this.m_aGLIPs.addLast(a_oLIP);
//	}

//	public LinkedList<GLIPInterface> getGLIPs() {
//		return this.m_aGLIPs;
//	}

	public void setRepeatingUnit(boolean isRepeat) {
		this.m_bIsRepeatingUnit = isRepeat;
	}

	public boolean isRepeatingUnit() {
		return this.m_bIsRepeatingUnit;
	}

	public void setMinRepeatCount(int min) {
		this.m_strMinRepeatCount = min;
	}

	public void setMaxRepeatCount(int max) {
		this.m_strMaxRepeatCount = max;
	}

	public int getMinRepeatCount() {
		return this.m_strMinRepeatCount;
	}

	public int getMaxRepeatCount() {
		return this.m_strMaxRepeatCount;
	}

	public LinkedList<GLIP> getGLIPs() {
		return this.m_aGLIPs;
	}

	public LinkedList<FuzzyGLIP> getFuzzyGLIPs() {
		return this.m_aFuzzyGLIPs;
	}
}
