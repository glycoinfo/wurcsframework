package org.glycoinfo.WURCSFramework.util.array.mass;

/**
 * Enum for atomic propaties
 * @author MasaakiMatsubara
 *
 */
public enum AtomicPropaties {

	/**
	 * Atomic weight of principal isotope is retrieved from the following site.
	 * http://ours.be/sci/aw.php
	 * 
	 */
	H ( "H" ,  1, 1.00782503207 , 1 ),
	D ( "D" ,  1, 2.014101778   , 1 ),
	He( "He",  2, 4.00260325415 , 0 ),
	Li( "Li",  3, 7.01600455    , 1 ),
	Be( "Be",  4, 9.0121822     , 2 ),
	B ( "B" ,  5, 11.0093054    , 3 ),
	C ( "C" ,  6, 12            , 4 ),
	N ( "N" ,  7, 14.0030740048 , 3 ),
	O ( "O" ,  8, 15.99491461956, 2 ),
	F ( "F" ,  9, 18.99840322   , 1 ),
	Ne( "Ne", 10, 19.9924401754 , 0 ),

	Na( "Na", 11, 22.9897692809 , 1 ),
	Mg( "Mg", 12, 23.9850417    , 2 ),
	Al( "Al", 13, 26.98153863   , 3 ),
	Si( "Si", 14, 27.9769265325 , 4 ),
	P ( "P" , 15, 30.97376163   , 5 ), // For phosphate (not 3 valence)
	S ( "S" , 16, 31.972071     , 6 ), // For sulfate (not 2 or 4 valence)
	Cl( "Cl", 17, 34.96885268   , 1 ),
	Ar( "Ar", 18, 39.9623831225 , 0 ),

	K ( "K" , 19, 38.96370668   , 1 ),
	Ca( "Ca", 20, 39.96259098   , 1 ),
	Sc( "Sc", 21, 44.9559119    , 1 ),
	Ti( "Ti", 22, 47.9479463    , 1 ),
	V ( "V" , 23, 50.9439595    , 1 ),
	Cr( "Cr", 24, 55.9349375    , 1 ),
	Mn( "Mn", 25, 54.9380451    , 1 ),
	Fe( "Fe", 26, 55.9349375    , 1 ),
	Co( "Co", 27, 58.933195     , 1 ),
	Ni( "Ni", 28, 57.9353429    , 1 ),
	Cu( "Cu", 29, 62.9295975    , 1 ),
	Zn( "Zn", 30, 63.9291422    , 1 ),
	Ga( "Ga", 31, 68.9255736    , 1 ),
	Ge( "Ge", 32, 73.9211778    , 1 ),
	As( "As", 33, 74.9215965    , 1 ),
	Se( "Se", 34, 79.9165213    , 1 ),
	Br( "Br", 35, 78.9183371    , 1 ),
	Kr( "Kr", 36, 83.911507     , 1 ),
	;

	private String m_strSymbol;
	private int    m_iAtomNum;
	private double m_dMass;
	private int    m_iValence;

	private AtomicPropaties( String a_strSymbol, int a_iAtomNum, double a_dMass, int a_iValence ) {
		this.m_strSymbol = a_strSymbol;
		this.m_iAtomNum = a_iAtomNum;
		this.m_dMass = a_dMass;
		this.m_iValence = a_iValence;
	}

	public static AtomicPropaties forSymbol(String a_strSymbol) {
		for ( AtomicPropaties atom : AtomicPropaties.values() ) {
			if ( a_strSymbol.equals( atom.m_strSymbol ) ) return atom;
		}
		return null;
	}

	public String getSymbol() {
		return this.m_strSymbol;
	}

	public int getAtomicNumber() {
		return this.m_iAtomNum;
	}

	public double getMass() {
		return this.m_dMass;
	}

	public int getValence() {
		return this.m_iValence;
	}
}
