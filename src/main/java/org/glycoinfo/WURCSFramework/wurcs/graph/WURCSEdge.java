package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitable;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorException;

/**
 * Class for edge between Backbone and Modification
 * @author MasaakiMatsubara
 *
 */
public class WURCSEdge implements WURCSVisitable {

	private Backbone m_objBackbone;
	private Modification m_objModification;
	private LinkedList<LinkagePosition> m_aLinkages = new LinkedList<LinkagePosition>();
	private boolean m_bIsReverse = false;

	public WURCSComponent getNextComponent() {
		if ( this.m_bIsReverse )
			return (WURCSComponent)this.m_objBackbone;
		return (WURCSComponent)this.m_objModification;
	}

	public void setBackbone(Backbone backbone) {
		this.m_objBackbone = backbone;
	}

	public Backbone getBackbone() {
		return this.m_objBackbone;
	}

	public void setModification(Modification mod) {
		this.m_objModification = mod;
	}

	public Modification getModification() {
		return this.m_objModification;
	}

	public boolean addLinkage( LinkagePosition link ) {
		if ( this.m_aLinkages.contains( link ) ) return false;
		return this.m_aLinkages.add( link );
	}

	public LinkedList<LinkagePosition> getLinkages() {
		return this.m_aLinkages;
	}

	public void reverse() {
		this.m_bIsReverse = true;
	}

	public void forward() {
		this.m_bIsReverse = false;
	}

	public boolean isReverse() {
		return this.m_bIsReverse;
	}

	public WURCSEdge copy() {
		WURCSEdge copy = new WURCSEdge();
		for ( LinkagePosition link : this.m_aLinkages ) {
			copy.addLinkage( link.copy() );
		}
		copy.m_bIsReverse = this.m_bIsReverse;
		return copy;
	}

	@Override
	public void accept(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		a_objVisitor.visit(this);
	}

}
