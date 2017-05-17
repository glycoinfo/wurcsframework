package org.glycoinfo.WURCSFramework.map.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.map.MAPFactory;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphExporter;
import org.glycoinfo.WURCSFramework.util.map.MAPGraphImporter;
import org.glycoinfo.WURCSFramework.util.map.analysis.MAPGraphAnalyzer;
import org.glycoinfo.WURCSFramework.util.map.analysis.MorganAlgorithmForMAP;
import org.glycoinfo.WURCSFramework.util.map.analysis.MorganAlgorithmWithAtomTypeForMAP;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPAtomAbstract;
import org.glycoinfo.WURCSFramework.wurcs.map.MAPGraph;
import org.junit.Test;

public class MAPTest {

	@Test
	public void testMAPGraphIO() throws WURCSFormatException {
		// Test for inputting/outputting MAP to/from MAPGraph
		LinkedList<String> t_strInputMAPs = new LinkedList<String>();
		t_strInputMAPs.add("*O(C^EC^ZC^ZC^ZC^ZC^E$3)");
		t_strInputMAPs.add("*3O(C^EC^ZC^ZC^ZC^ZC^E$3)");
		t_strInputMAPs.add("*O(C^EC^ZC^ZC^ZC^ZC^Z$3)");
		t_strInputMAPs.add("*OC(C^EC^EC^ZC^ZC^EC^E$4)/3=O/6O/7O/8O");
		t_strInputMAPs.add("*OC(C^EC^ZC^ZC^ZC^ZC^E$4)/9C/5C/3=O");
		t_strInputMAPs.add("*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O");

		LinkedList<String> t_strOutputMAPs = new LinkedList<String>();
		for ( String t_strMAP : t_strInputMAPs ) {
			MAPGraphImporter t_oImp = new MAPGraphImporter();
			MAPGraph t_oGraph = t_oImp.parseMAP(t_strMAP);
			MAPGraphExporter t_oExp = new MAPGraphExporter();
			t_strOutputMAPs.add( t_oExp.getMAP(t_oGraph) );
		}

		assertEquals( "*O(C^EC^ZC^ZC^ZC^ZC^E$3)", t_strOutputMAPs.get(0) );
		assertEquals( "*3O(C^EC^ZC^ZC^ZC^ZC^E$3)", t_strOutputMAPs.get(1) );
		assertEquals( "*O(C^EC^ZC^ZC^ZC^ZC^Z$3)", t_strOutputMAPs.get(2) );
		assertEquals( "*OC(C^EC^EC^ZC^ZC^EC^E$4)/3=O/6O/7O/8O", t_strOutputMAPs.get(3) );
		assertEquals( "*OC(C^EC^ZC^ZC^ZC^ZC^E$4)/9C/5C/3=O", t_strOutputMAPs.get(4) );
		assertEquals( "*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O", t_strOutputMAPs.get(5) );
	}

	@Test
	public void testMorganNumberCalculation() throws WURCSFormatException {
		// Test for calculating Morgan number of atoms in MAPGraph
		/**
		 * *1--O2           4--8
		 *      \               \
		 *       C3--C4         14-10
		 *      //    \\        /    \
		 *      C8    C5  -->  10     9
		 *       \    /         \    /
		 *       C7==C6          9--8
		 */
		String t_strMAP = "*O(C^EC^ZC^ZC^ZC^ZC^E$3)";
		MAPGraphImporter t_oMAPImp = new MAPGraphImporter();
		MAPGraph t_oMAP = t_oMAPImp.parseMAP(t_strMAP);
		MorganAlgorithmForMAP t_oMA = new MorganAlgorithmForMAP(t_oMAP);
		t_oMA.calcMorganNumber(null, null);
		assertEquals(  4, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(0) ) );
		assertEquals(  8, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(1) ) );
		assertEquals( 14, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(2) ) );
		assertEquals( 10, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(3) ) );
		assertEquals(  9, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(4) ) );
		assertEquals(  8, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(5) ) );
		assertEquals(  9, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(6) ) );
		assertEquals( 10, t_oMA.getMorganNumber( t_oMAP.getAtoms().get(7) ) );

		/**
		 * *1--O2    N9--*2 12--28    26--12
		 *      \    /           \    /
		 *      C3--C4           43--44
		 *     //    \\          /    \
		 *     C8    C5   -->   32    31
		 *      \    /           \    /
		 *      C7==C6           24--24
		 */
//		t_strMAP = "*1O(C^ZC^EC^ZC^ZC^ZC^E$3)/4N*2";
		t_strMAP = "*O(C^EC^ZC^ZC^EC^ZC^EC^ZC^ZC^EC^Z$3)/8O";
