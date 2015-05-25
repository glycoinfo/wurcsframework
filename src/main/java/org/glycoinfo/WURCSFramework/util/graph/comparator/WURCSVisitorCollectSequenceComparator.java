package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectSequence;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;

public class WURCSVisitorCollectSequenceComparator implements Comparator<WURCSVisitorCollectSequence> {

	@Override
	public int compare(WURCSVisitorCollectSequence o1, WURCSVisitorCollectSequence o2) {
		// For each edge and node
		int t_nEdges1 = o1.getEdges().size();
		int t_nEdges2 = o2.getEdges().size();
		int t_nEdges = ( t_nEdges1 < t_nEdges2 )? t_nEdges1 : t_nEdges2;

		WURCSEdgeComparatorSimple t_oCompEdge = new WURCSEdgeComparatorSimple();
		WURCSComponentComparator t_oCompNode = new WURCSComponentComparator();

		LinkedList<WURCSEdge> t_aEdges1 = o1.getEdges();
		LinkedList<WURCSEdge> t_aEdges2 = o2.getEdges();
		LinkedList<WURCSComponent> t_aNodes1 = o1.getNodes();
		LinkedList<WURCSComponent> t_aNodes2 = o2.getNodes();

		for ( int i=0; i<t_nEdges; i++ ) {
			// Compare edges
			int t_iComp = t_oCompEdge.compare(t_aEdges1.get(i), t_aEdges2.get(i));
			if ( t_iComp != 0 ) return t_iComp;

			// Compare nodes
			t_iComp = t_oCompNode.compare(t_aNodes1.get(i), t_aNodes2.get(i));
			if ( t_iComp != 0 ) return t_iComp;
		}

		// For node count
		if ( t_aNodes1.size() > t_aNodes2.size() ) return -1;
		if ( t_aNodes1.size() < t_aNodes2.size() ) return 1;

		// For branching point count
		if ( o1.getBranchCountOnBackbone() > o2.getBranchCountOnBackbone() ) return -1;
		if ( o1.getBranchCountOnBackbone() < o2.getBranchCountOnBackbone() ) return 1;
		if ( o1.getBranchCountOnModification() > o2.getBranchCountOnModification() ) return -1;
		if ( o1.getBranchCountOnModification() < o2.getBranchCountOnModification() ) return 1;

		// For repeating linkage
		LinkedList<Modification> t_aRepeats1 = o1.getRepeatModifications();
		LinkedList<Modification> t_aRepeats2 = o2.getRepeatModifications();
		// Prioritize bigger count of repeating units
		if ( t_aRepeats1.size() > t_aRepeats2.size() ) return -1;
		if ( t_aRepeats1.size() < t_aRepeats2.size() ) return 1;
		int t_iRepNum = t_aRepeats1.size();
		for ( int i=0; i<t_iRepNum; i++ ) {
			Modification t_oRep1 = t_aRepeats1.get(i);
			Modification t_oRep2 = t_aRepeats2.get(i);

			// For repeating unit
			int t_iComp = new RepeatComparator().compare((InterfaceRepeat)t_oRep1, (InterfaceRepeat)t_oRep2);
			if ( t_iComp != 0 ) return t_iComp;

			// For range of repeat
			int t_iRange1 = this.getRange(t_oRep1, o1);
			int t_iRange2 = this.getRange(t_oRep2, o2);
			// Prioritize unknown range
			if ( t_iRange1 == -1 && t_iRange2 != -1 ) return -1;
			if ( t_iRange1 != -1 && t_iRange2 == -1 ) return 1;
			// Prioritize larger range
			if ( t_iRange1 != t_iRange2 ) return t_iRange2 - t_iRange1;
		}

		// For cyclic linkage
		LinkedList<Modification> t_aCyclics1 = o1.getCyclicModifications();
		LinkedList<Modification> t_aCyclics2 = o2.getCyclicModifications();
		// Prioritize bigger count of repeating units
		if ( t_aCyclics1.size() > t_aCyclics2.size() ) return -1;
		if ( t_aCyclics1.size() < t_aCyclics2.size() ) return 1;
		int t_iCyclicNum = t_aCyclics1.size();
		for ( int i=0; i<t_iCyclicNum; i++ ) {
			Modification t_oCyclic1 = t_aCyclics1.get(i);
			Modification t_oCyclic2 = t_aCyclics2.get(i);

			// For range of repeat
			int t_iRange1 = this.getRange(t_oCyclic1, o1);
			int t_iRange2 = this.getRange(t_oCyclic2, o2);
			// Prioritize unknown range
			if ( t_iRange1 == -1 && t_iRange2 != -1 ) return -1;
			if ( t_iRange1 != -1 && t_iRange2 == -1 ) return 1;
			// Prioritize larger range
			if ( t_iRange1 != t_iRange2 ) return t_iRange2 - t_iRange1;
		}

		// For different length, prioritize longer sequence
		if ( t_nEdges1 != t_nEdges2 ) return t_nEdges2 - t_nEdges1;

		//
		return 0;
	}

	private int getRange(Modification a_oRepeat, WURCSVisitorCollectSequence a_oSeq) {
//		LinkedList<WURCSComponent> t_aNodes = a_oSeq.getNodes();
//		WURCSComponent t_oRepB1 = a_oRepeat.getEdges().getFirst().getBackbone();
//		WURCSComponent t_oRepB2 = a_oRepeat.getEdges().getFirst().getBackbone();

		int t_iBID1 = a_oSeq.getNodes().indexOf( a_oRepeat.getEdges().getFirst().getBackbone() );
		int t_iBID2 = a_oSeq.getNodes().indexOf( a_oRepeat.getEdges().getLast().getBackbone() );
		if ( t_iBID1 == -1 || t_iBID2 == -1 ) return -1;
		if ( t_iBID1 < t_iBID2 ) return t_iBID2 - t_iBID1;
		if ( t_iBID1 > t_iBID2 ) return t_iBID1 - t_iBID2;
		return 0;
	}
}
