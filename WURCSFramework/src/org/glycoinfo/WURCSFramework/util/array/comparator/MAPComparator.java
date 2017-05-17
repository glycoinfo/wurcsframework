package org.glycoinfo.WURCSFramework.util.array.comparator;

import java.util.Comparator;

/**
 * Class for comparing MAP string for reorder MOD or LIN
 * @author MasaakiMatsubara
 *
 */
public class MAPComparator implements Comparator<String> {

	@Override
	public int compare(String a_oMAPString1, String a_oMAPString2) {
		// Prioritize omitted MAP ("*O", "*=O" or "*O*")
		if (  a_oMAPString1.isEmpty() && !a_oMAPString2.isEmpty() ) return -1;
		if ( !a_oMAPString1.isEmpty() &&  a_oMAPString2.isEmpty() ) return 1;

		int t_iComp = a_oMAPString1.compareTo(a_oMAPString2);
		if ( t_iComp != 0 ) return t_iComp;

		// TODO: Change comparing methods e.g. using MAPGraph
		return 0;
	}

}
