package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;

/**
 * Converter class for genarating subsumption relationship
 * @author MasaakiMatsubara
 *
 */
public class WURCSSubsumptionConverter {

	MSStateDeterminationUtility m_oUtil = new MSStateDeterminationUtility();

	/**
	 * Convert MS to unknown ring size
	 * @param a_oMS Target MS
	 * @return Converted MS (null if target MS not has ring MOD or ring position is already unknown)
	 */
	public MS convertUnknownRingSize(MS a_oMS) {
		// Ignore for no ring or unknown ring position (return null)
		if ( !this.m_oUtil.hasRing(a_oMS) ) return null;
		if ( this.m_oUtil.isRingSizeUnknown(a_oMS) ) return null;

		MS t_oMS = new MS(a_oMS.getSkeletonCode(), a_oMS.getAnomericPosition(), a_oMS.getAnomericSymbol());
		MOD t_oRingMOD = this.m_oUtil.getRingMOD(a_oMS);
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			// Copy for not ring MOD
			if ( !t_oMOD.equals( t_oRingMOD ) ) {
				t_oMS.addMOD( this.copyMOD(t_oMOD) );
				continue;
			}

			// For ring MOD
			// Get LIPs and set anomeric position to start
			LIPs t_oRingStartLIPs = t_oMOD.getListOfLIPs().getFirst();
			LIPs t_oRingEndLIPs   = t_oMOD.getListOfLIPs().getLast();
			if ( t_oRingStartLIPs.getLIPs().getFirst().getBackbonePosition() != a_oMS.getAnomericPosition() ) {
				t_oRingStartLIPs = t_oMOD.getListOfLIPs().getLast();
				t_oRingEndLIPs   = t_oMOD.getListOfLIPs().getFirst();
			}

			// Make unknown linkage position LIP and store to list
			char t_cDirection = t_oRingEndLIPs.getLIPs().getFirst().getBackboneDirection();
			int  t_iMAPPos    = t_oRingEndLIPs.getLIPs().getFirst().getModificationPosition();
			LIP t_oUnkLIP = new LIP(-1, t_cDirection, t_iMAPPos);
			LinkedList<LIP> t_aUnkLIP = new LinkedList<LIP>();
			t_aUnkLIP.add(t_oUnkLIP);

			// Make MOD and add LIPs
			MOD t_oUnkRingSizeMOD = new MOD(t_oMOD.getMAPCode());
			t_oUnkRingSizeMOD.addLIPs( this.copyLIPs(t_oRingStartLIPs) );
			t_oUnkRingSizeMOD.addLIPs( new LIPs(t_aUnkLIP) );

			// Add MOD to MS
			t_oMS.addMOD(t_oUnkRingSizeMOD);
		}
		return t_oMS;
	}

	/**
	 * Convert MS anomeric symbol to unknown
	 * @param a_oMS Target MS
	 * @return Converted MS (null if MS is open chain or already unknown anomer)
	 */
	public MS convertUnknownAnomer(MS a_oMS) {
		if ( this.m_oUtil.isOpenChain(a_oMS) ) return null;
		if ( this.m_oUtil.isAnomerUnknown(a_oMS) ) return null;

		MS t_oMS = new MS(a_oMS.getSkeletonCode(), a_oMS.getAnomericPosition(), 'x');
		for ( MOD t_oMOD : a_oMS.getMODs() )
			t_oMS.addMOD( this.copyMOD(t_oMOD) );

		return t_oMS;
	}

	/**
	 * Convert MS (candidate) anomeric carbon to uncertain for reducing end
	 * @param a_oMS Target MS
	 * @return Converted MS (null if anomeric carbon is uncertain or MS does not have anomeric carbon or aldehyde/ketone)
	 */
	public MS convertAnomericCarbonToUncertain(MS a_oMS) {
		if ( this.m_oUtil.hasUncertainAnomericCarbon(a_oMS) ) return null;
		if ( !this.m_oUtil.hasPotentialCarbonylGroup(a_oMS) ) return null;

		String t_strUncertainSkeletonCode = this.convertCalbonylCarbonToUncertainForSC(a_oMS.getSkeletonCode());
		MS t_oMS = new MS(t_strUncertainSkeletonCode, 0, 'x');
		MOD t_oRingMOD = this.m_oUtil.getRingMOD(a_oMS);
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			if ( t_oRingMOD != null && t_oMOD.equals(t_oRingMOD) ) continue;
			t_oMS.addMOD( this.copyMOD(t_oMOD) );
		}
		return t_oMS;
	}

	private String convertCalbonylCarbonToUncertainForSC(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == 'o' ) t_cCD = 'u';
			if ( t_cCD == 'O' ) t_cCD = 'U';
			if ( t_cCD == 'a' ) t_cCD = (i == 0)? 'u' : 'U';
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Correct format of relative cofiguration
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a1211h-1a_1-5 -> null
	 * a4344h-1a_1-5 -> null
	 * h2124h        -> h212xh
	 * h4343h        -> h3434h
	 * </pre>
	 * @param a_oMS
	 * @return Correct format MS (null if it is already correct or there is no relative configuration)
	 */
	public MS correctRelativeConfiguration(MS a_oMS) {
		if ( !this.m_oUtil.hasRelativeConfiguration(a_oMS) ) return null;

		String t_strCorrectSC = this.correctRelativeConfigurationForSC( a_oMS.getSkeletonCode() );
		if ( t_strCorrectSC == null ) return null;
		return this.changeSkeletonCode(a_oMS, t_strCorrectSC);
	}

	public String correctRelativeConfigurationForSC(String a_strSC) {
		int t_nRelative = 0;
		boolean t_bIsCorrect = true;
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD != '3' && t_cCD != '4' && t_cCD != '7' && t_cCD != '8' ) continue;
			t_nRelative++;
			if ( t_cCD == '3' || t_cCD == '7' ) t_bIsCorrect = false;
			if ( t_cCD == '4' || t_cCD == '8' ) t_bIsCorrect = true;
		}

		if ( t_nRelative == 1 )
			return this.convertConfigurationRelativeToUnknownForSC(a_strSC);
		if ( t_bIsCorrect )
			return null;

		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			t_cCD = ( t_cCD == '3' )? '4' :
					( t_cCD == '4' )? '3' :
					( t_cCD == '7' )? '8' :
					( t_cCD == '8' )? '7' : t_cCD;
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Convert MS stereo to rerative configuration (D/L unknown)
	 * <pre>
	 * a2122h-1b_1-5 -> a4344h-1b_1-5
	 * a1211h-1a_1-5 -> a4344h-1a_1-5
	 * a4344h-1a_1-5 -> null
	 * h2121h        -> h3434h
	 * u212xh        -> u434xh
	 * </pre>
	 * @param a_oMS Target MS
	 * @return Converted MS (null if MS has no stereo or not contains absolute configuration)
	 */
	public MS convertConfigurationAbsoluteToRelative(MS a_oMS) {
		if (  this.m_oUtil.hasNoStereo(a_oMS) ) return null;
		if ( !this.m_oUtil.hasAbsoluteConfiguration(a_oMS) ) return null;
		if (  this.m_oUtil.hasRelativeConfiguration(a_oMS) ) return null;

		return this.changeSkeletonCode( a_oMS, this.convertConfigurationAbsoluteToRelativeForSC( a_oMS.getSkeletonCode()) );
	}

	private String convertConfigurationAbsoluteToRelativeForSC(String a_strSC) {
		boolean t_bIsD = this.m_oUtil.isDConfigurationForSC(a_strSC);
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == '1' ) t_cCD = (t_bIsD)? '3' : '4';
			if ( t_cCD == '2' ) t_cCD = (t_bIsD)? '4' : '3';
			if ( t_cCD == '5' ) t_cCD = (t_bIsD)? '7' : '8';
			if ( t_cCD == '6' ) t_cCD = (t_bIsD)? '8' : '7';
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Covert MS stereo to unknown configuration
	 * <pre>
	 * a2122h-1b_1-5 -> axxxxh-1b_1-5
	 * a1211h-1a_1-5 -> axxxxh-1a_1-5
	 * a4344h-1a_1-5 -> axxxxh-1a_1-5
	 * h2121h        -> hxxxxh
	 * hx212h        -> hxxxxh
	 * uxxxxh        -> null
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS (null if MS has no stereo or is already unknown configuration only)
	 */
	public MS convertConfigurationToUnknown(MS a_oMS) {
		if ( this.m_oUtil.hasNoStereo(a_oMS) ) return null;
		if ( !this.m_oUtil.hasRelativeConfiguration(a_oMS) && !this.m_oUtil.hasAbsoluteConfiguration(a_oMS) ) return null;

		return this.changeSkeletonCode( a_oMS, this.convertConfigurationToUnknownForSC( a_oMS.getSkeletonCode() ) );
	}

	private String convertConfigurationToUnknownForSC(String a_strSC) {
		String t_strNewSC = this.convertConfigurationRelativeToUnknownForSC(a_strSC);
		return this.convertConfigurationAbsoluteToUnknownForSC(t_strNewSC);
	}

	private String convertConfigurationAbsoluteToUnknownForSC(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == '1' || t_cCD == '2' ) t_cCD = 'x';
			if ( t_cCD == '5' || t_cCD == '6' ) t_cCD = 'X';
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Covert MS stereo configuration relative to unknown
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a1211h-1a_1-5 -> null
	 * a4344h-1a_1-5 -> axxxxh-1a_1-5
	 * h2121h        -> null
	 * hx214h        -> hx21xh
	 * uxxxxh        -> null
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS (null if MS not contain relative configuration)
	 */
	public MS convertCofigurationRelativeToUnknown(MS a_oMS) {
		if ( !this.m_oUtil.hasRelativeConfiguration(a_oMS) ) return null;

		return this.changeSkeletonCode( a_oMS, this.convertConfigurationRelativeToUnknownForSC( a_oMS.getSkeletonCode() ) );
	}

	private String convertConfigurationRelativeToUnknownForSC(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == '3' || t_cCD == '4' ) t_cCD = 'x';
			if ( t_cCD == '7' || t_cCD == '8' ) t_cCD = 'X';
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Covert MS stereo to D configuration from relative configuration
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a1211h-1a_1-5 -> null
	 * a4344h-1a_1-5 -> a2122h-1a_1-5
	 * h2121h        -> null
	 * hx212h        -> null
	 * uxxxxh        -> null
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS (null if MS has not relative configuration)
	 */
	public MS convertConfigurationRelativeToD(MS a_oMS) {
		if ( !this.m_oUtil.hasRelativeConfiguration(a_oMS) ) return null;

		return this.changeSkeletonCode( a_oMS, this.convertConfigurationRelativeToDForSC( a_oMS.getSkeletonCode() ) );
	}

	private String convertConfigurationRelativeToDForSC(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			t_cCD = ( t_cCD == '3' )? '1' :
				    ( t_cCD == '4' )? '2' :
				    ( t_cCD == '7' )? '5' :
				    ( t_cCD == '8' )? '6' : t_cCD;
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Covert MS stereo to L configuration from relative configuration
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a1211h-1a_1-5 -> null
	 * a4344h-1a_1-5 -> a1211h-1a_1-5
	 * h2121h        -> null
	 * hx212h        -> null
	 * uxxxxh        -> null
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS (null if MS has not relative configuration)
	 */
	public MS convertConfigurationRelativeToL(MS a_oMS) {
		if ( !this.m_oUtil.hasRelativeConfiguration(a_oMS) ) return null;

		return this.changeSkeletonCode( a_oMS, this.convertConfigurationRelativeToLForSC( a_oMS.getSkeletonCode() ) );
	}

	private String convertConfigurationRelativeToLForSC(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			t_cCD = ( t_cCD == '3' )? '2' :
				    ( t_cCD == '4' )? '1' :
				    ( t_cCD == '7' )? '6' :
				    ( t_cCD == '8' )? '5' : t_cCD;
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Convert unknown configuration to absolute
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a4344h-1a_1-5 -> null
	 * hx212h        -> h1212h, h2212h
	 * uxxxxh        -> u1111h, u1112h, ... , u2221h, u2222h
	 * </pre>
	 * @param a_oMS Target MS
	 * @return Possible absolute configuration of MSs by unknown configuration (null if there is no unknown configuration)
	 */
	public LinkedList<MS> convertConfigurationUnknownToAbsolutes(MS a_oMS) {
		if ( !this.m_oUtil.hasUnknownConfiguration(a_oMS) ) return null;

		LinkedList<MS> t_aAbsoluteMSs = new LinkedList<MS>();
		for ( String t_strSC : this.convertConfigurationUnknownToAbsolutesForSC( a_oMS.getSkeletonCode() ) )
			t_aAbsoluteMSs.add( this.changeSkeletonCode(a_oMS, t_strSC) );

		return t_aAbsoluteMSs;
	}

	private LinkedList<String> convertConfigurationUnknownToAbsolutesForSC(String a_strSC) {
		LinkedList<String> t_aNewSCs = new LinkedList<String>();
		t_aNewSCs.add("");
		for ( int i=0; i<a_strSC.length(); i++ ) {
			LinkedList<String> t_aTmpSCs = new LinkedList<String>();

//			char[] t_aCDs = new char[2];
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == 'x' ) {
				for ( String t_strSC : t_aNewSCs ) {
					t_aTmpSCs.add( t_strSC + '1' );
					t_aTmpSCs.add( t_strSC + '2' );
				}
			} else if ( t_cCD == 'X' ) {
				for ( String t_strSC : t_aNewSCs ) {
					t_aTmpSCs.add( t_strSC + '5' );
					t_aTmpSCs.add( t_strSC + '6' );
				}
			} else {
				for ( String t_strSC : t_aNewSCs )
					t_aTmpSCs.add( t_strSC + t_cCD );
			}
			t_aNewSCs = t_aTmpSCs;
		}
		return t_aNewSCs;
	}

	/**
	 * Convert deoxy style from CarbonDescriptor to MOD "*"
	 * <pre>
	 * a2122h-1b_1-5 -> null
	 * a1211h-1a_1-5 -> null
	 * a4344h-1a_1-5 -> null
	 * h2121h        -> null
	 * hx212h        -> null
	 * uxxxxh        -> null
	 * uxdxxh        -> uxxxxh_3*
	 * uxxxxm        -> uxxxxh_6*
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS (null if MS has not deoxy, "d" or "m")
	 */
	public MS moveDeoxyToMOD(MS a_oMS) {
		LinkedList<Integer> t_aDeoxyPos = this.getDeoxyPositions(a_oMS);
		if ( t_aDeoxyPos.isEmpty() ) return null;
		MS t_oNewMS = this.changeSkeletonCode( a_oMS, this.convertCarbonDescriptorDeoxyToHydroxyl( a_oMS.getSkeletonCode() ) );
		for ( Integer t_iPos : t_aDeoxyPos ) {
			MOD t_oDeoxyMOD = new MOD("*");
			LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();
			t_aLIPs.add( new LIP(t_iPos, ' ', 0) );
			t_oDeoxyMOD.addLIPs( new LIPs(t_aLIPs) );
			t_oNewMS.addMOD(t_oDeoxyMOD);
		}
		return t_oNewMS;
	}

	/**
	 * Get deoxy positions on MS
	 * @param a_oMS TargetMS
	 * @return List of deoxy, "d" or "m", positions (Empty list if there is no deoxy)
	 */
	public LinkedList<Integer> getDeoxyPositions(MS a_oMS) {
		LinkedList<Integer> t_aDeoxyPositions = new LinkedList<Integer>();
		String t_strSC = a_oMS.getSkeletonCode();
		for ( int i=0; i<t_strSC.length(); i++ ) {
			char t_cCD = t_strSC.charAt(i);
			if ( t_cCD == 'd' || t_cCD == 'm' ) t_aDeoxyPositions.add(i+1);
			if ( t_cCD == 'e' || t_cCD == 'z' || t_cCD == 'f' ) t_aDeoxyPositions.add(i+1);
		}
		return t_aDeoxyPositions;
	}

	private String convertCarbonDescriptorDeoxyToHydroxyl(String a_strSC) {
		String t_strNewSC = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			t_cCD = ( t_cCD == 'd' )? 'x' :
					( t_cCD == 'm' )? 'h' :
					( t_cCD == 'e' )? 'E' :
					( t_cCD == 'z' )? 'Z' :
					( t_cCD == 'f' )? 'F' : t_cCD;
			t_strNewSC += t_cCD;
		}
		return t_strNewSC;
	}

	/**
	 * Convert mod positions to unknown
	 * @param a_oMS TargetMS
	 * @return Converted MS
	 */
	public MS convertMODPositionToUnknown(MS a_oMS) {
		MS t_oNewMS = new MS(a_oMS.getSkeletonCode(), a_oMS.getAnomericPosition(), a_oMS.getAnomericSymbol());
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			// No change for ring
			if ( t_oMOD.getListOfLIPs().size() > 1 ) {
				t_oNewMS.addMOD( this.copyMOD(t_oMOD) );
				continue;
			}

			LIP t_oLIP = t_oMOD.getListOfLIPs().getFirst().getLIPs().getFirst();
			MOD t_oNewMOD = new MOD( t_oMOD.getMAPCode() );
			LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();
			t_aLIPs.add( new LIP( -1, t_oLIP.getBackboneDirection(), t_oLIP.getModificationPosition() ) );
			t_oNewMOD.addLIPs( new LIPs(t_aLIPs) );
			t_oNewMS.addMOD(t_oNewMOD);
		}
		return t_oNewMS;
	}

	/**
	 * Convert carbonyl group reduction
	 * <pre>
	 * a2122h-1b_1-5 -> h2122h
	 * u1211h        -> h1211h
	 * o4344h        -> h4344h
	 * h2121h        -> null
	 * </pre>
	 * @param a_oMS TargetMS
	 * @return Converted MS
	 */
	public MS convertCarbonylGroupToHydroxyl(MS a_oMS) {
		if ( !this.m_oUtil.hasPotentialCarbonylGroup(a_oMS) ) return null;

		// Convert uncertain
		MS t_oMS = this.convertAnomericCarbonToUncertain(a_oMS);

		int t_iCarbonylPos = this.m_oUtil.getPotentialCarbonylPosition(t_oMS);
		StringBuilder t_sbSkeletonCode = new StringBuilder( a_oMS.getSkeletonCode() );
		t_sbSkeletonCode.replace(t_iCarbonylPos-1, t_iCarbonylPos, (t_iCarbonylPos == 1)? "h" : "x");

		return this.changeSkeletonCode( t_oMS, t_sbSkeletonCode.toString() );
	}

	private MS changeSkeletonCode(MS a_oMS, String a_oSC) {
		return this.changeAnomerAndSkeletonCode(a_oMS, a_oSC, a_oMS.getAnomericPosition(), a_oMS.getAnomericSymbol());
	}

	private MS changeAnomericInformation(MS a_oMS, int a_iPos, char a_cSymbol) {
		return this.changeAnomerAndSkeletonCode(a_oMS, a_oMS.getSkeletonCode(), a_iPos, a_cSymbol);
	}

	private MS changeAnomerAndSkeletonCode(MS a_oMS, String a_oSC, int a_iPos, char a_cSymbol) {
		MS t_oMS = new MS(a_oSC, a_iPos, a_cSymbol);
		for ( MOD t_oMOD : a_oMS.getMODs() )
			t_oMS.addMOD( this.copyMOD(t_oMOD) );
		return t_oMS;
	}

	/**
	 * Copy MOD
	 * TODO: Make copy method to MOD class?
	 * @param a_oMOD
	 * @return
	 */
	private MOD copyMOD(MOD a_oMOD) {
		MOD t_oCopyMOD = new MOD(a_oMOD.getMAPCode());
		for ( LIPs t_oLIPs : a_oMOD.getListOfLIPs() ) {
			t_oCopyMOD.addLIPs( this.copyLIPs(t_oLIPs) );
		}
		return t_oCopyMOD;
	}

	/**
	 * Copy LIPs
	 * TODO: Make copy method to LIPs class?
	 * @param a_oLIPs
	 * @return
	 */
	private LIPs copyLIPs(LIPs a_oLIPs) {
		LinkedList<LIP> t_aLIP = new LinkedList<LIP>();
		for ( LIP t_oLIP : a_oLIPs.getLIPs() ) {
			t_aLIP.add( this.copyLIP(t_oLIP) );
		}
		return new LIPs(t_aLIP);
	}

	/**
	 * Copy LIP
	 * TODO: Make copy method to LIP class?
	 * @param a_oLIP
	 * @return
	 */
	private LIP copyLIP(LIP a_oLIP) {
		LIP t_oCopyLIP = new LIP(a_oLIP.getBackbonePosition(), a_oLIP.getBackboneDirection(), a_oLIP.getModificationPosition() );
		if ( a_oLIP.getBackboneProbabilityLower() != 1.0 ) {
			t_oCopyLIP.setBackboneProbabilityLower(a_oLIP.getBackboneProbabilityLower());
			t_oCopyLIP.setBackboneProbabilityUpper(a_oLIP.getBackboneProbabilityUpper());
		}
		if ( a_oLIP.getModificationProbabilityLower() != 1.0 ) {
			t_oCopyLIP.setModificationProbabilityLower(a_oLIP.getModificationProbabilityLower());
			t_oCopyLIP.setModificationProbabilityUpper(a_oLIP.getModificationProbabilityUpper());
		}
		return t_oCopyLIP;
	}
}
