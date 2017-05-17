package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

public class SUBST extends SEQMOD {

	private LinkedList<Integer> m_iPosition = new LinkedList<Integer>();

	public SUBST(int a_iID, String a_strMAP) {
		super(a_iID, a_strMAP);
	}

	public void addPosition(Integer a_iPos) {
		this.m_iPosition.add(a_iPos);
	}

	public LinkedList<Integer> getPositions() {
		return this.m_iPosition;
	}

}
