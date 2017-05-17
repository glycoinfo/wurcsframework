package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectSequence;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Monosaccharide;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;

public class MonosaccharideComparatorForInvertBackbone implements Comparator<Monosaccharide>  {

	@Override
	public int compare(Monosaccharide MS1, Monosaccharide MS2) {

		// Compare backbone
		Backbone b1 = MS1.getBackbone();
		Backbone b2 = MS2.getBackbone();
		BackboneComparator t_oBComp = new BackboneComparator();
		int t_iComp = t_oBComp.compare(b1, b2);
		if ( t_iComp != 0 ) return t_iComp;

		// Compare edges around backbone
		WURCSEdgeComparator t_oCompEdge = new WURCSEdgeComparator();
		int score1 = 0;
		int score2 = 0;


		// For modification
		LinkedList<WURCSEdge> t_aSubstLinkages1 = MS1.getSubstituentEdges();
		LinkedList<WURCSEdge> t_aSubstLinkages2 = MS2.getSubstituentEdges();
		int t_nSubstCount1 = t_aSubstLinkages1.size();
		int t_nSubstCount2 = t_aSubstLinkages2.size();
		// Prioritize larger number of connected modification
		if ( t_nSubstCount1 != t_nSubstCount2 )
			return t_nSubstCount2 - t_nSubstCount1;

		Collections.sort(t_aSubstLinkages1, t_oCompEdge);
		Collections.sort(t_aSubstLinkages2, t_oCompEdge);
		// For position of modification
		// TODO: add factor of MAP score
		score1 = 0;
		score2 = 0;
		for ( WURCSEdge edge : t_aSubstLinkages1 )
			score1 += edge.getLinkages().getFirst().getBackbonePosition();
		for ( WURCSEdge edge : t_aSubstLinkages2 )
			score2 += edge.getLinkages().getFirst().getBackbonePosition();
		// Prioritize smaller score
		t_iComp = score1 - score2;
		// XXX: remove print
		System.err.println(MS1.getClass()+":posM:"+t_iComp);
		if ( t_iComp != 0 ) return t_iComp;


		// For glycosidic
		int t_nParentCount1 = b1.getParentEdges().size();
		int t_nParentCount2 = b2.getParentEdges().size();
		// Prioritize smaller number of parent backbone
		t_iComp = t_nParentCount1 - t_nParentCount2;
		if ( t_iComp != 0 ) return t_iComp;

		// For position of parent linkage
		for ( WURCSEdge edge : b1.getParentEdges() ) {
			int pos1 = edge.getLinkages().getFirst().getBackbonePosition();
			score1 += pos1;
		}
		for ( WURCSEdge edge : b2.getParentEdges() ) {
			int pos2 = edge.getLinkages().getFirst().getBackbonePosition();
			score2 += pos2;
		}
		// Prioritize smaller score
		t_iComp = score1 - score2;
		if ( t_iComp != 0 ) return t_iComp;

		LinkedList<WURCSEdge> t_aGlycoLinkages1 = MS1.getChildGlycosidicEdges();
		LinkedList<WURCSEdge> t_aGlycoLinkages2 = MS2.getChildGlycosidicEdges();
		int t_nChildCount1 = t_aGlycoLinkages1.size();
		int t_nChildCount2 = t_aGlycoLinkages2.size();
		// Prioritize smaller number of child backbone
		t_iComp = t_nChildCount1 - t_nChildCount2;
		if ( t_iComp != 0 ) return t_iComp;

		Collections.sort(t_aGlycoLinkages1, t_oCompEdge);
		Collections.sort(t_aGlycoLinkages2, t_oCompEdge);
		// For position of child glycosidic linkage
		score1 = 0;
		score2 = 0;
		for ( WURCSEdge edge : t_aGlycoLinkages1 ) {
			int pos1 = edge.getLinkages().getFirst().getBackbonePosition();
			score1 += pos1;
		}
		for ( WURCSEdge edge : t_aGlycoLinkages2 ) {
			int pos2 = edge.getLinkages().getFirst().getBackbonePosition();
			score2 += pos2;
		}
		// Prioritize larger score
		t_iComp = score2 - score1;
		if ( t_iComp != 0 ) return t_iComp;


		// For child sequences
		WURCSVisitorCollectSequenceComparator t_oCompSeq = new WURCSVisitorCollectSequenceComparator();
		for ( int i=0, n=t_aGlycoLinkages1.size(); i<n; i++ ) {
			WURCSEdge t_oEdge1 = t_aGlycoLinkages1.get(i);
			WURCSEdge t_oEdge2 = t_aGlycoLinkages2.get(i);
			try {
				WURCSVisitorCollectSequence t_oSeq1 = new WURCSVisitorCollectSequence();
				t_oSeq1.start(t_oEdge1);
				WURCSVisitorCollectSequence t_oSeq2 = new WURCSVisitorCollectSequence();
				t_oSeq2.start(t_oEdge2);
				t_iComp = t_oCompSeq.compare(t_oSeq1, t_oSeq2);
			} catch (WURCSVisitorException e) {
				e.printStackTrace();
			}
			if ( t_iComp != 0 ) return t_iComp;
		}

		return 0;
	}

}
