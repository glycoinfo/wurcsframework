package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

public class MSPERI extends SEQMS {

	private MSCORE m_oCORE;
	private LinkedList<Integer> m_aPossiblePositions = new LinkedList<Integer>();

	public MSPERI(String a_strMS, MSCORE a_oCORE) {
		super(a_strMS);
		this.m_oCORE = a_oCORE;
	}

	public MSCORE getCoreStructure() {
		return this.m_oCORE;
	}

	public void addPossiblePosition(Integer a_iPos) {
		this.m_aPossiblePositions.addLast(a_iPos);
	}

	public LinkedList<Integer> getPossiblePositions() {
		return this.m_aPossiblePositions;
	}

}
