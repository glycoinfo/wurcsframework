package org.glycoinfo.WURCSFramework.util.graph.visitor;

import org.glycoinfo.WURCSFramework.util.WURCSException;

public class WURCSVisitorException extends WURCSException {

	public WURCSVisitorException(String a_strMessage) {
		super(a_strMessage);
	}

	public WURCSVisitorException(String a_strMessage,Throwable a_objThrowable) {
		super(a_strMessage, a_objThrowable);
	}
}
