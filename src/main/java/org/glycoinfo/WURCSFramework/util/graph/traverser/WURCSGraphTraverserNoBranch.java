package org.glycoinfo.WURCSFramework.util.graph.traverser;

import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphTraverserNoBranch extends WURCSGraphTraverser {

	private HashSet<WURCSEdge> m_aSearchedEdges = new HashSet<WURCSEdge>();

	public WURCSGraphTraverserNoBranch(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		super(a_objVisitor);
	}

	@Override
	public void traverse(WURCSComponent a_objResidue) throws WURCSVisitorException {

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objResidue.accept(this.m_objVisitor);

		// Stop if there are branch edges
		LinkedList<WURCSEdge> t_aChildEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : a_objResidue.getChildEdges() ) {
			// Ignore leaf modification
			WURCSComponent t_oNext = t_oEdge.getNextComponent();
			if ( t_oNext instanceof Modification && t_oNext.isLeaf() ) continue;
			t_aChildEdges.add(t_oEdge);
		}
		if ( t_aChildEdges.size() != 1 ) return;

		this.traverse(t_aChildEdges.getFirst());
	}

	@Override
	public void traverse(WURCSEdge a_objEdge) throws WURCSVisitorException {
		if ( this.m_aSearchedEdges.contains(a_objEdge) )
			throw new WURCSVisitorException("Illegal traverse.");

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objEdge.accept(this.m_objVisitor);

		this.m_aSearchedEdges.add(a_objEdge);

		this.traverse( a_objEdge.getNextComponent() );

	}

	@Override
	public void traverseGraph(WURCSGraph a_objGlycan) throws WURCSVisitorException {
		// Do nothing
	}

}
