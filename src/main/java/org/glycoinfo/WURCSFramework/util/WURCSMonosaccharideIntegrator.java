package org.glycoinfo.WURCSFramework.util;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;

/**
 *
 * @author issaku yamada
 * @author MasaakiMatsubara
 *
 */
public class WURCSMonosaccharideIntegrator {


	/**
	 * Convert UniqueRES to anobase
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of anobase
	 */
	public static UniqueRES convertAnobase(UniqueRES a_oURES){
		int    t_iURESID         = a_oURES.getUniqueRESID();
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();
		char   t_cAnomSymbol     = a_oURES.getAnomericSymbol();

		// For anomeric
		if ( t_iAnomPos != 0 )
			t_strSkeletonCode = WURCSMonosaccharideIntegrator.replaceAnomericCarbonDescriptor(t_strSkeletonCode, t_iAnomPos);

		UniqueRES t_oAnobaseURES = new UniqueRES(t_iURESID, t_strSkeletonCode, t_iAnomPos, t_cAnomSymbol);
		// Add trimed modifications
		for ( MOD t_oMOD : WURCSMonosaccharideIntegrator.extractCoreModifications(a_oURES) )
			t_oAnobaseURES.addMOD(t_oMOD);

		return t_oAnobaseURES;
	}

	/**
	 * Convert anobase to supersum
	 * @param a_oAnobase Supersum of anobase
	 * @return UniqueRES of anobase supersum
	 */
	public static UniqueRES convertSupersumAnobase(UniqueRES a_oAnobase) {
		int    t_iURESID         = a_oAnobase.getUniqueRESID();
		String t_strSkeletonCode = a_oAnobase.getSkeletonCode();
		int    t_iAnomPos        = a_oAnobase.getAnomericPosition();
		char   t_cAnomSymbol     = a_oAnobase.getAnomericSymbol();

		if ( t_iAnomPos == 0 ) {
			t_strSkeletonCode = WURCSMonosaccharideIntegrator.replaceAnomericCarbonDescriptor(t_strSkeletonCode, t_iAnomPos);
		} else if ( t_cAnomSymbol == 'x' ) {
			t_iAnomPos = 0;
		}

		UniqueRES t_oAnobaseURES = new UniqueRES(t_iURESID, t_strSkeletonCode, t_iAnomPos, 'x');
		for ( MOD t_oMOD : a_oAnobase.getMODs() )
			t_oAnobaseURES.addMOD(t_oMOD);

		return t_oAnobaseURES;
	}

	/**
	 * Convert UniqueRES to basetype
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of basetype
	 */
	public static UniqueRES convertBasetype(UniqueRES a_oURES) {
		int    t_iURESID         = a_oURES.getUniqueRESID();
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();

		if ( t_iAnomPos != -1 )
			t_strSkeletonCode = WURCSMonosaccharideIntegrator.replaceAnomericCarbonDescriptor(t_strSkeletonCode, t_iAnomPos);

		UniqueRES t_oBasetypeURES = new UniqueRES(t_iURESID, t_strSkeletonCode, 0, 'x');
		// Add trimed modifications
		for ( MOD t_oMOD : WURCSMonosaccharideIntegrator.extractCoreModifications(a_oURES) )
			t_oBasetypeURES.addMOD(t_oMOD);

		return t_oBasetypeURES;
	}

	/**
	 * Convert UniqueRES to reduced form
	 * @param a_oURES UniqueRES
	 * @return UniqueRES of reduced form
	 */
	public static UniqueRES convertReducedForm(UniqueRES a_oURES) {
		int    t_iURESID         = a_oURES.getUniqueRESID();
		String t_strSkeletonCode = a_oURES.getSkeletonCode();
		int    t_iAnomPos        = a_oURES.getAnomericPosition();

		t_strSkeletonCode = WURCSMonosaccharideIntegrator.replaceAnomericCarbonDescriptor(t_strSkeletonCode, t_iAnomPos);
		int pos = ( t_iAnomPos > 0 )? t_iAnomPos-1 :
				  ( t_strSkeletonCode.contains("u") )? t_strSkeletonCode.indexOf("u") :
				  ( t_strSkeletonCode.contains("U") )? t_strSkeletonCode.indexOf("U") : 0;
		StringBuilder t_sbReducedForm = new StringBuilder( t_strSkeletonCode );
		t_sbReducedForm.replace(pos, pos+1, ( pos == 0 )? "h" : "X");

		UniqueRES t_oReducedForm = new UniqueRES(t_iURESID, t_strSkeletonCode, 0, 'x');
		for ( MOD t_oMOD : a_oURES.getMODs() )
			t_oReducedForm.addMOD(t_oMOD);

		return t_oReducedForm;
	}


	/**
	 * Extract core modifications in UniqueRES
	 * @param a_oURES UniqueRES
	 * @return List of core modifications
	 */
	private static LinkedList<MOD> extractCoreModifications(UniqueRES a_oURES){

		LinkedList<MOD> t_aCoreModifs = new LinkedList<MOD>();
		for (MOD mod : a_oURES.getMODs()) {
			// Remove modification of a hydrogen on OH groups.
			// Remove ring modification
			if (mod.getMAPCode().startsWith("*") && !mod.getMAPCode().startsWith("*O") && !mod.getMAPCode().startsWith("*=O") )
				t_aCoreModifs.add(mod);
		}
		return t_aCoreModifs;
	}

	/**
	 * Replace anomeric CarbonDescriptor for basetype and anobase
	 * @param a_oURES UniqueRES contained target SkeletonCode
	 * @return String of replaced SkeletonCode
	 */
	private static String replaceAnomericCarbonDescriptor(String a_strSkeletonCode, int a_iAnomPos){

		// Return if anomeric info is already replaced
		if ( a_strSkeletonCode.contains("u") || a_strSkeletonCode.contains("U") )
			return a_strSkeletonCode;

		StringBuilder  t_sbBasetype = new StringBuilder( a_strSkeletonCode );

		// For unknown sugar
		if ( a_iAnomPos == -1 )
			a_iAnomPos = 0;

		// For ring
		if ( a_iAnomPos != 0 ) {
			t_sbBasetype.replace(a_iAnomPos-1, a_iAnomPos, ( a_iAnomPos == 1 )? "u" : "U");
			return t_sbBasetype.toString();
		}

		// For open chain

		// Replase "first" aldo or keto symbol "o"/"O" in open chain
		if ( a_strSkeletonCode.contains("o") || a_strSkeletonCode.contains("O") ) {
			int pos = a_strSkeletonCode.indexOf('o');
			// CarbonDescriptor for keto to be "U"
			t_sbBasetype.replace(pos, pos+1, ( pos == 0 )? "u" : "U");
			return t_sbBasetype.toString();
		}

		// Return if reduced (no anomeric position e.g. alditol)
		return a_strSkeletonCode;
	}
}
