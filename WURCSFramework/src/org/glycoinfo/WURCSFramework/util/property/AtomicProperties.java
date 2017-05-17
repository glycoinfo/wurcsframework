package org.glycoinfo.WURCSFramework.util.property;

import java.math.BigDecimal;

/**
 * Enum for atomic propaties for mass calculations
 * @author MasaakiMatsubara
 *
 */
public enum AtomicProperties {

	/**
	 * Atomic weight of principal isotope is retrieved from the following site.
	 * http://www.ciaaw.org/atomic-masses.htm
	 * CIAAW: M. Wang et al, "The Ame2012 Atomic Mass Evaluation", Chinese Physics C, 2012 (36) 1603-2014
	 *
	 * These are used to calculate monoisotopic mass
	 */
	H ( "H" , "hydrogen",      1,   1,   "1.0078250322", 1 ),
	He( "He", "helium",        2,   4,   "4.0026032541", 0 ),
	Li( "Li", "lithium",       3,   7,   "7.01600344",   1 ),
	Be( "Be", "beryllium",     4,   9,   "9.0121831",    2 ),
	B ( "B" , "boron",         5,  11,  "11.009305",     3 ),
	C ( "C" , "carbon",        6,  12,  "12.000000000",  4 ),
	N ( "N" , "nitrogen",      7,  14,  "14.003074004",  3 ),
	O ( "O" , "oxygen",        8,  16,  "15.994914620",  2 ),
	F ( "F" , "fluorine",      9,  19,  "18.998403163",  1 ),
	Ne( "Ne", "neon",         10,  20,  "19.99244018",   0 ),

	Na( "Na", "sodium",       11,  23,  "22.98976928",   1 ),
	Mg( "Mg", "magnesium",    12,  24,  "23.98504170",   2 ),
	Al( "Al", "aluminium",    13,  27,  "26.9815385",    3 ),
	Si( "Si", "silicon",      14,  28,  "27.976926535",  4 ),
	P ( "P" , "phosphorous",  15,  31,  "30.973761998",  5 ),
	S ( "S" , "sulfur",       16,  32,  "31.972071174",  6 ),
	Cl( "Cl", "chlorine",     17,  35,  "34.9688527",    7 ),
	Ar( "Ar", "argon",        18,  40,  "39.96238312",   0 ),

	K ( "K" , "potassium",    19,  39,  "38.96370649",   1 ),
	Ca( "Ca", "calcium",      20,  40,  "39.9625909",    1 ),
	Sc( "Sc", "scandium",     21,  45,  "44.955908",     1 ),
	Ti( "Ti", "titanium",     22,  48,  "47.947942",     1 ),
	V ( "V" , "vanadium",     23,  51,  "50.943957",     1 ),
	Cr( "Cr", "chromium",     24,  52,  "51.940506",     1 ),
	Mn( "Mn", "manganese",    25,  55,  "54.938044",     1 ),
	Fe( "Fe", "iron",         26,  56,  "55.934936",     1 ),
	Co( "Co", "cobalt",       27,  59,  "58.933194",     1 ),
	Ni( "Ni", "nickel",       28,  58,  "57.935342",     1 ),
	Cu( "Cu", "copper",       29,  63,  "62.929598",     1 ),
	Zn( "Zn", "zinc",         30,  64,  "63.929142",     1 ),
	Ga( "Ga", "gallium",      31,  69,  "68.925574",     1 ),
	Ge( "Ge", "germanium",    32,  74,  "73.92117776",   1 ),
	As( "As", "arsenic",      33,  75,  "74.921595",     1 ),
	Se( "Se", "selenium",     34,  80,  "79.916522",     1 ),
	Br( "Br", "bromine",      35,  79,  "78.918338",     1 ),
	Kr( "Kr", "krypton",      36,  84,  "83.91149773",   1 ),

	Rb( "Rb", "rubidium",     37,  85,  "84.91178974",   1 ),
	Sr( "Sr", "strontium",    38,  88,  "87.905613",     1 ),
	Y ( "Y" , "yttrium",      39,  89,  "88.90584",      1 ),
	Zr( "Zr", "zirconium",    40,  90,  "89.90470",      1 ),
	Nb( "Nb", "niobium",      41,  93,  "92.90637",      1 ),
	Mo( "Mo", "molybdenum",   42,  98,  "97.905405",     1 ),
	Tc( "Tc", "technetium",   43,  98,  "97.90721",      1 ),
	Ru( "Ru", "ruthenium",    44, 102, "101.904344",     1 ),
	Rh( "Rh", "rhodium",      45, 103, "102.90550",      1 ),
	Pd( "Pd", "palladium",    46, 106, "105.903480",     1 ),
	Ag( "Ag", "silver",       47, 107, "106.90509",      1 ),
	Cd( "Cd", "cadmium",      48, 114, "113.903365",     1 ),
	In( "In", "indium",       49, 115, "114.90387878",   1 ),
	Sn( "Sn", "tin",          50, 120, "119.902202",     1 ),
	Sb( "Sb", "antimony",     51, 121, "120.90381",      1 ),
	Te( "Te", "tellurium",    52, 130, "129.90622275",   1 ),
	I ( "I" , "iodine",       53, 127, "126.90447",      1 ),
	Xe( "Xe", "xenon",        54, 132, "131.90415509",   1 ),

