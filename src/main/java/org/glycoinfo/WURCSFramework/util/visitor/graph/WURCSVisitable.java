package org.glycoinfo.WURCSFramework.util.visitor.graph;


public interface WURCSVisitable {
    public void accept (WURCSVisitor a_objVisitor) throws WURCSVisitorException;

}
