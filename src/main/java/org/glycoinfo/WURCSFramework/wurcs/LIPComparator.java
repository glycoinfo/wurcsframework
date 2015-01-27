package org.glycoinfo.WURCSFramework.wurcs;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;

/**
 * Comparator for LIP
 * @author MasaakiMatsubara
 *
 */
public class LIPComparator implements Comparator<LIP> {

	@Override
	public int compare(LIP o1, LIP o2) {
		// For Backbone position
		int t_iSCPos1 = o1.getBackbonePosition();
		int t_iSCPos2 = o2.getBackbonePosition();
		if ( t_iSCPos1 != t_iSCPos2 ) {
			if ( t_iSCPos1 != -1 && t_iSCPos1 == -1 ) return -1;
			if ( t_iSCPos1 == -1 && t_iSCPos1 != -1 ) return 1;
			return t_iSCPos1 - t_iSCPos2;
		}

		// For Direction
		char t_cDirection1 = o1.getBackboneDirection();
		char t_cDirection2 = o2.getBackboneDirection();
		if ( t_cDirection1 != t_cDirection2 )
			return WURCSDataConverter.convertDirectionToID(t_cDirection1)
					- WURCSDataConverter.convertDirectionToID(t_cDirection2);

		// For MAP postition
		int t_iMAPPos1 = o1.getModificationPosition();
		int t_iMAPPos2 = o2.getModificationPosition();
		if ( t_iMAPPos1 != t_iMAPPos2 )
			return t_iMAPPos1-t_iMAPPos2;
		return 0;
	}

}
