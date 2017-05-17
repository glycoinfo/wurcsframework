package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

public class MS {

	public static int UNKNOWN_POSITION = -1;
	public static int OPEN_CHAIN = 0;
	private String          m_strSkeletonCode;
	private int             m_iAnomericPosition;
	private char            m_cAnomericSymbol;
	private LinkedList<MOD> m_aMODs = new LinkedList<MOD>();

	public MS(String a_strSkeletonCode, int a_iAnomericPosition, char a_cAnomericSymbol ) {
		this.m_strSkeletonCode   = a_strSkeletonCode;
		this.m_iAnomericPosition = a_iAnomericPosition;
		this.m_cAnomericSymbol   = a_cAnomericSymbol;
	}

	public void addMOD(MOD mod) {
		this.m_aMODs.addLast(mod);
	}

	public String getSkeletonCode() {
		return this.m_strSkeletonCode;
	}

	public int getAnomericPosition() {
		return this.m_iAnomericPosition;
	}

	public char getAnomericSymbol() {
		return this.m_cAnomericSymbol;
	}

	public LinkedList<MOD> getMODs() {
		return this.m_aMODs;
	}

}
