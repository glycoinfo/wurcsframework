package org.glycoinfo.WURCSFramework.exec;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorStripTypeIIIModification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class StripSubstituentForWURCS {

	public static void main(String[] args) {
		WURCSVisitorStripTypeIIIModification t_oStripper = new WURCSVisitorStripTypeIIIModification();
		for ( String t_strWURCS : args ) {
			try {
				WURCSFactory t_oFactoryIn = new WURCSFactory(t_strWURCS);
				WURCSGraph t_oGraph = t_oFactoryIn.getGraph();

				t_oStripper.start(t_oGraph);
				WURCSGraph t_oStripped = t_oStripper.getStrippedGraph();
				WURCSFactory t_oFactoryOut = new WURCSFactory(t_oStripped);
				System.out.println( t_oFactoryOut.getWURCS() );
			} catch (WURCSException e) {
				System.err.println( e.getErrorMessage() );
				e.printStackTrace();
				continue;
			}
		}

	}

}
