package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.Comparator;

/**
 * Comparator for atomic weight
 * @author MasaakiMatsubara
 *
 */
public class WeightComparator implements Comparator<Double> {

	private final double EPS = 0.000000001D;

	@Override
	public int compare(Double a_dWeight1, Double a_dWeight2) {
		// Return 0 if either of weights is null
		if ( a_dWeight1==null || a_dWeight2==null ) return 0;

		// Prioritize higher weight
		Double t_dWeightComp = a_dWeight1 - a_dWeight2;
		if ( Math.abs(t_dWeightComp) < EPS ) return 0;
		if ( t_dWeightComp > 0 ) return -1;
		if ( t_dWeightComp < 0 ) return 1;
		return 0;
	}

}
