package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;


public class Backbone_TBD extends Backbone {

	/** BackboneCarbons  */
	private LinkedList<BackboneCarbon_TBD> m_aCarbons = new LinkedList<BackboneCarbon_TBD>();
	private int  m_iAnomericPosition = 0;
	private char m_iAnomericSymbol   = 'x';

	/**
	 * Add backbone carbon
	 * @param bc BackboneCarbon
	 * @return true if addition is succeed
	 */
	public boolean addBackboneCarbon( BackboneCarbon_TBD bc ) {
		if ( this.m_aCarbons.contains(bc) ) return false;
		return this.m_aCarbons.add( bc );
	}

	/**
	 * Get list of BackboneCarbon in this component
	 * @return list of BackboneCarbon in this component
	 */
	public LinkedList<BackboneCarbon_TBD> getBackboneCarbons() {
		return this.m_aCarbons;
	}

	/** Get length of carbon chain */
	public int getLength() {
		return this.m_aCarbons.size();
	}

	/** Get skeltone code from BackboneCarbons */
	public String getSkeletonCode() {
		String code = "";
		for ( BackboneCarbon_TBD oBC : this.m_aCarbons ) {
			code += oBC.getDesctriptor().getChar();
		}
		return code;
	}


	public void setAnomericPosition(int a_iAnomPos) {
		this.m_iAnomericPosition = a_iAnomPos;
	}

	public void setAnomericSymbol(char a_cAnomSymbol) {
		this.m_iAnomericSymbol = a_cAnomSymbol;
	}

	public int getAnomericPosition() {
		return this.m_iAnomericPosition;
	}

	public char getAnomericSymbol() {
		return this.m_iAnomericSymbol;
	}

	/**
	 * Get anomeric edge which is not fazzy and contain glycosidic linkage
	 * @return edge Edge on anomeric position
	 */
	public WURCSEdge getAnomericEdge() {
		if ( this.m_iAnomericPosition == 0 ) return null;

		for ( WURCSEdge edge : this.getEdges() ) {
			if ( edge.getLinkages().size()>1 ) continue;
			if ( edge.getLinkages().get(0).getBackbonePosition() != this.m_iAnomericPosition ) continue;
			if ( edge.getModification() == null ) continue;
			if ( !edge.getModification().isGlycosidic() ) continue;
			if ( edge.getModification() instanceof InterfaceRepeat ) continue;
			return edge;
		}
		return null;
	}

	/**
	 * Whether or not the backbone has parent
	 * @return
	 */
	public boolean hasParent() {
		WURCSEdge t_objAnomEdge = this.getAnomericEdge();
		// If no anomeric position
		if ( t_objAnomEdge == null ) return false;
		// If anomeric position has glycosidic linkage
		if ( t_objAnomEdge.getModification().isGlycosidic() ) return true;
		// If anomeric modification is aglycone
		if ( t_objAnomEdge.getModification().isAglycone() ) return false;

		return true;
	}

	/**
	 * Calculate Backbone scores based on carbon score of CarbonDescriptor
	 * @return Integer of backbone score
	 */
	public int getBackboneScore() {
		int score = 0;
		score += 20 * this.m_iAnomericPosition;
		int length = this.m_aCarbons.size();
		for ( int i=0; i<length; i++ )
			score += (i+1) * this.m_aCarbons.get(i).getDesctriptor().getCarbonScore();
		return score;
	}

	/**
	 * Copy
	 * @return copied backbone
	 */
	public Backbone_TBD copy() {
		Backbone_TBD copy = new Backbone_TBD();
		for ( BackboneCarbon_TBD bc : this.m_aCarbons ) {
			copy.addBackboneCarbon(bc.copy(copy));
		}
		copy.setAnomericPosition(this.m_iAnomericPosition);
		copy.setAnomericSymbol(this.m_iAnomericSymbol);
		copy.removeAllEdges();
		// XXX Check needs for edge in copy
/*
		for ( WURCSEdge edge : this.getEdges() ) {
			if ( edge.getModification().isGlycosidic() ) continue;
			WURCSEdge copyEdge = edge.copy();
			copyEdge.setBackbone(copy);
			copyEdge.setModification(edge.getModification().copy());
			copy.addEdge(copyEdge);
		}
*/
		return copy;
	}

	/**
	 * Invert
	 * @return inverted backbone
	 */
	public void invert() {
		LinkedList<BackboneCarbon_TBD> inverts = new LinkedList<BackboneCarbon_TBD>();
		for ( BackboneCarbon_TBD orig : this.m_aCarbons )
			inverts.addFirst( orig.invert(this) );
		this.m_aCarbons.clear();
		for ( BackboneCarbon_TBD inv : inverts ) {
			this.m_aCarbons.add(inv);
		}
		for ( WURCSEdge edge : this.getEdges() ) {
			for ( LinkagePosition link : edge.getLinkages() ) {
				link.invertBackbonePosition(this.m_aCarbons.size());
			}
		}
		// Invert anomeric position
		if ( this.m_iAnomericPosition != 0 && this.m_iAnomericPosition != -1 )
			this.m_iAnomericPosition = this.m_aCarbons.size() + 1 - this.m_iAnomericPosition;
	}

	@Override
	public void accept(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		a_objVisitor.visit(this);
	}


}