//		t_strMAP = "*O(C^ZC^EC^ZC^ZC^EC^ZC^EC^ZC^ZC^E$3)/8O";
//		t_strMAP = "*OCCN*";
		t_oMAP = t_oMAPImp.parseMAP(t_strMAP);
		t_oMA = new MorganAlgorithmWithAtomTypeForMAP(t_oMAP);
		t_oMA.calcMorganNumber(null, null);
		for ( MAPAtomAbstract t_oAtom : t_oMAP.getAtoms() ) {
			System.err.println( t_oAtom.getSymbol()+":"+t_oMA.getMorganNumber(t_oAtom) );
		}
	}

	@Test
	public void testMAPGraphCIPOrder() throws WURCSFormatException {
		String t_strImputMAP = "*NCCO*";
		// Normalized in MAPFactory
		MAPFactory t_oFactory = new MAPFactory(t_strImputMAP);
		String t_strOutputMAP = t_oFactory.getMAPString();

	}

	@Test
	public void testMAPGraphNormalize() throws WURCSFormatException {
		LinkedList<String> t_strInputMAPs = new LinkedList<String>();
		t_strInputMAPs.add("*O(C^EC^ZC^ZC^ZC^ZC^E$3)");
		t_strInputMAPs.add("*3O(C^EC^ZC^ZC^ZC^ZC^E$3)");
		t_strInputMAPs.add("*O(C^EC^ZC^ZC^ZC^ZC^Z$3)");
		t_strInputMAPs.add("*OC(C^EC^EC^ZC^ZC^EC^E$4)/3=O/6O/7O/8O");
		t_strInputMAPs.add("*OC(C^EC^ZC^ZC^ZC^ZC^E$4)/5C/9C/3=O");
		t_strInputMAPs.add("*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O");
		t_strInputMAPs.add("*OCCN*");
		t_strInputMAPs.add("*1OCCN*2");
		t_strInputMAPs.add("*1NC=CO*2");
		t_strInputMAPs.add("*O(C^EC^ZC^ZC^EC^ZC^EC^ZC^ZC^EC^Z$3)/8O");
		// TODO: Renumbering Star Indices
//		t_strInputMAPs.add("*1OC=CO*2");


		LinkedList<String> t_strOutputMAPs = new LinkedList<String>();
		for ( String t_strMAP : t_strInputMAPs ) {
			// Normalized in MAPFactory
			MAPFactory t_oFactory = new MAPFactory(t_strMAP);
			t_strOutputMAPs.add( t_oFactory.getMAPString() );
		}
		assertEquals( "*O(C^EC^ZC^ZC^ZC^ZC^E$3)", t_strOutputMAPs.get(0) );
		assertEquals( "*O(C^EC^ZC^ZC^ZC^ZC^E$3)", t_strOutputMAPs.get(1) );
		assertEquals( "*O(C^ZC^ZC^ZC^ZC^ZC^E$3)", t_strOutputMAPs.get(2) );
		assertEquals( "*OC(C^EC^EC^ZC^ZC^EC^E$4)/8O/7O/6O/3=O", t_strOutputMAPs.get(3) );
		assertEquals( "*OC(C^EC^ZC^ZC^ZC^ZC^E$4)/9C/5C/3=O", t_strOutputMAPs.get(4) );
		assertEquals( "*OC(C^EC^ZC^ZC^ZC^ZC^ZC^ZC^Z$6/7^ZC^ZC^E$4)/3=O", t_strOutputMAPs.get(5) );
		assertEquals( "*1NCCO*2", t_strOutputMAPs.get(6) );

		assertEquals( "*1NCCO*2", t_strOutputMAPs.get(7) );
		assertEquals( "*1NC=^XCO*2", t_strOutputMAPs.get(8) );
		assertEquals( "*O(C^ZC^EC^ZC^ZC^EC^ZC^EC^ZC^ZC^E$3)/8O", t_strOutputMAPs.get(9) );
		// TODO: Renumbering Star Indices
//		assertEquals( "*OC=^XCO*", t_strOutputMAPs.get(10) );
	}

	@Test
	public void testMAPGraphTypeAnalyzer() throws WURCSFormatException {
		LinkedList<String> t_strInputMAPs = new LinkedList<String>();
		t_strInputMAPs.add( "*O" );
		t_strInputMAPs.add( "*=O" );
		t_strInputMAPs.add( "*O*" );
		t_strInputMAPs.add( "*N" );
		t_strInputMAPs.add( "*O(C^ZC^EC^ZC^ZC^ZC^E$3)/4O" );
		t_strInputMAPs.add( "*O(C^ZC^EC^ZC^ZC^ZC^E$3)/4O*" );
		t_strInputMAPs.add( "*1O(C^ZC^EC^ZC^ZC^ZC^E$3)/4N*2" );

		LinkedList<String> t_strTypes = new LinkedList<String>();
		for ( String t_strMAP : t_strInputMAPs ) {
			MAPFactory t_oFactory = new MAPFactory(t_strMAP);
			MAPGraph t_oMAP =  t_oFactory.getMAPGrpah();
			MAPGraphAnalyzer t_oAnal = new MAPGraphAnalyzer(t_oMAP);
			t_strTypes.add(t_oAnal.getType());
		}
		assertEquals( "I", t_strTypes.get(0) );
		assertEquals( "I", t_strTypes.get(1) );
		assertEquals( "I", t_strTypes.get(2) );
		assertEquals( "II", t_strTypes.get(3) );
		assertEquals( "III", t_strTypes.get(4) );
		assertEquals( "III", t_strTypes.get(5) );
		assertEquals( "II", t_strTypes.get(6) );
	}

	@Test
	public void testExistingMAPs() throws WURCSFormatException {
		LinkedList<String> t_strInputMAPs = new LinkedList<String>();
		t_strInputMAPs.add("*");
		t_strInputMAPs.add("*1NCCOP^XO*2/6O/6=O");
		t_strInputMAPs.add("*1OC^RO*2/3CO/6=O/3C");
		t_strInputMAPs.add("*1OC^X*2/3CO/5=O/3C");
		t_strInputMAPs.add("*1OP^X*2/3O/3=O");
		t_strInputMAPs.add("*Br");
		t_strInputMAPs.add("*C");
		t_strInputMAPs.add("*C=O");
		t_strInputMAPs.add("*CC/2=O");
		t_strInputMAPs.add("*CCNO/3=O");
		t_strInputMAPs.add("*CO");
		t_strInputMAPs.add("*CO/2=O");
		t_strInputMAPs.add("*C^RCO/2O");
		t_strInputMAPs.add("*C^SCO/3=O/2C");
		t_strInputMAPs.add("*Cl");
		t_strInputMAPs.add("*F");
		t_strInputMAPs.add("*I");
		t_strInputMAPs.add("*N");
		t_strInputMAPs.add("*N*");
		t_strInputMAPs.add("*NC");
		t_strInputMAPs.add("*NC/2C");
		t_strInputMAPs.add("*NC=O");
		t_strInputMAPs.add("*NCC");
		t_strInputMAPs.add("*NCC/3=O");
		t_strInputMAPs.add("*NCCCCO/6=O/3=O");
		t_strInputMAPs.add("*NCCF/4F/4F/3=O");
		t_strInputMAPs.add("*NCCO");
		t_strInputMAPs.add("*NCCO/3=O");
		t_strInputMAPs.add("*NCN/3=N");
		t_strInputMAPs.add("*NCO/3=O");
		t_strInputMAPs.add("*NC^SCO/4=O/3C");
		t_strInputMAPs.add("*NC^SCO/4=O/3CCCO/9=O");
		t_strInputMAPs.add("*NO/2=O");
		t_strInputMAPs.add("*NSO/3=O/3=O");
		t_strInputMAPs.add("*OC");
		t_strInputMAPs.add("*OC=O");
		t_strInputMAPs.add("*OCC");
		t_strInputMAPs.add("*OCC/3=O");
		t_strInputMAPs.add("*OCCCCO*/6=O/3=O");
		t_strInputMAPs.add("*OCCCCO/6=O/3=O");
		t_strInputMAPs.add("*OCCO/3=O");
		t_strInputMAPs.add("*OCC^RC/4O/3=O");
		t_strInputMAPs.add("*OCC^SC/4O/3=O");
		t_strInputMAPs.add("*OCC^XC/4O/3=O");
		t_strInputMAPs.add("*OC^RCO/4=O/3C");
		t_strInputMAPs.add("*OC^SCO/4=O/3C");
		t_strInputMAPs.add("*OC^XO*/3CO/6=O/3C");
		t_strInputMAPs.add("*ON");
		t_strInputMAPs.add("*OPO*/3O/3=O");
		t_strInputMAPs.add("*OPO/3O/3=O");
		t_strInputMAPs.add("*OP^XOCCN/3O/3=O");
		t_strInputMAPs.add("*OP^XOCCNC/7C/7C/3O/3=O");
		t_strInputMAPs.add("*OP^XOPO/5O/5=O/3O/3=O");
		t_strInputMAPs.add("*OP^XOP^XOCCN/5O/5=O/3O/3=O");
		t_strInputMAPs.add("*OP^XOP^XOPO/7O/7=O/5O/5=O/3O/3=O");
		t_strInputMAPs.add("*OSO*/3=O/3=O");
		t_strInputMAPs.add("*OSO/3=O/3=O");
		t_strInputMAPs.add("*S");
		t_strInputMAPs.add("*S*");
		t_strInputMAPs.add("*SC");

		for ( String t_strInputMAP : t_strInputMAPs ) {
			System.err.print(t_strInputMAP);
			MAPFactory t_oFactory = new MAPFactory(t_strInputMAP);
			String t_strOutputMAP = t_oFactory.getMAPString();
			if ( !t_strInputMAP.equals(t_strOutputMAP) )
				System.err.print(" -> "+t_strOutputMAP);
			System.err.println();
//			assertEquals( t_strInputMAP, t_strOutputMAP );
		}
	}

}
