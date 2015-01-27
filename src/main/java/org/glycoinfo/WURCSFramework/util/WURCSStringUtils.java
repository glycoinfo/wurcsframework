package org.glycoinfo.WURCSFramework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WURCSStringUtils {

	/**
	 * convert to URI string
	 * @param a_strUrl is a string
	 * @return encoded String
	 */
	public static String getURLString(String a_strUrl){
		
		try {
			a_strUrl = URLEncoder.encode(a_strUrl,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a_strUrl;
	}


}
