package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.graph.ModificationAnalyzer;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

/**
 * Class for stripping O-linked (Type-III) substituent from glycan
 * @author MasaakiMatsubara
 *
 */
public class WURCSVisitorStripTypeIIIModification2 implements WURCSVisitor {

	private LinkedList<WURCSEdge> m_aTargetEdges = new LinkedList<WURCSEdge>();
	private WURCSGraph m_oStrippedGraph;

	public WURCSGraph getStrippedGraph() {
		return this.m_oStrippedGraph;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {

	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Only for substituent
		if ( a_objWURCSEdge.getModification().isGlycosidic() ) return;

		try {
			if ( !this.isTypeIIIEdge(a_objWURCSEdge) ) return;
		} catch (WURCSFormatException e) {
			throw new WURCSVisitorException("Error in MAP import in class WURCSvisitorStropTypeIIIModification(visit)", e);
		}

		// Add target list
		this.m_aTargetEdges.addLast(a_objWURCSEdge);
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {

		try {
			this.m_oStrippedGraph = a_objGraph.copy();

			// Traverse and collect edges of O-linked (Type-III) substituent
			this.getTraverser(this).traverseGraph( this.m_oStrippedGraph );

			// Cutting edges for Type-III substituent
			LinkedList<Modification> t_aStrippingMods = new LinkedList<Modification>();
			for ( WURCSEdge t_oEdge : this.m_aTargetEdges ) {
				Modification t_oMod = t_oEdge.getModification();

				// Check edges which are divalent substituent
				if ( t_oMod.getEdges().size() > 1 ) {
					boolean t_bHasTypeII = false;
					for ( WURCSEdge t_oOtherEdge : t_oMod.getEdges() ) {
						if ( t_oEdge.equals(t_oOtherEdge) ) continue;
						if ( this.m_aTargetEdges.contains(t_oOtherEdge) ) continue;

						t_bHasTypeII = true;
					}
					// TODO: cut edge contained Type-II edge
					if ( t_bHasTypeII ) continue;
				}
				t_oEdge.getBackbone().removeEdge(t_oEdge);
				t_oMod.removeEdge(t_oEdge);

				// Collect naked Modification
				if ( !t_oMod.getEdges().isEmpty() ) continue;
				t_aStrippingMods.addLast(t_oMod);
			}

			// Remove Modifications from graph
			for ( Modification t_oNakedMod : t_aStrippingMods ) {
				this.m_oStrippedGraph.removeModification(t_oNakedMod);
			}
		} catch (WURCSException e) {
			throw new WURCSVisitorException( e.getErrorMessage() );
		}

	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aTargetEdges = new LinkedList<WURCSEdge>();
	}

	private boolean isTypeIIIEdge( WURCSEdge a_oEdge ) throws WURCSFormatException {
		if ( a_oEdge.getModification() == null ) return false;
/*
		int t_iMAPPos = 0;
		for ( LinkagePosition t_oLinkPos : a_objWURCSEdge.getLinkages() ) {
			t_iMAPPos = t_oLinkPos.getModificationPosition()
		}
*/
		int t_iMAPPos = a_oEdge.getLinkages().getFirst().getModificationPosition();
		ModificationAnalyzer t_oModAnal = new ModificationAnalyzer( a_oEdge.getModification() );
		return t_oModAnal.isTypeIIICarbon(t_iMAPPos);
	}
}
