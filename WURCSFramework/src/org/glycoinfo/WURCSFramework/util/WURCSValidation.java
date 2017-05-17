package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToGraph;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSGraphToArray;
import org.glycoinfo.WURCSFramework.util.graph.WURCSGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSValidation {

	private LinkedList<String> m_aErrors = new LinkedList<String>();
	private LinkedList<String> m_aWarnings = new LinkedList<String>();

	public LinkedList<String> getErrors() {
		return this.m_aErrors;
	}

	public LinkedList<String> getWarnings() {
		return this.m_aWarnings;
	}

	public void start(String a_strWURCS) {
		WURCSExporter t_oExporter = new WURCSExporter();
		WURCSImporter t_oImporter = new WURCSImporter();
		try {
			// Extract WURCS
			WURCSArray t_oWURCS = t_oImporter.extractWURCSArray(a_strWURCS);

			// Export WURCS string
			String t_strSortLIN = t_oExporter.getWURCSString(t_oWURCS);

			// To Graph
			WURCSArrayToGraph t_oA2G = new WURCSArrayToGraph();
			t_oA2G.start(t_oWURCS);
			WURCSGraph t_oGraph = t_oA2G.getGraph();

			// Normalize
			WURCSGraphNormalizer t_oNorm = new WURCSGraphNormalizer();
			t_oNorm.start(t_oGraph);

			// To Array
			WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
			t_oG2A.start(t_oGraph);
			t_oWURCS = t_oG2A.getWURCSArray();

			// Export WURCS string again
			String t_strSortGraph = t_oExporter.getWURCSString(t_oWURCS);

			// Check change strings
			if ( a_strWURCS.equals(t_strSortGraph) ) return;
			if ( ! a_strWURCS.equals(t_strSortLIN) ) this.m_aWarnings.add("LIN or MOD has sorted.");

			if ( t_strSortLIN.equals(t_strSortGraph) ) return;
			if ( t_oNorm.isInverted()              ) this.m_aWarnings.add("Backbone has inverted.");
			if ( t_oNorm.linkedAnomericPositions() ) this.m_aWarnings.add("Two monosaccharide which are connected each anomeric position has sorted.");
			if ( t_oNorm.hasCyclic()               ) this.m_aWarnings.add("Cyclic region has sorted.");
			if ( t_oNorm.isExpandedRepeatingUnit() ) this.m_aWarnings.add("Repeating unit has expanded.");

			if ( !t_oNorm.isInverted() && !t_oNorm.linkedAnomericPositions() && !t_oNorm.hasCyclic() && !t_oNorm.isExpandedRepeatingUnit() )
				this.m_aWarnings.add("WURCS has sorted for some reason.");

		}catch (WURCSFormatException e) {
			this.m_aErrors.add( e.getErrorMessage() );
		} catch (WURCSException e) {
			this.m_aErrors.add( e.getErrorMessage() );
		}
	}
}
