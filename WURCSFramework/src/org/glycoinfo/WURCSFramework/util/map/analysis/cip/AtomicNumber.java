package org.glycoinfo.WURCSFramework.util.map.analysis.cip;

import java.util.HashMap;

import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomGroup;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

/**
 * Class for calculating atomic number for MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class AtomicNumber {

	private HashMap<MAPAtomAbstract, Double> m_mapAtomToAdditionalWeight;

	public AtomicNumber() {
		this.m_mapAtomToAdditionalWeight = new HashMap<MAPAtomAbstract, Double>();
	}

	/**
	 * Set additional weight for MAPAtomAbstract to use in comparison of atoms
	 * @param a_oAtom MAPAtomAbstract of weighted atom
	 * @param a_dWeight Double of additional weight
	 */
	public void setAdditionalWeight(MAPAtomAbstract a_oAtom, double a_dWeight ) {
		this.m_mapAtomToAdditionalWeight.put(a_oAtom, a_dWeight);
	}

	/**
	 * Get atomic number for comparing priority of atoms
	 * @param a_oAtom MAPAtomAbstract of target atom
	 * @return Double of calculated atomic number
	 */
	public double getAtomicNumber(MAPAtomAbstract a_oAtom) {
		double t_dAtomicNumber = 0.0D;

		// For atom group number XXX: to consider it should be large or small number
		if ( a_oAtom instanceof MAPAtomGroup ) return 999.9D;

		AtomicProperties t_enumAP = AtomicProperties.forSymbol( a_oAtom.getSymbol() );
		t_dAtomicNumber = t_enumAP.getAtomicNumber();

		// Add small number for star
		if ( a_oAtom instanceof MAPStar )
			t_dAtomicNumber += 0.01D;

		// Add aditional weight
		if ( this.m_mapAtomToAdditionalWeight.containsKey(a_oAtom) )
			t_dAtomicNumber += this.m_mapAtomToAdditionalWeight.get(a_oAtom);

			return t_dAtomicNumber;
	}
}
