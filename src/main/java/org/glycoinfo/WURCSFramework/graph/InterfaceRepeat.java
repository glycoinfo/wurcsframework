package org.glycoinfo.WURCSFramework.graph;

/**
 * Interface for repeating unit
 * @author matsubara
 *
 */
public interface InterfaceRepeat {

	public void setMinRepeatCount(int a_nRepMin);
	public void setMaxRepeatCount(int a_nRepMax);
	public int getMinRepeatCount();
	public int getMaxRepeatCount();

}
