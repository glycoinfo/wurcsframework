package org.glycoinfo.WURCSFramework.util.map.analysis;

import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomGroup;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;

public class MorganAlgorithmWithAtomTypeForMAP extends MorganAlgorithmForMAP {

	public MorganAlgorithmWithAtomTypeForMAP(MAPGraph a_oGraph) {
		super(a_oGraph);
	}

	@Override
	protected int getAtomWeight( MAPAtomAbstract a_oAtom ) {
		// 0 for cyclic
		if ( a_oAtom instanceof MAPAtomCyclic )
			return 0;
		// 999 for atom group
		if ( a_oAtom instanceof MAPAtomGroup )
			return 999;
		AtomicProperties t_enumAP = AtomicProperties.forSymbol( a_oAtom.getSymbol() );
		// 999 for unknown atom
		if ( t_enumAP == null )
			return 999;
		// Atomic number for normal atom
		return t_enumAP.getAtomicNumber();
	}
}
