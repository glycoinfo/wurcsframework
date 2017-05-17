package org.glycoinfo.WURCSFramework.map.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.map.analysis.MAPStarComparator;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPStar;
import org.junit.Test;

public class MAPStarComparatorTest {

	@Test
	public void testMAPStarOrder() throws WURCSFormatException {
		String t_strImputMAP = "*NCCO*";
		MAPGraph t_oGraph = (new MAPGraphImporter()).parseMAP(t_strImputMAP);

		LinkedList<MAPStar> t_aStars = t_oGraph.getStars();
		MAPStar t_oStar1 = t_aStars.getFirst();
		MAPStar t_oStar2 = t_aStars.getLast();

		MAPStarComparator t_oCompStars = new MAPStarComparator(t_oGraph);
		assertTrue( t_oCompStars.compare(t_oStar1, t_oStar2) < 0 );
	}

}
