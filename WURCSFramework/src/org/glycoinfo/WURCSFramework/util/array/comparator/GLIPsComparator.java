package org.glycoinfo.WURCSFramework.util.array.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;

/**
 * Comparator for FuzzyGLIP
 * @author MasaakiMatsubara
 *
 */
public class GLIPsComparator implements Comparator<GLIPs> {

	private GLIPComparator m_oGLIPComp = new GLIPComparator();

	@Override
	public int compare(GLIPs o1, GLIPs o2) {
		// For number of GLIP (prioritize smaller number of GLIP)
		int t_nGLIP1 = o1.getGLIPs().size();
		int t_nGLIP2 = o2.getGLIPs().size();
		if ( t_nGLIP1 != t_nGLIP2 )
			return t_nGLIP1 - t_nGLIP2;

		// For each GLIP
		LinkedList<GLIP> t_aGLIPs1 = o1.getGLIPs();
		LinkedList<GLIP> t_aGLIPs2 = o2.getGLIPs();
		Collections.sort(t_aGLIPs1, this.m_oGLIPComp);
		Collections.sort(t_aGLIPs2, this.m_oGLIPComp);
		for ( int i=0; i<t_nGLIP1; i++ ) {
			GLIP t_oGLIP1 = t_aGLIPs1.get(i);
			GLIP t_oGLIP2 = t_aGLIPs2.get(i);
			if ( this.m_oGLIPComp.compare(t_oGLIP1, t_oGLIP2) != 0 )
				return this.m_oGLIPComp.compare(t_oGLIP1, t_oGLIP2);
		}

		return 0;
	}

}
