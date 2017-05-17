package org.glycoinfo.WURCSFramework.wurcs.graph;

public interface CarbonDescriptorInterface {
	/** Get SkeletonCode character of the carbon */
	public char getChar();

	/** Get String of hybrid orbital of the carbon */
	public String getHybridOrbital();

	/** Whether or not the carbon is terminal */
	public Boolean isTerminal();

	/** Get number of unique modifications connected the carbon */
	public int getNumberOfUniqueModifications();

	/** Get string of stereo */
	public String getStereo();

	/**
	 * Get string of modification
	 * @param num Number of modification
	 * @return String of modification
	 */
	public String getModification(int num);

	/** Get score of the carbon for Backbone comparison */
	public int getCarbonScore();
}
