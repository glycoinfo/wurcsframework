package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.glycoinfo.WURCSFramework.wurcs.WURCSException;

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

		throw new WURCSException("WURCSGraph seems not to have at least one root residue");
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
	 * @param a_objResidue
	 * @return
	 * @throws WURCSException
	 */
	public boolean removeBackbone(Backbone a_objResidue) throws WURCSException {
		WURCSEdge t_objLinkage;
		WURCSComponent t_objResidue;
		if ( a_objResidue == null )
			throw new WURCSException("Invalide residue.");
		// Search edges on the backbone
		for (Iterator<WURCSEdge> t_iterBackboneEdges = a_objResidue.getEdges().iterator(); t_iterBackboneEdges.hasNext();) {
			t_objLinkage = t_iterBackboneEdges.next();
			t_objResidue = t_objLinkage.getModification();
			if ( t_objResidue == null )
				throw new WURCSException("A linkage with a null modification exists.");
			// Remove edge
			t_objResidue.removeEdge(t_objLinkage);
			// Search edges on the connected modification
			for (Iterator<WURCSEdge> t_iterModificationEdges = a_objResidue.getEdges().iterator(); t_iterModificationEdges.hasNext();) {
				t_objLinkage = t_iterModificationEdges.next();
				t_objResidue = t_objLinkage.getBackbone();
				if ( t_objResidue == null )
					throw new WURCSException("A linkage with a null backbone exists.");
				// Remove edge
				t_objResidue.removeEdge(t_objLinkage);
			}
		}
		return this.m_aBackbones.remove(a_objResidue);
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
			throw new WURCSException("Invalide residue.");

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
			throw new WURCSException("Invalide residue.");
		if ( a_objLinkage == null )
			throw new WURCSException("Invalide linkage.");

		// For new Backbone
		if ( !this.m_aBackbones.contains(a_objBackbone) ) {
			a_objBackbone.removeAllEdges();
			this.m_aBackbones.add(a_objBackbone);
		}
		if ( !this.m_aBackbones.contains(a_objBackbone) )
			throw new WURCSException("Critical error imposible to add residue.");

		// For new Modification
		if ( !this.m_aModifications.contains(a_objModification) ) {
			a_objModification.removeAllEdges();
			this.m_aModifications.add(a_objModification);
		}
		if ( !this.m_aModifications.contains(a_objModification) )
			throw new WURCSException("Critical error imposible to add residue.");

/*
		// Check other linkage between the backbone and modification
		for ( WURCSEdge edge : a_objBackbone.getEdges() ) {
			if ( !edge.getModification().equals(a_objModification) ) continue;
			throw new WURCSException("The backbone and modification has already been connected.");
		}

		for ( WURCSEdge edge : a_objModification.getEdges() ) {
			if ( !edge.getBackbone().equals(a_objBackbone) ) continue;
			throw new WURCSException("The backbone and modification has already been connected.");
		}
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
		HashMap<Backbone, Backbone> t_hashOrigToCopy = new HashMap<Backbone, Backbone>();
		WURCSGraph copy = this.copy(t_hashOrigToCopy);
		return copy;
	}

	public WURCSGraph copy( HashMap<Backbone, Backbone> a_hashOrigToCopyBackbone ) throws WURCSException {
		WURCSGraph copy = new WURCSGraph();

		HashMap<Modification, Modification> t_hashOrigToCopyModification = new HashMap<Modification, Modification>();
		for ( Backbone t_origBack : this.m_aBackbones ) {
			Backbone t_copyBack = t_origBack.copy();
			a_hashOrigToCopyBackbone.put(t_origBack, t_copyBack);
			for ( WURCSEdge t_origEdge : t_origBack.getEdges() ) {
				Modification t_origModif = t_origEdge.getModification();
				if ( !t_hashOrigToCopyModification.containsKey(t_origModif) )
					t_hashOrigToCopyModification.put(t_origModif, t_origModif.copy());
				Modification t_copyModif = t_hashOrigToCopyModification.get(t_origModif);
				copy.addResidues( t_copyBack, t_origEdge.copy(), t_copyModif );
			}
		}

		return copy;
	}
}
