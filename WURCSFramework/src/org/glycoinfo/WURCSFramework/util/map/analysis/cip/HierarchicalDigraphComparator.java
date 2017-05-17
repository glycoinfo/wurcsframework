package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.Comparator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;

/**
 * Comparator for HierarchicalDigraph
 * @author MasaakiMatsubara
 *
 */
public class HierarchicalDigraphComparator implements Comparator<HierarchicalDigraphNode>{

	private static final double EPS = 0.000000001;
	private boolean m_bFoundSameBranch;

	public boolean foundSameBranch() {
		return this.m_bFoundSameBranch;
	}

	@Override
	public int compare(HierarchicalDigraphNode a_oNode1, HierarchicalDigraphNode a_oNode2) {
		LinkedList<HierarchicalDigraphNode> t_aGraph1Width = new LinkedList<HierarchicalDigraphNode>();
		LinkedList<HierarchicalDigraphNode> t_aGraph2Width = new LinkedList<HierarchicalDigraphNode>();

		this.m_bFoundSameBranch = false;

		// Compare using width search
		t_aGraph1Width.addLast(a_oNode1);
		t_aGraph2Width.addLast(a_oNode2);
		while ( t_aGraph1Width.size() != 0 && t_aGraph2Width.size() != 0 ) {
			HierarchicalDigraphNode t_oNode1 = t_aGraph1Width.removeFirst();
			HierarchicalDigraphNode t_oNode2 = t_aGraph2Width.removeFirst();

			MAPConnection t_oConn1 = t_oNode1.getConnection();
			MAPConnection t_oConn2 = t_oNode2.getConnection();

			// Prioritize exist atom
			if ( t_oConn1 != null && t_oConn2 == null ) return -1;
			if ( t_oConn1 == null && t_oConn2 != null ) return 1;

			// Return if comaparing same connections
			if ( t_oConn1 != null && t_oConn2 != null && t_oConn1.equals(t_oConn2) ) {
				this.m_bFoundSameBranch = true;
				return 0;
			}

			// Compare nodes
			int t_iComp = this.compareNodes(t_oNode1, t_oNode2);
			if ( t_iComp != 0 ) return t_iComp;

			// For children
			LinkedList<HierarchicalDigraphNode> t_aChildren1 = t_oNode1.getChildren();
			LinkedList<HierarchicalDigraphNode> t_aChildren2 = t_oNode2.getChildren();

			// Add children to search list
			for ( HierarchicalDigraphNode t_oChild1 : t_aChildren1 ) {
				t_aGraph1Width.addLast(t_oChild1);
			}
			for ( HierarchicalDigraphNode t_oChild2 : t_aChildren2 ) {
				t_aGraph2Width.addLast(t_oChild2);
			}

			int t_iDiff = t_aChildren1.size() - t_aChildren2.size();
			if ( t_iDiff == 0 ) continue;

			// Add pseudo node as child of digraph having less number of children
			HierarchicalDigraphNode t_oPseudoNode = new HierarchicalDigraphNode(null, 0.0D);
			LinkedList<HierarchicalDigraphNode> t_aWidth = (t_iDiff<0)? t_aGraph1Width : t_aGraph2Width;
			for ( int i=0; i<Math.abs(t_iDiff); i++ )
				t_aWidth.addLast(t_oPseudoNode);
		}

		return 0;
	}

	/**
	 * Compare HierarchicalDigraphNodes depending on the purpose (for extends)
	 * @param a_oNode1 First node
	 * @param a_oNode2 Second node
	 * @return Result of comparison
	 */
	protected int compareNodes(HierarchicalDigraphNode a_oNode1, HierarchicalDigraphNode a_oNode2) {
		// 1. First, prioritize the atom which has greater atomic number.
		// 2. Next, compare next atoms by first compare method.
		// 3. Repeat compare.

		// Prioritize greater atomic number
		if ( Math.abs(a_oNode1.getAverageAtomicNumber() - a_oNode2.getAverageAtomicNumber()) > EPS )
			return ( a_oNode1.getAverageAtomicNumber() > a_oNode2.getAverageAtomicNumber() ) ? -1 : 1;

		return 0;
	}
}
