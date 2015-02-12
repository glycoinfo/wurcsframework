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
		
		String a_strUrl3 = "";
		try {
			String a_strUrl1 = URLEncoder.encode(a_strUrl,"utf-8");
			String a_strUrl2 = a_strUrl1.replaceAll("<", "%3C");
			a_strUrl3 = a_strUrl2.replaceAll(">", "%3E");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a_strUrl3;
	}


}
