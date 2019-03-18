package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.glycoinfo.WURCSFramework.util.WURCSException;

/**
 * Class for WURCSGraph
 * @author MasaakiMatsubara
 *
 */
public class WURCSGraph {

	/** Contained Backbones */
	private ArrayList<Backbone> m_aBackbones = new ArrayList<Backbone>();
	/** Contained Modifications */
	private ArrayList<Modification> m_aModifications = new ArrayList<Modification>();

	public ArrayList<Backbone> getRootBackbones() throws WURCSException {
		ArrayList<Backbone> t_aResult = new ArrayList<Backbone>();

		Backbone t_objBackbone;
		// for all residues of the glycan
		Iterator<Backbone> t_iterBackbone = this.getBackboneIterator();
		while (t_iterBackbone.hasNext())
		{
			t_objBackbone = t_iterBackbone.next();

			boolean t_bIsRoot = true;
			for ( WURCSEdge t_oEdge : t_objBackbone.getEdges() ) {
				if ( !t_oEdge.isReverse() ) continue;
				t_bIsRoot = false;
			}
			if (t_bIsRoot) t_aResult.add(t_objBackbone);
		}
		if ( t_aResult.size() > 0 ) return t_aResult;

		throw new WURCSException("WURCSGraph seems not to have at least one root residue in class WURCSGraph(getRootBackbones)");
	}

	/**
	 *
	 * @return
	 */
	public Iterator<Backbone> getBackboneIterator()
	{
		return this.m_aBackbones.iterator();
	}

	public Iterator<Modification> getModificationIterator()
	{
		return this.m_aModifications.iterator();
	}

	/**
	 *
	 * @return
	 * @throws WURCSException
	 */
	public boolean isConnected() throws WURCSException
	{
		ArrayList<Backbone> t_objRoots = this.getRootBackbones();
		if ( t_objRoots.size() > 1 )
		{
			return false;
		}
		return true;
	}

	/**
	 * Remove backbone
	 * @param a_objBackbone
	 * @return true if succeeded
	 * @throws WURCSException
	 */
	public boolean removeBackbone(Backbone a_objBackbone) throws WURCSException {
		this.removeEdgesAroundComponent(a_objBackbone);
		return this.m_aBackbones.remove(a_objBackbone);
	}

	/**
	 * Remove modification
	 * @param a_objModification
	 * @return true if succeeded
	 * @throws WURCSException
	 */
	public boolean removeModification(Modification a_objModification) throws WURCSException {
		this.removeEdgesAroundComponent(a_objModification);
		return this.m_aModifications.remove(a_objModification);
	}

	private void removeEdgesAroundComponent(WURCSComponent a_objResidue) throws WURCSException {
		WURCSEdge t_objLinkage;
		WURCSComponent t_objResidue;
		if ( a_objResidue == null )
			throw new WURCSException("Invalide residue in class WURCSGraph(removeEdgesAroundComponent).");
		// Search edges on the modification
		while ( !a_objResidue.getEdges().isEmpty() ) {
			t_objLinkage = a_objResidue.getEdges().getFirst();
			t_objResidue = t_objLinkage.getBackbone();
			if ( t_objResidue == null )
				throw new WURCSException("A linkage with a null residue exists in class WURCSGraph(removeEdgesAroundComponent).");
			// Remove edge
			t_objResidue.removeEdge(t_objLinkage);
			a_objResidue.removeEdge(t_objLinkage);
		}
	}

	/**
	 * Get all Backbones
	 * @return
	 */
	public ArrayList<Backbone> getBackbones() {
		return this.m_aBackbones;
	}

	/**
	 * Get all Modifications
	 * @return
	 */
	public ArrayList<Modification> getModifications() {
		return this.m_aModifications;
	}

	/**
	 * Add Backbone
	 * @param a_objResidue
	 * @return
	 * @throws WURCSException
	 */
	public boolean addBackbone(Backbone a_objResidue) throws WURCSException {
		if ( a_objResidue == null )
			throw new WURCSException("Invalide residue in class WURCSGraph(addBAckbone).");

		if ( this.m_aBackbones.contains(a_objResidue) ) return false;
		a_objResidue.removeAllEdges();
		return this.m_aBackbones.add(a_objResidue);
	}

	/**
	 * Add Backbone and connected modification
	 * @param a_objBackbone
	 * @param a_objLinkage
	 * @param a_objModification
	 * @return
	 * @throws WURCSException
	 */
	public boolean addResidues(Backbone a_objBackbone, WURCSEdge a_objLinkage, Modification a_objModification) throws WURCSException {
		if ( a_objBackbone == null || a_objModification == null )
			throw new WURCSException("Invalide residue in class WURCSGraph(addResidues).");
		if ( a_objLinkage == null )
			throw new WURCSException("Invalide linkage in class WURCSGraph(addResidues).");

		// For new Backbone
		if ( !this.m_aBackbones.contains(a_objBackbone) ) {
			a_objBackbone.removeAllEdges();
			this.m_aBackbones.add(a_objBackbone);
		}
		if ( !this.m_aBackbones.contains(a_objBackbone) )
			throw new WURCSException("Critical error imposible to add residue in class WURCSGraph(addResidues).");

		// For new Modification
		if ( !this.m_aModifications.contains(a_objModification) ) {
			a_objModification.removeAllEdges();
			this.m_aModifications.add(a_objModification);
		}
		if ( !this.m_aModifications.contains(a_objModification) )
			throw new WURCSException("Critical error imposible to add residue in class WURCSGraph(addResidues).");

/*
//		// Check other linkage between the backbone and modification
//		for ( WURCSEdge edge : a_objBackbone.getEdges() ) {
//			if ( !edge.getModification().equals(a_objModification) ) continue;
//			throw new WURCSException("The backbone and modification has already been connected.");
//		}
//
//		for ( WURCSEdge edge : a_objModification.getEdges() ) {
//			if ( !edge.getBackbone().equals(a_objBackbone) ) continue;
//			throw new WURCSException("The backbone and modification has already been connected.");
//		}
*/
		// Test for indirect cyclic structures
//		if ( this.isParent(a_objModification,a_objBackbone) )
//		{
//			return this.addCyclic(a_objBackbone,a_objLinkage,a_objModification);
//		}

		a_objModification.addEdge(a_objLinkage);
		a_objBackbone.addEdge(a_objLinkage);
		a_objLinkage.setModification(a_objModification);
		a_objLinkage.setBackbone(a_objBackbone);

		return true;
	}

