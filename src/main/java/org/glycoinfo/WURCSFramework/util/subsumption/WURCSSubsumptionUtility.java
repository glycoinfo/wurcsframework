package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;

/**
 * Class for utility to generate WURCSSubsumption
 * @author st
 *
 */
public class WURCSSubsumptionUtility {
	
	/**
	 * 
	 * @param unqRES
	 * @return
	 */
	protected boolean haveRing(UniqueRES unqRES) {
		if(unqRES.getMODs().size() == 0) return false;
		for(MOD mod : unqRES.getMODs()) {
			if(mod.getMAPCode().equals("")) {
				if(unqRES.getAnomericPosition() == 0) return false;
				if(unqRES.getAnomericPosition() == mod.getListOfLIPs().getFirst().getLIPs().getFirst().getBackbonePosition()) return true;
			}else return false;
		}
		return false;
	}
	
	/**
	 * 
	 * @param unqRES
	 * @return
	 */
	protected boolean haveOtherMod(UniqueRES unqRES) {
		boolean haveMod = false;
		String basetype = WURCSBasetype.getBasetype(unqRES);

		for(MOD mod : unqRES.getMODs()) {
			if(mod.getMAPCode().equals("")) continue;
			if(!basetype.contains(mod.getMAPCode())) return true;
		}
		return haveMod;
	}

	/**
	 * 
	 * @param unqRES
	 * @return
	 */
	protected boolean haveMOD(UniqueRES unqRES) {
		for(MOD mod : unqRES.getMODs()) {
			if(mod.getMAPCode().equals("") && !haveRing(unqRES)) return true;
			if(!mod.getMAPCode().equals("")) return true;
		}
		return false;
	}

	/**
	 * 
	 * @param unqRES
	 * @param lst_Mod
	 * @param unknownPos
	 * @return
	 */
	protected LinkedList<MOD> selectCoreMOD(UniqueRES unqRES, LinkedList<MOD> lst_Mod, boolean unknownPos) {
		LinkedList<MOD> ret = new LinkedList<MOD>();

		for(MOD mod : lst_Mod) { 
			LinkedList<LIP> lnk_lips = new LinkedList<LIP>();
			if(mod.getMAPCode().equals("")) {
				if(!haveRing(unqRES)) { //handle of unhydro structure
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
	 * @param unqRES
	 * @param un_pos
	 * @return
	 */
	protected MOD generateRingPos(UniqueRES unqRES, boolean un_pos) {
		LinkedList<LIP> lip = new LinkedList<LIP>();
		boolean isRing = false;
		MOD ret = new MOD("");
		
		for(LIPs pos : unqRES.getMODs().getFirst().getListOfLIPs()) {
			if(!isRing) {
				if(pos.getLIPs().size() != 1) continue;
				if(pos.getLIPs().getFirst().getBackbonePosition() != unqRES.getAnomericPosition()) continue;	
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
	 * @param unqRES
	 * @return
	 */
	protected LinkedList<UniqueRES> generateMSlist(UniqueRES unqRES) {
		LinkedList<UniqueRES> ret = new LinkedList<UniqueRES>();
	
		int resID = unqRES.getUniqueRESID();
		char anomerCymbol = unqRES.getAnomericSymbol();
		int anomerPos = unqRES.getAnomericPosition();
		String skeletonCode = unqRES.getSkeletonCode();

		UniqueRES base = WURCSMonosaccharideIntegrator.supersumes(unqRES);
		String baseString = WURCSBasetype.getBasetype(unqRES);
		
		LinkedList<MOD> modList = new LinkedList<MOD>();
		
		//extract core modification and ring position
		for(MOD coreMOD : base.getMODs()) {
			if(coreMOD.getMAPCode().equals("")) { 
				modList.add(coreMOD);
				continue;
			}
			if(baseString.contains(coreMOD.getMAPCode())) modList.add(coreMOD);
			break;
		}
		
		for(MOD mod : base.getMODs()) {
			if(mod.getMAPCode().equals("") && this.haveRing(base)) continue;
			
			UniqueRES unqItem = new UniqueRES(resID, skeletonCode, anomerPos, anomerCymbol);
			for(Iterator<MOD> i = modList.iterator(); i.hasNext();) unqItem.addMOD(i.next());
			if(!baseString.contains(mod.getMAPCode())) unqItem.addMOD(mod);
			ret.add(unqItem);
		}	
		return ret;
	}
	
	//is debug utility 
	//It will delete
	protected String outpurTest(UniqueRES unqRES) {
		String ret = "";

		ret += unqRES.getSkeletonCode();
		if(unqRES.getAnomericSymbol() != ' ' && unqRES.getAnomericPosition() > 0)
			ret += "_" +  unqRES.getAnomericPosition() + unqRES.getAnomericSymbol();
		if(unqRES.getMODs().size() > 0) {
			for(MOD mod : unqRES.getMODs()) {
				if(mod.getMAPCode().equals("")) {
					ret += "_" + mod.getListOfLIPs().getFirst().getLIPs().getFirst().getBackbonePosition();
					ret += "-" + mod.getListOfLIPs().getFirst().getLIPs().getLast().getBackbonePosition();
					continue;
				}
				String pos = mod.getListOfLIPs().getFirst().getLIPs().getFirst().getBackbonePosition() == -1 ? "?" : String.valueOf(mod.getListOfLIPs().getFirst().getLIPs().getFirst().getBackbonePosition());
				ret += "_" + pos + mod.getMAPCode();
			}
		}
		return ret;
	}

}
