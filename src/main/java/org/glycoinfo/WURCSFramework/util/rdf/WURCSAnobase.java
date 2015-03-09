package org.glycoinfo.WURCSFramework.util.rdf;

import org.glycoinfo.WURCSFramework.util.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;

//TODO:
public class WURCSAnobase {


	// TODO: anobase string creation
	public static String getAnobase(UniqueRES uRes) {

		StringBuilder t_sbAnobase = new StringBuilder();
		t_sbAnobase.append(
			new WURCSExporter().getUniqueRESString( WURCSMonosaccharideIntegrator.convertAnobase(uRes) )
		);
		return t_sbAnobase.toString();
	}

}
