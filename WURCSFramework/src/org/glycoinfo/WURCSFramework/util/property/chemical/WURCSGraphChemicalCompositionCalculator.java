package org.glycoinfo.WURCSFramework.util.property.chemical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneUnknown;
import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

public class WURCSGraphChemicalCompositionCalculator extends ChemicalCompositionCalculatorAbstract {

	private WURCSGraph m_oGraph;
	private HashMap<Modification, Integer> m_mapModToValence = new HashMap<Modification, Integer>();

	public WURCSGraphChemicalCompositionCalculator( WURCSGraph a_oGraph ) {
		this.m_oGraph = a_oGraph;
		this.m_mapModToValence = new HashMap<Modification, Integer>();
	}

	@Override
	public void start() throws WURCSChemicalCompositionException {
		LinkedList<WURCSEdge> t_aEdges = new LinkedList<WURCSEdge>();

		// For backbones
		Iterator<Backbone> t_itB = this.m_oGraph.getBackboneIterator();
		while ( t_itB.hasNext() ) {
			Backbone t_oB = t_itB.next();

			this.addBackboneComposition(t_oB);

			t_aEdges.addAll( t_oB.getEdges() );
		}


		// For modifications with linkage
		Iterator<Modification> t_itM = this.m_oGraph.getModificationIterator();
		while ( t_itM.hasNext() ) {
			Modification t_oM = t_itM.next();

			this.addModificationComposition(t_oM);
		}

		// For edges
		int t_nSubstitution = 0;
		for ( WURCSEdge t_oEdge : t_aEdges ) {
			// Count edges for considering substitution of hydroxyl group
			t_nSubstitution++;

			// TODO: To consider substitution of carbonyl group "*=O"

			for ( LinkagePosition t_oLP : t_oEdge.getLinkages() ) {
				// Error if linkage has probabilities
				if ( t_oLP.getProbabilityUpper() != 1.0 && t_oLP.getProbabilityLower() != 1.0 )
					throw new WURCSChemicalCompositionException("Cannot calculate linkage with probability.");
			}
		}

		// Subtract number of hydroxyl group (O and H)
		this.addNumberOfElements( AtomicProperties.O , -t_nSubstitution );
		this.addNumberOfElements( AtomicProperties.H , -t_nSubstitution );
	}

	private void addBackboneComposition( Backbone a_oB ) throws WURCSChemicalCompositionException {

		// Error if Backbone is BackboneUnknown
		if ( a_oB instanceof BackboneUnknown )
			throw new WURCSChemicalCompositionException("Backbone having unknown repeated carbons is not handled.");

		// Count elements
		int t_nCarbons = 0;
		int t_nOxygens = 0;
		int t_nHydrogens = 0;
		for ( BackboneCarbon t_oBC : a_oB.getBackboneCarbons() ) {
			t_nCarbons++;

			boolean t_bIsTerminal = ( t_oBC.equals( a_oB.getBackboneCarbons().getFirst() ) || t_oBC.equals( a_oB.getBackboneCarbons().getLast() ) );
			char t_cSymbol = t_oBC.getDesctriptor().getChar();
			CarbonDescriptorChemicalComposition t_enumCDCC = CarbonDescriptorChemicalComposition.forCharacter(t_cSymbol, t_bIsTerminal);

			t_nOxygens += t_enumCDCC.getNumberOfOxgen();
			t_nHydrogens += t_enumCDCC.getNumberOfHydrogen();
		}

		// Add elements
		this.addNumberOfElements( AtomicProperties.C , t_nCarbons);
		this.addNumberOfElements( AtomicProperties.O , t_nOxygens);
		this.addNumberOfElements( AtomicProperties.H , t_nHydrogens);
	}

	private void addModificationComposition( Modification a_oM ) throws WURCSChemicalCompositionException {

		// Error if modification is repeat
		if ( a_oM instanceof InterfaceRepeat ) {
			throw new WURCSChemicalCompositionException("Cannot calculate repeating unit.");
		}

		String t_strMAP = a_oM.getMAPCode();
		// For omitted MAP
		if ( t_strMAP.equals("") ) {
			t_strMAP = "*O";
			if ( a_oM.getEdges().size() == 2 )
				t_strMAP = "*O*";
			if ( a_oM.getEdges().size() > 2 )
				throw new WURCSChemicalCompositionException("MAP must not be ommited for modification having three or more edges.");
		}

		MAPGraph t_oGraph;
		try {
			t_oGraph = (new MAPGraphImporter()).parseMAP(t_strMAP);
		} catch (WURCSFormatException e) {
			throw new WURCSChemicalCompositionException("Error in import MAP.", e);
		}
		MAPGraphChemicalCompositionCalculator t_oMAPCCC = new MAPGraphChemicalCompositionCalculator(t_oGraph);
		t_oMAPCCC.start();

		this.margeNumberOfElements( t_oMAPCCC );

		// Map modification and its valence
		this.m_mapModToValence.put( a_oM, t_oMAPCCC.getValence() );
	}
}
