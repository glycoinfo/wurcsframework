package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.comparator.GLIPComparator;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;

/**
 * Class for conversion from WURCSArray to WRUCSSequence object
 * @author MasaakiMatsubara
 *
 */
public class WURCSArrayToSequence {

	private WURCSSequence m_oSequence;
	private TreeMap<Integer, UniqueRES> m_mapRESIDtoURES = new TreeMap<Integer, UniqueRES>();

	public WURCSSequence getSequence() {
		return this.m_oSequence;
	}

	public void start(WURCSArray a_oWURCSArray) {
		WURCSExporter t_oExport = new WURCSExporter();

		WURCSSequence t_oSequence = new WURCSSequence( t_oExport.getWURCSString(a_oWURCSArray) );

		TreeMap<Integer, MS> t_mapURESIDToMS = new TreeMap<Integer, MS>();
		//Make MS from UniqueRES
		for ( UniqueRES t_oURES : a_oWURCSArray.getUniqueRESs() ) {
			String t_strMS = (new WURCSExporter()).getUniqueRESString(t_oURES);
			t_mapURESIDToMS.put( t_oURES.getUniqueRESID(), new MS(t_strMS) );
		}

		TreeMap<Integer, GRES> t_mapRESIDToGRES = new TreeMap<Integer, GRES>();
		// Make GRES from RES
		for ( RES t_oRES : a_oWURCSArray.getRESs() ) {
			int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oRES.getRESIndex() );

			m_mapRESIDtoURES.put( t_oRESID, a_oWURCSArray.getUniqueRESs().get( t_oRES.getUniqueRESID()-1 ) );

			MS t_oMS = t_mapURESIDToMS.get(t_oRES.getUniqueRESID());
			GRES t_oGRES = new GRES(t_oRESID, t_oMS);
			t_mapRESIDToGRES.put(t_oRESID, t_oGRES);
			t_oSequence.addGRES(t_oGRES);
		}

		// Make GLIN from LIN
		for ( LIN t_oLIN : a_oWURCSArray.getLINs() ) {
			// Ignore not glycosidic LIN
			if ( t_oLIN.getListOfGLIPs().size() > 2 ) continue;

			// Donor and acceptor GLIPs
			GLIPs t_oAcceptorGLIPs = t_oLIN.getListOfGLIPs().getFirst();
			GLIPs t_oDonorGLIPs    = t_oLIN.getListOfGLIPs().getLast();
			// Swap GLIPs
			if ( this.compareGLIPs(t_oAcceptorGLIPs, t_oDonorGLIPs) > 0 ) {
				GLIPs tmp        = t_oDonorGLIPs;
				t_oDonorGLIPs    = t_oAcceptorGLIPs;
				t_oAcceptorGLIPs = tmp;
			}
			if ( t_oAcceptorGLIPs == t_oDonorGLIPs ) t_oDonorGLIPs = null;

			GLIN t_oGLIN = new GLIN(
					t_oLIN.getMAPCode(),
					this.getGLINString(t_oAcceptorGLIPs, t_oDonorGLIPs, t_oLIN.getMAPCode(), t_mapRESIDToGRES)
				);
			t_oSequence.addGLIN(t_oGLIN);

			// Set repeat count
			if ( t_oLIN.isRepeatingUnit() ) {
				t_oGLIN.setRepeatCountMin( t_oLIN.getMinRepeatCount() );
				t_oGLIN.setRepeatCountMax( t_oLIN.getMaxRepeatCount() );
			}

			// Set acceptor MS and position (ignore same value)
			for ( GLIP t_oGLIP : t_oAcceptorGLIPs.getGLIPs() ) {
				if (! t_oGLIN.getAcceptorPositions().contains( t_oGLIP.getBackbonePosition() ) )
					t_oGLIN.addAcceptorPosition( t_oGLIP.getBackbonePosition() );

				int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
				GRES t_oGRES = t_mapRESIDToGRES.get(t_oRESID);
				if ( t_oGLIN.getAcceptorMSs().contains(t_oGRES) ) continue;
				t_oGRES.addAcceptorGLIN(t_oGLIN);
				t_oGLIN.addAcceptorMS( t_oGRES.getMS() );
			}

			if ( t_oDonorGLIPs == null ) continue;
			// Set donor MS and position (ignore same values in same GLIPs)
			for ( GLIP t_oGLIP : t_oDonorGLIPs.getGLIPs() ) {
				if (! t_oGLIN.getDonorPositions().contains( t_oGLIP.getBackbonePosition() ) )
					t_oGLIN.addDonorPosition( t_oGLIP.getBackbonePosition() );

				int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
				GRES t_oGRES = t_mapRESIDToGRES.get(t_oRESID);
				if ( t_oGLIN.getDonorMSs().contains(t_oGRES) ) continue;
				t_oGRES.addDonorGLIN(t_oGLIN);
				t_oGLIN.addDonorMS( t_oGRES.getMS() );
			}
		}

