package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.map.analysis.ValenceBondCalculator;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtom;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPBondType;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStereo;

/**
 * Class for checking stereochemistry using CIP system for MAP TODO: detailed stereochemistry check
 * @author MasaakiMatsubara
 *
 */
public class MAPStereochemistryCheckerUsingCIPSystem {

	private MAPGraph m_oGraph;
	private boolean m_bHasStereoChange;

	public MAPStereochemistryCheckerUsingCIPSystem(MAPGraph a_oGraph) {
		this.m_oGraph = a_oGraph;
		this.m_bHasStereoChange = false;
	}

	public boolean hasStereoChange() {
		return this.m_bHasStereoChange;
	}

	/**
	 * Reset atom and bond stereos in MAPGraph
	 */
	public void resetStereo() {
		HashSet<MAPConnection> t_aSearchedConns = new HashSet<MAPConnection>();
		for ( MAPAtomAbstract t_oAtomAb : this.m_oGraph.getAtoms() ) {
			// Ignore other than MAPAtoms
			if ( !(t_oAtomAb instanceof MAPAtom) ) continue;
			MAPAtom t_oAtom = (MAPAtom)t_oAtomAb;

			// Reset stereo for atom
			this.m_bHasStereoChange = this.resetAtomStereo(t_oAtom);

			// For bonds
			for ( MAPConnection t_oConn : this.m_oGraph.getAtomToConnections().get(t_oAtom) ) {
				// Ignore searched connections
				if ( t_aSearchedConns.contains(t_oConn) ) continue;
				// Ignore reverse searched connections
				if ( t_aSearchedConns.contains(t_oConn.getReverse()) ) continue;
				t_aSearchedConns.add(t_oConn);

				MAPAtomAbstract t_oConnAtomAb = t_oConn.getAtom();
				// Ignore other than MAPAtoms
				if ( !(t_oConnAtomAb instanceof MAPAtom) ) continue;
				MAPAtom t_oConnAtom = (MAPAtom)t_oConnAtomAb;

				// Reset stereo for bond
				this.m_bHasStereoChange = this.resetBondStereo(t_oAtom, t_oConnAtom);
			}
		}
	}

	/**
	 * Reset bond stereo
	 * @param a_oAtom1 Connecting atom to a_oAtom2
	 * @param a_oAtom2 Connecting atom to a_oAtom1
	 * @return True if stereo has reset
	 */
	private boolean resetBondStereo( MAPAtom a_oAtom1, MAPAtom a_oAtom2 ) {
		MAPConnection t_oBond = null;
		for ( MAPConnection t_oConn : a_oAtom1.getConnections() ) {
			if ( !t_oConn.getAtom().equals(a_oAtom2) ) continue;
			t_oBond = t_oConn;
		}
		// Return false if atoms are not connected
		if ( t_oBond == null ) return false;
		// Return false if reverse bond is not exist
		if ( t_oBond.getReverse() == null ) return false;

		boolean t_bHasStereo = this.hasBondStereo(a_oAtom1, a_oAtom2);

		// Set null if no stereo
		if ( !t_bHasStereo ) {
			// Return false if already null
			if ( t_oBond.getStereo() == null )
				return false;

			t_oBond.setStereo(null);
			t_oBond.getReverse().setStereo(null);
			return true;
		}

		// Set unknown stereo if stereo is presence but not set
		if ( t_oBond.getStereo() == null ) {
			t_oBond.setStereo(MAPStereo.UNKNOWN);
			t_oBond.getReverse().setStereo(MAPStereo.UNKNOWN);
			return true;
		}

		// TODO: reset E/Z
		return false;
	}

	/**
	 * Reset atom stereo
	 * @param a_oAtom Target atom
	 * @return True if stereo has reset
	 */
	private boolean resetAtomStereo( MAPAtom a_oAtom ) {

		// Check stereo
		boolean t_bHasStereo = this.hasAtomStereo(a_oAtom);
		// Set null stereo if no stereo
		if ( !t_bHasStereo ) {
			if ( a_oAtom.getStereo() == null )
				return false;

			a_oAtom.setStereo(null);
			return true;
		}

		// Set unknown stereo if there is stereo but it not set
		if ( a_oAtom.getStereo() == null ) {
			a_oAtom.setStereo(MAPStereo.UNKNOWN);
			return true;
		}

		// For the atom having stereo
		MAPStereo t_enumStereo = a_oAtom.getStereo();
		if ( t_enumStereo == MAPStereo.UNKNOWN )
			return false;

		// TODO: reset R/S
		return false;
	}

