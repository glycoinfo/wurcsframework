package org.glycoinfo.WURCSFramework.wurcs.array;


public class UniqueRES extends MS {

	private int             m_iUniqueRESID = 0;

	public UniqueRES(int a_iUniqueRESID, String a_strSkeletonCode, int a_iAnomericPosition, char a_cAnomericSymbol ) {
		super(a_strSkeletonCode, a_iAnomericPosition, a_cAnomericSymbol);
		this.m_iUniqueRESID = a_iUniqueRESID;
	}

	public UniqueRES(int a_iUniqueRESID, MS a_oMS) {
		super( a_oMS.getSkeletonCode(), a_oMS.getAnomericPosition(), a_oMS.getAnomericSymbol() );
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			this.addMOD(t_oMOD);
		}
		this.m_iUniqueRESID = a_iUniqueRESID;
	}

	public int getUniqueRESID() {
		return this.m_iUniqueRESID;
	}
}
