package org.glycoinfo.WURCSFramework.util.array;

import org.glycoinfo.WURCSFramework.util.WURCSException;


/**
 * Class for format exception in parsing WURCS string
 * @author MasaakiMatsubara
 */
public class WURCSFormatException extends WURCSException {

	private String m_strInput = null;

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
		super(a_strMessage, a_objThrowable);
	}

	/**
	 *
	 * @param a_strMessage
	 * @param a_strInput
	 */
	public WURCSFormatException(String a_strMessage, String a_strInput) {
		this(a_strMessage);
		this.m_strInput = a_strInput;
	}

	public String getInputString() {
		return this.m_strInput;
	}

}
