package org.glycoinfo.WURCSFramework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility class for string in WURCS
 * @author Issaku Yamada
 * @author Masaaki Matsubara
 *
 */
public class WURCSStringUtils {

	/**
	 * Convert to URI string
	 * @param a_strUrl is a target String
	 * @return encoded String
	 */
	public static String getURLString(String a_strUrl){

		String t_strUrl = "";
		try {
			t_strUrl = URLEncoder.encode(a_strUrl,"utf-8");
			t_strUrl = t_strUrl.replaceAll("<", "%3C");
			t_strUrl = t_strUrl.replaceAll(">", "%3E");
			t_strUrl = t_strUrl.replaceAll("\\*", "%2A");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return t_strUrl;
	}


}
