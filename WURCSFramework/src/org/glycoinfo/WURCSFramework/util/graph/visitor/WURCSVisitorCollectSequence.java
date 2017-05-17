package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSVisitorCollectSequenceComparator;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserNoBranch;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

/**
 * Class for collecting sequence of glycan residues
 * @author MasaakiMatsubara
 *
 */
public class WURCSVisitorCollectSequence implements WURCSVisitor {

//	private LinkedList<WURCSEdge>      m_aEdges = new LinkedList<WURCSEdge>();
	private LinkedList<LinkedList<WURCSEdge>> m_aMultiEdges = new LinkedList<LinkedList<WURCSEdge>>();
	private LinkedList<WURCSComponent> m_aNodes = new LinkedList<WURCSComponent>();

	private LinkedList<Modification> m_aRepeats = new LinkedList<Modification>();
	private LinkedList<Modification> m_aLeaves  = new LinkedList<Modification>();

	private LinkedList<Integer> m_aBranchingPoints = new LinkedList<Integer>();
	private int m_nBranchBackbone = 0;
	private int m_nBranchModification = 0;

	private int m_iDepth = 0;
	private int m_nTerminal = 0;

	private LinkedList<WURCSComponent> m_aParentNodes = new LinkedList<WURCSComponent>();
	private LinkedList<WURCSEdge>      m_aBranchEdges = new LinkedList<WURCSEdge>();

	protected void setParentNodes(LinkedList<WURCSComponent> a_aParentNodes) {
		this.m_aParentNodes.addAll(a_aParentNodes);
	}

	/**
	 * Get collected edges
	 * @return List of WURCSEdge
	 */
//	public LinkedList<WURCSEdge> getEdges() {
//		return this.m_aEdges;
//	}

	/**
	 * Get collected multiple edges
	 * @return List of WURCSEdge
	 */
	public LinkedList<LinkedList<WURCSEdge>> getMultiEdges() {
		return this.m_aMultiEdges;
	}

	/**
	 * Get collected nodes
	 * @return List of WURCSComponent
	 */
	public LinkedList<WURCSComponent> getNodes() {
		return this.m_aNodes;
	}

	/**
	 * Get repeat linkages
	 * @return
	 */
	public LinkedList<Modification> getRepeatModifications() {
		return this.m_aRepeats;
	}

	/**
	 * Get leaf modifications
	 * @return
	 */
	public LinkedList<Modification> getLeafModifications() {
		return this.m_aLeaves;
	}

	/**
	 * Get number of branching point on backbone
	 * @return Number of branching point on backbone
	 */
	public int getBranchCountOnBackbone() {
		return this.m_nBranchBackbone;
	}

	/**
	 * Get number of branching point on modification
	 * @return Number of branching point on modification
	 */
	public int getBranchCountOnModification() {
		return this.m_nBranchModification;
	}

	/**
	 * Get depth of the sequence
	 * @return Number of depth
	 */
	public int getDepth() {
		return this.m_iDepth;
	}

	/**
	 * Get terminal count
	 * @return Number of Terminal
	 */
	public int getTerminalCount() {
		return this.m_nTerminal;
	}

	public LinkedList<Integer> getBranchingPoints() {
		return this.m_aBranchingPoints;
	}

