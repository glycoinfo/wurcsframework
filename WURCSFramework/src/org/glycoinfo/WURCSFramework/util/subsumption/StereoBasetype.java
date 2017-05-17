package org.glycoinfo.WURCSFramework.util.subsumption;


/**
 * Enum class to determine stereo basetype
 * @author MasaakiMatsubara
 *
 */
public enum StereoBasetype {

	// Basetype
	// "3" and "4" in relative configuration are replace from "1" and "2" in "D" absolute configuration, respectively
	GRO("gro", "glycero", "4",    "3"),
	THR("thr", "threo",   "34",   "43"),
	ERY("ery", "erythro", "44",   "33"),
	ARA("ara", "arabino", "344",  "433"),
	RIB("rib", "ribo",    "444",  "333"),
	LYX("lyx", "lyxo",    "334",  "443"),
	XYL("xyl", "xylo",    "434",  "343"),
	ALL("all", "allo",    "4444", "3333"),
	ALT("alt", "altro",   "3444", "4333"),
	MAN("man", "manno",   "3344", "4433"),
	GLC("glc", "gluco",   "4344", "3433"),
	GUL("gul", "gulo",    "4434", "3343"),
	IDO("ido", "ido",     "3434", "4343"),
	TAL("tal", "talo",    "3334", "4443"),
	GAL("gal", "galacto", "4334", "3443");

	private String m_strThreeLett;
	private String m_strPrefix;
	private String m_strStereo;
	private String m_strOpposite;
	private boolean m_bIsOpposite = false;

	private StereoBasetype( String a_strThreeLett, String a_strPrefix, String a_strStereo, String a_strOpposite )
	{
		this.m_strThreeLett = a_strThreeLett;
		this.m_strPrefix = a_strPrefix;
		this.m_strStereo = a_strStereo;
		this.m_strOpposite = a_strOpposite;
	}

	public String getThreeLetterCode() {
		return this.m_strThreeLett;
	}

	public String getPrefix() {
		return this.m_strPrefix;
	}

	public String getStereoCode() {
		return this.m_strStereo;
	}

	public boolean isOpposite() {
		return this.m_bIsOpposite;
	}

	public static StereoBasetype forThreeLetterCode( String a_strName ) {
		for ( StereoBasetype t_objBasetype : StereoBasetype.values() )
			if ( t_objBasetype.m_strThreeLett.equalsIgnoreCase(a_strName) )
				return t_objBasetype;

		return null;
	}

	/**
	 * Get StereoBasetype from stereo code by relative configuration
	 * @param a_strCode String of stereo code by relative configuration
	 * @return StereoBasetype object corresponded to stereo code
	 */
	public static StereoBasetype forStereoCode( String a_strCode ) {
		for ( StereoBasetype t_objBasetype : StereoBasetype.values() ) {
			t_objBasetype.m_bIsOpposite = false;
			if ( t_objBasetype.m_strStereo.equals(a_strCode) ) return t_objBasetype;
			// For inverse stereo
			if ( t_objBasetype.m_strOpposite.equals(a_strCode) ) {
				// Set inverse flag
				t_objBasetype.m_bIsOpposite = true;
				return t_objBasetype;
			}
		}

		return null;
	}
}
