package org.glycoinfo.WURCSFramework.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileIOUtils {

	/** Get filepath by using FileDialog */
	public static String getFilepathFromFileDialog() {
		FileDialog dialog = new FileDialog((Frame)null, "", FileDialog.LOAD);
		dialog.setVisible(true);
		String dirname = dialog.getDirectory();
		String filename = dialog.getFile();
		return dirname+filename;
	}

	/** Open text file for read only */
	public static BufferedReader  openTextFileR( String fileName ) throws Exception {
		String charSet = "utf-8";
		return new BufferedReader( new InputStreamReader( skipUTF8BOM( new FileInputStream( new File(fileName) ), charSet), charSet) );
	}

	/** Open text file for write */
	public static PrintWriter openTextFileW( String fileName ) throws Exception {
		return openTextFileW( fileName, false );
	}

	public static PrintWriter openTextFileW( String fileName, boolean toAppend ) throws Exception {
		String charSet = "utf-8";
		boolean append = toAppend;
		boolean autoFlush = true;
		return new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File(fileName), append ), charSet ) ), autoFlush );

	}

	/** Skip BOM of UTF-8 */
	public static InputStream skipUTF8BOM( InputStream is, String charSet ) throws Exception{
		if( !charSet.toUpperCase().equals("UTF-8") ) return is;
		if( !is.markSupported() ){
			// if no mark supported, use BufferedInputStream
			is= new BufferedInputStream(is);
		}
		is.mark(3); // set mark to head
		if( is.available()>=3 ){
			byte b[]={0,0,0};
			is.read(b,0,3);
			if( b[0]!=(byte)0xEF ||
					b[1]!=(byte)0xBB ||
					b[2]!=(byte)0xBF ){
				is.reset();// if not BOM, roll back to head
			}
		}
		return is;
	}


}
