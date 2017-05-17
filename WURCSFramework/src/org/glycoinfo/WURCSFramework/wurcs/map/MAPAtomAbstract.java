package org.glycoinfo.WURCSFramework.wurcs.map;

import java.util.LinkedList;

/**
 * Abstract class for some atoms (or atomic groups) in MAPGraph
 * @author MasaakiMatsubara
 *
 */
public abstract class MAPAtomAbstract {

	private boolean m_bIsAromatic;
	private MAPConnection m_oParentConn;
	private LinkedList<MAPConnection> m_aChildConns;

	public MAPAtomAbstract() {
		this.m_bIsAromatic = false;
		this.m_oParentConn = null;
		this.m_aChildConns = new LinkedList<MAPConnection>();
	}

	public void setAromatic() {
		this.m_bIsAromatic = true;
	}

	public boolean isAromatic() {
		return this.m_bIsAromatic;
	}

	public void setParentConnection( MAPConnection a_oConn ) {
		this.m_oParentConn = a_oConn;
	}

	public MAPConnection getParentConnection() {
		return this.m_oParentConn;
	}

	public void addChildConnection(MAPConnection a_oConn) {
		this.m_aChildConns.addLast(a_oConn);
	}

	public LinkedList<MAPConnection> getChildConnections() {
		return this.m_aChildConns;
	}

	public LinkedList<MAPConnection> getConnections() {
		LinkedList<MAPConnection> t_aConnections = new LinkedList<MAPConnection>();
		if ( this.m_oParentConn != null )
			t_aConnections.add( this.m_oParentConn );
		t_aConnections.addAll( this.m_aChildConns );
		return t_aConnections;
	}

	public abstract String getSymbol();
	public abstract MAPStereo getStereo();
	public abstract int getValence();
	public abstract MAPAtomAbstract copy();
}
