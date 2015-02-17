package org.glycoinfo.WURCSFramework.util.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.FuzzyLIPOld;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MODOld;

/**
 * Comaprator for MOD
 * @author MasaakiMatsubara
 *
 */
public class MODComparatorOld implements Comparator<MODOld> {

	private LIPComparator      m_oLIPComp      = new LIPComparator();
	private FuzzyLIPComparatorOld m_oFuzzyLIPComp = new FuzzyLIPComparatorOld();

	@Override
	public int compare(MODOld o1, MODOld o2) {
		// For number of LIPs (prioritize larger number of LIPs)
		int t_nLIP1 = o1.getLIPs().size();
		int t_nLIP2 = o2.getLIPs().size();
		if ( t_nLIP1 != t_nLIP2 )
			return t_nLIP2 - t_nLIP1;

		// For number of FuzzyLIPs (prioritize larger number of FuzzyLIPs)
		int t_nFLIP1 = o1.getFuzzyLIPs().size();
		int t_nFLIP2 = o2.getFuzzyLIPs().size();
		if ( t_nFLIP1 != t_nFLIP2 )
			return t_nFLIP2 - t_nFLIP1;

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
		LinkedList<LIP> t_aLIPs1 = o1.getLIPs();
		LinkedList<LIP> t_aLIPs2 = o2.getLIPs();
		Collections.sort(t_aLIPs1, this.m_oLIPComp);
		Collections.sort(t_aLIPs2, this.m_oLIPComp);
		for ( int i=0; i<t_nLIP1; i++ ) {
			LIP t_oLIP1 = t_aLIPs1.get(i);
			LIP t_oLIP2 = t_aLIPs2.get(i);
			if ( this.m_oLIPComp.compare(t_oLIP1, t_oLIP2) != 0 )
				return this.m_oLIPComp.compare(t_oLIP1, t_oLIP2);
		}

		//For each FuzzyLIP
		LinkedList<FuzzyLIPOld> t_aFLIPs1 = o1.getFuzzyLIPs();
		LinkedList<FuzzyLIPOld> t_aFLIPs2 = o2.getFuzzyLIPs();
		Collections.sort(t_aFLIPs1, this.m_oFuzzyLIPComp);
		Collections.sort(t_aFLIPs2, this.m_oFuzzyLIPComp);
		for ( int i=0; i<t_nFLIP1; i++ ) {
			FuzzyLIPOld t_oFLIP1 = t_aFLIPs1.get(i);
			FuzzyLIPOld t_oFLIP2 = t_aFLIPs2.get(i);
			if ( this.m_oFuzzyLIPComp.compare(t_oFLIP1, t_oFLIP2) != 0 )
				return this.m_oFuzzyLIPComp.compare(t_oFLIP1, t_oFLIP2);
		}

		return 0;
	}

	private int countProbability(MODOld a_oMOD) {
		int t_nProb = 0;
		for ( LIP t_oLIP : a_oMOD.getLIPs() ) {
			if ( t_oLIP.getBackboneProbabilityLower() != 1 || t_oLIP.getModificationProbabilityLower() != 1 )
				t_nProb++;
		}
		for ( FuzzyLIPOld t_oFLIP : a_oMOD.getFuzzyLIPs() ) {
			for ( LIP t_oLIP : t_oFLIP.getLIPs() ) {
				if ( t_oLIP.getBackboneProbabilityLower() != 1 || t_oLIP.getModificationProbabilityLower() != 1 )
					t_nProb++;
			}
		}
		return t_nProb;
	}

	private int countUnkownPosition(MODOld a_oMOD) {
		int t_nUnkown = 0;
		for ( LIP t_oLIP : a_oMOD.getLIPs() ) {
			if ( t_oLIP.getBackbonePosition() == -1 )
				t_nUnkown++;
		}
		for ( FuzzyLIPOld t_oFLIP : a_oMOD.getFuzzyLIPs() ) {
			for ( LIP t_oLIP : t_oFLIP.getLIPs() ) {
				if ( t_oLIP.getBackbonePosition() == -1 )
					t_nUnkown++;
			}
		}
		return t_nUnkown;
	}

	private int countFuzzy(MODOld a_oMOD) {
		int t_nFuzzy = 0;
		for ( FuzzyLIPOld t_oFLIP : a_oMOD.getFuzzyLIPs() ) {
			t_nFuzzy += t_oFLIP.getLIPs().size();
		}
		return t_nFuzzy;
	}

}
