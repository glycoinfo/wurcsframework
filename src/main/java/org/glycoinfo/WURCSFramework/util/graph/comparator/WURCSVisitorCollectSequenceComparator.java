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
		WURCSEdgeComparatorSimple t_oCompEdge = new WURCSEdgeComparatorSimple();

		// For first edge
		int t_iComp = t_oCompEdge.compare(o1.getEdges().getFirst(), o2.getEdges().getFirst());
		if ( t_iComp != 0 ) return t_iComp;


		// Criteria like GlycoCT

		// For node count, bigger comes first
		t_iComp = o2.getNodes().size() - o1.getNodes().size();
		if ( t_iComp != 0 ) return t_iComp;

		// For depth, deeper comes first
		t_iComp = o2.getDepth() - o1.getDepth();
		if ( t_iComp != 0 ) return t_iComp;

		// For terminal count, larger comes first
		t_iComp = o2.getTerminalCount() - o1.getTerminalCount();
		if ( t_iComp != 0 ) return t_iComp;

		// For branching point count, smaller comes first
		t_iComp = o1.getBranchCountOnBackbone() - o2.getBranchCountOnBackbone();
		if ( t_iComp != 0 ) return t_iComp;

		// For each branching point, deeper comes first
		int t_nBranch = o1.getBranchingPoints().size();
		for ( int i=0; i<t_nBranch; i++ ) {
			t_iComp = o2.getBranchingPoints().get(i) - o1.getBranchingPoints().get(i);
			if ( t_iComp != 0 ) return t_iComp;
		}
		t_iComp = o1.getBranchCountOnModification() - o2.getBranchCountOnModification();
		if ( t_iComp != 0 ) return t_iComp;


		WURCSComponentComparator t_oCompNode = new WURCSComponentComparator();

		LinkedList<WURCSEdge> t_aEdges1 = o1.getEdges();
		LinkedList<WURCSEdge> t_aEdges2 = o2.getEdges();
		LinkedList<WURCSComponent> t_aNodes1 = o1.getNodes();
		LinkedList<WURCSComponent> t_aNodes2 = o2.getNodes();

		// For each edge and node
		int t_nEdges1 = t_aEdges1.size();
		int t_nEdges2 = t_aEdges2.size();
		int t_nEdges = ( t_nEdges1 < t_nEdges2 )? t_nEdges1 : t_nEdges2;
		for ( int i=0; i<t_nEdges; i++ ) {
			// Compare edges
			WURCSEdge t_oEdge1 = t_aEdges1.get(i);
			WURCSEdge t_oEdge2 = t_aEdges2.get(i);
			t_iComp = t_oCompEdge.compare( t_oEdge1, t_oEdge2 );
			if ( t_iComp != 0 ) return t_iComp;

			// Compare nodes
			t_iComp = t_oCompNode.compare(t_aNodes1.get(i), t_aNodes2.get(i));
			if ( t_iComp != 0 ) return t_iComp;
		}

		// For node count
		if ( t_aNodes1.size() > t_aNodes2.size() ) return -1;
		if ( t_aNodes1.size() < t_aNodes2.size() ) return 1;

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
			t_iComp = new RepeatComparator().compare((InterfaceRepeat)t_oRep1, (InterfaceRepeat)t_oRep2);
			if ( t_iComp != 0 ) return t_iComp;

			// For range of repeat, larger comes first
			int t_iRange1 = this.getRange(t_oRep1, o1);
			int t_iRange2 = this.getRange(t_oRep2, o2);
			t_iComp = t_iRange2 - t_iRange1;
			if ( t_iComp != 0 ) return t_iComp;

			// For repeat start, faster comes first
			int t_iStartID1 = o1.getNodes().indexOf( this.getEndEdge(t_oRep1, o1).getBackbone() );
			int t_iStartID2 = o2.getNodes().indexOf( this.getEndEdge(t_oRep1, o2).getBackbone() );
			t_iComp = t_iStartID1 - t_iStartID2;
			if ( t_iComp != 0 ) return t_iComp;
		}

		// For leaves
		LinkedList<Modification> t_aLeaves1 = o1.getLeafModifications();
		LinkedList<Modification> t_aLeaves2 = o2.getLeafModifications();
		// Prioritize smaller count of repeating units
		t_iComp = t_aLeaves1.size() - t_aLeaves2.size();
		if ( t_iComp != 0 ) return t_iComp;

		int t_nLeaves = t_aLeaves1.size();
		for ( int i=0; i<t_nLeaves; i++ ) {
			Modification t_oLeaf1 = t_aLeaves1.get(i);
			Modification t_oLeaf2 = t_aLeaves2.get(i);

			// Check cyclic
			if ( this.checkCyclic(t_oLeaf1, o1) && this.checkCyclic(t_oLeaf2, o2) ) {
				// For range of cyclic, larger comes first
				int t_iRange1 = this.getRange(t_oLeaf1, o1);
				int t_iRange2 = this.getRange(t_oLeaf2, o2);
				t_iComp = t_iRange2 - t_iRange1;
				if ( t_iComp != 0 ) return t_iComp;
			}
		}

		// For different length, prioritize longer sequence
		if ( t_nEdges1 != t_nEdges2 ) return t_nEdges2 - t_nEdges1;

		//
		return 0;
	}

	private boolean checkCyclic(Modification a_oMod, WURCSVisitorCollectSequence a_oSeq) {
		for ( WURCSEdge t_oEdge : a_oMod.getEdges() ) {
			if ( !a_oSeq.getNodes().contains( t_oEdge.getBackbone() ) ) return false;
		}
		return true;
	}

	private int getRange(Modification a_oMod, WURCSVisitorCollectSequence a_oSeq) {
		int t_iStartID = a_oSeq.getNodes().indexOf( this.getStartEdge(a_oMod, a_oSeq).getBackbone() );
		int t_iEndID   = a_oSeq.getNodes().indexOf( this.getEndEdge(a_oMod, a_oSeq).getBackbone() );
		if ( t_iStartID == -1 || t_iEndID == -1 ) return 0;
		return t_iEndID - t_iStartID;
	}

	private WURCSEdge getStartEdge(Modification a_oMod, WURCSVisitorCollectSequence a_oSeq) {
		WURCSEdge t_oStartEdge = a_oMod.getEdges().getFirst();
		if ( t_oStartEdge.isAnomeric() ) {
			t_oStartEdge = a_oMod.getEdges().getLast();
		}
		return t_oStartEdge;
	}

	private WURCSEdge getEndEdge(Modification a_oMod, WURCSVisitorCollectSequence a_oSeq) {
		WURCSEdge t_oEndEdge = a_oMod.getEdges().getFirst();
		if ( !t_oEndEdge.isAnomeric() ) {
			t_oEndEdge = a_oMod.getEdges().getLast();
		}
		return t_oEndEdge;
	}
}
