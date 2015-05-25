package org.glycoinfo.WURCSFramework.util.wurcsjson;

public enum WURCSstereoTemplate {

	//This enum is an index of SkeletonCode to Sugar
	//Each item are represented "Chilarity SugarName(SkeletonCode , GlycanBuilder base type, chilarity)"
	//1 is position in anomer position, D is chilarity, Glc is name of sugar, p is rings size.

	//TODO: current basetype -> WURCS basetype
	//Example of WURCS basetype, Glc is represented as below
	//D type is u12122h_2*NCC/3=O, L type is u21211h_2*NCC/3=O, and ?(unknown) is u32344h_2*NCC/3=O
	//Each member has contain three items "basetype", "chirality", "sugar name".
	
	//tri
	DGRO("u2h", 'D', "Gro"),
	LGRO("u1h", 'L', "Gro"),
	FGRO("uxh", '?', "Gro"),
	
	//tet
	DERY("u22h", 'D', "Ery"),
	LERY("u11h", 'L', "Ery"),      
	DTHR("u12h", 'D', "Thr"),
	LTHR("u21h", 'L', "Thr"),
	XTET("uxxh", '?', "Tet"),
	
	//pen
	XPEN("uxxxh", '?', "Pen"),
	DRIB("u222h", 'D', "Rib"),
	LRIB("u111h", 'L', "Rib"),     
	DARA("u122h", 'D', "Ara"),
	LARA("u211h", 'L', "Ara"),
	DXYL("u212h", 'D', "Xyl"),
	LXYL("u121h", 'L', "Xyl"),
	DLYX("u112h", 'D', "Lyx"),
	LLYX("u221h", 'L', "Lyx"), 
	DXUL("hU12h", 'D', "Xul"), //keto
	LXUL("hU21h", 'L', "Xul"), //keto
	DAPI("", 'D', "Api"),
	LAPI("", 'L', "Api"),
	//DPSI(),
	//LPSI(),
	
	//hex
	HEX("uxxxxh", '?', "Hex"),
	DALL("u2222h", 'D', "All"),
	LALL("u1111h", 'L', "All"),
	DALT("u1222h", 'D', "Alt"),
	LALT("u2111h", 'L', "Alt"),
	DGLC("u2122h", 'D', "Glc"),
	LGLC("u1211h", 'L', "Glc"),
	DGLCNAC("u2122h_2*NCC/3=O", 'D', "GlcNAc"),
	LGLCNAC("u1211h_2*NCC/3=O", 'D', "GlcNAc"),
	DGLCN("u2122h_2*N", 'D', "GlcN"),
	LGLCN("u1211h_2*N", 'L', "GlcN"),
	DGLCA("u2122a", 'D', "GlcA"),
	LGLCA("u1211a", 'L', "GlcA"),
	DMAN("u1122h", 'D', "Man"),
	LMAN("u2211h", 'L', "Man"),
	DMANNAC("u1122h_2*NCC/3=O", 'D', "ManNAc"),
	LMANNAC("u2211h_2*NCC/3=O", 'L', "ManNAc"),
	DMANN("u1122h_2*N", 'D', "ManN"),
	LMANN("u2211h_2*N", 'L', "ManN"),
	DMANA("u1122a", 'D', "ManA"),
	LMANA("u2211a", 'L', "ManA"),
	DGUL("u2212h", 'D', "Gul"),
	LGUL("u1121h", 'L', "Gul"),
	DIDO("u1212h", 'D', "Ido"),
	LIDO("u2121h", 'L', "Ido"),
	DIDONAC("u1212h_2*NCC/3=O", 'D', "IdoNAc"),
	LIDONAC("u2121h_2*NCC/3=O", 'L', "IdoNAc"),
	DIDON("u1212h_2*N", 'D', "IdoN"),
	LIDON("u2121h_2*N", 'L', "IdoN"),
	DIDOA("u1212a", 'D', "IdoA"),
	LIDOA("u2121a", 'L', "IdoA"),	
	DGAL("u2112h", 'D', "Gal"),
	LGAL("u1221h", 'L', "Gal"),
	DGALNAC("u2112h_2*NCC/3=O", 'D', "GalNAc"),
	LGALNAC("u1221h_2*NCC/3=O", 'L', "GalNAc"),
	DGALN("u2112h_2*N", 'D', "GalN"),
	LGALN("u1221h_2*N", 'L', "GalN"),
	DGALA("u2112a", 'D', "GalA"),
	LGALA("u1221a", 'L', "GalA"),
	DTAL("u1112h", 'D', "Tal"),
	LTAL("u2221h", 'L', "Tal"),
	DTAG("hU112h", 'D', "Tag"), //keto
	LTAG("hU221h", 'L', "Tag"), //keto
	DSOR("hU212h", 'D', "Sor"), //keto
	LSOR("hU121h", 'L', "Sor"), //keto
	DFRU("hU122h", 'D', "Fru"), //keto
	LFRU("hU211h", 'L', "Fru"), //keto 
	XFRU1("hxxxh", '?', "Fru" ), 
	DFUC("u2112m", 'D', "Fuc"),
	LFUC("u1221m", 'L', "Fuc"),
	DFUCNAC("u2112m_2*NCC/3=O", 'D', "FucNAc"),
	LFUCNAC("u1221m_2*NCC/3=O", 'L', "FucNAc"),
	DFUCN("u2112m_2*N", 'D', "FucN"),
	LFUCN("u1221m_2*N", 'L', "FucN"),
	DQUI("u2122m", 'D', "Qui"),
	LQUI("u1211m", 'L', "Qui"),
	DQUINAC("u2122m_2*NCC/3=O", 'D', "QuiNAc"),
	LQUINAC("u1211m_2*NCC/3=O", 'L', "QuiNAc"),
	DQUIN("u2122m_2*N", 'D', "QuiN"),
	LQUIN("u1211m_2*N", 'L', "QuiN"),
	DRHA("u1122m", 'D', "Rha"), 
	LRHA("u2211m", 'L', "Rha"), 
	dDTAL("u1112m", 'D', "dTal"),
	dLTAL("u2221m", 'L', "dTal"),
	/*DPAR("2d22m", 'D', "Par"),
	LPAR("1d11m", 'L', "Par"),
	LGLCOSONICACID("a2122", 'D', "glc-onic"),
	LGULOSONICACID("a2212", 'D', "gul-onic"),
	LGALOSONICACID("a1221", 'D', "gal-onic"),
	*/

