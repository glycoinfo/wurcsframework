package org.glycoinfo.WURCSFramework.wurcs.graph;


/**
 * Class for carbon descriptor of backbone SkeletonCode
 * @author MasaakiMatsubara
 *
 */
public enum CarbonDescriptor implements CarbonDescriptorInterface {

	// Terminal
	SZ3_METHYL_L   ( 'm', "sp3", 1,0, 1, null, null, "-H", "-H", "-H",  4 ), // -C(H)(H)(H)
	SZ3_METHYL_U   ( 'M', "sp3", 1,0, 1, null, null, "-X", "-X", "-X", 10 ), // -C(X)(X)(X)
	SZ3_HYDROXYL_L ( 'h', "sp3", 1,0, 2, null, null, "-H", "-H", "-O",  5 ), // -C(H)(H)(O)
	SZ3_HYDROXYL_U ( 'H', "sp3", 1,0, 2, null, null, "-H", "-H", "-X",  6 ), // -C(H)(H)(X)
	SZ3_ACETAL_L   ( 'c', "sp3", 1,0, 2, null, null, "-O", "-O", "-H",  6 ), // -C(O)(O)(H) ver 2.0 change (from 'b')
	SZ3_ACETAL_U   ( 'C', "sp3", 1,0, 2, null, null, "-O", "-O", "-X",  7 ), // -C(O)(O)(X) ver 2.0 new
	SZ3_DOUBLE_L   ( 'd', "sp3", 1,0, 2, null, null, "-X", "-X", "-H",  7 ), // -C(X)(X)(H) ver 2.0 change (from 'W')
	SZ3_DOUBLE_U   ( 'D', "sp3", 1,0, 2, null, null, "-X", "-X", "-X", 10 ), // -C(X)(X)(Y) ver 2.0 change (from 'L')
	SZ3_STEREO_S   ( '1', "sp3", 1,0, 3,  "S", true, "-X", "-X", "-H", 20 ), // -C(X)(Y)(H) ver 2.0 new                anomer
	SZ3_STEREO_R   ( '2', "sp3", 1,0, 3,  "R", true, "-X", "-X", "-H", 19 ), // -C(X)(Y)(H) ver 2.0 new                anomer
	SZ3_STEREO_s   ( '3', "sp3", 1,0, 3,  "s", true, "-X", "-X", "-H", 20 ), // -C(X)(Y)(H) ver 2.0 new                anomer
	SZ3_STEREO_r   ( '4', "sp3", 1,0, 3,  "r", true, "-X", "-X", "-H", 19 ), // -C(X)(Y)(H) ver 2.0 new                anomer
	SZ3_STEREO_X   ( 'x', "sp3", 1,0, 3,  "X", true, "-X", "-X", "-H", 18 ), // -C(X)(Y)(H) ver 2.0 new                anomer
	SZ3_NORING_S_L ( 's', "sp3", 1,0, 3,  "S", false,"-X", "-X", "-H", 10 ), // -C(X)(Y)(H) ver 2.0 new
	SZ3_NORING_R_L ( 'r', "sp3", 1,0, 3,  "R", false,"-X", "-X", "-H",  9 ), // -C(X)(Y)(H) ver 2.0 new
	SZ3_NORING_s_L ( 's', "sp3", 1,0, 3,  "s", false,"-X", "-X", "-H", 10 ), // -C(X)(Y)(H) ver 2.0 new
	SZ3_NORING_r_L ( 'r', "sp3", 1,0, 3,  "r", false,"-X", "-X", "-H",  9 ), // -C(X)(Y)(H) ver 2.0 new
	SZ3_NORING_X_L ( 'q', "sp3", 1,0, 3,  "X", false,"-X", "-X", "-H",  8 ), // -C(X)(Y)(H) ver 2.0 change (from 'U')
	SZ3_CHIRAL_S   ( '5', "sp3", 1,0, 3,  "S", true, "-X", "-X", "-X", 23 ), // -C(X)(Y)(X) ver 2.0 change (from '1')  anomer
	SZ3_CHIRAL_R   ( '6', "sp3", 1,0, 3,  "R", true, "-X", "-X", "-X", 22 ), // -C(X)(Y)(X) ver 2.0 change (from '2')  anomer
	SZ3_CHIRAL_s   ( '7', "sp3", 1,0, 3,  "s", true, "-X", "-X", "-X", 23 ), // -C(X)(Y)(Z) ver 2.0 change (from '3')  anomer
	SZ3_CHIRAL_r   ( '8', "sp3", 1,0, 3,  "r", true, "-X", "-X", "-X", 22 ), // -C(X)(Y)(Z) ver 2.0 change (from '4')  anomer
	SZ3_CHIRAL_X   ( 'X', "sp3", 1,0, 3,  "X", true, "-X", "-X", "-X", 21 ), // -C(X)(Y)(Z)                            anomer
	SZ3_NORING_S_U ( 'S', "sp3", 1,0, 3,  "S", false,"-X", "-X", "-X", 12 ), // -C(X)(Y)(Z) ver 2.0 new
	SZ3_NORING_R_U ( 'R', "sp3", 1,0, 3,  "R", false,"-X", "-X", "-X", 11 ), // -C(X)(Y)(Z) ver 2.0 new
	SZ3_NORING_s_U ( 'S', "sp3", 1,0, 3,  "s", false,"-X", "-X", "-X", 12 ), // -C(X)(Y)(Z) ver 2.0 new
	SZ3_NORING_r_U ( 'R', "sp3", 1,0, 3,  "r", false,"-X", "-X", "-X", 11 ), // -C(X)(Y)(Z) ver 2.0 new
	SZ3_NORING_X_U ( 'Q', "sp3", 1,0, 3,  "X", false,"-X", "-X", "-X", 10 ), // -C(X)(Y)(Z) ver 2.0 change (from 'R')
	SZ2_ALDEHYDE_L ( 'o', "sp2", 1,0, 2, null, null, "=O", "-H", null, 16 ), // -C(=O)(H)                              anomer
	SZ2_ACID_L     ( 'a', "sp2", 1,0, 2, null, null, "=O", "-O", null, 13 ), // -C(=O)(O)
	SZ2_ALDEHYDE_U ( 'O', "sp2", 1,0, 2, null, null, "=X", "-H", null, 17 ), // -C(=X)(H)                              anomer
	SZ2_ACID_U     ( 'A', "sp2", 1,0, 2, null, null, "=X", "-X", null, 14 ), // -C(=X)(Y)
	DZ2_METHYLENE_L( 'n', "sp2", 2,0, 1, null, null, "-H", "-H", null,  4 ), // =C(H)(H) ver 2.0 change (from 'v')
//	DZ2_ETHENE_L   ( 'c', "sp2", 2,0, 1, null, null, "-O", "-O", null,  6 ), // =C(O)(O) ver 2.0 reduce (merged into DZ2_METHYLENE_U)
	DZ2_METHYLENE_U( 'N', "sp2", 2,0, 1, null, null, "-X", "-X", null,  8 ), // =C(X)(X) ver 2.0 change (from 'V')
	DZ2_CISTRANS_EL( 'e', "sp2", 2,0, 2,  "E", null, "-X", "-H", null,  8 ), // =C(X)(H) ver 2.0 change (from 'G')
	DZ2_CISTRANS_ZL( 'z', "sp2", 2,0, 2,  "Z", null, "-X", "-H", null,  7 ), // =C(X)(H) ver 2.0 change (from 'I')
//	DZ2_CISTRANS_NL( 'p', "sp2", 2,0, 2,  "N", null, "-X", "-H", null,  7 ), // =C(X)(H) ver 2.0 change (from 'P' and impossible configuration)
	DZ2_CISTRANS_XL( 'f', "sp2", 2,0, 2,  "X", null, "-X", "-H", null,  7 ), // =C(X)(H) ver 2.0 change (from 'J')
	DZ2_CISTRANS_EU( 'E', "sp2", 2,0, 2,  "E", null, "-X", "-X", null, 10 ), // =C(X)(Y) ver 2.0 new
	DZ2_CISTRANS_ZU( 'Z', "sp2", 2,0, 2,  "Z", null, "-X", "-X", null,  9 ), // =C(X)(Y) ver 2.0 new
//	DZ2_CISTRANS_NU( 'P', "sp2", 2,0, 2,  "N", null, "-X", "-X", null,  8 ), // =C(X)(Y) ver 2.0 new (impossible configuration)
	DZ2_CISTRANS_XU( 'F', "sp2", 2,0, 2,  "X", null, "-X", "-X", null,  8 ), // =C(X)(Y) ver 2.0 new
	SZ1_XETHYNE    ( 'T', "sp" , 1,0, 1, null, null, "#X", null, null, 10 ), // -C(#X) ver 2.0 change (from 'Y')
	DZ1_KETENE_L   ( 'k', "sp" , 2,0, 1, null, null, "=O", null, null,  6 ), // =C(=O) ver 2.0 new
	DZ1_KETENE_U   ( 'K', "sp" , 2,0, 1, null, null, "=X", null, null,  6 ), // =C(=X) ver 2.0 change (from 'q')
	TZ1_ETHYNE_L   ( 't', "sp" , 3,0, 1, null, null, "-H", null, null,  4 ), // #C(H)
	TZ1_ETHYNE_U   ( 'T', "sp" , 3,0, 1, null, null, "-X", null, null,  6 ), // #C(X)
	// Ambiguous
	SZX_UNDEF_L    ( 'u',  "?" , 1,0, 0, null, null, "-X", "-H", null, 15 ), // -C(X)(Y)(H) or -C(=O)(H) ver 2.0 new ('x' and 'O')
	SZX_UNDEF_U    ( 'U',  "?" , 1,0, 0, null, null, "-X", "-X", null, 16 ), // -C(X)(Y)(Z) or -C(=X)(X) ver 2.0 new ('X' and 'A')
	SZX_UNDEF_ALL  ( '0',  "?" , 1,0, 0, null, null, null, null, null,  0 ), // terminal carbon type undefined at all
	// Non-terminal
	SS3_METHYNE    ( 'd', "sp3", 1,1, 1, null, null, "-H", "-H", null,  4 ), // -C(H)(H)-
	SS3_ACETAL     ( 'c', "sp3", 1,1, 1, null, null, "-O", "-O", null,  6 ), // -C(O)(O)- ver 2.0 change (from 'b')
	SS3_XMETHYNE   ( 'D', "sp3", 1,1, 1, null, null, "-X", "-X", null,  8 ), // -C(X)(X)-
	SS3_STEREO_S   ( '1', "sp3", 1,1, 2,  "S", null, "-X", "-H", null,  8 ), // -C(X)(Y)- ver 2.0 new
	SS3_STEREO_R   ( '2', "sp3", 1,1, 2,  "R", null, "-X", "-H", null,  7 ), // -C(X)(Y)- ver 2.0 new
	SS3_STEREO_s   ( '3', "sp3", 1,1, 2,  "s", null, "-X", "-H", null,  8 ), // -C(X)(Y)- ver 2.0 new
	SS3_STEREO_r   ( '4', "sp3", 1,1, 2,  "r", null, "-X", "-H", null,  7 ), // -C(X)(Y)- ver 2.0 new
	SS3_STEREO_X   ( 'x', "sp3", 1,1, 2,  "X", null, "-X", "-H", null,  6 ), // -C(X)(Y)- ver 2.0 new
	SS3_CHIRALA_S  ( '5', "sp3", 1,1, 2,  "S", true, "-X", "-X", null, 20 ), // -C(X)(Y)- ver 2.0 change (from '1')    anomer
	SS3_CHIRALA_R  ( '6', "sp3", 1,1, 2,  "R", true, "-X", "-X", null, 19 ), // -C(X)(Y)- ver 2.0 change (from '2')    anomer
	SS3_CHIRALA_s  ( '7', "sp3", 1,1, 2,  "s", true, "-X", "-X", null, 20 ), // -C(X)(Y)- ver 2.0 change (from '3')    anomer
	SS3_CHIRALA_r  ( '8', "sp3", 1,1, 2,  "r", true, "-X", "-X", null, 19 ), // -C(X)(Y)- ver 2.0 change (from '4')    anomer
	SS3_CHIRALA_X  ( 'X', "sp3", 1,1, 2,  "X", true, "-X", "-X", null, 18 ), // -C(X)(Y)-                              anomer
	SS3_CHIRAL_S   ( '5', "sp3", 1,1, 2,  "S", false,"-X", "-X", null, 10 ), // -C(X)(Y)- ver 2.0 change (from '1')
	SS3_CHIRAL_R   ( '6', "sp3", 1,1, 2,  "R", false,"-X", "-X", null,  9 ), // -C(X)(Y)- ver 2.0 change (from '2')
	SS3_CHIRAL_s   ( '7', "sp3", 1,1, 2,  "s", false,"-X", "-X", null, 10 ), // -C(X)(Y)- ver 2.0 change (from '3')
	SS3_CHIRAL_r   ( '8', "sp3", 1,1, 2,  "r", false,"-X", "-X", null,  9 ), // -C(X)(Y)- ver 2.0 change (from '4')
	SS3_CHIRAL_X   ( 'X', "sp3", 1,1, 2,  "X", false,"-X", "-X", null,  8 ), // -C(X)(Y)-
	SS2_KETONE_L   ( 'o', "sp2", 1,1, 1, null, null, "=O", null, null, 16 ), // -C(=O)- ver 2.0 change (from 'k')      anomer
	SS2_KETONE_U   ( 'O', "sp2", 1,1, 1, null, null, "=X", null, null, 17 ), // -C(=X)- ver 2.0 change (from 'K')      anomer
	DS2_CISTRANS_EL( 'e', "sp2", 2,1, 1,  "E", null, "-H", null, null,  6 ), // =C(H)-
	DS2_CISTRANS_ZL( 'z', "sp2", 2,1, 1,  "Z", null, "-H", null, null,  5 ), // =C(H)-
	DS2_CISTRANS_NL( 'n', "sp2", 2,1, 1,  "N", null, "-H", null, null,  4 ), // =C(H)-
	DS2_CISTRANS_XL( 'f', "sp2", 2,1, 1,  "X", null, "-H", null, null,  4 ), // =C(H)-
	DS2_CISTRANS_EU( 'E', "sp2", 2,1, 1,  "E", null, "-X", null, null,  8 ), // =C(X)-
	DS2_CISTRANS_ZU( 'Z', "sp2", 2,1, 1,  "Z", null, "-X", null, null,  7 ), // =C(X)-
	DS2_CISTRANS_NU( 'N', "sp2", 2,1, 1,  "N", null, "-X", null, null,  6 ), // =C(X)-
	DS2_CISTRANS_XU( 'F', "sp2", 2,1, 1,  "X", null, "-X", null, null,  6 ), // =C(X)-
	DD1_KETENE     ( 'K',  "sp", 2,2, 0, null, null, null, null, null,  4 ), // =C= ver 2.0 change (from 'q')
	TS1_ETHYNE     ( 'T',  "sp", 3,1, 0, null, null, null, null, null,  4 ), // #C- ver 2.0 change (from 'y')
	// Ambiguous
	SSX_UNDEF_U    ( 'U',  "?" , 1,1, 0, null, null, "-X", null, null, 15 ), // -C(X)(Y)- or -C(=X)- ver 2.0 new ('X' and 'O')
	SSX_UNDEF_ALL  ( '0',  "?" , 1,1, 0, null, null, null, null, null,  0 ), // non terminal carbon type undefined at all

