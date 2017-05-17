package org.glycoinfo.WURCSFramework.wurcs.map;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class for MAPGraph which indicate graph of atoms in MAP
 * @author MasaakiMatsubara
 *
 */
public class MAPGraph {

	private LinkedList<MAPStar> m_aStars;
	private LinkedList<MAPAtomAbstract> m_aAtoms;
	private LinkedList<MAPGraph> m_aChildGraphs;

	public MAPGraph() {
		this.m_aStars = new LinkedList<MAPStar>();
		this.m_aAtoms = new LinkedList<MAPAtomAbstract>();
		this.m_aChildGraphs = new LinkedList<MAPGraph>();
	}

	public boolean addAtom( MAPAtomAbstract a_oAtom ) {
		if ( this.m_aAtoms.contains(a_oAtom) ) return false;
		this.m_aAtoms.addLast(a_oAtom);
		if ( a_oAtom instanceof MAPStar )
			this.addStar( (MAPStar)a_oAtom );
		return true;
	}

	private boolean addStar( MAPStar a_oStar ) {
		if ( this.m_aStars.contains(a_oStar) ) return false;
		this.m_aStars.addLast(a_oStar);
		return true;
	}

	public LinkedList<MAPStar> getStars() {
		return this.m_aStars;
	}

	public LinkedList<MAPAtomAbstract> getAtoms() {
		return this.m_aAtoms;
	}

	public void addChildGraph( MAPGraph a_oChildGraph ) {
		this.m_aChildGraphs.addLast(a_oChildGraph);
	}

	public LinkedList<MAPGraph> getChildGraphs() {
		return this.m_aChildGraphs;
	}

	public HashMap<MAPAtomAbstract, LinkedList<MAPConnection>> getAtomToConnections() {
		HashMap<MAPAtomAbstract, LinkedList<MAPConnection>> t_mapAtomToConnections = new HashMap<MAPAtomAbstract, LinkedList<MAPConnection>>();
		for ( MAPAtomAbstract t_oAtom : this.getAtoms() ) {
			LinkedList<MAPConnection> t_aConnections = new LinkedList<MAPConnection>();
			// Add cyclic atom connections
			if ( t_oAtom instanceof MAPAtomCyclic )
				t_aConnections.addAll( t_mapAtomToConnections.get( ((MAPAtomCyclic)t_oAtom).getCyclicAtom() ) );

			// Correct connections
			t_aConnections.addAll( t_oAtom.getConnections() );

			// Map connections to atom
			if ( t_oAtom instanceof MAPAtomCyclic )
				t_oAtom = ((MAPAtomCyclic)t_oAtom).getCyclicAtom();
			t_mapAtomToConnections.put(t_oAtom, t_aConnections);
		}
		return t_mapAtomToConnections;
	}
}
