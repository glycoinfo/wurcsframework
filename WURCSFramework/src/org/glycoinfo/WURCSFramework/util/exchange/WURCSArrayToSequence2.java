package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.subsumption.MSStateDeterminationUtility;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.GLIN;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.GRES;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.MSPERI;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;

/**
 * Class for conversion from WURCSArray to WRUCSSequence object
 * @author MasaakiMatsubara
 *
 */
public class WURCSArrayToSequence2 {

	private WURCSSequence2 m_oSequence;
	private TreeMap<Integer, UniqueRES> m_mapRESIDtoURES = new TreeMap<Integer, UniqueRES>();

	public WURCSSequence2 getSequence() {
		return this.m_oSequence;
	}

	public void start(WURCSArray a_oWURCSArray) {
		WURCSExporter t_oExport = new WURCSExporter();

		WURCSSequence2 t_oSequence = new WURCSSequence2(
				t_oExport.getWURCSString(a_oWURCSArray),
				a_oWURCSArray.getUniqueRESCount(),
				a_oWURCSArray.getRESCount(),
				a_oWURCSArray.getLINCount()
			);

		TreeMap<Integer, MSPERI> t_mapURESIDToMS = new TreeMap<Integer, MSPERI>();
		MSStateDeterminationUtility t_oMSUtil = new MSStateDeterminationUtility();
		//Make MS from UniqueRES
		for ( UniqueRES t_oURES : a_oWURCSArray.getUniqueRESs() ) {
//			String t_strMS = (new WURCSExporter()).getUniqueRESString(t_oURES);
			MSArrayToSequence t_oMSA2S = new MSArrayToSequence();
			t_oMSA2S.start(t_oURES);
			MSPERI t_oPERI = t_oMSA2S.getSequenceMS();
			t_mapURESIDToMS.put( t_oURES.getUniqueRESID(), t_oPERI );

			// Add possible positions to MSPERI
			for ( int t_iPos : t_oMSUtil.getPossiblePositions(t_oURES) )
				t_oPERI.addPossiblePosition(t_iPos);
		}

		TreeMap<Integer, GRES> t_mapRESIDToGRES = new TreeMap<Integer, GRES>();
		// Make GRES from RES
		for ( RES t_oRES : a_oWURCSArray.getRESs() ) {
			int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oRES.getRESIndex() );

			m_mapRESIDtoURES.put( t_oRESID, a_oWURCSArray.getUniqueRESs().get( t_oRES.getUniqueRESID()-1 ) );

			MSPERI t_oMS = t_mapURESIDToMS.get(t_oRES.getUniqueRESID());
			GRES t_oGRES = new GRES(t_oRESID, t_oMS);

			t_mapRESIDToGRES.put(t_oRESID, t_oGRES);
			t_oSequence.addGRES(t_oGRES);
		}

		// Make GLIN from LIN
		int t_iNumLIN = 0;
		for ( LIN t_oLIN : a_oWURCSArray.getLINs() ) {
			// Ignore not glycosidic LIN
			if ( t_oLIN.getListOfGLIPs().size() > 2 ) continue;
			t_iNumLIN++;

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
/*
			GLIN t_oGLIN = new GLIN(
					t_oLIN.getMAPCode(),
					this.getGLINString(t_oAcceptorGLIPs, t_oDonorGLIPs, t_oLIN.getMAPCode(), t_mapRESIDToGRES)
				);
*/
			GLIN t_oGLIN = new GLIN( t_iNumLIN, t_oLIN.getMAPCode() );
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
				if ( !t_oGRES.getAcceptorGLINs().contains(t_oGLIN) )
					t_oGRES.addAcceptorGLIN(t_oGLIN);
				if ( !t_oGLIN.getAcceptor().contains(t_oGRES) )
					t_oGLIN.addAcceptor(t_oGRES);
			}

			if ( t_oDonorGLIPs == null ) continue;

			// Set donor MS and position (ignore same values in same GLIPs)
			for ( GLIP t_oGLIP : t_oDonorGLIPs.getGLIPs() ) {
				if (! t_oGLIN.getDonorPositions().contains( t_oGLIP.getBackbonePosition() ) )
					t_oGLIN.addDonorPosition( t_oGLIP.getBackbonePosition() );

				int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
				GRES t_oGRES = t_mapRESIDToGRES.get(t_oRESID);
				if ( !t_oGRES.getDonorGLINs().contains(t_oGLIN) )
					t_oGRES.addDonorGLIN(t_oGLIN);
				if ( !t_oGLIN.getDonor().contains(t_oGRES) )
					t_oGLIN.addDonor(t_oGRES);
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

}