	public boolean hasIllegalRepeat() {
//		System.err.println("check repeat:"+this.m_aRepeats);
		// For repeat
		for ( Modification t_oRep : this.m_aRepeats ) {
			WURCSEdge t_oStartEdge = t_oRep.getEdges().getFirst();
			WURCSEdge t_oEndEdge   = t_oRep.getEdges().getLast();
			// Anomeric side is end
			if ( t_oStartEdge.isAnomeric() ) {
				t_oEndEdge   = t_oRep.getEdges().getFirst();
				t_oStartEdge = t_oRep.getEdges().getLast();
			}

			int t_iStartID = this.m_aNodes.indexOf( t_oStartEdge.getBackbone() );
			int t_iEndID   = this.m_aNodes.indexOf( t_oEndEdge.getBackbone() );
//			System.err.println( t_iStartID+" vs "+t_iEndID );
			// Repeat is reversed
			if ( t_iStartID - t_iEndID < 0 ) return true;
			// Repeat is in terminal
//			if ( t_iStartID == this.m_aNodes.size()-1 || t_iEndID == 0 ) return true;
		}
		return false;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		if ( this.m_aParentNodes.contains(a_objBackbone) )
			throw new WURCSVisitorException("The backbone is already added in parent sequence.");

		if ( this.m_aNodes.contains(a_objBackbone) )
			throw new WURCSVisitorException("The backbone is already added.");

		// Add node
		this.m_aNodes.addLast(a_objBackbone);

		// Count depth
		this.m_iDepth++;

		// XXX Remove print
//		System.err.println(this.m_iDepth + ":" + a_objBackbone.getSkeletonCode() + a_objBackbone);

		// Check glycosidic branch edges
		LinkedList<WURCSEdge> t_aChildGlycosidicEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : a_objBackbone.getChildEdges() ) {
			Modification t_oMod = t_oEdge.getModification();
			// For repeating unit
			if ( t_oMod instanceof InterfaceRepeat ) {
				this.m_aRepeats.addLast(t_oMod);
				continue;
			}

			if ( !t_oMod.isGlycosidic() ) continue;

			// For multiple linkage to modification
			if ( this.m_aNodes.contains(t_oMod) ) continue;
			if ( this.m_aParentNodes.contains(t_oMod) ) continue;

			// For cyclic part
			if ( t_oMod.isLeaf() ) {
				this.m_aLeaves.addLast(t_oMod);
				continue;
			}

			t_aChildGlycosidicEdges.add(t_oEdge);
		}

		// Count terminal
		if ( t_aChildGlycosidicEdges.size() == 0 )
			this.m_nTerminal++;

		// Return if there is no branch
		if ( t_aChildGlycosidicEdges.size() < 2 ) return;

		// Count branch on backbone
		this.m_nBranchBackbone++;

		// Add depth of branching point
		this.m_aBranchingPoints.addLast(this.m_iDepth);

		// Traverse sequence of the branches, and add self sequence after sorting branch sequences
		this.traverseBranches(t_aChildGlycosidicEdges);
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// Ignore modificaitons of monosaccharide
		if ( !a_objModification.isGlycosidic() ) return;

		if ( this.m_aNodes.contains(a_objModification) )
			throw new WURCSVisitorException("The modification is already added.");

		// Add node
		this.m_aNodes.addLast(a_objModification);

		// Count depth
		this.m_iDepth++;

		// XXX Remove print
//		System.err.println( this.m_iDepth + ":" + a_objModification.getMAPCode() );