	Cs( "Cs", "caesium",      55, 133, "132.90545196",   1 ),
	Ba( "Ba", "barium",       56, 138, "137.905247",     1 ),
	La( "La", "lanthanum",    57, 139, "138.90636",      1 ),
	Ce( "Ce", "cerium",       58, 140, "139.90544",      1 ),
	Pr( "Pr", "praseodymium", 59, 141, "140.90766",      1 ),
	Nd( "Nd", "neodymium",    60, 142, "141.90773",      1 ),
	Pm( "Pm", "promethium",   61, 145, "144.91276",      1 ),
	Sm( "Sm", "samarium",     62, 152, "151.91974",      1 ),
	Eu( "Eu", "europium",     63, 153, "152.92124",      1 ),
	Gd( "Gd", "gadolinium",   64, 158, "157.92411",      1 ),
	Tb( "Tb", "terbium",      65, 159, "158.92535",      1 ),
	Dy( "Dy", "dysprosium",   66, 164, "163.92918",      1 ),
	Ho( "Ho", "holmium",      67, 165, "164.93033",      1 ),
	Er( "Er", "erbium",       68, 166, "165.93030",      1 ),
	Tm( "Tm", "thulium",      69, 169, "168.93422",      1 ),
	Yb( "Yb", "ytterbium",    70, 174, "173.93887",      1 ),
	Lu( "Lu", "lutetium",     71, 175, "174.94078",      1 ),
	Hf( "Hf", "hafnium",      72, 180, "179.94656",      1 ),
	Ta( "Ta", "tantalum",     73, 181, "180.94800",      1 ),
	W ( "W" , "tungsten",     74, 184, "183.950931",     1 ),
	Re( "Re", "rhenium",      75, 187, "186.95575",      1 ),
	Os( "Os", "osmium",       76, 192, "191.96148",      1 ),
	Ir( "Ir", "iridium",      77, 193, "192.96292",      1 ),
	Pt( "Pt", "platinum",     78, 195, "194.964792",     1 ),
	Au( "Au", "gold",         79, 197, "196.966569",     1 ),
	Hg( "Hg", "mercury",      80, 202, "201.970643",     1 ),
	Tl( "Tl", "thallium",     81, 205, "204.974428",     1 ),
	Pb( "Pb", "lead",         82, 208, "207.976653",     1 ),
	Bi( "Bi", "bismuth",      83, 209, "208.98040",      1 ),
	Th( "Th", "thorium",      90, 232, "232.03806",      1 ),
	Pa( "Pa", "protactinium", 91, 231, "231.03588",      1 ),
	U ( "U" , "uranium",      92, 238, "238.05079",      1 ),
	;

	private String m_strSymbol;
	private String m_strName;
	private int    m_iAtomNum;
	private int    m_iMassNum;
	private BigDecimal m_dMassMonoisotopic;
	private int    m_iValence;

	private AtomicProperties( String a_strSymbol, String a_strName, int a_iAtomNum, int a_iMassNum, String a_strMass, int a_iValence ) {
		this.m_strSymbol = a_strSymbol;
		this.m_strName = a_strName;
		this.m_iAtomNum = a_iAtomNum;
		this.m_iMassNum = a_iMassNum;
		this.m_dMassMonoisotopic = new BigDecimal(a_strMass);
		this.m_iValence = a_iValence;
	}

	public static AtomicProperties forSymbol(String a_strSymbol) {
		for ( AtomicProperties atom : AtomicProperties.values() ) {
			if ( a_strSymbol.equals( atom.m_strSymbol ) ) return atom;
		}
		return null;
	}

	public String getSymbol() {
		return this.m_strSymbol;
	}

	public String getAtomName() {
		return this.m_strName;
	}

	public int getAtomicNumber() {
		return this.m_iAtomNum;
	}

	public int getMassNumber() {
		return this.m_iMassNum;
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
