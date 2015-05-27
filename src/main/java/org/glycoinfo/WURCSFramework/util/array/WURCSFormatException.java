package org.glycoinfo.WURCSFramework.util.array;

import org.glycoinfo.WURCSFramework.util.WURCSException;


/**
 * Class for format exception in parsing WURCS string
 * @author MasaakiMatsubara
 */
public class WURCSFormatException extends WURCSException {

	/**
	 * @see org.glycoinfo.WURCSFramework.util.WURCSException
	 */
	public WURCSFormatException(String a_strMessage) {
		super(a_strMessage);
	}

	/**
	 * @see org.glycoinfo.WURCSFramework.util.WURCSException
	 */
	public WURCSFormatException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage,a_objThrowable);
	}

}