		// Check branch edges
		LinkedList<WURCSEdge> t_aChildEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge :  a_objModification.getChildEdges() ) {
			// For multiple linkage to Backbone
			if ( this.m_aNodes.contains( t_oEdge.getBackbone() ) ) continue;
			if ( this.m_aParentNodes.contains( t_oEdge.getBackbone() ) ) continue;
			t_aChildEdges.add(t_oEdge);
		}

		// Return if there is no branch
		if ( t_aChildEdges.size() < 2 ) return;

		// Count branch on modification
		this.m_nBranchModification++;

		// Traverse sequence of the branches, and add self sequence after sorting branch sequences
		this.traverseBranches(t_aChildEdges);
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// TODO: to make testcase
		for ( LinkedList<WURCSEdge> t_aMultiEdge : this.m_aMultiEdges ) {
			if ( t_aMultiEdge.contains(a_objWURCSEdge) )
				throw new WURCSVisitorException("The edge is already added.");
		}
		LinkedList<WURCSEdge> t_aEdge = new LinkedList<WURCSEdge>();
		t_aEdge.addLast(a_objWURCSEdge);
		this.m_aMultiEdges.addLast(t_aEdge);
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.clear();

		try {
			// Traverse each root backbones
			this.traverseRootBackbones( a_objGraph.getRootBackbones() );
		}
		catch (WURCSException e) {
			throw new WURCSVisitorException(e.getMessage());
		}
	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserNoBranch(a_objVisitor);
	}

	@Override
	public void clear() {
//		this.m_aEdges = new LinkedList<WURCSEdge>();
		this.m_aMultiEdges = new LinkedList<LinkedList<WURCSEdge>>();
		this.m_aNodes = new LinkedList<WURCSComponent>();

		this.m_aRepeats = new LinkedList<Modification>();
		this.m_aLeaves = new LinkedList<Modification>();

		this.m_nBranchBackbone = 0;
		this.m_nBranchModification = 0;

		this.m_aBranchingPoints = new LinkedList<Integer>();

		this.m_iDepth = 0;
		this.m_nTerminal = 0;

//		this.m_aParentNodes   = new LinkedList<WURCSComponent>();
		this.m_aBranchEdges = new LinkedList<WURCSEdge>();
	}

	/**
	 * Start by edge ( use multiple edge )
	 * @param a_oEdge
	 * @throws WURCSVisitorException
	 */
	public void start(WURCSEdge a_oEdge) throws WURCSVisitorException {
//		this.m_aEdges.addAll( a_oEdge );
		LinkedList<WURCSEdge> t_oMulti = new LinkedList<WURCSEdge>();
		t_oMulti.add(a_oEdge);
		this.start(t_oMulti);
	}

	/**
	 * Start by multiple edges
	 * @param a_aEdges
	 * @throws WURCSVisitorException
	 */
	public void start(LinkedList<WURCSEdge> a_aEdges) throws WURCSVisitorException {
		this.m_aMultiEdges.addLast(a_aEdges);
		// Traverse graph from one of multiple edge
		WURCSGraphTraverser t_oTraverser = this.getTraverser(this);
		t_oTraverser.traverse( a_aEdges.getFirst().getNextComponent() );
	}

	/**
	 * Start by root backbone
	 * @param a_oBackbone
	 * @throws WURCSVisitorException
	 */
	public void start(Backbone a_oBackbone) throws WURCSVisitorException {
//		// Store null edge
//		this.m_aEdges.addLast(null);
		// Store empty edge group
		this.m_aMultiEdges.addLast(new LinkedList<WURCSEdge>());

		// Traverse graph from backbone
		WURCSGraphTraverser t_oTraverser = this.getTraverser(this);
		t_oTraverser.traverse(a_oBackbone);
	}

	/**
	 * Traverse sequences from root backbones
	 * @param a_aRootBackbones
	 * @throws WURCSVisitorException
	 */
	private void traverseRootBackbones(ArrayList<Backbone> a_aRootBackbones) throws WURCSVisitorException {
		// Search the sequence from the start root backbones
		LinkedList<WURCSVisitorCollectSequence> t_aSequences = new LinkedList<WURCSVisitorCollectSequence>();
		for ( Backbone t_oRoot : a_aRootBackbones ) {
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.start(t_oRoot);
			t_aSequences.add(t_oSeq);
		}

		this.addSequences(t_aSequences);
	}

	/**
	 * Traverse branch sequences for addition to self sequence
	 * @param a_aBranchStartEdges Start edge of branches
	 * @throws WURCSVisitorException
	 */
	private void traverseBranches(LinkedList<WURCSEdge> a_aBranchStartEdges) throws WURCSVisitorException {
		// Add Start edge of branches
		this.m_aBranchEdges.addAll(a_aBranchStartEdges);

		// Combine multiple edge
		LinkedList<LinkedList<WURCSEdge>> t_aAllMultipleEdges = new LinkedList<LinkedList<WURCSEdge>>();
		for ( WURCSEdge t_oEdge : a_aBranchStartEdges ) {
			// Search edge group that link between same Backbone and Modification
			boolean t_bIsMultiple = false;
			for ( LinkedList<WURCSEdge> t_aMultipleEdges : t_aAllMultipleEdges ) {
				for ( WURCSEdge t_oMultipleEdge : t_aMultipleEdges ) {
					if ( t_oEdge.equals(t_oMultipleEdge) ) continue;
					if ( !t_oEdge.getBackbone().equals(t_oMultipleEdge.getBackbone()) ) continue;
					if ( !t_oEdge.getModification().equals(t_oMultipleEdge.getModification()) ) continue;
					t_bIsMultiple = true;
				}
				// Add edge to group if same edge group is exist
				if ( !t_bIsMultiple ) continue;
				t_aMultipleEdges.add( t_oEdge );
				break;
			}
			// Make new group if no same edge group
			if ( t_bIsMultiple ) continue;
			LinkedList<WURCSEdge> t_aNewEdges = new LinkedList<WURCSEdge>();
			t_aNewEdges.add(t_oEdge);
			t_aAllMultipleEdges.add( t_aNewEdges );
		}

		// Search the branch sequence from the start edges
		LinkedList<WURCSVisitorCollectSequence> t_aSequences = new LinkedList<WURCSVisitorCollectSequence>();
		for ( LinkedList<WURCSEdge> t_aEdges : t_aAllMultipleEdges ) {
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.setParentNodes(this.m_aParentNodes);
			t_oSeq.setParentNodes(m_aNodes);

			t_oSeq.start(t_aEdges);
			t_aSequences.add(t_oSeq);
		}
/*
		// Search the branch sequence from the start edges
		LinkedList<WURCSVisitorCollectSequence> t_aSequences = new LinkedList<WURCSVisitorCollectSequence>();
		for ( WURCSEdge t_oEdge : a_aBranchStartEdges ) {
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.setParentNodes(this.m_aParentNodes);
			t_oSeq.setParentNodes(m_aNodes);

			t_oSeq.start(t_oEdge);
			t_aSequences.add(t_oSeq);
		}
*/
		this.addSequences(t_aSequences);
	}

	/**
	 * Add sequences to self sequence
	 * @param a_aSequences Child sequences
	 */
	private void addSequences(LinkedList<WURCSVisitorCollectSequence> a_aSequences) {
		// Sort branch sequences
		Collections.sort(a_aSequences, new WURCSVisitorCollectSequenceComparator());

		int t_iMaxDepth = 0;
		for ( WURCSVisitorCollectSequence t_oSeq : a_aSequences ) {
			// Add nodes and edges of the branches sequence
			this.m_aMultiEdges.addAll( t_oSeq.getMultiEdges() );
//			this.m_aEdges.addAll(t_oSeq.getEdges());
			this.m_aNodes.addAll(t_oSeq.getNodes());

			this.m_aLeaves.addAll(t_oSeq.getLeafModifications());
			this.m_aRepeats.addAll(t_oSeq.getRepeatModifications());

			// For max depth
			if ( t_iMaxDepth < t_oSeq.getDepth() ) t_iMaxDepth = t_oSeq.getDepth();

			for ( int t_iBranchingDepth : t_oSeq.getBranchingPoints() ) {
				t_iBranchingDepth += this.m_iDepth;
				this.m_aBranchingPoints.addLast(t_iBranchingDepth);
			}

			// Add number of branch of the branches sequence
			this.m_nBranchBackbone     += t_oSeq.getBranchCountOnBackbone();
			this.m_nBranchModification += t_oSeq.getBranchCountOnModification();

			// Add terminal count
			this.m_nTerminal += t_oSeq.getTerminalCount();
		}

		// Add depth
		this.m_iDepth += t_iMaxDepth;
	}
}
