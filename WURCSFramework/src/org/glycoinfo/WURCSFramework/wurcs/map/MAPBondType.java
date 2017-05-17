package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Enum class for MAPBondType which indicate type of chemical bond (single, double, triple or unknown)
 * @author MasaakiMatsubara
 *
 */
public enum MAPBondType {

	SINGLE  ( '-', 1), // Single bond (default), omitted in MAP
	DOUBLE  ( '=', 2), // Double bond
	TRIPLE  ( '#', 3), // Triple bond
	AROMATIC( ' ', 4), // Aromatic bond, omitted in MAP
	UNKNOWN ( '+',-1); // Unknown bond type

	private char m_cSymbol;
	private int m_iNumber;

	private MAPBondType(char a_cSymbol, int a_iNum) {
		this.m_cSymbol = a_cSymbol;
		this.m_iNumber = a_iNum;
	}

	public char getSymbol() {
		return this.m_cSymbol;
	}

	public int getNumber(){
		return this.m_iNumber;
	}

	public static MAPBondType forSymbol(char a_cSymbol) {
		for ( MAPBondType t_enumType : MAPBondType.values() ) {
			if ( t_enumType.m_cSymbol != a_cSymbol ) continue;
			return t_enumType;
		}
		return null;
	}
}