	XXX_UNKNOWN    ( '?',  null, 0,0, 0, null, null, null, null, null,  0 ); // C???

	/** Charactor of carbon descriptor */
	private char   m_strChar;
	/** Hybrid orbital */
	private String m_strHybridOrbital;
	/** Bond type of connecting first carbon */
	private int    m_iBondTypeCarbon1;
	/** Bond type of connecting second carbon (0 at terminal) */
	private int    m_iBondTypeCarbon2;
	/** Number of unique modifications */
	private int    m_nUniqueModification;
	/** Chirality or geometrical isomerism */
	private String m_strStereo;
	/** Whether or not the carbon is foot of bridge on the ring */
	private Boolean m_bIsFootOfBridge;
	/** String of first unique modificaiton */
	private String m_strModification1;
	/** String of second unique modificaiton (or null) */
	private String m_strModification2;
	/** String of third unique modificaiton (or null) */
	private String m_strModification3;
	/** Score of carbon for comparison backbone */
	private int    m_iCarbonScore;

	/**
	 * Private constructor of CarbonDescriptor (a charactor of SkeltonCode)
	 * @param a_strChar SkeltonCode character of target carbon
	 * @param a_strOrbital Hybrid orbital of target carbon
	 * @param a_iTypeC1 Bond type of connection to first carbon
	 * @param a_iTypeC2 Bond type of connection to second carbon (0 at terminal carbon)
	 * @param a_nUniqMod Number of Unique modifications
	 * @param a_strStereo Stereo of target carbon
	 * @param a_bIsFoot Whether or not the carbon is foot of bridge on the ring
	 * @param a_strMod1 String of first modification
	 * @param a_strMod2 String of second modification (or null)
	 * @param a_strMod3 String of third modification (or null)
	 */
	private CarbonDescriptor( char a_strChar, String a_strOrbital, int a_iTypeC1, int a_iTypeC2, int a_nUniqMod,
			String a_strStereo, Boolean a_bIsFoot, String a_strMod1, String a_strMod2, String a_strMod3, int a_iScore ) {
		this.m_strChar             = a_strChar;
		this.m_strHybridOrbital    = a_strOrbital;
		this.m_iBondTypeCarbon1    = a_iTypeC1;
		this.m_iBondTypeCarbon2    = a_iTypeC2;
		this.m_nUniqueModification = a_nUniqMod;
		this.m_strStereo           = a_strStereo;
		this.m_bIsFootOfBridge     = a_bIsFoot;
		this.m_strModification1    = a_strMod1;
		this.m_strModification2    = a_strMod2;
		this.m_strModification3    = a_strMod3;
		this.m_iCarbonScore        = a_iScore;
	}

