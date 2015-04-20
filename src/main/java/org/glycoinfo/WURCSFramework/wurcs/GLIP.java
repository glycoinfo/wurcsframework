package org.glycoinfo.WURCSFramework.wurcs;

public class GLIP extends LIP {

	private String  m_strRESIndex;

	public GLIP(String a_strRESIndex, int a_iBackbonePosition, char a_iBackboneDirection, int a_iModificationPosition) {
		super(a_iBackbonePosition, a_iBackboneDirection, a_iModificationPosition);
		this.m_strRESIndex = a_strRESIndex;
	}

	public String getRESIndex() {
		return this.m_strRESIndex;
	}


}
