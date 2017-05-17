package org.glycoinfo.WURCSFramework.util.property.chemical;

import org.glycoinfo.WURCSFramework.util.map.analysis.ValenceBondCalculator;
import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtom;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomGroup;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

/**
 * Calculate MAPGraph chemical compositions
 * @author MasaakiMatsubara
 *
 */
public class MAPGraphChemicalCompositionCalculator extends ChemicalCompositionCalculatorAbstract {

	private MAPGraph m_oGraph;
	private int m_iValence;

	public MAPGraphChemicalCompositionCalculator( MAPGraph a_oGraph ) {
		this.m_oGraph = a_oGraph;
	}

	public int getValence() {
		return this.m_iValence;
	}

	@Override
	public void start() throws WURCSChemicalCompositionException {
		int t_nHiddenHydrogens = 0;
		ValenceBondCalculator t_oVBCalc = new ValenceBondCalculator(this.m_oGraph);
		for ( MAPAtomAbstract t_oAtomAb : this.m_oGraph.getAtoms() ) {
			// Error if MAP contains atom group (R)
			if ( t_oAtomAb instanceof MAPAtomGroup )
				throw new WURCSChemicalCompositionException("Atomic group is not handled.");
			// Ignore cyclic atom
			if ( t_oAtomAb instanceof MAPAtomCyclic )
				continue;

			MAPAtom t_oAtom = (MAPAtom)t_oAtomAb;

			// Count hidden hydrogens
			t_nHiddenHydrogens += t_oVBCalc.countRemainingValence(t_oAtom);

			// Count valence of the MAP
			if ( t_oAtom instanceof MAPStar ) {
				this.m_iValence += t_oAtom.getValence();
				continue;
			}

			// Add element
			AtomicProperties t_enumAP = AtomicProperties.forSymbol( t_oAtom.getSymbol() ) ;
			// Error if unknown symbol
			if ( t_enumAP == null )
				throw new WURCSChemicalCompositionException("Unknown atom symbol is not handled.");

			// Count element
			this.addNumberOfElements(t_enumAP, 1);
		}
		if ( t_nHiddenHydrogens > 0 );
			this.addNumberOfElements( AtomicProperties.H, t_nHiddenHydrogens );
	}
}
