package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;



/**
 * Class for modification
 * @author MasaakiMatsubara
 *
 */
public class Modification extends WURCSComponent{

	private String m_strMAPCode;

	public Modification( String MAPCode ) {
		this.m_strMAPCode = MAPCode;
	}

	public String getMAPCode() {
		return this.m_strMAPCode;
	}

	private LinkedList<LinkedList<String>> parseMAPToAtoms() {
		LinkedList<String> t_aAtoms = new LinkedList<String>();
		// TODO: change type atom name to atom ID
		LinkedList<String> t_aPrevAtoms = new LinkedList<String>();
		String t_strPrevAtom = null;
		for ( int i=0; i<this.m_strMAPCode.length(); i++ ) {
			char t_cX = this.m_strMAPCode.charAt(i);
			if ( t_cX == '(' || t_cX == ')' ) continue; // skip aromatic bracket
			if ( t_cX == '=' || t_cX == '#' ) continue; // skip multiple bond
			if ( t_cX == '^' ) { i++; continue; } // skip stereo

			// Concatenate following numbers
			String t_strPos = "";
			while ( i+1 < this.m_strMAPCode.length() ) {
				char t_cNext = this.m_strMAPCode.charAt(i+1);
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
				String t_strTargetAtom = t_aAtoms.get( t_iPos-1 );

				// Change previous atom if branching point
				if ( t_cX == '/' ) {
					t_strPrevAtom = t_strTargetAtom;
					continue;
				}

				// Add current atom if cyclic point
				if ( t_cX == '$' )
					t_strAtom = t_strTargetAtom;
			}
			// For other atoms which name is two characters
			else if ( i+1 < this.m_strMAPCode.length() ) {
				char t_cNext = this.m_strMAPCode.charAt(i+1);
				if ( Character.isLowerCase(t_cNext) )
					t_strAtom += t_cNext;
			}

			// Set atoms
			t_aPrevAtoms.addLast(t_strPrevAtom);
			t_aAtoms.addLast(t_strAtom);
			t_strPrevAtom = t_strAtom;
		}
		LinkedList<LinkedList<String>> t_aaAtomMap = new LinkedList<LinkedList<String>>();
		t_aaAtomMap.addLast( t_aAtoms );
		t_aaAtomMap.addLast( t_aPrevAtoms );
		return t_aaAtomMap;
	}

	public String getAtomConnectedBackboneCarbon(int a_iCarbonID) {
		String t_strCarbon = "*";
		if ( a_iCarbonID != 0 ) t_strCarbon += a_iCarbonID;
		LinkedList<LinkedList<String>> t_aaAtomMap = this.parseMAPToAtoms();
		LinkedList<String> t_aParsedAtoms = t_aaAtomMap.get(0);
		int t_iAtomPos = t_aParsedAtoms.indexOf( t_strCarbon );
		if ( t_iAtomPos == 0 )
			return t_aParsedAtoms.get(1);

		LinkedList<String> t_aPrevAtoms = t_aaAtomMap.get(1);
		return t_aPrevAtoms.get(t_iAtomPos);
	}

	/**
	 * Whether or not this is an aglycone.
	 * @return True if this is an aglycone
	 */
	public boolean isAglycone() {
		if ( this.getEdges().isEmpty() ) return false;
		for ( WURCSEdge edge : this.getEdges() ) {
			if ( edge.getLinkages().size() == 1
				&& edge.getBackbone().getAnomericPosition() == edge.getLinkages().getFirst().getBackbonePosition() ) continue;
			return false;
		}
		return true;
	}

	/**
	 * Whether or not this is in a glycosidic linkage.
	 * @return True if this is in a glycosidic linkage
	 */
	public boolean isGlycosidic() {
		LinkedList<WURCSEdge> edges = this.getEdges();
		if ( edges.isEmpty() || edges.size() == 1 ) return false;
		HashSet<Backbone> uniqBackbones = new HashSet<Backbone>();
		for ( WURCSEdge edge : edges ) {
			uniqBackbones.add( edge.getBackbone() );
			if ( uniqBackbones.size() > 1 ) // 2018/10/02 Masaaki added to speed up
				return true;
		}
		return false;
//		if ( uniqBackbones.size() < 2 ) return false;
//		return true;
	}

	/**
	 * Whether or not this is a ring ether.
	 * @return True if this is a ring ether
	 */
	public boolean isRing() {
		// False if glycosidic
		if ( this.isGlycosidic() ) return false;
		LinkedList<WURCSEdge> edges = this.getEdges();
		// False if not bridge (count of edge is not two)
		if ( edges.size() != 2 ) return false;
		// False if not ether (ether "*O*" is omitted)
		if ( !this.canOmitMAP() ) return false;
		Backbone t_oBackbone = edges.getFirst().getBackbone();
		int t_iAnomPos = t_oBackbone.getAnomericPosition();
		// False if backbone has no anomeric position (connected Backbone has no or unknown anomeric position)
//		if ( t_iAnomPos == -1 || t_iAnomPos == -1 ) return false;
// corrected by muller 190205 t_iAnomPos have to be 0 or -1 
// because no anomeric position or unknown anomeric position is correct condition
		if ( t_iAnomPos == 0 || t_iAnomPos == -1 ) return false;
		// False if not connected to anomeric position (not have a linkage to anomeric position)
		int t_nConnAnom = 0;
		for ( WURCSEdge edge : edges ) {
			if ( edge.getLinkages().getFirst().getBackbonePosition() == t_iAnomPos )
				t_nConnAnom++;
		}
		if ( t_nConnAnom != 1 ) return false;
		return true;
	}

	/**
	 *
	 * @return True if MAP code of this is omission terget
	 */
	public boolean canOmitMAP() {
		if ( this.m_strMAPCode.equals("") ) return true;

		// For modification of terminal carbon "A"
//		System.out.println(this.m_strMAPCode+":"+this.getEdges().isEmpty());
		if ( !this.getEdges().isEmpty() && ( this.m_strMAPCode.equals("*O") || this.m_strMAPCode.equals("*=O") ) ) {
			// XXX remove print
//			System.out.println(this.m_strMAPCode+":"+this.getEdges().isEmpty());
			WURCSEdge edge = this.getEdges().getFirst();
			Backbone t_oBackbone = edge.getBackbone();
			int pos = edge.getLinkages().getFirst().getBackbonePosition()-1;
			if ( t_oBackbone.getBackboneCarbons().get(pos).getDesctriptor().getChar() == 'A' ) return false;
		}
		if ( this.m_strMAPCode.equals("*O") || this.m_strMAPCode.equals("*=O") || this.m_strMAPCode.equals("*O*") )
			return true;
		return false;
	}

	/**
	 *
	 * @return True if modification has order of backbone carbon
	 */
	public boolean hasBackboneCarbonOrder() {
		return ( this.m_strMAPCode.contains("*1") );
	}


	public Modification copy() {
		return new Modification(this.m_strMAPCode);
	}

	@Override
	public void accept(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		a_objVisitor.visit(this);
	}

}
