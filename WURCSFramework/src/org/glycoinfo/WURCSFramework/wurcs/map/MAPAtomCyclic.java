package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Class for MAPAtomCyclic which indicate cyclic atom to represent tree structure
 * @author MasaakiMatsubara
 *
 */
public class MAPAtomCyclic extends MAPAtomAbstract {

	private MAPAtomAbstract m_oCyclicAtom;

	/**
	 * Constructor for MAPAtomCyclic
	 * @param a_oCyclicAtom MAPAtomAbstact of cyclic atom
	 */
	public MAPAtomCyclic(MAPAtomAbstract a_oCyclicAtom) {
		this.m_oCyclicAtom = a_oCyclicAtom;
	}

	public MAPAtomAbstract getCyclicAtom() {
		return this.m_oCyclicAtom;
	}

	@Override
	public boolean isAromatic() {
		return this.m_oCyclicAtom.isAromatic();
	}

	@Override
	public String getSymbol() {
		return this.m_oCyclicAtom.getSymbol();
	}

	@Override
	public MAPStereo getStereo() {
		return this.m_oCyclicAtom.getStereo();
	}

	@Override
	public MAPAtomAbstract copy() {
		return null;
	}

	@Override
	public int getValence() {
		// Return -1
		return -1;
	}
}
