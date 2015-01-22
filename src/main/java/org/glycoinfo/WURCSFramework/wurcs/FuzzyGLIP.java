package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class FuzzyGLIP {

	private LinkedList<GLIP> m_aGLIPs;
	private String m_strAlternative;

	public FuzzyGLIP(LinkedList<GLIP> a_aGLIPs, String a_strAlternative) {
		this.m_aGLIPs = a_aGLIPs;
		this.m_strAlternative = a_strAlternative;
	}

	public LinkedList<GLIP> getFuzzyGLIPs() {
		return this.m_aGLIPs;
	}

	public String getAlternativeType() {
		return this.m_strAlternative;
	}
}
