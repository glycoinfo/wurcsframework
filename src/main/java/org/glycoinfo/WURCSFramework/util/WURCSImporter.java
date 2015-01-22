package org.glycoinfo.WURCSFramework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class WURCSImporter {

	public WURCSArray WURCSsepalator(String a_strWURCS) {
		String version = "";
		int numBMU = 0;
		int numMLU = 0;
	
		Matcher size = Pattern.compile("=(.+)/(\\d+),(\\d+)").matcher(a_strWURCS);
		if(size.find()) {
			version = size.group(1);
			numBMU = Integer.parseInt(size.group(2));
			numMLU = Integer.parseInt(size.group(3));
		}

		WURCSArray wurcsContainer = new WURCSArray(version, numBMU, numMLU);
		
		//extract SkeletonCode and generate a residue
		Matcher mat = Pattern.compile("\\[(.+)\\]").matcher(a_strWURCS);
		if(mat.find()) 
			for(String s : mat.group(1).split("\\]\\[", -1)) 
				wurcsContainer = generateBMU(s, wurcsContainer);
			
		//extract MLU
		if(wurcsContainer.getRESCount() > 0) {
			for(String s : a_strWURCS.split("\\|", -1)) {
				s = s.replaceAll(".+\\]", "");
				if(s.matches("^\\d+\\+.+")) wurcsContainer =  extractMLU(s, wurcsContainer);
			}
		}
		
		return wurcsContainer;
	}

	public WURCSArray generateBMU(String a_strSkeletonCode, WURCSArray a_objWURCS) {
		//regex list
		String skeletonCode = "([a-zA-Z1-9<>]+)";
		String anomer = "(([\\d\\?]+)\\:([\\w\\?]+))?";
		String ringPositiron = "(([\\?\\w]),([\\?\\w]))?";
		String modification = ".*";

		//group(0) : 12122h+1:b|1,5|2*NCC/3=O
		//group(1) : 12122h
		//group(2) : 1:b|1,5 (into Matcher)
		//group(3) : 1,b
		//group(4) : 1
		//group(5) : b
		//group(6) : 1,5
		//group(7) : 1
		//group(8) : 5
		//group(9) : 2*NCC/3=O (all of modification)

		Matcher skeletonParts = Pattern.compile(skeletonCode + "\\+?(" + anomer + "\\|?" + ringPositiron + ")?" + "\\|?(" + modification + ")?").matcher(a_strSkeletonCode);

		if(skeletonParts.find()) {
			UniqueRES bmu = new UniqueRES("", -1, ' ');
			LinkedList<LIP> colinUnit = new LinkedList<LIP>();
			
			//extract SkeletonCode, anomeric state
			if(skeletonParts.group(3) == null)
				bmu = new UniqueRES(skeletonParts.group(1), -1, 'x');
			else if(skeletonParts.group(4).equals("?"))
				bmu = new UniqueRES(skeletonParts.group(1), -1, skeletonParts.group(5).charAt(0));
			else
				bmu = new UniqueRES(skeletonParts.group(1), Integer.parseInt(skeletonParts.group(4)), skeletonParts.group(5).charAt(0));
			
			//extract ring size
			if(skeletonParts.group(6) != null) {
				LIN mlu = new LIN("");
				for(String modPos : skeletonParts.group(6).split(",", -1)) {
					colinUnit = new LinkedList<LIP>();
					colinUnit.addLast(new LIP(0, 
							modPos.equals("?") ? -1 : Integer.parseInt(modPos), 
							modPos.indexOf(":") != -1 ? modPos.substring(modPos.indexOf(":") + 1, modPos.indexOf(":") + 2).charAt(0) : '0',
							modPos.indexOf("-") != -1 ? Integer.parseInt(modPos.substring(modPos.indexOf("-") + 1, modPos.indexOf("-") + 2)) : 1
					));
					mlu.addCOLIN(colinUnit);
				}
				bmu.addLIN(mlu);
			}
			
			//extract modification
			if(skeletonParts.group(9) != null && !skeletonParts.group(9).equals("")) {
				for(String m: skeletonParts.group(9).split("\\|")) {
					colinUnit = new LinkedList<LIP>();
					String map = m.indexOf("*") != -1 ? m.substring(m.indexOf("*")) : "";
					LIN mlu = new LIN(map);

					if(m.indexOf("\\") != -1) { //ambiguous position
						String separatePos = m.substring(0, m.indexOf("*"));
						for(String ambPos : separatePos.split("\\\\", -1))
							colinUnit.addLast(new LIP(0, Integer.parseInt(ambPos), '0', 1));
						mlu.addCOLIN(colinUnit);
					}else if(m.indexOf(",") != -1) {
						if(m.indexOf("*") != -1) { //pyruvate
							for(String modPos : m.substring(0, m.indexOf("*")).split(",",-1)) {
								colinUnit = new LinkedList<LIP>();
								colinUnit.addLast(new LIP(0, 
										modPos.equals("?") ? -1 : Integer.parseInt(modPos), 
										modPos.indexOf(":") != -1 ? modPos.substring(modPos.indexOf(":") + 1, modPos.indexOf(":") + 2).charAt(0) : '0',
										modPos.indexOf("-") != -1 ? Integer.parseInt(modPos.substring(modPos.indexOf("-") + 1, modPos.indexOf("-") + 2)) : 1
								)); 
								mlu.addCOLIN(colinUnit);
							}
						}else { //unhydro
							for(String modPos : m.split(",",-1)) {
								colinUnit = new LinkedList<LIP>();
								colinUnit.addLast(new LIP(0, 
										modPos.equals("?") ? -1 : Integer.parseInt(modPos), 
										modPos.indexOf(":") != -1 ? modPos.substring(modPos.indexOf(":") + 1, modPos.indexOf(":") + 2).charAt(0) : '0',
										modPos.indexOf("-") != -1 ? Integer.parseInt(modPos.substring(modPos.indexOf("-") + 1, modPos.indexOf("-") + 2)) : 1
								));
								mlu.addCOLIN(colinUnit);
							}
						}
					}else { //single bond position
						int backbone = m.substring(0, m.indexOf("*")).equals("?") ? -1 : Integer.parseInt(m.substring(0, m.indexOf("*")));
						colinUnit.addLast(new LIP(0, 
								backbone, 
								m.indexOf(":") != -1 ? m.substring(m.indexOf(":") + 1, m.indexOf(":") + 2).charAt(0) : '0',
								m.indexOf("-") != -1 ? Integer.parseInt(m.substring(m.indexOf("-") + 1, m.indexOf("-") + 2)) : 1
						));
						mlu.addCOLIN(colinUnit);
					}
					bmu.addLIN(mlu);
				}
			}
			a_objWURCS.addRES(bmu);
		}
		return a_objWURCS;
	}

	public WURCSArray extractMLU(String a_strMLU, WURCSArray a_objWURCS) {
		LinkedList<LIP> colinUnit = new LinkedList<LIP>();
		
		String left = "((\\d+)\\+(.+))";
		String right = "\\(?(\\d+)\\+([\\)?\\(?\\w+\\d+\\+\\\\?:\\-\\*^/=]*)";
		String prob = "[%\\.\\d+\\?\\+]*";
		String mod = "((\\*.+[^~])~(n|\\d+-?(\\d)?))";
		String rep = "(~(n|\\d+)-?(\\d)?)";

		//group(0) : all
		//group(1) : parent
		//group(2) : NodeID
		//group(3) : Parent Postion
		//group(4) : child/probability + child position
		//group(5) : direction
		//group(6) : child Position
		//group(7) : modification~n
		//group(8) : modification
		//group(9) : n/min, group(10) : max
		//group(11) : ~rep
		//group(12) : n/min, (13) : max
		
		Matcher mluString = Pattern.compile(left + "," + "(" + right + "|" + prob + ")" + mod+"?" + rep + "?").matcher(a_strMLU);
		if(mluString.find()) {
			
			String strColin = mluString.group(0);
			LIN mlu = new LIN(strColin.indexOf("*") != -1 ? strColin.substring(strColin.indexOf("*")) : "");
			
			if(mluString.group(7) != null || mluString.group(11) != null) {
				if(mluString.group(8) != null) mlu = new LIN(mluString.group(8));
				mlu.setRepeatingUnit(true);
			}
			
			String parent = mluString.group(0).substring(0, mluString.group(0).indexOf(","));
			colinUnit.addLast(new LIP(
					Integer.parseInt(parent.substring(0, parent.indexOf("+"))), 
					parent.substring(parent.indexOf("+") + 1, parent.indexOf("+") + 2).equals("?") ? -1 : Integer.parseInt(parent.substring(parent.indexOf("+") + 1, parent.indexOf("+") + 2)), 
					parent.indexOf(":") != -1 ? parent.substring(parent.indexOf(":") + 1, parent.indexOf(":") + 2).charAt(0) : '0',
					parent.indexOf("-") != -1 ? Integer.parseInt(parent.substring(parent.indexOf("-") + 1, parent.indexOf("-") + 2)) : 1));
			mlu.addCOLIN(colinUnit);
			strColin = strColin.replace(parent+",", "");
			
			if(strColin.indexOf("\\") != -1 && mlu.isRepeatingUnit() == false) { //ambiguous bond
				colinUnit = new LinkedList<LIP>();
				for(String s : mluString.group(4).split("\\\\", -1)) {
					s = s.replace("(", "");
					s = s.replace(")", "");
					colinUnit.addLast(new LIP(
							Integer.parseInt(s.substring(0, s.indexOf("+"))),
							s.indexOf("?") == -1 ? Integer.parseInt(s.substring(s.indexOf("+") + 1, s.indexOf("+") + 2)) : -1,
							s.indexOf(":") != -1 ? s.substring(s.indexOf(":") + 1, s.indexOf(":") + 2).charAt(0) : '0',
							s.indexOf("-") != -1 ? Integer.parseInt(s.substring(s.indexOf("-") + 1, s.indexOf("-") + 2)) : 1));
				}
				mlu.addCOLIN(colinUnit);
				a_objWURCS.addLIN(mlu);
			}else if(strColin.indexOf("%") != -1) {//probability bond
				colinUnit = new LinkedList<LIP>();
				String probColin = strColin.substring(strColin.lastIndexOf("%") + 1);
				strColin = strColin.replace(probColin, "");
				
				colinUnit.addLast(new LIP(
						Integer.parseInt(probColin.substring(0, probColin.indexOf("+"))),
						probColin.substring(probColin.indexOf("+") + 1, probColin.indexOf("+") + 2).equals("?") ? -1 : Integer.parseInt(probColin.substring(probColin.indexOf("+") + 1, probColin.indexOf("+") + 2)),
						probColin.indexOf(":") != -1 ? probColin.substring(probColin.indexOf(":") + 1, probColin.indexOf(":") + 2).charAt(0) : '0',
						probColin.indexOf("-") != -1 ? Integer.parseInt(probColin.substring(probColin.indexOf("-") + 1, probColin.indexOf("-") + 2)) : 1));
			
				List<String> list = new ArrayList<String>(Arrays.asList(strColin.split("%"))); // 新インスタンスを生成
				list.remove("");
				String[] trimProb = (String[]) list.toArray(new String[list.size()]);
			
				//set Probability parameter
				if(trimProb.length < 2) {
					if(trimProb[0].equals("?")) colinUnit.get(0).setProbabilityLower(-1);
					else	colinUnit.get(0).setProbabilityLower(Double.parseDouble(trimProb[0]));
				}else {
					if(trimProb[0].equals("?")) colinUnit.get(0).setProbabilityLower(-1);
					else	colinUnit.get(0).setProbabilityLower(Double.parseDouble(trimProb[0]));
					if(trimProb[1].equals("?")) colinUnit.get(0).setProbabilityUpper(-1);
					else colinUnit.get(0).setProbabilityUpper(Double.parseDouble(trimProb[1]));
				}
	
				mlu.addCOLIN(colinUnit);
				a_objWURCS.addLIN(mlu);
			}else if(strColin.equals(mluString.group(4))) {//extract single bond
				for(String unit : strColin.split(",")) {
					colinUnit = new LinkedList<LIP>();
					colinUnit.addLast(new LIP(
						Integer.parseInt(unit.substring(0, unit.indexOf("+"))),
						!unit.substring(unit.indexOf("+") + 1).equals("?") ? Integer.parseInt(unit.substring(unit.indexOf("+") + 1, unit.indexOf("+") + 2)) : -1,
						unit.indexOf(":") != -1 ? unit.substring(unit.indexOf(":") + 1, unit.indexOf(":") + 2).charAt(0) : '0',
						unit.indexOf("-") != -1 ? Integer.parseInt(unit.substring(unit.indexOf("-") + 1, unit.indexOf("-") + 2)) : 1));
					mlu.addCOLIN(colinUnit);
				}
	
				a_objWURCS.addLIN(mlu);
			}
				
			//extract modification with colin, repetition
			if(mlu.isRepeatingUnit() == true) { 
				if(mluString.group(4).indexOf("\\") != -1) {
					colinUnit = new LinkedList<LIP>();
					for(String s : mluString.group(4).split("\\\\", -1)) {
						s = s.replace("(", "");
						s = s.replace(")", "");
						colinUnit.addLast(new LIP(
							Integer.parseInt(s.substring(0, s.indexOf("+"))),
							s.indexOf("?") == -1 ? Integer.parseInt(s.substring(s.indexOf("+") + 1, s.indexOf("+") + 2)) : -1,
							s.indexOf(":") != -1 ? s.substring(s.indexOf(":") + 1, s.indexOf(":") + 2).charAt(0) : '0',
							s.indexOf("-") != -1 ? Integer.parseInt(s.substring(s.indexOf("-") + 1, s.indexOf("-") + 2)) : 1));
							
					}
					mlu.addCOLIN(colinUnit);
				}else {
					for(String unit : strColin.split(",")) {
						colinUnit = new LinkedList<LIP>();
						if(unit.indexOf("~") != -1) unit = unit.replaceAll("~.+", "");
						if(unit.indexOf("*") != -1) unit = unit.replaceAll("\\*.+", "");
						
						colinUnit.addLast(new LIP(
							Integer.parseInt(unit.substring(0, unit.indexOf("+"))),
							!unit.substring(unit.indexOf("+") + 1).equals("?") ? Integer.parseInt(unit.substring(unit.indexOf("+") + 1, unit.indexOf("+") + 2)) : -1,
							unit.indexOf(":") != -1 ? unit.substring(unit.indexOf(":") + 1, unit.indexOf(":") + 2).charAt(0) : '0',
							unit.indexOf("-") != -1 ? Integer.parseInt(unit.substring(unit.indexOf("-") + 1, unit.indexOf("-") + 2)) : 1));
						mlu.addCOLIN(colinUnit);
					}
				}

				if(mluString.group(7) != null)
					a_objWURCS.addLIN(setRepetingCount(mluString.group(9), mluString.group(10), mlu));
				if(mluString.group(11) != null)
					a_objWURCS.addLIN(setRepetingCount(mluString.group(12), mluString.group(13), mlu));
			}
		}
		return a_objWURCS;
	}
	
	public LIN setRepetingCount(String a_sRepMin, String a_sRepMax, LIN a_objMLU) {
		if(a_sRepMin.equals("n")) {
			a_objMLU.setMaxRepeatCount(-1);
			a_objMLU.setMinRepeatCount(-1);
		}else if(a_sRepMax == null) {
			a_objMLU.setMinRepeatCount(Integer.parseInt(a_sRepMin));
		}else {
			a_objMLU.setMinRepeatCount(Integer.parseInt(a_sRepMin));
			a_objMLU.setMaxRepeatCount(Integer.parseInt(a_sRepMax));
		}
		return a_objMLU;
	}
	
	public void extractAmbiguousPosition(String a_strAmbiguous) {
		
	} 

}