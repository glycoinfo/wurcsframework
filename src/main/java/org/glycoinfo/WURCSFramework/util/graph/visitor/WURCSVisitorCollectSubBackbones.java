package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.HashSet;

import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSVisitorCollectSubBackbones implements WURCSVisitor {

	private HashSet<Backbone> m_aBackbones = new HashSet<Backbone>();

	public HashSet<Backbone> getBackbones() {
		return this.m_aBackbones;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		this.m_aBackbones.add(a_objBackbone);
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Do nothing
	}

	public void start(Backbone a_objBackbone) throws WURCSVisitorException {
		this.clear();
		WURCSGraphTraverser t_oTraverser = this.getTraverser(this);
		t_oTraverser.traverse(a_objBackbone);
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aBackbones.clear();
	}

}
