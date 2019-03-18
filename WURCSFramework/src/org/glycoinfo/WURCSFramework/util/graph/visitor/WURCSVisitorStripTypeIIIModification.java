package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.map.analysis.MAPGraphAnalyzer;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

/**
 * Class for stripping O-linked (Type-III) substituent from glycan
 * @author MasaakiMatsubara
 *
 */
public class WURCSVisitorStripTypeIIIModification implements WURCSVisitor {

	private LinkedList<Modification> m_aStrippingMods = new LinkedList<Modification>();
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
		if ( this.m_aStrippingMods.contains(a_objModification) ) return;
		// Only for substituent
		if ( a_objModification.isGlycosidic() && !this.isAlternativeSubstituent(a_objModification) )
			return;

		String t_strMAP = a_objModification.getMAPCode();
		if ( t_strMAP.equals("") ) {
			// For hydroxyl groups "*O"
			t_strMAP = "*O";
			// For ring ether
			if ( a_objModification.getEdges().size() == 2 )
				t_strMAP = "*O*";
		}

		// MAP analysis
		try {
			MAPGraph t_oMAP = (new MAPGraphImporter()).parseMAP(t_strMAP);
			MAPGraphAnalyzer t_oMAPAnal = new MAPGraphAnalyzer(t_oMAP);
			if ( !t_oMAPAnal.isTypeIII() ) return;
		} catch (WURCSFormatException e) {
			throw new WURCSVisitorException("Error in MAP import in class WURCSVisitorStripTypeIIIModification(visit)", e);
		}

		this.m_aStrippingMods.addLast(a_objModification);
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.clear();

		try {
			this.m_oStrippedGraph = a_objGraph.copy();

			// Traverse and collect O-linked (Type-III) substituent
			this.getTraverser(this).traverseGraph( this.m_oStrippedGraph );

			// Strip Type-III substituent
			for ( Modification t_oStrippingMod : this.m_aStrippingMods ) {
				System.err.println( "[info] Removed MAP: "+t_oStrippingMod.getMAPCode() + "WURCSVisitorStripTypeIIIModification(start(WURCSGraph))");
				LinkedList<WURCSEdge> t_aRemoveEdges = new LinkedList<WURCSEdge>();
				for ( WURCSEdge t_oEdge : t_oStrippingMod.getEdges() ) {
					// Remove edge from Backbone and Modification
					Backbone t_oBackbone = t_oEdge.getBackbone();
					t_oBackbone.removeEdge(t_oEdge);
					t_aRemoveEdges.add(t_oEdge);
				}
				for ( WURCSEdge t_oRemoveEdge : t_aRemoveEdges )
					t_oStrippingMod.removeEdge( t_oRemoveEdge );

				// Remove edges from read-in and read-out edges in alternative mod
				if ( t_oStrippingMod instanceof ModificationAlternative ) {
					ModificationAlternative t_oAlt = (ModificationAlternative)t_oStrippingMod;

					for ( WURCSEdge t_oRemoveEdge : t_aRemoveEdges ) {
						if ( t_oAlt.getLeadInEdges().contains( t_oRemoveEdge ) )
							t_oAlt.getLeadInEdges().remove( t_oRemoveEdge );
						if ( t_oAlt.getLeadOutEdges().contains( t_oRemoveEdge ) )
							t_oAlt.getLeadOutEdges().remove( t_oRemoveEdge );
					}
				}
				// Remove Modification
				this.m_oStrippedGraph.removeModification(t_oStrippingMod);
			}

		} catch (WURCSException e) {
			throw new WURCSVisitorException( e.getErrorMessage(), e );
		}

	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aStrippingMods = new LinkedList<Modification>();
	}

	private boolean isAlternativeSubstituent(Modification a_objModification) {
		if ( !( a_objModification instanceof ModificationAlternative ) )
			return false;
		if ( ((ModificationAlternative)a_objModification).getLeadOutEdges().isEmpty() )
			return true;
		return false;
	}
}
