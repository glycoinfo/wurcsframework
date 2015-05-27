package org.glycoinfo.WURCSFramework.wurcs.array;


public class UniqueRES extends MS {

	private int             m_iUniqueRESID = 0;

	public UniqueRES(int a_iUniqueRESID, String a_strSkeletonCode, int a_iAnomericPosition, char a_cAnomericSymbol ) {
		super(a_strSkeletonCode, a_iAnomericPosition, a_cAnomericSymbol);
		this.m_iUniqueRESID = a_iUniqueRESID;
	}

	public int getUniqueRESID() {
		return this.m_iUniqueRESID;
	}
}
