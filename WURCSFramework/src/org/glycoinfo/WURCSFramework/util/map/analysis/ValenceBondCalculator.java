package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPBondType;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

public class ValenceBondCalculator {

	private HashMap<MAPAtomAbstract, LinkedList<MAPConnection>> m_mapAtomToConnections;

	public ValenceBondCalculator(MAPGraph a_oGraph) {
		this.m_mapAtomToConnections = a_oGraph.getAtomToConnections();
	}

	/**
	 * Count number of valence on an atom
	 * @param a_oAtom Target atom
	 * @return Number of valence (-1 if the number is unknown or the atom is cyclic)
	 */
	public int countValence( MAPAtomAbstract a_oAtom ) {
		// Return -1 if the atom is cyclic
		if ( a_oAtom instanceof MAPAtomCyclic )
			return -1;

		// Count connection and valence
		int t_nValence = 0;
		int t_nAromatic = 0;
		for ( MAPConnection t_oConn : this.m_mapAtomToConnections.get(a_oAtom) ) {
			MAPBondType t_enumBT = t_oConn.getBondType();
			// Return -1 if unknown bond type is contained
			if ( t_enumBT == MAPBondType.UNKNOWN )
				return -1;
			if ( t_enumBT == MAPBondType.AROMATIC ) {
				t_nAromatic++;
				continue;
			}

			t_nValence += t_enumBT.getNumber();
		}

		// Return -1 if aromatic bond number is one, four or more
		if ( t_nAromatic == 1 || t_nAromatic > 3 )
			return -1;
		// For two aromatic bond
		if ( t_nAromatic == 2 )
			t_nValence += t_nAromatic * 1.5;
		// For three aromatic bond
		if ( t_nAromatic == 3 ) {
			// Return -1 if other valence is exist
			if ( t_nValence > 0 )
				return -1;
			// Reset valence number
			t_nValence = 4;
		}

		return t_nValence;
	}

	/**
	 * Count number of remaining valence on an atom (number of hidden hydrogen)
	 * @param a_oAtom Target atom
	 * @return Number of remaining valence (-1 if the number is unknown or the atom is cyclic)
	 */
	public int countRemainingValence( MAPAtomAbstract a_oAtom ) {

		int t_nBasicValence = a_oAtom.getValence();

		// Return -1 if valence number is unknown
		if ( t_nBasicValence == -1 )
			return -1;

		// Count connection and valence
		int t_nValence = this.countValence(a_oAtom);
		if ( t_nValence == -1 ) return -1;

		// Count remaining valence
		int t_iDiff = t_nBasicValence - t_nValence;
		if ( t_nBasicValence > 4 ) {
			if ( t_iDiff > 6 ) t_iDiff -= 6; // For the atom having three lone pair (Cl)
			if ( t_iDiff > 4 ) t_iDiff -= 4; // For the atom having two lone pair (S, P)
			if ( t_iDiff > 2 ) t_iDiff -= 2; // For the atom having a lone pair
		}

		return t_iDiff;
	}

	/**
	 * Count number of connections containing hidden hydrogens
	 * @param a_oAtom Target atom
	 * @return Number of connections (-1 if the number is unknown)
	 */
	public int countConnections( MAPAtomAbstract a_oAtom ) {
		// Count remaining valence
		int t_iDiff = this.countRemainingValence(a_oAtom);
		if ( t_iDiff == -1 ) return -1;

		// Add number of remaining valence as hidden hydrogens to nunber of connections
		LinkedList<MAPConnection> t_aConns = this.m_mapAtomToConnections.get(a_oAtom);
		int t_nConnection = t_aConns.size() + t_iDiff;
		return t_nConnection;
	}

	/**
	 * Check connections having tautomarism on the atom
	 * @param a_oAtom Target atom
	 * @return True if tautomaric connections are exist
	 */
	public boolean hasTautomaricConnections( MAPAtomAbstract a_oAtom ) {
		int t_nDoubleBondNOS = 0;
		int t_nSingleBondNOSWithHydrogen = 0;
		for ( MAPConnection t_oConn : this.m_mapAtomToConnections.get(a_oAtom) ) {
			MAPAtomAbstract t_oAtom = t_oConn.getAtom();
			// Ignore other than N, O or S
			if ( !t_oAtom.getSymbol().equals("N") && !t_oAtom.getSymbol().equals("O") && !t_oAtom.getSymbol().equals("S") )
				continue;

			// Count double bond NOS
			if ( t_oConn.getBondType() == MAPBondType.DOUBLE ) {
				if ( this.countRemainingValence(t_oAtom) == 0 )
					t_nDoubleBondNOS++;
			}
			// Count single bond NOS with hydrogen
			if ( t_oConn.getBondType() == MAPBondType.SINGLE ) {
				if ( this.countRemainingValence(t_oAtom) == 1 )
					t_nSingleBondNOSWithHydrogen++;
			}
		}
		return ( t_nDoubleBondNOS > 0 && t_nSingleBondNOSWithHydrogen > 0 );
	}
}
