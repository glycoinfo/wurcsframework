package org.glycoinfo.WURCSFramework.wurcs;

/**
 * Class for exception of WURCS object
 * @author MasaakiMatsubara
 */
public class WURCSFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	protected String m_strMessage;

	/**
	 * Constructor
	 * @param a_strMessage
	 */
	public WURCSFormatException(String a_strMessage) {
		super(a_strMessage);
		this.m_strMessage = a_strMessage;
	}

	/**
	 * Constructor with throwable
	 * @param a_strMessage
	 */
	public WURCSFormatException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage,a_objThrowable);
		this.m_strMessage = a_strMessage;
	}

	public String getErrorMessage() {
		return this.m_strMessage;
	}
}
