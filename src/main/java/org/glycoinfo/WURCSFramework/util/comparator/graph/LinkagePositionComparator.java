package org.glycoinfo.WURCSFramework.util.comparator.graph;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.graph.DirectionDescriptor;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;

/**
 * Class for LinkagePosition comparison
 * @author MasaakiMatsubara
 *
 */
public class LinkagePositionComparator implements Comparator<LinkagePosition> {

	@Override
	public int compare(LinkagePosition o1, LinkagePosition o2) {

		// Compare position number on backbone
		// Prioritize lower position number
		int iPCB1 = o1.getBackbonePosition();
		int iPCB2 = o2.getBackbonePosition();
		if ( iPCB1 != iPCB2 ) return iPCB1 - iPCB2;

		// Compare string of direction on backbone carbon
		// "0" > "1" > "2" > "3" > "e" > "z" > "x"
		// N > U > D > T > E > Z > X
		DirectionDescriptor strDMB1 = o1.getDirection();
		DirectionDescriptor strDMB2 = o2.getDirection();
		if ( !strDMB1.equals(strDMB2) )
			return strDMB1.getScore() - strDMB2.getScore();

		// Compare position number on modification
		int iPCM1 = o1.getModificationPosition();
		int iPCM2 = o2.getModificationPosition();
		if ( iPCM1 != iPCM2 ) return iPCM1 - iPCM2;

		return 0;
	}

}
