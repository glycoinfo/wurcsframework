package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.map.analysis.cip.MAPConnectionComparatorUsingCIPSystem;
import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

/**
 * Connection comparator for generating MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class MAPConnectionComparatorForMAPGraph implements Comparator<MAPConnection> {

	private MAPGraph m_oSubMol;
	private LinkedList<MAPAtomAbstract> m_aTraversedAtoms;
	private LinkedList<MAPConnection> m_aTraversedConnections;
	private HashMap<MAPAtomAbstract, Integer> m_mapAtomToInitialMorganNumber;
	private HashMap<MAPAtomAbstract, Integer> m_mapAtomToCurrentMorganNumber;
	private MAPConnectionComparatorUsingCIPSystem m_oCIPComp;

	public MAPConnectionComparatorForMAPGraph(MAPGraph a_oSubMol) {
		this.m_oSubMol = a_oSubMol;
		this.m_aTraversedAtoms = new LinkedList<MAPAtomAbstract>();
		this.m_aTraversedConnections = new LinkedList<MAPConnection>();

		this.m_mapAtomToInitialMorganNumber = this.calcMorganNumber();
		this.m_mapAtomToCurrentMorganNumber = this.calcMorganNumber();
		this.m_oCIPComp = new MAPConnectionComparatorUsingCIPSystem(a_oSubMol);
	}

	/**
	 * Add tail connection
	 * @param a_oConn tail connection
	 */
	public void addTailConnection(MAPConnection a_oConn) {
		if ( !this.m_aTraversedAtoms.contains( a_oConn.getReverse().getAtom() ) )
			this.m_aTraversedAtoms.addLast( a_oConn.getReverse().getAtom() );
		this.m_aTraversedAtoms.addLast( a_oConn.getAtom() );
		this.m_aTraversedConnections.addLast( a_oConn );
		this.m_aTraversedConnections.addLast( a_oConn.getReverse() );
		// Recalculate Morgan number for current state
		this.m_mapAtomToCurrentMorganNumber = this.calcMorganNumber();
	}

	/**
	 * Calculate Morgan numbers for current state
	 * @return HashMap of atom to morgan number
	 */
	public HashMap<MAPAtomAbstract, Integer> calcMorganNumber() {
		MorganAlgorithmForMAP t_oMA = new MorganAlgorithmForMAP(this.m_oSubMol);
		t_oMA.calcMorganNumber(this.m_aTraversedAtoms, this.m_aTraversedConnections);
		return t_oMA.getAtomToMorganNumber();
	}

	@Override
	public int compare(MAPConnection a_oConn1, MAPConnection a_oConn2) {

		int t_iComp = 0;

		MAPAtomAbstract t_oStart1 = a_oConn1.getReverse().getAtom();
		MAPAtomAbstract t_oStart2 = a_oConn2.getReverse().getAtom();
		MAPAtomAbstract t_oEnd1 = a_oConn1.getAtom();
		MAPAtomAbstract t_oEnd2 = a_oConn2.getAtom();

		// 1. Prioritize connected aromatic atom
		if ( this.m_aTraversedAtoms.getLast().isAromatic() ){
			if(  t_oEnd1.isAromatic()   && !t_oEnd2.isAromatic()   ) return -1;
			if( !t_oEnd1.isAromatic()   &&  t_oEnd2.isAromatic()   ) return 1;
			if(  t_oStart1.isAromatic() && !t_oStart2.isAromatic() ) return -1;
			if( !t_oStart1.isAromatic() &&  t_oStart2.isAromatic() ) return 1;
		}

		// 2. Prioritize a connection of the path searched latter
		t_iComp = this.m_aTraversedAtoms.indexOf(t_oStart2) - this.m_aTraversedAtoms.indexOf(t_oStart1);
		if ( t_iComp != 0 ) return t_iComp;

		// 3.1. Prioritize MAPStar
		LinkedList<MAPStar> t_aStars = this.m_oSubMol.getStars();
		if (  t_aStars.contains(t_oEnd1) && !t_aStars.contains(t_oEnd2) ) return -1;
		if ( !t_aStars.contains(t_oEnd1) &&  t_aStars.contains(t_oEnd2) ) return 1;

		// 3.2. Prioritize a connection connecting MAPStar
		int t_nStars1 = 0;
		int t_nStars2 = 0;
		for ( MAPConnection t_oConn : t_oEnd1.getConnections() )
			if ( t_aStars.contains(t_oConn.getAtom()) ) t_nStars1++;
		for ( MAPConnection t_oConn : t_oEnd2.getConnections() ) {
			if ( t_aStars.contains(t_oConn.getAtom()) ) t_nStars2++;
		}
		if ( t_nStars1!=0 && t_nStars2==0 ) return -1;
		if ( t_nStars1==0 && t_nStars2!=0 ) return 1;

		// 4. Prioritize higher Morgan number (toword center of non-search region)
		t_iComp = this.m_mapAtomToCurrentMorganNumber.get(t_oEnd2) - this.m_mapAtomToCurrentMorganNumber.get(t_oEnd1);
		if ( t_iComp != 0 ) return t_iComp;

		// 5. Prioritize large initial EC number (toword center of all region)
		t_iComp = this.m_mapAtomToInitialMorganNumber.get(t_oEnd2) - this.m_mapAtomToInitialMorganNumber.get(t_oEnd1);
		if ( t_iComp != 0 ) return t_iComp;

		// 6. Prioritize smaller atomic number
		AtomicProperties t_enumAP1 = AtomicProperties.forSymbol( t_oEnd1.getSymbol() );
		AtomicProperties t_enumAP2 = AtomicProperties.forSymbol( t_oEnd2.getSymbol() );
		// 6.1 Prioritize known atom symbols
		if ( t_enumAP1 == null && t_enumAP2 != null ) return -1;
		if ( t_enumAP1 != null && t_enumAP2 == null ) return 1;
		if ( t_enumAP1 != null && t_enumAP2 != null ) {
			t_iComp = t_enumAP1.getAtomicNumber() - t_enumAP2.getAtomicNumber();
			if ( t_iComp != 0 ) return t_iComp;
		}

		// 7. Prioritize lower number of bond type (bond order)
		t_iComp = a_oConn1.getBondType().getNumber() - a_oConn2.getBondType().getNumber();
		if ( t_iComp != 0 ) return t_iComp;

		// 8. Compare by CIP order
		t_iComp = this.m_oCIPComp.compare(a_oConn1, a_oConn2);
		if ( t_iComp != 0 ) return t_iComp;

		// For Backbone carbons
		if ( !t_aStars.contains(t_oEnd1) || !t_aStars.contains(t_oEnd2) ) return 0;

		// 9. Prioritize lower Star Index
		t_iComp = ((MAPStar)t_oEnd1).getStarIndex() - ((MAPStar)t_oEnd2).getStarIndex();
		if ( t_iComp != 0 ) return t_iComp;

		return 0;
	}

}