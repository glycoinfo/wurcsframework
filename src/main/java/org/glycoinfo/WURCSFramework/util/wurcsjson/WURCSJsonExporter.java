package org.glycoinfo.WURCSFramework.util.wurcsjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class for output Json style string from WURCS 
 * @author st
 */
public class WURCSJsonExporter {
	
	/**
	 * 
	 * @param o_arrWURCS
	 * @return
	 * @throws Exception
	 */
	public JSONArray generateWURCSJSON(String a_strWURCS) throws Exception {
		//generate WURCS sequence
		WURCSImporter wi = new WURCSImporter();
		WURCSArray o_arrWURCS = wi.extractWURCSArray(a_strWURCS);
		WURCSArrayToSequence was = new WURCSArrayToSequence();
		was.start(o_arrWURCS);
		WURCSSequence o_seqWURCS = was.getSequence();

		//TODO : How to extract(or defined) as root node in ambiguous structure from WURCSSequence
		HashMap<Integer, WURCSAntennaPoint> raList = extractAmbiguousNode(o_seqWURCS);
		
		/** return WURCSJson string */
		return readWURCS(o_seqWURCS, o_arrWURCS, raList);
	}
	
	/**
	 * Read WURCS strig in WURCSSequence and convert Json string 
	 * @param o_seqWURCS is WURCSSequence
	 * @param o_arrWURCS is WURCSArray
	 * @param raList
	 * @return
	 */
	private JSONArray readWURCS(WURCSSequence o_seqWURCS, WURCSArray o_arrWURCS, HashMap<Integer, WURCSAntennaPoint> raList) {
		JSONArray ret = new JSONArray();
		
		for(GRES gres : o_seqWURCS.getGRESs()) {
			JSONObject SkeletonJson = new JSONObject();
			ArrayList<String> modList = new ArrayList<String>();
			
			int residueNum = gres.getID();
			SkeletonJson.put("ID", residueNum);
			SkeletonJson.put("Index", WURCSDataConverter.convertRESIDToIndex(residueNum));
			
			//define ambiguous structure node
			if(raList.containsKey(residueNum))
				if(raList.get(residueNum).isAmbigous()) SkeletonJson.put("anotate", "isAmbigous");
			
			int uniqueResID = o_arrWURCS.getRESs().get(residueNum - 1).getUniqueRESID();
			
			//define basetype
			new WURCSBasetype();
			String basetype = WURCSBasetype.getBasetype(o_arrWURCS.getUniqueRESs().get(uniqueResID - 1));
			SkeletonJson.put("MS", gres.getMS().getString());
			
			//define anomer position
			char anomerSymbol = o_arrWURCS.getUniqueRESs().get(uniqueResID - 1).getAnomericSymbol();
			int anomerPos = o_arrWURCS.getUniqueRESs().get(uniqueResID - 1).getAnomericPosition();
			SkeletonJson.put("anomerCymbol", String.valueOf(anomerSymbol));
			SkeletonJson.put("anomerPosition", anomerPos);
			
			
			//define MOD in SkeletonCode
			LinkedList<MOD> currentMOD = o_arrWURCS.getUniqueRESs().get(uniqueResID - 1).getMODs();	
			
			//define monosaccharide super class
			WURCSSuperClass wsc = WURCSSuperClass.getSuperClass(String.valueOf(basetype.length()));

			//extract modification in basetype
			SkeletonCodeModTrimmer baseTrim = new SkeletonCodeModTrimmer();
			
			//generate sugar name from basetype
			WURCSstereoTemplate wst = WURCSstereoTemplate.getBaseType(basetype);
		
			//check modification in stereo
			if(wst == null) {
				basetype = baseTrim.checkSkeletonModification(basetype);
				if(baseTrim.hasModification()) modList = baseTrim.getModificationList(); 
				wst = WURCSstereoTemplate.getBaseType(basetype);
			}
			if(wst == null) {
				System.out.println(o_seqWURCS.getWURCS());
				System.out.println(basetype);
				System.out.println(WURCSBasetype.getBasetype(o_arrWURCS.getUniqueRESs().get(residueNum - 1)) + " : this monosaccharide is not defined in WURCSBaseType");
				return new JSONArray();
			}
			
			//
			SkeletonJson.put("Basetype", basetype);
			
			//define sugar name as IUPAC
			String sugaName = baseTrim.hasModification() ? wst.getSugarName() + wsc.getSuperClass() : wst.getSugarName();
			SkeletonJson.put("SugarName", sugaName);
			
			//extract modification including ringPos and generate native substituent structure
			SkeletonJson.put("modification", extractModification(currentMOD, modList, wst));
			
			//Donar is indicate child node/Acceptor is indicate parent
			//Acceptor is handle as current node			
			SkeletonJson.put("Linkage", extractGLIN(gres));

			ret.add(SkeletonJson);
		}
		
		return ret;
	}
	
