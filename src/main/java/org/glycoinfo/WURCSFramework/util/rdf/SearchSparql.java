package org.glycoinfo.WURCSFramework.util.rdf;

import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public interface SearchSparql {

	String getExactWhere(String sequence) throws WURCSFormatException;
}