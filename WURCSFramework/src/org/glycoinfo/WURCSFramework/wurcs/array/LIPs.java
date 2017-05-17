package org.glycoinfo.WURCSFramework.wurcs.array;

import java.util.LinkedList;

/**
 * Class for list of LIP
 * @author MasaakiMatsubara
 *
 */
public class LIPs {
	private LinkedList<LIP> m_aLIPs;

	public LIPs(LinkedList<LIP> a_aLIPs) {
		this.m_aLIPs = a_aLIPs;
	}

	public LinkedList<LIP> getLIPs() {
		return this.m_aLIPs;
	}

	public boolean isFuzzy() {
		if ( this.m_aLIPs.get(0).getBackbonePosition() == -1 )
			return true;
		if ( this.m_aLIPs.size() > 1 ) return true;
		return false;
	}
}
