package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.graph.ModificationAnalyzer;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSVisitorSeparateWURCSGraphByAglycone implements WURCSVisitor {

	private LinkedList<Modification> m_aAglycones;
	private HashMap<Modification, String> m_mapAglyconeToAbbrMAP;
	private LinkedList<String> m_aUniqueMAPAglycone;
//	private HashMap<Modification, LinkedList<WURCSEdge>> m_mapAglyconeToEdges;
	private HashMap<WURCSGraph, LinkedList<Modification>> m_mapGraphToAglycones;
	private LinkedList<WURCSGraph> m_aSeparatedGraphs;
	private LinkedList<WURCSGraph> m_aSeparatedGraphsWithOneAtom;

	public LinkedList<Modification> getAglycones() {
		return this.m_aAglycones;
	}

	public HashMap<Modification, String> getMapAglyconeToAbbr() {
		return this.m_mapAglyconeToAbbrMAP;
	}

	public LinkedList<WURCSGraph> getSeparatedGraphs() {
		return this.m_aSeparatedGraphs;
	}

	public LinkedList<WURCSGraph> getSeparatedGraphsWithOneAtom() {
		return this.m_aSeparatedGraphsWithOneAtom;
	}

	public HashMap<WURCSGraph, LinkedList<Modification>> getMapSeparatedGraphToAglycones() {
		return this.m_mapGraphToAglycones;
	}

	@Override
	public void clear() {
		this.m_aAglycones = new LinkedList<Modification>();
		this.m_mapAglyconeToAbbrMAP = new HashMap<Modification, String>();
		this.m_aUniqueMAPAglycone = new LinkedList<String>();
		this.m_aSeparatedGraphs = new LinkedList<WURCSGraph>();
		this.m_aSeparatedGraphsWithOneAtom = new LinkedList<WURCSGraph>() ;
		this.m_mapGraphToAglycones = new HashMap<WURCSGraph, LinkedList<Modification>>();
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		for ( WURCSEdge t_oEdge : a_objBackbone.getEdges() ) {
			if ( ! t_oEdge.getModification().isAglycone() ) continue;
			if ( t_oEdge.getModification().isGlycosidic() ) continue;
			Modification t_oModif = t_oEdge.getModification();
			if ( t_oModif.getMAPCode().equals("") ) continue;
			if ( t_oModif.getMAPCode().equals("*O") ) continue;
			// Add aglycone
			this.addAglyconeInformation(t_oModif);
		}
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		if ( !a_objModification.isAglycone() ) return;
		if ( a_objModification.getMAPCode().equals("") ) return;
		if ( a_objModification.getMAPCode().equals("*O*") ) return;
		if ( a_objModification.getMAPCode().length() < 4 ) return;
		// Add aglycone
		this.addAglyconeInformation(a_objModification);
	}

	private void addAglyconeInformation(Modification a_oModif) {
		if ( this.m_aAglycones.contains(a_oModif) ) return;
		this.m_aAglycones.add(a_oModif);
		// Set aglycone unieque ID
		String t_strMAP = a_oModif.getMAPCode();
		if ( !this.m_aUniqueMAPAglycone.contains( t_strMAP ) )
			this.m_aUniqueMAPAglycone.addLast( t_strMAP );
		this.m_mapAglyconeToAbbrMAP.put( a_oModif, "@"+(this.m_aUniqueMAPAglycone.indexOf(t_strMAP)+1) );
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Do nothing
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.clear();

		try {
			WURCSGraph t_oCopyGraph = a_objGraph.copy();

			// Collect aglycones
			WURCSVisitorCollectSequence t_oSeq = new WURCSVisitorCollectSequence();
			t_oSeq.start(t_oCopyGraph);
			for ( WURCSComponent t_oNode : t_oSeq.getNodes() )
				t_oNode.accept(this);

			for ( Modification t_oMod : t_oSeq.getLeafModifications() ) {
				t_oMod.accept(this);
			}

			for ( Modification t_oMod : t_oSeq.getRepeatModifications() )
				t_oMod.accept(this);

			// Do nothing if no aglycone
			if ( this.m_aAglycones.isEmpty() ) return;

			// All aglycone string make to be "@" if unique aglycone is only one
			if ( this.m_aUniqueMAPAglycone.size() == 1 )
				for ( Modification t_oAglycone : this.m_mapAglyconeToAbbrMAP.keySet() )
					this.m_mapAglyconeToAbbrMAP.put(t_oAglycone, "@");

			HashMap<String, String> t_mapAbbrMAPToOneAtom =  new HashMap<String, String>();
			// Separate edges around aglycone
			HashMap<Modification, Modification> t_mapAbbrModifToAglycone = new HashMap<Modification, Modification>();
			for ( Modification t_oAglycone : this.m_aAglycones ) {
//				LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();

				ModificationAnalyzer t_oModAnal = new ModificationAnalyzer( t_oAglycone );

				// Reconnect new aglycones
//				this.m_mapAglyconeToEdges.put(t_oAglycone, t_aEdges);
				for ( WURCSEdge t_oEdge : t_oAglycone.getEdges() ) {
					Backbone t_oBackbone = t_oEdge.getBackbone();
					WURCSEdge t_oNewEdge = t_oEdge.copy();
					Modification t_oAbbrModif = new Modification( this.m_mapAglyconeToAbbrMAP.get(t_oAglycone) );
					t_oCopyGraph.addResidues(t_oEdge.getBackbone(), t_oNewEdge, t_oAbbrModif);
					t_mapAbbrModifToAglycone.put(t_oAbbrModif, t_oAglycone);
					t_oBackbone.removeEdge(t_oEdge);

					// Make one atom aglycone MAP
					int t_iMAPPos = t_oEdge.getLinkages().getFirst().getModificationPosition();
					String t_strOneAtomMAP = "*"+t_oModAnal.getAtomConnectedBackboneCarbon(t_iMAPPos);
					// Map abbr modif to one atom MAP
					t_mapAbbrMAPToOneAtom.put(t_oAbbrModif.getMAPCode(), t_strOneAtomMAP);
				}

				// Remove aglycone from copied graph
				t_oAglycone.removeAllEdges();
				t_oCopyGraph.removeModification(t_oAglycone);
			}

			// Check connecting backbones
			WURCSVisitorCollectConnectingBackboneGroups t_oGroup = new WURCSVisitorCollectConnectingBackboneGroups();
			t_oGroup.start(t_oCopyGraph);
			LinkedList<HashSet<Backbone>> t_aBackboneList = t_oGroup.getBackboneGroups();
			if ( t_aBackboneList.size() == 1 ) {
				this.m_aSeparatedGraphs.add(t_oCopyGraph);
				this.m_mapGraphToAglycones.put(t_oCopyGraph, this.m_aAglycones);
				this.m_aSeparatedGraphsWithOneAtom.add( this.makeOneAtomGraph(t_oCopyGraph, t_mapAbbrMAPToOneAtom) );
				return;
			}

			// Make separated graphs
			for ( HashSet<Backbone> t_aBackbones : t_oGroup.getBackboneGroups() ) {
				WURCSGraph t_oNewGraph = new WURCSGraph();
				this.m_mapGraphToAglycones.put(t_oNewGraph, new LinkedList<Modification>());

				// Copy nodes
				HashMap<Modification, Modification> t_mapOrigToCopyMod = new HashMap<Modification, Modification>();
				for ( Backbone t_oBackbone : t_aBackbones ) {
					Backbone t_oCopyBackbone = t_oBackbone.copy();
					for ( WURCSEdge t_oEdge : t_oBackbone.getEdges() ) {
						Modification t_oModification = t_oEdge.getModification();

						// Map original modification to copy
						if ( !t_mapOrigToCopyMod.containsKey(t_oModification) )
							t_mapOrigToCopyMod.put(t_oModification, t_oModification.copy());

						Modification t_oCopyModification = t_mapOrigToCopyMod.get(t_oModification);

						// Map graph and aglycone
						if ( t_oModification.isAglycone() && t_oModification.getMAPCode().contains("@") ) {
							Modification t_oAglycone = t_mapAbbrModifToAglycone.get(t_oModification);
							this.m_mapGraphToAglycones.get(t_oNewGraph).addLast(t_oAglycone);
						}

						WURCSEdge t_oCopyEdge = t_oEdge.copy();
						t_oCopyEdge.forward();
						t_oNewGraph.addResidues(t_oCopyBackbone, t_oCopyEdge, t_oCopyModification);
					}
				}
				this.m_aSeparatedGraphs.add(t_oNewGraph);

				this.m_aSeparatedGraphsWithOneAtom.add( this.makeOneAtomGraph(t_oNewGraph, t_mapAbbrMAPToOneAtom) );
			}
		} catch (WURCSException e) {
			throw new WURCSVisitorException( e.getErrorMessage() );
		}

	}

	private WURCSGraph makeOneAtomGraph(WURCSGraph a_oGraph, HashMap<String, String> a_mapAbbrToOneAtom) throws WURCSException {
		// Make separated graphs with one atom
		WURCSGraph t_oCopySepGraph = a_oGraph.copy();
		// Collect abbr modifs
		LinkedList<Modification> t_aAbbrModifs = new LinkedList<Modification>();
		for ( Modification t_oModif : t_oCopySepGraph.getModifications() ) {
			if ( !a_mapAbbrToOneAtom.containsKey( t_oModif.getMAPCode() ) ) continue;
			t_aAbbrModifs.add(t_oModif);
		}
		// Reconnect one atom modification and remove abbr modifs
		for ( Modification t_oModif : t_aAbbrModifs ) {
			Modification t_oOneAtomModif = new Modification( a_mapAbbrToOneAtom.get(t_oModif.getMAPCode()) );

			// Reconnect one atom modification
			for ( WURCSEdge t_oEdge : t_oModif.getEdges() ) {
				Backbone t_oBackbone = t_oEdge.getBackbone();
				WURCSEdge t_oNewEdge = new WURCSEdge();
				for ( LinkagePosition t_oLinkPos : t_oEdge.getLinkages() ) {
					LinkagePosition t_oNewLink = new LinkagePosition(
							t_oLinkPos.getBackbonePosition(),
							t_oLinkPos.getDirection(),
							t_oLinkPos.canOmitDirection(), 0, true);
					t_oNewEdge.addLinkage(t_oNewLink);
				}
				t_oCopySepGraph.addResidues(t_oBackbone, t_oNewEdge, t_oOneAtomModif);
				t_oBackbone.removeEdge(t_oEdge);
			}
			t_oCopySepGraph.removeModification(t_oModif);
		}
		return t_oCopySepGraph;
	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}
}