	/** Get SkeletonCode character of the carbon */
	public char getChar() {
		return this.m_strChar;
	}

	/** Get String of hybrid orbital of the carbon */
	public String getHybridOrbital() {
		return this.m_strHybridOrbital;
	}

	/** Whether or not the carbon is terminal */
	public Boolean isTerminal() {
		if ( this.m_strChar == '?' ) return null;
		return ( this.m_iBondTypeCarbon2 == 0 );
	}

	/** Get number of unique modifications connected the carbon */
	public int getNumberOfUniqueModifications() {
		return this.m_nUniqueModification;
	}

	/** Get string of stereo */
	public String getStereo() {
		return this.m_strStereo;
	}

	/** Whether or not the carbon is foot of bridge (null to false) */
	public boolean isFootOfBridge() {
		return  this.m_bIsFootOfBridge == null ? false : this.m_bIsFootOfBridge;
	}

	/**
	 * Get string of modification
	 * @param num Number of modification
	 * @return String of modification
	 */
	public String getModification(int num) {
		if (num == 1) return this.m_strModification1;
		if (num == 2) return this.m_strModification2;
		if (num == 3) return this.m_strModification3;
		return null;
	}

	/** Get score of the carbon for Backbone comparison */
	public int getCarbonScore() {
		return this.m_iCarbonScore;
	}

