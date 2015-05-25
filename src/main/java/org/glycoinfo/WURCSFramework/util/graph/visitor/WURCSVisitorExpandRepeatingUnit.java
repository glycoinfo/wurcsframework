package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSGraphToArray;
import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSEdgeComparator;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTreeStoppable;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSException;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSVisitorExpandRepeatingUnit implements WURCSVisitor {

	private WURCSGraph m_oGraph;
	private LinkedList<ModificationRepeat> m_aModificationsOfRepeat = new LinkedList<ModificationRepeat>();
	private LinkedList<Backbone> m_aRepeatBackbones = new LinkedList<Backbone>();
	private LinkedList<Modification> m_aRepeatModifications = new LinkedList<Modification>();
	private WURCSEdge m_oUnitStartEdge = null;
	private WURCSEdge m_oUnitEndEdge   = null;
	private WURCSEdge m_oRepeatEdgeStartSide = null;
	private WURCSEdge m_oRepeatEdgeEndSide   = null;

	private WURCSGraphTraverserTreeStoppable m_oTraverser;

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		this.m_aRepeatBackbones.addLast(a_objBackbone);
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// Collect modifications in the repeating unit
		if ( !this.m_aRepeatModifications.contains(a_objModification) )
			this.m_aRepeatModifications.addLast(a_objModification);

		// Collect nested repeat modifications
		if (!( a_objModification instanceof ModificationRepeat ) ) return;
		if ( this.m_aModificationsOfRepeat.contains(a_objModification) )
			return;

		this.m_aModificationsOfRepeat.addLast( (ModificationRepeat)a_objModification );
	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// XXX remove print
//		System.err.println(this.m_aRepeatBackbones.indexOf(a_objWURCSEdge.getBackbone())+":"+a_objWURCSEdge.printEdge());
		if ( !a_objWURCSEdge.equals(this.m_oUnitEndEdge) &&
			 !a_objWURCSEdge.equals(this.m_oUnitStartEdge) &&
			 !a_objWURCSEdge.equals(this.m_oRepeatEdgeEndSide) &&
			 !a_objWURCSEdge.equals(this.m_oRepeatEdgeStartSide) ) return;
//		System.err.println("stop");
		this.m_oTraverser.stop();
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.m_oGraph = a_objGraph;

		LinkedList<ModificationRepeat> t_aIgnoreModReps = new LinkedList<ModificationRepeat>();
		LinkedList<ModificationRepeat> t_aModReps = new LinkedList<ModificationRepeat>();
		for ( Modification t_oMod : this.m_oGraph.getModifications() ) {
			if ( ! (t_oMod instanceof ModificationRepeat )) continue;
			ModificationRepeat t_oModRep = (ModificationRepeat)t_oMod;

			if ( t_oModRep.getMinRepeatCount() == -1 || t_oModRep.getMaxRepeatCount() == -1 ) {
				System.err.println("Can't expand repeating unit with unknown repeat count.");
				t_aIgnoreModReps.add(t_oModRep);
				continue;
			}
			if ( t_oModRep.getMinRepeatCount() != t_oModRep.getMaxRepeatCount() ) {
				System.err.println("Can't expand repeating unit with ranged repeat count.");
				t_aIgnoreModReps.add(t_oModRep);
				continue;
			}
			t_aModReps.addLast( t_oModRep );
		}

		if ( t_aModReps.isEmpty() ) return;

		System.err.println("Repeating unit expansion>>>");
		// Check nested repeating structure
		LinkedList<ModificationRepeat> t_aSortedModReps = new LinkedList<ModificationRepeat>();
//		this.m_bCheckNested = true;
		while ( true ) {
			this.clear();
			if ( t_aModReps.isEmpty() ) break;

			ModificationRepeat t_oModRep = t_aModReps.removeFirst();
			this.traverseRepeat(t_oModRep);

			if ( this.m_aModificationsOfRepeat.contains(t_oModRep) )
				throw new WURCSVisitorException("Illegal composition of repeating unit is found.");

			// Add sorted list if no nested repeating unit in the repeating unit
			boolean t_bCheckNested = false;
			for ( ModificationRepeat t_oNestedModRep : this.m_aModificationsOfRepeat ) {
				if ( t_aIgnoreModReps.contains(t_oNestedModRep) ) continue;

				// Ignore repeating unit linked external backbone
				if ( this.checkExternalLinkage(t_oNestedModRep) ) continue;

				if ( t_aSortedModReps.contains(t_oNestedModRep) ) continue;
				t_bCheckNested = true;
			}
			if ( !t_bCheckNested ) {
				t_aSortedModReps.addLast(t_oModRep);
				continue;
			}
			t_aModReps.addLast(t_oModRep);
		}

		// Development repeating units
//		this.m_bCheckNested = false;
		try {
			for ( ModificationRepeat t_oModRep : t_aSortedModReps )
				this.expandRepeatingUnits(t_oModRep);

		} catch (WURCSException e) {
			throw new WURCSVisitorException(e.getErrorMessage());
		}

	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTreeStoppable(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aModificationsOfRepeat = new LinkedList<ModificationRepeat>();
		this.m_aRepeatBackbones       = new LinkedList<Backbone>();

		this.m_oUnitStartEdge = null;
		this.m_oUnitEndEdge   = null;
		this.m_oRepeatEdgeStartSide = null;
		this.m_oRepeatEdgeEndSide   = null;
	}

	/**
	 * Expand repeating unit
	 * @param a_oModRep
	 * @throws WURCSException
	 */
	private void expandRepeatingUnits(ModificationRepeat a_oModRep) throws WURCSException {
		this.clear();

		// Collect repeating units
		this.traverseRepeat(a_oModRep);
/*
		WURCSGraph t_oGraph = new WURCSGraph();
		// Copy repeating units
		HashMap<Backbone, Backbone>         t_mapOrig2CopyB = new HashMap<Backbone, Backbone>();
		HashMap<Modification, Modification> t_mapOrig2CopyM = new HashMap<Modification, Modification>();
		for ( Backbone t_oBackOrig : this.m_aRepeatBackbones ) {
			Backbone t_oBackCopy = t_oBackOrig.copy();
			t_mapOrig2CopyB.put(t_oBackOrig, t_oBackCopy);
			for ( WURCSEdge t_oEdgeOrig : t_oBackOrig.getEdges() ) {
				Modification t_oModifOrig = t_oEdgeOrig.getModification();

				if ( t_oModifOrig.equals(a_oModRep) ) continue;
//				if ( !this.m_aRepeatModifications.contains(t_oModifOrig) ) continue;
				// Do not copy start and end edge
				if ( t_oEdgeOrig.equals(this.m_oUnitStartEdge) ) continue;
				if ( t_oEdgeOrig.equals(this.m_oUnitEndEdge) ) continue;

				// Do not copy external repeating linkage
				if ( t_oModifOrig instanceof ModificationRepeat &&
					 this.checkExternalLinkage( (ModificationRepeat)t_oModifOrig ) ) continue;

				// Copy modification
				if ( !t_mapOrig2CopyM.containsKey(t_oModifOrig) )
					t_mapOrig2CopyM.put(t_oModifOrig, t_oModifOrig.copy());
				Modification t_copyModif = t_mapOrig2CopyM.get(t_oModifOrig);

				// Connect copied backbone and modification
				t_oGraph.addResidues( t_oBackCopy, t_oEdgeOrig.copy(), t_copyModif );
			}
		}
*/
		Backbone t_oStartRepBOrig = this.m_oRepeatEdgeStartSide.getBackbone();
		Backbone t_oEndRepBOrig   = this.m_oRepeatEdgeEndSide.getBackbone();
//		Backbone t_oStartRepB = t_mapOrig2CopyB.get(t_oStartRepBOrig);
//		Backbone t_oEndRepB = t_mapOrig2CopyB.get(t_oEndRepBOrig);
		WURCSEdge t_oUnitEndEdge = this.m_oUnitEndEdge;

		while ( a_oModRep.getMinRepeatCount() > 1 ) {

			// Copy repeating units
			HashMap<Backbone, Backbone>         t_mapOrig2CopyB = new HashMap<Backbone, Backbone>();
			HashMap<Modification, Modification> t_mapOrig2CopyM = new HashMap<Modification, Modification>();
//			t_mapOrig2CopyB = new HashMap<Backbone, Backbone>();
//			t_mapOrig2CopyM = new HashMap<Modification, Modification>();
//			WURCSGraph t_oCopyGraph = t_oGraph.copy(t_mapOrig2CopyB, t_mapOrig2CopyM);

			for ( Backbone t_oBackOrig : this.m_aRepeatBackbones ) {
				Backbone t_oBackCopy = t_oBackOrig.copy();
				t_mapOrig2CopyB.put(t_oBackOrig, t_oBackCopy);
				for ( WURCSEdge t_oEdgeOrig : t_oBackOrig.getEdges() ) {
					Modification t_oModifOrig = t_oEdgeOrig.getModification();

					// Do not copy external modifications
					if ( t_oModifOrig.equals(a_oModRep) ) continue;
					if ( !this.m_aRepeatModifications.contains(t_oModifOrig) ) continue;
//					if ( t_oEdgeOrig.equals(this.m_oUnitStartEdge) ) continue;
//					if ( t_oEdgeOrig.equals(this.m_oUnitEndEdge) ) continue;

					// Do not copy external repeating linkage
					if ( t_oModifOrig instanceof ModificationRepeat &&
						 this.checkExternalLinkage( (ModificationRepeat)t_oModifOrig ) ) continue;

					// Copy modification
					if ( !t_mapOrig2CopyM.containsKey(t_oModifOrig) )
						t_mapOrig2CopyM.put(t_oModifOrig, t_oModifOrig.copy());
					Modification t_copyModif = t_mapOrig2CopyM.get(t_oModifOrig);

					// Connect copied backbone and modification
					this.m_oGraph.addResidues( t_oBackCopy, t_oEdgeOrig.copy(), t_copyModif );
				}
			}

			Backbone t_oEndRepBOld = t_oUnitEndEdge.getBackbone();
			Backbone t_oEndRepB = t_mapOrig2CopyB.get(t_oEndRepBOrig);
			if ( t_oUnitEndEdge != null ) {
				// Reconnect edge from end backbone to copied backbone
				t_oEndRepBOld.removeEdge(t_oUnitEndEdge);
				t_oUnitEndEdge.setBackbone(t_oEndRepB);
				t_oEndRepB.addEdge(t_oUnitEndEdge);
			}

			// Make and connect new edges between copied start backbone and end backbone from repeating linkage
			Backbone t_oStartRepB = t_mapOrig2CopyB.get(t_oStartRepBOrig);

			WURCSEdge t_oStartEdge = this.m_oRepeatEdgeStartSide.copy();
			t_oStartEdge.reverse();
			WURCSEdge t_oEndEdge   = this.m_oRepeatEdgeEndSide.copy();
			Modification t_oNewMod = new Modification(a_oModRep.getMAPCode());
			this.m_oGraph.addResidues(t_oEndRepBOld, t_oEndEdge, t_oNewMod);
			this.m_oGraph.addResidues(t_oStartRepB, t_oStartEdge, t_oNewMod);

			// Decrement repeat count
			int t_iMinRepCount = a_oModRep.getMinRepeatCount();
			int t_iMaxRepCount = a_oModRep.getMaxRepeatCount();
			t_iMinRepCount--;
			t_iMaxRepCount--;
			a_oModRep.setMinRepeatCount(t_iMinRepCount);
			a_oModRep.setMaxRepeatCount(t_iMaxRepCount);

			// XXX For test
			WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
			t_oG2A.start(this.m_oGraph);
			WURCSArray t_oArray = t_oG2A.getWURCSArray();
			String t_strWURCS = (new WURCSExporter()).getWURCSString(t_oArray);
			System.err.println(t_strWURCS);

//			t_oStartRepB = t_mapOrig2CopyB.get(t_oStartRepB);
//			t_oEndRepB = t_mapOrig2CopyB.get(t_oEndRepB);
//			t_oGraph = t_oCopyGraph;

		}

		// remove repeating modification
		this.m_oGraph.removeModification(a_oModRep);
	}

	private void traverseRepeat(ModificationRepeat a_objRepeat) throws WURCSVisitorException {
		if ( a_objRepeat.getEdges().size() != 2 )
			throw new WURCSVisitorException("Repeat linkage must have two edges.");

		// Set start and end edge on repeating linkage
		boolean t_bFirstIsStart = this.isStartEdge(a_objRepeat.getEdges().getFirst());
		boolean t_bLastIsStart  = this.isStartEdge(a_objRepeat.getEdges().getLast());
		if ( ( t_bFirstIsStart &&  t_bLastIsStart ) || (!t_bFirstIsStart && !t_bLastIsStart ) )
			throw new WURCSVisitorException("Can't be specified start edge for repeating unit.");

		this.m_oRepeatEdgeStartSide = (t_bFirstIsStart)? a_objRepeat.getEdges().getFirst() : a_objRepeat.getEdges().getLast();
		this.m_oRepeatEdgeEndSide   = (t_bFirstIsStart)? a_objRepeat.getEdges().getLast()  : a_objRepeat.getEdges().getFirst();

		WURCSEdgeComparator t_oEdgeComp = new WURCSEdgeComparator();
		// Find start and end edge of repeating unit
		for ( WURCSEdge t_oEdge : this.m_oRepeatEdgeStartSide.getBackbone().getEdges() ) {
			if ( !t_oEdge.getModification().isGlycosidic() ) continue;
			if ( t_oEdge.getModification() instanceof ModificationRepeat ) continue;
			if ( this.m_oRepeatEdgeStartSide.equals(t_oEdge) ) continue;
			if ( t_oEdgeComp.compareLinkagePositions(this.m_oRepeatEdgeStartSide.getLinkages(), t_oEdge.getLinkages()) != 0 ) continue;
			this.m_oUnitStartEdge = t_oEdge;
		}
		for ( WURCSEdge t_oEdge : this.m_oRepeatEdgeEndSide.getBackbone().getEdges() ) {
			if ( !t_oEdge.getModification().isGlycosidic() ) continue;
			if ( t_oEdge.getModification() instanceof ModificationRepeat ) continue;
			if ( this.m_oRepeatEdgeEndSide.equals(t_oEdge) ) continue;
			if ( t_oEdgeComp.compareLinkagePositions(this.m_oRepeatEdgeEndSide.getLinkages(), t_oEdge.getLinkages()) != 0 ) continue;
			this.m_oUnitEndEdge = t_oEdge;
		}

		// Traverse from backbone of start edge
		this.m_oTraverser = (WURCSGraphTraverserTreeStoppable)this.getTraverser(this);
		// XXX remove comment
		System.err.println(this.m_oRepeatEdgeStartSide.printEdge()+" : "+this.m_oRepeatEdgeEndSide.printEdge());
		if ( this.m_oUnitStartEdge != null && this.m_oUnitEndEdge != null )
			System.err.println(this.m_oUnitStartEdge.printEdge()+" : "+this.m_oUnitEndEdge.printEdge());
		this.m_oTraverser.traverse(this.m_oRepeatEdgeStartSide.getBackbone());

	}

	private boolean isStartEdge(WURCSEdge a_oEdge) {
		if ( a_oEdge.getLinkages().size() > 1 ) return false;
		int t_iAnomPos = a_oEdge.getBackbone().getAnomericPosition();
		if ( t_iAnomPos == 0 || t_iAnomPos == -1 ) return true;
		if ( a_oEdge.getLinkages().getFirst().getBackbonePosition() == t_iAnomPos ) return true;
		return false;
	}

	private boolean checkExternalLinkage(ModificationRepeat a_oModRep) {
		for ( WURCSEdge t_oEdge : a_oModRep.getEdges() ) {
			if ( this.m_aRepeatBackbones.contains( t_oEdge.getBackbone() ) ) continue;
			return true;
		}
		return false;
	}
}
