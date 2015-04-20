package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorException;


/**
 * Class for backbone of monosaccharide
 * @author MasaakiMatsubara
 *
 */
public class Backbone extends WURCSComponent{

	/** BackboneCarbons  */
	private LinkedList<BackboneCarbon> m_aCarbons = new LinkedList<BackboneCarbon>();
	/** Anomeric carbon which assigned */
	private BackboneCarbon m_objAnomericCarbon = null;
	/** Configurational carbon which assigned D/L */
//	private LinkedList<BackboneCarbon> m_objConfigurationalCarbons = new LinkedList<BackboneCarbon>();

	/**
	 * Add backbone carbon
	 * @param bc BackboneCarbon
	 * @return true if addition is succeed
	 */
	public boolean addBackboneCarbon( BackboneCarbon bc ) {
		if ( this.m_aCarbons.contains(bc) ) return false;
		this.checkAnomeric(bc);
		return this.m_aCarbons.add( bc );
	}

	/**
	 * Get list of BackboneCarbon in this component
	 * @return list of BackboneCarbon in this component
	 */
	public LinkedList<BackboneCarbon> getBackboneCarbons() {
		return this.m_aCarbons;
	}

	/** Get skeltone code from BackboneCarbons */
	public String getSkeletonCode() {
		String code = "";
		for ( BackboneCarbon oBC : this.m_aCarbons ) {
			code += oBC.getDesctriptor().getChar();
		}
		return code;
	}

	public int getAnomericPosition() {
		if ( this.m_objAnomericCarbon == null ) return 0;
		// For open chain
		CarbonDescriptor CD = this.m_objAnomericCarbon.getDesctriptor();
		if ( ! CD.isFootOfBridge() ) return 0;
		return this.getBackboneCarbons().indexOf(this.m_objAnomericCarbon)+1;
	}

	/** Get anomeric symbol */
	public char getAnomericSymbol() {
		// Get configurational carbon
		int pos = this.getAnomericPosition();
		if ( pos == 0 ) return 'x';
		int i = 0;
		BackboneCarbon bcConfig = null;
		for ( BackboneCarbon bc : this.getBackboneCarbons() ) {
			if ( !bc.isChiral() ) continue;
			i++;
			bcConfig = bc;
			// XXX remove print
//			System.err.print(bc.getDesctriptor().getChar());
			if ( i == 5 ) break;
		}

		// Determine anomeric charactor
		char anom = 'x';
		if ( bcConfig == null ) return anom;
		if ( this.m_objAnomericCarbon == null ) return anom;
		// XXX remove print
//		System.err.println(" "+bcConfig.getDesctriptor().getChar());

		char cConfig = bcConfig.getDesctriptor().getChar();
		char cAnom = this.m_objAnomericCarbon.getDesctriptor().getChar();

		if ( cConfig == 'x' || cConfig == 'X' ) {
			return (cAnom == '3' || cAnom == '7')? 'b' :
				   (cAnom == '4' || cAnom == '8')? 'a' : anom;
		}
		if ( !Character.isDigit(cConfig) ) return anom;
		int iConfig = Character.getNumericValue(cConfig);

		if ( !Character.isDigit(cAnom) ) return anom;
		int iAnom = Character.getNumericValue(cAnom);

		// XXX remove print
//		System.err.println(pos + ":" + iAnom + " vs " + i +":"+ iConfig);
		anom = ( iConfig%2 == iAnom%2 )? 'a' : 'b';

		return anom;
	}

	/**
	 * Get anomeric edge which is not fazzy and contain glycosidic linkage
	 * @return edge Edge on anomeric position
	 */
	public WURCSEdge getAnomericEdge() {
		if ( this.getAnomericPosition() == 0 ) return null;

		for ( WURCSEdge edge : this.getEdges() ) {
			if ( edge.getLinkages().size()>1 ) continue;
			if ( edge.getLinkages().get(0).getBackbonePosition() != this.getAnomericPosition() ) continue;
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

	public boolean hasUnknownLength() {
		for ( BackboneCarbon bc : this.m_aCarbons ) {
			if ( bc.hasUnknownLength() ) return true;
		}
		return false;
	}

	private void clear() {
		this.m_aCarbons.clear();
		this.m_objAnomericCarbon = null;
	}

	/**
	 * Copy
	 * @return copied backbone
	 */
	public Backbone copy() {
		Backbone copy = new Backbone();
		for ( BackboneCarbon bc : this.m_aCarbons ) {
			copy.addBackboneCarbon(bc.copy(copy));
		}
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
		LinkedList<BackboneCarbon> inverts = new LinkedList<BackboneCarbon>();
		for ( BackboneCarbon orig : this.m_aCarbons )
			inverts.addFirst( orig.invert(this) );
		this.clear();
		for ( BackboneCarbon inv : inverts ) {
			this.m_aCarbons.add(inv);
			this.checkAnomeric(inv);
		}
		for ( WURCSEdge edge : this.getEdges() ) {
			for ( LinkagePosition link : edge.getLinkages() ) {
				link.invertBackbonePosition(this.getBackboneCarbons().size());
			}
		}
	}

	private void checkAnomeric(BackboneCarbon bc) {
		// Set anomeric carbon
		if ( ! bc.isAnomeric() ) return;
		if ( this.m_objAnomericCarbon == null )
			this.m_objAnomericCarbon = bc;
		if ( this.m_objAnomericCarbon.getDesctriptor() == bc.getDesctriptor() ) return;

		if ( this.m_objAnomericCarbon.getDesctriptor().getCarbonScore() - bc.getDesctriptor().getCarbonScore() < 0 )
			this.m_objAnomericCarbon = bc;
	}

	@Override
	public void accept(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		a_objVisitor.visit(this);
	}

}
