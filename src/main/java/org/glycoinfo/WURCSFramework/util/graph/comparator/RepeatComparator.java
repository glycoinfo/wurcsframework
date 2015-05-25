package org.glycoinfo.WURCSFramework.util.graph.comparator;

import java.util.Comparator;

import org.glycoinfo.WURCSFramework.wurcs.graph.InterfaceRepeat;

public class RepeatComparator implements Comparator<InterfaceRepeat> {

	@Override
	public int compare(InterfaceRepeat o1, InterfaceRepeat o2) {
		// Prioritize unknown max count
		if ( o1.getMaxRepeatCount() == -1 && o2.getMaxRepeatCount() != -1 ) return -1;
		if ( o1.getMaxRepeatCount() != -1 && o2.getMaxRepeatCount() == -1 ) return 1;

		// Prioritize unknown min count
		if ( o1.getMinRepeatCount() == -1 && o2.getMinRepeatCount() != -1 ) return -1;
		if ( o1.getMinRepeatCount() != -1 && o2.getMinRepeatCount() == -1 ) return 1;

		if ( o1.getMaxRepeatCount() == -1 && o2.getMaxRepeatCount() == -1 &&
			 o1.getMinRepeatCount() == -1 && o2.getMinRepeatCount() == -1 ) return 0;

		// Prioritize bigger count for max
		if ( o1.getMaxRepeatCount() > o2.getMaxRepeatCount() ) return -1;
		if ( o1.getMaxRepeatCount() < o2.getMaxRepeatCount() ) return 1;

		if ( o1.getMinRepeatCount() > o2.getMinRepeatCount() ) return -1;
		if ( o1.getMinRepeatCount() < o2.getMinRepeatCount() ) return 1;

		return 0;
	}

}
