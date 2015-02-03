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
			StringBuilder  t_sbBasetpe = new StringBuilder();
			WURCSExporter export = new WURCSExporter();
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
				// remove modification of a hydrogen on OH groups. 
				// remove ring modification
				if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") )
					t_sbBasetpe.append("_" + export.getMODString(mod));
			}
			return t_sbBasetpe.toString();
	}


}
