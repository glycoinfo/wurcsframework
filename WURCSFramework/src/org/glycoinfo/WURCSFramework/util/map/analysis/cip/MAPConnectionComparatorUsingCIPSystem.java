package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.Collections;
import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;


/**
 * Comparator class for coonections by CIP order
 * @author matsubara
 *
 */
public class MAPConnectionComparatorUsingCIPSystem implements Comparator<MAPConnection> {

	private MAPGraph m_oMAPGraph;
	private AtomicNumber m_oANum;

	public MAPConnectionComparatorUsingCIPSystem(MAPGraph a_oGraph, AtomicNumber a_oANum) {
		this.m_oMAPGraph = a_oGraph;
		this.m_oANum = a_oANum;
	}

	public MAPConnectionComparatorUsingCIPSystem(MAPGraph a_oGraph) {
		this( a_oGraph, new AtomicNumber() );
	}

	public int compare(MAPConnection a_oConnection1, MAPConnection a_oConnection2) {

		// Compare by CIP order using HierarchicalDigraphs with iterative deepening depth-first search (IDDFS)
		int t_iDepth = 1;
		HierarchicalDigraphNode t_oHD1 = null;
		HierarchicalDigraphNode t_oHD2 = null;
		while ( true ) {
			// Create hierarchical digraph starting from connection
			HierarchicalDigraphCreator t_oHDCreate1 = this.getHDCreator(a_oConnection1, t_iDepth);
			HierarchicalDigraphCreator t_oHDCreate2 = this.getHDCreator(a_oConnection2, t_iDepth);

			t_oHD1 = t_oHDCreate1.getHierarchicalDigraph();
			t_oHD2 = t_oHDCreate2.getHierarchicalDigraph();

			// Compare CIP orders using HierarchicalDigraph
			HierarchicalDigraphComparator t_oHDComp = new HierarchicalDigraphComparator();
			// Sort children
			this.sortChildren(t_oHD1, t_oHDComp);
			this.sortChildren(t_oHD2, t_oHDComp);
			int t_iComp = t_oHDComp.compare(t_oHD1, t_oHD2);
			if ( t_iComp != 0 ) return t_iComp;

			// Stop comparation if same connections were compared between both of digraphs
			if ( t_oHDComp.foundSameBranch() ) break;

			// Return if full search has completed
			if ( t_oHDCreate1.isCompletedFullSearch() && t_oHDCreate2.isCompletedFullSearch() ) break;

			t_iDepth++;
		}

		// Compare stereochemistries of created HierarchicalDigraphs
		HierarchicalDigraphComparator t_oHDComp = new HierarchicalDigraphComparatorForStereo();
		// Sort children
		this.sortChildren(t_oHD1, t_oHDComp);
		this.sortChildren(t_oHD2, t_oHDComp);
		int t_iComp = t_oHDComp.compare(t_oHD1, t_oHD2);
		if ( t_iComp != 0 ) return t_iComp;

		return 0;
	}

	/**
	 * Create HierarchicalDigraph starting with a_oConnection with depth of t_iDepth
	 * @param a_oConnection Start connection of HierarchicalDigraph
	 * @param t_iDepth Depth of HierarchicalDigraph
	 * @return Creator class of HierarchicalDigraph
	 */
	private HierarchicalDigraphCreator getHDCreator(MAPConnection a_oConnection, int t_iDepth) {
		HierarchicalDigraphCreator t_oHDCreator = new HierarchicalDigraphCreator(this.m_oANum, this.m_oMAPGraph);
		t_oHDCreator.start(a_oConnection, t_iDepth);
		return t_oHDCreator;
	}

	/**
	 * Sort child nodes of HierarchicalDigraph iterative using HierarchicalDigraphComparator
	 * @param a_oHD Root node of HierarchicalDigraph
	 * @param a_oHDComp Comparator for HierarhichalDigraph
	 */
	private void sortChildren( HierarchicalDigraphNode a_oHD, HierarchicalDigraphComparator a_oHDComp ) {
		if ( a_oHD.getChildren().isEmpty() ) return;
		for ( HierarchicalDigraphNode t_oChild : a_oHD.getChildren() ) {
			this.sortChildren(t_oChild, a_oHDComp);
		}
		Collections.sort( a_oHD.getChildren(), a_oHDComp );
	}

}