package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitable;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;

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

	public boolean isAnomeric() {
		int t_iAnomPos = this.getBackbone().getAnomericPosition();
		if ( this.getLinkages().size() > 2 ) return false;
		for ( LinkagePosition t_oLiP : this.getLinkages() ) {
			if ( t_oLiP.getBackbonePosition() == t_iAnomPos ) return true;
		}
		return false;
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

	/**
	 * For debug
	 * @return String of edge information
	 */
	public String printEdge() {
		if ( this.getBackbone() == null || this.getModification() == null )
			return "";
		String t_strSkeletonCode = this.getBackbone().getSkeletonCode();
		String t_strMAP = this.getModification().getMAPCode();
		if ( this.getModification() instanceof InterfaceRepeat )
			t_strMAP += "~"+((InterfaceRepeat)this.getModification()).getMinRepeatCount();
		String t_strEdge = "";
		for ( LinkagePosition t_oLiP : this.getLinkages() ) {
			if ( !t_strEdge.equals("") ) t_strEdge += "|";
			t_strEdge += t_oLiP.getBackbonePosition();
			if ( t_oLiP.getDirection() != DirectionDescriptor.L )
				t_strEdge += t_oLiP.getDirection();
			if ( t_oLiP.getModificationPosition() != 0 )
				t_strEdge += t_oLiP.getModificationPosition();
		}
		return t_strSkeletonCode+"_"+t_strEdge+t_strMAP;
	}

}