	public WURCSGraph copy() throws WURCSException {
		HashMap<Backbone, Backbone> t_hashOrigToCopyB = new HashMap<Backbone, Backbone>();
		HashMap<Modification, Modification> t_hashOrigToCopyM = new HashMap<Modification, Modification>();
		WURCSGraph copy = this.copy(t_hashOrigToCopyB, t_hashOrigToCopyM);
		return copy;
	}

	public WURCSGraph copy( HashMap<Backbone, Backbone> a_hashOrigToCopyB ) throws WURCSException {
		HashMap<Modification, Modification> t_hashOrigToCopyM = new HashMap<Modification, Modification>();
		WURCSGraph copy = this.copy(a_hashOrigToCopyB, t_hashOrigToCopyM);
		return copy;
	}

	public WURCSGraph copy( HashMap<Backbone, Backbone> a_hashOrigToCopyBackbone, HashMap<Modification, Modification> a_hashOrigToCopyModification ) throws WURCSException {
		WURCSGraph copy = new WURCSGraph();

		for ( Backbone t_origBack : this.m_aBackbones ) {
			Backbone t_copyBack = t_origBack.copy();
			a_hashOrigToCopyBackbone.put(t_origBack, t_copyBack);
			for ( WURCSEdge t_origEdge : t_origBack.getEdges() ) {
				Modification t_origModif = t_origEdge.getModification();
				if ( !a_hashOrigToCopyModification.containsKey(t_origModif) )
					a_hashOrigToCopyModification.put(t_origModif, t_origModif.copy());
				Modification t_copyModif = a_hashOrigToCopyModification.get(t_origModif);
				WURCSEdge t_copyEdge = t_origEdge.copy();
				copy.addResidues( t_copyBack, t_copyEdge, t_copyModif );

				// For alternative unit copy
				if ( !(t_origModif instanceof ModificationAlternative ) ) continue;
				// For lead in edges
				if ( ((ModificationAlternative)t_origModif).getLeadInEdges().contains( t_origEdge ) )
					((ModificationAlternative)t_copyModif).addLeadInEdge(t_copyEdge);
				// For lead out edges
				if ( ((ModificationAlternative)t_origModif).getLeadOutEdges().contains( t_origEdge ) )
					((ModificationAlternative)t_copyModif).addLeadOutEdge(t_copyEdge);
			}
			// For no edge Backbone
			if ( t_origBack.getEdges().isEmpty() )
				copy.addBackbone(t_copyBack);
		}

		return copy;
	}
// repeat and cyclic is not implemented in yet.
	public void dump() {
//		System.out.print("Dump>");
		for (Backbone t_objBackbone : this.getBackbones()) {
			System.out.print(this.getBackbones().indexOf(t_objBackbone)+","+t_objBackbone.getSkeletonCode()+"-"+t_objBackbone.getAnomericPosition()+t_objBackbone.getAnomericSymbol()+",");
			for(WURCSEdge t_objEdge : t_objBackbone.getEdges()) {
				System.out.print("["+this.getBackbones().indexOf(t_objEdge.getBackbone())+"-"+this.getModifications().indexOf(t_objEdge.getModification()));
				if(t_objEdge.isReverse()) {System.out.print(",r");} else {System.out.print(",n");};
				if(t_objEdge.isAnomeric()) {System.out.print(",a");} else {System.out.print(",n");};
				for(LinkagePosition t_objLinkage : t_objEdge.getLinkages()) {
					System.out.print("<"+t_objLinkage.getBackbonePosition()+","+t_objLinkage.getModificationPosition()+",");
					System.out.print(t_objLinkage.getProbabilityLower()+"-"+t_objLinkage.getProbabilityUpper()+">");
					}
				System.out.print("]");
				}
//			System.out.println();
			}
		for (Modification t_objModification : this.getModifications()) {
			System.out.print(this.getModifications().indexOf(t_objModification)+t_objModification.getMAPCode()+",");
			for(WURCSEdge t_objEdge : t_objModification.getEdges()) {
				System.out.print("["+this.getModifications().indexOf(t_objEdge.getModification())+"-"+this.getBackbones().indexOf(t_objEdge.getBackbone()));
				if(t_objEdge.isReverse()) {System.out.print(",r");} else {System.out.print(",n");};
				for(LinkagePosition t_objLinkage : t_objEdge.getLinkages()) {
					System.out.print("<"+t_objLinkage.getBackbonePosition()+","+t_objLinkage.getModificationPosition()+",");
					System.out.print(t_objLinkage.getProbabilityLower()+"-"+t_objLinkage.getProbabilityUpper()+">");
					}
				System.out.print("]");
				}
//			System.out.println();
			}
	}

}
