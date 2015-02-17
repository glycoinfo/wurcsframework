package org.glycoinfo.WURCSFramework.util.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIPOld;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LINOld;

/**
 * Comparator for LIN
 * @author MasaakiMatsubara
 *
 */
public class LINComparatorOld implements Comparator<LINOld> {

	private GLIPComparator      m_oGLIPComp      = new GLIPComparator();
	private FuzzyGLIPComparatorOld m_oFuzzyGLIPComp = new FuzzyGLIPComparatorOld();

	@Override
	public int compare(LINOld o1, LINOld o2) {

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
		int t_nGLIP1 = o1.getGLIPs().size();
		int t_nGLIP2 = o2.getGLIPs().size();
		if ( t_nGLIP1 != t_nGLIP2 )
			return t_nGLIP2 - t_nGLIP1;

		// For number of FuzzyGLIPs (prioritize larger number of FuzzyGLIPs)
		int t_nFGLIP1 = o1.getFuzzyGLIPs().size();
		int t_nFGLIP2 = o2.getFuzzyGLIPs().size();
		if ( t_nFGLIP1 != t_nFGLIP2 )
			return t_nFGLIP2 - t_nFGLIP1;

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

		//For each FuzzyGLIP
		LinkedList<FuzzyGLIPOld> t_aFGLIPs1 = o1.getFuzzyGLIPs();
		LinkedList<FuzzyGLIPOld> t_aFGLIPs2 = o2.getFuzzyGLIPs();
		Collections.sort(t_aFGLIPs1, this.m_oFuzzyGLIPComp);
		Collections.sort(t_aFGLIPs2, this.m_oFuzzyGLIPComp);
		for ( int i=0; i<t_nFGLIP1; i++ ) {
			FuzzyGLIPOld t_oFGLIP1 = t_aFGLIPs1.get(i);
			FuzzyGLIPOld t_oFGLIP2 = t_aFGLIPs2.get(i);
			if ( this.m_oFuzzyGLIPComp.compare(t_oFGLIP1, t_oFGLIP2) != 0 )
				return this.m_oFuzzyGLIPComp.compare(t_oFGLIP1, t_oFGLIP2);
		}		return 0;
	}

	private int compareRepeatCount(int a_nRep1, int a_nRep2) {
		// For unkown count
		if ( a_nRep1 != -1 && a_nRep2 == -1 ) return -1;
		if ( a_nRep1 == -1 && a_nRep2 != -1 ) return 1;

		return a_nRep1 - a_nRep2;
	}

	private int countProbability(LINOld a_oLIN) {
		int t_nProb = 0;
		for ( GLIP t_oGLIP : a_oLIN.getGLIPs() ) {
			if ( t_oGLIP.getBackboneProbabilityLower() != 1 || t_oGLIP.getModificationProbabilityLower() != 1 )
				t_nProb++;
		}
		for ( FuzzyGLIPOld t_oFGLIP : a_oLIN.getFuzzyGLIPs() ) {
			for ( GLIP t_oGLIP : t_oFGLIP.getGLIPs() ) {
				if ( t_oGLIP.getBackboneProbabilityLower() != 1 || t_oGLIP.getModificationProbabilityLower() != 1 )
					t_nProb++;
			}
		}
		return t_nProb;
	}

	private int countUnkownPosition(LINOld a_oLIN) {
		int t_nUnkown = 0;
		for ( GLIP t_oGLIP : a_oLIN.getGLIPs() ) {
			if ( t_oGLIP.getBackbonePosition() == -1 )
				t_nUnkown++;
		}
		for ( FuzzyGLIPOld t_oFGLIP : a_oLIN.getFuzzyGLIPs() ) {
			for ( GLIP t_oGLIP : t_oFGLIP.getGLIPs() ) {
				if ( t_oGLIP.getBackbonePosition() == -1 )
					t_nUnkown++;
			}
		}
		return t_nUnkown;
	}

	private int countFuzzy(LINOld a_oLIN) {
		int t_nFuzzy = 0;
		for ( FuzzyGLIPOld t_oFGLIP : a_oLIN.getFuzzyGLIPs() ) {
			t_nFuzzy += t_oFGLIP.getGLIPs().size();
		}
		return t_nFuzzy;
	}
}
