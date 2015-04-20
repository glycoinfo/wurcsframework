package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.TreeMap;

import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.WURCSFramework.wurcsRDF.WURCSSequenceExporterRDFModel;

public class WURCStoRDF {

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		String t_strFilePath = "C:\\GlycoCTList\\";

		Date day = new Date();
		long start = day.getTime();

		Boolean t_bPrefix = true;

		String t_strFilePathR = t_strFilePath + "20150318\\20150318result-GlyTouCan_GlycoCTmfWURCS.txt";
		File file = new File(t_strFilePathR);

		if(!file.isFile()) throw new Exception();
		TreeMap<String, String> t_mapIndexToWURCS = openString(t_strFilePathR);
		try{
			// Open print writer
//			PrintWriter pwRDF1  = openTextFileW(t_strFilePath+"20150318\\WURCS-RDF-0.5.1.ttl");
//			PrintWriter pwRDF2  = openTextFileW(t_strFilePath+"20150318\\WURCS-RDF-0.5.2.ttl");
//			PrintWriter pwMSRDF = openTextFileW(t_strFilePath+"20150318\\WURCS-MS-RDF.ttl");
			PrintWriter pwSEQRDF = openTextFileW(t_strFilePath+"20150318\\WURCS-SEQ-RDF-0.1.ttl");

			for(String key : t_mapIndexToWURCS.keySet()) {
				WURCSImporter t_oImport = new WURCSImporter();
				WURCSArray t_oWURCS = t_oImport.extractWURCSArray(t_mapIndexToWURCS.get(key));
				String t_strAccessionNumber = key;
				System.out.println(key);
/*
				// Generate RDF strings (ver 0.5.2)
				WURCSRDFModelGlycan t_oRDFExport1 = new WURCSRDFModelGlycan( t_strAccessionNumber, t_oWURCS );
				pwRDF2.println( t_oRDFExport1.get_RDF("TURTLE") );

				// Generate RDF strings (ver 0.5.1)
				WURCSRDFModelGlycanOld t_oRDFExport2 = new WURCSRDFModelGlycanOld( t_strAccessionNumber, t_oWURCS );
				pwRDF1.println( t_oRDFExport2.get_RDF("TURTLE") );

				// Generate monosaccharide RDF strings
				WURCSRDFModelMs t_oMSExport = new WURCSRDFModelMs(t_oWURCS.getUniqueRESs() );
				pwMSRDF.println( t_oMSExport.get_RDF("TURTLE") );
*/
				// Conversion WURCS sequence style
				WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
				t_oA2S.start(t_oWURCS);
				WURCSSequence t_oSeq = t_oA2S.getSequence();

				// For using WURCSSequence
				WURCSSequenceExporterRDFModel t_oSeqExport = new WURCSSequenceExporterRDFModel( t_strAccessionNumber, t_oSeq );
				pwSEQRDF.println( t_oSeqExport.get_RDF("TURTLE") );
			}

			System.out.println("Fin...");
			day = new Date();
			long end = day.getTime();
			System.out.println("time:" + (end - start) + " m sec.");

		}catch(IOException e){
			System.out.println(e);
		}

	}

	/** Open text file for write */
	public static PrintWriter openTextFileW( String fileName ) throws Exception {
		String charSet = "utf-8";
		boolean append = false;
		boolean autoFlush = true;
		return new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File(fileName), append ), charSet ) ), autoFlush );

	}


	//input WURCS string file
	public static TreeMap<String, String> openString(String a_strFile) throws Exception {
		try {
			return readWURCS(new BufferedReader(new FileReader(a_strFile)));
		}catch (IOException e) {
			throw new Exception();
		}
	}

	public static TreeMap<String, String> readWURCS(BufferedReader a_bfFile) throws IOException {
		String line = "";
		TreeMap<String, String> wret = new TreeMap<String, String>();
		wret.clear();

		while((line = a_bfFile.readLine()) != null) {
			line.trim();
			if(line.indexOf("WURCS") != -1) {

				String[] IDandWURCS  = line.split("\t");
				if (IDandWURCS.length == 2) {
					wret.put(IDandWURCS[0].trim(), IDandWURCS[1]);
				}
			}
		}
		a_bfFile.close();

		return wret;
	}

}
