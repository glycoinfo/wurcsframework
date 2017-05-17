package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPBondType;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;


/**
 * Class for creating hierarchical digraph
 * @author MasaakiMatsubara
 *
 */
public class HierarchicalDigraphCreator {

	private AtomicNumber m_oANumCalc;
	private HierarchicalDigraphNode m_oRootHD;
	private int m_iDepthLimit;
	private boolean m_bIsCompletedFullSearch;
	private HashMap<MAPAtomAbstract, LinkedList<MAPConnection>> m_mapAtomToConnections;

	public HierarchicalDigraphCreator( AtomicNumber a_oANumCalc, MAPGraph a_oGraph ) {
		this.m_oANumCalc = a_oANumCalc;
		this.m_oRootHD = null;
		this.m_iDepthLimit = 1;
		this.m_bIsCompletedFullSearch = true;
		this.m_mapAtomToConnections = a_oGraph.getAtomToConnections();
	}

	public HierarchicalDigraphNode getHierarchicalDigraph() {
		return this.m_oRootHD;
	}

	public boolean isCompletedFullSearch() {
		return this.m_bIsCompletedFullSearch;
	}

	/**
	 * Make hierarchical digraph from the connection
	 * @param a_oGraph Target graph
	 * @param a_oStart Connection of start
	 * @param a_iDepth Depth limit of depth search, which must be an integer 1 or more
	 */
	public void start( MAPConnection a_oStart, int a_iDepth ) {
		this.m_oRootHD = new HierarchicalDigraphNode( a_oStart, this.getAtomicNumber(a_oStart.getAtom()) );
		this.m_iDepthLimit = a_iDepth;

		// Set start atom
		LinkedList<MAPAtomAbstract> t_aAncestors = new LinkedList<MAPAtomAbstract>();
		t_aAncestors.add(a_oStart.getReverse().getAtom());

		// Do IDDFS
		this.m_bIsCompletedFullSearch = this.depthSearch( this.m_oRootHD, t_aAncestors );
	}

	private double getAtomicNumber(MAPAtomAbstract a_oAtom) {
		return this.m_oANumCalc.getAtomicNumber(a_oAtom);
	}

	/**
	 * Construct HierarchicalDigraph using depth-limited search
	 * @param a_oHD Current node of HierarchicalDigraph
	 * @param a_iDepth Distance from depth limit
	 * @param a_aAncestors list of ancestor atoms
	 * @return false if the search reachs depth limit.
	 */
	private boolean depthSearch( HierarchicalDigraphNode a_oHD, LinkedList<MAPAtomAbstract> a_aAncestors ) {
		MAPAtomAbstract t_oAtom = a_oHD.getConnection().getAtom();
		if ( t_oAtom instanceof MAPAtomCyclic )
			t_oAtom = ((MAPAtomCyclic)t_oAtom).getCyclicAtom();

		if ( a_aAncestors.contains( t_oAtom ) ) return true;
		if ( a_aAncestors.size() > this.m_iDepthLimit ) return false;

		a_aAncestors.addLast(t_oAtom);

		boolean t_bIsCompletedFullSearch = true;

		// Add children
		int t_nAromaticConnection = 0;
		double t_dSumAtomicNumber = 0;
		for ( MAPConnection t_oConn : this.m_mapAtomToConnections.get(t_oAtom) ) {
			double t_dAtomicNumber = this.getAtomicNumber(t_oConn.getAtom());

			// Count aromatic connections and sum atomic number of naighbor aromatic atom
			boolean t_bIsAromatic = false;
			if ( t_oAtom.isAromatic() || t_oConn.getAtom().isAromatic() ) {
				t_nAromaticConnection++;
				t_dSumAtomicNumber += t_dAtomicNumber;
				t_bIsAromatic = true;
			}

			// Add child digraph as duplicated atoms for multiple connection
			MAPBondType t_enumBondType = t_oConn.getBondType();
			if ( t_enumBondType == MAPBondType.TRIPLE || t_enumBondType == MAPBondType.DOUBLE ) {
				if ( !t_bIsAromatic )
					a_oHD.addChild( new HierarchicalDigraphNode( null, t_dAtomicNumber ) );
				if ( t_enumBondType == MAPBondType.TRIPLE )
					a_oHD.addChild( new HierarchicalDigraphNode( null, t_dAtomicNumber ) );
			}

			// Depth search for child digraph except for reverse connection
			if ( t_oConn.equals( a_oHD.getConnection().getReverse() ) ) continue;
			HierarchicalDigraphNode t_oChildHD = new HierarchicalDigraphNode( t_oConn, t_dAtomicNumber );
			a_oHD.addChild( t_oChildHD );
			// Set false to full search flag reaching depth limit
			if ( !this.depthSearch(t_oChildHD, a_aAncestors) )
				t_bIsCompletedFullSearch = false;
		}
		// Add duplicated atom for aromatic bond
		if ( t_nAromaticConnection != 0 )
			a_oHD.addChild( new HierarchicalDigraphNode( null, t_dSumAtomicNumber/(double)t_nAromaticConnection ) );

		a_aAncestors.removeLast();

		return t_bIsCompletedFullSearch;
	}
}