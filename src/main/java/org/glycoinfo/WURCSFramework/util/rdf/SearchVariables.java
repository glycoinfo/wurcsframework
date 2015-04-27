package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

public class SearchVariables {

	String strGlycoSeqVariable;
	
	public void setGlycoSequenceUri(String strGlycoSeqVariable) {
		this.strGlycoSeqVariable = strGlycoSeqVariable;
	}

	public String getGlycoSequenceUri() {
		return strGlycoSeqVariable;
	}

}
