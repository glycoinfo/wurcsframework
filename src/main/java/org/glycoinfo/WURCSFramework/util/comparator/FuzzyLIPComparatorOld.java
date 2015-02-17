package org.glycoinfo.WURCSFramework.util.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyLIPOld;
import org.glycoinfo.WURCSFramework.wurcs.LIP;

/**
 * Comparator for FuzzyLIP
 * @author MasaakiMatsubara
 *
 */
public class FuzzyLIPComparatorOld implements Comparator<FuzzyLIPOld> {

	private LIPComparator m_oLIPComp = new LIPComparator();

	@Override
	public int compare(FuzzyLIPOld o1, FuzzyLIPOld o2) {
		// For number of LIP (prioritize smaller number of LIP)
		int t_nLIP1 = o1.getLIPs().size();
		int t_nLIP2 = o2.getLIPs().size();
		if ( t_nLIP1 != t_nLIP2 )
			return t_nLIP1 - t_nLIP2;

		// For each LIP
		LinkedList<LIP> t_aLIPs1 = o1.getLIPs();
		LinkedList<LIP> t_aLIPs2 = o2.getLIPs();
		Collections.sort(t_aLIPs1, this.m_oLIPComp);
		Collections.sort(t_aLIPs2, this.m_oLIPComp);
		for ( int i=0; i<t_nLIP1; i++ ) {
			LIP t_LIP1 = t_aLIPs1.get(i);
			LIP t_LIP2 = t_aLIPs2.get(i);
			if ( this.m_oLIPComp.compare(t_LIP1, t_LIP2) != 0 )
				return this.m_oLIPComp.compare(t_LIP1, t_LIP2);
		}

		return 0;
	}

}
