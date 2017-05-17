package org.glycoinfo.WURCSFramework.util;

/**
 * Class for exception of WURCS object
 * @author MasaakiMatsubara
 */
public class WURCSException extends Exception {
	private String m_strMessage;

	/**
	 *
	 * @param a_strMessage
	 */
	public WURCSException(String a_strMessage) {
		super(a_strMessage);
		this.m_strMessage = a_strMessage;
	}

	/**
	 *
	 * @param a_strMessage
	 */
	public WURCSException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage,a_objThrowable);
		this.m_strMessage = a_strMessage;
	}

	public String getErrorMessage() {
		return this.m_strMessage;
	}

	private static final long serialVersionUID = 1L;

}
