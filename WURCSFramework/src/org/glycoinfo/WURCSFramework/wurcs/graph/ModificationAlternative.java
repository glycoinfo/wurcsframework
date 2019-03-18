package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.comparator.WURCSEdgeComparator;

/**
 * Class for modification at alternative linkage
 * @author MasaakiMatsubara
 *
 */
public class ModificationAlternative extends Modification {

	public ModificationAlternative(String MAPCode) {
		super(MAPCode);
	}

	private LinkedList<WURCSEdge> m_aLeadInEdges  = new LinkedList<WURCSEdge>();
	private LinkedList<WURCSEdge> m_aLeadOutEdges = new LinkedList<WURCSEdge>();

	public void addLeadInEdge(WURCSEdge a_oInEdge) {
		this.m_aLeadInEdges.addLast(a_oInEdge);
	}

	public LinkedList<WURCSEdge> getLeadInEdges() {
		return this.m_aLeadInEdges;
	}

	public void addLeadOutEdge(WURCSEdge a_oOutEdge) {
		this.m_aLeadOutEdges.addLast(a_oOutEdge);
	}

	public LinkedList<WURCSEdge> getLeadOutEdges() {
		return this.m_aLeadOutEdges;
	}

	/**
	 * Return true if this is composition linkages like following:
	 * <p> a?|b?|c?}-{a?|b?|c? </p>
	 * This must have the same linkages between lead in and out linkages and no substituent.
	 * The linkages must be connected to all of the monosaccharides in the glycan.
	 * @author Masaaki Matsubara
	 */
	public boolean isGlycosidicLinkageForComposition() {
		if ( this.m_aLeadInEdges.isEmpty() || this.m_aLeadOutEdges.isEmpty() )
			return false;
		if ( this.m_aLeadInEdges.size() != this.m_aLeadOutEdges.size())
			return false;
		/* Compare lead in and lead out linkages on the ModificationAlternative  */
		LinkedList<WURCSEdge> t_lInEdges  = this.m_aLeadInEdges;
		LinkedList<WURCSEdge> t_lOutEdges = this.m_aLeadOutEdges;
		int nEdges = t_lInEdges.size();
		WURCSEdgeComparator t_compEdges = new WURCSEdgeComparator();
		Collections.sort(t_lInEdges,  t_compEdges);
		Collections.sort(t_lOutEdges, t_compEdges);
		for ( int i=0; i<nEdges; i++) {
			WURCSEdge t_edgeIn  = t_lInEdges.get(i);
			WURCSEdge t_edgeOut = t_lOutEdges.get(i);
			int t_iComp = t_compEdges.compare(t_edgeIn, t_edgeOut);
			if ( t_iComp != 0 )
				return false;
		}
		return true;
	}

	/**
	 * Return true if this have like the following substituent linkages:
	 * <p> a?|b?|c?}*OCC/3=O </p>
	 * This must have lead in linkages, substituent and no lead out linkages.
	 * The linkages must be connected to all of the monosaccharides in the glycan.
	 * @author Masaaki Matsubara
	 */
	public boolean isUnderdeteminedSubstituent() {
		if ( this.m_aLeadInEdges.isEmpty() || !this.m_aLeadOutEdges.isEmpty() )
			return false;
		if ( this.getMAPCode().isEmpty() )
			return false;
		return true;
	}

	@Override
	public boolean isRing() {
		return false;
	}

	public ModificationAlternative copy() {
		return new ModificationAlternative(this.getMAPCode());
	}

}
