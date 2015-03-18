package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.graph.Backbone;
import org.glycoinfo.WURCSFramework.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.graph.Modification;
import org.glycoinfo.WURCSFramework.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.graph.ModificationRepeat;
import org.glycoinfo.WURCSFramework.graph.ModificationRepeatAlternative;
import org.glycoinfo.WURCSFramework.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.comparator.graph.WURCSEdgeComparator;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class WURCSGraphToArray implements WURCSVisitor {

	private String m_strVersion = "2.0";
	private LinkedList<Backbone>     m_aBackbones;
	private LinkedList<Modification> m_aGlycosidicModifications;
	private LinkedList<ModificationAlternative> m_aGlycosidicModificationAlternatives;

	private WURCSArray m_oWURCS = null;
	private LinkedList<String>    m_aURESString;
	private LinkedList<UniqueRES> m_aURES;
	private LinkedList<RES>       m_aRES;
	private LinkedList<LIN>       m_aLIN;

	private WURCSEdgeComparator m_oEdgeComp = new WURCSEdgeComparator();
	private WURCSExporter       m_oExporter = new WURCSExporter();

	@Override
	public void visit(Backbone a_objBackbone) throws WURCSVisitorException {
		if ( this.m_aBackbones.contains(a_objBackbone) ) return;
		this.m_aBackbones.addLast(a_objBackbone);

		// Make candidate UniqueRES
		UniqueRES t_oURESCandidate
			= new UniqueRES(
				this.m_aURES.size()+1,
				a_objBackbone.getSkeletonCode(),
				a_objBackbone.getAnomericPosition(),
				a_objBackbone.getAnomericSymbol()
			);

		//XXX
		System.out.println( this.m_oExporter.getUniqueRESString( t_oURESCandidate ) );
		// Searce edges for MOD
		LinkedList<WURCSEdge> edges = a_objBackbone.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		HashSet<Modification> searchedMods = new HashSet<Modification>();
		for ( WURCSEdge t_oMODEdge : edges ) {
			Modification t_oModif = t_oMODEdge.getModification();
			if ( searchedMods.contains(t_oModif) ) continue;
			if ( t_oModif.isGlycosidic() ) continue;
			if ( t_oModif instanceof InterfaceRepeat ) {
				if ( !this.m_aGlycosidicModifications.contains(t_oModif) )
					this.m_aGlycosidicModifications.add(t_oModif);
				continue;
			}

			searchedMods.add(t_oModif);

			// Make MOD
			MOD t_oMOD = this.makeMOD(t_oModif);
			if ( t_oMOD == null ) continue;
			t_oURESCandidate.addMOD( this.makeMOD(t_oModif) );

			if ( !t_oMODEdge.isReverse() ) continue;
			System.err.println("has parent");
		}

		// Check unique
		String t_strNewURES = this.m_oExporter.getUniqueRESString(t_oURESCandidate);
		if (! this.m_aURESString.contains(t_strNewURES) ) {
			this.m_aURESString.addLast(t_strNewURES);
			this.m_aURES.addLast(t_oURESCandidate);
		}

		// Make new RES
		int t_iURESID = this.m_aURESString.indexOf(t_strNewURES)+1;
		String t_strRESIndex =  WURCSDataConverter.convertRESIDToIndex( this.m_aBackbones.size()+1 );
		this.m_aRES.addLast( new RES( t_iURESID, t_strRESIndex ) );
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		if ( !a_objModification.isGlycosidic() ) return;
		// Add modifiation at glycosidic linkage
		if ( this.m_aGlycosidicModifications.contains(a_objModification) ) return;
		this.m_aGlycosidicModifications.addLast(a_objModification);
	}

	@Override
	public void visit(ModificationAlternative a_objModificationAlternative) throws WURCSVisitorException {
		// TODO 自動生成されたメソッド・スタブ
		if ( !a_objModificationAlternative.isGlycosidic() )
			throw new WURCSVisitorException("ModificationAlternative must be Glycosidic linkage.");
		// Add modifiation at glycosidic linkage
		if ( this.m_aGlycosidicModificationAlternatives.contains(a_objModificationAlternative) ) return;
		this.m_aGlycosidicModificationAlternatives.addLast(a_objModificationAlternative);

	}


	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
/*
		// Search test
		if ( !a_objWURCSEdge.isReverse() ) {
			Backbone t_oB = a_objWURCSEdge.getBackbone();
			System.err.println( this.m_aBackbones.indexOf( t_oB ) +":"+t_oB.getSkeletonCode()+":"+a_objWURCSEdge.getLinkages().getFirst().getBackbonePosition() );
		} else {
			Modification t_oM = a_objWURCSEdge.getModification();
			System.err.println( "M to B: "+a_objWURCSEdge.getLinkages().getFirst().getBackbonePosition() );
		}
*/
		// Do nothing
	}

	@Override
	public void start(WURCSGraph a_objGraph) throws WURCSVisitorException {
		this.clear();

		WURCSGraphTraverser t_objTraverser = this.getTraverser(this);
		t_objTraverser.traverseGraph(a_objGraph);

		this.makeWURCSArray();
	}

	@Override
	public WURCSGraphTraverser getTraverser(WURCSVisitor a_objVisitor) throws WURCSVisitorException {
		return new WURCSGraphTraverserTree(a_objVisitor);
	}

	@Override
	public void clear() {
		this.m_aBackbones = new LinkedList<Backbone>();
		this.m_aGlycosidicModifications = new LinkedList<Modification>();
		this.m_aGlycosidicModificationAlternatives = new LinkedList<ModificationAlternative>();

		this.m_strVersion = "2.0";
		this.m_aURESString = new LinkedList<String>();
		this.m_aURES = new LinkedList<UniqueRES>();
		this.m_aRES = new LinkedList<RES>();
		this.m_aLIN = new LinkedList<LIN>();
	}

	public WURCSArray getWURCSArray() {
		System.out.println( this.m_oExporter.getWURCSString(this.m_oWURCS) );
		return this.m_oWURCS;
	}

	private void makeWURCSArray() {
		// Make LIN list
		for ( Modification mod : this.m_aGlycosidicModifications ) {
			this.m_aLIN.addLast( this.makeLIN(mod) );
		}
		for ( ModificationAlternative mod : this.m_aGlycosidicModificationAlternatives ) {
			this.m_aLIN.addLast( this.makeLIN(mod) );
		}

		this.m_oWURCS = new WURCSArray(this.m_strVersion, this.m_aURES.size(), this.m_aRES.size(), this.m_aLIN.size());
		for ( UniqueRES t_oURES : this.m_aURES )
			this.m_oWURCS.addUniqueRES(t_oURES);

		for ( RES t_oRES : this.m_aRES )
			this.m_oWURCS.addRES(t_oRES);

		for ( LIN t_oLIN : this.m_aLIN )
			this.m_oWURCS.addLIN(t_oLIN);

	}

	private MOD makeMOD(Modification a_oMod) {

		String t_strMAP = a_oMod.getMAPCode();
		// Omittion
//		if ( t_strMAP.equals("*O") || t_strMAP.equals("*=O") ) return null;
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";

		MOD t_oMOD = new MOD(t_strMAP);

		LinkedList<WURCSEdge> edges = a_oMod.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		for ( WURCSEdge t_oMODEdge : edges )
			t_oMOD.addLIPs( this.makeLIPs(t_oMODEdge) );

		return t_oMOD;
	}

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

	private LIN makeLIN(ModificationAlternative a_oMod) {
		String t_strMAP = a_oMod.getMAPCode();
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";
		LIN t_oLIN = new LIN(t_strMAP);

		if ( !a_oMod.getLeadInEdges().isEmpty() ) {

		}
		t_oLIN.addGLIPs( this.makeGLIPs(a_oMod.getLeadInEdges(), "}") );
		t_oLIN.addGLIPs( this.makeGLIPs(a_oMod.getLeadOutEdges(), "{") );
		LinkedList<WURCSEdge> edges = a_oMod.getLeadInEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		for ( WURCSEdge t_oEdge : edges ) {
			if ( a_oMod.getLeadInEdges().contains(t_oEdge) ) continue;
			if ( a_oMod.getLeadOutEdges().contains(t_oEdge) ) continue;
			t_oLIN.addGLIPs( this.makeGLIPs(t_oEdge) );
		}

		if ( a_oMod.getClass() != ModificationRepeatAlternative.class ) return t_oLIN;

		ModificationRepeatAlternative t_oModRep = (ModificationRepeatAlternative)a_oMod;
		// Set Repeating
		t_oLIN.setRepeatingUnit(true);
		t_oLIN.setMinRepeatCount( t_oModRep.getMinRepeatCount() );
		t_oLIN.setMaxRepeatCount( t_oModRep.getMaxRepeatCount() );

		return t_oLIN;
	}

	private LIN makeLIN(Modification a_oMod) {

		String t_strMAP = a_oMod.getMAPCode();
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";
		LIN t_oLIN = new LIN(t_strMAP);

		LinkedList<WURCSEdge> edges = a_oMod.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		for ( WURCSEdge t_oEdge : edges ) {
			t_oLIN.addGLIPs( this.makeGLIPs(t_oEdge) );
		}
		if ( a_oMod.getClass() != ModificationRepeat.class ) return t_oLIN;

		ModificationRepeat t_oModRep = (ModificationRepeat)a_oMod;
		// Set Repeating
		t_oLIN.setRepeatingUnit(true);
		t_oLIN.setMinRepeatCount( t_oModRep.getMinRepeatCount() );
		t_oLIN.setMaxRepeatCount( t_oModRep.getMaxRepeatCount() );

		return t_oLIN;
	}

	private GLIPs makeGLIPs(LinkedList<WURCSEdge> a_aEdges, String a_strAlternativeType) {
		Collections.sort( a_aEdges, this.m_oEdgeComp );
		LinkedList<GLIP> t_aGLIPs = new LinkedList<GLIP>();
		for ( WURCSEdge t_oEdge : a_aEdges ) {
			Backbone backbone = t_oEdge.getBackbone();
			int t_iRES = this.m_aBackbones.indexOf(backbone)+1;
			String t_strRESIndex = WURCSDataConverter.convertRESIDToIndex(t_iRES);

			for ( LinkagePosition t_oLinkPos : t_oEdge.getLinkages() ) {
				GLIP t_oGLIP = this.makeGLIP(t_strRESIndex, t_oLinkPos, t_oEdge.getModification().canOmitMAP());
				t_aGLIPs.addLast(t_oGLIP);
			}
		}
		GLIPs t_oGLIPs = new GLIPs(t_aGLIPs);
		t_oGLIPs.setAlternative(a_strAlternativeType);
		return t_oGLIPs;

	}

	private GLIPs makeGLIPs(WURCSEdge a_oEdge) {
		// Make string of RES
		Backbone backbone = a_oEdge.getBackbone();
		int t_iRES = this.m_aBackbones.indexOf(backbone)+1;
		String t_strRESIndex = WURCSDataConverter.convertRESIDToIndex(t_iRES);

		LinkedList<GLIP> t_aGLIPs = new LinkedList<GLIP>();
		for ( LinkagePosition t_oLinkPos : a_oEdge.getLinkages() ) {
			GLIP t_oGLIP = this.makeGLIP(t_strRESIndex, t_oLinkPos, a_oEdge.getModification().canOmitMAP());
			t_aGLIPs.addLast(t_oGLIP);
		}
		return new GLIPs(t_aGLIPs);
	}

	private GLIP makeGLIP(String a_strRESIndex, LinkagePosition a_oLinkPos, boolean a_bOmitModif) {
		GLIP t_oGLIP = new GLIP(
				a_strRESIndex,
				a_oLinkPos.getBackbonePosition(),
				a_bOmitModif ? ' ' : a_oLinkPos.getDirection().getName(),
				a_bOmitModif ? 0 : a_oLinkPos.getModificationPosition()
			);
		// Set probability
		if ( a_oLinkPos.getProbabilityLower() != 1 || a_oLinkPos.getProbabilityUpper() != 1 ) {
			if ( a_oLinkPos.getProbabilityPosition() == LinkagePosition.BACKBONESIDE ) {
				t_oGLIP.setBackboneProbabilityLower( a_oLinkPos.getProbabilityLower() );
				t_oGLIP.setBackboneProbabilityUpper( a_oLinkPos.getProbabilityUpper() );
			}
			if ( a_oLinkPos.getProbabilityPosition() == LinkagePosition.MODIFICATIONSIDE ) {
				t_oGLIP.setModificationProbabilityLower( a_oLinkPos.getProbabilityLower() );
				t_oGLIP.setModificationProbabilityUpper( a_oLinkPos.getProbabilityUpper() );
			}
		}
		return t_oGLIP;
	}

}
