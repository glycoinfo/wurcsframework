package org.glycoinfo.WURCSFramework.util.array.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;

/**
 * Comparator for LIN
 * @author MasaakiMatsubara
 *
 */
public class LINComparator implements Comparator<LIN> {

	private GLIPsComparator      m_oGLIPsComp      = new GLIPsComparator();
	private MAPComparator        m_oMAPComp        = new MAPComparator();

	@Override
	public int compare(LIN o1, LIN o2) {

		// For repeat or not
		if ( !o1.isRepeatingUnit() &&  o2.isRepeatingUnit() ) return -1;
		if (  o1.isRepeatingUnit() && !o2.isRepeatingUnit() ) return 1;

		// For repeat count
		if ( o1.isRepeatingUnit() && o2.isRepeatingUnit() ) {
			int t_nRepMax1 = o1.getMaxRepeatCount();
			int t_nRepMax2 = o2.getMaxRepeatCount();
			if ( t_nRepMax1 != t_nRepMax2 )
				return this.compareRepeatCount(t_nRepMax1, t_nRepMax2);

			int t_nRepMin1 = o1.getMinRepeatCount();
			int t_nRepMin2 = o2.getMinRepeatCount();
			if ( t_nRepMin1 != t_nRepMin2 )
				return this.compareRepeatCount(t_nRepMin1, t_nRepMin2);
		}

		// For linkage has probability
		int t_nProbCount1 = this.countProbability(o1);
		int t_nProbCount2 = this.countProbability(o2);
		if ( t_nProbCount1 != t_nProbCount2 )
			return t_nProbCount1 - t_nProbCount2;

		// For unkown positions
		int t_nUnkownPosCount1 = this.countUnkownPosition(o1);
		int t_nUnkownPosCount2 = this.countUnkownPosition(o2);
		if ( t_nUnkownPosCount1 != t_nUnkownPosCount2 )
			return t_nUnkownPosCount1 - t_nUnkownPosCount2;

		// For fuzzy count
		int t_nFuzzyCount1 = this.countFuzzy(o1);
		int t_nFuzzyCount2 = this.countFuzzy(o2);
		if ( t_nFuzzyCount1 != t_nFuzzyCount2 )
			return t_nFuzzyCount1 - t_nFuzzyCount2;

		// For number of GLIPs (prioritize larger number of GLIPs)
		int t_nGLIPs1 = o1.getListOfGLIPs().size();
		int t_nGLIPs2 = o2.getListOfGLIPs().size();
		if ( t_nGLIPs1 != t_nGLIPs2 )
			return t_nGLIPs2 - t_nGLIPs1;

		// For each GLIPs
		LinkedList<GLIPs> t_aGLIPs1 = o1.getListOfGLIPs();
		LinkedList<GLIPs> t_aGLIPs2 = o2.getListOfGLIPs();
		Collections.sort(t_aGLIPs1, this.m_oGLIPsComp);
		Collections.sort(t_aGLIPs2, this.m_oGLIPsComp);
		for ( int i=0; i<t_nGLIPs1; i++ ) {
			GLIPs t_oGLIPs1 = t_aGLIPs1.get(i);
			GLIPs t_oGLIPs2 = t_aGLIPs2.get(i);
			if ( this.m_oGLIPsComp.compare(t_oGLIPs1, t_oGLIPs2) != 0 )
				return this.m_oGLIPsComp.compare(t_oGLIPs1, t_oGLIPs2);
		}

		// For MAP
		int t_iComp = this.m_oMAPComp.compare(o1.getMAPCode(), o2.getMAPCode());
		if ( t_iComp != 0 ) return t_iComp;

		return 0;
	}

	private int compareRepeatCount(int a_nRep1, int a_nRep2) {
		// For unkown count
		if ( a_nRep1 != -1 && a_nRep2 == -1 ) return -1;
		if ( a_nRep1 == -1 && a_nRep2 != -1 ) return 1;

		return a_nRep1 - a_nRep2;
	}

	private int countProbability(LIN a_oLIN) {
		int t_nProb = 0;
		for ( GLIPs t_oGLIPs : a_oLIN.getListOfGLIPs() ) {
			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				if ( t_oGLIP.getBackboneProbabilityLower() != 1 || t_oGLIP.getModificationProbabilityLower() != 1 )
					t_nProb++;
			}
		}
		return t_nProb;
	}

	private int countUnkownPosition(LIN a_oLIN) {
		int t_nUnkown = 0;
		for ( GLIPs t_oGLIPs : a_oLIN.getListOfGLIPs() ) {
			for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
				if ( t_oGLIP.getBackbonePosition() == -1 )
					t_nUnkown++;
			}
		}
		return t_nUnkown;
	}

	private int countFuzzy(LIN a_oLIN) {
		int t_nFuzzy = 0;
		for ( GLIPs t_oGLIPs : a_oLIN.getListOfGLIPs() ) {
			if ( t_oGLIPs.getGLIPs().size() > 1 )
				t_nFuzzy += t_oGLIPs.getGLIPs().size();
		}
		return t_nFuzzy;
	}
}
