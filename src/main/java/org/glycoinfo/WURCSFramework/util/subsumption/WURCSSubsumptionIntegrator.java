package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
/**
 * @author st
 *
 */
public class WURCSSubsumptionIntegrator {

	private WURCSSubsumptionUtility sumeUtil = new WURCSSubsumptionUtility();
	
	/**
	 * Test utility
	 * @param unqRES
	 * @return
	 */
	public String getSubsumption(UniqueRES unqRES) {
		LinkedList<UniqueRES> msList = new LinkedList<UniqueRES>();
		
		if(!sumeUtil.haveOtherMod(unqRES)) msList.add(unqRES);
		else msList = sumeUtil.generateMSlist(unqRES);
	
		for(UniqueRES unqIndex : msList) {
			if(unqIndex.getAnomericSymbol() != ' ') {//have anomer
				if(!sumeUtil.haveRing(unqIndex)) {//have not ring position
					supersumeRing(unqIndex);
					removeRing(unqIndex);
				}else {//have ring position
					supersumeAnomer(unqIndex);
					removeRing(unqIndex);
				}
			}else if(!sumeUtil.haveRing(unqIndex)) {//have not ring and anomer position
				convertOpenChain(unqIndex);
			}
			
			if(sumeUtil.haveMOD(unqIndex)) {//have modification
				supersumeMOD(unqIndex);
				if(sumeUtil.haveOtherMod(unqIndex)) supersumeCoreMOD(unqIndex);
				supersumeChilarity(unqIndex);
				removeAnomer(unqIndex);
			}

			supersume(unqIndex);
			System.out.println("");
		}
		return "";
	}	

