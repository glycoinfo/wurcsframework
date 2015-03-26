package org.glycoinfo.WURCSFramework.util.comparator.graph;

import java.util.Comparator;
import java.util.HashSet;

import org.glycoinfo.WURCSFramework.graph.Backbone;
import org.glycoinfo.WURCSFramework.graph.Modification;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorCollectSubBackbones;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorCollectSubModifications;
import org.glycoinfo.WURCSFramework.util.visitor.graph.WURCSVisitorException;

public class RootBackboneComparator implements Comparator<Backbone>{

	@Override
	public int compare(Backbone b1, Backbone b2) {

		HashSet<Backbone> t_aB1 = this.getSubBackbones(b1);
		HashSet<Backbone> t_aB2 = this.getSubBackbones(b2);
		if (  t_aB1.contains(b2) && !t_aB2.contains(b1) ) return -1;
		if ( !t_aB1.contains(b2) &&  t_aB2.contains(b1) ) return 1;

		HashSet<Modification> t_aM1 = this.getSubModifications(b1);
		HashSet<Modification> t_aM2 = this.getSubModifications(b2);
		Modification m1 = null;
		Modification m2 = null;
		if ( b1.getAnomericEdge() != null ) m1 = b1.getAnomericEdge().getModification();
		if ( b2.getAnomericEdge() != null ) m2 = b2.getAnomericEdge().getModification();

		if ( m1 != null && m2 != null ) {
			// For parent modifications are same
			if ( m1 == m2 ) return (new BackboneComparator()).compare(b1, b2);
			if (  t_aM1.contains(m2) && !t_aM2.contains(m1) ) return -1;
			if ( !t_aM1.contains(m2) &&  t_aM2.contains(m1) ) return 1;
		}
		if ( m2 != null && t_aM1.contains(m2) ) return -1;
		if ( m1 != null && t_aM2.contains(m1) ) return 1;


		return 0;
	}

	private HashSet<Backbone> getSubBackbones(Backbone b) {
		WURCSVisitorCollectSubBackbones t_oCollect = new WURCSVisitorCollectSubBackbones();
		try {
			t_oCollect.start(b);
			return t_oCollect.getBackbones();
		} catch (WURCSVisitorException e) {
			return new HashSet<Backbone>();
		}
	}

	private HashSet<Modification> getSubModifications(Backbone b) {
		WURCSVisitorCollectSubModifications t_oCollect = new WURCSVisitorCollectSubModifications();
		try {
			t_oCollect.start(b);
			return t_oCollect.getModifications();
		} catch (WURCSVisitorException e) {
			return new HashSet<Modification>();
		}
	}
}
