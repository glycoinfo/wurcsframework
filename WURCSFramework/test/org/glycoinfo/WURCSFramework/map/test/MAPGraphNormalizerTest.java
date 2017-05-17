package org.glycoinfo.WURCSFramework.map.test;

import static org.junit.Assert.*;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.map.analysis.MAPGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomCyclic;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPConnection;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.junit.Test;

public class MAPGraphNormalizerTest {


	@Test
	public void testNormalize() throws WURCSFormatException {
		String t_strInputMAP = "*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O";
		MAPGraph t_oInputGraph = (new MAPGraphImporter()).parseMAP(t_strInputMAP);
		MAPGraphNormalizer t_oNorm = new MAPGraphNormalizer(t_oInputGraph);
		// Result graph is null before start
		assertNull(t_oNorm.getNormalizedGraph());
		// Start normalize
		t_oNorm.start();
	}

	@Test
	public void testMAPGraphCopy() throws WURCSFormatException {
		String t_strInputMAP = "*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O";
		MAPGraph t_oInputGraph = (new MAPGraphImporter()).parseMAP(t_strInputMAP);
		MAPGraphNormalizer t_oNorm = new MAPGraphNormalizer(t_oInputGraph);

		MAPGraph t_oGraph = t_oNorm.copyGraphWithNoParentConnections(t_oInputGraph);
		for ( MAPAtomAbstract t_oAtom : t_oGraph.getAtoms() ) {
			// Error if there are parent connections
			assertNull( t_oAtom.getParentConnection() );
			// Error if there is MAPAtomCyclic
			assertFalse( t_oAtom instanceof MAPAtomCyclic );
			for ( MAPConnection t_oConn : t_oAtom.getChildConnections() ) {
				// Error if there is no reverse connection
				assertNotNull( t_oConn.getReverse() );
			}
		}

	}

}
