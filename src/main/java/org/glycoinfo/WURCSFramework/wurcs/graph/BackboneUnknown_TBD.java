package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

public class BackboneUnknown_TBD extends Backbone_TBD {

	public BackboneUnknown_TBD(char a_cAnomSymbol) {
		this.setAnomericSymbol(a_cAnomSymbol);
	}

	@Override
	public boolean addBackboneCarbon(BackboneCarbon bc) {
		return false;
	}

	@Override
	public LinkedList<BackboneCarbon_TBD> getBackboneCarbons() {
		return new LinkedList<BackboneCarbon_TBD>();
	}

	@Override
	/** Get skeltone code from BackboneCarbons */
	public String getSkeletonCode() {
		return "<0>";
	}

	@Override
	public int getAnomericPosition() {
		char t_cAnomSym = this.getAnomericSymbol();
		if ( t_cAnomSym == 'o' || t_cAnomSym == 'x' ) return 0;
		// TODO: position
		return -1;
//		return 1;
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
	public BackboneUnknown_TBD copy() {
		return new BackboneUnknown_TBD(this.getAnomericSymbol());
	}

	@Override
	public void invert() {
		// Do notthing
	}


}
