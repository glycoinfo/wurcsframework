package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.graph.WURCSGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSException;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneUnknown;
import org.glycoinfo.WURCSFramework.wurcs.graph.CarbonDescriptor;
import org.glycoinfo.WURCSFramework.wurcs.graph.DirectionDescriptor;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationRepeatAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSArrayToGraph {

	private WURCSGraph m_oGraph = new WURCSGraph();
	private WURCSGraphNormalizer t_oNormal;
	private LinkedList<Backbone> m_aBackbones = new LinkedList<Backbone>();

	/**
	 * Get converted WURCSGraph
	 * @return WURCSGraph
	 */
	public WURCSGraph getGraph() {
		return this.m_oGraph;
	}

	/**
	 * Check method for invert backbone in normalizing graph
	 * @return
	 */
	public boolean isInverted() {
		if ( this.t_oNormal == null ) return false;
		return this.t_oNormal.isInverted();
	}

	public boolean hasAnomLink() {
		if ( this.t_oNormal == null ) return false;
		return this.t_oNormal.linkedAnomericPositions();
	}

	public boolean hasCyclic() {
		if ( this.t_oNormal == null ) return false;
		return this.t_oNormal.hasCyclic();
	}

	/**
	 * Start conversion from WURCSArray to WURCSGraph
	 * @param a_oArray
	 * @throws WURCSFormatException
	 * @throws WURCSException
	 */
	public void start(WURCSArray a_oArray) throws WURCSExchangeException, WURCSException {

		WURCSExporter t_oExport = new WURCSExporter();
		for ( RES t_oRES : a_oArray.getRESs() ) {
			int t_iURESID = t_oRES.getUniqueRESID();
			UniqueRES t_oURES = a_oArray.getUniqueRESs().get(t_iURESID-1);

			Backbone t_oBackbone = this.convertToBackbone( t_oURES );
			// For each MOD in UniqueRES
			for ( MOD t_oMOD : t_oURES.getMODs() ) {
				Modification t_oModif = new Modification( t_oMOD.getMAPCode() );
				for ( LIPs t_oLIPs : t_oMOD.getListOfLIPs() ) {
					WURCSEdge t_oEdge = new WURCSEdge();
					for ( LIP t_oLIP : t_oLIPs.getLIPs() ) {
						t_oEdge.addLinkage( this.convertToLinkagePosition(t_oLIP) );
					}
					this.m_oGraph.addResidues(t_oBackbone, t_oEdge, t_oModif);
				}
			}
			this.m_aBackbones.addLast(t_oBackbone);
		}

		for ( LIN t_oLIN : a_oArray.getLINs() ) {
			// Check alternative
			LinkedList<GLIPs> t_aLeadInGLIPs  = new LinkedList<GLIPs>();
			LinkedList<GLIPs> t_aLeadOutGLIPs = new LinkedList<GLIPs>();
			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {
				if ( t_oGLIPs.getAlternativeType() == null ) continue;
				if ( t_oGLIPs.getGLIPs().size() < 2 )
					throw new WURCSExchangeException("Alternative GLIPs must have two or more GLIP. : "+t_oExport.getGLIPsString(t_oGLIPs));

				if ( t_oGLIPs.getAlternativeType().equals("}") ) t_aLeadInGLIPs.add(t_oGLIPs);
				if ( t_oGLIPs.getAlternativeType().equals("{") ) t_aLeadOutGLIPs.add(t_oGLIPs);
			}
			if ( t_aLeadInGLIPs.size()  > 1 ) throw new WURCSExchangeException("Two or more lead in GLIPs is found. : ");
			if ( t_aLeadOutGLIPs.size() > 1 ) throw new WURCSExchangeException("Two or more lead out GLIPs is found. : ");

			// Construct modification (or its subclasses)
			Modification t_oModif = new Modification( t_oLIN.getMAPCode() );
			if ( !t_aLeadInGLIPs.isEmpty() || !t_aLeadOutGLIPs.isEmpty() ) {
				ModificationAlternative t_oModifAlt = new ModificationAlternative(t_oLIN.getMAPCode());
				if ( t_oLIN.isRepeatingUnit() ) {
					ModificationRepeatAlternative t_oModifRepAlt = new ModificationRepeatAlternative(t_oLIN.getMAPCode());
					t_oModifRepAlt.setMinRepeatCount(t_oLIN.getMinRepeatCount());
					t_oModifRepAlt.setMaxRepeatCount(t_oLIN.getMaxRepeatCount());
					t_oModifAlt = t_oModifRepAlt;
				}
				t_oModif = t_oModifAlt;
			} else if ( t_oLIN.isRepeatingUnit() ) {
				ModificationRepeat t_oModifRep = new ModificationRepeat(t_oLIN.getMAPCode());
				t_oModifRep.setMinRepeatCount(t_oLIN.getMinRepeatCount());
				t_oModifRep.setMaxRepeatCount(t_oLIN.getMaxRepeatCount());
				t_oModif = t_oModifRep;
			}

			for ( GLIPs t_oGLIPs : t_oLIN.getListOfGLIPs() ) {
				// Make edges and linkages
				HashMap<Backbone, WURCSEdge> t_mapB2Edge = new HashMap<Backbone, WURCSEdge>();
				for ( GLIP t_oGLIP : t_oGLIPs.getGLIPs() ) {
					int t_iRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
					Backbone t_oBackbone = this.m_aBackbones.get(t_iRESID-1);
					if ( !t_mapB2Edge.containsKey(t_oBackbone) )
						t_mapB2Edge.put(t_oBackbone, new WURCSEdge() );
					WURCSEdge t_oEdge = t_mapB2Edge.get(t_oBackbone);
					t_oEdge.addLinkage( this.convertToLinkagePosition(t_oGLIP) );
				}

				// Add linkages to graph
				LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();
				for ( Backbone t_oBackbone : t_mapB2Edge.keySet() ) {
					WURCSEdge t_oEdge = t_mapB2Edge.get(t_oBackbone);
					this.m_oGraph.addResidues(t_oBackbone, t_oEdge, t_oModif);
					t_aEdges.add(t_oEdge);
				}

				// For alternative edges
				if ( !(t_oModif instanceof ModificationAlternative) ) continue;
				ModificationAlternative t_oModifAlt = (ModificationAlternative)t_oModif;
				if ( t_aLeadInGLIPs.contains(t_oGLIPs) ) {
					for ( WURCSEdge t_oEdge : t_aEdges ) t_oModifAlt.addLeadInEdge(t_oEdge);
				}
				if ( t_aLeadOutGLIPs.contains(t_oGLIPs) ) {
					for ( WURCSEdge t_oEdge : t_aEdges ) t_oModifAlt.addLeadOutEdge(t_oEdge);
				}
			}

		}

		// For monosaccharide with no modification
		for ( Backbone t_oBackbone : this.m_aBackbones ) {
			if ( this.m_oGraph.getBackbones().contains(t_oBackbone) ) continue;
			this.m_oGraph.addBackbone(t_oBackbone);
		}

		// Normalize graph
		this.t_oNormal = new WURCSGraphNormalizer();
		this.t_oNormal.start( this.m_oGraph );
	}

	/**
	 * Convert to LinkagePosition from LIP
	 * @param t_oLIP LIP in WURCSArray
	 * @return LinkagePosition Converted from LIP
	 * @throws WURCSFormatException
	 * @throws WURCSException
	 */
	private LinkagePosition convertToLinkagePosition(LIP t_oLIP) throws WURCSExchangeException, WURCSException {
		int     t_iBPos      = t_oLIP.getBackbonePosition();
		char    t_cDirection = t_oLIP.getBackboneDirection();
		int     t_iMPos      = t_oLIP.getModificationPosition();
		boolean t_bCompressDirection = ( t_cDirection == ' ' && t_iMPos == 0 );
		boolean t_bCompressMPos = ( t_iMPos == 0 );
		DirectionDescriptor t_enumDirection = DirectionDescriptor.forChar( t_oLIP.getBackboneDirection() );
		if ( t_enumDirection == null )
			throw new WURCSExchangeException("Unknown DirectionDescriptor is found.");
		LinkagePosition t_oLinkPos = new LinkagePosition(t_iBPos, t_enumDirection, t_bCompressDirection, t_iMPos, t_bCompressMPos);

		// Set probabilities
		if ( t_oLIP.getBackboneProbabilityLower() != 1 ) {
			t_oLinkPos.setProbabilityPosition( LinkagePosition.BACKBONESIDE );
			t_oLinkPos.setProbabilityLower( t_oLIP.getBackboneProbabilityLower() );
			t_oLinkPos.setProbabilityUpper( t_oLIP.getBackboneProbabilityUpper() );
		}
		if ( t_oLIP.getModificationProbabilityLower() != 1 ) {
			t_oLinkPos.setProbabilityPosition( LinkagePosition.MODIFICATIONSIDE );
			t_oLinkPos.setProbabilityLower( t_oLIP.getModificationProbabilityLower() );
			t_oLinkPos.setProbabilityUpper( t_oLIP.getModificationProbabilityUpper() );
		}
		return t_oLinkPos;
	}

	private Backbone convertToBackbone(UniqueRES a_oURES) throws WURCSExchangeException {
		Backbone t_oBackbone = new Backbone();
		LinkedList<String> t_aCDString = this.parseSkeletonCode( a_oURES.getSkeletonCode() );

		// For unknown monosaccharide
		if ( t_aCDString.getFirst().equals("<0>") ) {
			char t_cAnomSymbol = a_oURES.getAnomericSymbol();
			if ( t_cAnomSymbol == ' ' ) t_cAnomSymbol = 'x';
			BackboneUnknown t_oUnknown = new BackboneUnknown( t_cAnomSymbol );
			return t_oUnknown;
		}

		for ( int i=0; i< t_aCDString.size(); i++ ) {
			String t_strCD = t_aCDString.get(i);
			boolean t_bIsTerminal = ( i == 0 || i == t_aCDString.size()-1 );
			boolean t_bIsAnomeric = ( i == a_oURES.getAnomericPosition()-1 );

			BackboneCarbon t_oBC = new BackboneCarbon( t_oBackbone, CarbonDescriptor.forCharacter(t_strCD.charAt(0) ,t_bIsTerminal), t_bIsAnomeric );
			t_oBackbone.addBackboneCarbon(t_oBC);
		}

		return t_oBackbone;
	}

	private LinkedList<String> parseSkeletonCode(String a_strSkeletonCode) throws WURCSExchangeException {
		LinkedList<String> t_aCDString = new LinkedList<String>();
		int length = a_strSkeletonCode.length();
		for ( int i=0; i<length; i++ ) {
			char t_cName = a_strSkeletonCode.charAt(i);
			if ( Character.isAlphabetic(t_cName) || Character.isDigit(t_cName) ) {
				t_aCDString.add(""+t_cName);
				continue;
			}
			// For unknown length
			if ( !a_strSkeletonCode.substring(i, i+3).equals("<0>") )
				throw new WURCSExchangeException("unknown CarbonDescriptor is found : "+a_strSkeletonCode);
			i += 3;
			t_aCDString.add("<0>");
		}
		return t_aCDString;
	}

}
