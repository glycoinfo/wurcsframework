package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.Monosaccharide;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;


/**
 * Comparator for a monosaccharide composed of component around a backbone
 * @author MasaakiMatsubara
 *
 */
public class MonosaccharideComparator implements Comparator<Monosaccharide> {

	@Override
	public int compare(Monosaccharide MS1, Monosaccharide MS2) {
		// For alternative root
		if ( !MS1.checkAroundAlternative() &&  MS2.checkAroundAlternative() ) return -1;
		if (  MS1.checkAroundAlternative() && !MS2.checkAroundAlternative() ) return 1;

		// Compare backbone
		Backbone b1 = MS1.getBackbone();
		Backbone b2 = MS2.getBackbone();
		BackboneComparator t_oBComp = new BackboneComparator();
		int t_iComp = t_oBComp.compare(b1, b2);
		if ( t_iComp != 0 ) return t_iComp;

		// For ring modification
		LinkedList<Modification> t_aRingMod1 = MS1.getRingModifications();
		LinkedList<Modification> t_aRingMod2 = MS2.getRingModifications();
		if ( t_aRingMod1.size() > t_aRingMod2.size() ) return -1;
		if ( t_aRingMod1.size() < t_aRingMod2.size() ) return 1;

		// For ring size

		// For edge and modification around backbone
		LinkedList<WURCSEdge> t_aGlycoLinkages1 = MS1.getChildGlycosidicEdges();
		LinkedList<WURCSEdge> t_aGlycoLinkages2 = MS2.getChildGlycosidicEdges();
		LinkedList<WURCSEdge> t_aSubstLinkages1 = MS1.getSubstituentEdges();
		LinkedList<WURCSEdge> t_aSubstLinkages2 = MS2.getSubstituentEdges();
		int t_nChildCount1 = t_aGlycoLinkages1.size();
		int t_nChildCount2 = t_aGlycoLinkages2.size();
		int t_nSubstCount1 = t_aSubstLinkages1.size();
		int t_nSubstCount2 = t_aSubstLinkages2.size();
		// Prioritize smaller number of child backbone
		if ( t_nChildCount1 != t_nChildCount2 )
			return t_nChildCount1 - t_nChildCount2;
		// Prioritize larger number of connected modification
		if ( t_nSubstCount1 != t_nSubstCount2 )
			return t_nSubstCount2 - t_nSubstCount1;

		// Prioritize smaller number of child


		return 0;
	}

}
