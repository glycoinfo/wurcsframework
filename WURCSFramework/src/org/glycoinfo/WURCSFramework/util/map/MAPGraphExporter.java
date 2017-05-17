package org.glycoinfo.WURCSFramework.util.map;

import java.util.HashMap;

import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPBondType;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

public class MAPGraphExporter {

	public String getMAP(MAPGraph a_oGraph) {
		String t_strMAPCode = "";

		HashMap<MAPAtomAbstract, Integer> t_mapAtomToID = new HashMap<MAPAtomAbstract, Integer>();
		int t_nAtom = 0;
		boolean t_bInAromatic = false;
		MAPAtomAbstract t_oPrevAtom = null;
		for ( MAPAtomAbstract t_oAtom : a_oGraph.getAtoms() ) {
			t_nAtom++;

			// For aromatic
			if ( !t_bInAromatic && t_oAtom.isAromatic() ) {
				t_strMAPCode += "(";
				t_bInAromatic = true;
			}
			if ( t_bInAromatic && !t_oAtom.isAromatic() ) {
				t_strMAPCode += ")";
				t_bInAromatic = false;
			}

			// For parent connection
			if ( t_oAtom.getParentConnection() != null ) {
				MAPConnection t_oConn = t_oAtom.getParentConnection();

				// For starting branch point
//				int t_iParentNum = t_oConn.getAtom().getNumber();
//				if ( t_oAtom.getNumber() - t_iParentNum > 1 )
				if ( t_oPrevAtom!=null && !t_oConn.getAtom().equals(t_oPrevAtom) ) {
					int t_iParentNum = t_mapAtomToID.get( t_oConn.getAtom() );
					t_strMAPCode += "/"+t_iParentNum;
				}

				// For bond type
				if ( t_oConn.getBondType() != MAPBondType.SINGLE ) {
					MAPBondType t_enumType = t_oConn.getBondType();
					if ( !t_bInAromatic )
						t_strMAPCode += t_enumType.getSymbol();
				}
				// For bond stereo
				if ( t_oConn.getStereo() != null )
					t_strMAPCode += "^"+t_oConn.getStereo().getSymbol();
			}

			t_oPrevAtom = t_oAtom;

			// For backbone carbon
			if ( t_oAtom instanceof MAPStar ) {
				MAPStar t_oCarbon = (MAPStar)t_oAtom;
				String t_strCarbon = "*";
				if ( t_oCarbon.getStarIndex() != 0 )
					t_strCarbon += t_oCarbon.getStarIndex();
				t_strMAPCode += t_strCarbon;
				continue;
			}

			// For cyclic atom
			if ( t_oAtom instanceof MAPAtomCyclic ) {
				MAPAtomCyclic t_oCyclic = (MAPAtomCyclic)t_oAtom;
				t_strMAPCode += "$"+t_mapAtomToID.get( t_oCyclic.getCyclicAtom() );
				t_nAtom--;
				continue;
			}

			// For other atom
			t_strMAPCode += t_oAtom.getSymbol();
			if ( t_oAtom.getStereo() != null )
				t_strMAPCode += "^"+t_oAtom.getStereo().getSymbol();

			// Map atom to ID
			t_mapAtomToID.put(t_oAtom, t_nAtom);
		}
		// For end of aromatic at last
		if ( t_bInAromatic ) t_strMAPCode += ")";

		return t_strMAPCode;
	}


}
