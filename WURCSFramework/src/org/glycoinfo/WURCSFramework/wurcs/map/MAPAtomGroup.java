package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Class for MAPAtomGroup which indicate some atomic group ("R" group)
 * @author MasaakiMatsubara
 *
 */
public class MAPAtomGroup extends MAPAtomAbstract {

	private int m_iGroupID;

	/**
	 * Constructor of MAPAtomGroup
	 * @param a_iGroupID Integer of group ID (0 if no ID)
	 */
	public MAPAtomGroup(int a_iGroupID) {
		this.m_iGroupID = a_iGroupID;
	}

	public int getGroupID() {
		return this.m_iGroupID;
	}

	@Override
	public String getSymbol() {
		return "R";
	}

	@Override
	public MAPStereo getStereo() {
		return null;
	}

	@Override
	public MAPAtomAbstract copy() {
		return new MAPAtomGroup(this.m_iGroupID);
	}

	@Override
	public int getValence() {
		// Return always 1
		return 1;
	}

}
