package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class FuzzyLIP {

	private LinkedList<LIP> m_aLIPs = new LinkedList<LIP>();

	public FuzzyLIP(LinkedList<LIP> a_aLIPs) {
		this.m_aLIPs = a_aLIPs;
	}

	public LinkedList<LIP> getLIPs() {
		return this.m_aLIPs;
	}
}
