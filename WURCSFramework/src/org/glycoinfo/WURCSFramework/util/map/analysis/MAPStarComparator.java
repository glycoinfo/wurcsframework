package org.glycoinfo.WURCSFramework.util.map.analysis;

import java.util.Comparator;
import java.util.HashMap;

import org.glycoinfo.WURCSFramework.util.map.analysis.cip.MAPConnectionComparatorUsingCIPSystem;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;


/**
 * Comparator for MAPStars in a MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class MAPStarComparator implements Comparator<MAPStar>{

	private MAPGraph m_oGraph;
	private HashMap<MAPAtomAbstract, Integer> m_mapAtomToMorganNumber;
	private HashMap<MAPAtomAbstract, Integer> m_mapAtomToMorganNumberWithAtomType;
	private MAPConnectionComparatorUsingCIPSystem m_oCIPComp;

	public MAPStarComparator(MAPGraph a_oGraph) {
		this.m_oGraph = a_oGraph;
		// Calc initial Morgan number
		MorganAlgorithmForMAP t_oMA = new MorganAlgorithmForMAP(this.m_oGraph);
		t_oMA.calcMorganNumber(null, null);
		this.m_mapAtomToMorganNumber = t_oMA.getAtomToMorganNumber();
		// Calc initial Morgan number with atom type
		t_oMA = new MorganAlgorithmWithAtomTypeForMAP(this.m_oGraph);
		t_oMA.calcMorganNumber(null, null);
		this.m_mapAtomToMorganNumberWithAtomType = t_oMA.getAtomToMorganNumber();

		// Set CIP order calculator
		this.m_oCIPComp = new MAPConnectionComparatorUsingCIPSystem(a_oGraph);
	}

	@Override
	public int compare(MAPStar a_oStar1, MAPStar a_oStar2) {

		int t_iComp = 0;

		// Compare Morgan number, lower number is prior than higher one
		int t_iMorganNum1 = this.m_mapAtomToMorganNumber.get(a_oStar1);
		int t_iMorganNum2 = this.m_mapAtomToMorganNumber.get(a_oStar2);
		t_iComp = t_iMorganNum1 - t_iMorganNum2;
		if ( t_iComp != 0 ) return t_iComp;

		// Compare Morgan number with atom type, lower number is prior than higher one
		t_iMorganNum1 = this.m_mapAtomToMorganNumberWithAtomType.get(a_oStar1);
		t_iMorganNum2 = this.m_mapAtomToMorganNumberWithAtomType.get(a_oStar2);
		t_iComp = t_iMorganNum1 - t_iMorganNum2;
		if ( t_iComp != 0 ) return t_iComp;

		// Compare CIP order
		MAPConnection t_oConn1 = a_oStar1.getConnection();
		MAPConnection t_oConn2 = a_oStar2.getConnection();
		t_iComp = this.m_oCIPComp.compare(t_oConn1, t_oConn2);
		if ( t_iComp != 0 ) return t_iComp;

		// Compare Star Indices
		int t_iStarIndex1 = a_oStar1.getStarIndex();
		int t_iStarIndex2 = a_oStar2.getStarIndex();
		t_iComp = t_iStarIndex1 - t_iStarIndex2;
		if ( t_iComp != 0 ) return t_iComp;
/*
		// Compare stereo weight
		Double t_dStereoWeight1 = this.m_oGraph.getWeightOfBackboneCarbon(a_oStar1);
		Double t_dStereoWeight2 = this.m_oGraph.getWeightOfBackboneCarbon(a_oStar2);
		WeightComparator t_oWComp = new WeightComparator();
		t_iComp = t_oWComp.compare(t_dStereoWeight1, t_dStereoWeight2);
		if ( t_iComp != 0 ) return t_iComp;
*/
		return 0;
	}

}

