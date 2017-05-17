package org.glycoinfo.WURCSFramework.util;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

public class WURCSFileWriter extends FileIOUtils {

	private static final String m_strDate = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
	private static final String m_strSep  = File.separator;

	/**
	 * Print WURCS list with ID
	 * <pre>
	 * ID	WURCS
	 * 00001	WURCS=2.0/2,7,6/[u2122h][c2122h-1b_1-5]/1-2-2-2-2-2-2/a6-b1_b3-c1_b6-d1_d6-e1_e3-f1_e6-g1
	 * </pre>
	 * @param a_mapIDtoWURCS	TreeMap of WURCS with ID
	 * @param a_strRootFilePath	String of root directory for output file
	 * @param a_strFileName		String of output file name
	 * @return PrintWriter of output file
	 */
	public static PrintWriter printWURCSList(TreeMap<String, String> a_mapIDtoWURCS, String a_strRootFilePath, String a_strFileName) {
		String t_strResultFile = getResultFilePath(a_strRootFilePath, a_strFileName);

		try {
			PrintWriter pw = openTextFileW(t_strResultFile);

			for ( String id : a_mapIDtoWURCS.keySet() ) {
				pw.println(id+"\t"+a_mapIDtoWURCS.get(id));
			}
			return pw;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Print list of monosaccharide string in WURCS
	 * @param a_mapMSList
	 * @param a_strRootFilePath
	 * @param a_strFileName
	 * @return
	 */
	public static PrintWriter printMSList(TreeSet<String> a_mapMSList, String a_strRootFilePath, String a_strFileName) {
		String t_strResultFile = getResultFilePath(a_strRootFilePath, a_strFileName);
		try {
			PrintWriter pw = openTextFileW(t_strResultFile);

			for ( String t_strMS : a_mapMSList ) {
				pw.println(t_strMS);
			}
			return pw;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add date string to filename and directory
	 * @param a_strRootFilePath
	 * @param a_strFileName
	 * @return
	 */
	public static String getResultFilePath(String a_strRootFilePath, String a_strFileName) {
		return getResultDirPath(a_strRootFilePath)+m_strSep+m_strDate+a_strFileName;
	}

	public static String getResultDirPath(String a_strRootFilePath) {
		// mkdir for result
		String t_strResultPath = a_strRootFilePath + m_strDate;
		File resultDir = new File(t_strResultPath);
		if ( ! resultDir.exists() ) resultDir.mkdirs();

		return t_strResultPath;
	}

}
