package org.glycoinfo.WURCSFramework.util.mass;


public enum CarbonDescriptorPropaties {
	SZ3_METHYL_L   ( 'm', true, 0, 3 ), // -C(H)(H)(H) "CH3"
	SZ3_METHYL_U   ( 'M', true, 3, 3 ), // -C(X)(X)(X) "C(OH)3"
	SZ3_HYDROXYL_L ( 'h', true, 1, 3 ), // -C(O)(H)(H) "CH2OH"
	SZ3_HYDROXYL_U ( 'H', true, 1, 3 ), // -C(H)(H)(X) "CH2OH"
	SZ3_ACETAL_L   ( 'c', true, 2, 3 ), // -C(O)(O)(H) "CH(OH)2"
	SZ3_ACETAL_U   ( 'C', true, 3, 3 ), // -C(O)(O)(X) "C(OH)3"
	SZ3_DOUBLE_L   ( 'd', true, 2, 3 ), // -C(X)(X)(H) "CH(OH)2"
	SZ3_DOUBLE_U   ( 'D', true, 3, 3 ), // -C(X)(X)(Y) "C(OH)3"
	SZ3_STEREO_S   ( '1', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_STEREO_R   ( '2', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_STEREO_s   ( '3', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_STEREO_r   ( '4', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_STEREO_X   ( 'x', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_NORING_S_L ( 's', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_NORING_R_L ( 'r', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_NORING_X_L ( 'q', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	SZ3_CHIRAL_S   ( '5', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_CHIRAL_R   ( '6', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_CHIRAL_s   ( '7', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_CHIRAL_r   ( '8', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_CHIRAL_X   ( 'X', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_NORING_S_U ( 'S', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_NORING_R_U ( 'R', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ3_NORING_X_U ( 'Q', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	SZ2_ALDEHYDE_L ( 'o', true, 1, 1 ), // -C(=O)(H) "COH"
	SZ2_ACID_L     ( 'a', true, 2, 1 ), // -C(=O)(O) "COOH"
	SZ2_ALDEHYDE_U ( 'O', true, 1, 2 ), // -C(=X)(H) "CHOH"
	SZ2_ACID_U     ( 'A', true, 2, 2 ), // -C(=X)(Y) "C(OH)2"
	SZ2_UNDEFINED_L( 'u', true, 1, 1 ), // -C(=X)(H) or -C(X)(Y)(H) "CH(OH)2"
	SZ2_UNDEFINED_U( 'U', true, 2, 1 ), // -C(=X)(Y) or -C(X)(Y)(Z) "C(OH)3"
	DZ2_METHYLENE_L( 'n', true, 0, 2 ), // =C(H)(H) "CH2"
	DZ2_METHYLENE_U( 'N', true, 2, 2 ), // =C(X)(X) "C(OH)2"
	DZ2_CISTRANS_EL( 'e', true, 1, 2 ), // =C(X)(H) "CHOH"
	DZ2_CISTRANS_ZL( 'z', true, 1, 2 ), // =C(X)(H) "CHOH"
	DZ2_CISTRANS_XL( 'f', true, 1, 2 ), // =C(X)(H) "CHOH"
	DZ2_CISTRANS_EU( 'E', true, 1, 2 ), // =C(X)(Y) "C(OH)2"
	DZ2_CISTRANS_ZU( 'Z', true, 1, 2 ), // =C(X)(Y) "C(OH)2"
	DZ2_CISTRANS_XU( 'F', true, 1, 2 ), // =C(X)(Y) "C(OH)2"
	SZ1_XETHYNE    ( 'T', true, 1, 1 ), // -C(#X) "COH"
	DZ1_KETENE_L   ( 'k', true, 1, 0 ), // =C(=O) "CO"
	DZ1_KETENE_U   ( 'K', true, 1, 1 ), // =C(=X) "COH"
	TZ1_ETHYNE_L   ( 't', true, 0, 1 ), // #C(H) "CH"
	TZ1_ETHYNE_U   ( 'T', true, 1, 1 ), // #C(X) "COH"

	// Non-terminal
	SS3_METHYNE    ( 'd', false, 0, 2 ), // -C(H)(H)- "CH2"
	SS3_ACETAL     ( 'b', false, 2, 2 ), // -C(O)(O)- "C(OH)2"
	SS3_XMETHYNE   ( 'D', false, 2, 2 ), // -C(X)(X)- "C(OH)2"
	SS3_STEREO_S   ( '1', false, 1, 2 ), // -C(X)(H)- "CHOH"
	SS3_STEREO_R   ( '2', false, 1, 2 ), // -C(X)(H)- "CHOH"
	SS3_STEREO_s   ( '3', false, 1, 2 ), // -C(X)(H)- "CHOH"
	SS3_STEREO_r   ( '4', false, 1, 2 ), // -C(X)(H)- "CHOH"
	SS3_STEREO_X   ( 'x', false, 1, 2 ), // -C(X)(H)- "CHOH"
	SS3_CHIRAL_S   ( '5', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	SS3_CHIRAL_R   ( '6', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	SS3_CHIRAL_s   ( '7', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	SS3_CHIRAL_r   ( '8', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	SS3_CHIRAL_X   ( 'X', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	SS2_KETONE_L   ( 'o', false, 1, 0 ), // -C(=O)- "CO"
	SS2_KETONE_U   ( 'O', false, 1, 1 ), // -C(=X)- "COH"
	SS2_UNDEFINED_U( 'U', false, 1, 0 ), // -C(=X)- "COH"
	DS2_CISTRANS_EL( 'e', false, 0, 1 ), // =C(H)- "CH"
	DS2_CISTRANS_ZL( 'z', false, 0, 1 ), // =C(H)- "CH"
	DS2_CISTRANS_NL( 'n', false, 0, 1 ), // =C(H)- "CH"
	DS2_CISTRANS_XL( 'f', false, 0, 1 ), // =C(H)- "CH"
	DS2_CISTRANS_EU( 'E', false, 1, 1 ), // =C(X)- "COH"
	DS2_CISTRANS_ZU( 'Z', false, 1, 1 ), // =C(X)- "COH"
	DS2_CISTRANS_NU( 'N', false, 1, 1 ), // =C(X)- "COH"
	DS2_CISTRANS_XU( 'F', false, 1, 1 ), // =C(X)- "COH"
	DD1_ALLENE     ( 'K', false, 0, 0 ), // =C= "C"
	TS1_ETHYNE     ( 'T', false, 0, 0 ), // #C- "C"

	XXX_UNKNOWN    ( '?', false, 0, 0 ); // C???

	/** Charactor of carbon descriptor */
	private char    m_strChar;
	/** Whether or not carbon is terminal */
	private boolean m_isTerminal;
	/** Number of oxygen in basic modification (at =O or -OH) */
	private int     m_iNumberOfOxygen;
	/** Number of oxygen in basic modification (at -OH or -H) */
	private int     m_iNumberOfHydrogen;

	private CarbonDescriptorPropaties( char a_cName, boolean a_isTerminal, int a_nO, int a_nH ) {
		this.m_strChar           = a_cName;
		this.m_isTerminal        = a_isTerminal;
		this.m_iNumberOfOxygen   = a_nO;
		this.m_iNumberOfHydrogen = a_nH;
	}

	/** Get default mass for the descriptor */
	public double getDefaultMass() {
		return AtomicPropaties.C.getMass() +
			   AtomicPropaties.O.getMass() * this.m_iNumberOfOxygen +
			   AtomicPropaties.H.getMass() * this.m_iNumberOfHydrogen;
	}

	/**
	 * Match and get CarbonDescriptor which correspond to a character of SkeltonCode
	 * @param cName A character of SkeltonCode
	 * @return
	 */
	public static CarbonDescriptorPropaties forCharacter(char cName, boolean isTerminal) {
		for ( CarbonDescriptorPropaties cd : CarbonDescriptorPropaties.values() ) {
			if ( cd.m_strChar == cName && cd.m_isTerminal == isTerminal ) return cd;
		}
		return null;
	}

}
