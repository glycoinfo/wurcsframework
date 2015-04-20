package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitable;

/**
 * Abstract class for component of Carbohydrate
 * @author MasaakiMatsubara
 *
 */
public abstract class WURCSComponent implements WURCSVisitable{

	/** Edges between Backbone and Modification */
	private LinkedList<WURCSEdge> m_aEdges = new LinkedList<WURCSEdge>();

	/**
	 * Add edge
	 * @param edge Edge
	 * @return true if addition is succeed
	 */
	public boolean addEdge( WURCSEdge edge ) {
		if ( this.m_aEdges.contains(edge) ) return false;
		return this.m_aEdges.add(edge);
	}

	/**
	 * Get list of edges
	 * @return list of edges
	 */
	public LinkedList<WURCSEdge> getEdges() {
		return this.m_aEdges;
	}

	/**
	 * Remove edge
	 * @param edge Edge
	 * @throws WURCSException
	 */
	public boolean removeEdge( WURCSEdge edge ) throws WURCSException {
		if ( edge == null )
			throw new WURCSException("Cant delete null linkage.");
		if ( !this.m_aEdges.contains(edge) ) return false;
		return this.m_aEdges.remove(edge);
	}

	/**
	 * Remove all edge
	 */
	public void removeAllEdges() {
		this.m_aEdges = new LinkedList<WURCSEdge>();
	}

	public abstract WURCSComponent copy();
}
