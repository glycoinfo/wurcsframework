package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;

/**
 * Class for Modificaiton comparison
 * @author MasaakiMatsubara
 * TODO:
 */
public class ModificationComparator implements Comparator<Modification> {

	@Override
	public int compare(Modification m1, Modification m2) {

		// For MAPs
		String t_strMAP1 = m1.getMAPCode();
		String t_strMAP2 = m2.getMAPCode();

		// For branching point
//		if (  )

		// For edge count
		int edgeCount1 = m1.getEdges().size();
		int edgeCount2 = m2.getEdges().size();
		if ( edgeCount1 != edgeCount2 ) return edgeCount1 - edgeCount2;
		return 0;
	}

	private int countBackboneCarbonInMAP(String a_strMAP) {
		int t_nBC = 0;
		for ( int i=0; i<a_strMAP.length(); i++ ) {
			char t_cCD = a_strMAP.charAt(i);
			if ( t_cCD == '*' ) t_nBC++;
		}
		return t_nBC;
	}
}
