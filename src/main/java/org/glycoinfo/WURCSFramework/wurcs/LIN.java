package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class LIN extends MODAbstract {

	private LinkedList<GLIPs> m_aGLIPs = new LinkedList<GLIPs>();

	private boolean m_bIsRepeatingUnit = false;
	private int m_strMinRepeatCount = 0;
	private int m_strMaxRepeatCount = 0;
	private String scPosition = null;
	private String uri = null;
	private RES parentRES, childRES = null;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		if (uri.equals(""))
			this.uri = parentRES.getuRes().getSkeletonCode() + "" + childRES.getuRes().getSkeletonCode();
		else
			this.uri = uri;
	}

	public String getScPosition() {
		return scPosition;
	}

	public void setScPosition(String scPosition) {
		this.scPosition = scPosition;
	}

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
	
	public GLIP getFirstGLIP() {
		return this.m_aGLIPs.getFirst().getGLIPs().getFirst();
	}
	
	public GLIP getLastGLIP() {
		return this.m_aGLIPs.getLast().getGLIPs().getLast();
	}

	public boolean containRES(RES a_oRES) {
		for ( GLIPs t_oGLIPs : this.getListOfGLIPs() ) {
			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				if ( a_oRES.getRESIndex().equals( t_oGLIP.getRESIndex() ) ) return true;
			}
		}
		return false;
	}
	
	public boolean isPreviousLin(RES a_oRES) {
	return getLastGLIP().getRESIndex().equals(a_oRES.getRESIndex());
	}
	
	public boolean isNextLin(RES a_oRES) {
	return getFirstGLIP().getRESIndex().equals(a_oRES.getRESIndex());
	}
	
	public void setURI(RES a, RES b) {
		
	}

	public RES getParentRES() {
		return parentRES;
	}

	public void setParentRES(RES parentRES) {
		this.parentRES = parentRES;
	}

	public RES getChildRES() {
		return childRES;
	}

	public void setChildRES(RES childRES) {
		this.childRES = childRES;
	}

	@Override
	public String toString() {
		return "LIN [m_aGLIPs=" + m_aGLIPs + ", m_bIsRepeatingUnit="
				+ m_bIsRepeatingUnit + ", m_strMinRepeatCount="
				+ m_strMinRepeatCount + ", m_strMaxRepeatCount="
				+ m_strMaxRepeatCount + ", scPosition=" + scPosition + ", uri="
				+ uri + 
				", parentRES=" + (null != parentRES? parentRES.getRESIndex() : "") + 
				", childRES=" + (null != childRES? parentRES.getRESIndex() : "") 
				+ "]";
	}
}
