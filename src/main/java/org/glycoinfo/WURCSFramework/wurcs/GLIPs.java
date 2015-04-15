package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class GLIPs {

	private LinkedList<GLIP> m_aGLIPs;
	private String m_strAlternative;

	public GLIPs(LinkedList<GLIP> a_aGLIPs) {
		this.m_aGLIPs = a_aGLIPs;
	}

	public void setAlternative(String a_strAlternative) {
		this.m_strAlternative = a_strAlternative;
	}

	public LinkedList<GLIP> getGLIPs() {
		return this.m_aGLIPs;
	}

	public String getAlternativeType() {
		return this.m_strAlternative;
	}

	public boolean isFuzzy() {
		if ( this.m_aGLIPs.get(0).getBackbonePosition() == -1 )
			return true;
		if ( this.m_aGLIPs.size() > 1 ) return true;
		return false;
	}

	@Override
	public String toString() {
		return "GLIPs [m_aGLIPs=" + m_aGLIPs + ", m_strAlternative="
				+ m_strAlternative + ", isFuzzy:>" + isFuzzy() + "]";
	}
}
