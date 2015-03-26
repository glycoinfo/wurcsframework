package org.glycoinfo.WURCSFramework.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.comparator.graph.BackboneComparator;

/**
 * Class for normalize WURCSGraph before traverse
 * @author MasaakiMatsubara
 *
 */
public class WURCSGraphNormalizer {

	/** For check backbone invert */
	private boolean m_bInverted = false;
	/** For check linkage between anomeric position */
	private boolean m_bAnomBond = false;
	/** For check contain cyclic part*/
	private boolean m_bCyclic = false;

	public boolean isInverted() {
		return this.m_bInverted;
	}

	public boolean linkedAnomericPositions() {
		return this.m_bAnomBond;
	}

	public boolean hasCyclic() {
		return this.m_bCyclic;
	}

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
			// XXX remove print
//			System.err.println(iComp);
			if ( iComp > 0 ) {
				t_objBackbone.invert();
				this.m_bInverted = true;
			}
			if ( iComp != 0 ) continue;

			// Symmetry check
			// XXX remove print
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
				// XXX remove print
				System.err.println(t_oBComp.compare(origBackbone, copyBackbone));
				System.err.println("Invert Backbone");
				origBackbone.invert();
				this.m_bInverted = true;
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

		// Reverse edges for open chain backbones
		LinkedList<Backbone> t_aOpenChainBackbones = new LinkedList<Backbone>();
		t_iterBackbone = a_oGraph.getBackboneIterator();
		while (t_iterBackbone.hasNext()) {
			t_objBackbone = t_iterBackbone.next();
			if ( t_objBackbone.getAnomericPosition() != 0 ) continue;
			t_aOpenChainBackbones.add(t_objBackbone);
		}
		this.reverseEdgeForOpenChain(t_aOpenChainBackbones);


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
				if ( t_oEdge.getBackbone() != t_aRootCandidate.getFirst() ) continue;
				t_oEdge.forward();
			}
			this.m_bAnomBond = true;
		}

		// For repeat and alternative
		t_iterModification = a_oGraph.getModificationIterator();
		while (t_iterModification.hasNext()) {
			t_objModification = t_iterModification.next();
			if ( !(t_objModification instanceof InterfaceRepeat) && !(t_objModification instanceof ModificationAlternative) ) continue;

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

			// XXX remove print
			for ( Backbone b : t_aCyclicBackbones ) {
				System.err.print("-"+a_oGraph.getBackbones().indexOf(b));
			}
			System.err.println(":");

			// Sort Backbones in cyclic
			Collections.sort( t_aCyclicBackbones, t_oBComp );
			t_aCyclicBackbones.getFirst().getAnomericEdge().forward();
			t_hashSearchedBackbones.addAll(t_aCyclicBackbones);
			this.m_bCyclic = true;
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

	/**
	 * Reverse edges for open chain backbone recursive
	 * @param a_aOpenChainBackbones List of open chain backbones
	 */
	private void reverseEdgeForOpenChain(LinkedList<Backbone> a_aOpenChainBackbones) {
		BackboneComparator t_oBComp = new BackboneComparator();
		LinkedList<Backbone> t_aReversedBackbones = new LinkedList<Backbone>();
		for ( Backbone t_objBackbone : a_aOpenChainBackbones ) {
			// Collect candidate parent backbone for open chain backbone
			LinkedList<Backbone> t_aChildBackbone = new LinkedList<Backbone>();
			LinkedList<Backbone> t_aParentBackboneCandidate = new LinkedList<Backbone>();
//			HashMap<Modification, Backbone> t_mapMtoB = new HashMap<Modification, Backbone>();
			for ( WURCSEdge t_oEdgeB2M : t_objBackbone.getEdges() ) {
				if ( !t_oEdgeB2M.getModification().isGlycosidic() ) continue;
				Modification t_oModif = t_oEdgeB2M.getModification();
				if ( t_oModif instanceof InterfaceRepeat ) continue;
				for ( WURCSEdge t_oEdgeM2B  : t_oModif.getEdges() ) {
					if ( t_oEdgeM2B == t_oEdgeB2M ) continue;
					if ( t_oEdgeM2B.isReverse() ) {
						t_aChildBackbone.add(t_oEdgeM2B.getBackbone());
						continue;
					}
					boolean t_bParentHasParent = false;
					for ( WURCSEdge t_oEdge : t_oEdgeM2B.getBackbone().getEdges() ) {
						if ( !t_oEdge.isReverse() ) continue;
						t_bParentHasParent = true;
					}
					if ( !t_bParentHasParent ) continue;

					t_aParentBackboneCandidate.add( t_oEdgeM2B.getBackbone() );
				}
			}
			if ( t_aParentBackboneCandidate.isEmpty() ) continue;

			// Reverse edge which linked parent backbone with the highest priority of the candidates
			Collections.sort(t_aParentBackboneCandidate, t_oBComp);
			if ( t_aChildBackbone.contains( t_aParentBackboneCandidate.getFirst() ) ) continue;
			t_aReversedBackbones.add(t_objBackbone);

			for ( WURCSEdge t_oEdgeM2B : t_aParentBackboneCandidate.getFirst().getEdges() ) {
				if ( !t_oEdgeM2B.getModification().isGlycosidic() ) continue;
				Modification t_oModif = t_oEdgeM2B.getModification();
				for ( WURCSEdge t_oEdgeB2M  : t_oModif.getEdges() ) {
					if ( t_oEdgeM2B == t_oEdgeB2M ) continue;
					if ( t_oEdgeB2M.getBackbone() != t_objBackbone ) continue;
					System.err.println("4");
					System.err.println( t_objBackbone.getSkeletonCode() );
					t_oEdgeB2M.reverse();
				}
			}
		}
		if ( t_aReversedBackbones.isEmpty() ) return;
		for ( Backbone t_oRemoveBackbone : t_aReversedBackbones )
			a_aOpenChainBackbones.remove(t_oRemoveBackbone);
		this.reverseEdgeForOpenChain(a_aOpenChainBackbones);
	}
}
