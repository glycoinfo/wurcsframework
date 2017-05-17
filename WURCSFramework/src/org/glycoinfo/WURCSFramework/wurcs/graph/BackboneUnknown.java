package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

/**
 * Class for Backbone of unknown monosaccharide
 * @author MasaakiMatsubara
 *
 */
public class BackboneUnknown extends Backbone {

	private char m_cAnomericSymbol;

	public BackboneUnknown(char a_cAnomSymbol) {
		this.m_cAnomericSymbol = a_cAnomSymbol;
	}

	@Override
	public boolean addBackboneCarbon(BackboneCarbon bc) {
		return false;
	}

	@Override
	public LinkedList<BackboneCarbon> getBackboneCarbons() {
		return new LinkedList<BackboneCarbon>();
	}

	@Override
	/** Get skeltone code from BackboneCarbons */
	public String getSkeletonCode() {
		return "<0>";
	}

	@Override
	public int getAnomericPosition() {
		if ( this.m_cAnomericSymbol == 'o' || this.m_cAnomericSymbol == 'x' ) return 0;
		// TODO: position
		return -1;
//		return 1;
	}

	@Override
	public char getAnomericSymbol() {
		return this.m_cAnomericSymbol;
	}

	@Override
	public WURCSEdge getAnomericEdge() {
		return null;
	}

	@Override
	public boolean hasParent() {
		return false;
	}

	@Override
	public boolean hasUnknownLength() {
		return true;
	}

	@Override
	public Backbone copy() {
		return new BackboneUnknown(this.m_cAnomericSymbol);
	}

	@Override
	public void invert() {
		// Do notthing
	}

}
