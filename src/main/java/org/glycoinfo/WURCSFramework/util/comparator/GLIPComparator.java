package org.glycoinfo.WURCSFramework.util.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;

/**
 * Comparator for GLIP
 * @author MasaakiMatsubara
 *
 */
public class GLIPComparator implements Comparator<GLIP>{

	private LIPComparator m_oLIPComp = new LIPComparator();
	@Override
	public int compare(GLIP o1, GLIP o2) {

		// For probabilities
		if ( this.m_oLIPComp.compareProbabilities(o1, o2) != 0 )
			return this.m_oLIPComp.compare(o1, o2);

		// For RES Index
		String t_strRESIndex1 = o1.getRESIndex();
		String t_strRESIndex2 = o2.getRESIndex();
		if (! t_strRESIndex1.equals(t_strRESIndex2) ) {
			if (! t_strRESIndex1.equals("?") &&   t_strRESIndex2.equals("?") ) return -1;
			if (  t_strRESIndex1.equals("?") && ! t_strRESIndex2.equals("?") ) return 1;

			return WURCSDataConverter.convertRESIndexToID(t_strRESIndex1)
					- WURCSDataConverter.convertRESIndexToID(t_strRESIndex2);
		}

		// For LIP
		return this.m_oLIPComp.compareLIP(o1, o2);
	}

}
