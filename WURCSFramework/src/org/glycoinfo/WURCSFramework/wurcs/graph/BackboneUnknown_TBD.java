package org.glycoinfo.WURCSFramework.wurcs.graph;


public class BackboneUnknown_TBD extends BackboneUnknown {

	public BackboneUnknown_TBD(char a_cAnomSymbol) {
		super(a_cAnomSymbol);
	}

	@Override
	/** Get skeltone code from BackboneCarbons */
	public String getSkeletonCode() {
		return "<Q>";
	}

	@Override
	public BackboneUnknown_TBD copy() {
		return new BackboneUnknown_TBD(this.getAnomericSymbol());
	}
}
