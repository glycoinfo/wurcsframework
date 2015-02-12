package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSBaseType;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.LIPs;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class WURCSrdfGLIPs {
	
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
//		WURCSExporter export = new WURCSExporter();
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
			
			
			

			// RING information for pyranose, furanose
			for (MOD mod : uRes.getMODs()) {
				
				for (LIPs lips : mod.getListOfLIPs() ) {
					for (LIP lip : lips.getLIPs()) {
						if (uRes.getAnomericPosition() == lip.getBackbonePosition()){
							t_sbMonosaccharide.append("\twurcs:has_ring	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + WURCSStringUtils.getURLString(export.getMODString(mod)) + "> ;\n");
						}
					}
				}
			}
			
			
			
			//BaseType
			t_sbMonosaccharide.append("\twurcs:has_BaseType	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/BaseType/" 
					+ WURCSStringUtils.getURLString(WURCSBaseType.getBaseType( uRes )) + "> ;\n");
			
			
			
//			System.out.println("BaseType:" + WURCSBaseType.getBaseType( uRes ));
//			System.out.println("getURLString.BaseType:" + WURCSStringUtils.getURLString(WURCSBaseType.getBaseType( uRes )));
			// getBiologicalMonosacccharide
//			t_sbMonosaccharide.append("\twurcs:has_BiologicalMonosacccharide	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/BiologicalMonosacccharide/" 
//					+ WURCSStringUtils.getURLString(this.getBiologicalMonosacccharide( uRes )) + "> ;\n");
			
			
			
			
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
		
		// basetype
		t_sbMonosaccharide.append("# BaseType\n");
		for (UniqueRES uRes : a_aUniqueRESs) {
			//getBaseType
			t_sbMonosaccharide.append("<http://rdf.glycoinfo.org/glycan/wurcs/2.0/BaseType/" + WURCSStringUtils.getURLString(WURCSBaseType.getBaseType( uRes )) + ">\n");
			t_sbMonosaccharide.append("\twurcs:BaseType\t\"" + WURCSBaseType.getBaseType( uRes ) + "\"^^xsd:string .\n");
		}
		t_sbMonosaccharide.append("\n");
		
		
		
		
		
		t_sbMonosaccharide.append("# MOD\n");
		// MOD
		String endString = ";";
		
		
//		LinkedList<String> m_aMod = new LinkedList<String>();
		
		for (UniqueRES uRes : a_aUniqueRESs) {
			
			for (MOD mod : uRes.getMODs()) {
				endString = ";";
				
				t_sbMonosaccharide.append("<http://rdf.glycoinfo.org/glycan/wurcs/2.0/MOD/" + WURCSStringUtils.getURLString(export.getMODString(mod)) + "> \n");
				t_sbMonosaccharide.append("\ta	wurcs:MOD ;\n");

//				if (mod.getLIPs().size() > 0) endString = ";";
				endString = (mod.getListOfLIPs().size() > 0) ? ";": ".";
				if (mod.getMAPCode().length() > 0) {
					t_sbMonosaccharide.append("\t	wurcs:has_MAP \"" + WURCSStringUtils.getURLString(mod.getMAPCode()) + "\"^^xsd:string " + endString + "\n");
				}
				int j = 1;
				for (LIPs lips: mod.getListOfLIPs()) {
					endString = (mod.getListOfLIPs().size() == j) ? "." : ";";
					t_sbMonosaccharide.append("\twurcs:has_LIP	<http://rdf.glycoinfo.org/glycan/wurcs/2.0/LIP/" + WURCSStringUtils.getURLString(export.getLIPsString(lips)) + "> " + endString + "\n");
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
			
			// # 
			t_sbGlycan.append("##\n");
			
			// # WURCS
			String t_strWURCSString = export.getWURCSString(a_oWURCS);
			t_sbGlycan.append("# " + t_strWURCSString + "\n");
			
			// # AccessionNumber
			t_sbGlycan.append("# " + a_strAccessionNumber + "\n");
			t_sbGlycan.append("\n");
			
			// # Saccharide Triple
			SaccharideTriple saccharide= new SaccharideTriple(a_strAccessionNumber);
			t_sbGlycan.append(saccharide.get_SaccharideTtl(a_bPrefix));
			t_sbGlycan.append("\n");
			
			// Glycosequence Triple
			GlycosequenceTriple glycoseq = new GlycosequenceTriple(a_strAccessionNumber, a_oWURCS);
			t_sbGlycan.append(glycoseq.get_GlycosequenceTriple(a_oWURCS, a_bPrefix));
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
			LINTripleGLIPs lint = new LINTripleGLIPs(a_strAccessionNumber, a_oWURCS.getLINs());
			t_sbGlycan.append(lint.getLINTriple(a_bPrefix));
//			t_sbGlycan.append("\n");
			
/*			//TODO: 
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
			t_sbGlycan.append("# GLIPS\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (FuzzyGLIP fglip : lin.getFuzzyGLIPs()) {

						FuzzyGLIPTriple fglipTriple = new FuzzyGLIPTriple(a_strAccessionNumber, fglip);
						t_sbGlycan.append(fglipTriple.getFuzzyGLIPTriple(a_bPrefix));
				}
			}
//			t_sbGlycan.append("\n");
*/
			
			//TODO: 
			// GLIPS
			t_sbGlycan.append("# GLIPS\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (GLIPs fglip : lin.getListOfGLIPs()) {

					GLIPSTriple fglipTriple = new GLIPSTriple(a_strAccessionNumber, fglip);
						t_sbGlycan.append(fglipTriple.getGLIPSTriple(a_bPrefix));
				}
			}
			
			
			
			//TODO: 
			// GLIP of fuzzyGLIP
			t_sbGlycan.append("# GLIP\n");
			for (LIN lin : a_oWURCS.getLINs()) {
				for (GLIPs fglip : lin.getListOfGLIPs()) {
					for (GLIP glip : fglip.getGLIPs()) {
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
	
/*
	// TODO: basetype string creation 
	private String getBaseType(UniqueRES uRes) {
			StringBuilder  t_sbBasetpe = new StringBuilder();
			WURCSExporterForGLIPs export = new WURCSExporterForGLIPs();
			int m_iAnomericposition = uRes.getAnomericPosition();
			// remove anomeric Carbon Descriptor
			if (m_iAnomericposition == 1) {
				t_sbBasetpe.append("o" + uRes.getSkeletonCode().substring(1));
			}
			// replace keto position carbondescriptor to "k"
			else if (m_iAnomericposition > 1) {
				t_sbBasetpe.append(uRes.getSkeletonCode().substring(0,m_iAnomericposition - 1) + "k" + uRes.getSkeletonCode().substring(m_iAnomericposition));
			}
			for (MOD mod : uRes.getMODs()) {
				if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") )
					t_sbBasetpe.append("_" + export.getMODString(mod));
			}
			return t_sbBasetpe.toString();
	}
*/
/*
	private String getBiologicalMonosacccharide(UniqueRES uRes) {
		StringBuilder  t_sb = new StringBuilder();
		WURCSExporterForGLIPs export = new WURCSExporterForGLIPs();
		t_sb.append(uRes.getSkeletonCode());
		t_sb.append("-");
		t_sb.append(uRes.getAnomericPosition());
		t_sb.append(uRes.getAnomericSymbol());
		
		for (MOD mod : uRes.getMODs()) {
			if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") )
				t_sb.append("_" + export.getMODString(mod));
		}
		return t_sb.toString();
	}
*/
	
	
}