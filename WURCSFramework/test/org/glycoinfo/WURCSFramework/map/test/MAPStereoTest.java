package org.glycoinfo.WURCSFramework.map.test;

import static org.junit.Assert.*;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphExporter;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.map.analysis.cip.MAPStereochemistryCheckerUsingCIPSystem;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.junit.Test;

public class MAPStereoTest {

	@Test
	public void testStereoChangingMAPStar() throws WURCSFormatException {
		/**
		 * *3  *1 *2  *4
		 *  |   | |   |
		 *  O   O O   O
		 *   \ /   \ /
		 *    C     C
		 * ?->||   ||<-?
		 *     C   C
		 *      \ /
		 *     ?->C
		 *      / \
		 *   H3C   H
		 */
//		String t_strStereoMAP = "*1OCO*3/3=^ZCOC^ROC=^ZCO*2/11O*4/8C";
		String t_strStereoMAP = "*OCO*/3=COCOC=CO*/11O*/8C";
		MAPGraph t_oGraph = (new MAPGraphImporter()).parseMAP(t_strStereoMAP);

		MAPStereochemistryCheckerUsingCIPSystem t_oStereo = new MAPStereochemistryCheckerUsingCIPSystem(t_oGraph);
		t_oStereo.resetStereo();
		String t_strOutMAP = (new MAPGraphExporter()).getMAP(t_oGraph);
		assertEquals( "*OCO*/3=^XCOC^XOC=^XCO*/11O*/8C", t_strOutMAP );
	}

}
