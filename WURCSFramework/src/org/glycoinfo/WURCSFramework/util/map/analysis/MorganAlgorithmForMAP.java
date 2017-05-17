package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

/**
 * Class for calculating Morgan number using Morgan algorithm for MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class MorganAlgorithmForMAP {

	private MAPGraph m_oGraph;
	private HashMap<MAPAtomAbstract, Integer> m_mapAtomToMorganNumber = new HashMap<MAPAtomAbstract, Integer>();

	public MorganAlgorithmForMAP(MAPGraph a_oGraph) {
		this.m_oGraph = a_oGraph;
	}

	/** Get updated Morgan numbers */
	public HashMap<MAPAtomAbstract, Integer> getAtomToMorganNumber() {
		HashMap<MAPAtomAbstract, Integer> t_mapAtomToMorganNumberCopy = new HashMap<MAPAtomAbstract, Integer>();
		for ( MAPAtomAbstract t_oAtom : this.m_mapAtomToMorganNumber.keySet() )
			t_mapAtomToMorganNumberCopy.put(t_oAtom, this.m_mapAtomToMorganNumber.get(t_oAtom));
		return t_mapAtomToMorganNumberCopy;
	}

	public int getMorganNumber(MAPAtomAbstract a_oAtom) {
		if ( this.m_mapAtomToMorganNumber.get(a_oAtom) == null ) return 0;
		return this.m_mapAtomToMorganNumber.get(a_oAtom);
	}

	/**
	 * Calculate Morgan number of the MAPGraph ignoring atoms and connections specified as auguments
	 * @param a_oIgnoreConns Ignore list of MAPConnections
	 * @param a_oIgnoreAtoms Ignore list of MAPAtomAbstracts
	 */
	public void calcMorganNumber(final LinkedList<MAPAtomAbstract> a_oIgnoreAtoms, final LinkedList<MAPConnection> a_oIgnoreConns){

		// Init Morgan numbers
		HashMap<MAPAtomAbstract, Integer> t_mapAtomToMorganNum = new HashMap<MAPAtomAbstract, Integer>();
		LinkedList<MAPAtomAbstract> t_aTargetAtoms = this.m_oGraph.getAtoms();
		LinkedList<MAPAtomCyclic> t_aCyclics = new LinkedList<MAPAtomCyclic>();
		for ( MAPAtomAbstract t_oAtom : t_aTargetAtoms ) {
			if ( t_oAtom instanceof MAPAtomCyclic ) {
				t_aCyclics.add( (MAPAtomCyclic)t_oAtom );
				continue;
			}
			t_mapAtomToMorganNum.put( t_oAtom, this.getAtomWeight(t_oAtom) );
		}

		// Count Morgan number
		int t_iUniqCountPrev = 1;
		while (true) {
			for ( MAPAtomAbstract t_oAtom : t_aTargetAtoms )
				this.m_mapAtomToMorganNumber.put( t_oAtom, t_mapAtomToMorganNum.get(t_oAtom) );

			for ( MAPAtomAbstract t_oAtom : t_aTargetAtoms )
				t_mapAtomToMorganNum.put(t_oAtom, 0);

			for ( MAPAtomAbstract t_oAtom : this.m_oGraph.getAtoms() ) {
				// Collect connections
				LinkedList<MAPConnection> t_aConnections = new LinkedList<MAPConnection>();
				if ( t_oAtom.getParentConnection() != null )
					t_aConnections.add( t_oAtom.getParentConnection() );
				t_aConnections.addAll( t_oAtom.getChildConnections() );
				if ( t_oAtom instanceof MAPAtomCyclic ) {
					t_oAtom = ((MAPAtomCyclic)t_oAtom).getCyclicAtom();
				}
				if( a_oIgnoreAtoms != null && a_oIgnoreAtoms.contains(t_oAtom)) continue;

				// Add Morgan numbers of connected atoms
				int t_iNum = t_mapAtomToMorganNum.get(t_oAtom);
				for ( MAPConnection t_oConn : t_aConnections ) {
					MAPAtomAbstract t_oConnAtom = t_oConn.getAtom();
					if ( t_oConnAtom instanceof MAPAtomCyclic )
						t_oConnAtom = ((MAPAtomCyclic)t_oConnAtom).getCyclicAtom();
					if ( a_oIgnoreConns != null && a_oIgnoreConns.contains(t_oConn) ) continue;
					if ( a_oIgnoreAtoms != null && a_oIgnoreAtoms.contains(t_oConnAtom) ) continue;
					Integer t_iConnNum = this.m_mapAtomToMorganNumber.get(t_oConnAtom);
					if ( t_iConnNum == null ) continue;
					t_iNum += t_iConnNum;
				}
				t_mapAtomToMorganNum.put(t_oAtom, t_iNum);
			}
			int t_iUniqCount = this.countUniqueNumber(t_mapAtomToMorganNum);
			// XXX: remove print
//			System.err.println( t_iUniqCount );
			if ( t_iUniqCount <= t_iUniqCountPrev ) break;
			t_iUniqCountPrev = t_iUniqCount;
		}
	}

	/**
	 * Get atom weight (for extends)
	 * @param a_oAtom Target atom
	 * @return Number of atom weight
	 */
	protected int getAtomWeight( MAPAtomAbstract a_oAtom ) {
		// Return always 1 for default
		return 1;
	}

	/**
	 * Return the number of unique Morgan numbers
	 * @return the number of unique Morgan numbers
	 */
	private int countUniqueNumber(HashMap<MAPAtomAbstract, Integer> a_mapAtomToMorganNum) {
		LinkedList<Integer> t_aUniqNums = new LinkedList<Integer>();
		for ( Integer t_iNum : a_mapAtomToMorganNum.values() ) {
			if ( t_iNum == null ) continue;
			if ( t_aUniqNums.contains(t_iNum) ) continue;
			t_aUniqNums.add(t_iNum);
		}
		return t_aUniqNums.size();
	}
}
