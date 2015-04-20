package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.util.LinkedList;

public class WURCSQuadruple {

	private String m_strGraph;
	private LinkedList<WURCSTriple> m_aTriples = new LinkedList<WURCSTriple>();

	public void setGraph(String a_strGraph) {
		this.m_strGraph = a_strGraph;
	}

	public String getGraph() {
		return this.m_strGraph;
	}

	public void addTriple(WURCSTriple a_oTriple) {
		this.m_aTriples.add(a_oTriple);
	}

	public LinkedList<WURCSTriple> getTriples() {
		return this.m_aTriples;
	}
}
