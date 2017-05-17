package org.glycoinfo.WURCSFramework.wurcs.map;

/**
 * Class for MAPStar
 * @author MasaakiMatsubara
 *
 */
public class MAPStar extends MAPAtom {

	private int m_iStarIndex;
	private MAPConnection m_oConnection;

	public MAPStar() {
		super("C");
		this.m_iStarIndex = 0;
	}

	public void setStarIndex(int a_iNum) {
		this.m_iStarIndex = a_iNum;
	}

	public int getStarIndex() {
		return this.m_iStarIndex;
	}

	public void setConnection(MAPConnection a_oConn) {
		this.m_oConnection = a_oConn;
	}

	public MAPConnection getConnection() {
		return this.m_oConnection;
	}

	@Override
	public MAPAtomAbstract copy() {
		MAPStar t_oCopy = new MAPStar();
		if ( this.isAromatic() ) t_oCopy.setAromatic();
		t_oCopy.setStarIndex(this.m_iStarIndex);
		return t_oCopy;
	}

	@Override
	public int getValence() {
		// MAPStar has 1 valence basicly
		if ( this.m_oConnection == null )
			return 1;

		if ( this.m_oConnection.getBondType() == MAPBondType.AROMATIC )
			return -1;

		return this.m_oConnection.getBondType().getNumber();
	}
}
