package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSDataConverter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.comparator.GLIPComparator;
import org.glycoinfo.WURCSFramework.util.comparator.GLIPsComparator;
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

	public WURCSSequence getSequence() {
		return this.m_oSequence;
	}

	public void start(WURCSArray a_oWURCSArray) {
		WURCSSequence t_oRDF = new WURCSSequence();

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
			MS t_oMS = t_mapURESIDToMS.get(t_oRES.getUniqueRESID());
			GRES t_oGRES = new GRES(t_oRESID, t_oMS);
			t_mapRESIDToGRES.put(t_oRESID, t_oGRES);
			t_oRDF.addGRES(t_oGRES);
		}

		// Make GLIN from LIN
		for ( LIN t_oLIN : a_oWURCSArray.getLINs() ) {
			// Ignore not glycosidic LIN
			if ( t_oLIN.getListOfGLIPs().size() > 2 ) continue;

			GLIN t_oGLIN = new GLIN( t_oLIN.getMAPCode(), this.getGLINString(t_oLIN, t_mapRESIDToGRES) );
			t_oRDF.addGLIN(t_oGLIN);

			// Set repeat count
			if ( t_oLIN.isRepeatingUnit() ) {
				t_oGLIN.setRepeatCountMin( t_oLIN.getMinRepeatCount() );
				t_oGLIN.setRepeatCountMax( t_oLIN.getMaxRepeatCount() );
			}

			// Donor and acceptor GLIPs
			GLIPs t_oDonorGLIPs    = t_oLIN.getListOfGLIPs().getFirst();
			GLIPs t_oAcceptorGLIPs = t_oLIN.getListOfGLIPs().getLast();
			if ( (new GLIPsComparator()).compare(t_oDonorGLIPs, t_oAcceptorGLIPs) > 0 ) {
				GLIPs tmp        = t_oDonorGLIPs;
				t_oDonorGLIPs    = t_oAcceptorGLIPs;
				t_oAcceptorGLIPs = tmp;
			}
			if ( t_oDonorGLIPs == t_oAcceptorGLIPs ) t_oAcceptorGLIPs = null;

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

			if ( t_oAcceptorGLIPs == null ) continue;
			// Set acceptor MS and position (ignore same value)
			for ( GLIP t_oGLIP : t_oAcceptorGLIPs.getGLIPs() ) {
				if (! t_oGLIN.getAcceptorPositions().contains( t_oGLIP.getBackbonePosition() ) )
					t_oGLIN.addAcceptorPosition( t_oGLIP.getBackbonePosition() );

				int t_oRESID = WURCSDataConverter.convertRESIndexToID( t_oGLIP.getRESIndex() );
				GRES t_oGRES = t_mapRESIDToGRES.get(t_oRESID);
				if ( t_oGLIN.getDonorMSs().contains(t_oGRES) ) continue;
				t_oGRES.addAcceptorGLIN(t_oGLIN);
				t_oGLIN.addAcceptorMS( t_oGRES.getMS() );
			}
		}

		this.m_oSequence = t_oRDF ;
	}

	/**
	 * Get string of GLIN which contains monosaccharide string
	 * @param a_oLIN LIN
	 * @return String of GLIN
	 */
	private String getGLINString(LIN a_oLIN, TreeMap<Integer, GRES> a_mapRESIDToGRES) {
		WURCSExporter t_oExport = new WURCSExporter();

		String t_strGLIN = "";

		// Sort GLIPs
		LinkedList<GLIPs> t_aGLIPs = a_oLIN.getListOfGLIPs();
		Collections.sort(t_aGLIPs, (new GLIPsComparator()));
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
		t_strGLIN += a_oLIN.getMAPCode();

		return t_strGLIN;
	}

}
