package org.glycoinfo.WURCSFramework.util.graph.traverser;

import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;

public class WURCSGraphTraverserTreeStoppable extends WURCSGraphTraverserTree {

	private boolean m_bStop = false;

	public WURCSGraphTraverserTreeStoppable(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		super(a_objVisitor);
	}

	@Override
	public void traverse(WURCSComponent a_objResidue) throws WURCSVisitorException {

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objResidue.accept(this.m_objVisitor);

		LinkedList<WURCSEdge> t_aEdges = a_objResidue.getChildEdges();
		Collections.sort(t_aEdges, this.m_oComp);
		for ( WURCSEdge t_oEdge : t_aEdges ) {

			this.m_bStop = false;

			this.traverse( t_oEdge );
			// callback after return
//			this.m_iState = WURCSGlycanTraverser.RETURN;
//			a_objBackbone.accept(this.m_objVisitor);
		}
		// callback after subtree
//		this.m_iState = WURCSGlycanTraverser.LEAVE;
//		a_objBackbone.accept(this.m_objVisitor);
	}

	@Override
	public void traverse(WURCSEdge a_objEdge) throws WURCSVisitorException {
		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objEdge.accept(this.m_objVisitor);

		if ( this.m_aSearchedEdges.contains(a_objEdge) )
			throw new WURCSVisitorException("Illegal traverse.");

		this.m_aSearchedEdges.add(a_objEdge);

		if ( this.m_bStop ) return;

		this.traverse( a_objEdge.getNextComponent() );

		// callback of the function after subtree
//		this.m_iState = WURCSGlycanTraverser.LEAVE;
//		a_objEdge.accept(this.m_objVisitor);
	}

	/**
	 * Set stop traverse flag
	 */
	public void stop() {
		this.m_bStop = true;
	}
}
