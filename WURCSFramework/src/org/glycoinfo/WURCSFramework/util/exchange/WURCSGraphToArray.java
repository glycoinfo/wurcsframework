package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSEdgeComparator;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverser;
import org.glycoinfo.WURCSFramework.util.graph.traverser.WURCSGraphTraverserTree;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitor;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectConnectingBackboneGroups;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectSequence;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneUnknown;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

/**
 * Class for conversion from WURCSGraph to WURCSArray
 * @author MasaakiMatsubara
 *
 */
public class WURCSGraphToArray implements WURCSVisitor {

	private String m_strVersion = "2.0";
	private LinkedList<Backbone>     m_aBackbones;
	private LinkedList<Modification> m_aGlycosidicModifications;
	private LinkedList<ModificationAlternative> m_aGlycosidicModificationAlternatives;

	private WURCSArray m_oWURCS = null;
	private boolean m_bIsComposition = false;
	private LinkedList<String>    m_aURESString;
	private LinkedList<UniqueRES> m_aURES;
	private LinkedList<RES>       m_aRES;
	private LinkedList<LIN>       m_aLIN;

	private WURCSEdgeComparator m_oEdgeComp = new WURCSEdgeComparator();
	private WURCSExporter       m_oExporter = new WURCSExporter();

	/**
	 * Get converted WURCSArray
	 * @return Converted WURCSArray
	 */
	public WURCSArray getWURCSArray() {
		// XXX remove print
//		System.out.println( this.m_oExporter.getWURCSString(this.m_oWURCS) );
		return this.m_oWURCS;
	}

// add three methods for test routine get Backbones and Modifications
// by muller 181016
		public LinkedList<Backbone> getG2ABackbones() {
			return m_aBackbones;
			}
		public LinkedList<Modification> getG2AModifications() {
			return m_aGlycosidicModifications;
			}
		public LinkedList<ModificationAlternative> getG2AModificationAlternatives() {
			return m_aGlycosidicModificationAlternatives;
			}
// end of addition

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

		//XXX remove print
//		System.out.println( this.m_oExporter.getUniqueRESString( t_oURESCandidate ) );
		// Searce edges for MOD
		LinkedList<MOD> t_aMODsCandidate = new LinkedList<MOD>();
		HashSet<Modification> searchedMods = new HashSet<Modification>();
		LinkedList<WURCSEdge> edges = a_objBackbone.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
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
			t_aMODsCandidate.add(t_oMOD);

