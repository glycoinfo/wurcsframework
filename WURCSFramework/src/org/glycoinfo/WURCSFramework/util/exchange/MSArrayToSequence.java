package org.glycoinfo.WURCSFramework.util.exchange;

import java.util.Collections;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.comparator.LIPsComparator;
import org.glycoinfo.WURCSFramework.util.subsumption.MSStateDeterminationUtility;
import org.glycoinfo.WURCSFramework.wurcs.array.LIP;
import org.glycoinfo.WURCSFramework.wurcs.array.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.BRIDGE;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.MSCORE;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.MSPERI;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.SEQMOD;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.SUBST;

public class MSArrayToSequence {

	private MSPERI m_oPERI;

	public MSPERI getSequenceMS() {
		return this.m_oPERI;
	}

	public void start(MS a_oMS) {
		// Extract core structure
		int  t_iAnomPos    = a_oMS.getAnomericPosition();
		char t_cAnomSymbol = a_oMS.getAnomericSymbol();
		String t_strSkeletonCode = a_oMS.getSkeletonCode();
		MS t_oCoreMS = new MS(t_strSkeletonCode, t_iAnomPos, t_cAnomSymbol);

		// Extract core MOD and store periference MOD
		LinkedList<MOD> t_aPeriMODs = new LinkedList<MOD>();
		MSStateDeterminationUtility t_oSubUtil = new MSStateDeterminationUtility();
		for ( MOD t_oMOD : a_oMS.getMODs() ) {
			if ( t_oSubUtil.isCoreMOD(t_oMOD) ) {
				t_oCoreMS.addMOD(t_oMOD);
				continue;
			}
			t_aPeriMODs.add(t_oMOD);
		}

		// Construct MS object
		WURCSExporter t_oExport = new WURCSExporter();
		String t_strMS = t_oExport.getMSString( a_oMS );
		String t_strCoreMS = t_oExport.getMSString( t_oCoreMS );
		MSCORE t_oCORE = new MSCORE( t_strCoreMS, t_strSkeletonCode, t_iAnomPos, t_cAnomSymbol );
		// TODO: make unique ID for bridge and subst
		int t_iMODID = 1;
		for ( MOD t_oMOD : t_oCoreMS.getMODs() ) {
			SEQMOD t_oSMOD = this.convertMODToSEQMOD(t_oMOD, t_iMODID++);
			if ( t_oSMOD == null ) continue;
			if ( t_oSMOD instanceof SUBST  ) t_oCORE.addSubstituent( (SUBST)t_oSMOD );
			if ( t_oSMOD instanceof BRIDGE ) t_oCORE.addDivalentSubstituent( (BRIDGE)t_oSMOD );
		}

		MSPERI t_oPERI = new MSPERI( t_strMS, t_oCORE );
		for ( MOD t_oMOD : t_aPeriMODs ) {
			SEQMOD t_oSMOD = this.convertMODToSEQMOD(t_oMOD, t_iMODID++);
			if ( t_oSMOD == null ) continue;
			if ( t_oSMOD instanceof SUBST  ) t_oPERI.addSubstituent( (SUBST)t_oSMOD );
			if ( t_oSMOD instanceof BRIDGE ) t_oPERI.addDivalentSubstituent( (BRIDGE)t_oSMOD );
		}
		this.m_oPERI = t_oPERI;
	}

	private SEQMOD convertMODToSEQMOD(MOD a_oMOD, int a_iID) {
		SEQMOD t_oSEQMOD = null;
		if ( a_oMOD.getListOfLIPs().size() == 1 ) {
			SUBST t_oSUBST = new SUBST(a_iID, a_oMOD.getMAPCode());
			for ( LIP t_oLIP : a_oMOD.getListOfLIPs().getFirst().getLIPs() ) {
				t_oSUBST.addPosition(t_oLIP.getBackbonePosition());
			}
			t_oSEQMOD = t_oSUBST;
		} else if ( a_oMOD.getListOfLIPs().size() == 2 ) {
			BRIDGE t_oBRIDGE = new BRIDGE(a_iID, a_oMOD.getMAPCode());
			LinkedList<LIPs> t_aLIPs = a_oMOD.getListOfLIPs();
			Collections.sort(t_aLIPs, new LIPsComparator());
			for ( LIP t_oLIP : t_aLIPs.getFirst().getLIPs() ) {
				t_oBRIDGE.addStartPosition(t_oLIP.getBackbonePosition());
			}
			for ( LIP t_oLIP : t_aLIPs.getLast().getLIPs() ) {
				t_oBRIDGE.addEndPosition(t_oLIP.getBackbonePosition());
			}
			t_oSEQMOD = t_oBRIDGE;
		} else {
			return null;
		}

		// TODO: Probability

		return t_oSEQMOD;
	}
}
