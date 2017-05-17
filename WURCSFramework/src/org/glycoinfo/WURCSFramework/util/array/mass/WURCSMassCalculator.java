package org.glycoinfo.WURCSFramework.util.array.mass;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;
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

	private static int m_iPrecisionMax = 0;
	private static int m_iPrecisionMin = 999;

	/**
	 * Calculate mass for WURCSArray
	 * @param a_objWURCS WURCSArray
	 * @return Double of mass value
	 */
	public static BigDecimal calcMassWURCS(WURCSArray a_objWURCS) throws WURCSMassException {
		// Ignore compositions
		if ( a_objWURCS.isComposition() )
			throw new WURCSMassException("Cannot calculate mass of compositions.");

		// Reset precision
		m_iPrecisionMax = 0;

		WURCSExporter t_oExporter = new WURCSExporter();
		// For unique RES mass
		HashMap<UniqueRES, Double> t_hashUniqREStoMass = new HashMap<UniqueRES, Double>();
		HashMap<UniqueRES, BigDecimal> t_hashUniqREStoMassBD = new HashMap<UniqueRES, BigDecimal>();
		LinkedList<UniqueRES> uRESList = a_objWURCS.getUniqueRESs();
		for ( UniqueRES ures : uRESList ) {
			double t_dMass = 0;
			BigDecimal t_bdMass = new BigDecimal("0");
			String skCode = ures.getSkeletonCode();

			// For unknown
			if ( skCode.contains("<0>") || skCode.contains("<Q>") )
				throw new WURCSMassException("Cannot calculate unknown carbon length. : "+t_oExporter.getUniqueRESString(ures));

			BigDecimal t_bdSCMass = getMassSkeletonCode(skCode);
//			double skMass = getMassSkeletonCode(skCode);
//			System.out.println(skCode+": "+skMass);
			t_bdMass = t_bdMass.add(t_bdSCMass);
			for ( MOD mod : ures.getMODs() ) {
				BigDecimal t_dMassMOD = calcMassLinkage(mod.getMAPCode(), mod.getListOfLIPs().size() );
//				System.out.println(t_oExporter.getMODString(mod)+": "+t_dMassMOD);
				t_bdMass = t_bdMass.add(t_dMassMOD);
			}
			t_hashUniqREStoMass.put(ures, t_dMass);
			t_hashUniqREStoMassBD.put(ures, t_bdMass);
		}

		BigDecimal t_bdMass = new BigDecimal("0");
		for ( RES res : a_objWURCS.getRESs() ) {
			// Get unique residue object
			UniqueRES ures = uRESList.get( res.getUniqueRESID() - 1 );
			t_bdMass = t_bdMass.add( t_hashUniqREStoMassBD.get(ures) );
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
			BigDecimal t_bdMassLIN = calcMassLinkage(lin.getMAPCode(), lin.getListOfGLIPs().size() );
			t_bdMass = t_bdMass.add(t_bdMassLIN);
		}

		System.out.println(t_bdMass);
		// round
		t_bdMass = t_bdMass.round( new MathContext( m_iPrecisionMax, RoundingMode.HALF_UP ) );
		return t_bdMass;
	}

	/**
	 * Calculate mass for linkage
	 * @param a_strMAPCode MAP in linkage
	 * @param nLink Number of connection in linkage
	 * @return Value of mass
	 */
	public static BigDecimal calcMassLinkage(String a_strMAPCode, int nLink) {
//		double OHMass = getMassMAP("*O");
		BigDecimal t_bdOHMass = getMassMAP("*O");
//		double HMass = getMassMAP("*H");
		BigDecimal t_bdHMass = getMassMAP("*H");
//		double hydrateMass = new BigDecimal(OHMass).add( new BigDecimal(HMass) ).doubleValue();
		BigDecimal t_bdHydrateMass = t_bdOHMass.add(t_bdHMass);

		BigDecimal t_bdMass = new BigDecimal("0");
		// Dehydration for glycosidic linkage
		int nDehydration = 0;
		if ( nLink > 1 ) {
			nDehydration = nLink - 1;
//			System.out.println("Glycosidic linkage: " );
			t_bdMass = t_bdMass.subtract( t_bdHydrateMass.multiply( new BigDecimal(nDehydration) ) );
		}

		// Continue if MAPCode is omitted
		if ( a_strMAPCode.length() == 0 )
			return t_bdMass;

		// For double bond in acid
		if ( a_strMAPCode.length() > 1 && a_strMAPCode.charAt(1) == '=' )
			t_bdMass = t_bdMass.add(t_bdHMass);

		// Substitution from hydroxyl group to modification
		t_bdMass = t_bdMass.subtract( t_bdOHMass.subtract( t_bdHMass.multiply( new BigDecimal(nDehydration) ) ) );

		BigDecimal t_bdMAPMass = getMassMAP(a_strMAPCode);
//		System.out.println(a_strMAPCode+": "+MAPMass);
		t_bdMass = t_bdMass.add( t_bdMAPMass );

		return t_bdMass;
	}

	/**
	 * Calculate mass from skeleton code
	 * @param a_strSkeleton SkeletonCode
	 * @return basic mass value of skeleton code
	 */
	public static BigDecimal getMassSkeletonCode(String a_strSkeleton) {
		BigDecimal t_bdMass = new BigDecimal("0");

		int l = a_strSkeleton.length();
		for ( int i=0; i < l; i++ ) {
			char c = a_strSkeleton.charAt(i);

			// Get mass
			CarbonDescriptorPropaties t_enumCDP = CarbonDescriptorPropaties.forCharacter(c, ( i == 0 || i == l-1 ) );
			t_bdMass = t_bdMass.add( t_enumCDP.getDefaultMass() );

			// Update significant digit
			updatePrecisionMax( t_enumCDP.getMaxSignificantDigit() );
		}
		return t_bdMass;
	}

	/**
	 * Calculate mass from MAP code
	 * @param a_strMAP MAP code
	 * @return	basic mass value of MAP code
	 */
	public static BigDecimal getMassMAP(String a_strMAP) {
		BigDecimal t_bdMass = new BigDecimal("0");
		if ( a_strMAP.equals("*S") ){
			t_bdMass = t_bdMass.add( AtomicProperties.S.getMass() );
			t_bdMass = t_bdMass.add( AtomicProperties.H.getMass() );

			updatePrecisionMax( AtomicProperties.S.getMassPrecision() );
			updatePrecisionMax( AtomicProperties.H.getMassPrecision() );
			return t_bdMass;
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
			if ( c == '=' ) {
				if ( a_strMAP.charAt(i+1) != '^' ) continue;
				i++;i++;
				continue;
			}
			if ( c == '#' ) { nConnection++; continue; }

			// For '*'
			if ( c == '*' ) {
				// Skip MAP position
				while (i+1 != a_strMAP.length() && Character.isDigit( a_strMAP.charAt(i+1) ))
					i++;

				nTotalValence++;
				if ( i+1 == a_strMAP.length() ) continue;

				// Fix balence number by bond order
				if ( a_strMAP.charAt(i+1) == '=' ) nTotalValence++;
				if ( a_strMAP.charAt(i+1) == '#' ) nTotalValence += 2;

				continue;
			}
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
			AtomicProperties prop = AtomicProperties.forSymbol(symbol);
			if ( prop == null ) return new BigDecimal("0");
			nTotalValence += prop.getValence();

			t_bdMass = t_bdMass.add( prop.getMass() );
//			System.out.println(t_bdMass);

			// Update significant digit max
			updatePrecisionMax( prop.getMassPrecision() );
		}
		// Count hidden hydrogens
		int nH = nTotalValence - 2*nConnection;
//		System.out.println(nTotalValence + "," + nConnection);
		t_bdMass = t_bdMass.add( AtomicProperties.H.getMass().multiply( new BigDecimal(nH) ) );

		// Update significant digit max
		if ( nH > 0 )
			updatePrecisionMax( AtomicProperties.H.getMassPrecision() );

		return t_bdMass;
	}

	private static void updatePrecisionMax(int a_iPrecision) {
		if ( m_iPrecisionMax < a_iPrecision )
			m_iPrecisionMax = a_iPrecision;
	}
}
