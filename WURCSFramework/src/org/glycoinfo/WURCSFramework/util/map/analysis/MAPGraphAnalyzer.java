package org.glycoinfo.WURCSFramework.util.map.analysis;

import org.glycoinfo.WURCSFramework.util.map.MAPGraphExporter;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;

public class MAPGraphAnalyzer {

	private MAPGraph m_oMAPGraph;

	public MAPGraphAnalyzer(MAPGraph a_oMAP) {
		this.m_oMAPGraph = a_oMAP;
	}

	/**
	 * Get type for monosaccharide classification (Type-I, II or III)
	 * @return String of type;
	 */
	public String getType() {
		if ( this.isTypeI()   ) return "I";
		if ( this.isTypeII()  ) return "II";
		if ( this.isTypeIII() ) return "III";
		return null;
	}

	public boolean isTypeI() {
		String t_strMAPCode = (new MAPGraphExporter()).getMAP(this.m_oMAPGraph) ;
		if ( t_strMAPCode.equals("*O") || t_strMAPCode.equals("*=O") || t_strMAPCode.equals("*O*") )
			return true;
		return false;

	}

	public boolean isTypeII() {
		if ( this.isTypeI() ) return false;
		for ( MAPStar t_oStar : this.m_oMAPGraph.getStars() ) {
			String t_strSymbol = "H";
			if ( t_oStar.getConnection() != null)
				t_strSymbol = t_oStar.getConnection().getAtom().getSymbol();
			if ( !t_strSymbol.equals("O") )
				return true;
		}
		return false;
	}

	public boolean isTypeIII() {
		if ( this.isTypeI() ) return false;
		if ( this.isTypeII() ) return false;
		return true;
	}

}
