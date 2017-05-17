package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

/**
 * Class of monosaccharide for WURCS sequence
 * @author MasaakiMatsubara
 *
 */
public class SEQMS {

	private String m_strMonosaccharide;
	private LinkedList<SUBST> m_aSUBSTs = new LinkedList<SUBST>();
	private LinkedList<BRIDGE>  m_aRINGs  = new LinkedList<BRIDGE>();

	public SEQMS(String a_strMS) {
		this.m_strMonosaccharide = a_strMS;
	}

	public String getString() {
		return this.m_strMonosaccharide;
	}

	public void addDivalentSubstituent(BRIDGE a_oRING) {
		this.m_aRINGs.add(a_oRING);
	}

	public LinkedList<BRIDGE> getDivalentSubstituents() {
		return this.m_aRINGs;
	}

	public void addSubstituent(SUBST a_oSUBST) {
		this.m_aSUBSTs.add(a_oSUBST);
	}

	public LinkedList<SUBST> getSubstituents() {
		return this.m_aSUBSTs;
	}

}
