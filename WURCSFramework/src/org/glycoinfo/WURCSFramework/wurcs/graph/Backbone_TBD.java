package org.glycoinfo.WURCSFramework.wurcs.graph;



public class Backbone_TBD extends Backbone {

	/** BackboneCarbons  */
	private int  m_iAnomericPosition = 0;
	private char m_iAnomericSymbol   = 'x';

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
		int length = this.getLength();
		for ( int i=0; i<length; i++ )
			score += (i+1) * this.getBackboneCarbons().get(i).getDesctriptor().getCarbonScore();
		return score;
	}

	/**
	 * Copy
	 * @return copied backbone
	 */
	public Backbone_TBD copy() {
		Backbone_TBD copy = new Backbone_TBD();
		for ( BackboneCarbon bc : this.getBackboneCarbons() ) {
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
		super.invert();
		// Invert anomeric position
		if ( this.m_iAnomericPosition != 0 && this.m_iAnomericPosition != -1 )
			this.m_iAnomericPosition = this.getLength() + 1 - this.m_iAnomericPosition;
	}

}
