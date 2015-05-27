package org.glycoinfo.WURCSFramework.util.array.mass;

import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIP;
import org.glycoinfo.WURCSFramework.wurcs.array.GLIPs;
import org.glycoinfo.WURCSFramework.wurcs.array.LIN;
import org.glycoinfo.WURCSFramework.wurcs.array.MOD;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.UniqueRES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;

/**
 * Class for calculation of mass from WURCS
 * @author MasaakiMatsubara
 *
 */
public class WURCSMassCalculator {

	/**
	 * Calculate mass for WURCSArray
	 * @param a_objWURCS WURCSArray
	 * @return Double of mass value
	 */
	public static double calcMassWURCS(WURCSArray a_objWURCS) throws WURCSMassException {

		WURCSExporter t_oExporter = new WURCSExporter();
		// For unique RES mass
		HashMap<UniqueRES, Double> t_hashUniqREStoMass = new HashMap<UniqueRES, Double>();
		LinkedList<UniqueRES> uRESList = a_objWURCS.getUniqueRESs();
		for ( UniqueRES ures : uRESList ) {
			double t_dMass = 0;
			String skCode = ures.getSkeletonCode();

			// For unknown
			if ( skCode.contains("<0>") )
				throw new WURCSMassException("Cannot calculate unknown carbon length. : "+t_oExporter.getUniqueRESString(ures));

			double skMass = getMassSkeletonCode(skCode);
//			System.out.println(skCode+": "+skMass);
			t_dMass += skMass;
			for ( MOD mod : ures.getMODs() ) {
				double t_dMassMOD = calcMassLinkage(mod.getMAPCode(), mod.getListOfLIPs().size() );
//				System.out.println(t_oExporter.getMODString(mod)+": "+t_dMassMOD);
				t_dMass += t_dMassMOD;
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
			// For repeat
			if ( lin.isRepeatingUnit() )
				throw new WURCSMassException("Cannot calculate repeating unit. : "+t_oExporter.getLINString(lin));

			// Check probability
			for ( GLIPs glips : lin.getListOfGLIPs() ) {
				GLIP glip0 = glips.getGLIPs().getFirst();
				if ( glip0.getBackboneProbabilityLower() != 1.0 || glip0.getModificationProbabilityLower() != 1.0 )
					throw new WURCSMassException("Cannot calculate linkage with probability. : "+t_oExporter.getLINString(lin));
			}
			double t_dMassLIN = calcMassLinkage(lin.getMAPCode(), lin.getListOfGLIPs().size() );
//			System.out.println(t_oExporter.getLINString(lin)+": "+t_dMassLIN);
			t_dMass += t_dMassLIN;
		}

		return t_dMass;
	}

	/**
	 * Calculate mass for linkage
	 * @param a_strMAPCode MAP in linkage
	 * @param nLink Number of connection in linkage
	 * @return Value of mass
	 */
	public static double calcMassLinkage(String a_strMAPCode, int nLink) {
		double OHMass = getMassMAP("*O");
		double HMass = getMassMAP("*H");
		double hydrateMass = OHMass+HMass;

		double t_dMass = 0;
		// Dehydration for glycosidic linkage
		int nDehydration = 0;
		if ( nLink > 1 ) {
			nDehydration = nLink - 1;
//			System.out.println("Glycosidic linkage: " );
			t_dMass -= hydrateMass * nDehydration;
		}

		// Continue if MAPCode is omitted
		if ( a_strMAPCode.length() == 0 ) return t_dMass;

		// For double bond in acid
		if ( a_strMAPCode.charAt(1) == '=' )
			t_dMass += HMass * 2;

		// Substitution from hydroxyl group to modification
		t_dMass -= OHMass - HMass*nDehydration;

		double MAPMass = getMassMAP(a_strMAPCode);
//		System.out.println(a_strMAPCode+": "+MAPMass);
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
		if ( a_strMAP.equals("*S") ){
			t_dMass += AtomicPropaties.S.getMass();
			t_dMass += AtomicPropaties.H.getMass();
			return t_dMass;
		}

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
