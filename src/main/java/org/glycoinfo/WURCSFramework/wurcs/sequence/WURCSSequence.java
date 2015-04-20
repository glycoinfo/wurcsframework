package org.glycoinfo.WURCSFramework.wurcs.sequence;

import java.util.LinkedList;

/**
 * Class for sequence of WURCS (for new RDF)
 * @author MasaakiMatsubara
 *
 */
public class WURCSSequence {

	private String m_strWURCS;
	private LinkedList<GRES> m_aGRES = new LinkedList<GRES>();
	private LinkedList<GLIN> m_aGLIN = new LinkedList<GLIN>();

	public WURCSSequence(String a_strWURCS) {
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
