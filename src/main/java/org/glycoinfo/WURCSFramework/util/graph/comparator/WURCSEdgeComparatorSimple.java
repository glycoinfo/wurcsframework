package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;

/**
 * Class for WURCSEdge comparison only linkage information
 * @author MassaakiMatsubara
 *
 */
public class WURCSEdgeComparatorSimple implements Comparator<WURCSEdge> {

	@Override
	public int compare(WURCSEdge o1, WURCSEdge o2) {
		// For null edge
		if ( o1 != null && o2 == null ) return -1;
		if ( o1 == null && o2 != null ) return 1;
		if ( o1 == null && o2 == null ) return 0;

		// For unknown LinkagePosition, not unknown comes first
		int t_iPos1 = o1.getLinkages().getFirst().getBackbonePosition();
		int t_iPos2 = o2.getLinkages().getFirst().getBackbonePosition();
//		if ( t_iPos1 != -1 && t_iPos2 == -1) return -1;
//		if ( t_iPos1 == -1 && t_iPos2 != -1) return 1;
//		if ( t_iPos1 == -1 && t_iPos2 == -1) return 0;

		// In GlycoCT criteria unknown comes first
		// TODO: check priority
		if ( t_iPos1 != -1 && t_iPos2 == -1) return 1;
		if ( t_iPos1 == -1 && t_iPos2 != -1) return -1;
		if ( t_iPos1 == -1 && t_iPos2 == -1) return 0;

		// For size of LinkagePosition, smaller (not fuzzier) comes first
//		if (o1.getLinkages().size() < o2.getLinkages().size()) return -1;
//		if (o1.getLinkages().size() > o2.getLinkages().size()) return 1;

		// For GlycoCT criteria unknown comes first
		// TODO: check priority
		if (o1.getLinkages().size() < o2.getLinkages().size()) return 1;
		if (o1.getLinkages().size() > o2.getLinkages().size()) return -1;

		// Compare linkages inside WURCSEdges if of equal size
		LinkedList<LinkagePosition> t_aLinkages1 = o1.getLinkages();
		LinkedList<LinkagePosition> t_aLinkages2 = o2.getLinkages();
		LinkagePositionComparator t_oLinkComp = new LinkagePositionComparator();
		Collections.sort( t_aLinkages1, t_oLinkComp );
		Collections.sort( t_aLinkages2, t_oLinkComp );

		// For linkages
		int t_nLinkSize = t_aLinkages1.size();
		for (int i = 0; i < t_nLinkSize; i++) {
			int t_iComp = t_oLinkComp.compare( t_aLinkages1.get(i), t_aLinkages2.get(i) );
			if ( t_iComp == 0 ) continue;
			return t_iComp;
		}

		return 0;
	}
}