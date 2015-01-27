package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class WURCSrdf {
	
//	private String m_strAccessionNumber;
	private String m_strWURCS_RDF;
	private String m_strWURCS_monosaccharide_RDF;
	
	public void setWURCSrdfTriple(String a_strAccessionNumber, WURCSArray a_oWURCS, Boolean a_bPrefix){
		this.setWURCSmonosaccharideTriple(a_oWURCS.getUniqueRESs(), a_bPrefix);
		this.setWURCSglycanTriple(a_strAccessionNumber, a_oWURCS, a_bPrefix);
	}
	
	
	//TODO: 
	public String getWURCS_RDF(){
		return m_strWURCS_RDF;
	}
	//TODO: 
	public String getWURCS_monosaccharide_RDF(){
		return m_strWURCS_monosaccharide_RDF;
	}
	
	private void setWURCSmonosaccharideTriple(LinkedList<UniqueRES> a_aUniqueRESs, Boolean a_bPrefix) {
		WURCSExporter export = new WURCSExporter();
		StringBuilder t_sbMonosaccharide = new StringBuilder();		
		t_sbMonosaccharide.append("# monosaccharide\n");
		
		int i = 1;
		for (UniqueRES uRes : a_aUniqueRESs) {
			String endString = ".";
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				break;
			}
			String t_strMS = "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/" 
					+ WURCSStringUtils.getURLString(export.getUniqueRESString(uRes)) + ">";
			
			
//			t_strMonosaccharideRdfXml.append("# monosaccharide\n");
			t_sbMonosaccharide.append(t_strMS + "\n");
			
			t_sbMonosaccharide.append("\ta	wurcs:monosaccharide ; \n");
			
			t_sbMonosaccharide.append("\twurcs:has_SC	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/SkeletonCode/" 
					+ WURCSStringUtils.getURLString(uRes.getSkeletonCode()) + "> ;\n");
			t_sbMonosaccharide.append("\twurcs:has_anomeric_symbol\t\"" + uRes.getAnomericSymbol() + "\"^^xsd:string ;\n");
			t_sbMonosaccharide.append("\twurcs:has_anomeric_position\t\"" + uRes.getAnomericPosition() + "\"^^xsd:integer " + endString + "\n");
			int j = 1;
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				if (uRes.getMODs().size() == j) endString = ".";
				t_sbMonosaccharide.append("\twurcs:has_MOD	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + WURCSStringUtils.getURLString(export.getMODString(mod)) + "> " + endString + "\n");
				j++;
			}
			t_sbMonosaccharide.append("\n");
			i++;
		}
		
		t_sbMonosaccharide.append("# MOD\n");
		// MOD
		String endString = ";";
		
		for (UniqueRES uRes : a_aUniqueRESs) {
			
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				
				t_sbMonosaccharide.append("<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + WURCSStringUtils.getURLString(export.getMODString(mod)) + "> \n");
				t_sbMonosaccharide.append("\ta	wurcs:MOD ;\n");

//				if (mod.getLIPs().size() > 0) endString = ";";
				endString = (mod.getLIPs().size() > 0) ? ";": ".";
				if (mod.getMAPCode().length() > 0) {
					t_sbMonosaccharide.append("\t	wurcs:has_MAP \"" + WURCSStringUtils.getURLString(mod.getMAPCode()) + "\"^^xsd:string " + endString + "\n");
				}
				int j = 1;
				for (LIP lip: mod.getLIPs()) {
					endString = (mod.getLIPs().size() == j) ? "." : ";";
					t_sbMonosaccharide.append("\twurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/" + WURCSStringUtils.getURLString(export.getLIPString(lip)) + "> " + endString + "\n");
					j++;
				}
				

				t_sbMonosaccharide.append("\n");
				
			}
			t_sbMonosaccharide.append("\n");
		}
		t_sbMonosaccharide.append("\n");
		this.m_strWURCS_monosaccharide_RDF = t_sbMonosaccharide.toString();
	}
	
	
	// TODO: 
	private void setWURCSglycanTriple(String a_strAccessionNumber, WURCSArray a_oWURCS, Boolean a_bPrefix)
	{
			StringBuilder t_sbGlycan= new StringBuilder();
			
			WURCSExporter export = new WURCSExporter();
			
			// # WURCS
			String t_strWURCSString = export.getWURCSString(a_oWURCS);
			t_sbGlycan.append("#" + t_strWURCSString + "\n");
			
			// # AccessionNumber
			t_sbGlycan.append("#" + a_strAccessionNumber + "\n");
			t_sbGlycan.append("\n");
			
			// # Saccharide Triple
			SaccharideTriple saccharide= new SaccharideTriple(a_strAccessionNumber);
			t_sbGlycan.append(saccharide.get_SaccharideTtl(a_bPrefix));
			t_sbGlycan.append("\n");
			
			// Glycosequence Triple
			GlycosequenceTriple glycoseq = new GlycosequenceTriple(a_strAccessionNumber, a_oWURCS);
			t_sbGlycan.append(glycoseq.get_GlycosequenceTriple(a_bPrefix));
			t_sbGlycan.append("\n");
			
			// uniqueRES Triple
			UniqueRESTriple ures = new UniqueRESTriple(a_strAccessionNumber, a_oWURCS.getUniqueRESs());
			t_sbGlycan.append(ures.get_UniqueRESTriples(a_bPrefix));
			t_sbGlycan.append("\n");
			
			// RES Triple
			RESTriple ress = new RESTriple(a_strAccessionNumber, a_oWURCS);
			t_sbGlycan.append(ress.getRESTriple(a_bPrefix));
//			t_sbGlycan.append("\n");
			
			// LIN Triple
			LINTriple lint = new LINTriple(a_strAccessionNumber, a_oWURCS.getLINs());
			t_sbGlycan.append(lint.getLINTriple(a_bPrefix));
//			t_sbGlycan.append("\n");
			
			//TODO: 
			// GLIP
			t_sbGlycan.append("# GLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (GLIP glip : lin.getGLIPs()) {
					GLIPTriple gliptri = new GLIPTriple(
							a_strAccessionNumber
							, export.getGLIPString(glip)
							, glip.getRESIndex()
							, glip.getBackbonePosition()
							, glip.getBackboneDirection()
							, glip.getModificationPosition()
							);
					t_sbGlycan.append(gliptri.getGLIPTriple(a_bPrefix));
				}
			}
			
			//TODO: 
			// FuzzyGLIP
			t_sbGlycan.append("# fuzzyGLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {

						FuzzyGLIPTriple fglipTriple = new FuzzyGLIPTriple(a_strAccessionNumber, fglip);
						t_sbGlycan.append(fglipTriple.getFuzzyGLIPTriple(a_bPrefix));
				}
			}