	/**
	 * Generate open chain structure from uniqueRes
	 * Open chain structure is describe as o1212h
	 * 
	 * @param unqRES
	 * @return
	 */
	public UniqueRES convertOpenChain(UniqueRES unqRES) {
		UniqueRES base = WURCSMonosaccharideIntegrator.convertBasetype(unqRES);
		String stereo = base.getSkeletonCode();
		
		if(stereo.contains("u")) stereo = stereo.replace('u', 'o');
		if(stereo.contains("U")) stereo = stereo.replace('U', 'O');
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, 0, 'x');
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);
		
		System.out.println("open : " + sumeUtil.outpurTest(ret));
		return ret;
	}

	/**
	 * If the SkeletonCode have clearly anomer information, change to ambiguous anomer position. 
	 * 12122h-1b_1-5_2*NCC/3=O -> x2122h_1x_1-5_2*NCC/3=O
	 *
	 * @param unqRES
	 * @param s_RingPos
	 * @return
	 */
	public UniqueRES supersumeAnomer(UniqueRES unqRES) {
		if(unqRES.getAnomericSymbol() == 'x') return unqRES;
		
		UniqueRES ss = WURCSMonosaccharideIntegrator.supersumes(unqRES);
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), ss.getSkeletonCode(), unqRES.getAnomericPosition(), 'x');
		if(sumeUtil.haveRing(unqRES)) ret.addMOD(sumeUtil.generateRingPos(unqRES, false));
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);

		System.out.println("unknown ano : " + sumeUtil.outpurTest(ret));

		return ret;
	}

	/**
	 * If the SkeletonCode have clearly ring information, change to ambiguous ring position. 
	 * 12122h-1b_1-5_2*NCC/3=O -> 12122h_1b_1-?_2*NCC/3=O
	 *
	 * @param unqRES
	 * @param s_RingPos
	 * @return
	 */
	public UniqueRES supersumeRing(UniqueRES unqRES) {
		String stereo = unqRES.getSkeletonCode();
		
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, unqRES.getAnomericPosition(), unqRES.getAnomericSymbol());
		if(sumeUtil.haveRing(unqRES)) ret.addMOD(sumeUtil.generateRingPos(unqRES, true));
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);

		System.out.println("unknown ring : " + sumeUtil.outpurTest(ret));

		return ret;
	}

	/**
	 * 
	 * @param unqRES
	 * @param s_RingPos
	 * @return
	 */
	public UniqueRES supersumeChilarity(UniqueRES unqRES) {
		String stereo = unqRES.getSkeletonCode();
		stereo = stereo.replaceAll("1", "3");
		stereo = stereo.replaceAll("2", "4");
		stereo = stereo.replaceAll("5", "7");
		stereo = stereo.replaceAll("6", "8");
		
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, unqRES.getAnomericPosition(), unqRES.getAnomericSymbol());
		if(sumeUtil.haveRing(unqRES)) ret.addMOD(sumeUtil.generateRingPos(unqRES, false));
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);
		
		System.out.println("unknown chi : " + sumeUtil.outpurTest(ret));
		
		return ret;
	}

	/**
	 * 
	 * @param unqRES
	 * @param s_RingPos
	 * @return
	 */
	public UniqueRES supersumeMOD(UniqueRES unqRES) {
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), unqRES.getSkeletonCode(), unqRES.getAnomericPosition(), unqRES.getAnomericSymbol());
		if(sumeUtil.haveRing(unqRES)) ret.addMOD(sumeUtil.generateRingPos(unqRES, false));
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), true)) ret.addMOD(mod);

		System.out.println("unknown mod : " + sumeUtil.outpurTest(ret));
		return ret;
	}

	/**
	 * 
	 * @param unqRES
	 * @param s_RingPos
	 * @return
	 */
	public UniqueRES supersumeCoreMOD(UniqueRES unqRES) {
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), unqRES.getSkeletonCode(), unqRES.getAnomericPosition(), unqRES.getAnomericSymbol());
		if(sumeUtil.haveRing(unqRES)) ret.addMOD(sumeUtil.generateRingPos(unqRES, false));
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) {
			if(WURCSBasetype.getBasetype(unqRES).contains(mod.getMAPCode())) ret.addMOD(mod);
			break;
		}
		
		System.out.println("remove mod : " + sumeUtil.outpurTest(ret));
		return ret;
	}
	
	/**
	 * Delete anomer and ring position in this UniqueRES
	 * @param unqRES
	 * @return
	 */
	public UniqueRES removeRing(UniqueRES unqRES) {
		String stereo = WURCSMonosaccharideIntegrator.convertBasetype(unqRES).getSkeletonCode();
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, 0, ' ');
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);

		System.out.println("remove ring : " + sumeUtil.outpurTest(ret));
		return ret;
	}
	
	/**
	 * 
	 * @param unqRES
	 * @return
	 */
	public UniqueRES removeAnomer(UniqueRES unqRES) {
		String stereo = WURCSMonosaccharideIntegrator.convertBasetype(unqRES).getSkeletonCode();
		stereo = stereo.replaceAll("1", "3");
		stereo = stereo.replaceAll("2", "4");
		stereo = stereo.replaceAll("5", "7");
		stereo = stereo.replaceAll("6", "8");

		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, 0, ' ');
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);

		System.out.println("remove ano : " + sumeUtil.outpurTest(ret));
		return ret;
	}
	
	/**
	 * 
	 * @param unqRES
	 * @return
	 */
	public UniqueRES supersume(UniqueRES unqRES) {
		String stereo = WURCSMonosaccharideIntegrator.convertBasetype(unqRES).getSkeletonCode();
		stereo = stereo.replaceAll("1", "3");
		stereo = stereo.replaceAll("2", "4");
		stereo = stereo.replaceAll("5", "7");
		stereo = stereo.replaceAll("6", "8");
		
		UniqueRES ret = new UniqueRES(unqRES.getUniqueRESID(), stereo, 0, ' ');
		for(MOD mod : sumeUtil.selectCoreMOD(unqRES, unqRES.getMODs(), false)) ret.addMOD(mod);

		System.out.println("subsume : " + sumeUtil.outpurTest(ret));
		return ret;
	}
}