package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class MOD extends MODAbstract{

	private LinkedList<LIP> m_aLIPs = new LinkedList<LIP>();
	private LinkedList<FuzzyLIP> m_aFuzzyLIPs = new LinkedList<FuzzyLIP>();

	public MOD(String a_strMAP) {
		super(a_strMAP);
	}

	public void addLIP(LIP a_oLIP) {
		this.m_aLIPs.addLast(a_oLIP);
	}

	public void addFuzzyLIP(FuzzyLIP a_oLIP) {
		this.m_aFuzzyLIPs.addLast(a_oLIP);
	}

	public LinkedList<LIP> getLIPs() {
		return this.m_aLIPs;
	}

	public LinkedList<FuzzyLIP> getFuzzyLIPs() {
		return this.m_aFuzzyLIPs;
	}

	//public String getMODCount() {
	//	return this.m_aLIPs.size();
	//}
}
