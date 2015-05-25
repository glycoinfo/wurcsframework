package org.glycoinfo.WURCSFramework.util.wurcsjson;

public class WURCSParamBaggage {
	private int start = 0;
	private int end = 0;
	
	private int antennaRoot = 0;
	private boolean ambigous = false;
	
	private String repeatingMod = "";
	private int repeatingCountMin = 0;
	private int repeatingCountMax = 0;
	private boolean repeat = false;
	
	public WURCSParamBaggage(int a_intAntenna) {
		this.antennaRoot = a_intAntenna;
		this.ambigous = true;
	}
	
	public WURCSParamBaggage(int a_intMin, int a_intMax, int a_intStart, int a_intEnd, String a_strMod) {
		this.repeatingCountMax = a_intMax;
		this.repeatingCountMin = a_intMin;
		this.start = a_intStart;
		this.end = a_intEnd;
		this.repeatingMod = a_strMod;
		this.repeat = true;
	}
	
	public WURCSParamBaggage() {
	}
	
	public void setStart(int a_intStart) {
		this.start = a_intStart;
	}
	
	public void setEnd(int a_intEnd) {
		this.end = a_intEnd;
	}
	
	public int getRepeatingMax() {
		return this.repeatingCountMax;
	}
	
	public int getRepeatingMin() {
		return this.repeatingCountMin;
	}
	
	public int getStart() {
		return this.start;
	}
	
	public int getEnd() {
		return this.end;
	}
	
	public int getAntennaRoot() {
		return this.antennaRoot;
	}
	
	public String getSubstituent() {
		return this.repeatingMod;
	}
	
	public boolean isRepeat() {
		return this.repeat;
	}
	
	public boolean isAmbigous() {
		return this.ambigous;
	}
}