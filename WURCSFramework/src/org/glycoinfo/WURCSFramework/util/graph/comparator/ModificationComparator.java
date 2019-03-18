package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;

/**
 * Class for Modificaiton comparison
 * @author MasaakiMatsubara
 * TODO:
 */
public class ModificationComparator implements Comparator<Modification> {

	@Override
	public int compare(Modification m1, Modification m2) {

		// For MAPs
		String t_strMAP1 = m1.getMAPCode();
		String t_strMAP2 = m2.getMAPCode();
		int m_iComp;

//muller added code for compare MAPCode 180606
// length
		m_iComp = t_strMAP1.length() - t_strMAP2.length();
//		if(m_iComp!=0) System.err.println("Modification comparator1:"+t_strMAP1+","+t_strMAP2+","+m_iComp);
		if ( m_iComp !=0 ) return m_iComp;
// dictionary order
//		m_iComp=t_strMAP1.compareTo(t_strMAP2);
		m_iComp=t_strMAP2.compareTo(t_strMAP1);
//		if(m_iComp!=0) System.err.println("Modification comparator2:"+t_strMAP1+","+t_strMAP2+","+m_iComp);
		if ( m_iComp !=0 ) return m_iComp;
//end muller

		// For branching point
//		if (  )

		// For edge count
		int edgeCount1 = m1.getEdges().size();
		int edgeCount2 = m2.getEdges().size();
		if ( edgeCount1 != edgeCount2 ) return edgeCount1 - edgeCount2;
		return 0;
	}

	// i think a usage of "Backbone" slightly wrong in MAP
	private int countBackboneCarbonInMAP(String a_strMAP) {
		int t_nBC = 0;
		for ( int i=0; i<a_strMAP.length(); i++ ) {
			char t_cCD = a_strMAP.charAt(i);
			if ( t_cCD == '*' ) t_nBC++;
		}
		return t_nBC;
	}
}
