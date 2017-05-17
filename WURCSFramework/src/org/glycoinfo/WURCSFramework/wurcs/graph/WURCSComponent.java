package org.glycoinfo.WURCSFramework.wurcs.graph;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitable;

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
	 * Get list of parent edges
	 * @return list of parent edges
	 */
	public LinkedList<WURCSEdge> getParentEdges() {
		LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : this.m_aEdges ) {
			if ( !t_oEdge.getNextComponent().equals(this) ) continue;
			t_aEdges.addLast(t_oEdge);
		}
		return t_aEdges;
	}

	/**
	 * Get list of child edges
	 * @return list of child edges
	 */
	public LinkedList<WURCSEdge> getChildEdges() {
		LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();
		for ( WURCSEdge t_oEdge : this.m_aEdges ) {
			if ( t_oEdge.getNextComponent().equals(this) ) continue;
			t_aEdges.addLast(t_oEdge);
		}
		return t_aEdges;
	}

	/**
	 * Remove edge
	 * @param edge Edge
	 * @throws WURCSException
	 */
	public boolean removeEdge( WURCSEdge edge ) throws WURCSException {
		if ( edge == null )
			throw new WURCSException("Can not delete null linkage.");
		if ( !this.m_aEdges.contains(edge) ) return false;
		return this.m_aEdges.remove(edge);
	}

	/**
	 * Remove all edge
	 */
	public void removeAllEdges() {
		this.m_aEdges = new LinkedList<WURCSEdge>();
	}

	/**
	 * Check the node is a leaf
	 * @return true if this component is leaf
	 */
	public boolean isLeaf() {
		for ( WURCSEdge t_oEdge : this.m_aEdges ) {
			if ( !t_oEdge.getNextComponent().equals(this) ) return false;
		}
		return true;
	}

	/**
	 * Check the component is a root
	 * @return true if this component is root
	 */
	public boolean isRoot() {
		for ( WURCSEdge t_oEdge : this.m_aEdges ) {
			if ( t_oEdge.getNextComponent().equals(this) ) return false;
		}
		return true;
	}

	public abstract WURCSComponent copy();
}
