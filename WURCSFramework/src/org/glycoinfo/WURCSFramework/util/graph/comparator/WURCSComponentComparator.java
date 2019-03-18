package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.Monosaccharide;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSComponent;

public class WURCSComponentComparator implements Comparator<WURCSComponent> {

	@Override
	public int compare(WURCSComponent o1, WURCSComponent o2) {

		// Prioritize root node
		if (  o1.isRoot() && !o2.isRoot() ) return -1;
		if ( !o1.isRoot() &&  o2.isRoot() ) return 1;

		// Prioritize smaller number of parent
//		if ( o1.getParentEdges().size() < o2.getParentEdges().size() ) return -1;
//		if ( o1.getParentEdges().size() > o2.getParentEdges().size() ) return 1;

		// Prioritize Backbone than Modification
		if ( o1 instanceof Backbone && o2 instanceof Modification ) return -1;
		if ( o1 instanceof Modification && o2 instanceof Backbone ) return 1;

		// For Backbones
		if ( o1 instanceof Backbone && o2 instanceof Backbone ) {
			Monosaccharide t_oMS1 = new Monosaccharide( (Backbone)o1 );
			Monosaccharide t_oMS2 = new Monosaccharide( (Backbone)o2 );
			MonosaccharideComparator t_oComp = new MonosaccharideComparator();
			int t_iComp = t_oComp.compare(t_oMS1, t_oMS2);
//			System.err.println("MS: "+t_iComp);
/*
			Backbone t_oB1 = (Backbone)o1;
			Backbone t_oB2 = (Backbone)o2;
			BackboneComparator t_oComp = new BackboneComparator();
			int t_iComp = t_oComp.compare(t_oB1, t_oB2);
*/
			if ( t_iComp != 0 ) return t_iComp;
		}

		// For Modifications
// muller modifyed 180606
// 007-smnapshot original source code
//		if ( o1 instanceof Modification && o2 instanceof Modification ) {
//			Modification t_oM1 = (Modification)o1;
//			Modification t_oM2 = (Modification)o2;
//			ModificationComparator t_oComp = new ModificationComparator();
//			int t_iComp = t_oComp.compare(t_oM1, t_oM2);
//			if ( t_iComp != 0 ) return t_iComp;
//		}
// end of original code
		int t_oNEdge1=o1.getEdges().size();
		int t_oNEdge2=o2.getEdges().size();
		int t_oNEdge =(t_oNEdge1 < t_oNEdge2 ) ? t_oNEdge1: t_oNEdge2;
		ModificationComparator t_oComp = new ModificationComparator();
		for (int i=0;i<t_oNEdge;i++) {
			Modification t_oM1=o1.getEdges().get(i).getModification();
			Modification t_oM2=o2.getEdges().get(i).getModification();
			if(t_oM1.getMAPCode().length()!=0&&t_oM2.getMAPCode().length()!=0) {
				int t_iComp=t_oComp.compare(t_oM1, t_oM2);
				if(t_iComp!=0) return t_iComp;
			}
		}
// muller end modify

		//
		int t_nEdge1 = o1.getEdges().size();
		int t_nEdge2 = o2.getEdges().size();

		return 0;
	}

}
