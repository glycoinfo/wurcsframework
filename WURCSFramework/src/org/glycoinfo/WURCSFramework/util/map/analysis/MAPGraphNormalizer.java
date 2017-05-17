package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.map.analysis.cip.MAPStereochemistryCheckerUsingCIPSystem;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

/**
 * Class for normalizing MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class MAPGraphNormalizer {

	private MAPGraph m_oGraph;
	private MAPGraph m_oResult;
	private boolean m_bHasChangedStarIndices;
	private boolean m_bIsReorderedMAPGraph;
	private boolean m_bHasChangedStereo;

	public MAPGraphNormalizer(MAPGraph a_oGraph) {
		this.m_oGraph = a_oGraph;
		this.m_oResult = null;
		this.m_bHasChangedStarIndices = false;
		this.m_bIsReorderedMAPGraph = false;
	}

	public MAPGraph getNormalizedGraph() {
		return this.m_oResult;
	}

	public boolean hasChangedStarIndices() {
		return this.m_bHasChangedStarIndices;
	}

	public boolean hasReorderedMAPGraph() {
		return this.m_bIsReorderedMAPGraph;
	}

	public boolean hasChangedStereo() {
		return this.m_bHasChangedStereo;
	}

	/**
	 * Start normalization of MAPGraph.<br>
	 * 1. Change connection graph to be no parent connections<br>
	 * 2. Reorder star indices for MAPStars<br>
	 * 3. Retraverse MAPAtoms<br>
	 * 4. Make new graph with ordered MAPAtoms with parent connections
	 */
	public void start() {
		// Change connection graph to be no parent connections
		MAPGraph t_oNoParentGraph = this.copyGraphWithNoParentConnections(this.m_oGraph);

		// Reset stereos
		MAPStereochemistryCheckerUsingCIPSystem t_oStereoCheck = new MAPStereochemistryCheckerUsingCIPSystem(t_oNoParentGraph);
		t_oStereoCheck.resetStereo();

		// Order Star indices
		LinkedList<MAPStar> t_aStars = this.orderStarIndices(t_oNoParentGraph);
		MAPStar t_oHeadStar = t_aStars.getFirst();

		// Traverse graph
		LinkedList<MAPConnection> t_aOrderedConns = this.orderConnections(t_oHeadStar, t_oNoParentGraph);

		// Convert connections to MAPGraph
		MAPGraph t_oResultGraph = new MAPGraph();

		// Set first atom to MAPGraph
		t_oResultGraph.addAtom(t_oHeadStar);
		// Reconnect connections and add MAPAtomCyclic
		for ( MAPConnection t_oConn : t_aOrderedConns ) {

			MAPAtomAbstract t_oAtom = t_oConn.getAtom();

			// Get reverse connection as parent
			MAPConnection t_oParentConn = t_oConn.getReverse();

			// Remove parent connection from child connections
			t_oAtom.getChildConnections().remove(t_oParentConn);

			// For cyclic
			if ( t_oResultGraph.getAtoms().contains(t_oAtom) )
				t_oAtom = new MAPAtomCyclic( t_oAtom );

			t_oAtom.setParentConnection(t_oParentConn);

			// Add atom
			t_oResultGraph.addAtom(t_oAtom);
		}

		this.m_oResult = t_oResultGraph;
	}

	/**
	 * Copy MAPGraph with no parent connections
	 * @param a_oGraph Target MAPGraph
	 * @return Copied MAPGraph with no parent connections
	 */
	public MAPGraph copyGraphWithNoParentConnections( MAPGraph a_oGraph ) {
		MAPGraph t_oCopyGraph = new MAPGraph();

		// Copy and map atoms
		HashMap<MAPAtomAbstract, MAPAtomAbstract> t_mapOrigToCopyAtom = new HashMap<MAPAtomAbstract, MAPAtomAbstract>();
		for ( MAPAtomAbstract t_oAtom : a_oGraph.getAtoms() ) {
			// Copy and map atoms
			MAPAtomAbstract t_oCopy = t_oAtom.copy();
			if ( t_oAtom instanceof MAPAtomCyclic )
				t_oCopy = t_mapOrigToCopyAtom.get( ((MAPAtomCyclic)t_oAtom).getCyclicAtom() );
			// Set parameters
			if ( t_oAtom.isAromatic() ) t_oCopy.setAromatic();

			t_mapOrigToCopyAtom.put(t_oAtom, t_oCopy);
		}
		// Copy and map connections
		HashMap<MAPConnection, MAPConnection> t_mapOrigToCopyConn = new HashMap<MAPConnection, MAPConnection>();
		for ( MAPAtomAbstract t_oAtom : a_oGraph.getAtoms() ) {
			MAPAtomAbstract t_oCopy = t_mapOrigToCopyAtom.get(t_oAtom);
			// For parent connections
			if ( t_oAtom.getParentConnection() != null ) {
				MAPConnection t_oConn = t_oAtom.getParentConnection();
				MAPAtomAbstract t_oCopyConnAtom = t_mapOrigToCopyAtom.get( t_oConn.getAtom() );
				MAPConnection t_oCopyConn = t_oConn.copy(t_oCopyConnAtom);
				t_oCopy.addChildConnection(t_oCopyConn);
				t_mapOrigToCopyConn.put(t_oConn, t_oCopyConn);

				// Set reverse (parent connections are always copied after copy of child connections)
				MAPConnection t_oCopyRev = t_mapOrigToCopyConn.get( t_oConn.getReverse() );
				t_oCopyConn.setReverse(t_oCopyRev);
				t_oCopyRev.setReverse(t_oCopyConn);
			}
			// For child connections
			for ( MAPConnection t_oConn : t_oAtom.getChildConnections() ) {
				MAPAtomAbstract t_oCopyConnAtom = t_mapOrigToCopyAtom.get( t_oConn.getAtom() );
				MAPConnection t_oCopyConn = t_oConn.copy(t_oCopyConnAtom);
				t_oCopy.addChildConnection(t_oCopyConn);
				t_mapOrigToCopyConn.put(t_oConn, t_oCopyConn);
			}

			// Add to graph
			if ( !t_oCopyGraph.getAtoms().contains(t_oCopy) )
				t_oCopyGraph.addAtom(t_oCopy);

			// For MAPStar connection
			if ( t_oCopy instanceof MAPStar ) {
				MAPStar t_oStar = (MAPStar)t_oCopy;
				if ( !t_oStar.getConnections().isEmpty() )
					t_oStar.setConnection( t_oStar.getConnections().getFirst() );
			}
		}

		return t_oCopyGraph;
	}

	/**
	 * (Re)order Star Indices
	 * @param a_oGraph Target MAPGraph
	 * @return Ordered MAPStars
	 */
	private LinkedList<MAPStar> orderStarIndices(MAPGraph a_oGraph) {
		// Check and order MAPStars
		LinkedList<MAPStar> t_aStars = a_oGraph.getStars();
		MAPStarComparator t_oStarComp = new MAPStarComparator(a_oGraph);
		Collections.sort(t_aStars, t_oStarComp);

		// Order star indices
		HashMap<MAPStar, Integer> t_mapStarToIndex = new HashMap<MAPStar, Integer>();
		int t_iIndex = 1;
		t_mapStarToIndex.put( t_aStars.getFirst(), t_iIndex );

		int t_nCarbons = t_aStars.size();
		for ( int i=0 ; i<t_nCarbons-1; i++ ) {

			MAPStar t_oCi = t_aStars.get(i);
			MAPStar t_oCj = t_aStars.get(i+1);

			int t_iComp = t_oStarComp.compare( t_oCi, t_oCj );
			if ( t_iComp != 0 ) t_iIndex++;
			t_mapStarToIndex.put( t_oCj, t_iIndex );
		}
		// Set Star Index 0 when all carbons are same order
		if ( t_iIndex == 1 )
			for ( MAPStar t_oC : t_aStars )
				t_mapStarToIndex.put(t_oC, 0);
		// Compare reordered indices
		for ( MAPStar t_oC : t_aStars ) {
			if ( t_oC.getStarIndex() == t_mapStarToIndex.get(t_oC) ) continue;
			this.m_bHasChangedStarIndices = true;
		}
		// Reset Star Indices if the orders are changed
		if ( this.m_bHasChangedStarIndices )
			for ( MAPStar t_oC : t_aStars )
				t_oC.setStarIndex( t_mapStarToIndex.get(t_oC) );

		return t_aStars;
	}

	/**
	 * (Re)order connections started from a_oStart
	 * @param a_oStart Start atom to traverse
	 * @return Ordered MAPConnections
	 */
	private LinkedList<MAPConnection> orderConnections(MAPAtomAbstract a_oStart, MAPGraph a_oGraph) {
		// Return empty list if no start connection
		if ( a_oStart.getConnections().isEmpty() )
			return new LinkedList<MAPConnection>();

		// Traverse atoms
		LinkedList<MAPConnection> t_aTraversedConnections = new LinkedList<MAPConnection>();
		LinkedList<MAPAtomAbstract> t_aTraversedAtoms = new LinkedList<MAPAtomAbstract>();
		t_aTraversedAtoms.addLast(a_oStart);
		t_aTraversedConnections.addLast(  a_oStart.getConnections().getFirst() );

		MAPConnectionComparatorForMAPGraph t_oConnComp = new MAPConnectionComparatorForMAPGraph(a_oGraph);

		LinkedList<MAPConnection> t_aSelectedConnections = new LinkedList<MAPConnection>();
		while (true) {
			MAPConnection t_oTailConn = t_aTraversedConnections.getLast();

			// Get neighbour connections
			for ( MAPConnection t_oConn : t_oTailConn.getAtom().getConnections() ) {
				// Ignore stored connections
				if ( t_aTraversedConnections.contains(t_oConn) ) continue;
				if ( t_aTraversedConnections.contains(t_oConn.getReverse()) ) continue;
				if ( t_aSelectedConnections.contains(t_oConn) ) continue;
				if ( t_aSelectedConnections.contains(t_oConn.getReverse()) ) continue;

				t_aSelectedConnections.add(t_oConn);
			}

			// Break if no connections for searching
			if(t_aSelectedConnections.size()==0) break;

			// Reverse connection if the start atom is not nearer from tail atom than end atom
			for ( MAPConnection t_oConn : t_aSelectedConnections ) {
				int t_iStartAtomNum = t_aTraversedAtoms.indexOf( t_oConn.getReverse().getAtom() );
				int t_iEndAtomNum = t_aTraversedAtoms.indexOf( t_oConn.getAtom() );
				if ( t_iStartAtomNum==-1 || t_iEndAtomNum==-1 ) continue;
				if ( t_iStartAtomNum >= t_iEndAtomNum ) continue;
				// Reverse connection
				int t_iRevNum = t_aSelectedConnections.indexOf(t_oConn);
				t_aSelectedConnections.set(t_iRevNum, t_oConn.getReverse());
			}

			// Add tail connection as ignore atom and connection to calculate Morgan Number of subgraph
			t_oConnComp.addTailConnection( t_oTailConn );

			// Sort stored connections
			Collections.sort( t_aSelectedConnections, t_oConnComp );

			// Store the most prioritize connection
			MAPConnection t_oSelectedConn = t_aSelectedConnections.removeFirst();
			t_aTraversedConnections.addLast(t_oSelectedConn);
			t_aTraversedAtoms.addLast(t_oSelectedConn.getAtom());
		}

		return t_aTraversedConnections;
	}

}
