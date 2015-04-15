package org.glycoinfo.WURCSFramework.util.comparator.graph;

import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.graph.Backbone;
import org.glycoinfo.WURCSFramework.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.graph.WURCSEdge;

/**
 * Class for Backbone comparison
 * @author MasaakiMatsubara
 */
public class BackboneComparator implements Comparator<Backbone> {

	@Override
	public int compare(Backbone b1, Backbone b2) {

		// For root nodes
		WURCSEdge t_oAnomEdge1 = b1.getAnomericEdge();
		WURCSEdge t_oAnomEdge2 = b2.getAnomericEdge();
		boolean isRoot1 = ( t_oAnomEdge1 == null || t_oAnomEdge1.getModification().isAglycone() );
		boolean isRoot2 = ( t_oAnomEdge2 == null || t_oAnomEdge2.getModification().isAglycone() );
		// Prioritize root
		if (  isRoot1 && !isRoot2  ) return -1;
		if ( !isRoot1 &&  isRoot2  ) return 1;

		// For number of parent backbone
		LinkedList<Backbone> t_aParents1 = new LinkedList<Backbone>();
		LinkedList<Backbone> t_aParents2 = new LinkedList<Backbone>();
		if ( !isRoot1 )
			for ( WURCSEdge parentEdge : t_oAnomEdge1.getModification().getEdges() )
				t_aParents1.add( parentEdge.getBackbone() );
		if ( !isRoot2 )
			for ( WURCSEdge parentEdge : t_oAnomEdge2.getModification().getEdges() )
				t_aParents2.add( parentEdge.getBackbone() );
		// Prioritize smaller number of parent
		if ( t_aParents1.size() != t_aParents2.size() )
			return t_aParents1.size() - t_aParents2.size();


		// For number of connected backbone and modification
		// If a backbone connected to this backbone with anomeric position, the backbone is child.
		/*
		 *  B0->M1-A->B      ---> : edge to non-anomeric position of other backbone
		 *   |   +--->B      -A-> : edge to anomeric position of other backbone
		 *   |   +-A->B      "-A->" means edge to child backbone from B0
		 *   +->M2--->B
		 *   +->M3-A->B      In the case, there is 3 child of 5 connected backbone
		 */
		LinkedList<WURCSEdge> t_aGlycosidicLinkages1 = new LinkedList<WURCSEdge>();
		LinkedList<WURCSEdge> t_aGlycosidicLinkages2 = new LinkedList<WURCSEdge>();
		LinkedList<WURCSEdge> t_aSubstituentLinkages1 = new LinkedList<WURCSEdge>();
		LinkedList<WURCSEdge> t_aSubstituentLinkages2 = new LinkedList<WURCSEdge>();
		int t_nChildCount1 = 0;
		int t_nChildCount2 = 0;
		int t_nBackboneCount1 = 0;
		int t_nBackboneCount2 = 0;
		int t_nModificationCount1 = 0;
		int t_nModificationCount2 = 0;
		for ( WURCSEdge edgeB2M1 : b1.getEdges() ) {
			// Ignore parent edge
			if ( edgeB2M1.equals(t_oAnomEdge1) ) continue;
			for ( WURCSEdge edgeM2B1 : edgeB2M1.getModification().getEdges() ) {
				// Ignore reverse edge
				if ( edgeM2B1.equals(edgeB2M1) ) continue;
				t_nBackboneCount1++;
				if ( edgeM2B1.getBackbone().getAnomericEdge() != null &&
					!edgeM2B1.getBackbone().getAnomericEdge().equals(edgeM2B1) ) continue;
				t_nChildCount1++;
			}
			// Collect glycosidic linkages
			if ( edgeB2M1.getModification().isGlycosidic() ) {
				t_aGlycosidicLinkages1.addLast(edgeB2M1);
				continue;
			}
			// Ignore modification which can omit
			if ( edgeB2M1.getModification().canOmitMAP() ) continue;
			t_aSubstituentLinkages1.addLast(edgeB2M1);
			t_nModificationCount1++;
		}
		for ( WURCSEdge edgeB2M2 : b2.getEdges() ) {
			// Ignore parent edge
			if ( edgeB2M2.equals(t_oAnomEdge2) ) continue;
			for ( WURCSEdge edgeM2B2 : edgeB2M2.getModification().getEdges() ) {
				// Ignore reverse edge
				if ( edgeM2B2.equals(edgeB2M2) ) continue;
				t_nBackboneCount2++;
				if ( edgeM2B2.getBackbone().getAnomericEdge() != null &&
					!edgeM2B2.getBackbone().getAnomericEdge().equals(edgeM2B2) ) continue;
				t_nChildCount2++;
			}
			// Collect glycosidic linkages
			if ( edgeB2M2.getModification().isGlycosidic() ) {
				t_aGlycosidicLinkages2.addLast(edgeB2M2);
				continue;
			}
			// Ignore modification which can omit
			if ( edgeB2M2.getModification().canOmitMAP() ) continue;
			t_aSubstituentLinkages2.addLast(edgeB2M2);
			t_nModificationCount2++;
		}
		// Prioritize larger number of child backbone
		if ( t_nChildCount1 != t_nChildCount2 )
			return t_nChildCount2 - t_nChildCount1;
		// Prioritize larger number of connected backbone
		if ( t_nBackboneCount1 != t_nBackboneCount2 )
			return t_nBackboneCount2 - t_nBackboneCount1;
		// Prioritize larger number of connected modification
		if ( t_nModificationCount1 != t_nModificationCount2 )
			return t_nModificationCount2 - t_nModificationCount1;


		// For ambiguousness of carbon number
		// Prioritize no unknown length
		if ( !b1.hasUnknownLength() &&  b2.hasUnknownLength() ) return -1;
		if (  b1.hasUnknownLength() && !b2.hasUnknownLength() ) return 1;

		// For backbone length
		// Prioritize longer backbone
		if ( b1.getBackboneCarbons().size() != b2.getBackboneCarbons().size() )
			return b2.getBackboneCarbons().size() - b1.getBackboneCarbons().size();

		// For score of backbone carbons
		// Compare backbone scores
		int score1 = this.getBackboneScore(b1);
		int score2 = this.getBackboneScore(b2);
		// Prioritize smaller score
		if ( score1 != score2 ) return score1 - score2;

		// For position of modification
		score1 = 0;
		score2 = 0;
		for ( WURCSEdge edge : t_aSubstituentLinkages1 )
			score1 += edge.getLinkages().getFirst().getBackbonePosition();
		for ( WURCSEdge edge : t_aSubstituentLinkages2 )
			score2 += edge.getLinkages().getFirst().getBackbonePosition();
		// Prioritize smaller score
		if ( score1 != score2 ) return score1 - score2;

		// TODO: Compare position of glycosidic linkage
		score1 = 0;
		score2 = 0;
		for ( WURCSEdge edge : t_aGlycosidicLinkages1 )
			score1 += edge.getLinkages().getFirst().getBackbonePosition();
		for ( WURCSEdge edge : t_aGlycosidicLinkages2 )
			score2 += edge.getLinkages().getFirst().getBackbonePosition();
		// Prioritize larger score
		if ( score1 != score2 ) return score2 - score1;

		return 0;
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
