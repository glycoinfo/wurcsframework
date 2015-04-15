package org.glycoinfo.WURCSFramework.graph;

/**
 * Class for repeating unit
 * @author MasaakiMatsubara
 *
 */
public class Repeat implements InterfaceRepeat {

	public static final int UNKNOWN = -1;

	/** minima count for this repeat unit ; -1 for unknown, 0 for no repeat */
	private int m_iMinRepeatCount = Repeat.UNKNOWN;
	/** maxima count for this repeat unit ; -1 for unknown, 0 for no repeat */
	private int m_iMaxRepeatCount = Repeat.UNKNOWN;

	public void setMinRepeatCount(int a_nRepMin) {
		this.m_iMinRepeatCount = a_nRepMin;
	}

	public int getMinRepeatCount() {
		return this.m_iMinRepeatCount;
	}

	public void setMaxRepeatCount(int a_nRepMax) {
		this.m_iMaxRepeatCount = a_nRepMax;
	}

	public int getMaxRepeatCount() {
		return this.m_iMaxRepeatCount;
	}


}