	/**
	 * Define a linkage information and repeating structure in this node and child position.
	 * Parent position is extract from acceptor,
	 * Child position is extract from donor.
	 * @param a_objGRES is GRES
	 * @return
	 */
	private JSONObject extractGLIN(GRES a_objGRES) {
		JSONObject ret = new JSONObject();
		ArrayList<String> child = new ArrayList<String>();
		ArrayList<LinkedList<Integer>> donor = new ArrayList<LinkedList<Integer>>();
		ArrayList<LinkedList<Integer>> acceptor = new ArrayList<LinkedList<Integer>>();
		
		for(GLIN aGLIN : a_objGRES.getAcceptorGLINs()) {
			if(!aGLIN.isRepeat()) {
				donor.add(aGLIN.getDonorPositions());
				acceptor.add(aGLIN.getAcceptorPositions());
				for(MS childMS : aGLIN.getDonorMSs()) child.add(childMS.getString());
				continue;
			}
			
			//extract repeating imformation
			if(aGLIN.getRepeatCountMax() != 0) {
				JSONObject repeatEnd = new JSONObject();
				repeatEnd.put("start", aGLIN.getDonorMSs().getFirst().getString());
				repeatEnd.put("min", aGLIN.getRepeatCountMin());
				repeatEnd.put("max", aGLIN.getRepeatCountMax());
				repeatEnd.put("bridge", aGLIN.getMAP());
				repeatEnd.put("pDonor", aGLIN.getDonorPositions());
				repeatEnd.put("pAcceptor", aGLIN.getAcceptorPositions());
				repeatEnd.put("pos", "end");
				ret.put("repeat", repeatEnd);					
			}
		}
		
		ret.put("child", child);
		ret.put("pDonor", donor);
		ret.put("pAcceptor", acceptor);
		
		System.out.println("===>" + ret);
		
		for(GLIN cGLIN : a_objGRES.getDonorGLINs()) {
			if(cGLIN.getRepeatCountMax() != 0) {
				JSONObject repeatStart = new JSONObject();
				repeatStart.put("pos", "start");
				repeatStart.put("cDonor", cGLIN.getDonorPositions());
				repeatStart.put("cAcceptor", cGLIN.getAcceptorPositions());
				repeatStart.put("end", cGLIN.getAcceptorMSs().getFirst().getString());
				ret.put("repeat", repeatStart);
			}
			
			if(!cGLIN.isRepeat()) {
				ret.put("cDonor", cGLIN.getDonorPositions());
				ret.put("cAcceptor", cGLIN.getAcceptorPositions());
			}
		}
		
		return ret;
	}
	
	/**
	 * Extract modification from current residue
	 * These modification are defined as "modification" in JsonObject
	 * @param a_lstMOD is LinkedList<MOD> from WURCSArray
	 * @param a_arrMOD is ArrayList<String> of modification in current residue.
	 * @param wst is WURCSstereoTemplate
	 * @return
	 */
	private JSONObject extractModification(LinkedList<MOD> a_lstMOD, ArrayList<String> a_arrMOD, WURCSstereoTemplate wst) {
		JSONObject ret = new JSONObject();
		
		for(MOD mod : a_lstMOD) {
			//ring pos
			if(mod.getMAPCode().equals("")) {
				StringBuilder ringPos = new StringBuilder();
				for(LIPs lip : mod.getListOfLIPs()) ringPos.append(lip.getLIPs().getFirst().getBackbonePosition());
				ret.put("rings", ringPos);
				continue;
			}
			
			//substituent
			if(!wst.getBaseType().contains(mod.getMAPCode())) {
				for(LIPs lip : mod.getListOfLIPs())
					a_arrMOD.add(lip.getLIPs().getFirst().getBackbonePosition() + mod.getMAPCode());
			}
		}
		
		if(a_arrMOD.size() > 0) ret.put("index", a_arrMOD);
		
		return ret;
	}
	
	/**
	 * define node position in ambiguous structure 
	 * @param o_seqWURCS
	 * @return
	 */
	private HashMap<Integer, WURCSAntennaPoint> extractAmbiguousNode(WURCSSequence o_seqWURCS) {
		HashMap<Integer, WURCSAntennaPoint> ret = new HashMap<Integer, WURCSAntennaPoint>();
		
		Matcher glipStr = Pattern.compile("^WURCS=2.0/(\\d.+\\d)/(.+)/(\\d.+\\d)/(.+)$").matcher(o_seqWURCS.getWURCS());
		String glips = ""; 
		if(glipStr.find()) glips = glipStr.group(4); 
		for(String sitem : glips.split("_")) {
			if(sitem.indexOf("}") != -1) {
				Integer init = WURCSDataConverter.convertRESIndexToID(sitem.substring(0, 1));
				ret.put(init, new WURCSAntennaPoint(init));
				break;
			}
		}
		
		return ret;
	}
	
}
