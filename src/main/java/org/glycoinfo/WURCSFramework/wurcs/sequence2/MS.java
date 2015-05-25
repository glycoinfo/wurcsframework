package org.glycoinfo.WURCSFramework.wurcs.sequence2;

/**
 * Class of monosaccharide for WURCS sequence
 * @author MasaakiMatsubara
 *
 */
public class MS {

	private String m_strMonosaccharide;

	public MS(String a_strMS) {
		this.m_strMonosaccharide = a_strMS;
	}

	public String getString() {
		return this.m_strMonosaccharide;
	}
}