	/**
	 * (Re)order MAPStars by stereo TODO: add methods for (re)ordering Star Indices
	 */
	private void orderMAPStarsByStereo() {
		LinkedList<MAPAtom> t_oStereoAtoms = new LinkedList<MAPAtom>();
		for ( MAPAtomAbstract t_oAtomAb : this.m_oGraph.getAtoms() ) {
			// Ignore other than MAPAtom
			if ( !(t_oAtomAb instanceof MAPAtom) ) continue;

			MAPAtom t_oAtom = (MAPAtom)t_oAtomAb;
			if ( !this.hasStereo(t_oAtom, null) ) continue;

			LinkedList<MAPStar> t_aRelatedStars = this.getMAPStarsChangingStereo(t_oAtom);
			if ( t_aRelatedStars.isEmpty() ) continue;

			// Check Star Indices are numbered uniquely
			HashSet<Integer> t_aUniqueIndices = new HashSet<Integer>();
			boolean t_bHasUniqueNumbers = true;
			for ( MAPStar t_oStar : t_aRelatedStars ) {
				if ( t_aUniqueIndices.contains( t_oStar.getStarIndex() ) ) {
					t_bHasUniqueNumbers = false;
					break;
				}
				t_aUniqueIndices.add( t_oStar.getStarIndex() );
			}
			if ( t_bHasUniqueNumbers ) continue;

			// TODO: add methods for ordering Star Indices
		}

	}

	public LinkedList<MAPStar> getMAPStarsChangingStereo( MAPAtom a_oAtom ) {
		LinkedList<MAPStar> t_aRelatedStars = new LinkedList<MAPStar>();

		// Calc default orders for connections (do not weight stars)
		LinkedList<MAPConnection> t_aConns = this.m_oGraph.getAtomToConnections().get(a_oAtom);
		HashMap<MAPConnection, Integer> t_mapConnToOrderOrig = this.getConnectionsOrder(t_aConns, new AtomicNumber());

		// Collect stars changing atom stereo
		for ( MAPStar t_oStar : this.m_oGraph.getStars() ) {
			// Weight a star
			AtomicNumber t_oAN = new AtomicNumber();
			t_oAN.setAdditionalWeight(t_oStar, 0.1D);

			// Compare connections
			HashMap<MAPConnection, Integer> t_mapConnToOrder = this.getConnectionsOrder(t_aConns, t_oAN);

			// Compare orders
			boolean t_bHasChanged = false;
			for ( MAPConnection t_oConn : t_mapConnToOrderOrig.keySet() ) {
				int t_iOrder1 = t_mapConnToOrderOrig.get(t_oConn);
				int t_iOrder2 = t_mapConnToOrder.get(t_oConn);
				if ( t_iOrder1 != t_iOrder2 ) {
					t_bHasChanged = true;
					break;
				}
			}
			// Add star if order has changed
			if ( !t_bHasChanged ) continue;
			t_aRelatedStars.add(t_oStar);
		}

		return t_aRelatedStars;
	}

	private boolean hasBondStereo( MAPAtom a_oAtom1, MAPAtom a_oAtom2 ) {
		MAPConnection t_oBond = null;
		for ( MAPConnection t_oConn : this.m_oGraph.getAtomToConnections().get(a_oAtom1) ) {
			if ( !t_oConn.getAtom().equals(a_oAtom2) ) continue;
			t_oBond = t_oConn;
		}
		// Return false if atoms are not connected
		if ( t_oBond == null ) return false;
		// Return false if reverse bond is not exist
		if ( t_oBond.getReverse() == null ) return false;

		// return false if bond is not double or aromatic
		if ( t_oBond.getBondType() != MAPBondType.DOUBLE && t_oBond.getBondType() != MAPBondType.AROMATIC)
			return false;

		// Check number of connection
		if ( !this.hasThreeConnections(a_oAtom1) ) return false;
		if ( !this.hasThreeConnections(a_oAtom2) ) return false;

		// Check stereo
		if ( !this.hasStereo( a_oAtom1, t_oBond              ) ) return false;
		if ( !this.hasStereo( a_oAtom2, t_oBond.getReverse() ) ) return false;

		return true;
	}

	private boolean hasAtomStereo( MAPAtom a_oAtom ) {
		if ( !this.hasFourConnections(a_oAtom) ) return false;

		return this.hasStereo(a_oAtom, null);
	}

