package org.glycoinfo.WURCSFramework.util.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

public class ModificationAnalyzer {

	private Modification m_oModification;
	private boolean m_bIsHydroxy = false;
	private boolean m_bIsEther = false;
	private LinkedList<String> m_aParsedAtoms = new LinkedList<String>();
	// TODO: change type atom name to atom ID
	private LinkedList<String> m_aPrevAtoms = new LinkedList<String>();
	private LinkedList<Integer> m_aPrevPoss = new LinkedList<Integer>();
	private LinkedList<Integer> m_aEtherONums = new LinkedList<Integer>();

	private MAPGraph m_oMAPGraph;

	public ModificationAnalyzer(Modification a_oModification) throws WURCSFormatException {
		this.m_oModification = a_oModification;

		if ( !a_oModification.canOmitMAP() ) {
			this.m_oMAPGraph = (new MAPGraphImporter()).parseMAP( a_oModification.getMAPCode() );
			this.parseMAPToAtoms( a_oModification.getMAPCode() );
			this.collectEtherONum();
			return;
		}

		// Flag for ether linkage "*O*"
		if ( a_oModification.isGlycosidic() ) {
			this.m_bIsEther = true;
			return;
		}

		// Flag for hydroxy group "*O"
		this.m_bIsHydroxy = true; // "*O"
	}

	public boolean isHydroxy() {
		return this.m_bIsHydroxy;
	}

	public boolean isEther() {
		return this.m_bIsEther;
	}

	public boolean isTypeII() {
		return true;
	}

	public String getConnectedAtom(int a_iCarbonID) {
		if ( this.m_bIsHydroxy || this.m_bIsEther ) return "O";

		for ( MAPStar t_oCarbon : this.m_oMAPGraph.getStars() ) {
			if ( t_oCarbon.getStarIndex() != a_iCarbonID ) continue;
			if ( t_oCarbon.getParentConnection() == null ) {
				if ( t_oCarbon.getChildConnections().isEmpty() )
					return "H";
				return t_oCarbon.getChildConnections().getFirst().getAtom().getSymbol();
			}

			return t_oCarbon.getParentConnection().getAtom().getSymbol();
		}
		return null;
	}

	public String getAtomConnectedBackboneCarbon(int a_iCarbonID) {
		if ( this.m_bIsHydroxy || this.m_bIsEther ) return "O";

		String t_strCarbon = "*";
		if ( a_iCarbonID != 0 ) t_strCarbon += a_iCarbonID;

		int t_iAtomPos = this.m_aParsedAtoms.indexOf( t_strCarbon );

		// Return second atom in MAP if target carbon is found at first atom
		if ( t_iAtomPos == 0 )
			return this.m_aParsedAtoms.get(1);

		// Return previous atom of target carbon
		return this.m_aPrevAtoms.get(t_iAtomPos);
	}

	public boolean containEther() {
		return ! this.m_aEtherONums.isEmpty();
	}

	private boolean isConnectedEtherO( int a_iCarbonID ) {
		String t_strCarbon = "*";
		if ( a_iCarbonID != 0 ) t_strCarbon += a_iCarbonID;

		int t_iAtomPos = this.m_aParsedAtoms.indexOf( t_strCarbon );
		if ( t_iAtomPos == 0 && this.m_aEtherONums.contains(1) )
			return true;

		int t_iPrevNum = this.m_aPrevPoss.get(t_iAtomPos);
		return this.m_aEtherONums.contains(t_iPrevNum);
	}

	public boolean isTypeIIICarbon( int a_iCarbonID ) {
		if ( this.isConnectedEtherO(a_iCarbonID) ) return false;
		return "O".equals( this.getAtomConnectedBackboneCarbon(a_iCarbonID) );
	}

	private void parseMAPToAtoms( String a_strMAPCode ) {
		int t_iPrevPos = -1;
		String t_strPrevAtom = null;
		for ( int i=0; i<a_strMAPCode.length(); i++ ) {
			char t_cX = a_strMAPCode.charAt(i);
			if ( t_cX == '(' || t_cX == ')' ) continue; // skip aromatic bracket
			if ( t_cX == '=' || t_cX == '#' ) continue; // skip multiple bond symbol
			if ( t_cX == '^' ) { i++; continue; } // skip stereo

			// Concatenate following numbers
			String t_strPos = "";
			while ( i+1 < a_strMAPCode.length() ) {
				char t_cNext = a_strMAPCode.charAt(i+1);
				if ( !Character.isDigit(t_cNext) ) break;
				t_strPos += t_cNext;
				i++;
			}

			String t_strAtom = ""+t_cX;

			// For Backbone carbon
			if ( t_cX == '*' ) {
				t_strAtom += t_strPos;
			}
			// For branching and cyclic point
			else if ( !t_strPos.equals("") ){
				int t_iPos = Integer.valueOf(t_strPos);
				String t_strTargetAtom = this.m_aParsedAtoms.get( t_iPos-1 );

				// Change previous atom if branching point
				if ( t_cX == '/' ) {
					t_iPrevPos = t_iPos-1;
					t_strPrevAtom = t_strTargetAtom;
					continue;
				}

				// Add current atom if cyclic point
				if ( t_cX == '$' )
					t_strAtom = t_strTargetAtom;
			}
			// For other atoms which name is two characters
			else if ( i+1 < a_strMAPCode.length() ) {
				char t_cNext = a_strMAPCode.charAt(i+1);
				if ( Character.isLowerCase(t_cNext) )
					t_strAtom += t_cNext;
			}

			// Add atoms
			this.m_aPrevAtoms.addLast( t_strPrevAtom );
			this.m_aParsedAtoms.addLast( t_strAtom );
			this.m_aPrevPoss.addLast( t_iPrevPos );
			t_strPrevAtom = t_strAtom;
			t_iPrevPos = i;
		}
	}

	private void collectEtherONum() {
		if ( this.m_bIsHydroxy ) return;
		if ( this.m_bIsEther ) {
			this.m_aEtherONums.addLast(1);
			return;
		}

		if ( this.m_aParsedAtoms.size() < 3 ) return;

		// For ether contained first carbon
		if ( this.m_aParsedAtoms.get(1).equals("O") ) {
			for ( int i=2; i<this.m_aParsedAtoms.size(); i++ ) {
				String t_strAtom = this.m_aParsedAtoms.get(i);
				if ( t_strAtom == null || !t_strAtom.contains("*") ) continue;

				int t_iPrevNum = this.m_aPrevPoss.get(i);
				if ( t_iPrevNum != 1 ) continue;
				this.m_aEtherONums.addLast(1);
			}
		}

		// For ether not contained first carbon
		for ( int i=2; i<this.m_aParsedAtoms.size(); i++ ) {
			String t_strAtom1 = this.m_aParsedAtoms.get(i);
			if ( t_strAtom1 == null || !t_strAtom1.contains("*") ) continue;
			if ( "O".equals(this.m_aPrevAtoms.get(i)) ) continue;

			int t_iPrevNum1 = this.m_aPrevPoss.get(i);

			for ( int j=i+1; j<this.m_aParsedAtoms.size(); j++ ) {
				String t_strAtom2 = this.m_aParsedAtoms.get(j);
				if ( t_strAtom2 == null || !t_strAtom2.contains("*") ) continue;
				int t_iPrevNum2 = this.m_aPrevPoss.get(j);

				if ( t_iPrevNum1 != t_iPrevNum2 ) continue;
				this.m_aEtherONums.addLast(t_iPrevNum1);
			}
		}
	}

}
