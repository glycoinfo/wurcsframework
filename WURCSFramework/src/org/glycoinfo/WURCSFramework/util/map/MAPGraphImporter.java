package org.glycoinfo.WURCSFramework.util.map;

import java.util.HashMap;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtom;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomGroup;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPBondType;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStereo;

public class MAPGraphImporter {

	public MAPGraph parseMAP( String a_strMAPCode ) throws WURCSFormatException {
		MAPGraph t_oGraph = new MAPGraph();

		HashMap<Integer, MAPAtomAbstract> t_mapIDToAtom = new HashMap<Integer, MAPAtomAbstract>();
		boolean t_bInAromatic = false;
		MAPBondType t_enumBondType = null;
		MAPStereo t_enumBondStereo = null;
		MAPAtomAbstract t_oPrevAtom = null;
		int t_nAtom = 0;
		for ( int i=0; i<a_strMAPCode.length(); i++ ) {
			char t_cX = a_strMAPCode.charAt(i);
			// For aromatic bracket
			if ( t_cX == '(' ) { t_bInAromatic = true;  continue; } // Set aromatic in
			if ( t_cX == ')' ) { t_bInAromatic = false; continue; } // Set aromatic out

			// For multiple bond symbol
			if ( MAPBondType.forSymbol(t_cX) != null ) { // '=' or '#'
				t_enumBondType = MAPBondType.forSymbol(t_cX);
				continue;
			}

			// For stereochemical symbol
			if ( t_cX == '^' ) {
				char t_cNext = a_strMAPCode.charAt(++i);
				MAPStereo t_enumStereo = MAPStereo.forSymbol(t_cNext);

				// For aromatic bond
				if ( t_bInAromatic )
					t_enumBondType = MAPBondType.AROMATIC;

				// For cis-trans ('E', 'Z' or 'X')
				if ( t_enumBondType == MAPBondType.DOUBLE || t_enumBondType == MAPBondType.AROMATIC ) {
					t_enumBondStereo = t_enumStereo;
					continue;
				}

				// For chirality ('R', 'S' or 'X')
				((MAPAtom)t_oPrevAtom).setStereo(t_enumStereo);
				continue;
			}

			String t_strAtom = ""+t_cX;
			if ( i+1 < a_strMAPCode.length() ) {
				char t_cNext = a_strMAPCode.charAt(i+1);
				if ( Character.isLowerCase(t_cNext) ) {
					t_strAtom += t_cNext;
					i++;
				}
			}

			// Concatenate following numbers
			String t_strPos = "";
			while ( i+1 < a_strMAPCode.length() ) {
				char t_cNext = a_strMAPCode.charAt(i+1);
				if ( !Character.isDigit(t_cNext) ) break;
				t_strPos += t_cNext;
				i++;
			}
			int t_iIndexNum = t_strPos.equals("")? 0 : Integer.valueOf(t_strPos);

			// For branching point
			if ( t_cX == '/' ) {
//				t_oPrevAtom = t_oGraph.getAtoms().get(t_iPos-1);
				t_oPrevAtom = t_mapIDToAtom.get(t_iIndexNum);
				continue;
			}


			MAPAtomAbstract t_oAtom = null;
//			int t_iAtomNum = t_oGraph.getAtoms().size()+1;
			t_nAtom++;

			// For Backbone carbon
			if ( t_cX == '*' ) {
				MAPStar t_oCarbon = new MAPStar();
				t_oCarbon.setStarIndex(t_iIndexNum);

				t_oAtom = t_oCarbon;
			}
			// For cyclic atom
			else if ( t_cX == '$' ) {
				t_oAtom = new MAPAtomCyclic( t_mapIDToAtom.get(t_iIndexNum) );
				t_nAtom--;
			}
			// For atom group
			else if ( t_strAtom.equals("R") ) {
				t_oAtom = new MAPAtomGroup(t_iIndexNum);
			}
			// For atom other than Backbone carbon
			else {
				t_oAtom = new MAPAtom(t_strAtom);
			}

			// Set aromatic
			if ( t_bInAromatic ) t_oAtom.setAromatic();

			// Error if no stereochemistry on aromatic bond
			if ( t_oPrevAtom != null && t_oPrevAtom.isAromatic() && t_oAtom.isAromatic() && t_enumBondStereo == null )
				throw new WURCSFormatException("Aromatic bond must have stereochemistry.");

			// Connect atoms
			if ( t_oPrevAtom != null ) {
				MAPConnection t_oChildConn = new MAPConnection(t_oAtom);
				MAPConnection t_oParentConn = new MAPConnection(t_oPrevAtom);
				if ( t_enumBondType != null ) {
					t_oChildConn.setBondType( t_enumBondType );
					t_oParentConn.setBondType( t_enumBondType );
				}
				if ( t_enumBondStereo != null ) {
					t_oChildConn.setStereo( t_enumBondStereo );
					t_oParentConn.setStereo( t_enumBondStereo );
				}
				t_oPrevAtom.addChildConnection(t_oChildConn);
				t_oAtom.setParentConnection(t_oParentConn);

				// Set reverse
				t_oChildConn.setReverse(t_oParentConn);
				t_oParentConn.setReverse(t_oChildConn);

				// Set connection from star
				if ( t_oAtom instanceof MAPStar )
					((MAPStar)t_oAtom).setConnection( t_oParentConn );
				if ( t_oPrevAtom instanceof MAPStar )
					((MAPStar)t_oPrevAtom).setConnection( t_oChildConn );
			}

			// Add atom
			t_oGraph.addAtom(t_oAtom);

			t_oPrevAtom = t_oAtom;

			// Initialize
			t_enumBondType = null;
			t_enumBondStereo = null;

			// Map atom to ID except for MAPAtomCyclic
			if ( t_oAtom instanceof MAPAtomCyclic ) continue;

			t_mapIDToAtom.put(t_nAtom, t_oAtom);
		}
		return t_oGraph;
	}

}
