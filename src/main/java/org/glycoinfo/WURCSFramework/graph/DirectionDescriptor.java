package org.glycoinfo.WURCSFramework.graph;

public enum DirectionDescriptor {

	N('n', 0), // No chiral or no geometrical isomerism for terminal carbon
	U('u', 1), // First (uno or up)
	D('d', 2), // Second (dos or down)
	T('t', 3), // Third (tres) for terminal chiral carbon
	E('e', 4), // Trans (entgegen)
	Z('z', 5), // Cis (zusammen)
	X('x', 6), // Unknown chiral or unknown geometrical isomerism
	_(' ', 7); // Compressed

	private char m_cName;
	private int  m_iScore;

	private DirectionDescriptor(char a_cDirection, int a_iScore) {
		this.m_cName = a_cDirection;
		this.m_iScore = a_iScore;
	}

	public char getName() {
		return this.m_cName;
	}

	public int getScore() {
		return this.m_iScore;
	}

	public static DirectionDescriptor forChar(char a_cDirection) {
		for ( DirectionDescriptor DD : DirectionDescriptor.values() ) {
			if ( DD.m_cName == a_cDirection ) return DD;
		}
		return null;
	}
}
