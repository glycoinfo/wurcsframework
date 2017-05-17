package org.glycoinfo.WURCSFramework.util.array.mass;

import java.math.BigDecimal;


/**
 * Enum for atomic propaties
 * @author MasaakiMatsubara
 *
 */
public enum AtomicPropatiesOld {

	/**
	 * Atomic weight of principal isotope is retrieved from the following site.
	 * http://ours.be/sci/aw.php
	 * These are used to calculate monoisotopic mass
	 */
	H ( "H" ,  1,  "1.00782503207", 1 ),
	D ( "D" ,  1,  "2.014101778"  , 1 ),
	He( "He",  2,  "4.00260325415", 0 ),
	Li( "Li",  3,  "7.01600455"   , 1 ),
	Be( "Be",  4,  "9.0121822"    , 2 ),
	B ( "B" ,  5, "11.0093054"    , 3 ),
	C ( "C" ,  6, "12.0000000"    , 4 ),
	N ( "N" ,  7, "14.0030740048" , 3 ),
	O ( "O" ,  8, "15.99491461956", 2 ),
	F ( "F" ,  9, "18.99840322"   , 1 ),
	Ne( "Ne", 10, "19.9924401754" , 0 ),

	Na( "Na", 11, "22.9897692809" , 1 ),
	Mg( "Mg", 12, "23.9850417"    , 2 ),
	Al( "Al", 13, "26.98153863"   , 3 ),
	Si( "Si", 14, "27.9769265325" , 4 ),
	P ( "P" , 15, "30.97376163"   , 5 ), // For phosphate (not 3 valence)
	S ( "S" , 16, "31.972071"     , 6 ), // For sulfate (not 2 or 4 valence)
	Cl( "Cl", 17, "34.96885268"   , 1 ),
	Ar( "Ar", 18, "39.9623831225" , 0 ),

	K ( "K" , 19, "38.96370668"   , 1 ),
	Ca( "Ca", 20, "39.96259098"   , 1 ),
	Sc( "Sc", 21, "44.9559119"    , 1 ),
	Ti( "Ti", 22, "47.9479463"    , 1 ),
	V ( "V" , 23, "50.9439595"    , 1 ),
	Cr( "Cr", 24, "55.9349375"    , 1 ),
	Mn( "Mn", 25, "54.9380451"    , 1 ),
	Fe( "Fe", 26, "55.9349375"    , 1 ),
	Co( "Co", 27, "58.933195"     , 1 ),
	Ni( "Ni", 28, "57.9353429"    , 1 ),
	Cu( "Cu", 29, "62.9295975"    , 1 ),
	Zn( "Zn", 30, "63.9291422"    , 1 ),
	Ga( "Ga", 31, "68.9255736"    , 1 ),
	Ge( "Ge", 32, "73.9211778"    , 1 ),
	As( "As", 33, "74.9215965"    , 1 ),
	Se( "Se", 34, "79.9165213"    , 1 ),
	Br( "Br", 35, "78.9183371"    , 1 ),
	Kr( "Kr", 36, "83.911507"     , 1 ),

	Rb( "Rb", 37, "84.911789738"  , 1 ),
	Sr( "Sr", 38, "87.9056121"    , 1 ),
	Y ( "Y" , 39, "88.9058483"    , 1 ),
	Zr( "Zr", 40, "89.9047044"    , 1 ),
	Nb( "Nb", 41, "92.9063781"    , 1 ),
	Mo( "Mo", 42, "97.9054082"    , 1 ),
	Tc( "Tc", 43, "98.9054082"    , 1 ),
	Ru( "Ru", 44,"101.9046493"    , 1 ),
	Rh( "Rh", 45,"102.905504"     , 1 ),
	Pb( "Pb", 46,"105.903486"     , 1 ),
	Ag( "Ag", 47,"106.905097"     , 1 ),
	Cd( "Cd", 48,"113.9033585"    , 1 ),
	In( "In", 49,"114.903878"     , 1 ),
	Sn( "Sn", 50,"119.9021947"    , 1 ),
	Sb( "Sb", 51,"120.9038157"    , 1 ),
	Te( "Te", 52,"129.906224"     , 1 ),
	I ( "I" , 53,"126.904473"     , 1 ),
	Xe( "Xe", 54,"131.9041535"    , 1 ),
	;

	private String m_strSymbol;
	private int    m_iAtomNum;
	private BigDecimal m_dMassMonoisotopic;
	private int    m_iValence;

	private AtomicPropatiesOld( String a_strSymbol, int a_iAtomNum, String a_strMass, int a_iValence ) {
		this.m_strSymbol = a_strSymbol;
		this.m_iAtomNum = a_iAtomNum;
		this.m_dMassMonoisotopic = new BigDecimal(a_strMass);
		this.m_iValence = a_iValence;
	}

	public static AtomicPropatiesOld forSymbol(String a_strSymbol) {
		for ( AtomicPropatiesOld atom : AtomicPropatiesOld.values() ) {
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

	public BigDecimal getMass() {
		return this.m_dMassMonoisotopic;
	}

	public double getMassDouble() {
		return this.m_dMassMonoisotopic.doubleValue();
	}

	public int getValence() {
		return this.m_iValence;
	}

	public int getMassPrecision() {
		return this.m_dMassMonoisotopic.precision();
	}


}
