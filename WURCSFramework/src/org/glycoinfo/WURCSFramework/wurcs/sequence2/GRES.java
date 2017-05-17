package org.glycoinfo.WURCSFramework.wurcs.sequence2;

import java.util.LinkedList;

/**
 * Class of GRES for WURCS sequence
 * @author MasaakiMatsubara
 *
 */
public class GRES {

	private int m_iID;
	private MSPERI m_objMonosaccharide;
	private LinkedList<GLIN> m_aDonorGLIN    = new LinkedList<GLIN>();
	private LinkedList<GLIN> m_aAcceptorGLIN = new LinkedList<GLIN>();

	public GRES(int a_iID, MSPERI a_oMS) {
		this.m_iID = a_iID;
		this.m_objMonosaccharide = a_oMS;
	}

	public int getID() {
		return this.m_iID;
	}

	public MSPERI getMS() {
		return this.m_objMonosaccharide;
	}

	public void addDonorGLIN(GLIN a_oDGLIN) {
		this.m_aDonorGLIN.add(a_oDGLIN);
	}

	public LinkedList<GLIN> getDonorGLINs() {
		return this.m_aDonorGLIN;
	}

	public void addAcceptorGLIN(GLIN a_oAGLIN) {
		this.m_aAcceptorGLIN.add(a_oAGLIN);
	}

	public LinkedList<GLIN> getAcceptorGLINs() {
		return this.m_aAcceptorGLIN;
	}
}
