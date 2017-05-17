package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorSeparateWURCSGraphByAglycone;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSFactoryForAglycone extends WURCSFactory {

	private boolean m_bHasAglycone = true;
	private LinkedList<String> m_aSeparatedWURCSs = new LinkedList<String>();
	private LinkedList<String> m_aAglycones = new LinkedList<String>();
	private LinkedList<String> m_aSeparetedWURCSsWithAglycone = new LinkedList<String>();
	private LinkedList<String> m_aStandardWURCSs = new LinkedList<String>();

	public WURCSFactoryForAglycone(String a_strWURCS) throws WURCSException {
		super(a_strWURCS);
		this.initialize();
	}

	public WURCSFactoryForAglycone(WURCSArray a_oArray) throws WURCSException {
		super(a_oArray);
		this.initialize();
	}

	public WURCSFactoryForAglycone(WURCSGraph a_oGraph) throws WURCSException {
		super(a_oGraph);
		this.initialize();
	}

	public boolean hasAglycone() {
		return this.m_bHasAglycone;
	}

	public LinkedList<String> getSeparatedWURCSs() {
		return this.m_aSeparatedWURCSs;
	}

	public LinkedList<String> getSeparatedAglycones() {
		return this.m_aAglycones;
	}

	public LinkedList<String> getSeparatedWURCSsWithAglycone() {
		return this.m_aSeparetedWURCSsWithAglycone;
	}

	public LinkedList<String> getStandardWURCSs() {
		return this.m_aStandardWURCSs;
	}

	private void initialize() throws WURCSException {
		// Get original graph
		WURCSGraph t_oGraph = this.getGraph();

		// Treatment aglycone
		WURCSVisitorSeparateWURCSGraphByAglycone t_oSeparateGraph = new WURCSVisitorSeparateWURCSGraphByAglycone();
		t_oSeparateGraph.start(t_oGraph);

		// Return if no aglycone
		if ( t_oSeparateGraph.getAglycones().isEmpty() ) {
			this.m_bHasAglycone = false;
			return;
		}

		// For aglycone separated
		for ( WURCSGraph t_oSepGraph : t_oSeparateGraph.getSeparatedGraphs() ) {
			// Generate separated WURCS
			WURCSFactory t_oSepFactory = new WURCSFactory(t_oSepGraph);
			String t_strSepWURCS = t_oSepFactory.getWURCS();
			this.m_aSeparatedWURCSs.addLast(t_strSepWURCS);

			// Set separated aglycone (distinct)
			LinkedList<String> t_aUniqueAbbrs = new LinkedList<String>();
			String t_strAglycone = "";
			for ( Modification t_oAglycone : t_oSeparateGraph.getMapSeparatedGraphToAglycones().get(t_oSepGraph) ) {
				String t_strAbbr = t_oSeparateGraph.getMapAglyconeToAbbr().get(t_oAglycone);
				if ( t_aUniqueAbbrs.contains(t_strAbbr) ) continue;
				t_aUniqueAbbrs.add(t_strAbbr);
				String t_strAglyconeAbbr = t_strAbbr+": "+t_oAglycone.getMAPCode();
				t_strAglycone += "\t"+t_strAglyconeAbbr;
				if ( this.m_aAglycones.contains(t_strAglyconeAbbr) ) continue;
				this.m_aAglycones.addLast(t_strAglyconeAbbr);
			}
			this.m_aSeparetedWURCSsWithAglycone.addLast( t_strSepWURCS+t_strAglycone );
		}

		// For standard (remain one atom aglycone)
		for ( WURCSGraph t_oSepGraphOneAtom : t_oSeparateGraph.getSeparatedGraphsWithOneAtom() ) {
			WURCSFactory t_oSepFactory = new WURCSFactory(t_oSepGraphOneAtom);
			String t_strStdWURCS = t_oSepFactory.getWURCS();
			this.m_aStandardWURCSs.addLast(t_strStdWURCS);
		}

	}
}
