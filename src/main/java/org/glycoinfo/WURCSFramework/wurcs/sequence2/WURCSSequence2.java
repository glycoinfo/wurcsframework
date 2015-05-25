package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

/**
 * Class for sequence of WURCS (to define parent or child residues)
 * @author MasaakiMatsubara
 *
 */
public class WURCSSequence2 {

	private String m_strWURCS;
	private LinkedList<GRES> m_aGRES = new LinkedList<GRES>();
	private LinkedList<GLIN> m_aGLIN = new LinkedList<GLIN>();

	public WURCSSequence2(String a_strWURCS) {
		this.m_strWURCS = a_strWURCS;
	}

	public String getWURCS() {
		return this.m_strWURCS;
	}

	public void addGRES(GRES a_oGRES) {
		this.m_aGRES.add(a_oGRES);
	}

	public LinkedList<GRES> getGRESs() {
		return this.m_aGRES;
	}

	public void addGLIN(GLIN a_oGLIN) {
		this.m_aGLIN.add(a_oGLIN);
	}

	public LinkedList<GLIN> getGLINs() {
		return this.m_aGLIN;
	}
}
