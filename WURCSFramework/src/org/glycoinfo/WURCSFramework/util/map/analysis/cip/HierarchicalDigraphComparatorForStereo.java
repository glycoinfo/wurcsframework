package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStereo;

public class HierarchicalDigraphComparatorForStereo extends HierarchicalDigraphComparator {

	@Override
	protected int compareNodes( HierarchicalDigraphNode a_oNode1, HierarchicalDigraphNode a_oNode2 ) {
		// 4. Compare isotope (but not perfomed here since isomer infomation is reduced at normalization)

		// 5. When two groups are same as substance and topology (atomic elements, numbers, bond orders and masses of composition are same)
		//    but stereochemistries are different.
		//    First, Z is prior than E for geometrical isomerisms of the double bonds.
		//    Next, like (R,R or S,S) is prior than unlike (R,S or S,R) for the diastereo isomerism.
		//    Next, R is prior than S for enantiomerism.
		//    Finaly, r is prior than s for pseudoasymmetry atoms.
		if ( a_oNode1.getConnection() == null || a_oNode2.getConnection() == null ) return 0;

		MAPConnection t_oConn1 = a_oNode1.getConnection();
		MAPConnection t_oConn2 = a_oNode2.getConnection();

		// Compare bond stereo
		int t_iComp = this.compareBondStereo(t_oConn1, t_oConn2);
		if ( t_iComp != 0 ) return t_iComp;

		// Stereo for parent atom
		MAPStereo t_enumAtomStereo1 = t_oConn1.getReverse().getAtom().getStereo();
		MAPStereo t_enumAtomStereo2 = t_oConn2.getReverse().getAtom().getStereo();

		// Stereo for child atom
		MAPStereo t_enumNextStereo1 = t_oConn1.getAtom().getStereo();
		MAPStereo t_enumNextStereo2 = t_oConn2.getAtom().getStereo();

		// For diastereoisomerism, prioritize "like" (R,R or S,S) more than "unlike" (R,S or S,R)
		if( t_enumAtomStereo1!=null && t_enumAtomStereo2!=null){
			if( t_enumNextStereo1 == t_enumAtomStereo1 && t_enumNextStereo2 != t_enumAtomStereo2 ) return -1;
			if( t_enumNextStereo1 != t_enumAtomStereo1 && t_enumNextStereo2 == t_enumAtomStereo2 ) return 1;
		}

		if ( t_enumNextStereo1!=null && t_enumNextStereo2==null ) return -1;
		if ( t_enumNextStereo1==null && t_enumNextStereo2!=null ) return 1;
		if( t_enumNextStereo1 != null && t_enumNextStereo2 != null) {
			// For unknown enantiomerism, prioritize known side
			if( t_enumNextStereo1 != MAPStereo.UNKNOWN && t_enumNextStereo2 == MAPStereo.UNKNOWN ) return -1;
			if( t_enumNextStereo1 == MAPStereo.UNKNOWN && t_enumNextStereo2 != MAPStereo.UNKNOWN ) return 1;

			// For enantiomerism, prioritize "R" more than "S"
			if( t_enumNextStereo1 == MAPStereo.RECTUS   && t_enumNextStereo2 != MAPStereo.RECTUS   ) return -1;
			if( t_enumNextStereo1 != MAPStereo.RECTUS   && t_enumNextStereo2 == MAPStereo.RECTUS   ) return 1;
			if( t_enumNextStereo1 == MAPStereo.SINISTER && t_enumNextStereo2 != MAPStereo.SINISTER ) return -1;
			if( t_enumNextStereo1 != MAPStereo.SINISTER && t_enumNextStereo2 == MAPStereo.SINISTER ) return 1;

			// For the atom with pseudoasymmetry, prioritize "r" more than "s"
			if( t_enumNextStereo1 == MAPStereo.P_RECTUS   && t_enumNextStereo2 == MAPStereo.P_SINISTER ) return -1;
			if( t_enumNextStereo1 == MAPStereo.P_SINISTER && t_enumNextStereo2 == MAPStereo.P_RECTUS   ) return 1;
		}

		return 0;
	}

	private int compareBondStereo( MAPConnection a_oConn1, MAPConnection a_oConn2 ) {
		// For double or aromatic bond geometrical isomerism, "Z" > "E" > "X" > no isomerism
		MAPStereo t_enumBondStereo1 = a_oConn1.getStereo();
		MAPStereo t_enumBondStereo2 = a_oConn2.getStereo();
		if ( t_enumBondStereo1!=null && t_enumBondStereo2==null ) return -1;
		if ( t_enumBondStereo1==null && t_enumBondStereo2!=null ) return 1;
		if ( t_enumBondStereo1!=null && t_enumBondStereo1!=null ) {
			if( t_enumBondStereo1 != MAPStereo.UNKNOWN && t_enumBondStereo2 == MAPStereo.UNKNOWN ) return -1;
			if( t_enumBondStereo1 == MAPStereo.UNKNOWN && t_enumBondStereo2 != MAPStereo.UNKNOWN ) return 1;
			if( t_enumBondStereo1 == MAPStereo.CIS    && t_enumBondStereo2 == MAPStereo.TRANCE ) return -1;
			if( t_enumBondStereo1 == MAPStereo.TRANCE && t_enumBondStereo2 == MAPStereo.CIS    ) return 1;
		}

		return 0;
	}

}
