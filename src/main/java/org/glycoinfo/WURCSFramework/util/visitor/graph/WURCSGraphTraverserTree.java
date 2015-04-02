package org.glycoinfo.WURCSFramework.util.visitor.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.comparator.graph.BackboneComparator;
import org.glycoinfo.WURCSFramework.util.comparator.graph.WURCSEdgeComparator;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSException;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphTraverserTree extends WURCSGraphTraverser {

	WURCSEdgeComparator m_oComp = new WURCSEdgeComparator();
	private HashSet<WURCSEdge> m_aSearchedEdges = new HashSet<WURCSEdge>();

	public WURCSGraphTraverserTree(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		super(a_objVisitor);
	}

	@Override
	public void traverse(WURCSComponent a_objResidue) throws WURCSVisitorException {

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objResidue.accept(this.m_objVisitor);

		LinkedList<WURCSEdge> t_aEdges = a_objResidue.getEdges();
		Collections.sort(t_aEdges, this.m_oComp);
		for ( WURCSEdge t_oEdge : t_aEdges ) {

			if ( a_objResidue.equals( t_oEdge.getNextComponent() ) ) continue;

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
		if ( this.m_aSearchedEdges.contains(a_objEdge) )
			throw new WURCSVisitorException("Illegal traverse.");

		// callback of the function before subtree
		this.m_iState = WURCSGraphTraverser.ENTER;
		a_objEdge.accept(this.m_objVisitor);

		this.m_aSearchedEdges.add(a_objEdge);

		this.traverse( a_objEdge.getNextComponent() );

		// callback of the function after subtree
//		this.m_iState = WURCSGlycanTraverser.LEAVE;
//		a_objEdge.accept(this.m_objVisitor);
	}

	@Override
	public void traverseGraph(WURCSGraph a_objGlycan) throws WURCSVisitorException {
		ArrayList<Backbone> t_aRoot;
		try {
			// get root nodes of forest of graphs
			t_aRoot = a_objGlycan.getRootBackbones();
			// Priorize according to WURCSGlycan all isolated subgraphs and process consecutivly
			BackboneComparator t_oBComp = new BackboneComparator();
			Collections.sort(t_aRoot,t_oBComp);

			Iterator<Backbone> t_objIterator = t_aRoot.iterator();
			while ( t_objIterator.hasNext() )
				this.traverse(t_objIterator.next());
		}
		catch (WURCSException e) {
			throw new WURCSVisitorException(e.getMessage());
		}

	}

}
