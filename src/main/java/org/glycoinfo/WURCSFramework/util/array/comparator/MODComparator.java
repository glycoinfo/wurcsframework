package org.glycoinfo.WURCSFramework.util.array.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;

/**
 * Comaprator for MOD
 * @author MasaakiMatsubara
 *
 */
public class MODComparator implements Comparator<MOD> {

	private LIPsComparator      m_oLIPsComp      = new LIPsComparator();

	@Override
	public int compare(MOD o1, MOD o2) {
		// For number of LIPs (prioritize larger number of LIPs)
		int t_nLIPs1 = o1.getListOfLIPs().size();
		int t_nLIPs2 = o2.getListOfLIPs().size();
		if ( t_nLIPs1 != t_nLIPs2 )
			return t_nLIPs2 - t_nLIPs1;

/*
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
*/

		// For each LIP
		LinkedList<LIPs> t_aLIPs1 = o1.getListOfLIPs();
		LinkedList<LIPs> t_aLIPs2 = o2.getListOfLIPs();
		Collections.sort(t_aLIPs1, this.m_oLIPsComp);
		Collections.sort(t_aLIPs2, this.m_oLIPsComp);
		for ( int i=0; i<t_nLIPs1; i++ ) {
			LIPs t_oLIPs1 = t_aLIPs1.get(i);
			LIPs t_oLIPs2 = t_aLIPs2.get(i);
			if ( this.m_oLIPsComp.compare(t_oLIPs1, t_oLIPs2) != 0 )
				return this.m_oLIPsComp.compare(t_oLIPs1, t_oLIPs2);
		}

		return 0;
	}

	private int countProbability(MOD a_oMOD) {
		int t_nProb = 0;
		for ( LIPs t_oLIPs : a_oMOD.getListOfLIPs() ) {
			for ( LIP t_oLIP : t_oLIPs.getLIPs() ) {
				if ( t_oLIP.getBackboneProbabilityLower() != 1 || t_oLIP.getModificationProbabilityLower() != 1 )
					t_nProb++;
			}
		}
		return t_nProb;
	}

	private int countUnkownPosition(MOD a_oMOD) {
		int t_nUnkown = 0;
		for ( LIPs t_oLIPs : a_oMOD.getListOfLIPs() ) {
			for ( LIP t_oLIP : t_oLIPs.getLIPs() ) {
				if ( t_oLIP.getBackbonePosition() == -1 )
					t_nUnkown++;
			}
		}
		return t_nUnkown;
	}

	private int countFuzzy(MOD a_oMOD) {
		int t_nFuzzy = 0;
		for ( LIPs t_oLIPs : a_oMOD.getListOfLIPs() ) {
			if ( t_oLIPs.getLIPs().size() > 1 )
				t_nFuzzy += t_oLIPs.getLIPs().size();
		}
		return t_nFuzzy;
	}

}
