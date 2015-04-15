package org.glycoinfo.WURCSFramework.util.visitor.graph;

import org.glycoinfo.WURCSFramework.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.graph.WURCSGraph;

/**
 * Abstract class for WURCSGlycan traverser
 * @author MasaakiMatsubara
 *
 */
public abstract class WURCSGraphTraverser {
	public static final int ENTER  = 0;
	public static final int LEAVE  = 1;
	public static final int RETURN = 2;

	protected WURCSVisitor m_objVisitor = null;
	protected int m_iState = 0;
	protected int m_iNode = 0;

	public WURCSGraphTraverser ( WURCSVisitor a_objVisitor ) throws WURCSVisitorException {
		if ( a_objVisitor == null )
			throw new WURCSVisitorException("Null visitor given to traverser");

		this.m_objVisitor = a_objVisitor;
	}

	public abstract void traverse( WURCSComponent a_objResidue    ) throws WURCSVisitorException;
//	public abstract void traverse( Backbone     a_objBackbone     ) throws WURCSVisitorException;
//	public abstract void traverse( Modification a_objModification ) throws WURCSVisitorException;
	public abstract void traverse( WURCSEdge    a_objEdge         ) throws WURCSVisitorException;

	public abstract void traverseGraph( WURCSGraph a_objGlycan ) throws WURCSVisitorException;

	public int getState() {
		return this.m_iState;
	}

	public int getNode() {
		return this.m_iNode;
	}
}
