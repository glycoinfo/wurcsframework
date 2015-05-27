package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class FuzzyLIPOld {

	private LinkedList<LIP> m_aLIPs = new LinkedList<LIP>();

	public FuzzyLIPOld(LinkedList<LIP> a_aLIPs) {
		this.m_aLIPs = a_aLIPs;
	}

	public LinkedList<LIP> getLIPs() {
		return this.m_aLIPs;
	}
}
