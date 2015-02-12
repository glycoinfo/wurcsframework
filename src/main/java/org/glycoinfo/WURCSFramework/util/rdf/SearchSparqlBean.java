package org.glycoinfo.WURCSFramework.util.rdf;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public class SearchSparqlBean implements SearchSparql {
	String strGlycoSeqVariable;
	
	public String getExactWhere(String sequence) throws WURCSFormatException {
		return getWhere(sequence);
	}

	public String getWhere(String a_strWURCS)
			throws WURCSFormatException {
		
		String m_strHED = "";
		int m_iPosition = 1;
		String strSPAQRL = "";
		StringBuilder sb = new StringBuilder();

		String m_strError = "";
		if (a_strWURCS.contains("%")) {
			m_strError += "#        not support this WURCS String with Probability (%) \n";
			throw new WURCSFormatException(m_strError);
		}
		if (a_strWURCS.contains("~")) {
			m_strError += "#        not support this WURCS String with repeat unit (~)\n";
			throw new WURCSFormatException(m_strError);
		}

		WURCSImporter ws = new WURCSImporter();

		WURCSArray m_oWURCSArray = ws.extractWURCSArray(a_strWURCS);
		WURCSExporter export = new WURCSExporter();

		sb.append("?" + strGlycoSeqVariable + " wurcs:has_uniqueRES ");
		int m_iRES = 1;
		for (UniqueRES uRES : m_oWURCSArray.getUniqueRESs()) {
			String m_strEnd = ",";
			if (m_oWURCSArray.getUniqueRESs().size() == m_iRES) {
				m_strEnd = " .";
			}
			sb.append(" ?uRES" + uRES.getUniqueRESID() + m_strEnd);
			m_iRES++;
		}
		sb.append("\n");

		for (UniqueRES uRES : m_oWURCSArray.getUniqueRESs()) {
			sb.append("  ?uRES"
					+ uRES.getUniqueRESID()
					+ " wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/");
			sb.append(WURCSStringUtils.getURLString(export
					.getUniqueRESString(uRES)) + "> .\n");
		}

		// ?RESa wurcs:is_uniqueRES ?uRES1 .
		sb.append("# RES\n");
		for (RES m_aRES : m_oWURCSArray.getRESs()) {
			sb.append("  ?RES" + m_aRES.getRESIndex()
					+ " wurcs:is_uniqueRES ?uRES" + m_aRES.getUniqueRESID()
					+ " .\n");
		}

		// has_LIN Section
		// ?gseq wurcs:has_LIN ?LIN1, ?LIN2, ?LIN3
		sb.append("# LIN\n");
		int m_iLIN = 1;
		sb.append("  ?gseq wurcs:has_LIN ");
		for (LIN a_oLIN : m_oWURCSArray.getLINs()) {
			String m_strEnd = ",";
			if (m_oWURCSArray.getLINs().size() == m_iLIN) {
				m_strEnd = ".";
			}
			sb.append("?LIN"
					+ this.removeChar4SPARQL(export.getLINString(a_oLIN)) + " "
					+ m_strEnd + " ");
			m_iLIN++;

		}
		sb.append("\n");

		// has_GLIPS Section

		// sb.append("# LIN\n");
		int m_iLINhas_GLIPS = 0;
		for (LIN a_oLIN : m_oWURCSArray.getLINs()) {
			// GLIP List in LIN
			// ?LIN1 wurcs:has_GLIPS ?GLIPS1, ?GLIPS2 . ?GLIPS3, ...,
			m_iLINhas_GLIPS++;
			sb.append("# LIN" + m_iLINhas_GLIPS + "\n");

			sb.append("  ?LIN"
					+ this.removeChar4SPARQL(export.getLINString(a_oLIN))
					+ " wurcs:has_GLIPS ");

			LinkedList<String> m_aGLIPS = new LinkedList<String>();
			for (GLIPs a_oGLIPs : a_oLIN.getListOfGLIPs()) {
				m_aGLIPS.add("?GLIPS"
						+ this.removeChar4SPARQL(export
								.getGLIPsString(a_oGLIPs)));
			}
			String m_strEnd = ",";
			int m_iGLIPS = 1;
			for (String strGLIPS : m_aGLIPS) {
				if (m_aGLIPS.size() == m_iGLIPS) {
					m_strEnd = ".";
				}
				sb.append("  " + strGLIPS + " " + m_strEnd + " ");
				m_iGLIPS++;
			}
			sb.append(" \n");

			// repeat unit
			if (a_oLIN.isRepeatingUnit()) {
				sb.append("  ?LIN"
						+ this.removeChar4SPARQL(export.getLINString(a_oLIN))
						+ "  wurcs:is_repeat \"true\"^^xsd:boolean .\n");
			}
		}
		sb.append(" \n");

		// sb.append("# LIN1\n");
		int m_iLINGLIPS = 0;
		for (LIN a_oLIN : m_oWURCSArray.getLINs()) {

			m_iLINGLIPS++;
			// TODO:
			// <GLIPS>
			int m_iGLIPS = 0;
			for (GLIPs a_oGLIPS : a_oLIN.getListOfGLIPs()) {
				m_iGLIPS++;
				sb.append("# LIN" + m_iLINGLIPS + ": GLIPS" + m_iGLIPS + "\n");

				sb.append("  ?GLIPS"
						+ this.removeChar4SPARQL(export
								.getGLIPsString(a_oGLIPS))
						+ " wurcs:has_GLIP ?GLIP"
						+ this.removeChar4SPARQL(export
								.getGLIPsString(a_oGLIPS)) + " . \n");

				for (GLIP a_oGLIP : a_oGLIPS.getGLIPs()) {
					sb.append("  ?GLIP"
							+ this.removeChar4SPARQL(getGLIPSting(a_oGLIP))
							+ " wurcs:has_SC_position "
							+ a_oGLIP.getBackbonePosition() + " .\n");
					sb.append("  ?GLIP"
							+ this.removeChar4SPARQL(getGLIPSting(a_oGLIP))
							+ " wurcs:has_RES ?RES"
							+ removeChar4SPARQL(a_oGLIP.getRESIndex()) + " .\n");

				}

				// isFuzzy ?
				if (a_oGLIPS.getGLIPs().size() == 1) {
					sb.append("  ?GLIPS"
							+ this.removeChar4SPARQL(export
									.getGLIPsString(a_oGLIPS))
							+ " wurcs:isFuzzy \"false\"^^xsd:boolean .\n");
				} else {
					sb.append("  ?GLIPS"
							+ this.removeChar4SPARQL(export
									.getGLIPsString(a_oGLIPS))
							+ " wurcs:isFuzzy \"true\"^^xsd:boolean .\n");
				}

			} // end for <GLIPS>
		}

//		sb.append("}");

		strSPAQRL = sb.toString();

		return strSPAQRL;
	}

	@Override
	public void setGlycoSequenceVariable(String strGlycoSeqVariable) {
		this.strGlycoSeqVariable = strGlycoSeqVariable;
	}

	public String getGlycoSequenceVariable() {
		return strGlycoSeqVariable;
	}

	private String getGLIPSting(GLIP a_oGLIP) {
		return a_oGLIP.getRESIndex() + a_oGLIP.getBackbonePosition()
				+ a_oGLIP.getBackboneDirection();
	}

	private String removeChar4SPARQL(String a_str) {
		return a_str.replace("-", "").replace(" ", "").replace("|", "")
				.replace("~", "Repeat").replace("%", "Pro")
				.replaceAll("([a-zA-Z]\\?+)", "Question");
	}
}
