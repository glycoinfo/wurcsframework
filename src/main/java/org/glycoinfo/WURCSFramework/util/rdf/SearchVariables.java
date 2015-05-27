package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;

public class SearchVariables {

	String strGlycoSeqVariable;
	
	public void setGlycoSequenceUri(String strGlycoSeqVariable) {
		this.strGlycoSeqVariable = strGlycoSeqVariable;
	}

	public String getGlycoSequenceUri() {
		return strGlycoSeqVariable;
	}

}
