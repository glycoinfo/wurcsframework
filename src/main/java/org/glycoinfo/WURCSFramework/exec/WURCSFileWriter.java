package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class WURCSFileWriter {

	public static PrintWriter printWURCS(TreeMap<String, String> a_mapIDtoWURCS, String a_strFilePath, String a_strFileName) {
		// mkdir for result
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String t_strDate = sdf.format(new Date());
		String t_strResultPath = a_strFilePath + t_strDate;
		File resultDir = new File(t_strResultPath);
		if ( ! resultDir.exists() ) resultDir.mkdirs();

		try {
			PrintWriter pw = WURCSFileWriter.openTextFileW(t_strResultPath+"\\"+a_strFileName);

			for ( String id : a_mapIDtoWURCS.keySet() ) {
				pw.println(id+"\t"+a_mapIDtoWURCS.get(id));
			}
			return pw;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	/** Open text file for write */
	public static PrintWriter openTextFileW( String fileName ) throws Exception {
		String charSet = "utf-8";
		boolean append = false;
		boolean autoFlush = true;
		return new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File(fileName), append ), charSet ) ), autoFlush );

	}


}
