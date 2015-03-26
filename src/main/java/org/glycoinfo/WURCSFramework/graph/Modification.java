package org.glycoinfo.WURCSFramework.graph;

import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorException;



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

	/**
	 * Whether or not this is an aglycone
	 * @return True or false
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

	public boolean isGlycosidic() {
		LinkedList<WURCSEdge> edges = this.getEdges();
		if ( edges.isEmpty() || edges.size() == 1 ) return false;
		HashSet<Backbone> uniqBackbones = new HashSet<Backbone>();
		for ( WURCSEdge edge : edges ) {
			uniqBackbones.add( edge.getBackbone() );
		}
		if ( uniqBackbones.size() < 2 ) return false;
		return true;
	}

	/**
	 *
	 * @return true if MAP code of the Modification is omission terget
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

	public Modification copy() {
		return new Modification(this.m_strMAPCode);
	}

	@Override
	public void accept(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		a_objVisitor.visit(this);
	}

}
