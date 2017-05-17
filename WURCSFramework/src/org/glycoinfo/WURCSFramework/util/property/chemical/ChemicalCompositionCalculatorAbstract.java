package org.glycoinfo.WURCSFramework.util.property.chemical;

import java.util.HashMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;

public abstract class ChemicalCompositionCalculatorAbstract {

	private HashMap<AtomicProperties, Integer> m_mapAtomToNumber;

	public ChemicalCompositionCalculatorAbstract() {
		this.m_mapAtomToNumber = new HashMap<AtomicProperties, Integer>();
	}

	public void addNumberOfElements( AtomicProperties a_enumAP, int a_nCount ) {
		int t_nCount = a_nCount;
		if ( this.m_mapAtomToNumber.containsKey(a_enumAP) )
			t_nCount += this.m_mapAtomToNumber.get(a_enumAP);
		this.m_mapAtomToNumber.put(a_enumAP, t_nCount);
	}

	public void margeNumberOfElements( ChemicalCompositionCalculatorAbstract a_oComp ) {
		HashMap<AtomicProperties, Integer> t_mapAtomToNumber = a_oComp.getNumberOfElements();
		for ( AtomicProperties t_enumAP : t_mapAtomToNumber.keySet() ) {
			this.addNumberOfElements(t_enumAP, t_mapAtomToNumber.get( t_enumAP ) );
		}
	}

	public HashMap<AtomicProperties, Integer> getNumberOfElements() {
		return this.m_mapAtomToNumber;
	}

	/**
	 * Get string of chemical composition
	 * @return String of chemical composition
	 */
	public String getComposition() {
		String t_strComposition = "";

		// List carbon composition
		if ( this.m_mapAtomToNumber.containsKey( AtomicProperties.C ) )
			t_strComposition += "C"+this.m_mapAtomToNumber.get( AtomicProperties.C );
		// List hydrogen composition
		if ( this.m_mapAtomToNumber.containsKey( AtomicProperties.H ) )
			t_strComposition += "H"+this.m_mapAtomToNumber.get( AtomicProperties.H );

		// List other element symbols ordering alphabetically
		TreeSet<String> t_aSymbols = new TreeSet<String>();
		for ( AtomicProperties t_enumAP : this.m_mapAtomToNumber.keySet() ) {
			if ( t_enumAP == AtomicProperties.C ) continue;
			if ( t_enumAP == AtomicProperties.H ) continue;
			t_aSymbols.add( t_enumAP.getSymbol() );
		}
		for ( String t_strSymbol : t_aSymbols )
			t_strComposition += t_strSymbol+this.m_mapAtomToNumber.get( AtomicProperties.forSymbol(t_strSymbol) );

		return t_strComposition;
	}

	public abstract void start() throws WURCSChemicalCompositionException;
}
