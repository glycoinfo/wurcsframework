package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;

/**
 * Class for WURCSEdge comparison
 * @author MassaakiMatsubara
 *
 */
public class WURCSEdgeComparator implements Comparator<WURCSEdge> {

	@Override
	public int compare(WURCSEdge o1, WURCSEdge o2) {
		// For reverse edge (prioritize parent side whitch is not reverse)
		if ( !o1.isReverse() &&  o2.isReverse() ) return -1;
		if (  o1.isReverse() && !o2.isReverse() ) return 1;

		// For unknown LinkagePosition, not unknown comes first
		int t_iPos1 = o1.getLinkages().getFirst().getBackbonePosition();
		int t_iPos2 = o2.getLinkages().getFirst().getBackbonePosition();
		if ( t_iPos1 != -1 && t_iPos2 == -1) return -1;
		if ( t_iPos1 == -1 && t_iPos2 != -1) return 1;

		// For size of LinkagePosition, smaller comes first
		if (o1.getLinkages().size() < o2.getLinkages().size()) return -1;
		if (o1.getLinkages().size() > o2.getLinkages().size()) return 1;

		// If of equal size, compare linkages inside WURCSEdges
		LinkedList<LinkagePosition> t_aLinkages1 = o1.getLinkages();
		LinkedList<LinkagePosition> t_aLinkages2 = o2.getLinkages();
		int t_iCompPos = this.compareLinkagePositions(t_aLinkages1, t_aLinkages2);
		if ( t_iCompPos != 0 ) return t_iCompPos;

		// Compare connected Backbone
		if ( !o1.getBackbone().equals(o2.getBackbone()) ) {
			BackboneComparator t_oBComp = new BackboneComparator();
			return t_oBComp.compare(o1.getBackbone(), o2.getBackbone());
		}

		// Compare connected Modification
		if ( !o1.getModification().equals(o2.getModification()) ) {
			ModificationComparator t_oMComp = new ModificationComparator();
			return t_oMComp.compare(o1.getModification(), o2.getModification());
		}

		return 0;
	}

	public int compareLinkagePositions(LinkedList<LinkagePosition> a_aLinkages1, LinkedList<LinkagePosition> a_aLinkages2) {
		LinkagePositionComparator t_oLinkComp = new LinkagePositionComparator();
		Collections.sort( a_aLinkages1, t_oLinkComp );
		Collections.sort( a_aLinkages2, t_oLinkComp );

		// For linkages
		for (int i = 0; i < a_aLinkages1.size(); i++) {
			if ( t_oLinkComp.compare( a_aLinkages1.get(i), a_aLinkages2.get(i) ) == 0 ) continue;
			return t_oLinkComp.compare( a_aLinkages1.get(i), a_aLinkages2.get(i) );
		}
		return 0;
	}
}
