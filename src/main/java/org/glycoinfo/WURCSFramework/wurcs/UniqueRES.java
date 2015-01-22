package org.glycoinfo.WURCSFramework.wurcs;

import java.util.LinkedList;

public class UniqueRES {

	private int             m_iUniqueRESID = 0;
//	private String          m_strMonosaccharide;
	private String          m_strSkeletonCode;
	private int             m_iAnomericPosition;
	private char            m_cAnomericSymbol;
	private LinkedList<MOD> m_aMODs = new LinkedList<MOD>();

	public UniqueRES(int a_iUniqueRESID, String a_strSkeletonCode, int a_iAnomericPosition, char a_cAnomericSymbol ) {
		this.m_iUniqueRESID = a_iUniqueRESID;
		this.m_strSkeletonCode   = a_strSkeletonCode;
		this.m_iAnomericPosition = a_iAnomericPosition;
		this.m_cAnomericSymbol   = a_cAnomericSymbol;
	}

/*
	public UniqueRES(int a_iUniqueRESID, String a_strMonosaccharide, String a_strSkeletonCode, int a_iAnomericPosition, char a_cAnomericSymbol ) {
		this.m_iUniqueRESID = a_iUniqueRESID;
		this.m_strMonosaccharide = a_strMonosaccharide;
		this.m_strSkeletonCode   = a_strSkeletonCode;
		this.m_iAnomericPosition = a_iAnomericPosition;
		this.m_cAnomericSymbol   = a_cAnomericSymbol;
	}
*/

	public void addMOD(MOD mod) {
		this.m_aMODs.addLast(mod);
	}

	public int getUniqueRESID() {
		return this.m_iUniqueRESID;
	}

//	public String getMonosaccharide() {
//		return this.m_strMonosaccharide;
//	}

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
