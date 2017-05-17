package org.glycoinfo.WURCSFramework.util.graph.analysis;

import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphStateDeterminator {

	public WURCSGraphStateDeterminator() {

	}

	public SubsumptionLevel getSubsumptionLevel(WURCSGraph a_oGraph) {

		// For monosaccharide
		if ( a_oGraph.getBackbones().size() == 1 )
			return SubsumptionLevel.LVX;

		// Check glycosidic linkages
		// TODO: LV4 check
		boolean t_bHasGlycosidicLinkage = false;
		Iterator<Modification> t_itModification = a_oGraph.getModificationIterator();
		while ( t_itModification.hasNext() ) {
			Modification t_oModification = t_itModification.next();
			if ( t_oModification.isGlycosidic() )
				t_bHasGlycosidicLinkage = true;
		}

		if ( !t_bHasGlycosidicLinkage )
			return SubsumptionLevel.LV3;

		// Check linkages
//		boolean t_bFullyDefined = true;
		boolean t_bHasDefinedAcceptorPosition = false;
		boolean t_bHasDefinedAnomericSymbol = false;
		Iterator<Backbone> t_itBackbone = a_oGraph.getBackboneIterator();
		while ( t_itBackbone.hasNext() ) {
			Backbone t_oBackbone = t_itBackbone.next();
			// Check acceptor position
			if ( this.hasDefinedAcceptorPosition( t_oBackbone.getEdges() ) )
				t_bHasDefinedAcceptorPosition = true;

			// Ignore open chain
			if ( this.isOpenChain(t_oBackbone) ) continue;

			// Check anomer
			if ( this.isDefinedAnomericState(t_oBackbone) )
				t_bHasDefinedAnomericSymbol = true;
		}

//		if ( t_bFullyDefined )
//			return SubsumptionLevel.LV0;

		if ( t_bHasDefinedAcceptorPosition || t_bHasDefinedAnomericSymbol )
			return SubsumptionLevel.LV1;

		return SubsumptionLevel.LVX;
	}

	private boolean hasDefinedAcceptorPosition(LinkedList<WURCSEdge> a_oEdges) {
		// Check acceptor position
		for ( WURCSEdge t_oEdge : a_oEdges ) {
			// Ignore substituent linkage
			if ( !t_oEdge.getModification().isGlycosidic() ) continue;
			// Ignore anomeric edge
			if ( this.isAnomericEdge(t_oEdge) ) continue;

			// true if defined position is found
			for ( LinkagePosition t_oPos : t_oEdge.getLinkages() )
				if ( t_oPos.getBackbonePosition() > 0 ) return true;
		}
		return false;
	}

	private boolean isFullyDefinedAcceptorPosition(LinkedList<WURCSEdge> a_oEdges) {
		// Check acceptor position
		for ( WURCSEdge t_oEdge : a_oEdges ) {
			// Ignore substituent linkage
			if ( !t_oEdge.getModification().isGlycosidic() ) continue;
			// Ignore anomeric edge
			if ( this.isAnomericEdge(t_oEdge) ) continue;

			// false if position is unknown
			if ( t_oEdge.getLinkages().getFirst().getBackbonePosition() == -1 )
				return false;

			// false if alternative position
			if ( t_oEdge.getLinkages().size() > 1 ) return false;

		}
		return true;
	}

	private boolean isAnomericEdge(WURCSEdge a_oEdge) {
		// False for not glycosidic linkeage
		if ( !a_oEdge.getModification().isGlycosidic() ) return false;
		// False for alternative position
		if ( a_oEdge.getLinkages().size() > 1 ) return false;

		int t_iPosition = a_oEdge.getLinkages().getFirst().getBackbonePosition();

		// False for unknown position
		if ( t_iPosition == -1 ) return false;

		// False for anomeric position is not match
		if ( t_iPosition != a_oEdge.getBackbone().getAnomericPosition() ) return false;
		return true;
	}
/*
	private boolean isFullyDefinedBackbone(Backbone a_oBackbone) {
		// false if unknown backbone "<Q>"
		if ( a_oBackbone instanceof BackboneUnknown_TBD ) return false;

		// false if uncertain anomeric position
		if ( this.hasUncertainAnomericPosition(a_oBackbone) ) return false;

		// false if anomer unknown
		if ( !this.isDefinedAnomericState(a_oBackbone) ) return false;

		return true;
	}
*/
/*
	private boolean isFullyDefinedStereo(Backbone a_oBackbone) {
		for ( BackboneCarbon t_oBC : a_oBackbone.getBackboneCarbons() ) {
			char t_cCD = t_oBC.getDesctriptor().getChar();
			t_cCD
			if ( t_oBC.getDesctriptor().getChar() == 'o' ) return false;
			if ( t_oBC.getDesctriptor().getChar() == 'O' ) return false;
		}
		return false;
	}
*/
	/**
	 * Whether or not Backbone has defined anomer
	 * @param a_oBackbone
	 * @return true if backbone is open chain or anomeric symbol is not 'x'
	 */
	private boolean isDefinedAnomericState(Backbone a_oBackbone) {
		if ( this.hasUncertainAnomericPosition(a_oBackbone) ) return false;
		if ( a_oBackbone.getAnomericSymbol() == 'x' ) return false;
		return true;
	}

	private boolean isOpenChain(Backbone a_oBackbone) {
		if ( a_oBackbone.getAnomericPosition() != 0 ) return false;
		if ( this.hasUncertainAnomericPosition(a_oBackbone) ) return false;
		return true;
	}

	private boolean hasCarbonylGroup(Backbone a_oBackbone) {
		for ( BackboneCarbon t_oBC : a_oBackbone.getBackboneCarbons() ) {
			if ( t_oBC.getDesctriptor().getChar() == 'o' ) return true;
			if ( t_oBC.getDesctriptor().getChar() == 'O' ) return true;
		}
		return false;
	}

	private boolean hasUncertainAnomericPosition(Backbone a_oBackbone) {
		for ( BackboneCarbon t_oBC : a_oBackbone.getBackboneCarbons() ) {
			if ( t_oBC.getDesctriptor().getChar() == 'u' ) return true;
			if ( t_oBC.getDesctriptor().getChar() == 'U' ) return true;
		}
		return false;
	}
}