//			t_sbGlycan.append("\n");
			
			//TODO: 
			// GLIP of fuzzyGLIP
			t_sbGlycan.append("# GLIP of fuzzyGLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
					for (GLIP glip : fglip.getFuzzyGLIPs()) {
						GLIPTriple gliptri = new GLIPTriple(
								a_strAccessionNumber
								, export.getGLIPString(glip)
								, glip.getRESIndex()
								, glip.getBackbonePosition()
								, glip.getBackboneDirection()
								, glip.getModificationPosition()
								);
						t_sbGlycan.append(gliptri.getGLIPTriple(a_bPrefix));
					}
				}
			}

//			t_sbGlycan.append("\n");

/*			
			t_sbGlycan.append("# -------------------\n");			
			String a_strAccessionNumberURI = "<http://rdf.glycoinfo.org/glycan/" + a_strAccessionNumber;
			String endString = ";";
			int i = 1;
*/
			
/*			
			// fuzzyGLIP
			t_sbGlycan.append("# fuzzyGLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
					endString = ";";
					t_sbGlycan.append(a_strAccessionNumberURI + "/wurcs/2.0/fuzzyGLIP/" + WURCSStringUtils.getURLString(export.getFuzzyGLIPString(fglip)) + "> \n");
					t_sbGlycan.append("\ta	wurcs:fuzzyGLIP ;\n");
					
					for (GLIP glip : fglip.getFuzzyGLIPs()) {
						t_sbGlycan.append("\twurcs:has_GLIP\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + WURCSStringUtils.getURLString(export.getGLIPString(glip)) + "> " + endString + " \n");
					}
					if (fglip.getAlternativeType().length() > 0) {
						t_sbGlycan.append("\twurcs:has_alternative\t" + a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + WURCSStringUtils.getURLString(fglip.getAlternativeType()) + "> . \n");
					}
				}
			}

			t_sbGlycan.append("\n");
*/			
			
/*			
			// GLIP of fuzzyGLIP
			t_sbGlycan.append("# GLIP of fuzzyGLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {
					for (GLIP glip : fglip.getFuzzyGLIPs()) {

						GLIPTriple gliptri = new GLIPTriple(
								a_strAccessionNumber
								, export.getGLIPString(glip)
								, glip.getRESIndex()
								, glip.getBackbonePosition()
								, glip.getBackboneDirection()
								, glip.getModificationPosition()
								);
						t_sbGlycan.append(gliptri.getGLIPTriple(a_bPrefix));
						
						
						t_sbGlycan.append(a_strAccessionNumberURI + "/wurcs/2.0/GLIP/" + WURCSStringUtils.getURLString(export.getGLIPString(glip)) + ">  \n");
						t_sbGlycan.append("\ta	wurcs:GLIP ;\n");
						t_sbGlycan.append("\t\twurcs:has_RES\t" + a_strAccessionNumberURI + "/wurcs/2.0/RES/" + glip.getRESIndex() + "> ; \n");
						if (glip.getBackboneDirection() != ' ')
							t_sbGlycan.append("\twurcs:has_direction\t\"" + glip.getBackboneDirection() + "\"^^xsd:string ; \n");
						if (glip.getModificationPosition() != 0)
								t_sbGlycan.append("\twurcs:has_MAP_position\t\"" + glip.getModificationPosition() + "\"^^xsd:integer ; \n");
						t_sbGlycan.append("\t\twurcs:has_SC_position\t\"" + glip.getBackbonePosition() + "\"^^xsd:integer .\n");
					
					}
				}
			}
*/
//			t_sbGlycan.append("\n");

		this.m_strWURCS_RDF = t_sbGlycan.toString();
//		this.m_strWURCS_monosaccharide_RDF = t_sbMonosaccharide.toString();
//		return  this;
	}
	


	//TODO: 
	public String getWURCSPrefix(){
		StringBuilder sb = new StringBuilder();
		sb.append(Prefix.getPrefixs());
		sb.append("\n");
		return sb.toString();
	}
	



}