	private boolean hasStereo( MAPAtom a_oAtom, MAPConnection a_oIgnoreConn ) {
		ValenceBondCalculator t_oVBCalc = new ValenceBondCalculator(this.m_oGraph);
		// Count connected hydrogens
		int t_iDiff = t_oVBCalc.countRemainingValence(a_oAtom);
		// Return false if the number is unknown
		if ( t_iDiff == -1 ) return false;
		// Return false if two or more hydrogens are connected
		if ( t_iDiff > 1 ) return false;

		// Check tautomeric isomerism
		if ( t_oVBCalc.hasTautomaricConnections(a_oAtom) ) return false;


		// Order connections
		LinkedList<MAPConnection> t_aConns = this.m_oGraph.getAtomToConnections().get(a_oAtom);
		if ( a_oIgnoreConn != null && t_aConns.contains(a_oIgnoreConn) )
			t_aConns.remove(a_oIgnoreConn);

		// Check uniqueness of connections with no factor

		AtomicNumber t_oAN = new AtomicNumber();
		// Weight stars by Star indices
//		for ( MAPStar t_oStar : this.m_oGraph.getStars() ) {
//			t_oAN.setAdditionalWeight(t_oStar, 0.1D * t_oStar.getStarIndex());
//		}
		// Get ordered connections
		HashMap<MAPConnection, Integer> t_mapConnToOrder = this.getConnectionsOrder(t_aConns, t_oAN);
		// Check uniqueness of connections
		HashSet<Integer> t_aUniqueOrders = new HashSet<Integer>();
		boolean t_bHasUniqueConnections = true;
		for ( MAPConnection t_oConn : t_mapConnToOrder.keySet() ) {
			if ( t_aUniqueOrders.contains( t_mapConnToOrder.get(t_oConn) ) ) {
				t_bHasUniqueConnections = false;
				break;
			}
			t_aUniqueOrders.add( t_mapConnToOrder.get(t_oConn) );
		}
		if ( t_bHasUniqueConnections )
			return true;


		// Check uniqueness of connections using MAPStars changing stereo

		// Check presence of MAPStars changing stereo
		if ( this.getMAPStarsChangingStereo(a_oAtom).isEmpty() ) return false;

		// Weight each star changing stereo by different factor
		AtomicNumber t_oAN2 = new AtomicNumber();
		int t_iFactor = 1;
		for ( MAPStar t_oStar : this.getMAPStarsChangingStereo(a_oAtom) ) {
			t_oAN2.setAdditionalWeight(t_oStar, 0.1D * t_iFactor);
			t_iFactor++;
		}
		// Get ordered connections
		t_mapConnToOrder = this.getConnectionsOrder(t_aConns, t_oAN2);
		// Check uniqueness of connections
		t_aUniqueOrders = new HashSet<Integer>();
		t_bHasUniqueConnections = true;
		for ( MAPConnection t_oConn : t_mapConnToOrder.keySet() ) {
			if ( t_aUniqueOrders.contains( t_mapConnToOrder.get(t_oConn) ) ) {
				t_bHasUniqueConnections = false;
				break;
			}
			t_aUniqueOrders.add( t_mapConnToOrder.get(t_oConn) );
		}
		if ( t_bHasUniqueConnections )
			return true;

		return false;
	}

	/**
	 * Check three connections including hidden hydrogens and double bond for sp2 atom
	 * @param a_oAtom Target atom (on the double bond)
	 * @return True if number of connections on the atom is three
	 */
	private boolean hasThreeConnections( MAPAtom a_oAtom ) {
		ValenceBondCalculator t_oVBCalc = new ValenceBondCalculator(this.m_oGraph);
		// Count number of connections
		int t_iConns = t_oVBCalc.countConnections(a_oAtom);
		// Return false if the number is unknown
		if ( t_iConns == -1 ) return false;
		// Return false if the atom does not have four connections
		if ( t_iConns != 3 ) return false;

		return true;
	}

	/**
	 * Check four connections including hidden hydrogens for sp3 atom
	 * @param a_oAtom Target atom
	 * @return True if number of connections on the a_oAtom is four (false if aromatic)
	 */
	private boolean hasFourConnections( MAPAtom a_oAtom ) {
		// Ignore aromatic atom (not consider sp2 atom)
		if ( a_oAtom.isAromatic() ) return false;

		ValenceBondCalculator t_oVBCalc = new ValenceBondCalculator(this.m_oGraph);
		// Count number of connections
		int t_iConns = t_oVBCalc.countConnections(a_oAtom);
		// Return false if the number is unknown
		if ( t_iConns == -1 ) return false;
		// Return false if the atom does not have four connections
		if ( t_iConns != 4 ) return false;

		return true;
	}

	/**
	 * Calculate order number of connections
	 * @param a_oAtom Target atom having connections
	 * @param a_oAN AtomicNumber having atom weights
	 * @return HashMap of order number of MAPConnections
	 */
	private HashMap<MAPConnection, Integer> getConnectionsOrder( LinkedList<MAPConnection> a_aConns, AtomicNumber a_oAN ) {
		MAPConnectionComparatorUsingCIPSystem t_oConnComp = new MAPConnectionComparatorUsingCIPSystem(this.m_oGraph, a_oAN);
		Collections.sort(a_aConns, t_oConnComp);
		// Order connections
		int t_iOrder = 1;
		HashMap<MAPConnection, Integer> t_mapConnToOrder = new HashMap<MAPConnection, Integer>();
		MAPConnection t_oPrevConn = null;
		for ( MAPConnection t_oConn : a_aConns ) {
			if ( t_oPrevConn == null ) {
				t_oPrevConn = t_oConn;
				t_mapConnToOrder.put(t_oConn, t_iOrder);
				continue;
			}
			// Increment if different connetions are found
			if ( t_oConnComp.compare(t_oPrevConn, t_oConn) != 0 )
				t_iOrder++;
			t_mapConnToOrder.put(t_oConn, t_iOrder);
		}

		return t_mapConnToOrder;
	}
}
