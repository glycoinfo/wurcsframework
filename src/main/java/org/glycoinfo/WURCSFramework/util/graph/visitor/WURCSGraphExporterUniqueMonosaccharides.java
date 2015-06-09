package org.glycoinfo.WURCSFramework.util.graph.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneUnknown;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphExporterUniqueMonosaccharides implements WURCSVisitor {

	private LinkedList<String>   m_aMSString;
	private LinkedList<Backbone> m_aBackbones;
	private LinkedList<MS>       m_aMSs;
	private WURCSExporter       m_oExporter = new WURCSExporter();

	public LinkedList<String> getMSStrings() {
		return this.m_aMSString;
	}

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		if ( this.m_aBackbones.contains(a_objBackbone) ) return;
		this.m_aBackbones.addLast(a_objBackbone);

		// Make candidate UniqueRES
		MS t_oMSCandidate = new MS(
				a_objBackbone.getSkeletonCode(),
				a_objBackbone.getAnomericPosition(),
				a_objBackbone.getAnomericSymbol()
			);

		//XXX remove print
//		System.out.println( this.m_oExporter.getUniqueRESString( t_oURESCandidate ) );
		// Searce edges for MOD
		LinkedList<MOD> t_aMODsCandidate = new LinkedList<MOD>();
		HashSet<Modification> searchedMods = new HashSet<Modification>();
		for ( WURCSEdge t_oMODEdge : a_objBackbone.getEdges() ) {
			Modification t_oModif = t_oMODEdge.getModification();
			if ( searchedMods.contains(t_oModif) ) continue;
			if ( t_oModif instanceof InterfaceRepeat ) continue;

			searchedMods.add(t_oModif);

			// Make MOD
			MOD t_oMOD = this.makeMOD(t_oModif, a_objBackbone);
			if ( t_oMOD == null ) continue;
			if ( t_oMOD.getMAPCode().equals("") && t_oMOD.getListOfLIPs().size() < 2 ) continue;
			t_aMODsCandidate.add(t_oMOD);
		}

		// Comb out for omiting MOD
		LinkedList<MOD> t_aMODsForAdd = new LinkedList<MOD>();
		HashMap<Integer, LinkedList<MOD>> t_mapPosToMODs = new HashMap<Integer,LinkedList<MOD>>();
		for ( MOD t_oMOD : t_aMODsCandidate ) {
			// For unknown backbone
			if ( a_objBackbone instanceof BackboneUnknown ) {
				t_aMODsForAdd.add(t_oMOD);
				continue;
			}
			if ( t_oMOD.getListOfLIPs().size() != 1 || t_oMOD.getListOfLIPs().getFirst().getLIPs().size() != 1 ){
				t_aMODsForAdd.add(t_oMOD);
				continue;
			}
			int pos = t_oMOD.getListOfLIPs().getFirst().getLIPs().getFirst().getBackbonePosition();
			if ( pos == -1 ) {
				t_aMODsForAdd.add(t_oMOD);
				continue;
			}
			if ( !t_mapPosToMODs.containsKey(pos) )
				t_mapPosToMODs.put(pos, new LinkedList<MOD>());
			t_mapPosToMODs.get(pos).add(t_oMOD);
		}
		for ( Integer pos : t_mapPosToMODs.keySet() ) {
			LinkedList<MOD> t_aMODs = t_mapPosToMODs.get(pos);

			LinkedList<String> t_aMAPs = new LinkedList<String>();
			for ( MOD t_oMOD : t_aMODs ) t_aMAPs.add(t_oMOD.getMAPCode());
			if ( t_aMAPs.size() == 1 && t_aMAPs.contains("*O") ) continue;
			if ( t_aMAPs.size() == 2 && t_aMAPs.contains("*O") && t_aMAPs.contains("*=O") ) continue;
			t_aMODsForAdd.addAll(t_aMODs);
		}
		for ( MOD t_oMOD : t_aMODsForAdd ) {
			if ( t_oMOD.getMAPCode().equals("*O") ) continue;
			t_oMSCandidate.addMOD( t_oMOD );
		}

		// Check unique
		String t_strNewMS = this.m_oExporter.getMSString(t_oMSCandidate);
		if (! this.m_aMSString.contains(t_strNewMS) ) {
			this.m_aMSString.addLast(t_strNewMS);
			this.m_aMSs.addLast(t_oMSCandidate);
		}
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// Do nothing
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
		this.m_aBackbones = new LinkedList<Backbone>();
		this.m_aMSs       = new LinkedList<MS>();

		this.m_aMSString = new LinkedList<String>();
	}

	/**
	 * Convert Modification to MOD
	 * @param a_oMod Modification in monosaccharide
	 * @return Converted MOD
	 */
	private MOD makeMOD(Modification a_oMod, Backbone a_oBack) {

		String t_strMAP = a_oMod.getMAPCode();
		// Omittion
//		if ( t_strMAP.equals("*O") || t_strMAP.equals("*=O") ) return null;
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";

		MOD t_oMOD = new MOD(t_strMAP);

		for ( WURCSEdge t_oMODEdge : a_oMod.getEdges() ) {
			if ( !t_oMODEdge.getBackbone().equals(a_oBack) ) continue;
			t_oMOD.addLIPs( this.makeLIPs(t_oMODEdge) );
		}

		return t_oMOD;
	}

	/**
	 * Convert WURCSEdge to LIPs
	 * @param a_oEdge WURCSEdge between Backbone and Modification in monosaccharide
	 * @return Converted LIPs
	 */
	private LIPs makeLIPs(WURCSEdge a_oEdge) {
		boolean t_bCanOmitModif = a_oEdge.getModification().canOmitMAP();
		boolean t_bIsSingle = ( a_oEdge.getModification().getEdges().size() == 1 );
		LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();

		// For each LinkagePosition
		for ( LinkagePosition t_oLinkPos : a_oEdge.getLinkages() ) {
			LIP t_oLIP = new LIP(
					t_oLinkPos.getBackbonePosition(),
					( t_bCanOmitModif || t_bIsSingle ) ? ' ' : t_oLinkPos.getDirection().getName(),
					( t_bCanOmitModif || t_bIsSingle ) ? 0 : t_oLinkPos.getModificationPosition()
				);
			// Set probability
			if ( t_oLinkPos.getProbabilityLower() != 1 || t_oLinkPos.getProbabilityUpper() != 1 ) {
				if ( t_oLinkPos.getProbabilityPosition() == LinkagePosition.BACKBONESIDE ) {
					t_oLIP.setBackboneProbabilityLower( t_oLinkPos.getProbabilityLower() );
					t_oLIP.setBackboneProbabilityUpper( t_oLinkPos.getProbabilityUpper() );
				}
				if ( t_oLinkPos.getProbabilityPosition() == LinkagePosition.MODIFICATIONSIDE ) {
					t_oLIP.setModificationProbabilityLower( t_oLinkPos.getProbabilityLower() );
					t_oLIP.setModificationProbabilityUpper( t_oLinkPos.getProbabilityUpper() );
				}
			}
			t_aLIPs.addLast(t_oLIP);
		}
		return new LIPs(t_aLIPs);
	}


}
