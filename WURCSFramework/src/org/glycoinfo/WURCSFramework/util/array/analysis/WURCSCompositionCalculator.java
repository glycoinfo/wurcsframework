package org.glycoinfo.WURCSFramework.util.array.analysis;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;

/**
 * Class for calculating parameters related compositions
 * @author MasaakiMatsubara
 *
 */
public class WURCSCompositionCalculator {

	/**
	 * Get map of monosaccharide string and its cardinality
	 * @param a_oArray WURCSArray or target glycan
	 * @return HashMap of monosaccharide string and its cardinality
	 */
	public HashMap<String, Integer> getCardinalityMap(WURCSArray a_oArray) {

		HashMap<String, Integer> t_mapMSToCardinality = new HashMap<String, Integer>();

		LinkedList<UniqueRES> t_aURESs = a_oArray.getUniqueRESs();
		for ( UniqueRES t_oURES : t_aURESs ) {
			String t_strMS = (new WURCSExporter()).getUniqueRESString(t_oURES);

			// Count cardinality
			int t_iCardinality = 0;
			int t_iURESID = t_oURES.getUniqueRESID();
			for ( RES t_oRES : a_oArray.getRESs() ) {
				if ( t_oRES.getUniqueRESID() == t_iURESID )
					t_iCardinality++;
			}

			t_mapMSToCardinality.put(t_strMS, t_iCardinality);
		}

		return t_mapMSToCardinality;
	}
}
