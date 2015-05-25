package org.glycoinfo.WURCSFramework.util.wurcsjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.atlas.json.JsonObject;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSAnobase;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBasetype;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;

public class WURCSReader {
	
	public JsonArray generateWURCSJSON(WURCSArray o_arrWURCS) throws Exception {
		JsonArray ret = new JsonArray();
		LinkedHashMap<Integer, ArrayList<Integer>> edgeList = new LinkedHashMap<Integer, ArrayList<Integer>>();
		HashMap<String, Integer> redNum = new HashMap<String, Integer>();
		HashMap<Integer, WURCSParamBaggage> raList = new HashMap<Integer, WURCSParamBaggage>();
		
		//generate WURCS sequence
		WURCSArrayToSequence was = new WURCSArrayToSequence();
		was.start(o_arrWURCS);
		WURCSSequence o_seqWURCS = was.getSequence();
		
		int count = 0;
		for(RES res : o_arrWURCS.getRESs()) {
			redNum.put(res.getRESIndex(), count);
			count++;
		}
		
		Matcher glipStr = Pattern.compile("^WURCS=2.0/(\\d.+\\d)/(.+)/(\\d.+\\d)/(.+)$").matcher(o_seqWURCS.getWURCS());
		String glips = ""; 
		if(glipStr.find()) glips = glipStr.group(4); 
		
		for(String sitem : glips.split("_")) {
			if(sitem.equals("")) {}//monosaccharide
			else if(sitem.indexOf("}") != -1) {//extract root node in ambigous structure
				Integer init = redNum.get(sitem.substring(0, 1));
				raList.put(init, new WURCSParamBaggage(init));
			}else if(sitem.indexOf("~") != -1) {//extract repeating GLIP
				String start = sitem.substring(0, sitem.indexOf("-") - 1);
				String end = sitem.substring(sitem.indexOf("-") + 1, sitem.indexOf("-") + 2);
				int max = 0;
				int min = 0;
				
				String mod = sitem.contains("*") ? sitem.substring(sitem.indexOf("*"), sitem.indexOf("~")) : "";
				String repCount = sitem.substring(sitem.indexOf("~") + 1);
				if(repCount.matches("n")) max = min = -1;
				else if(repCount.matches("\\d:\\d")) {
					min = Integer.parseInt(repCount.substring(0, repCount.indexOf(":")));
					max = Integer.parseInt(repCount.substring(repCount.indexOf(":") + 1, repCount.length()));
				}
				
				if(redNum.get(start) > redNum.get(end))
					raList.put(redNum.get(start), new WURCSParamBaggage(min, max, redNum.get(end), redNum.get(start), mod) );
				else raList.put(redNum.get(end), new WURCSParamBaggage(min, max, redNum.get(start), redNum.get(end), mod) );
			}else {
				String key = sitem.indexOf("|") == -1 ? sitem.substring(0, 1) : sitem.substring(sitem.indexOf("-")+1, sitem.indexOf("-")+2);
				
				
				if(edgeList.containsKey(redNum.get(key))) {
					edgeList.get(redNum.get(key)).add(redNum.get(sitem.substring(sitem.indexOf("-")+1, sitem.indexOf("-")+2)) );
				}else {
					//extract edge
					ArrayList<Integer> content = new ArrayList<Integer>();
					if(sitem.indexOf("|") == -1) content.add(redNum.get(sitem.substring(sitem.indexOf("-")+1, sitem.indexOf("-")+2)) );
					else content.add(redNum.get(sitem.substring(sitem.indexOf("-")-2, sitem.indexOf("-")-1)) );
					edgeList.put(redNum.get(key), content);
				}
			}
		}
		
		//SkeletonCode-anomer_ringPos(modification)_modification_modification....
		for(RES gitem : o_arrWURCS.getRESs()) {
			JsonObject SkeletonJson = new JsonObject();
			ArrayList<String> modList = new ArrayList<String>();
			
			String nodeID = gitem.getRESIndex();
			int residueNum = redNum.get(nodeID);
					
			new WURCSBasetype();
			String basetype = WURCSBasetype.getBasetype(o_arrWURCS.getUniqueRESs().get(gitem.getUniqueRESID() - 1));
			SkeletonJson.put("BaseType", basetype);
			
			SkeletonJson.put("SkeletonCode", o_seqWURCS.getGRESs().get(residueNum).getMS().getString());
			
			//extract anomer position
			new WURCSAnobase();
			String anobase = WURCSAnobase.getAnobase(o_arrWURCS.getUniqueRESs().get(gitem.getUniqueRESID() - 1));
			if(anobase.matches(".+-[\\d\\?][abx].*")) {
				String SkeletonCode = anobase.contains("_") ? anobase.substring(0, anobase.indexOf("_")) : anobase;

				String[] sa = SkeletonCode.split("-");
				SkeletonJson.put("anomer", sa[1]);
			}
					
			//if monosaccharide is alditol or alc
			if(!basetype.matches("^[\\d<].+") && !basetype.contains("u") && !basetype.contains("U")) {
				modList.add("1*" + basetype.substring(0, 1));
				basetype = new StringBuilder().append(basetype).replace(0, 1, "u").toString();
			}
			
			//stereo have upper case of character
			if(basetype.indexOf("h".toUpperCase()) != -1)
				basetype = new StringBuilder().append(basetype.replaceAll("H", "h")).toString();
			if(basetype.indexOf("a".toUpperCase()) != -1)
				basetype = new StringBuilder().append(basetype.replaceAll("A", "a")).toString();
			
			//generate sugar name from basetype
			WURCSstereoTemplate wst = WURCSstereoTemplate.getBaseType(basetype);
			
			//prepare to retry that is generate sugar name from basetype 
			boolean hasMOd = false;
			basetype = basetype.contains("_") ? basetype.substring(0, basetype.indexOf("_")) : basetype;
			WURCSSuperClass wsc = WURCSSuperClass.getSuperClass(String.valueOf(basetype.length()));
			
			//check modification in stereo
			if(wst == null) {
				
				StringBuilder tmp = new StringBuilder().append(basetype);

				//extract relative and configurative position in basetype
				if(tmp.toString().matches(".*[5-8].*")) {
					for(char c : tmp.toString().toCharArray()) {
						if(String.valueOf(c).matches("\\d")) {
							if(Integer.parseInt(String.valueOf(c)) > 4) {
								String pos = String.valueOf(c);
								tmp.replace(tmp.indexOf(pos), tmp.indexOf(pos) + 1, "*");
							}
						}
					}
				}
				
				//extract -COOH and Deoxy at end position
				if(tmp.lastIndexOf("a") == tmp.length() - 1 || tmp.lastIndexOf("m") == tmp.length() - 1) {//a, m
					int modPosition = tmp.length() - 1;
					modList.add(String.valueOf(modPosition + 1) + "*" + tmp.substring(tmp.length() - 1, tmp.length()));
					tmp.replace(modPosition, modPosition + 1, "h");
				}
				
				//extract unsaturate position in stereo, unsaturate is handle as modification
				if(tmp.toString().matches(".+[ezfEZF].*")) {
					String unsatPos = "";
					while(tmp.toString().matches(".+[ezfEZF].*")) {
						int modPosition = 0;
						if(tmp.toString().contains("e") || tmp.toString().contains("E"))
							modPosition = tmp.indexOf("e") != -1 ? tmp.indexOf("e") : tmp.indexOf("E");
						if(tmp.toString().contains("z") || tmp.toString().contains("Z"))
							modPosition = tmp.indexOf("z") != -1 ? tmp.indexOf("z") : tmp.indexOf("Z");
						if(tmp.toString().contains("f") || tmp.toString().contains("F"))
							modPosition = tmp.indexOf("f") != -1 ? tmp.indexOf("f") : tmp.indexOf("F");
							
						if(tmp.toString().indexOf("e") != -1 || tmp.toString().indexOf("f") != -1 || tmp.toString().indexOf("z") != -1)
							modList.add(modPosition + 1 + "*d");
						unsatPos += String.valueOf(modPosition + 1);
						tmp.replace(modPosition, modPosition + 1, "*");
					}
					modList.add(unsatPos);
				}
				
				//If stere have an unique elements, these element are handled modification 
				if(tmp.toString().contains("d") || tmp.toString().contains("o") || tmp.toString().contains("a")) {
					hasMOd = true;				
					while(tmp.toString().contains("d") || tmp.toString().contains("o") || tmp.toString().contains("a")) {
						int modPosition = 0;
						if(tmp.toString().contains("d"))	modPosition = tmp.indexOf("d"); //deoxy
						if(tmp.toString().contains("o"))	modPosition = tmp.indexOf("o"); //ketone
						if(tmp.toString().contains("a"))	modPosition = tmp.indexOf("a"); //carboxy acid
						
						modList.add(String.valueOf(modPosition + 1) + "*" + tmp.substring(modPosition, modPosition + 1));
						tmp.replace(modPosition, modPosition + 1, "*");
					}
				}
				
				basetype = tmp.toString().replaceAll("\\*", "").toString();
				basetype = basetype.startsWith("U") ? basetype.replace("U", "u") : basetype;
				
				wst = WURCSstereoTemplate.getBaseType(basetype);
			
				if(wst == null) {
					System.out.println(o_seqWURCS.getWURCS());
					System.out.println(WURCSBasetype.getBasetype(o_arrWURCS.getUniqueRESs().get(gitem.getUniqueRESID() - 1)) + " : this monosaccharide is not defined in WURCSBaseType");
					return new JsonArray();
				}
			}
			String sugaName = hasMOd ? wst.getSugarName() + wsc.getSuperClass() : wst.getSugarName();
			
			//extract modification including ringPos and generate native substituent structure
			LinkedList<MOD> currentMOD = o_arrWURCS.getUniqueRESs().get(gitem.getUniqueRESID()-1).getMODs();	
			for(MOD mod : currentMOD) {
				//ring pos
				if(mod.getMAPCode().equals("")) {
					StringBuilder ringPos = new StringBuilder();
					for(LIPs lip : mod.getListOfLIPs()) ringPos.append(lip.getLIPs().getFirst().getBackbonePosition());
					SkeletonJson.put("rings", ringPos.toString());
				}
				
				//substituent
				if(!wst.getBaseType().contains(mod.getMAPCode())) {
					for(LIPs lip : mod.getListOfLIPs())
						modList.add(lip.getLIPs().getFirst().getBackbonePosition() + mod.getMAPCode());
				}
			}
			
			//Donar is indicate child node/Acceptor is indicate parent
			//Acceptor is handle as current node
			ArrayList<LinkedList<Integer>> acceptor = new ArrayList<LinkedList<Integer>>();
			ArrayList<LinkedList<Integer>> donor = new ArrayList<LinkedList<Integer>>();
			for(GLIN glinItem : o_seqWURCS.getGRESs().get(residueNum).getAcceptorGLINs()) {
				//extract child position
				donor.add(glinItem.getDonorPositions());
				//extract parent position
				acceptor.add(glinItem.getAcceptorPositions());
				
			}
			
			//generate Json object
			//Each object including SkeletonCode, anomer, ID, modification, ring positon, GLIP and child node
			SkeletonJson.put("SugarName", sugaName);
			SkeletonJson.put("ID", nodeID);
			if(donor.size() > 0 && acceptor.size() > 0) {
				SkeletonJson.put("Donor", donor.toString());
				SkeletonJson.put("Acceptor", acceptor.toString());
			}
			if(modList.size() > 0) SkeletonJson.put("mod", modList.toString());
			if(edgeList.get(residueNum) != null) SkeletonJson.put("child", edgeList.get(residueNum).toString());
			
			//add an end bracket information
			if(raList.containsKey(residueNum)) {
				if(raList.get(residueNum).isRepeat()) {
					JsonObject repeatEnd = new JsonObject();
					if(residueNum == raList.get(residueNum).getStart()) {
						repeatEnd.put("start", residueNum);
					}else {
						JsonObject repeatStart = new JsonObject();
						repeatStart.put("GLIP", "");
						repeatStart.put("start", residueNum);

						if(!raList.get(residueNum).getSubstituent().equals(""))
							repeatStart.put("bridge", raList.get(residueNum).getSubstituent());
						else
							repeatStart.put("bridge", "none");

						((JsonObject) ret.get(raList.get(residueNum).getStart())).put("repeat", repeatStart);
					}
					//define end repeating point in JsonObject
					repeatEnd.put("end", residueNum);
					repeatEnd.put("min", raList.get(residueNum).getRepeatingMin());
					repeatEnd.put("max", raList.get(residueNum).getRepeatingMax());
					SkeletonJson.put("repeat", repeatEnd);
				}
				if(raList.get(residueNum).isAmbigous()) SkeletonJson.put("anotate", "isAmbigous");
			}	

			ret.add(SkeletonJson);
		}
		
		return ret;
	}
}