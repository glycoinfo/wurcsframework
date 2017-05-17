package org.glycoinfo.WURCSFramework.util.subsumption;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.array.MS;

public class TestSubsumption {

	public static void main(String[] args) {
		String t_strMS = "a2122h-1b_1-5_6*OPO/3O/3=O";
		t_strMS = "h2122h";
		t_strMS = "h2122m";
		t_strMS = "a2122h-1a_1-5";
//		t_strMS = "Aad21122h-2a_2-6_5*NCC/3=O";
//		t_strMS = "Aad43344h-2a_2-6_5*NCC/3=O";
//		t_strMS = "a11221h-1a_1-5";
//		t_strMS = "a44334h-1a_1-5";
//		t_strMS = "a414xh-1x_1-5";
//		t_strMS = "a21x2h-1x_1-5";
//		t_strMS = "h2U22221h";
//		t_strMS = "h121U111h";
//		t_strMS = "h222U212h";

		try {
			WURCSImporter t_oImport = new WURCSImporter();
			MS t_oMS = t_oImport.extractMS(t_strMS);

			WURCSSubsumptionIntegrator t_oIntegrator = new WURCSSubsumptionIntegrator();
			WURCSExporter t_oExport = new WURCSExporter();
			System.out.println( t_oExport.getMSString(t_oMS) +":" );
			LinkedList<MS> t_aSuperMSs = t_oIntegrator.makeSupersumedAllMSs(t_oMS);
			for ( MS t_oSuperMS : t_aSuperMSs )
				System.out.println( t_oExport.getMSString(t_oSuperMS) );

			MS t_oComposition = t_oIntegrator.makeMSComposition(t_oMS);
			System.out.println( "Composition: "+t_oExport.getMSString( t_oComposition ) );

			WURCSSubsumptionConverter t_oConv = new WURCSSubsumptionConverter();
			MS t_oReduction = t_oConv.convertCarbonylGroupToHydroxyl(t_oMS);
			System.out.println( "Reduction: "+t_oExport.getMSString( t_oReduction ) );

			LinkedList<MS> t_aExactMSs = t_oIntegrator.standardizeStereoToExact(t_oMS);
			for ( MS t_oStanderdMS : t_aExactMSs ) {
				System.out.println( "Exact standard: "+t_oExport.getMSString( t_oStanderdMS ) );
				LinkedList<String> t_aStereos = new MSStateDeterminationUtility().extractStereo(t_oStanderdMS);
				for ( String t_oBasetype : t_aStereos ) {
					System.out.println( t_oBasetype );
				}
			}

		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
