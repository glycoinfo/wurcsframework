package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;

/**
 * Utility class to determine a state of ambiguity at partial structure of MS for WURCSSubsumption
 * @author ShinichiroTsuchiya
 * @author MasaakiMatsubara
 *
 */
public class MSStateDeterminationUtility {

	// Possible position of glycosidic linkage check

	/**
	 * Get possible positions of glycosidic linkage
	 * @return List of Integer of possible position number of glycosidic linkage
	 */
	public LinkedList<Integer> getPossiblePositions(MS a_oMS) {
		LinkedList<Integer> t_aPossiblePos = new LinkedList<Integer>();
		String t_strSkeletonCode = a_oMS.getSkeletonCode();
		LinkedList<Integer> t_aOnePosList = this.getOneSubstitutionPositionForSkeletonCode(t_strSkeletonCode);
		LinkedList<Integer> t_aTwoPosList = this.getTwoSubstitutionPositionForSkeletonCode(t_strSkeletonCode);
		LinkedList<Integer> t_aThreePosList = this.getTwoSubstitutionPositionForSkeletonCode(t_strSkeletonCode);
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			for ( LIPs t_oLIPS : t_oMOD.getListOfLIPs() ) {
				if ( t_oLIPS.getLIPs().size() > 1 ) continue;
				Integer t_iPos = t_oLIPS.getLIPs().getFirst().getBackbonePosition();
				// Remove positions of modification from possible position
				if ( t_aOnePosList.contains(t_iPos) ) t_aOnePosList.remove(t_iPos);
				// If two possible position count remain, reduce to one
				if ( t_aTwoPosList.contains(t_iPos) ) {
					t_aTwoPosList.remove(t_iPos);
					t_aOnePosList.add(t_iPos);
				}
				// If three possible position count remain, reduce to two
				if ( t_aThreePosList.contains(t_iPos) ) {
					t_aThreePosList.remove(t_iPos);
					t_aTwoPosList.add(t_iPos);
				}
			}
		}
		t_aPossiblePos.addAll(t_aOnePosList);
		t_aPossiblePos.addAll(t_aTwoPosList);
		t_aPossiblePos.addAll(t_aThreePosList);
		return t_aPossiblePos;
	}

	private LinkedList<Integer> getOneSubstitutionPositionForSkeletonCode(String a_strSC) {
		LinkedList<Integer> t_aPosList = new LinkedList<Integer>();
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			// For terminal carbon
			if ( i == 0 || i == a_strSC.length()-1) {
				if ( t_cCD == 'h' || t_cCD == 'A' || t_cCD == 'e' || t_cCD == 'z' || t_cCD == 'T' )
					t_aPosList.add(i+1);
				continue;
			}
			// For non-terminal carbon
			if ( t_cCD == '1' || t_cCD == '2' || t_cCD == '3' || t_cCD == '4' || t_cCD == 'x'  // For sp3
			  || t_cCD == 'E' || t_cCD == 'Z' || t_cCD == 'F' )  // For sp2
				t_aPosList.add(i+1);
		}
		return t_aPosList;
	}

	private LinkedList<Integer> getTwoSubstitutionPositionForSkeletonCode(String a_strSC) {
		LinkedList<Integer> t_aPosList = new LinkedList<Integer>();
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			// For terminal carbon
			if ( i == 0 || i == a_strSC.length()-1) {
				if ( t_cCD == 'c' || t_cCD == '1' || t_cCD == '2' || t_cCD == '3' || t_cCD == '4' || t_cCD == 'x'  // For sp3
				  || t_cCD == 'N' || t_cCD == 'E' || t_cCD == 'Z' || t_cCD == 'F' ) //For sp2
					t_aPosList.add(i+1);
				continue;
			}
			// For non-terminal carbon
			if ( t_cCD == '5' || t_cCD == '6' || t_cCD == '7' || t_cCD == '8' || t_cCD == 'X' )
				t_aPosList.add(i+1);
		}
		return t_aPosList;
	}

	private LinkedList<Integer> getThreeSubstitutionPositionForSkeletonCode(String a_strSC) {
		LinkedList<Integer> t_aPosList = new LinkedList<Integer>();
		for ( int i=0; i<a_strSC.length(); i++ ) {
			// For terminal carbon only
			if ( i != 0 && i != a_strSC.length()-1) continue;
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == 'M' || t_cCD == 'C' || t_cCD == '5' || t_cCD == '6' || t_cCD == '7' || t_cCD == '8' || t_cCD == 'X' )
				t_aPosList.add(i+1);
		}
		return t_aPosList;
	}

	// Anomeric carbon check

	/**
	 * Does MS have uncertain anomeric carbon ('u' or 'U')
	 * @param a_oMS Target MS
	 * @return True if MS has uncertain anomeric carbon
	 */
	protected boolean hasUncertainAnomericCarbon(MS a_oMS) {
		return ( a_oMS.getSkeletonCode().contains("u") || a_oMS.getSkeletonCode().contains("U") );
	}

	/**
	 * Is MS open chain
	 * @param a_oMS Target MS
	 * @return True if MS is open chain, false if MS has uncertain anomeric carbon or is not open chain
	 */
	protected boolean isOpenChain(MS a_oMS) {
		if ( this.hasUncertainAnomericCarbon(a_oMS) ) return false;
		return ( a_oMS.getAnomericPosition() == MS.OPEN_CHAIN  );
	}

	/**
	 * Does MS has (potential) carbonyl group
	 * @param a_oMS Target MS
	 * @return true if MS has (uncertain) anomeric carbon or aldehyde/ketone group
	 */
	protected boolean hasPotentialCarbonylGroup(MS a_oMS) {
		if ( this.hasUncertainAnomericCarbon(a_oMS) ) return true;
		String t_strSkeletonCode = a_oMS.getSkeletonCode();
		return ( t_strSkeletonCode.contains("a") || t_strSkeletonCode.contains("o") || t_strSkeletonCode.contains("O") );
	}

	protected int getPotentialCarbonylPosition(MS a_oMS) {
		if ( !this.hasPotentialCarbonylGroup(a_oMS) ) return 0;
		if ( a_oMS.getAnomericPosition() > 0 ) return a_oMS.getAnomericPosition();
		String t_strSkeletonCode = a_oMS.getSkeletonCode();
		if ( t_strSkeletonCode.contains("o") ) return t_strSkeletonCode.indexOf("o")+1;
		if ( t_strSkeletonCode.contains("O") ) return t_strSkeletonCode.indexOf("O")+1;
		if ( t_strSkeletonCode.contains("u") ) return t_strSkeletonCode.indexOf("u")+1;
		if ( t_strSkeletonCode.contains("U") ) return t_strSkeletonCode.indexOf("U")+1;
		return -1;
	}

	/**
	 * Is anomeric symbol of MS unknown
	 * @param a_oMS Target MS
	 * @return True if anomeric symbol of MS is unknown, false if MS is open chain or anomeric symbol of MS is known
	 */
	protected boolean isAnomerUnknown(MS a_oMS) {
		if ( this.isOpenChain(a_oMS) ) return false;
		if ( a_oMS.getAnomericSymbol() == 'x' ) return true;
		return false;
	}

	// Ring check

	/**
	 * Get MOD as ring of MS which have anomeric position in LIP
	 * @param a_oMS Target MS
	 * @return MOD of ring modification (null if there is no ring or ring is not found)
	 */
	protected MOD getRingMOD(MS a_oMS) {
		if ( this.isOpenChain(a_oMS) )                return null;
		if ( this.hasUncertainAnomericCarbon(a_oMS) ) return null;

		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			if ( t_oMOD.getListOfLIPs().size() != 2 ) continue;
			for ( LIPs t_oLIPs : t_oMOD.getListOfLIPs() ) {
				if ( t_oLIPs.getLIPs().size() != 1 ) continue;
				if ( t_oLIPs.getLIPs().get(0).getBackbonePosition() != a_oMS.getAnomericPosition() ) continue;
				return t_oMOD;
			}
		}
		return null;
	}

	/**
	 * Does MS contain ring MOD
	 * @param a_oMS Target MS
	 * @return True if MS contain ring MOD
	 */
	protected boolean hasRing(MS a_oMS) {
		return ( this.getRingMOD(a_oMS) != null );
	}

	/**
	 * Does ring of MS have unknown position
	 * @param a_oMS Target MS
	 * @return True if the ring has unknown position, false if there is no ring or ring has not unknown position
	 */
	protected boolean isRingSizeUnknown(MS a_oMS) {
		if ( !this.hasRing(a_oMS) ) return false;

		for ( LIPs t_oRingLIPs : this.getRingMOD(a_oMS).getListOfLIPs() ) {
			for ( LIP t_oLIP : t_oRingLIPs.getLIPs() ) {
				if ( t_oLIP.getBackbonePosition() == -1 ) return true;
			}
		}
		return false;
	}

	// Configuration check

	/**
	 * Does MS have no stereo (no chiral carbon)
	 * @param a_oMS Target MS
	 * @return True if MS does not have stereo
	 */
	protected boolean hasNoStereo(MS a_oMS) {
		if ( this.hasUnknownConfiguration(a_oMS) ) return false;
		if ( this.hasRelativeConfiguration(a_oMS) ) return false;
		if ( this.hasAbsoluteConfiguration(a_oMS) ) return false;
		return true;
	}

	/**
	 * Is MS configuration unknown only
	 * @param a_oMS Target MS
	 * @return True if MS SkeletonCode contains unknown configuration only
	 */
	protected boolean hasUnknownConfiguration(MS a_oMS) {
		String t_oSkeletonCode = a_oMS.getSkeletonCode();
		for ( int i=0; i<t_oSkeletonCode.length(); i++ ) {
			char t_cCD = t_oSkeletonCode.charAt(i);
			if ( t_cCD == 'x' || t_cCD == 'X' ) return true;
		}
		return false;
	}

	/**
	 * Is MS configuration relative
	 * @param a_oMS Target MS
	 * @return True if MS SkeletonCode contains relative configuration
	 */
	protected boolean hasRelativeConfiguration(MS a_oMS) {
		String t_oSkeletonCode = a_oMS.getSkeletonCode();
		for ( int i=0; i<t_oSkeletonCode.length(); i++ ) {
			char t_cCD = t_oSkeletonCode.charAt(i);
			if ( t_cCD == '3' || t_cCD == '4' || t_cCD == '7' || t_cCD == '8' ) return true;
		}
		return false;
	}

	/**
	 * Count relative configuration in the MS
	 * @param a_oMS Target MS
	 * @return Number of relative configuration count
	 */
	protected int countRelativeConfiguration(MS a_oMS) {
		int t_nRelative = 0;
		String t_oSkeletonCode = a_oMS.getSkeletonCode();
		for ( int i=0; i<t_oSkeletonCode.length(); i++ ) {
			char t_cCD = t_oSkeletonCode.charAt(i);
			if ( t_cCD == '3' || t_cCD == '4' || t_cCD == '7' || t_cCD == '8' ) t_nRelative++;
		}
		return t_nRelative;
	}

	/**
	 * Is MS configuration absolute
	 * @param a_oMS Target MS
	 * @return True if MS SkeletonCode contains absolute configuration
	 */
	protected boolean hasAbsoluteConfiguration(MS a_oMS) {
		String t_oSkeletonCode = a_oMS.getSkeletonCode();
		for ( int i=0; i<t_oSkeletonCode.length(); i++ ) {
			char t_cCD = t_oSkeletonCode.charAt(i);
			if ( t_cCD == '1' || t_cCD == '2' || t_cCD == '5' || t_cCD == '6' ) return true;
		}
		return false;
	}

	/**
	 * Is MS configuration D.
	 * (If the last of CarbonDescriptor which has stereo is '2' or '6')
	 * <pre>
	 * a2122h-1a_1-5 -> true
	 * a2126h-1a_1-5 -> true
	 * a1211h-1a_1-5 -> false
	 * a4344h-1a_1-5 -> false
	 * axxxxh-1a_1-5 -> false
	 * </pre>
	 * @param a_oMS Target MS
	 * @return True if MS configuration is D, false if MS configuration is L or last stereo is fuzzy
	 */
	protected boolean isDConfiguration(MS a_oMS) {
		if ( !this.hasAbsoluteConfiguration(a_oMS) ) return false;
		if ( this.isFuzzyConfigurationForSC(a_oMS.getSkeletonCode()) ) return false;
		return this.isDConfigurationForSC(a_oMS.getSkeletonCode());
	}

	protected boolean isFuzzyConfigurationForSC(String a_strSC) {
		boolean t_bIsD = false;
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == '2' || t_cCD == '6' || t_cCD == '1' || t_cCD == '5' ) t_bIsD = false;
			if ( t_cCD == '3' || t_cCD == '4' || t_cCD == '7' || t_cCD == 'x' || t_cCD == 'X' ) t_bIsD = true;
		}
		return t_bIsD;
	}

	protected boolean isDConfigurationForSC(String a_strSC) {
		boolean t_bIsD = false;
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			if ( t_cCD == '2' || t_cCD == '6' ) t_bIsD = true;
			if ( t_cCD == '1' || t_cCD == '5' ) t_bIsD = false;
		}
		return t_bIsD;
	}

	// Core modification check

	/**
	 * Does MS have deoxy
	 * @param a_oMS TargetMS
	 * @return True if MS has deoxy (SkeletonCode contains deoxy CarbonDescriptor "d" or "m")
	 */
	protected boolean hasDeoxy(MS a_oMS) {
		String t_oSkeletonCode = a_oMS.getSkeletonCode();
		if ( t_oSkeletonCode.contains("d") || t_oSkeletonCode.contains("m") ) return true;
		if ( t_oSkeletonCode.contains("e") || t_oSkeletonCode.contains("z") || t_oSkeletonCode.contains("f") ) return true;
		return false;
	}

	/**
	 * Is MOD core modification
	 * TODO: need check
	 * @param a_oMOD Target MOD
	 * @return True if MOD is core modification
	 */
	public boolean isCoreMOD(MOD a_oMOD) {
		String t_strMAP = a_oMOD.getMAPCode();

		// For ring of core
		if ( t_strMAP.equals("") ) {
			if ( a_oMOD.getListOfLIPs().size() == 2 ) return true;
			return false;
		}

		int t_iMAPPos = a_oMOD.getListOfLIPs().getFirst().getLIPs().getFirst().getModificationPosition();
		if ( t_iMAPPos == 0 ) {
			if ( t_strMAP.length() < 2 ) return true;
			if ( t_strMAP.charAt(1) != 'O' ) return true;
			return false;
		}

		// For first atom
		int t_iPos = ( t_iMAPPos == 1 )? 1 : t_strMAP.lastIndexOf("*")-1;
		if ( t_strMAP.charAt(t_iPos) != 'O' ) return true;
		if ( a_oMOD.getListOfLIPs().size() == 1 ) return false;

		// For second atom
		t_iMAPPos = a_oMOD.getListOfLIPs().getLast().getLIPs().getFirst().getModificationPosition();
		t_iPos = ( t_iMAPPos == 1 )? 1 : t_strMAP.lastIndexOf("*")-1;
		if ( t_strMAP.charAt( t_iPos ) != 'O' ) return true;
		return false;
	}

	// Stereo check

	/**
	 * Extract stereo from the MS. The MS must be absolute or relative configuration.
	 * @param a_oMS Target MS
	 * @return List of stereo (null if there is no stereo or stereo contain unknown configuration or both of absolute and relative)
	 */
	public LinkedList<String> extractStereo(MS a_oMS) {
		LinkedList<String> t_aStereos = new LinkedList<String>();
		if ( this.hasNoStereo(a_oMS) || this.hasUnknownConfiguration(a_oMS) ) return t_aStereos;

		// Check configuration for absolute
		Boolean t_bIsD = null;
		MS t_oRelativeMS = a_oMS;
		if ( this.hasAbsoluteConfiguration(a_oMS) ) {
			if ( this.hasRelativeConfiguration(a_oMS) ) return t_aStereos;
			t_bIsD = this.isDConfiguration(a_oMS);

			// Convert to relative configuration
			t_oRelativeMS = new WURCSSubsumptionConverter().convertConfigurationAbsoluteToRelative(a_oMS);
		}

		// Extract stereo from relative configuration
		LinkedList<StereoBasetype> t_aStereoBasetypes = this.extractStereoBasetype( t_oRelativeMS.getSkeletonCode() );
		if ( t_aStereoBasetypes == null ) return null;

		for ( StereoBasetype t_oBasetype : t_aStereoBasetypes ) {
			String t_strStereo = ( t_oBasetype.isOpposite() )? "l/d-" : "d/l-";

			// For absolute configuration
			if ( t_bIsD != null ) {
				t_strStereo = ( t_bIsD )? "d" : "l";
				if ( t_oBasetype.isOpposite() )
					t_strStereo = ( t_bIsD )? "l" : "d";
			}

			t_strStereo += t_oBasetype.getThreeLetterCode();
			t_aStereos.addFirst(t_strStereo);
		}
		return t_aStereos;
	}

	/**
	 * Extract stereo basetype from the MS. The MS must be relative configuration only.
	 * @param a_strSC String of SkeletonCode
	 * @return List of StereoBasetype (null if relative configuration is unknown stereo basetype)
	 */
	public LinkedList<StereoBasetype> extractStereoBasetype(String a_strSC) {
		LinkedList<StereoBasetype> t_aBasetypes = new LinkedList<StereoBasetype>();

		String t_strBasetype = "";
		for ( int i=0; i<a_strSC.length(); i++ ) {
			char t_cCD = a_strSC.charAt(i);
			t_cCD = ( t_cCD == '7' )? '3' :
					( t_cCD == '8' )? '4' :
					t_cCD;
			if ( t_cCD == '3' || t_cCD == '4' ) t_strBasetype += t_cCD;

			if ( t_strBasetype.length() != 4 ) continue;

			StereoBasetype t_enumStereo = StereoBasetype.forStereoCode(t_strBasetype);
			if ( t_enumStereo == null ) return null;
			t_aBasetypes.add(t_enumStereo);

			t_strBasetype = "";
		}
		if ( !t_strBasetype.equals("") ) {
			StereoBasetype t_enumStereo = StereoBasetype.forStereoCode(t_strBasetype);
			if ( t_enumStereo == null ) return null;
			t_aBasetypes.add(t_enumStereo);
		}

		return t_aBasetypes;
	}

	/**
	 * TODO: Modify for core modification methods
	 */

	/**
	 *
	 * TODO: modify
	 * @param msRES
	 * @return
	 */
	protected boolean haveOtherMod(MS msRES) {
		boolean haveMod = false;

		for(MOD mod : msRES.getMODs()) {
			if(mod.getMAPCode().equals("")) continue;
			if(!this.generateMSwithCoreMod(msRES).contains(mod.getMAPCode())) return true;
		}
		return haveMod;
	}

	/**
	 *
	 * TODO: modify
	 * @param msRES
	 * @return
	 */
	protected boolean haveMOD(MS msRES) {
		for(MOD mod : msRES.getMODs()) {
			if(mod.getMAPCode().equals("") && !hasRing(msRES)) return true;
			if(!mod.getMAPCode().equals("")) return true;
		}
		return false;
	}

	/**
	 *
	 * TODO: modify
	 * @param msRES
	 * @param lst_Mod
	 * @param unknownPos
	 * @return
	 */
	protected LinkedList<MOD> selectCoreMOD(MS msRES, LinkedList<MOD> lst_Mod, boolean unknownPos) {
		LinkedList<MOD> ret = new LinkedList<MOD>();

		for(MOD mod : lst_Mod) {
			LinkedList<LIP> lnk_lips = new LinkedList<LIP>();
			if(mod.getMAPCode().equals("")) {
				if(!hasRing(msRES)) { //handle of unhydro structure
					lnk_lips.add(mod.getListOfLIPs().getFirst().getLIPs().getFirst());
					lnk_lips.add(mod.getListOfLIPs().getLast().getLIPs().getFirst());
				} else continue;
			}

			if(!mod.getMAPCode().equals("")) {
				if(unknownPos) lnk_lips.addLast(new LIP(-1, ' ', 0));
				else lnk_lips.addLast(mod.getListOfLIPs().getFirst().getLIPs().getFirst());
			}

			MOD modItem = new MOD(mod.getMAPCode());
			modItem.addLIPs(new LIPs(lnk_lips));
			ret.add(modItem);
		}
		return ret;
	}

	/**
	 *
	 * TODO: remove
	 * @param msRES
	 * @param un_pos
	 * @return
	 */
	protected MOD generateRingPos(MS msRES, boolean un_pos) {
		LinkedList<LIP> lip = new LinkedList<LIP>();
		boolean isRing = false;
		MOD ret = new MOD("");

		for(LIPs pos : msRES.getMODs().getFirst().getListOfLIPs()) {
			if(!isRing) {
				if(pos.getLIPs().size() != 1) continue;
				if(pos.getLIPs().getFirst().getBackbonePosition() != msRES.getAnomericPosition()) continue;
			}
			isRing = true;

			if(pos.getLIPs().getFirst().getBackbonePosition() > 2 && un_pos) lip.add(new LIP(-1, ' ', 0));
			else lip.add(new LIP(pos.getLIPs().getFirst().getBackbonePosition(), ' ', 0));
		}

		ret.addLIPs(new LIPs(lip));
		return ret;
	}

	/**
	 *
	 * TODO: modify
	 * @param msRES
	 * @return
	 */
	protected String generateMSwithCoreMod(MS msRES) {
		String ret = msRES.getSkeletonCode();

		if(this.haveMOD(msRES)) {
			for(MOD mod : msRES.getMODs()) {
				if(mod.getMAPCode().startsWith("*N")) ret += "_" + mod.getMAPCode();
			}
		}

		return ret;
	}


	/**
	 *
	 * @param msRES
	 * @return
	 */
	protected LinkedList<MS> generateMSlist(MS msRES) {
		LinkedList<MS> ret = new LinkedList<MS>();

		char anomerCymbol = msRES.getAnomericSymbol();
		int anomerPos = msRES.getAnomericPosition();
		String skeletonCode = msRES.getSkeletonCode();

		LinkedList<MOD> modList = new LinkedList<MOD>();

		for(MOD coreMOD : msRES.getMODs()) {
			if(coreMOD.getMAPCode().equals("")) {
				modList.add(coreMOD);
				continue;
			}
			if(this.generateMSwithCoreMod(msRES).contains(coreMOD.getMAPCode())) modList.add(coreMOD);
			break;
		}

		for(MOD mod : msRES.getMODs()) {
			if(mod.getMAPCode().equals("") && this.hasRing(msRES)) continue;

			MS unqItem = new MS(skeletonCode, anomerPos, anomerCymbol);
			for(Iterator<MOD> i = modList.iterator(); i.hasNext();) unqItem.addMOD(i.next());
			if(!this.generateMSwithCoreMod(msRES).contains(mod.getMAPCode())) unqItem.addMOD(mod);
			ret.add(unqItem);
		}
		return ret;
	}
}
