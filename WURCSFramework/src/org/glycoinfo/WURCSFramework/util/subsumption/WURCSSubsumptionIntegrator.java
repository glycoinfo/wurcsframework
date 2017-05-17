package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;
/**
 * Class for generating subsumption informations using MS
 * @author ShinichiroTsuchiya
 * @author MasaakiMatsubara
 *
 */
public class WURCSSubsumptionIntegrator {

	private MSStateDeterminationUtility m_oMSStateUtil = new MSStateDeterminationUtility();

	/**
	 * Test utility
	 * TODO: remove old method
	 * @param msRES
	 * @return
	 */
	public String getSubsumption(MS msRES) {
		LinkedList<MS> msList = new LinkedList<MS>();

		this.m_oMSStateUtil.generateMSwithCoreMod(msRES);

		if(!m_oMSStateUtil.haveOtherMod(msRES)) msList.add(msRES);
		else msList = m_oMSStateUtil.generateMSlist(msRES);

		for(MS unqIndex : msList) {
			if(unqIndex.getAnomericSymbol() != ' ') {//have anomer
				if(!m_oMSStateUtil.hasRing(unqIndex)) {//have not ring position
					supersumeRing(unqIndex);
					removeRing(unqIndex);
				}else {//have ring position
					supersumeAnomer(unqIndex);
					removeRing(unqIndex);
				}
			}else if(!m_oMSStateUtil.hasRing(unqIndex)) {//have not ring and anomer position
				getOpenChain(unqIndex);
			}

			if(m_oMSStateUtil.haveMOD(unqIndex)) {//have modification
				supersumeMOD(unqIndex);
				if(m_oMSStateUtil.haveOtherMod(unqIndex)) supersumeCoreMOD(unqIndex);
				supersumeChilarity(unqIndex);
				removeAnomer(unqIndex);
			}
		}

		return "";
	}

	/**
	 * Generate open chain structure from uniqueRes<br>
	 * Open chain structure is described as o1212h<br>
	 * TODO: remove old method
	 * @param a_oMS
	 * @return
	 */
	public MS getOpenChain(MS a_oMS) {
		int t_iAnomPos = a_oMS.getAnomericPosition();


		// For open chain and not contain uncertain anomeric carbon
		if ( t_iAnomPos == MS.OPEN_CHAIN && !this.m_oMSStateUtil.hasUncertainAnomericCarbon(a_oMS) )
			return a_oMS;
		// For unknown anomeric position
		if ( t_iAnomPos == MS.UNKNOWN_POSITION ) {
			MS t_oNewMS = new MS(a_oMS.getSkeletonCode(), 0, 'o' );
			for ( MOD mod : a_oMS.getMODs() )
				t_oNewMS.addMOD(mod);
			return t_oNewMS;
		}

		// Set CarbonDescriptor for open chain
		String t_strOpenCD = "o";	// For terminal
		if ( t_iAnomPos != 1 && t_iAnomPos != a_oMS.getSkeletonCode().length() )
			t_strOpenCD = "O";		// For non terminal

		// Replace CarbonDescriptor at  anomeric position
		StringBuilder t_sbSC = new StringBuilder(a_oMS.getSkeletonCode());
		t_sbSC.replace(t_iAnomPos-1, t_iAnomPos, t_strOpenCD);

		MS t_oOpenMS = new MS(t_sbSC.toString(), 0, 'o');
		MOD t_oRingMod = m_oMSStateUtil.getRingMOD(a_oMS);
		for ( MOD mod : a_oMS.getMODs() ) {
			if ( mod.equals(t_oRingMod) ) continue;
			t_oOpenMS.addMOD(mod);
		}

		return t_oOpenMS;
	}

