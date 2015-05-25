package org.glycoinfo.WURCSFramework.util.graph.traverser;

import java.util.HashSet;

import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSEdgeComparator;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphTraverserConnectingGroup extends WURCSGraphTraverser {

	WURCSEdgeComparator m_oComp = new WURCSEdgeComparator();
	private HashSet<WURCSComponent> m_aSearchedNodes = new HashSet<WURCSComponent>();
	private HashSet<WURCSEdge>      m_aSearchedEdges = new HashSet<WURCSEdge>();

	public WURCSGraphTraverserConnectingGroup(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		super(a_objVisitor);
	}

	@Override
	public void traverse(WURCSComponent a_objResidue) throws WURCSVisitorException {
		if ( this.m_aSearchedNodes.contains(a_objResidue) ) return;

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objResidue.accept(this.m_objVisitor);

		this.m_aSearchedNodes.add(a_objResidue);

		// Traverse all edge
		for ( WURCSEdge t_oEdge : a_objResidue.getEdges() )
			this.traverse( t_oEdge );
	}

	@Override
	public void traverse(WURCSEdge a_objEdge) throws WURCSVisitorException {
		if ( this.m_aSearchedEdges.contains(a_objEdge) ) return;

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objEdge.accept(this.m_objVisitor);

		this.m_aSearchedEdges.add(a_objEdge);

		// Traverse both of Backbone and Modificaiton
		this.traverse( a_objEdge.getBackbone() );
		this.traverse( a_objEdge.getModification() );
	}

	@Override
	public void traverseGraph(WURCSGraph a_objGlycan) throws WURCSVisitorException {
		// Do nothing
	}
}
