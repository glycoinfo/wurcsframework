package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Class for MAPConnection which indicate direction of bond to a MAPAtomAbstract
 * @author MasaakiMatsubara
 *
 */
public class MAPConnection {

	private MAPAtomAbstract m_oAtom;
	private MAPBondType m_enumBondType;
	private MAPStereo m_enumStereo;
	private MAPConnection m_oReverse;

	/**
	 * Constructor of MAPConnection
	 * @param a_oAtom Connected MAPAtomAbstract
	 */
	public MAPConnection( MAPAtomAbstract a_oAtom ) {
		this.m_oAtom = a_oAtom;
		this.m_enumBondType = MAPBondType.SINGLE;
		this.m_enumStereo = null;
	}

	public MAPAtomAbstract getAtom() {
		return this.m_oAtom;
	}

	public void setBondType( MAPBondType a_enumBondType ) {
		this.m_enumBondType = a_enumBondType;
	}

	public MAPBondType getBondType() {
		return this.m_enumBondType;
	}

	public void setStereo( MAPStereo a_enumStereo ) {
		this.m_enumStereo = a_enumStereo;
	}

	public MAPStereo getStereo() {
		return this.m_enumStereo;
	}

	public void setReverse( MAPConnection a_oReverse ) {
		this.m_oReverse = a_oReverse;
	}

	public MAPConnection getReverse() {
		return this.m_oReverse;
	}

	public MAPConnection copy( MAPAtomAbstract a_oAtom ) {
		MAPConnection t_oCopy = new MAPConnection(a_oAtom);
		t_oCopy.setBondType(this.m_enumBondType);
		t_oCopy.setStereo(this.m_enumStereo);
		return t_oCopy;
	}
}
