package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class LINOld extends MODAbstract {

	//private LinkedList<GLIPInterface> m_aGLIPs = new LinkedList<GLIPInterface>();
	private LinkedList<GLIP> m_aGLIPs = new LinkedList<GLIP>();
	private LinkedList<FuzzyGLIPOld> m_aFuzzyGLIPs = new LinkedList<FuzzyGLIPOld>();

	private boolean m_bIsRepeatingUnit = false;
	private int m_strMinRepeatCount = 0;
	private int m_strMaxRepeatCount = 0;
/*
	public LIN(String a_strMOD, String a_strMAP) {
		super(a_strMOD, a_strMAP);
	}
*/
	public LINOld(String a_strMAP) {
		super(a_strMAP);
	}

	public void addGLIP(GLIP a_oGLIP) {
		this.m_aGLIPs.addLast(a_oGLIP);
	}

	public void addFuzzyGLIP(FuzzyGLIPOld a_oFuzzyGLIP) {
		this.m_aFuzzyGLIPs.addLast(a_oFuzzyGLIP);
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

	public LinkedList<FuzzyGLIPOld> getFuzzyGLIPs() {
		return this.m_aFuzzyGLIPs;
	}

}
