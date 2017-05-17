package org.glycoinfo.WURCSFramework.util.property.chemical;



/**
 * Enum class for calculating composition of CarbonDescriptors
 * @author MasaakiMatsubara
 *
 */
public enum CarbonDescriptorChemicalComposition {

	T_m( 'm', true, 0, 3 ), // -C(H)(H)(H) "CH3"
	T_M( 'M', true, 3, 3 ), // -C(X)(X)(X) "C(OH)3"
	T_h( 'h', true, 1, 3 ), // -C(O)(H)(H) "CH2OH"
	T_c( 'c', true, 2, 3 ), // -C(X)(X)(H) "CH(OH)2"
	T_C( 'C', true, 3, 3 ), // -C(X)(X)(X) "C(OH)3"
	T_1( '1', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_2( '2', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_3( '3', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_4( '4', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_x( 'x', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_5( '5', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	T_6( '6', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	T_7( '7', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	T_8( '8', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	T_X( 'X', true, 3, 3 ), // -C(X)(Y)(Z) "C(OH)3"
	T_o( 'o', true, 1, 1 ), // -C(=X)(H) "COH"
	T_O( 'O', true, 1, 2 ), // -C(=X)(H) "CHOH"
	T_A( 'A', true, 2, 1 ), // -C(=X)(Y) "COOH"
	T_n( 'n', true, 0, 2 ), // =C(H)(H) "CH2"
	T_N( 'N', true, 2, 2 ), // =C(X)(X) "C(OH)2"
	T_e( 'e', true, 1, 2 ), // =C(X)(H) "CHOH"
	T_z( 'z', true, 1, 2 ), // =C(X)(H) "CHOH"
	T_f( 'f', true, 1, 2 ), // =C(X)(H) "CHOH"
	T_E( 'E', true, 2, 2 ), // =C(X)(Y) "C(OH)2"
	T_Z( 'Z', true, 2, 2 ), // =C(X)(Y) "C(OH)2"
	T_F( 'F', true, 2, 2 ), // =C(X)(Y) "C(OH)2"
	T_T( 'T', true, 1, 1 ), // -C(#X) or #C(X) "COH"
	T_K( 'K', true, 1, 0 ), // =C(=X) "CO"
	T_t( 't', true, 0, 1 ), // #C(H) "CH"

	T_a( 'a', true, 2, 3 ), // -C(X)(Y)(H) "CH(OH)2"
	T_u( 'u', true, 1, 1 ), // -C(=X)(H) or -C(X)(Y)(H) "CHO"

	// Non-terminal
	N_d( 'd', false, 0, 2 ), // -C(H)(H)- "CH2"
	N_C( 'C', false, 2, 2 ), // -C(X)(X)- "C(OH)2"
	N_1( '1', false, 1, 2 ), // -C(X)(H)- "CHOH"
	N_2( '2', false, 1, 2 ), // -C(X)(H)- "CHOH"
	N_3( '3', false, 1, 2 ), // -C(X)(H)- "CHOH"
	N_4( '4', false, 1, 2 ), // -C(X)(H)- "CHOH"
	N_x( 'x', false, 1, 2 ), // -C(X)(H)- "CHOH"
	N_5( '5', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	N_6( '6', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	N_7( '7', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	N_8( '8', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	N_X( 'X', false, 2, 2 ), // -C(X)(Y)- "C(OH)2"
	N_O( 'O', false, 1, 0 ), // -C(=X)- "CO"
	N_e( 'e', false, 0, 1 ), // =C(H)- "CH"
	N_z( 'z', false, 0, 1 ), // =C(H)- "CH"
	N_n( 'n', false, 0, 1 ), // =C(H)- "CH"
	N_f( 'f', false, 0, 1 ), // =C(H)- "CH"
	N_E( 'E', false, 1, 1 ), // =C(X)- "COH"
	N_Z( 'Z', false, 1, 1 ), // =C(X)- "COH"
	N_N( 'N', false, 1, 1 ), // =C(X)- "COH"
	N_F( 'F', false, 1, 1 ), // =C(X)- "COH"
	N_K( 'K', false, 0, 0 ), // =C= "C"
	N_T( 'T', false, 0, 0 ), // #C- "C"

	N_a( 'a', false, 2, 2 ), // -C(X)(X)- "C(OH)2"
	N_U( 'U', false, 1, 0 ), // -C(=X)- or -C(X)(X)- "CO"

	X_Q( 'Q', false,-1,-1 ), // C???
	X_X( '?', false,-1,-1 ); // C???

	/** Charactor of carbon descriptor */
	private char    m_strChar;
	/** Whether or not carbon is terminal */
	private boolean m_isTerminal;
	/** Number of oxygen in basic modification (at =O or -OH) */
	private int     m_iNumberOfOxygen;
	/** Number of hydrogen on the carbon and basic modification (at -H or -OH) */
	private int     m_iNumberOfHydrogen;

	private CarbonDescriptorChemicalComposition( char a_cName, boolean a_isTerminal, int a_nO, int a_nH ) {
		this.m_strChar           = a_cName;
		this.m_isTerminal        = a_isTerminal;
		this.m_iNumberOfOxygen   = a_nO;
		this.m_iNumberOfHydrogen = a_nH;
	}

	public int getNumberOfOxgen() {
		return this.m_iNumberOfOxygen;
	}

	public int getNumberOfHydrogen() {
		return this.m_iNumberOfHydrogen;
	}

	/**
	 * Match and get CarbonDescriptor which correspond to a character of SkeltonCode
	 * @param a_cName A character of SkeltonCode
	 * @return
	 */
	public static CarbonDescriptorChemicalComposition forCharacter(char a_cName, boolean a_bIsTerminal) {
		for ( CarbonDescriptorChemicalComposition t_enumCDCC : CarbonDescriptorChemicalComposition.values() ) {
			if ( t_enumCDCC.m_strChar == a_cName && t_enumCDCC.m_isTerminal == a_bIsTerminal ) return t_enumCDCC;
		}
		return null;
	}
}