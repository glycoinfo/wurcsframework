package org.glycoinfo.WURCSFramework.util.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.graph.comparator.BackboneComparator;
import org.glycoinfo.WURCSFramework.util.graph.comparator.MonosaccharideComparatorForInvertBackbone;
import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSVisitorCollectSequenceComparator;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectSequence;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorExpandRepeatingUnit;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.Monosaccharide;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

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
	/** For check expanded repeating unit */
	private boolean m_bExpandedRep = false;

	public boolean isInverted() {
		return this.m_bInverted;
	}

	public boolean linkedAnomericPositions() {
		return this.m_bAnomBond;
	}

	public boolean hasCyclic() {
		return this.m_bCyclic;
	}

	public boolean isExpandedRepeatingUnit() {
		return this.m_bExpandedRep;
	}

	public void start(WURCSGraph a_oGraph) throws WURCSException {
		Backbone t_objBackbone;
		Modification t_objModification;
		Iterator<Backbone> t_iterBackbone = a_oGraph.getBackboneIterator();

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
		this.reverseEdgeForOpenChain(a_oGraph);

		// TODO: Check all open chain or all root glycan
		this.checkAllRoot(a_oGraph);


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

			// Get root backbone in root
			Backbone t_oRoot = this.getRootOfBackbones(t_aRootCandidate, a_oGraph);

//			this.forwardParentEdgeForRootBackbone(t_oRoot, t_aRootCandidate);

//			Collections.sort(t_aRootCandidate, t_oBComp);

			// Reorder direction of edge for root backbone
			for ( WURCSEdge t_oEdge : t_objModification.getEdges() ) {
				if ( t_oEdge.getBackbone() == t_oRoot ) continue;
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
			System.err.println("Cyclic part is found: "+t_aCyclicBackbones.size()+" membered");

			// Get root backbone in cyclic
			Backbone t_oRoot = this.getRootOfBackbones(t_aCyclicBackbones, a_oGraph);
			this.forwardParentSideEdgeForRootBackbone(t_oRoot, t_aCyclicBackbones);

			// Sort Backbones in cyclic
//			Collections.sort( t_aCyclicBackbones, t_oBComp );
//			t_aCyclicBackbones.getFirst().getAnomericEdge().forward();
//			t_hashSearchedBackbones.addAll(t_aCyclicBackbones);
			this.m_bCyclic = true;
		}

		// Invert check for Backbone
		this.invertBackbones(a_oGraph);

		// Expand exact repeating unit
		WURCSVisitorExpandRepeatingUnit t_oExpandRep = new WURCSVisitorExpandRepeatingUnit();
		t_oExpandRep.start(a_oGraph);
		this.m_bExpandedRep = t_oExpandRep.hasDone();

	}

	private void invertBackbones(WURCSGraph a_oGraph) throws WURCSException {
		// Invert check for Backbone
		LinkedList<Backbone> t_aSymmetricBackbone = new LinkedList<Backbone>();
		Iterator<Backbone> t_iterBackbone = a_oGraph.getBackboneIterator();
		while (t_iterBackbone.hasNext())
		{
			Backbone t_objBackbone = t_iterBackbone.next();
			Backbone copy   = t_objBackbone.copy();
			Backbone invert = t_objBackbone.copy();
			invert.invert();
			// Invert check with no edge
			int iComp = new BackboneComparator().compare(copy, invert);
			// XXX remove print
//			System.err.println(iComp);

			// Invert if inverted backbone is prior than original
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

		// Invert check for symmetric Backbone with connected linkages
		MonosaccharideComparatorForInvertBackbone t_oMSComp = new MonosaccharideComparatorForInvertBackbone();
		HashMap<Backbone, Backbone> t_hashOrigToInvert = new HashMap<Backbone, Backbone>();
		a_oGraph.copy(t_hashOrigToInvert);
		for ( Backbone t_oOrigBackbone : t_aSymmetricBackbone ) {
			Backbone t_oCopyBackbone = t_hashOrigToInvert.get(t_oOrigBackbone);
			t_oCopyBackbone.invert();

			// Compare monosaccharides
			int t_iComp = t_oMSComp.compare(new Monosaccharide(t_oOrigBackbone), new Monosaccharide(t_oCopyBackbone));

			if ( t_iComp > 0 ) {
				// XXX remove print
				System.err.println(t_iComp);
				System.err.println("Invert Backbone");
				t_oOrigBackbone.invert();
				this.m_bInverted = true;
			}
		}
	}

	/**
	 * Get root backbone from candidate root backbones with WURCSVisitorCollectSequence
	 * @param a_aCandidateRootBackbones
	 * @param a_oGraph
	 * @return
	 * @throws WURCSException
	 */
	private Backbone getRootOfBackbones(LinkedList<Backbone> a_aCandidateRootBackbones, WURCSGraph a_oGraph) throws WURCSException {
		LinkedList<WURCSVisitorCollectSequence> t_aSeqs = new LinkedList<WURCSVisitorCollectSequence>();
		HashMap<WURCSVisitorCollectSequence, Backbone> t_hashSeqToCB = new HashMap<WURCSVisitorCollectSequence, Backbone>();

		HashMap<Backbone, Integer> t_mapB2ID = new HashMap<Backbone, Integer>();
//		int i = 0;
		for ( Backbone t_oCB : a_aCandidateRootBackbones ) {
			// Copy graph
			HashMap<Backbone, Backbone> t_hashOrigToCopyB = new HashMap<Backbone, Backbone>();
//			WURCSGraph t_oCopyGraph = a_oGraph.copy(t_hashOrigToCopyB);
			a_oGraph.copy(t_hashOrigToCopyB);
			// Collect copied cyclic backbones
			LinkedList<Backbone> t_aCopiedCyclicBackbones = new LinkedList<Backbone>();
			for ( Backbone t_oCB2 : a_aCandidateRootBackbones ) {
				t_aCopiedCyclicBackbones.add( t_hashOrigToCopyB.get(t_oCB2) );
			}

			// Forward cyclic edge for copied cyclic backbone
			Backbone t_oCopyCB = t_hashOrigToCopyB.get(t_oCB);
			this.forwardParentSideEdgeForRootBackbone(t_oCopyCB, t_aCopiedCyclicBackbones);

			// Collect sequence
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.start(t_oCopyCB);

			// Check illegal repeat
			if ( t_oSeq.hasIllegalRepeat() ) continue;

			// TODO: test
//			i++;
//			WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
//			t_oG2A.start( t_oCopyGraph );
//			System.err.println( i + " : " + (new WURCSExporter()).getWURCSString( t_oG2A.getWURCSArray() ) );
//			t_mapB2ID.put(t_oCB, i);

			t_aSeqs.add(t_oSeq);
			t_hashSeqToCB.put(t_oSeq, t_oCB);
		}

		// Sort sequences
		Collections.sort(t_aSeqs, new WURCSVisitorCollectSequenceComparator());
		// TODO: test
//		String t_strSorted = "";
//		for ( WURCSVisitorCollectSequence t_oSeq : t_aSeqs ) {
//			if ( !t_strSorted.isEmpty() ) t_strSorted += ",";
//			t_strSorted += t_mapB2ID.get( t_hashSeqToCB.get(t_oSeq) );
//		}
//		System.err.println( "Sorted: "+t_strSorted );

		// Get first backbone
		return t_hashSeqToCB.get( t_aSeqs.getFirst() );
	}

	/**
	 * Forword parent side edges for root backbones
	 * @param a_oBackbone Target backbone
	 * @param a_aCandidateRootBackbones Backbones which is root candidate
	 */
	private void forwardParentSideEdgeForRootBackbone(Backbone a_oBackbone, LinkedList<Backbone> a_aCandidateRootBackbones) {
		for ( WURCSEdge t_oParentEdge : a_oBackbone.getParentEdges() ) {
			// Forward only cyclic parent
			boolean t_bIsParent = false;
			for ( WURCSEdge t_oParentParentEdge : t_oParentEdge.getModification().getParentEdges() ) {
				if ( !a_aCandidateRootBackbones.contains( t_oParentParentEdge.getBackbone() ) ) continue;
				t_bIsParent = true;
			}
			if ( !t_bIsParent ) continue;
			t_oParentEdge.forward();
		}
	}

	/**
	 * Check cyclic backbones
	 * @param a_aBackbones Backbones in cyclic
	 * @return true if the backbones is cyclic
	 */
	private boolean checkCyclic(LinkedList<Backbone> a_aBackbones) {
		Backbone t_oHead = a_aBackbones.getFirst();
		for ( WURCSEdge t_oParentEdge : t_oHead.getParentEdges() ) {
			Modification t_oParentMod = t_oParentEdge.getModification();
			if ( t_oParentMod instanceof InterfaceRepeat ) continue;

			for ( WURCSEdge t_oParentParentEdge : t_oParentMod.getParentEdges() ) {
				Backbone t_oParentBack = t_oParentParentEdge.getBackbone();
				// Check cyclic
				if ( a_aBackbones.contains(t_oParentBack) ) return true;

				// Recursive search
				a_aBackbones.addFirst(t_oParentBack);
				if ( this.checkCyclic(a_aBackbones) ) return true;
				a_aBackbones.removeFirst();
			}
		}
		return false;
	}

	/**
	 * Reverse edges for open chain backbone recursive
	 * @param a_oGraph
	 */
	private void reverseEdgeForOpenChain(WURCSGraph a_oGraph) {
		// Collect leave of modifications
		LinkedList<Modification> t_aLeafModifications = new LinkedList<Modification>();
		for ( Modification t_oMod : a_oGraph.getModifications() ) {
			if ( t_oMod instanceof InterfaceRepeat ) continue;
			if ( !t_oMod.isLeaf() ) continue;
			if ( !t_oMod.isGlycosidic() ) continue;
			t_aLeafModifications.add(t_oMod);
		}

		// Check relation neighbor parent backbones
		while (true) {
			LinkedList<Modification> t_aNotLeaves = new LinkedList<Modification>();
			for ( Modification t_oLeaf : t_aLeafModifications ) {
				// Collect parent side edges
				LinkedList<WURCSEdge> t_aParentSideEdges = new LinkedList<WURCSEdge>();

				for ( WURCSEdge t_oEdge : t_oLeaf.getEdges() ) {
					Backbone t_oBackbone = t_oEdge.getBackbone();
					if ( t_oBackbone.isRoot() && t_oBackbone.getAnomericPosition() < 1 ) continue;
					t_aParentSideEdges.add(t_oEdge);
				}
				if ( t_aParentSideEdges.isEmpty() ) continue;

				// Reverse edge for childe side edges
				for ( WURCSEdge t_oEdge : t_oLeaf.getEdges() ) {
					if ( t_aParentSideEdges.contains(t_oEdge) ) continue;
					t_oEdge.reverse();
				}
				t_aNotLeaves.add(t_oLeaf);
			}

			// Break if there is no remove leaf
			if ( t_aNotLeaves.isEmpty() ) break;
			for ( Modification t_oNotLeaf : t_aNotLeaves )
				t_aLeafModifications.remove(t_oNotLeaf);
		}

		if ( t_aLeafModifications.isEmpty() ) return;

		// Check relation neighbor child backbones
		while (true) {
			// Collect backbones having child backbone
			LinkedList<Backbone> t_aBackboneWithChild = new LinkedList<Backbone>();
			for ( Backbone t_oBackbone : a_oGraph.getBackbones() ) {
				boolean t_bHasChildBackbone = false;
				for ( WURCSEdge t_oChildEdge : t_oBackbone.getChildEdges() ) {
					if ( t_oChildEdge.getModification().getChildEdges().isEmpty() ) continue;
					t_bHasChildBackbone = true;
				}
				if ( !t_bHasChildBackbone ) continue;
				t_aBackboneWithChild.add(t_oBackbone);
			}

			LinkedList<Modification> t_aNotLeaves = new LinkedList<Modification>();
			for ( Modification t_oLeaf : t_aLeafModifications ) {
				// Collect child side edges
				LinkedList<WURCSEdge> t_aChildSideEdges = new LinkedList<WURCSEdge>();
				for ( WURCSEdge t_oEdge : t_oLeaf.getEdges() ) {
					// Backbone have child
					if ( !t_aBackboneWithChild.contains( t_oEdge.getBackbone() ) ) continue;
					t_aChildSideEdges.add(t_oEdge);
				}
				if ( t_aChildSideEdges.isEmpty() ) continue;
				// Not change if all of edges are child side
				if ( t_aChildSideEdges.size() == t_oLeaf.getEdges().size() ) continue;

				// Reverse edge for childe side edges
				for ( WURCSEdge t_oEdge : t_aChildSideEdges )
					t_oEdge.reverse();

				t_aNotLeaves.add(t_oLeaf);
			}

			// Break if there is no remove leaf
			if ( t_aNotLeaves.isEmpty() ) break;
			for ( Modification t_oNotLeaf : t_aNotLeaves )
				t_aLeafModifications.remove(t_oNotLeaf);
		}
	}

	private void checkAllRoot(WURCSGraph a_oGraph) throws WURCSException {
		if ( a_oGraph.getBackbones().size() == 1 ) return;
		// Check all root backbone
		boolean t_bIsAllRoot = true;
		for ( Backbone t_oBackbone : a_oGraph.getBackbones() ) {
			if ( t_oBackbone.isRoot() ) continue;
			t_bIsAllRoot = false;
		}
		if ( !t_bIsAllRoot ) return;

		// For all root backbone
		LinkedList<WURCSVisitorCollectSequence> t_aSeqs = new LinkedList<WURCSVisitorCollectSequence>();
		HashMap<WURCSVisitorCollectSequence, Backbone> t_hashSeqToCB = new HashMap<WURCSVisitorCollectSequence, Backbone>();
		for ( Backbone t_oCB : a_oGraph.getBackbones() ) {
			// Copy graph
			HashMap<Backbone, Backbone> t_hashOrigToCopyB = new HashMap<Backbone, Backbone>();
			WURCSGraph t_oCopyGraph = a_oGraph.copy(t_hashOrigToCopyB);

			// Reverse edges around root candidate
			Backbone t_oCopyCB = t_hashOrigToCopyB.get(t_oCB);
			this.reverseEdgesAroundBackbone(t_oCopyCB);

			// Reverse edges around root candidate
			this.reverseEdgeForOpenChain(t_oCopyGraph);

			// Collect sequence
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.start(t_oCopyCB);

			// Check illegal repeat
			if ( t_oSeq.hasIllegalRepeat() ) continue;

			t_aSeqs.add(t_oSeq);
			t_hashSeqToCB.put(t_oSeq, t_oCB);
		}

		if ( t_aSeqs.isEmpty() )
			throw new WURCSException("There is no result sequence.");

		// Sort sequences
		Collections.sort(t_aSeqs, new WURCSVisitorCollectSequenceComparator());

		// Set second backbone to root
		Backbone t_oRoot = t_hashSeqToCB.get( t_aSeqs.getFirst() );
		this.reverseEdgesAroundBackbone(t_oRoot);
		this.reverseEdgeForOpenChain(a_oGraph);

	}

	private void reverseEdgesAroundBackbone(Backbone a_oBackbone) {
		for ( WURCSEdge t_oEdge : a_oBackbone.getChildEdges() ) {
			Modification t_oMod = t_oEdge.getModification();
			if ( t_oMod instanceof InterfaceRepeat ) continue;
			if ( !t_oMod.isLeaf() ) continue;
			if ( !t_oMod.isGlycosidic() ) continue;

			// Reverse edge of child backbones
			for ( WURCSEdge t_oEdge2 : t_oMod.getEdges() ) {
				if ( t_oEdge2.equals(t_oEdge) ) continue;
				t_oEdge2.reverse();
			}
		}
	}
}
