package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserConnectingGroup;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSVisitorGroupBackbones implements WURCSVisitor {

	private LinkedList<HashSet<Backbone>> m_aBackboneGroups = new LinkedList<HashSet<Backbone>>();

	private HashSet<Backbone>     m_aBackbones;
	private HashSet<Modification> m_aModifications;
	private HashSet<WURCSEdge>    m_aEdges;

	public LinkedList<HashSet<Backbone>> getBackboneGroups() {
		return this.m_aBackboneGroups;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		this.m_aBackbones.add(a_objBackbone);
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		this.m_aModifications.add(a_objModification);
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		this.m_aEdges.add(a_objWURCSEdge);
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		WURCSGraphTraverser t_objTraverser = this.getTraverser(this);
//		t_objTraverser.traverseGraph(a_objGraph);
		try {
			ArrayList<Backbone> t_aAllBackbones = a_objGraph.getBackbones();
			for ( Backbone t_oStart : t_aAllBackbones ) {
				// Check backbone has contained other backbone group
				boolean t_bHasContained = false;
				for ( HashSet<Backbone> t_aBackboneGroup : this.m_aBackboneGroups ) {
					if ( !t_aBackboneGroup.contains(t_oStart) ) continue;
					t_bHasContained = true;
					break;
				}
				if ( t_bHasContained ) continue;

				// Traverse from root backbone and collect connected backbones
				this.clear();
				t_objTraverser.traverse(t_oStart);

				// Add backbone group
				this.m_aBackboneGroups.addLast(this.m_aBackbones);
			}
		} catch (WURCSException e) {
			throw new WURCSVisitorException(e.getErrorMessage());
		}
	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserConnectingGroup(this);
	}

	@Override
	public void clear() {
		this.m_aBackbones     = new HashSet<Backbone>();
		this.m_aModifications = new HashSet<Modification>();
		this.m_aEdges         = new HashSet<WURCSEdge>();

	}

}