	//hep
	DGRODMAN("u11222h", 'D', "Hep"), //211222h-1:5 → a-dgro-dman-HEP-1:5
	LGRODMAN("u11221h", 'L', "Hep"), //211221h-1:5 → a-lgro-dman-HEP-1:5
	DGRODALT("u12222h", 'D', "Hep"), //212222h-1:5 → a-dgro-dalt-HEP-1:5
	LGRODALT("u12221h", 'L', "Hep"), //212221h-1:5 → a-dgro-dalt-HEP-1:5
	DGRODGAL("u21122h", 'D', "Hep"), //121122h-1:5 → b-dgro-dgal-HEP-1:5
	LGRODGAL("u21121h", 'L', "Hep"), //121122h-1:5 → b-dgro-dgal-HEP-1:5
	DGROLGLC("u12112h", 'D', "Hep"), //212112h-1:5 → b-dgro-lglc-HEP-1:5
	LGROLGLC("u12111h", 'L', "Hep"), //212112h-1:5 → b-dgro-lglc-HEP-1:5
	DGRODGLC("u21222h", 'D', "Hep"),//221222h-1,5 → a-dgro-dglc-HEP-1:5
	LGRODGLC("u21221h", 'L', "Hep"),//221222h-1,5 → a-dgro-dglc-HEP-1:5
	XHEP("uxxxxxh", '?', "Hep"),
	
	//oct
	XOCT("uxxxxxxh", '?', "Oct"),
	DKO("aU11122h", 'D', "Ko"),
	LKO("aU22211h", 'L', "Ko"),
	DKDO("aUd1122h", 'D', "Kdo"), //keto
	LKDO("aUd2211h", 'L', "Kdo"), //keto
	DKDN("dU21122h", 'D', "Kdn"), //keto
	LKDN("dU12211h", 'L', "Kdn"), //keto
	//LGRODGLCOCT("h21221", 'L', "gro-D-glcOct"),
	
