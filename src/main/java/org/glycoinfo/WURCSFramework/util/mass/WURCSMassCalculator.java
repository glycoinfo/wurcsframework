package org.glycoinfo.WURCSFramework.util.mass;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.wurcs.LIN;
import org.glycoinfo.WURCSFramework.wurcs.MOD;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;


public class WURCSMassCalculator {

	public static double getMassWURCS(WURCSArray a_objWURCS) {

		// For unique RES mass
		HashMap<UniqueRES, Double> t_hashUniqREStoMass = new HashMap<UniqueRES, Double>();
		LinkedList<UniqueRES> uRESList = a_objWURCS.getUniqueRESs();
		for ( UniqueRES ures : uRESList ) {
			double t_dMass = 0;
			String skCode = ures.getSkeletonCode();
			double skMass = getMassSkeletonCode(skCode);
			System.out.println(skCode+": "+skMass);
			t_dMass += skMass;
			for ( MOD mod : ures.getMODs() ) {
				t_dMass += getMassGlycosidic(mod.getMAPCode(), mod.getLIPs().size() );
			}
			t_hashUniqREStoMass.put(ures, t_dMass);
		}

		double t_dMass = 0;
		for ( RES res : a_objWURCS.getRESs() ) {
			// Get unique residue object
			UniqueRES ures = uRESList.get( res.getUniqueRESID() - 1 );
			t_dMass += t_hashUniqREStoMass.get(ures);
		}

		for ( LIN lin : a_objWURCS.getLINs() ) {
			t_dMass += getMassGlycosidic(lin.getMAPCode(), lin.getGLIPs().size()+lin.getFuzzyGLIPs().size() );
		}

		return t_dMass;
	}

	public static double getMassGlycosidic(String MAPCode, int nLink) {
		double OHMass = getMassMAP("*O");
		double HMass = getMassMAP("H");
		double hydrateMass = OHMass+HMass;

		System.out.println(nLink);
		double t_dMass = 0;
		// Dehydration for glycosidic linkage
		int nDehydration = 0;
		if ( nLink > 1 ) {
			nDehydration = nLink - 1;
			System.out.println("Glycosidic linkage");
			t_dMass -= hydrateMass * nDehydration;
		}

		// Continue if MAPCode is omitted
		if ( MAPCode.length() == 0 ) return t_dMass;

		// Substitution from hydroxyl group to modification
		t_dMass -= OHMass - HMass*nDehydration;

		double MAPMass = getMassMAP(MAPCode);
		System.out.println(MAPCode+": "+MAPMass);
		t_dMass += MAPMass;
		return t_dMass;
	}

	/**
	 * Calculate mass from skeleton code
	 * @param a_strSkeleton SkeletonCode
	 * @return basic mass value of skeleton code
	 */
	public static double getMassSkeletonCode(String a_strSkeleton) {
		double t_dMass = 0;
		int l = a_strSkeleton.length();
		for ( int i=0; i < l; i++ ) {
			char c = a_strSkeleton.charAt(i);

			t_dMass += CarbonDescriptorPropaties.forCharacter(c, ( i == 0 || i == l-1 ) ).getDefaultMass();
		}
		return t_dMass;
	}

	/**
	 * Calculate mass from MAP code
	 * @param a_strMAP MAP code
	 * @return	basic mass value of MAP code
	 */
	public static double getMassMAP(String a_strMAP) {
		double t_dMass = 0;

		int nTotalValence = 0;
		int nConnection = -1;
		for ( int i=0; i < a_strMAP.length(); i++ ) {
			char c = a_strMAP.charAt(i);

			// For skip code
			if ( c == '(' || c == ')' ) continue;

			// For '^'
			if ( c == '^' ) {
				i++;
				c = a_strMAP.charAt(i);
				if ( c == 'E' || c == 'Z' ) nConnection++;
//				if ( c == 'R' || c == 'S' || c == 'X' ) continue;
				continue;
			}
			// For '/'
			if ( c == '/' ) { i++; continue; }

			nConnection++;
			// For double '=' and triple '#' bond
			if ( c == '=' ) continue;
			if ( c == '#' ) { nConnection++; continue; }

			// For '*'
			if ( c == '*' ) { nTotalValence++; continue; }
			// For '$'
			if ( c == '$' ) { i++; continue; }

			// For atom symbol
			String symbol = ""+c;
			// For two character symbol
			if ( i+1 != a_strMAP.length() ) {
				char second = a_strMAP.charAt(i+1);
				if ( Character.isLowerCase(second) ) {
					i++;
					symbol += second;
				}
			}
			// Get atomic mass and valence
			AtomicPropaties prop = AtomicPropaties.forSymbol(symbol);
			if ( prop == null ) return 0;
			nTotalValence += prop.getValence();

			t_dMass += prop.getMass();
//			System.out.println(t_dMass);
		}
		// Count hidden hydrogens
		int nH = nTotalValence - 2*nConnection;
//		System.out.println(nTotalValence + "," + nConnection);
		t_dMass += nH * AtomicPropaties.H.getMass();

		return t_dMass;
	}


}
