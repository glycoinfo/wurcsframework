package org.glycoinfo.WURCSFramework.util.wurcsjson;

public class WURCSAntennaPoint {
	private int antennaRoot = 0;
	private boolean ambigous = false;
	
	public WURCSAntennaPoint(int a_intAntenna) {
		this.antennaRoot = a_intAntenna;
		this.ambigous = true;
	}
	
	public WURCSAntennaPoint() {}
	
	public int getAntennaRoot() {
		return this.antennaRoot;
	}
	
	public boolean isAmbigous() {
		return this.ambigous;
	}
}