	//non
	XNON("uxxxxxxxh", '?', "Non"),
	DNEU("aUd21122h", 'D', "Neu"), //keto
	LNEU("aUd12211h", 'L', "Neu"), //keto
	DNEUAC("aUd21122h_5*NCC/3=O", 'D', "NeuAc"), //keto
	LNEUAC("aUd12211h_5*NCC/3=O", 'L', "NeuAc"), //keto
	DNEUGC("aUd21122h_5*NCCO/3=O", 'D', "NeuGc"), //keto
	LNEUGC("aUd12211h_5*NCCO/3=O", 'L', "NeuGc"), //keto
	DLEG("aUd11122m", 'D', "Leg"), //keto
	LLEG("aUd22211m", 'L', "Leg"), //keto
	NonA("aUd22111m", 'L', "Non"),
	LGROD3GALNON("aUd21121", 'L', "Non"),
	DGROD39GALNON("aUd21122m", 'D', "Non"),
	LGROD39GALNON("aUd21121m", 'L', "Non"),
	DGROL39MANNON("aUd22112m", 'D', "Non"), 
	LGROL39MANNON("aUd22111m", 'L', "Non"),
	DGROL39ALTNON("aUd21112m", 'D', "Non"),
	LGROL39ALTNON("aUd21111m", 'L', "Non"),
	DGROL39GALNON("aUd12212m", 'D', "Non"),
	LGROL39GLCNON("aUd12111m", 'D', "Non"),
	LGROD39TALNON("aUd11121m", 'L', "Non"),
	N1("aUd11221m", '?', "Non"),
	N2("aUd11221h", '?', "Non"),
	
	//Dec
	//DERYD3GALDEC("ad211222", "?D-Ery-D-3-deoxy-Gal-DEC-onic", 'D', "ery-D-3-deoxy-galDec-onic"),
	//LERYD3GALDEC("ad211211", "?L-Ery-D-3-deoxy-Gal-DEC-onic", 'L', "ery-D-3-deoxy-galDec-onic"),

	//WURCS 2.0 jorker monosaccharide
	XGRO("u4h", '?', "Gro"),
	XTHR("u34h", '?', "Thr"),
	XERY("u44h", '?', "Ery"),
	XARA("u344h", '?', "Ara"),
	XRIB("u444h", '?', "Rib"),
	XLYX("u334h", '?', "Lyx"),
	XXYL("u434h", '?', "Xyl"),
	XALL("u4444h", '?', "All"),
	XALT("u3444h", '?', "Alt"),
	XMAN("u3344h", '?', "Man"),
	XMANNAC("u3344h_2*NCC/3=O", '?', "ManNAc"),
	XMANN("u3344h_2*N", '?', "ManN"),
	XRHA("u3344m", '?', "Rha"),
	XGLC("u4344h", '?', "Glc"),
	XGLCNAC("u4344h_2*NCC/3=O", '?', "GlcNAc"),
	XGLCN("u4344h_2*N", '?', "GlcN"),
	XGUL("u4434h", '?', "Gul"),
	XIDO("u3434h", '?', "Ido"),
	XTAL("u3334h", '?', "Tal"),
	XGAL("u4334h", '?', "Gal"),
	XFRU("hU344h", '?', "Fru"),
	XFUC("u4334m", '?', "Fuc"),
	XQUI("u4344m", '?', "Qui"),
	XdTAL("u3334m", '?', "dTal"),
	XGROXMAN("u3344xh", '?', "HEP"),
	
	//Undefined monosaccahride
	FAZZY("<0>", '?', "SUG");	 

	private String basetype;
	private char chilarity; 
	private String SugarName;

	public void setSugarName(String in) {
		this.SugarName = in;
	}

	public String getBaseType() {
		return this.basetype;
	}

	public char getChilarity() {
		return this.chilarity;
	}

	public String getSugarName() {
		return this.SugarName;
	}

	//monosaccharide construct
	private WURCSstereoTemplate(String _basetype, char _chilarity, String _sugarname) {
		this.basetype = _basetype;
		this.chilarity = _chilarity;
		this.SugarName = _sugarname;
	}

	/*
	 * @param : SkeletonCode
	 * @return : Glycan builder base sugar basetype
	 */
	public static WURCSstereoTemplate getBaseType(String str) {
		WURCSstereoTemplate[] enumArray = WURCSstereoTemplate.values();

		// 取得出来たenum型分ループします。
		for(WURCSstereoTemplate enumStr : enumArray) {
			// 引数とenum型の文字列部分を比較します。
			if (str.equals(enumStr.basetype.toString()))
				return enumStr;
		}
		return null;
	}

	@Override
	public String toString() {
		return basetype;
	}
}
