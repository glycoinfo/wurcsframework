package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class WURCSStructureUtils {

	private WURCSArray m_oWURCS;
	private TreeMap<Integer, TreeSet<Integer>> m_hashParentIDtoChildID = new TreeMap<Integer, TreeSet<Integer>>();

	public WURCSStructureUtils(WURCSArray a_oWURCS) {
		this.m_oWURCS = a_oWURCS;

		// Make hash parent ID to child ID
		for ( LIN t_oLIN : this.m_oWURCS.getLINs() ) {
			// Get parent GLIPs
			GLIPs t_oParentGLIPs = t_oLIN.getListOfGLIPs().get(0);
			GLIPs t_oChildGLIPs = t_oLIN.getListOfGLIPs().get(1);
			int t_iGLIPsComp = this.compareGLIPs(t_oParentGLIPs, t_oChildGLIPs);
			// Swap parent and child
			if ( t_iGLIPsComp > 0 ) {
				GLIPs tmp = t_oParentGLIPs;
				t_oParentGLIPs = t_oChildGLIPs;
				t_oChildGLIPs = tmp;
			}
			for ( GLIP t_oParentGLIP : t_oParentGLIPs.getGLIPs() ) {
				int t_iParentID = WURCSDataConverter.convertRESIndexToID( t_oParentGLIP.getRESIndex() );
				if ( !this.m_hashParentIDtoChildID.containsKey(t_iParentID) )
					this.m_hashParentIDtoChildID.put(t_iParentID, new TreeSet<Integer>());
				for ( GLIP t_oChildGLIP : t_oChildGLIPs.getGLIPs() ) {
					int t_iChildID = WURCSDataConverter.convertRESIndexToID( t_oChildGLIP.getRESIndex() );
					this.m_hashParentIDtoChildID.get(t_iParentID).add(t_iChildID);
				}
			}
		}
	}

	/**
	 * Get hash map converting parent residue id to child residue id
	 * @return TreeMap of hash
	 */
	public TreeMap<Integer, TreeSet<Integer>> getHashParentIDToChildID() {
		return this.m_hashParentIDtoChildID;
	}

	/**
	 * Get string of LIN in carbbank style
	 * @param a_oLIN LIN
	 * @return String of LIN in carbbank style (null if it is not normal glycosidic linkage)
	 */
	public String getLINStringCarbbankStyle(LIN a_oLIN) {
		if ( a_oLIN.getListOfGLIPs().size() > 2 ) return null;

		// Get parent GLIPs
		GLIPs t_oParentGLIPs = a_oLIN.getListOfGLIPs().get(0);
		GLIPs t_oChildGLIPs = a_oLIN.getListOfGLIPs().get(1);
		int t_iGLIPsComp = this.compareGLIPs(t_oParentGLIPs, t_oChildGLIPs);
		// Swap parent and child
		if ( t_iGLIPsComp > 0 ) {
			GLIPs tmp = t_oParentGLIPs;
			t_oParentGLIPs = t_oChildGLIPs;
			t_oChildGLIPs = tmp;
		}
		if ( t_oChildGLIPs.getGLIPs().size() != 1 ) return null;

		LinkedList<UniqueRES> t_aParentURES = this.getUniqueRESFromGLIPs(t_oParentGLIPs);
		if ( t_aParentURES.size() != 1 ) return null;

		String t_strLINString = "";
		// Anomeric symbol and position of child residue
		LinkedList<UniqueRES> t_aChildURES  = this.getUniqueRESFromGLIPs(t_oChildGLIPs);
		char t_cAnomSymbol = t_aChildURES.getFirst().getAnomericSymbol();
		if ( t_cAnomSymbol == 'x' ) t_cAnomSymbol = '?';
		t_strLINString = ""+t_cAnomSymbol + t_aChildURES.getFirst().getAnomericPosition() + "-";

		// Linkage position of parent residue(s)
		String t_strParentPos = "";
		for ( GLIP t_oGLIP : t_oParentGLIPs.getGLIPs() ) {
			if ( !t_strParentPos.equals("") ) t_strParentPos += "/";
			t_strParentPos += t_oGLIP.getBackbonePosition();
		}
		t_strLINString += t_strParentPos;
		return t_strLINString;
	}

	/**
	 * Get parent UniqueRES(s) of LIN
	 * @param a_oLIN
	 * @return List of parent UniqueRES
	 */
	public LinkedList<UniqueRES> getParentUniqueRESOfLIN(LIN a_oLIN) {
		if ( a_oLIN.getListOfGLIPs().size() > 2 ) return null;

		// Get parent GLIPs
		GLIPs t_oParentGLIPs = null;
		GLIPs t_oGLIPs1 = a_oLIN.getListOfGLIPs().get(0);
		GLIPs t_oGLIPs2 = a_oLIN.getListOfGLIPs().get(1);
		int t_iGLIPsComp = this.compareGLIPs(t_oGLIPs1, t_oGLIPs2);
		if ( t_iGLIPsComp < 0 ) t_oParentGLIPs = t_oGLIPs1;
		if ( t_iGLIPsComp > 0 ) t_oParentGLIPs = t_oGLIPs2;
		if ( t_iGLIPsComp == 0 ) return null;

		return this.getUniqueRESFromGLIPs(t_oParentGLIPs);
	}

	/**
	 * Get child UniqueRES(s) of LIN
	 * @param a_oLIN
	 * @return List of child UniqueRES
	 */
	public LinkedList<UniqueRES> getChildUniqueRESOfLIN(LIN a_oLIN) {
		if ( a_oLIN.getListOfGLIPs().size() > 2 ) return null;

		// Get parent GLIPs
		GLIPs t_oChildGLIPs = null;
		GLIPs t_oGLIPs1 = a_oLIN.getListOfGLIPs().get(0);
		GLIPs t_oGLIPs2 = a_oLIN.getListOfGLIPs().get(1);
		int t_iGLIPsComp = this.compareGLIPs(t_oGLIPs1, t_oGLIPs2);
		if ( t_iGLIPsComp < 0 ) t_oChildGLIPs = t_oGLIPs2;
		if ( t_iGLIPsComp > 0 ) t_oChildGLIPs = t_oGLIPs1;
		if ( t_iGLIPsComp == 0 ) return null;

		return this.getUniqueRESFromGLIPs(t_oChildGLIPs);
	}

	/**
	 * Compare GLIPs by RES index (and linkage position)
	 * @param a_oGLIPs1
	 * @param a_oGLIPs2
	 * @return
	 */
	private int compareGLIPs(GLIPs a_oGLIPs1, GLIPs a_oGLIPs2) {
		int t_iRESID1 = WURCSDataConverter.convertRESIndexToID( a_oGLIPs1.getGLIPs().getLast().getRESIndex() );
		int t_iRESID2 = WURCSDataConverter.convertRESIndexToID( a_oGLIPs2.getGLIPs().getLast().getRESIndex() );
		if ( t_iRESID1 < t_iRESID2 ) return -1;
		if ( t_iRESID1 > t_iRESID2 ) return 1;

		// For linkage between same RES
		int t_iPos1 = a_oGLIPs1.getGLIPs().getLast().getBackbonePosition();
		int t_iPos2 = a_oGLIPs2.getGLIPs().getLast().getBackbonePosition();
		if ( t_iPos1 < t_iPos2 ) return -1;
		if ( t_iPos1 > t_iPos2 ) return 1;

		return 0;
	}

	/**
	 * Get UniqueRES(s) from GLIPs
	 * @param a_oGLIPs
	 * @return List of UniqueRES in GLIPs
	 */
	private LinkedList<UniqueRES> getUniqueRESFromGLIPs(GLIPs a_oGLIPs) {
		if ( a_oGLIPs == null ) return null;
		LinkedList<String> t_aRESIndexes = new LinkedList<String>();
		for ( GLIP t_oGLIP : a_oGLIPs.getGLIPs() ) {
			if ( t_aRESIndexes.contains( t_oGLIP.getRESIndex() ) ) continue;
			t_aRESIndexes.add( t_oGLIP.getRESIndex() );
		}
		LinkedList<UniqueRES> t_aURESs = new LinkedList<UniqueRES>();
		for ( RES t_oRES : this.m_oWURCS.getRESs() ) {
			if ( !t_aRESIndexes.contains( t_oRES.getRESIndex() ) ) continue;

			for ( UniqueRES t_oURES : this.m_oWURCS.getUniqueRESs() ) {
				if ( t_oRES.getUniqueRESID() != t_oURES.getUniqueRESID() ) continue;
				t_aURESs.add( t_oURES );
			}
		}
		return t_aURESs;
	}
}
