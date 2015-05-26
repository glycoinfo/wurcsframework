package org.glycoinfo.WURCSFramework.util.wurcsjson;

import java.util.ArrayList;
import org.glycoinfo.WURCSFramework.wurcs.graph.CarbonDescriptor;

/**
 * Class for extract a modification in basetype
 * @author st
 */
//TODO : change a method to reference charactor from DirectionDescriptor
public class SkeletonCodeModTrimmer {
	
	private ArrayList<String> modList = new ArrayList<String>();
	private boolean hasMod = false;
	
	public String checkSkeletonModification(String basetype) {
		int basesize = basetype.length();
		
		//if monosaccharide is alditol or alc
		if(!basetype.matches("^[\\d<].+") && !basetype.contains("u") && !basetype.contains("U"))
			basetype = this.AlcoholTrim(basetype);
		
		//stereo have upper case of character
		if(basetype.indexOf("h".toUpperCase()) != -1 || basetype.indexOf("a".toUpperCase()) != -1)
			basetype = this.UpperCharacterTrim(basetype);

		//extract relative and configurative position in basetype
		if(basetype.toString().matches(".*[5-8].*"))
			basetype = this.RelativeModificationTrim(basetype);

		//extract -COOH and Deoxy at end position
		if(basetype.lastIndexOf("a") == basetype.length() - 1 || 
				basetype.lastIndexOf("m") == basetype.length() - 1)
			basetype = this.EndModificationTrim(basetype);
		
		//extract unsaturate position in stereo, unsaturate is handle as modification
		if(basetype.matches(".+[ezfEZF].*")) basetype = this.DoubleBondTrim(basetype);

		//If stere have an unique elements, these element are handled modification 
		if(basetype.contains("d") || basetype.contains("o") || basetype.contains("a"))
			basetype = this.ModificationTrim(basetype);

		//replace "*" in basetype
		basetype = basetype.replaceAll("\\*", "").toString();		
		basetype = basetype.startsWith("U") ? basetype.replace("U", "u") : basetype;
		
		if(basetype.length() != basesize) this.hasMod = true;
			
		return basetype;
	}
	
	/*
	 * Extract modification as "m", "k", "o" in SkeletonCode
	 */
	public String ModificationTrim(String a_strBase) {
		StringBuilder tmp = new StringBuilder().append(a_strBase);

		while(tmp.toString().contains("d") || tmp.toString().contains("o") || tmp.toString().contains("a")) {
			int modPos = 0;
			if(tmp.toString().contains("d"))	modPos = tmp.indexOf("d"); //deoxy
			if(tmp.toString().contains("o"))	modPos = tmp.indexOf("o"); //ketone
			if(tmp.toString().contains("a"))	modPos = tmp.indexOf("a"); //carboxy acid
			
			this.modList.add(String.valueOf(modPos + 1) + "*" + tmp.substring(modPos, modPos + 1));
			tmp.replace(modPos, modPos + 1, "*");
		}
		
		return tmp.toString();
	}
	
	/*
	 * Extract double bond position in SkeletonCode
	 */
	public String DoubleBondTrim(String a_strBase) {
		StringBuilder tmp = new StringBuilder().append(a_strBase);
		String unsatPos = "";
		
		while(tmp.toString().matches(".+[ezfEZF].*")) {
			int modPos = 0;
			if(tmp.toString().contains("e") || tmp.toString().contains("E"))
				modPos = tmp.indexOf("e") != -1 ? tmp.indexOf("e") : tmp.indexOf("E");
			if(tmp.toString().contains("z") || tmp.toString().contains("Z"))
				modPos = tmp.indexOf("z") != -1 ? tmp.indexOf("z") : tmp.indexOf("Z");
			if(tmp.toString().contains("f") || tmp.toString().contains("F"))
				modPos = tmp.indexOf("f") != -1 ? tmp.indexOf("f") : tmp.indexOf("F");
				
			if(tmp.indexOf("e") != -1 || tmp.indexOf("f") != -1 || tmp.indexOf("z") != -1) 
				this.modList.add(modPos + 1 + "*d");

			unsatPos += String.valueOf(modPos + 1);
			tmp.replace(modPos, modPos + 1, "*");
		}
		this.modList.add(unsatPos);
		
		return tmp.toString();
	}
	
	/*
	 * Extract upper class character in SkeletonCode
	 */
	public String UpperCharacterTrim(String a_strBase) {
		if(a_strBase.indexOf("h".toUpperCase()) != -1)
			a_strBase = new StringBuilder().append(a_strBase.replaceAll("H", "h")).toString();
		if(a_strBase.indexOf("a".toUpperCase()) != -1)
			a_strBase = new StringBuilder().append(a_strBase.replaceAll("A", "a")).toString();
		return a_strBase;
	}
	
	/*
	 * Extract a relative modification position that is larger than "4" in SkeletonCode
	 */
	public String RelativeModificationTrim(String a_strBase) {
		StringBuilder ret = new StringBuilder().append(a_strBase);
		
		for(char c : a_strBase.toString().toCharArray()) {
			if(String.valueOf(c).matches("\\d") && Integer.parseInt(String.valueOf(c)) > 4) {
				String pos = String.valueOf(c);
				ret.replace(a_strBase.indexOf(pos), a_strBase.indexOf(pos) + 1, "*");
			}
		}
		return ret.substring(0, ret.indexOf("_")).toString();	
	}
	
	/*
	 * Extract end position a modification in SkeletonCode
	 */
	public String EndModificationTrim(String a_strBase) {
		int modPosition = a_strBase.length() - 1;
		StringBuilder tmp = new StringBuilder().append(a_strBase);
		
		this.modList.add(String.valueOf(modPosition + 1) + "*" + tmp.substring(tmp.length() - 1, tmp.length()));
		tmp.replace(modPosition, modPosition + 1, "h");
	
		return tmp.toString();
	}
	
	/*
	 * Extract alcohol in SkeletonCode
	 */
	public String AlcoholTrim(String a_strBase) {
		this.modList.add("1*" + a_strBase.substring(0, 1));
		a_strBase = new StringBuilder().append(a_strBase).replace(0, 1, "u").toString();

		return a_strBase;
	}
	
	public ArrayList<String> getModificationList() {
		return this.modList;
	}
	
	public boolean hasModification() {
		return this.hasMod;
	}
}