			if ( !t_oMODEdge.isReverse() ) continue;
			// XXX remove print
			System.err.println("[info] has parent in class WURCSGraphToArray (visit(backbone))");
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
			if ( t_oMOD.getMAPCode().equals("*=O") ) continue;
			t_oURESCandidate.addMOD( t_oMOD );
		}

		// Check unique
		String t_strNewURES = this.m_oExporter.getUniqueRESString(t_oURESCandidate);
		if (! this.m_aURESString.contains(t_strNewURES) ) {
			this.m_aURESString.addLast(t_strNewURES);
			this.m_aURES.addLast(t_oURESCandidate);
		}

		// Make new RES
		int t_iURESID = this.m_aURESString.indexOf(t_strNewURES)+1;
		String t_strRESIndex =  WURCSDataConverter.convertRESIDToIndex( this.m_aBackbones.size() );
		this.m_aRES.addLast( new RES( t_iURESID, t_strRESIndex ) );
	}

	@Override
	public void visit(Modification a_objModification) throws WURCSVisitorException {
		// For alternative
		if ( a_objModification instanceof ModificationAlternative ) {
			this.visit((ModificationAlternative)a_objModification);
			return;
		}

		if ( !a_objModification.isGlycosidic() ) return;
		// Add modifiation at glycosidic linkage
		if ( this.m_aGlycosidicModifications.contains(a_objModification) ) return;
		this.m_aGlycosidicModifications.addLast(a_objModification);
	}

	private void visit(ModificationAlternative a_objModificationAlternative) throws WURCSVisitorException {
		if ( !a_objModificationAlternative.isGlycosidic() )
			throw new WURCSVisitorException("ModificationAlternative must be Glycosidic linkage in class WURCSGraphToArray(visit).");
		// Add modifiation at glycosidic linkage
		if ( this.m_aGlycosidicModificationAlternatives.contains(a_objModificationAlternative) ) return;
		this.m_aGlycosidicModificationAlternatives.addLast(a_objModificationAlternative);

	}

	@Override
	public void visit(WURCSEdge a_objWURCSEdge) throws WURCSVisitorException {
		// Do nothing

/*
		// Search test
		System.err.println(a_objWURCSEdge);
		if ( !a_objWURCSEdge.isReverse() ) {
			Backbone t_oB = a_objWURCSEdge.getBackbone();
			System.err.println( this.m_aBackbones.indexOf( t_oB ) +":"+a_objWURCSEdge.printEdge() );
		} else {
			System.err.println( "M to B: "+a_objWURCSEdge.printEdge() );
		}
*/
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

//		WURCSGraphTraverser t_objTraverser = this.getTraverser(this);
//		t_objTraverser.traverseGraph(a_objGraph);

		// Check composition
		WURCSVisitorCollectConnectingBackboneGroups t_oGroup = new WURCSVisitorCollectConnectingBackboneGroups();
		t_oGroup.start(a_objGraph);
		if ( t_oGroup.getBackboneGroups().size() > 1 )
			this.m_bIsComposition = true;


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

	/**
	 * Convert WURCSGraph object to WURCSArray
	 */
	private void makeWURCSArray() {
		// Make LIN list
		for ( Modification mod : this.m_aGlycosidicModifications ) {
			this.m_aLIN.addLast( this.makeLIN(mod) );
		}
		for ( ModificationAlternative mod : this.m_aGlycosidicModificationAlternatives ) {
			this.m_aLIN.addLast( this.makeLIN(mod) );
		}

		this.m_oWURCS = new WURCSArray(this.m_strVersion, this.m_aURES.size(), this.m_aRES.size(), this.m_aLIN.size(), this.m_bIsComposition);
		for ( UniqueRES t_oURES : this.m_aURES )
			this.m_oWURCS.addUniqueRES(t_oURES);

		for ( RES t_oRES : this.m_aRES )
			this.m_oWURCS.addRES(t_oRES);

		for ( LIN t_oLIN : this.m_aLIN )
			this.m_oWURCS.addLIN(t_oLIN);

	}

	/**
	 * Convert Modification to MOD
	 * @param a_oMod Modification in monosaccharide
	 * @return Converted MOD
	 */
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

	/**
	 * Convert WURCSEdge to LIPs
	 * @param a_oEdge WURCSEdge between Backbone and Modification in monosaccharide
	 * @return Converted LIPs
	 */
	private LIPs makeLIPs(WURCSEdge a_oEdge) {
		boolean t_bCanOmitModif = a_oEdge.getModification().canOmitMAP();
		boolean t_bHasCarbonOrder = a_oEdge.getModification().hasBackboneCarbonOrder();
		boolean t_bIsSingle = ( a_oEdge.getModification().getEdges().size() == 1 );
		LinkedList<LIP> t_aLIPs = new LinkedList<LIP>();

		// For each LinkagePosition
		for ( LinkagePosition t_oLinkPos : a_oEdge.getLinkages() ) {
			// For anomeric position
			boolean t_bOmitDirection = t_oLinkPos.canOmitDirection();
			if ( a_oEdge.getBackbone().getAnomericPosition() == t_oLinkPos.getBackbonePosition() )
				t_bOmitDirection = true;
			LIP t_oLIP = new LIP(
					t_oLinkPos.getBackbonePosition(),
					( t_oLinkPos.canOmitModificationPosition() && t_bOmitDirection ) ?
							' ' : t_oLinkPos.getDirection().getName(),
					( t_bCanOmitModif || t_oLinkPos.canOmitModificationPosition() ) ? 0 : t_oLinkPos.getModificationPosition()
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

	/**
	 * Convert ModificationAlternative to LIN
	 * @param a_oMod ModificationAlternative
	 * @return Conveted alternative LIN
	 */
	private LIN makeLIN(ModificationAlternative a_oMod) {
		String t_strMAP = a_oMod.getMAPCode();
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";
		LIN t_oLIN = new LIN(t_strMAP);

		if ( !a_oMod.getLeadInEdges().isEmpty() )
			t_oLIN.addGLIPs( this.makeGLIPs(a_oMod.getLeadInEdges(), "}") );
		if ( !a_oMod.getLeadOutEdges().isEmpty() )
			t_oLIN.addGLIPs( this.makeGLIPs(a_oMod.getLeadOutEdges(), "{") );
		LinkedList<WURCSEdge> edges = a_oMod.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		for ( WURCSEdge t_oEdge : edges ) {
			if ( a_oMod.getLeadInEdges().contains(t_oEdge) ) continue;
			if ( a_oMod.getLeadOutEdges().contains(t_oEdge) ) continue;
			t_oLIN.addGLIPs( this.makeGLIPs(t_oEdge) );
		}

		this.setRepeat(a_oMod, t_oLIN);

		return t_oLIN;
	}

	/**
	 * COnvert Modification to LIN
	 * @param a_oMod Modification between two different Backbone (Glycosidic linkage)
	 * @return Converted glycosidic LIN
	 */
	private LIN makeLIN(Modification a_oMod) {

		String t_strMAP = a_oMod.getMAPCode();
		if ( t_strMAP.equals("*O*") ) t_strMAP = "";
		LIN t_oLIN = new LIN(t_strMAP);

		LinkedList<WURCSEdge> edges = a_oMod.getEdges();
		Collections.sort( edges, this.m_oEdgeComp );
		for ( WURCSEdge t_oEdge : edges ) {
			t_oLIN.addGLIPs( this.makeGLIPs(t_oEdge) );
		}

		this.setRepeat(a_oMod, t_oLIN);

		return t_oLIN;
	}

	/**
	 * Set repeat information from Modification to LIN
	 * @param a_oMod Modification
	 * @param a_oLIN LIN
	 */
	private void setRepeat( Modification a_oMod, LIN a_oLIN ) {
		if ( ! (a_oMod instanceof InterfaceRepeat) ) return;
		InterfaceRepeat t_oModRep = (InterfaceRepeat)a_oMod;
		// Set Repeating
		a_oLIN.setRepeatingUnit(true);
		a_oLIN.setMinRepeatCount( t_oModRep.getMinRepeatCount() );
		a_oLIN.setMaxRepeatCount( t_oModRep.getMaxRepeatCount() );
	}

	/**
	 * Convert alternative WURCSEdges to GLIPs
	 * @param a_aEdges WURCSEdges on alternative modification
	 * @param a_strAlternativeType Type of alternative ("{" or "}")
	 * @return Converted alternative GLIPs
	 */
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

	/**
	 * Convert WURCSEdge to GLIPs
	 * @param a_oEdge WURCSEdge in glycosidic linkage
	 * @return Converted GLIPs
	 */
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

	/**
	 * COnvert LinkagePosition to GLIP
	 * @param a_strRESIndex Index of RES
	 * @param a_oLinkPos LinkagePosition in glicosidic linkage
	 * @param a_bOmitModif Wether or not modification can omit
	 * @return Converted GLIP
	 */
	private GLIP makeGLIP(String a_strRESIndex, LinkagePosition a_oLinkPos, boolean a_bOmitModif) {
		GLIP t_oGLIP = new GLIP(
				a_strRESIndex,
				a_oLinkPos.getBackbonePosition(),
				( ( a_bOmitModif || a_oLinkPos.canOmitModificationPosition() ) && a_oLinkPos.canOmitDirection() ) ?
					' ' : a_oLinkPos.getDirection().getName(),
				( a_bOmitModif || a_oLinkPos.canOmitModificationPosition() ) ?
					0 : a_oLinkPos.getModificationPosition()
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
