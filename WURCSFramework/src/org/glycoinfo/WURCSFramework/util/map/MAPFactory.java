package org.glycoinfo.WURCSFramework.util.map;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.analysis.MAPGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

public class MAPFactory {

	private String m_strMAP;
	private MAPGraph m_oMAPGraph;

	public MAPFactory(String a_strMAP) throws WURCSFormatException {
		this( new MAPGraphImporter().parseMAP(a_strMAP) );
	}

	public MAPFactory(MAPGraph a_oMAPGraph) {
		this.m_oMAPGraph = this.normalizeGraph(a_oMAPGraph);
		this.m_strMAP = new MAPGraphExporter().getMAP(this.m_oMAPGraph);
	}

	public String getMAPString() {
		return this.m_strMAP;
	}

	public MAPGraph getMAPGrpah() {
		return this.m_oMAPGraph;
	}

	private MAPGraph normalizeGraph(MAPGraph a_oGraph) {
		MAPGraphNormalizer t_oNormGraph = new MAPGraphNormalizer(a_oGraph);
		t_oNormGraph.start();
		return t_oNormGraph.getNormalizedGraph();
	}
}
