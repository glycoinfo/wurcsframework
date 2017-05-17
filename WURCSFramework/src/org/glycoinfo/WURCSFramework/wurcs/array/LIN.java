package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class LIN extends MODAbstract {

	private LinkedList<GLIPs> m_aGLIPs = new LinkedList<GLIPs>();

	private boolean m_bIsRepeatingUnit = false;
	private int m_strMinRepeatCount = 0;
	private int m_strMaxRepeatCount = 0;

	public LIN(String a_strMAP) {
		super(a_strMAP);
	}

	public void addGLIPs(GLIPs a_oGLIPs) {
		this.m_aGLIPs.addLast(a_oGLIPs);
	}

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

	public LinkedList<GLIPs> getListOfGLIPs() {
		return this.m_aGLIPs;
	}

	public boolean containRES(RES a_oRES) {
		for ( GLIPs t_oGLIPs : this.getListOfGLIPs() ) {
			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				if ( a_oRES.getRESIndex().equals( t_oGLIP.getRESIndex() ) ) return true;
			}
		}
		return false;
	}
}
