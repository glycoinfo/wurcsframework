package org.glycoinfo.WURCSFramework.util.array.mass;

/**
 * Class for exception of WURCS mass calculator
 * @author MasaakiMatsubara
 *
 */
public class WURCSMassException extends Exception {

	private static final long serialVersionUID = 1L;
	protected String m_strMessage;

	/**
	 * Constructor
	 * @param a_strMessage
	 */
	public WURCSMassException(String a_strMessage) {
		super(a_strMessage);
		this.m_strMessage = a_strMessage;
	}

	/**
	 * Constructor with throwable
	 * @param a_strMessage
	 */
	public WURCSMassException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage,a_objThrowable);
		this.m_strMessage = a_strMessage;
	}

	public String getErrorMessage() {
		return this.m_strMessage;
	}

}
