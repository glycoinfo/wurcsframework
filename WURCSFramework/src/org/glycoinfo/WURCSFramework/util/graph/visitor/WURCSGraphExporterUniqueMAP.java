package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphExporterUniqueMAP implements WURCSVisitor {

	private LinkedList<String> m_aMAPStrings;

	public LinkedList<String> getMAPStrings() {
		return this.m_aMAPStrings;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		if ( a_objModification.getMAPCode().equals("") ) return;
		if ( m_aMAPStrings.contains( a_objModification.getMAPCode() ) ) return;

		this.m_aMAPStrings.add( a_objModification.getMAPCode() );
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.clear();

		WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
		t_oSeq.start(a_objGraph);
		for ( WURCSComponent t_oNode : t_oSeq.getNodes() )
			t_oNode.accept(this);

		for ( Modification t_oMod : t_oSeq.getLeafModifications() ) {
			t_oMod.accept(this);
		}

		for ( Modification t_oMod : t_oSeq.getRepeatModifications() )
			t_oMod.accept(this);

	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aMAPStrings = new LinkedList<String>();
	}

}
