package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class MOD extends MODAbstract{

	private LinkedList<LIPs> m_aListOfLIPs = new LinkedList<LIPs>();

	public MOD(String a_strMAP) {
		super(a_strMAP);
	}

	public void addLIPs(LIPs a_oLIPs) {
		this.m_aListOfLIPs.addLast(a_oLIPs);
	}

	public LinkedList<LIPs> getListOfLIPs() {
		return this.m_aListOfLIPs;
	}

	//public String getMODCount() {
	//	return this.m_aLIPs.size();
	//}
}