		this.m_oSequence = t_oSequence ;
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

		// Prioritize RES which has known anomeric position
		int t_iAnomPos1 = this.m_mapRESIDtoURES.get(t_iRESID1).getAnomericPosition();
		int t_iAnomPos2 = this.m_mapRESIDtoURES.get(t_iRESID2).getAnomericPosition();
		int t_iPos1 = a_oGLIPs1.getGLIPs().getLast().getBackbonePosition();
		int t_iPos2 = a_oGLIPs2.getGLIPs().getLast().getBackbonePosition();
		// Prioritize not anomeric position
		if ( t_iPos1 != t_iAnomPos1 && t_iPos2 == t_iAnomPos2 ) return -1;
		if ( t_iPos1 == t_iAnomPos1 && t_iPos2 != t_iAnomPos2 ) return 1;
		if ( t_iAnomPos1 > 0 && t_iAnomPos2 <= 0 ) {
			if ( t_iPos1 != t_iAnomPos1 ) return -1;
			if ( t_iPos1 == t_iAnomPos1 ) return 1;
		}
		if ( t_iAnomPos1 <= 0 && t_iAnomPos2 > 0 ) {
			if ( t_iPos2 != t_iAnomPos2 ) return 1;
			if ( t_iPos2 == t_iAnomPos2 ) return -1;
		}

		// Prioritize higher position number
		if ( t_iPos1 != t_iPos2 ) return t_iPos2 - t_iPos1;

		// Prioritize lower residue number
		if ( t_iRESID1 != t_iRESID2 ) return t_iRESID1 - t_iRESID2;

		return 0;
	}

	/**
	 * Get string of GLIN which contains monosaccharide string
	 * @param a_oLIN LIN
	 * @return String of GLIN
	 */
	private String getGLINString(GLIPs a_oAGLIPs, GLIPs a_oDGLIPs, String a_strMAP, TreeMap<Integer, GRES> a_mapRESIDToGRES) {
		WURCSExporter t_oExport = new WURCSExporter();

		String t_strGLIN = "";

		LinkedList<GLIPs> t_aGLIPs = new LinkedList<GLIPs>();
		t_aGLIPs.add(a_oAGLIPs);
		t_aGLIPs.add(a_oDGLIPs);
		for ( GLIPs t_oGLIPs : t_aGLIPs ) {
			String t_strExGLIPs = "";

			// Sort GLIP in GLIPs
			LinkedList<GLIP> t_aGLIP = t_oGLIPs.getGLIPs();
			Collections.sort(t_aGLIP, (new GLIPComparator()));
			for ( GLIP t_oGLIP : t_aGLIP ) {
				if ( t_strExGLIPs != "" ) t_strExGLIPs += "|";
				// Concatenate monosaccharide string
				int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
				String t_strMS = a_mapRESIDToGRES.get(t_oRESID).getMS().getString();
				t_strExGLIPs += "["+t_strMS+"]";
				t_strExGLIPs += t_oExport.getLIPString(t_oGLIP);
			}

			// Alternative
			if ( t_oGLIPs.getAlternativeType() != null ) {
				if ( t_oGLIPs.getAlternativeType().equals("{") )
					t_strExGLIPs = "{" + t_strExGLIPs;
				if ( t_oGLIPs.getAlternativeType().equals("}") )
					t_strExGLIPs += "}";
			}

			if ( t_strGLIN != "" ) t_strGLIN += "-";
			t_strGLIN += t_strExGLIPs;
		}

		// MAP in LIN
		t_strGLIN += a_strMAP;

		return t_strGLIN;
	}

}
