package org.glycoinfo.WURCSFramework.util.array.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;

/**
 * Comparator for LIP
 * @author MasaakiMatsubara
 *
 */
public class LIPComparator implements Comparator<LIP> {

	@Override
	public int compare(LIP o1, LIP o2) {
		// For probabilities
		if ( this.compareProbabilities(o1, o2) != 0 )
			return this.compareProbabilities(o1, o2);

		return this.compareLIP(o1, o2);
	}

	public int compareLIP(LIP o1, LIP o2) {

		// For Backbone position
		int t_iSCPos1 = o1.getBackbonePosition();
		int t_iSCPos2 = o2.getBackbonePosition();
		if ( t_iSCPos1 != t_iSCPos2 ) {
			if ( t_iSCPos1 != -1 && t_iSCPos2 == -1 ) return -1;
			if ( t_iSCPos1 == -1 && t_iSCPos2 != -1 ) return 1;
			return t_iSCPos1 - t_iSCPos2;
		}

		// For Direction
		char t_cDirection1 = o1.getBackboneDirection();
		char t_cDirection2 = o2.getBackboneDirection();
		if ( t_cDirection1 != t_cDirection2 )
			return WURCSDataConverter.convertDirectionToID(t_cDirection1)
					- WURCSDataConverter.convertDirectionToID(t_cDirection2);

		// For MAP postition
		int t_iMAPPos1 = o1.getModificationPosition();
		int t_iMAPPos2 = o2.getModificationPosition();
		if ( t_iMAPPos1 != t_iMAPPos2 )
			return t_iMAPPos1-t_iMAPPos2;

		return 0;
	}

	public int compareProbabilities(LIP o1, LIP o2) {
		// For probabilities
		double t_dBProbLower1 = o1.getBackboneProbabilityLower();
		double t_dBProbLower2 = o2.getBackboneProbabilityLower();
		if ( t_dBProbLower1 != t_dBProbLower2 )
			return this.compareProbabilities(t_dBProbLower1, t_dBProbLower2);

		double t_dBProbUpper1 = o1.getBackboneProbabilityUpper();
		double t_dBProbUpper2 = o2.getBackboneProbabilityUpper();
		if ( t_dBProbUpper1 != t_dBProbUpper2 )
			return this.compareProbabilities(t_dBProbUpper1, t_dBProbUpper2);

		double t_dMProbLower1 = o1.getModificationProbabilityLower();
		double t_dMProbLower2 = o2.getModificationProbabilityLower();
		if ( t_dMProbLower1 != t_dMProbLower2 )
			return this.compareProbabilities(t_dMProbLower1, t_dMProbLower2);

		double t_dMProbUpper1 = o1.getModificationProbabilityUpper();
		double t_dMProbUpper2 = o2.getModificationProbabilityUpper();
		if ( t_dMProbUpper1 != t_dMProbUpper2 )
			return this.compareProbabilities(t_dMProbUpper1, t_dMProbUpper2);

		return 0;
	}

	private int compareProbabilities(double p1, double p2) {
		// For unkown probability
		if ( p1 != -1 && p2 == -1 ) return -1;
		if ( p1 == -1 && p2 != -1 ) return 1;

		if ( p1 > p2 ) return -1;
		if ( p1 < p2 ) return 1;

		return 0;
	}
}