	/**
	 * Get CarbonDescriptor which correspond to a character of SkeltonCode
	 * @param cName A character of SkeltonCode
	 * @param isTerminal Whether or not the charactor is at terminal
	 * @return
	 */
	public static CarbonDescriptor forCharacter(char cName, boolean isTerminal) {
		for ( CarbonDescriptor cd : CarbonDescriptor.values() ) {
			if ( cd.m_strChar != cName ) continue;
			if ( cd.isTerminal() != isTerminal ) continue;
			return cd;
		}
		return CarbonDescriptor.XXX_UNKNOWN;
	}

	/**
	 * Get CarbonDescriptor by carbon situation
	 * @param a_strOrbital
	 * @param a_iTypeC1
	 * @param a_iTypeC2
	 * @param a_nUniqMod
	 * @param a_strStereo
	 * @param a_bIsFoot
	 * @param a_strMod1
	 * @param a_strMod2
	 * @param a_strMod3
	 * @return
	 */
	public static CarbonDescriptor forCarbonSituation( String a_strOrbital, int a_iTypeC1, int a_iTypeC2, int a_nUniqMod,
			String a_strStereo, Boolean a_bIsFoot, String a_strMod1, String a_strMod2, String a_strMod3 ) {
		// Match values
		for ( CarbonDescriptor cd : CarbonDescriptor.values() ) {
			if ( cd.m_strHybridOrbital != null
				&& !cd.m_strHybridOrbital.equals( a_strOrbital ) ) continue;
			if (  cd.m_iBondTypeCarbon1 != a_iTypeC1             ) continue;
			if (  cd.m_iBondTypeCarbon2 != a_iTypeC2             ) continue;
			if (  cd.m_nUniqueModification != a_nUniqMod         ) continue;
			if ( cd.m_strStereo != null
				&& !cd.m_strStereo.equals( a_strStereo )         ) continue;
			if ( cd.m_bIsFootOfBridge != null
				&& !cd.m_bIsFootOfBridge.equals( a_bIsFoot )     ) continue;
			if ( !cd.matchModifications( a_strMod1, a_strMod2, a_strMod3 ) ) continue;
			return cd;
		}
		return CarbonDescriptor.XXX_UNKNOWN;
	}

	private boolean matchModifications( String Mod1, String Mod2, String Mod3 ) {

		// Set this modification strings to array
		String[] mMods = new String[3];
		mMods[0] = this.m_strModification1;
		mMods[1] = this.m_strModification2;
		mMods[2] = this.m_strModification3;
		// Set argument modification strings to array
		String[] aMods = new String[3];
		aMods[0] = Mod1;
		aMods[1] = Mod2;
		aMods[2] = Mod3;

		// Matching each modifications
		for ( int i=0; i<3; i++ ) {
			if ( mMods[i] == null ) {
				if ( aMods[i] == null ) return true;
				return false;
			}
			if ( aMods[i] == null ) return false;
			if ( !mMods[i].equals(aMods[i]) ) {
				if ( mMods[i].charAt(0) != aMods[i].charAt(0) ) return false;
				if ( mMods[i].charAt(1) != 'X' ) return false;
			}
		}

		return true;
	}
}
