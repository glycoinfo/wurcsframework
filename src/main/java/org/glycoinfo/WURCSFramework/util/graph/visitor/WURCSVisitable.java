package org.glycoinfo.WURCSFramework.util.graph.visitor;


public interface WURCSVisitable {
    public void accept (WURCSVisitor a_objVisitor) throws WURCSVisitorException;

}
