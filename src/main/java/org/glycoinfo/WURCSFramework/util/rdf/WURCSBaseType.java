package org.glycoinfo.WURCSFramework.util.rdf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSNumberUtils;
import org.glycoinfo.WURCSFramework.util.WURCSStringUtils;
import org.glycoinfo.WURCSFramework.wurcs.FuzzyGLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.LIP;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;

//TODO: 
public class WURCSBaseType {
		

	// TODO: basetype string creation 
	public static String getBaseType(UniqueRES uRes) {
		
		// TODO: input = "WURCS=2.0/2,4,3/[x122h-1x_1-4][x<nx>h-?x_?-?]/1-1-2-2/a5-b1_b3-c1_b5-d1";
		
			StringBuilder  t_sbBasetpe = new StringBuilder();
			WURCSExporter export = new WURCSExporter();
			int m_iAnomericposition = uRes.getAnomericPosition();
			// remove anomeric Carbon Descriptor
			if (m_iAnomericposition == 1) {
				// aldose
				t_sbBasetpe.append("u" + uRes.getSkeletonCode().substring(1));
			}
			// replace keto position carbondescriptor to "k"
			else if (m_iAnomericposition > 1) {
				// ketose
				t_sbBasetpe.append(uRes.getSkeletonCode().substring(0,m_iAnomericposition - 1) + "U" + uRes.getSkeletonCode().substring(m_iAnomericposition));
			}
			else {
				t_sbBasetpe.append(uRes.getSkeletonCode());
			}
			
//			System.out.println("SkeletonCode:" + uRes.getSkeletonCode());
//			System.out.println("t_sbBasetpe:" + t_sbBasetpe);
			
			String m_strBaseType = t_sbBasetpe.toString();
			
			if (m_strBaseType.length() > 1) {
				if (m_strBaseType.substring(0,1).equals("o") || m_strBaseType.substring(0,1).equals("O")) {
					m_strBaseType = "u" + m_strBaseType.substring(1);
				}
				if (m_strBaseType.substring(0,1).equals("o") || m_strBaseType.substring(0,1).equals("O")) {
					m_strBaseType = "U" + m_strBaseType.substring(1);
				}
			}
			
			// carbonDescriptor
//			m_strBaseType = m_strBaseType.replaceAll("d", "c"); // terminal
//			m_strBaseType = m_strBaseType.replaceAll("d", "d"); // non-terminal
			
//			m_strBaseType = m_strBaseType.replaceAll("C", "M");
//			m_strBaseType = m_strBaseType.replaceAll("D", "M"); // terminal
//			m_strBaseType = m_strBaseType.replaceAll("D", "c"); // non-terminal
//			m_strBaseType = m_strBaseType.replaceAll("O", "o");
			
//			m_strBaseType = m_strBaseType.replaceAll("H", "h");
//			m_strBaseType = m_strBaseType.replaceAll("A", "a"); //
//			m_strBaseType = m_strBaseType.replaceAll("K", "k"); //
			
			

			
			
			StringBuilder  t_sbMod = new StringBuilder();
			if (m_strBaseType.length() > 0) {
				for (MOD mod : uRes.getMODs()) {
					// remove modification of a hydrogen on OH groups. 
					// remove ring modification
					if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") )
						t_sbMod.append("_" + export.getMODString(mod));
				}
			}
			return m_strBaseType + t_sbMod.toString();
	}


}
