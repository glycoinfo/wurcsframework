package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class WURCSArray {

	//private String m_strWURCS = "";
	private String m_strVersion = "2.0";
	private int    m_nNumberOfUniqueRES;
	private int    m_nNumberOfRES;
	private int    m_nNumberOfLIN;
	private boolean m_bIsComposition;
	private LinkedList<UniqueRES> m_aUniqueRESs = new LinkedList<UniqueRES>();
	private LinkedList<RES> m_aRESs = new LinkedList<RES>();
	private LinkedList<LIN> m_aLINs = new LinkedList<LIN>();

	public WURCSArray(String version, int numUniqueRES, int numRES, int numLIN, boolean isComposition) {
		//this.m_strWURCS = strWURCS;
		this.m_strVersion = version;
		this.m_nNumberOfUniqueRES = numUniqueRES;
		this.m_nNumberOfRES = numRES;
		this.m_nNumberOfLIN = numLIN;
		this.m_bIsComposition = isComposition;
	}

	public void addUniqueRES(UniqueRES a_objRES) {
		this.m_aUniqueRESs.addLast(a_objRES);
	}

	public void addLIN(LIN a_objLIN) {
		this.m_aLINs.addLast(a_objLIN);
	}

	public void addRES(RES a_objRES) {
		this.m_aRESs.addLast(a_objRES);
	}

//	public String getWURCS() {
//		return this.m_strWURCS;
//	}

	public String getVersion() {
		return this.m_strVersion;
	}

	public int getUniqueRESCount() {
		return this.m_nNumberOfUniqueRES;
	}

	public int getRESCount() {
		return this.m_nNumberOfRES;
	}

	public int getLINCount() {
		return this.m_nNumberOfLIN;
	}

	public boolean isComposition() {
		return this.m_bIsComposition;
	}

	public LinkedList<UniqueRES> getUniqueRESs() {
		return this.m_aUniqueRESs;
	}

	public LinkedList<RES> getRESs() {
		return this.m_aRESs;
	}

	public LinkedList<LIN> getLINs() {
		return this.m_aLINs;
	}
}
