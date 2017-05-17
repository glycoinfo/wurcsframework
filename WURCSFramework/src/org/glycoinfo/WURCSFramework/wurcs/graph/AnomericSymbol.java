package org.glycoinfo.WURCSFramework.wurcs.graph;

public enum AnomericSymbol {

	ALPHA  ('a', 0), // alpha anomer
	BETA   ('b', 1), // beta anomer
	UP     ('u', 2), // up,   absolute representation
	DOWN   ('d', 3), // down, absolute representation
	UNKNOWN('x', 4); // anomer uknown

	private char m_cName;
	private int  m_iScore;

	private AnomericSymbol(char a_cSymbol, int a_iScore) {
		this.m_cName = a_cSymbol;
		this.m_iScore = a_iScore;
	}

	public char getName() {
		return this.m_cName;
	}

	public int getScore() {
		return this.m_iScore;
	}

	public static AnomericSymbol forChar(char a_cSymbol) {
		for ( AnomericSymbol t_enumAnom : AnomericSymbol.values() ) {
			if ( t_enumAnom.m_cName == a_cSymbol ) return t_enumAnom;
		}
		return null;
	}

}
