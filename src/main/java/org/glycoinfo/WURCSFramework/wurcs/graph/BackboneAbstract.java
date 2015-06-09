package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

public abstract class BackboneAbstract extends WURCSComponent {
	public abstract boolean addBackboneCarbon(BackboneCarbon bc);
	public abstract LinkedList<BackboneCarbon> getBackboneCarbons();

	public abstract String getSkeletonCode();

	public abstract int getAnomericPosition();
	public abstract char getAnomericSymbol();

	public abstract WURCSEdge getAnomericEdge();

	public abstract boolean hasParent();

	public abstract boolean hasUnknownLength();

	public abstract void invert();
}
