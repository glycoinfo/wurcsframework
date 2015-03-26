package org.glycoinfo.WURCSFramework.util.visitor.graph;

import org.glycoinfo.WURCSFramework.graph.Backbone;
import org.glycoinfo.WURCSFramework.graph.Modification;
import org.glycoinfo.WURCSFramework.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.graph.WURCSGraph;

/**
 * Interface of WURCSVisitor
 * @author MasaakiMatsubara
 *
 */
public interface WURCSVisitor {
	public void visit( Backbone                a_objBackbone                ) throws WURCSVisitorException;
	public void visit( Modification            a_objModification            ) throws WURCSVisitorException;

	public void visit( WURCSEdge a_objWURCSEdge ) throws WURCSVisitorException;

	public void start( WURCSGraph a_objGraph ) throws WURCSVisitorException;

	public WURCSGraphTraverser getTraverser( WURCSVisitor a_objVisitor ) throws WURCSVisitorException;

	public void clear();

}
