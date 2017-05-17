package org.glycoinfo.WURCSFramework.util;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToGraph;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence2;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSGraphToArray;
import org.glycoinfo.WURCSFramework.util.graph.WURCSGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;

/**
 * Factory class of WURCS objects
 * @author MasaakiMatsubara
 *
 */
public class WURCSFactory {

	private String m_strWURCS;
	private WURCSArray m_oArray;
	private WURCSGraph m_oGraph;
	private WURCSSequence2 m_oSeq;

	public WURCSFactory(String a_strWURCS) throws WURCSException {
		this.parseString(a_strWURCS);
	}

	public WURCSFactory(WURCSArray a_oArray) throws WURCSException {
		this.convertArrayToGraph(a_oArray);
	}

	public WURCSFactory(WURCSGraph a_oGraph) throws WURCSException {
		this.setWURCSObjects(a_oGraph);
	}

	public String getWURCS() {
		return this.m_strWURCS;
	}

	public WURCSArray getArray() {
		return this.m_oArray;
	}

	public WURCSGraph getGraph() {
		return this.m_oGraph;
	}

	public WURCSSequence2 getSequence() {
		// Convert array to sequence
		// TODO: make graph to sequence system
		WURCSArrayToSequence2 t_oA2S = new WURCSArrayToSequence2();
		t_oA2S.start(this.m_oArray);
		this.m_oSeq = t_oA2S.getSequence();

		return this.m_oSeq;
	}

	private void parseString(String a_strWURCS) throws WURCSException {
		WURCSImporter t_oImportArray = new WURCSImporter();
		WURCSArray t_oArray = t_oImportArray.extractWURCSArray(a_strWURCS);

		this.convertArrayToGraph(t_oArray);
	}

	private void convertArrayToGraph(WURCSArray a_oArray) throws WURCSException {
		WURCSArrayToGraph t_oA2G = new WURCSArrayToGraph();
		t_oA2G.start(a_oArray);
		WURCSGraph t_oGraph = t_oA2G.getGraph();
		this.setWURCSObjects(t_oGraph);
	}

	private void setWURCSObjects(WURCSGraph a_oGraph) throws WURCSException {
		// Set graph
		this.m_oGraph = a_oGraph;

		// Normalize graph
		WURCSGraphNormalizer t_oNorm = new WURCSGraphNormalizer();
		t_oNorm.start( this.m_oGraph );

		// Convert graph to array
		WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
		t_oG2A.start(a_oGraph);
		WURCSArray t_oArray = t_oG2A.getWURCSArray();
		this.m_oArray = t_oArray;

		// Convert array to string
		WURCSExporter t_oExport = new WURCSExporter();
		this.m_strWURCS = t_oExport.getWURCSString(t_oArray);
	}

}
