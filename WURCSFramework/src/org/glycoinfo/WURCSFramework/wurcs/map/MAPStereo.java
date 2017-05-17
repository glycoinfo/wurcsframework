package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Enum class for MAPStereo which indicate a stereochemistry of chiral atom or double bond
 * @author MasaakiMatsubara
 *
 */
public enum MAPStereo {

	RECTUS    ('R'),
	SINISTER  ('S'),
	P_RECTUS  ('r'),
	P_SINISTER('s'),
	CIS       ('Z'),
	TRANCE    ('E'),
	UNKNOWN   ('X');

	private char m_cSymbol;

	private MAPStereo(char a_cSymbol) {
		this.m_cSymbol = a_cSymbol;
	}

	public char getSymbol() {
		return this.m_cSymbol;
	}

	public static MAPStereo forSymbol(char a_cSymbol) {
		for ( MAPStereo t_oStereo : MAPStereo.values() ) {
			if ( t_oStereo.m_cSymbol != a_cSymbol ) continue;
			return t_oStereo;
		}
		return null;
	}
}