	/**
	 * Change anomeric position to ambiguous if the SkeletonCode have exact anomeric information.
	 * <pre>
	 * 12122h-1b_1-5_2*NCC/3=O -> x2122h-1x_1-5_2*NCC/3=O
	 * </pre>
	 * TODO: remove old method
	 * @param msRES
	 * @return
	 */
	public MS supersumeAnomer(MS msRES) {
		if(msRES.getAnomericSymbol() == 'x') return msRES;

		//UniqueRES ss = WURCSMonosaccharideIntegrator.supersumes(unqRES);
		MS ret = new MS(msRES.getSkeletonCode(), msRES.getAnomericPosition(), 'x');
		if(m_oMSStateUtil.hasRing(msRES)) ret.addMOD(m_oMSStateUtil.generateRingPos(msRES, false));
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * If the SkeletonCode have clearly ring information, change to ambiguous ring position.
	 * <pre>
	 * 12122h-1b_1-5_2*NCC/3=O -> 12122h_1b_1-?_2*NCC/3=O
	 * </pre>
	 * TODO: remove old method
	 * @param msRES
	 * @param s_RingPos
	 * @return
	 */
	public MS supersumeRing(MS msRES) {
		String stereo = msRES.getSkeletonCode();

		MS ret = new MS(stereo, msRES.getAnomericPosition(), msRES.getAnomericSymbol());
		if(m_oMSStateUtil.hasRing(msRES)) ret.addMOD(m_oMSStateUtil.generateRingPos(msRES, true));
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * TODO: remove old method
	 * @param msRES
	 * @param s_RingPos
	 * @return
	 */
	public MS supersumeChilarity(MS msRES) {
		String stereo = msRES.getSkeletonCode();
		stereo = stereo.replaceAll("1", "3");
		stereo = stereo.replaceAll("2", "4");
		stereo = stereo.replaceAll("5", "7");
		stereo = stereo.replaceAll("6", "8");

		MS ret = new MS(stereo, msRES.getAnomericPosition(), msRES.getAnomericSymbol());
		if(m_oMSStateUtil.hasRing(msRES)) ret.addMOD(m_oMSStateUtil.generateRingPos(msRES, false));
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * TODO: remove old method
	 * @param msRES
	 * @param s_RingPos
	 * @return
	 */
	public MS supersumeMOD(MS msRES) {
		MS ret = new MS(msRES.getSkeletonCode(), msRES.getAnomericPosition(), msRES.getAnomericSymbol());
		if(m_oMSStateUtil.hasRing(msRES)) ret.addMOD(m_oMSStateUtil.generateRingPos(msRES, false));
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), true)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * TODO: remove old method
	 * @param msRES
	 * @param s_RingPos
	 * @return
	 */
	public MS supersumeCoreMOD(MS msRES) {
		MS ret = new MS(msRES.getSkeletonCode(), msRES.getAnomericPosition(), msRES.getAnomericSymbol());
		if(m_oMSStateUtil.hasRing(msRES)) ret.addMOD(m_oMSStateUtil.generateRingPos(msRES, false));
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) {
			if(this.m_oMSStateUtil.generateMSwithCoreMod(msRES).contains(mod.getMAPCode())) ret.addMOD(mod);
			break;
		}

		return ret;
	}

	/**
	 * Delete anomer and ring position in this UniqueRES
	 * TODO: remove old method
	 * @param msRES
	 * @return
	 */
	public MS removeRing(MS msRES) {
		MS ret = new MS(msRES.getSkeletonCode(), 0, ' ');
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * TODO: remove old method
	 * @param msRES
	 * @return
	 */
	public MS removeAnomer(MS msRES) {
		String stereo = msRES.getSkeletonCode();
//		stereo = stereo.replaceAll("1", "3");
//		stereo = stereo.replaceAll("2", "4");
//		stereo = stereo.replaceAll("5", "7");
//		stereo = stereo.replaceAll("6", "8");

		MS ret = new MS(stereo, 0, ' ');
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}

	/**
	 * TODO: remove old method
	 * @param msRES
	 * @return
	 */
	public MS supersume(MS msRES) {
		String stereo = msRES.getSkeletonCode();
		// Convert D/L unknown
		stereo = stereo.replaceAll("1", "3");
		stereo = stereo.replaceAll("2", "4");
		stereo = stereo.replaceAll("5", "7");
		stereo = stereo.replaceAll("6", "8");

		MS ret = new MS(stereo, 0, ' ');
		for(MOD mod : m_oMSStateUtil.selectCoreMOD(msRES, msRES.getMODs(), false)) ret.addMOD(mod);

		return ret;
	}


	/**
	 * Standardize stereo in SkeletonCode to fuzzy
	 * @param a_oMS Target MS
	 * @return Standardized MS (null if there is no stereo or already standardized)
	 */
	public LinkedList<MS> standardizeStereoToFuzzy(MS a_oMS) {
		LinkedList<MS> t_aMSs = new LinkedList<MS>();
		t_aMSs.add(a_oMS);

		MSStateDeterminationUtility t_oState = new MSStateDeterminationUtility();
		if ( t_oState.hasNoStereo(a_oMS) ) return t_aMSs;

/*
		// Standardize to unknown
		if ( t_oState.hasUnknownConfiguration(a_oMS) ) {
			MS t_oUnk = t_oConv.convertCofigurationToUnknown(a_oMS);
			if ( t_oUnk == null ) return t_aMSs;

			t_aMSs = new LinkedList<MS>();
			t_aMSs.add( t_oUnk );
			return t_aMSs;
		}
*/
		if ( !t_oState.hasRelativeConfiguration(a_oMS) ||
			 !t_oState.hasAbsoluteConfiguration(a_oMS) ) return t_aMSs;

		// Standardize to relative if absolute contains
		WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();
		t_aMSs = new LinkedList<MS>();
		MS t_oAbsD = t_oConv.convertConfigurationRelativeToD(a_oMS);
		MS t_oAbsL = t_oConv.convertConfigurationRelativeToL(a_oMS);
		t_aMSs.add( t_oConv.convertConfigurationAbsoluteToRelative(t_oAbsD) );
		t_aMSs.add( t_oConv.convertConfigurationAbsoluteToRelative(t_oAbsL) );
		return t_aMSs;
	}

	/**
	 * Standardize stereo in SkeletonCode to exact
	 * @param a_oMS Target MS
	 * @return Standardized MS (itself only if already standard)
	 */
	public LinkedList<MS> standardizeStereoToExact(MS a_oMS) {
		MSStateDeterminationUtility t_oState = new MSStateDeterminationUtility();

		LinkedList<MS> t_aMSs = new LinkedList<MS>();
		t_aMSs.add(a_oMS);
		if ( t_oState.hasNoStereo(a_oMS) ) return t_aMSs;
		WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();

		if ( t_oState.hasAbsoluteConfiguration(a_oMS) ) {
			if ( !t_oState.hasRelativeConfiguration(a_oMS) &&
				 !t_oState.hasUnknownConfiguration(a_oMS) ) return t_aMSs;

			// Convert absolute to relative if relative and absolute is mixed
			if ( t_oState.hasRelativeConfiguration(a_oMS) ) {
				t_aMSs = new LinkedList<MS>();
				t_aMSs.add( t_oConv.convertConfigurationRelativeToD(a_oMS) );
				t_aMSs.add( t_oConv.convertConfigurationRelativeToL(a_oMS) );
			}

			if ( !t_oState.hasUnknownConfiguration(a_oMS) ) return t_aMSs;

			// Convert unknown to absolute if unknown and absolute is mixed
			LinkedList<MS> t_aTmpMSs = new LinkedList<MS>();
			for ( MS t_oMS : t_aMSs )
				t_aTmpMSs.addAll( t_oConv.convertConfigurationUnknownToAbsolutes(t_oMS) );

			return t_aTmpMSs;
		}

		if ( !t_oState.hasUnknownConfiguration(a_oMS) ) return this.standardizeStereoToFuzzy(a_oMS);

		// Convert unknown to relative if unknown and relative is mixed
		t_aMSs = new LinkedList<MS>();
		WURCSExporter t_oExport = new WURCSExporter();
		LinkedList<String> t_aMSStrings = new LinkedList<String>();
		// unknown to absolute
		for ( MS t_oMS : t_oConv.convertConfigurationUnknownToAbsolutes(a_oMS) ) {
			// Standardize MS that relative and absolute is mixed
			for ( MS t_oStandardMS : this.standardizeStereoToFuzzy(t_oMS) ) {
				// Duplicate check
				if ( t_aMSStrings.contains( t_oExport.getMSString( t_oStandardMS ) ) ) continue;
				t_aMSStrings.add( t_oExport.getMSString( t_oStandardMS ) );

				t_aMSs.add( t_oStandardMS );
			}
		}

		return t_aMSs;
	}

	/**
	 * Generate supersumed MSs from target MS
	 * @param a_oMS Target MS
	 * @return List of supersumed MSs of target MS
	 */
	public LinkedList<MS> makeSupersumedAllMSs(MS a_oMS) {
		// Correct MS
		WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();
		MS t_oCorrectMS = t_oConv.correctRelativeConfiguration(a_oMS);
		if ( t_oCorrectMS == null )
			t_oCorrectMS = a_oMS;

		LinkedList<MS> t_aSubsumedMSs = new LinkedList<MS>();
		t_aSubsumedMSs.add(t_oCorrectMS);

		LinkedList<MS> t_aStanderdMSs = this.standardizeStereoToFuzzy(t_oCorrectMS);
		if ( t_aStanderdMSs != null )
			t_aSubsumedMSs = t_aStanderdMSs;

		WURCSExporter t_oExport = new WURCSExporter();
		LinkedList<String> t_aSubsumedMSStrings = new LinkedList<String>();
		for ( MS t_oMS : t_aSubsumedMSs ) {
			t_aSubsumedMSStrings.add(t_oExport.getMSString(t_oMS));
		}

		// TODO: Make subsumes by not core substituents
		// Make ring position unknown
		LinkedList<MS> t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertUnknownRingSize(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		// Make anomeric symbol unknown
		t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertUnknownAnomer(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		// Make ring/chain unknown
		t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertAnomericCarbonToUncertain(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		// Make D/L unknown
		t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertConfigurationAbsoluteToRelative(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		// Make all configuration unknown
		t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertConfigurationToUnknown(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		return t_aSubsumedMSs;
	}

	public LinkedList<MS> makeSubsumedMSs(MS a_oMS) {
		LinkedList<MS> t_aSubsumedMSs = new LinkedList<MS>();
		t_aSubsumedMSs.add(a_oMS);

		WURCSExporter t_oExport = new WURCSExporter();
		LinkedList<String> t_aSubsumedMSStrings = new LinkedList<String>();
		t_aSubsumedMSStrings.add(t_oExport.getMSString(a_oMS));

		WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();
		// TODO: Make subsumes
		// Make all configuration unknown
		LinkedList<MS> t_aNewMSs = new LinkedList<MS>();
		for ( MS t_oTargetMS : t_aSubsumedMSs ) {
			MS t_oSuperMS = t_oConv.convertConfigurationToUnknown(t_oTargetMS);
			if ( t_oSuperMS == null ) continue;

			String t_strSuperMS = t_oExport.getMSString(t_oSuperMS);
			if ( t_aSubsumedMSStrings.contains(t_strSuperMS) ) continue;
			t_aNewMSs.add(t_oSuperMS);
			t_aSubsumedMSStrings.add(t_strSuperMS);
		}
		t_aSubsumedMSs.addAll(t_aNewMSs);

		return t_aSubsumedMSs;
	}

	public MS makeMSComposition(MS a_oMS) {
		MS t_oComposition = a_oMS;

		WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();

		// Make all configuration unknown
		t_oComposition = t_oConv.convertConfigurationToUnknown(t_oComposition);
		if ( t_oComposition == null ) return null;

		// Change style of deoxy if MS contains deoxy
		if ( this.m_oMSStateUtil.hasDeoxy(t_oComposition) ) {
			t_oComposition = t_oConv.moveDeoxyToMOD(t_oComposition);
			t_oComposition = t_oConv.convertMODPositionToUnknown(t_oComposition);
		}

		return t_oComposition;
	}

	// TODO: Modify or remove following static methods in WURCSMonosaccharideIntegrator

	/**
	 * Convert UniqueRES to anobase
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of anobase
	 */
	public static MS convertAnobase(MS a_oURES){
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();
		char   t_cAnomSymbol     = a_oURES.getAnomericSymbol();

		// For anomeric
		if ( t_iAnomPos != 0 )
			t_strSkeletonCode = replaceAnomericCarbonDescriptorToUndef(t_strSkeletonCode, t_iAnomPos);

		MS t_oAnobaseURES = new MS(t_strSkeletonCode, t_iAnomPos, t_cAnomSymbol);
		// Add trimed modifications
		for ( MOD t_oMOD : extractCoreModifications(a_oURES) )
			t_oAnobaseURES.addMOD(t_oMOD);

		return t_oAnobaseURES;
	}

	/**
	 * Convert to supersum
	 * @param a_oMS Subsumes of monosaccharide
	 * @return Supersumed UniqueRES
	 */
	public static MS supersumes(MS a_oMS) {
		String t_strSkeletonCode = a_oMS.getSkeletonCode();
		int    t_iAnomPos        = a_oMS.getAnomericPosition();
		char   t_cAnomSymbol     = a_oMS.getAnomericSymbol();

		if ( t_iAnomPos == 0 ) {
			t_strSkeletonCode = replaceAnomericCarbonDescriptorToUndef(t_strSkeletonCode, t_iAnomPos);
		} else if ( t_cAnomSymbol == 'x' ) {
			t_strSkeletonCode = replaceAnomericCarbonDescriptorToUndef(t_strSkeletonCode, t_iAnomPos);
			t_iAnomPos = 0;
		} else {
			t_strSkeletonCode = replaceAnomericCarbonDescriptorToUnknown(t_strSkeletonCode, t_iAnomPos);
		}

		MS t_oSupersumedMS = new MS(t_strSkeletonCode, t_iAnomPos, 'x');
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			boolean isAnomRing = false;
			for (LIPs t_oLIPs : t_oMOD.getListOfLIPs() ) {
				if ( t_oLIPs.getLIPs().size() != 1 ) continue;
				if ( t_oLIPs.getLIPs().getFirst().getBackbonePosition() != a_oMS.getAnomericPosition() ) continue;
				isAnomRing = true;
				break;
			}
			if ( t_oMOD.getListOfLIPs().size() == 2 && isAnomRing && t_cAnomSymbol == 'x' ) continue;
			t_oSupersumedMS.addMOD(t_oMOD);
		}

		return t_oSupersumedMS;
	}

	/**
	 * Convert UniqueRES to basetype
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of basetype
	 */
	public static MS convertBasetype(MS a_oURES) {
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();

		if ( t_iAnomPos != -1 )
			t_strSkeletonCode = replaceAnomericCarbonDescriptorToUndef(t_strSkeletonCode, t_iAnomPos);

		MS t_oBasetypeURES = new MS(t_strSkeletonCode, 0, 'x');
		// Add trimed modifications
		for ( MOD t_oMOD : extractCoreModifications(a_oURES) )
			t_oBasetypeURES.addMOD(t_oMOD);

		return t_oBasetypeURES;
	}

	/**
	 * Convert UniqueRES to reduced form
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of reduced form
	 */
	public static MS convertReducedForm(MS a_oURES) {
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();

		t_strSkeletonCode = replaceAnomericCarbonDescriptorToUndef(t_strSkeletonCode, t_iAnomPos);
		int pos = ( t_iAnomPos > 0 )? t_iAnomPos-1 :
				  ( t_strSkeletonCode.contains("u") )? t_strSkeletonCode.indexOf("u") :
				  ( t_strSkeletonCode.contains("U") )? t_strSkeletonCode.indexOf("U") : 0;
		StringBuilder t_sbReducedForm = new StringBuilder( t_strSkeletonCode );
		t_sbReducedForm.replace(pos, pos+1, ( pos == 0 )? "h" : "X");

		MS t_oReducedForm = new MS(t_strSkeletonCode, 0, 'x');
		for ( MOD t_oMOD : a_oURES.getMODs() )
			t_oReducedForm.addMOD(t_oMOD);

		return t_oReducedForm;
	}


	/**
	 * Extract core modifications in UniqueRES
	 * @param a_oURES UniqueRES
	 * @return List of core modifications
	 */
	private static LinkedList<MOD> extractCoreModifications(MS a_oURES){

		LinkedList<MOD> t_aCoreModifs = new LinkedList<MOD>();
		for (MOD mod : a_oURES.getMODs()) {
			// Remove modification of a hydrogen on OH groups.
			// Remove ring modification
			if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") && !mod.getMAPCode().startsWith("*=O") )
				t_aCoreModifs.add(mod);
		}
		return t_aCoreModifs;
	}

	/**
	 * Replace anomeric CarbonDescriptor for basetype and anobase
	 * @param a_oURES UniqueRES contained target SkeletonCode
	 * @return String of replaced SkeletonCode
	 */
	private static String replaceAnomericCarbonDescriptorToUndef(String a_strSkeletonCode, int a_iAnomPos){

		// Return if anomeric info is already replaced
		if ( a_strSkeletonCode.contains("u") || a_strSkeletonCode.contains("U") )
			return a_strSkeletonCode;

		StringBuilder  t_sbBasetype = new StringBuilder( a_strSkeletonCode );

		// For unknown sugar
		if ( a_iAnomPos == -1 )
			a_iAnomPos = 0;

		// For ring
		if ( a_iAnomPos != 0 ) {
			t_sbBasetype.replace(a_iAnomPos-1, a_iAnomPos, ( a_iAnomPos == 1 )? "u" : "U");
			return t_sbBasetype.toString();
		}

		// For open chain

		// Replase "first" aldo or keto symbol "o"/"O" in open chain
		if ( a_strSkeletonCode.contains("o") || a_strSkeletonCode.contains("O") ) {
			int pos = a_strSkeletonCode.indexOf('o');
			if ( pos == -1 ) pos = a_strSkeletonCode.indexOf('O');
			// CarbonDescriptor for keto to be "U"
			t_sbBasetype.replace(pos, pos+1, ( pos == 0 )? "u" : "U");
			return t_sbBasetype.toString();
		}

		// Return if reduced (no anomeric position e.g. alditol)
		return a_strSkeletonCode;
	}

	private static String replaceAnomericCarbonDescriptorToUnknown(String a_strSkeletonCode, int a_iAnomPos){

		// Return if anomeric info is already replaced
		if ( a_strSkeletonCode.contains("u") || a_strSkeletonCode.contains("U") )
			return a_strSkeletonCode;

		StringBuilder  t_sbBasetype = new StringBuilder( a_strSkeletonCode );

		// For unknown sugar
		if ( a_iAnomPos == -1 )
			a_iAnomPos = 0;

		// For ring
		if ( a_iAnomPos != 0 ) {
			t_sbBasetype.replace(a_iAnomPos-1, a_iAnomPos, ( a_iAnomPos == 1 )? "x" : "X");
			return t_sbBasetype.toString();
		}

		// For open chain
		// Return if reduced (no anomeric position e.g. alditol)
		return a_strSkeletonCode;
	}

}