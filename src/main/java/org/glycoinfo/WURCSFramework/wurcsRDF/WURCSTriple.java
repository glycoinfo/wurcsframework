package org.glycoinfo.WURCSFramework.wurcsRDF;

public class WURCSTriple{
//	private String accessionNumber;
	private String subject;
	private String predicate;
	private Object object;
		
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
}
