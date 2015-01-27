package org.glycoinfo.WURCSFramework.util;

public class WURCSNumberUtils {

	/**
	 * Is the character a integer?
	 * @param a_strNumber is a string
	 * @return if integer return true;
	 */
	public static boolean isInteger(String a_strNumber) {
		try {
			Integer.parseInt(a_strNumber);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}

	/**
	 * Is the character a double?
	 * @param a_strNumber is a string
	 * @return if double return true;
	 */
	public static boolean isDouble(String a_strNumber) {
		try {
			Double.parseDouble(a_strNumber);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}


}
