package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class MODOld extends MODAbstract{

	private LinkedList<LIP> m_aLIPs = new LinkedList<LIP>();
	private LinkedList<FuzzyLIPOld> m_aFuzzyLIPs = new LinkedList<FuzzyLIPOld>();

	public MODOld(String a_strMAP) {
		super(a_strMAP);
	}

	public void addLIP(LIP a_oLIP) {
		this.m_aLIPs.addLast(a_oLIP);
	}

	public void addFuzzyLIP(FuzzyLIPOld a_oLIP) {
		this.m_aFuzzyLIPs.addLast(a_oLIP);
	}

	public LinkedList<LIP> getLIPs() {
		return this.m_aLIPs;
	}

	public LinkedList<FuzzyLIPOld> getFuzzyLIPs() {
		return this.m_aFuzzyLIPs;
	}
	//public String getMODCount() {
	//	return this.m_aLIPs.size();
	//}
}
