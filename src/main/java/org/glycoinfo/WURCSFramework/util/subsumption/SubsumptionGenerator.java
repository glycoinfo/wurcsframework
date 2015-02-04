package org.glycoinfo.WURCSFramework.util.subsumption;

import java.awt.PrintJob;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public class SubsumptionGenerator {
	public static void main(String[] args) throws Exception {
	
	//String input = "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1";
	//String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/1-2-3-4-2-5-4-2-6-7/a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3|c6_g1-c3|c6";
	//String input = "WURCSString = "WURCS=2.0/2,3,2/[11112h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1";
	String input = "WURCS=2.0/2,2,1/[12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5]/1-2/a4-b1";
	if(input == null || input.equals("")) throw new Exception();

	WURCSImporter ws = new WURCSImporter();
	WURCSArray wsArray = ws.extractWURCSArray(input);
	
	
	
	WURCSExporter export = new WURCSExporter();
	String WURCSString = export.getWURCSString(wsArray);
	if (! input.equals(WURCSString) ) {
		System.out.println("Change WURCS string in importer and exporter");
		System.out.println(input);
		System.out.println(WURCSString);
	}
	
	//WURCSImporter ws = new WURCSImporter();
	//WURCSArray wurcsArray = ws.extractWURCSArray(WURCSString);
	//subsumption:  (LIP / MAP)
		//[4Ac, 6Ac はMAPに入ってる。xはピラノース(閉環)、uは開/閉環どちらか不明,a/bにかかわる情報は全てx]
	//modify new_wurcsArray
	//[b-GalpNAc(6Ac)](1-4)[a-GlcpN(4Ac)]    <Start>
		//ano lin
	//[?-GalpNAc(6Ac)](1-4)[?-GlcpN(4Ac)]	[x2112h-1x_1-5_2*NCC/3=O_6*OCC/3=O]
			//ano
	//[b-GalpNAc(?Ac)](?-?)[a-GlcpN(?Ac)]	[12112h-1b_1-5_2*NCC/3=O_?*OCC/3=O]
			//lin
	//[?-GalpNAc(?Ac)](?-?)[?-GlcpN(?Ac)]	[x2112h-1x_1-5_2*NCC/3=O_?*OCC/3=O]
	//[?-GalNAc(?Ac)][?-GlcN(?Ac)]	[u2112h_2*NCC/3=O_?*OCC/3=O]
			//stereo
	//[HexNAc(?Ac)][HexN(?Ac)]   <UniqueRes>
	//WURCS=2.0/2,2,0/[uxxxxh_2*NCC/3=O_?*OCC/3=O][uxxxxh_2*N_?*OCC/3=O]/1-2/
	//[HexNAc][HexN](Ac)2          <Goal><UniqueRes+LIN>
	//WURCS=2.0/2,2,2/[uxxxxh_2*NCC/3=O][uxxxxh_2*N]/1-2/??*OCC/3=O_??*OCC/3=O
	//WURCSExporter export = new WURCSExporter();
	//String new_WURCSString = export.getWURCSString(new_wurcsArray);
	//new_WURCSString = "WURCS=2.0/2,3,2/[x1112h-1x_1-5][x1122h-1x_1-5]/1-2-2/a3-b1_a6-c1";
	//＊リピートは。。To do
	//WURCSArray とSubsumptionレベル






	
	
	
	}
	
}