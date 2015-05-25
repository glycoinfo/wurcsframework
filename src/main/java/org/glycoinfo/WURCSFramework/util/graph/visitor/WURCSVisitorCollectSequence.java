package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSVisitorCollectSequenceComparator;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserNoBranch;
import org.glycoinfo.WURCSFramework.wurcs.WURCSException;
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

	private LinkedList<WURCSEdge>      m_aEdges = new LinkedList<WURCSEdge>();
	private LinkedList<WURCSComponent> m_aNodes = new LinkedList<WURCSComponent>();

	private LinkedList<Modification> m_aRepeats = new LinkedList<Modification>();
	private LinkedList<Modification> m_aCyclics = new LinkedList<Modification>();

	private int m_nBranchBackbone = 0;
	private int m_nBranchModification = 0;

	private LinkedList<WURCSComponent> m_aParentNodes   = new LinkedList<WURCSComponent>();
	private LinkedList<WURCSEdge>      m_aBranchEdges = new LinkedList<WURCSEdge>();

	/**
	 * Get collected edges
	 * @return List of WURCSEdge
	 */
	public LinkedList<WURCSEdge> getEdges() {
		return this.m_aEdges;
	}

	/**
	 * Get collected nodes
	 * @return List of WURCSComponent
	 */
	public LinkedList<WURCSComponent> getNodes() {
		return this.m_aNodes;
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
	 * Get repeat linkages
	 * @return
	 */
	public LinkedList<Modification> getRepeatModifications() {
		return this.m_aRepeats;
	}

	/**
	 * Get cyclic modifications
	 * @return
	 */
	public LinkedList<Modification> getCyclicModifications() {
		return this.m_aCyclics;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		if ( this.m_aNodes.contains(a_objBackbone) )
			throw new WURCSVisitorException("The backbone is already added.");

		this.m_aNodes.addLast(a_objBackbone);

		// Check glycosidic branch edges
		LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : a_objBackbone.getChildEdges() ) {
			Modification t_oMod = t_oEdge.getModification();
			if ( !t_oMod.isGlycosidic() ) continue;

			// For repeating unit
			if ( t_oMod instanceof InterfaceRepeat ) {
				this.m_aRepeats.addLast(t_oMod);
				continue;
			}

			// For cyclic part
			if ( t_oMod.isLeaf() ) {
				this.m_aCyclics.addLast(t_oMod);
				continue;
			}

			t_aEdges.add(t_oEdge);
		}

		// Return if there is no branch
		if ( t_aEdges.size() < 2 ) return;

		// Count branch on backbone
		this.m_nBranchBackbone++;

		// Traverse sequence of the branches, and add self sequence after sorting branch sequences
		this.traverseBranches(t_aEdges);
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// Ignore modificaitons of monosaccharide
		if ( !a_objModification.isGlycosidic() ) return;

		if ( this.m_aNodes.contains(a_objModification) )
			throw new WURCSVisitorException("The modification is already added.");

		this.m_aNodes.addLast(a_objModification);

		// Check branch edges
		LinkedList<WURCSEdge> t_aEdges = a_objModification.getChildEdges();

		// Return if there is no branch
		if ( t_aEdges.size() < 2 ) return;

		// Count branch on modification
		this.m_nBranchModification++;

		// Traverse sequence of the branches, and add self sequence after sorting branch sequences
		this.traverseBranches(t_aEdges);
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		if ( this.m_aEdges.contains(a_objWURCSEdge) )
			throw new WURCSVisitorException("The edge is already added.");

		this.m_aEdges.addLast(a_objWURCSEdge);
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
		this.m_aEdges = new LinkedList<WURCSEdge>();
		this.m_aNodes = new LinkedList<WURCSComponent>();

		this.m_aParentNodes   = new LinkedList<WURCSComponent>();
		this.m_aBranchEdges = new LinkedList<WURCSEdge>();
	}

	/**
	 * Start by edge
	 * @param a_oEdge
	 * @throws WURCSVisitorException
	 */
	public void start(WURCSEdge a_oEdge) throws WURCSVisitorException {
		// Traverse graph from edge
		WURCSGraphTraverser t_oTraverser = this.getTraverser(this);
		t_oTraverser.traverse(a_oEdge);
	}

	/**
	 * Start by root backbone
	 * @param a_oBackbone
	 * @throws WURCSVisitorException
	 */
	public void start(Backbone a_oBackbone) throws WURCSVisitorException {
		// Store null edge
		this.m_aEdges.addLast(null);

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

		// Search the branch sequence from the start edges
		LinkedList<WURCSVisitorCollectSequence> t_aSequences = new LinkedList<WURCSVisitorCollectSequence>();
		for ( WURCSEdge t_oEdge : a_aBranchStartEdges ) {
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.start(t_oEdge);
			t_aSequences.add(t_oSeq);
		}

		this.addSequences(t_aSequences);
	}

	/**
	 * Add sequences to self sequence
	 * @param a_aSequences
	 */
	private void addSequences(LinkedList<WURCSVisitorCollectSequence> a_aSequences) {
		// Sort branch sequences
		Collections.sort(a_aSequences, new WURCSVisitorCollectSequenceComparator());

		for ( WURCSVisitorCollectSequence t_oSeq : a_aSequences ) {
			// Add nodes and edges of the branches sequence
			this.m_aEdges.addAll(t_oSeq.getEdges());
			this.m_aNodes.addAll(t_oSeq.getNodes());

			this.m_aCyclics.addAll(t_oSeq.getCyclicModifications());
			this.m_aRepeats.addAll(t_oSeq.getRepeatModifications());

			// Add number of branch of the branches sequence
			this.m_nBranchBackbone     += t_oSeq.getBranchCountOnBackbone();
			this.m_nBranchModification += t_oSeq.getBranchCountOnModification();
		}
	}
}
