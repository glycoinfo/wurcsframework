package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

public class BRIDGE extends SEQMOD {

	private LinkedList<Integer> m_iFirstPosition  = new LinkedList<Integer>();
	private LinkedList<Integer> m_iSecondPosition = new LinkedList<Integer>();

	public BRIDGE(int a_iID, String a_strMAP) {
		super(a_iID, a_strMAP);
	}

	public void addStartPosition(Integer a_iPos) {
		this.m_iFirstPosition.add(a_iPos);
	}

	public LinkedList<Integer> getStartPositions() {
		return this.m_iFirstPosition;
	}

	public void addEndPosition(Integer a_iPos) {
		this.m_iSecondPosition.add(a_iPos);
	}

	public LinkedList<Integer> getEndPositions() {
		return this.m_iSecondPosition;
	}
}
