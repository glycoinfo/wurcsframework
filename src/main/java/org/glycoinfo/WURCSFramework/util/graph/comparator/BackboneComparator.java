package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneUnknown;

/**
 * Class for Backbone comparison
 * @author MasaakiMatsubara
 */
public class BackboneComparator implements Comparator<Backbone> {

	@Override
	public int compare(Backbone b1, Backbone b2) {

		// For unknown monosaccharide
		if ( !(b1 instanceof BackboneUnknown) &&  (b2 instanceof BackboneUnknown) ) return -1;
		if (  (b1 instanceof BackboneUnknown) && !(b2 instanceof BackboneUnknown) ) return 1;


		// For ambiguousness of carbon number
		// Prioritize known length
		if ( !b1.hasUnknownLength() &&  b2.hasUnknownLength() ) return -1;
		if (  b1.hasUnknownLength() && !b2.hasUnknownLength() ) return 1;

		// For backbone length
		// Prioritize longer backbone
		int t_iComp = b2.getBackboneCarbons().size() - b1.getBackboneCarbons().size();
		if ( t_iComp != 0 ) return t_iComp;

		// For score of backbone carbons
		// Compare backbone scores
		t_iComp = this.compareBackboneScore(b1, b2);
		if ( t_iComp != 0 ) return t_iComp;

		// For same score symmetry backbone (not same SkeletonCode)
		String strCode1 = b1.getSkeletonCode();
		String strCode2 = b2.getSkeletonCode();
		// Prioritize larger string
		if ( !strCode1.equals(strCode2) ) return strCode2.compareTo(strCode1);

		return 0;
	}

	public int compareBackboneScore(Backbone b1, Backbone b2) {
		int score1 = this.getBackboneScore(b1);
		int score2 = this.getBackboneScore(b2);
		// Prioritize smaller score
		return score1 - score2;
	}

	/**
	 * Calculate Backbone scores based on carbon score of CarbonDescriptor
	 * @param b Backbone
	 * @return Integer of backbone score
	 */
	private int getBackboneScore(Backbone b) {
		int score = 0;
		LinkedList<BackboneCarbon> t_aBCs = b.getBackboneCarbons();
		for ( int i=0; i<t_aBCs.size(); i++ )
			score += (i+1) * t_aBCs.get(i).getDesctriptor().getCarbonScore();
		return score;
	}
}
