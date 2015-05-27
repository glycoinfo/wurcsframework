package org.glycoinfo.WURCSFramework.util.rdf;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSMonosaccharideIntegrator;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;

//TODO:
public class WURCSBasetype {


	// TODO: basetype string creation
	public static String getBasetype(UniqueRES uRes) {

			StringBuilder t_sbBasetpe = new StringBuilder();
			t_sbBasetpe.append(
				new WURCSExporter().getUniqueRESString( WURCSMonosaccharideIntegrator.convertBasetype(uRes) )
			);
			return t_sbBasetpe.toString();
	}
}
