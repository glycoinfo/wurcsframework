package org.glycoinfo.WURCSFramework.wurcs.sequence2;

public class MSCORE extends SEQMS {

	private String m_strSkeletonCode;
	private int m_iAnomericPosition;
	private char m_cAnomericSymbol;

	public MSCORE(String a_strMS, String a_strSC, int a_iAnomPos, char a_cAnomSym) {
		super(a_strMS);
		this.m_strSkeletonCode   = a_strSC;
		this.m_iAnomericPosition = a_iAnomPos;
		this.m_cAnomericSymbol   = a_cAnomSym;
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


}
