package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;

public class HierarchicalDigraphNode {

	/** Connection to this graph */
	private MAPConnection m_oConnection;
	/** Average of atomic number(s) (for conjucate system) */
	private double m_dAverageAtomicNumber;
	/** Chidren of this graph */
	private LinkedList<HierarchicalDigraphNode> m_aChildren = new LinkedList<HierarchicalDigraphNode>();

	public HierarchicalDigraphNode(MAPConnection a_oConn, double a_dAveNum) {
		this.m_oConnection = a_oConn;
		this.m_dAverageAtomicNumber = a_dAveNum;
	}

	public MAPConnection getConnection() {
		return this.m_oConnection;
	}

	public double getAverageAtomicNumber() {
		return this.m_dAverageAtomicNumber;
	}

	public void addChild(HierarchicalDigraphNode a_oChild) {
		this.m_aChildren.addLast(a_oChild);
	}

	public LinkedList<HierarchicalDigraphNode> getChildren() {
		return this.m_aChildren;
	}
}
