package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

/**
 * Class for monosaccharide around a backbone
 * @author MasaakiMatsubara
 *
 */
public class Monosaccharide {

	private Backbone m_oCoreBackbone;

	public Monosaccharide(Backbone a_oBackbone) {
		this.m_oCoreBackbone = a_oBackbone;
	}

	public Backbone getBackbone() {
		return this.m_oCoreBackbone;
	}

	public boolean checkAroundAlternative() {
		for ( WURCSEdge t_oEdge : this.m_oCoreBackbone.getEdges() ) {
			if ( t_oEdge.getModification() instanceof ModificationAlternative ) return true;
		}
		return false;
	}

	public LinkedList<Modification> getRingModifications() {
		LinkedList<Modification> t_aRingMods = new LinkedList<Modification>();
		for ( WURCSEdge t_oEdge : this.m_oCoreBackbone.getEdges() ) {
			Modification t_oMod = t_oEdge.getModification();
			if ( t_oMod.isGlycosidic() ) continue;
			if ( t_oMod.getEdges().size() < 2 ) continue;
			if ( t_oEdge.getModification() instanceof InterfaceRepeat ) continue;
			t_aRingMods.add(t_oMod);
		}
		return t_aRingMods;
	}

	/**
	 * Get substituent edges
	 * @return List of WURCSEdge which are not glycosidic, single linkage and cannot omit MAP
	 */
	public LinkedList<WURCSEdge> getSubstituentEdges() {
		LinkedList<WURCSEdge> t_aModEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : this.m_oCoreBackbone.getChildEdges() ) {
			if ( t_oEdge.getModification().isGlycosidic() ) continue;
			if ( t_oEdge.getModification().getEdges().size() != 1 ) continue;
			if ( t_oEdge.getModification().canOmitMAP() ) continue;
			t_aModEdges.add(t_oEdge);
		}
		return t_aModEdges;
	}

	public LinkedList<WURCSEdge> getChildGlycosidicEdges() {
		LinkedList<WURCSEdge> t_aModEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : this.m_oCoreBackbone.getChildEdges() ) {
			if ( !t_oEdge.getModification().isGlycosidic() ) continue;
			if ( t_oEdge.equals(this.m_oCoreBackbone.getAnomericEdge()) ) continue;

			t_aModEdges.add(t_oEdge);
		}
		return t_aModEdges;
	}
}
