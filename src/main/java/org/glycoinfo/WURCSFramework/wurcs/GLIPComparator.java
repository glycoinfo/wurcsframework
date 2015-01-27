package org.glycoinfo.WURCSFramework.wurcs;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;

/**
 * Comparator for GLIP
 * @author MasaakiMatsubara
 *
 */
public class GLIPComparator implements Comparator<GLIP>{

	@Override
	public int compare(GLIP o1, GLIP o2) {
		// For RES Index
		String t_strRESIndex1 = o1.getRESIndex();
		String t_strRESIndex2 = o2.getRESIndex();
		if (! t_strRESIndex1.equals(t_strRESIndex2) ) {
			if (! t_strRESIndex1.equals("?") &&   t_strRESIndex2.equals("?") ) return -1;
			if (  t_strRESIndex1.equals("?") && ! t_strRESIndex2.equals("?") ) return 1;

			return WURCSDataConverter.convertRESIndexToID(t_strRESIndex1)
					- WURCSDataConverter.convertRESIndexToID(t_strRESIndex2);
		}

		LIPComparator t_oLIPComp = new LIPComparator();
		return t_oLIPComp.compare(o1, o2);
	}

}
