package org.glycoinfo.WURCSFramework.util.wurcsjson;

public enum WURCSSuperClass {
	
	c1("1", ""),
	c2("2", ""),
	c3("3", "TRI"),
	c4("4", "TET"),
	c5("5", "PEN"),
	c6("6", "HEX"),
	c7("7", "HEP"),
	c8("8", "OCT"),
	c9("9", "NON"),
	C10("10", "DEC");
	
	private String size;
	private String superClass;
	
	//monosaccharide construct
	private WURCSSuperClass(String _size, String _superclass) {
		this.size = _size;
		this.superClass = _superclass;
	}

	/*
	 * @param : SkeletonCode
	 * @return : Glycan builder base sugar basetype
	 */
	public static WURCSSuperClass getSuperClass(String str) {
		WURCSSuperClass[] enumArray = WURCSSuperClass.values();

		for(WURCSSuperClass enumStr : enumArray) {
			if (str.equals(enumStr.size.toString()))
				return enumStr;
		}
		return null;
	}

	public String getMonosaccharideSize() {
		return this.size;
	}
	
	public String getSuperClass() {
		return this.superClass;
	}
	
	@Override
	public String toString() {
		return size;
	}
}
