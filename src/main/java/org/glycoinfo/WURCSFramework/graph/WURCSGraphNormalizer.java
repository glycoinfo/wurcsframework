package org.glycoinfo.WURCSFramework.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.comparator.graph.BackboneComparator;

/**
 *
 * @author MasaakiMatsubara
 *
 */
public class WURCSGraphNormalizer {

	public void start(WURCSGraph a_oGraph) throws WURCSException {
		Backbone t_objBackbone;
		Modification t_objModification;
		BackboneComparator t_oBComp = new BackboneComparator();
		Iterator<Backbone> t_iterBackbone = a_oGraph.getBackboneIterator();

		// Invert Backbone
		LinkedList<Backbone> t_aSymmetricBackbone = new LinkedList<Backbone>();
		while (t_iterBackbone.hasNext())
		{
			t_objBackbone = t_iterBackbone.next();
			Backbone copy   = t_objBackbone.copy();
			Backbone invert = t_objBackbone.copy();
			invert.invert();
			int iComp = t_oBComp.compare(copy, invert);
			if ( iComp > 0 ) t_objBackbone.invert();
			if ( iComp != 0 ) continue;

			// Symmetry check
			System.err.println( "Symmetry backbone: " + t_objBackbone.getSkeletonCode() );
			t_aSymmetricBackbone.addLast(t_objBackbone);
		}
		// Invert symmetric Backbone
		HashMap<Backbone, Backbone> t_hashOrigToInvert = new HashMap<Backbone, Backbone>();
		a_oGraph.copy(t_hashOrigToInvert);
		for ( Backbone origBackbone : t_aSymmetricBackbone ) {
			Backbone copyBackbone = t_hashOrigToInvert.get(origBackbone);
			copyBackbone.invert();
			if ( t_oBComp.compare(origBackbone, copyBackbone) > 0 ) {
				System.err.println("Invert Backbone");
				origBackbone.invert();
			}
		}

		// Reverse edges for non-root Backbones
//		LinkedList<Backbone> t_aRoots = new LinkedList<Backbone>();
		t_iterBackbone = a_oGraph.getBackboneIterator();
		while (t_iterBackbone.hasNext()) {
			t_objBackbone = t_iterBackbone.next();

			if ( ! t_objBackbone.hasParent() ) continue;

//			System.err.println("Reverse: "+a_oGraph.getBackbones().indexOf(t_objBackbone));
			t_objBackbone.getAnomericEdge().reverse();
		}


		// For glycosidic linkage between anomeric positions
		Iterator<Modification> t_iterModification = a_oGraph.getModificationIterator();
		while (t_iterModification.hasNext()) {
			t_objModification = t_iterModification.next();
			if ( !t_objModification.isGlycosidic() ) continue;
			if ( !t_objModification.isAglycone() ) continue;

			// Sort candidate root backbone
			LinkedList<Backbone> t_aRootCandidate = new LinkedList<Backbone>();
			for ( WURCSEdge t_oEdge : t_objModification.getEdges() )
				t_aRootCandidate.add( t_oEdge.getBackbone() );
			Collections.sort(t_aRootCandidate, t_oBComp);

			// Reorder direction of edge for root backbone
			for ( WURCSEdge t_oEdge : t_objModification.getEdges() ) {
				if (! t_oEdge.getBackbone().equals(t_aRootCandidate.getFirst()) ) continue;
				t_oEdge.forward();
			}
		}

		// For repeat and alternative
		t_iterModification = a_oGraph.getModificationIterator();
		while (t_iterModification.hasNext()) {
			t_objModification = t_iterModification.next();
			if ( !(t_objModification instanceof InterfaceRepeat) && !(t_objModification instanceof ModificationAlternative) )
				continue;

			for ( WURCSEdge t_oEdge : t_objModification.getEdges() )
				t_oEdge.forward();
		}

		// For cyclic
		t_iterBackbone = a_oGraph.getBackboneIterator();
		HashSet<Backbone> t_hashSearchedBackbones = new HashSet<Backbone>();
		while (t_iterBackbone.hasNext()) {
			t_objBackbone = t_iterBackbone.next();
			if ( t_hashSearchedBackbones.contains(t_objBackbone) ) continue;
			t_hashSearchedBackbones.add(t_objBackbone);

			LinkedList<Backbone> t_aCyclicBackbones = new LinkedList<Backbone>();
			t_aCyclicBackbones.addFirst( t_objBackbone );

			// Check cyclic recursive
			if ( !this.checkCyclic(t_aCyclicBackbones) ) continue;

			for ( Backbone b : t_aCyclicBackbones ) {
				System.err.print("-"+a_oGraph.getBackbones().indexOf(b));
			}
			System.err.println(":");

			// Sort Backbones in cyclic
			Collections.sort( t_aCyclicBackbones, t_oBComp );
			t_aCyclicBackbones.getFirst().getAnomericEdge().forward();
			t_hashSearchedBackbones.addAll(t_aCyclicBackbones);
		}

	}

	private boolean checkCyclic(LinkedList<Backbone> a_aBackbones) {
		Backbone t_oHead = a_aBackbones.getFirst();
		WURCSEdge t_oAnomEdge = t_oHead.getAnomericEdge();
		// Head backbone is root
		if ( t_oAnomEdge == null || !t_oAnomEdge.isReverse() ) return false;

		// Search parent modification edges
		for ( WURCSEdge t_oModifEdge : t_oAnomEdge.getModification().getEdges() ) {
			if ( t_oModifEdge.isReverse() ) continue;

			Backbone t_oParentBack = t_oModifEdge.getBackbone();
			if ( t_oHead.equals(t_oParentBack) ) continue;

			// Check cyclic
			if ( a_aBackbones.contains(t_oParentBack) ) return true;

			// Recursive search
			a_aBackbones.addFirst(t_oModifEdge.getBackbone());
			if ( this.checkCyclic(a_aBackbones) ) return true;
			a_aBackbones.removeFirst();
		}
		return false;
	}

}
