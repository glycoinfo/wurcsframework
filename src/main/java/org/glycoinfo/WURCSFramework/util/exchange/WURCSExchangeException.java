package org.glycoinfo.WURCSFramework.util.exchange;

import org.glycoinfo.WURCSFramework.wurcs.WURCSException;

/**
 * Class for exception in exchange between WURCS objects
 * @author MasaakiMatsubara
 *
 */
public class WURCSExchangeException extends WURCSException {

	/**
	 * @see org.glycoinfo.WURCSFramework.wurcs.WURCSException
	 */
	public WURCSExchangeException(String a_strMessage) {
		super(a_strMessage);
	}

	/**
	 * @see org.glycoinfo.WURCSFramework.wurcs.WURCSException
	 */
	public WURCSExchangeException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage,a_objThrowable);
	}

}
