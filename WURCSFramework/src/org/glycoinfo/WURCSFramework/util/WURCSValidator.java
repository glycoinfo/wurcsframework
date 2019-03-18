package org.glycoinfo.WURCSFramework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSExchangeException;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorCollectConnectingBackboneGroups;
import org.glycoinfo.WURCSFramework.util.graph.visitor.WURCSVisitorException;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.BackboneCarbon;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

/**
 * Class for validating WURCS. 
 * This class validate WURCS and find errors, warnings, errors.
 * @author Masaaki Matsubara
 * modified by muller
 */
public class WURCSValidator {

	private List<String> m_lErrors;
	private List<String> m_lWarnings;
	private int m_iTheNumberOfErrors;
	private int m_iTheNumberOfWarnings;
	private String m_sOutWURCS;

	public WURCSValidator() {
		this.m_lErrors = new ArrayList<>();
		this.m_lWarnings = new ArrayList<>();
		this.m_iTheNumberOfErrors=0;
		this.m_iTheNumberOfWarnings=0;
	}

	public List<String> getErrors() {
		return this.m_lErrors;
	}

	public List<String> getWarnings() {
		return this.m_lWarnings;
	}
	
	public int getTheNumberOfErrors() {
		return this.m_iTheNumberOfErrors;
	}

	public int getTheNumberOfWarnings() {
		return this.m_iTheNumberOfWarnings;
	}
	
	public String getStandardWURCS() {
		return this.m_sOutWURCS;
		}

	/**
	 * Starts validation.
	 * @param a_strWURCS String of WURCS to be validated
	 */
	public void start(String a_strWURCS) {
		try {
			WURCSFactory factory = new WURCSFactory(a_strWURCS);
//			WURCSArray array = factory.getArray();
			WURCSGraph graph = factory.getGraph();
//			for ( Backbone t_bb : graph.getBackbones() ) {
//				System.err.print("["+t_bb.getSkeletonCode()+"]");
//				}

			// Check # of backbone groups
			WURCSVisitorCollectConnectingBackboneGroups t_oGroup = new WURCSVisitorCollectConnectingBackboneGroups();
			t_oGroup.start(graph);
			if ( t_oGroup.getBackboneGroups().size() > 1 ) {
//				m_iTheNumberOfErrors++;
//				this.m_lErrors.add("All residues must be connected.");
				m_iTheNumberOfWarnings++;
				this.m_lWarnings.add("All residues must be connected.");
				}

			// Validate Backbones
			for ( Backbone t_bb : graph.getBackbones() ) {
//				System.err.println("skeleton: "+t_bb.getSkeletonCode()+" contain: "+t_bb.getSkeletonCode().contains("<Q>"));
				if(t_bb.getSkeletonCode().contains("<Q>")) continue;
				this.validateAnomer(t_bb);
				this.validateStereo(t_bb);
				this.validateBond(t_bb);
				this.validateAvailableCarbonDescriptor(t_bb);
				}

			// Validate Modifications
			for ( Modification t_mod : graph.getModifications() ) {
//				System.err.print("number of Edge: "+t_mod.getEdges().size()+" MAPCode: "+t_mod.getMAPCode()+" glycosidic: "+t_mod.isGlycosidic());
				for(WURCSEdge t_edge: t_mod.getEdges()) {
//					System.err.print(" Linkage: "+t_edge.getLinkages().size()+" backbone position: "+t_edge.getLinkages().getFirst().getBackbonePosition()+" modification position: "+t_edge.getLinkages().getFirst().getModificationPosition());
					}
//				System.err.println();
				// TODO: validate available substituents
				this.validateSubstituents(t_mod);
				}
			// if no error is found standardized WURCS is saved
			if(m_iTheNumberOfErrors==0) {
				// make standardized WURCS
				WURCSFactory factory2 = new WURCSFactory(graph);
				String wurcs2 = factory2.getWURCS();
// making standard wurcs second time
//				System.err.println("[info] output WURCS compares to inputted WURCS");
				if(wurcs2.compareTo(a_strWURCS)!=0) {
					System.err.println("[info] output WURCS is not equal to inputted WURCS");
					WURCSFactory factory3 = new WURCSFactory(wurcs2);
//					WURCSArray array3 = factory3.getArray();
					WURCSGraph graph3 = factory3.getGraph();
					WURCSFactory factory_last = new WURCSFactory(graph3);
					m_sOutWURCS = factory_last.getWURCS();
					if(m_sOutWURCS.compareTo(wurcs2)!=0){
						m_iTheNumberOfWarnings++;
						this.m_lWarnings.add("This WURCS is not standardized.");
						}
					else {
						System.err.println("[info] This WURCS become standard in second standardization.");
						}
					}
				else {
					System.err.println("[info] output WURCS is equal to inputted WURCS");
					m_sOutWURCS = wurcs2;
					}
				}
			else {
				m_sOutWURCS = null;
				}
			} catch (WURCSFormatException ex) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("WURCSFormatException: "+ex.getMessage());
			} catch (WURCSExchangeException exx) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("WURCSExchangeException: "+exx.getMessage());
			} catch (WURCSVisitorException exxx) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("WURCSVisitorException: "+exxx.getMessage());
			} catch (WURCSException e) {
//				System.err.println("exception: "+e.getMessage());
//				this.m_lErrors.add("WURCSException");
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("WURCSEXception: "+e.getMessage());
//				e.printStackTrace();
				}
			}

	/**
	 * Validates anomer state of backbone. If there is error or something wrong, the messages are stored.
	 * @param a_bb Backbone to be checked anomer state
	 */
	private void validateAnomer(Backbone a_bb) {
		// Get anomer information from backbone carbons
		boolean t_bUncertainAnomer = false;
		boolean t_bCarbonyl = false;
		boolean t_bRingForm = false;
		int t_iAnomPos = 0;
//
		int nu=0; 
		int nk=0; 
		int na=0;
//		System.err.println("anomeric position: "+a_bb.getAnomericPosition());
//		System.err.println("descriptor: unknown: "+nu+" aldo(keto)se: "+nk+ " hemiacetal: "+na+" total: "+(nu+nk+na)+"\n");
//		for(WURCSEdge t_edge: a_bb.getEdges()) {
//			System.err.print("linkage: "+t_edge.getLinkages().getFirst().getBackbonePosition()+" anomeric: "+t_edge.isAnomeric()+" ring: "+t_edge.getModification().isRing()+" glycosidic: "+t_edge.getModification().isGlycosidic()+" MAP: "+t_edge.getModification().getMAPCode()+"\n");
//			}
		for ( BackboneCarbon t_bc : a_bb.getBackboneCarbons() ) {
			char desc=t_bc.getDesctriptor().getChar();
			if ( t_bc.getDesctriptor().getChar() == 'u' || t_bc.getDesctriptor().getChar() == 'U' ) {
				t_bUncertainAnomer = true;
				nu++;
				}
			if ( t_bc.getDesctriptor().getChar() == 'o' || t_bc.getDesctriptor().getChar() == 'O' ) {
				t_bCarbonyl = true;
				nk++;
				}
			if ( t_bc.getDesctriptor().getChar() == 'a' ) {
				na++;
//				if ( t_bRingForm ) {
//					m_iTheNumberOfErrors++;
//					this.m_lErrors.add("Anomer carbon must be only one in a monosaccharide.");
//					continue;
//				}
				t_iAnomPos = a_bb.getBackboneCarbons().indexOf(t_bc)+1;
				t_bRingForm = true;
				}
//			System.err.println("carbon descriptor: "+desc+ "nu,nk,na:"+nu+","+nk+","+na);
			}
//		System.err.println("descriptor: unknown: "+nu+" aldo(keto)se: "+nk+ " hemiacetal: "+na+" total: "+(nu+nk+na)+"number of Edges: "+a_bb.getEdges().size()+"\n");
// count the number of alcohol
		int nalcohol=0;
		for(BackboneCarbon t_bcc : a_bb.getBackboneCarbons() ) {
			if(t_bcc.getDesctriptor().getChar() == 'c') nalcohol+=2;
			else if(t_bcc.getDesctriptor().getChar() == 'h') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'e') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'z') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'f') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'M') nalcohol+=3;
			else if(t_bcc.getDesctriptor().getChar() == 'N') nalcohol+=2;
			else if(t_bcc.getDesctriptor().getChar() == '1') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '2') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '3') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '4') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'x') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '5') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '6') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '7') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == '8') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'X') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'C') nalcohol+=2;
			else if(t_bcc.getDesctriptor().getChar() == 'N') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'E') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'Z') nalcohol++;
			else if(t_bcc.getDesctriptor().getChar() == 'F') nalcohol++;
			}
// validation of multi-alcohol group
		if(nalcohol<2) {
//				m_iTheNumberOfErrors++;
//				this.m_lErrors.add("carbohydrate must have alcohol (-X in WURCS) at least 2 ("+nalcohol+" in backbone ["+a_bb.getSkeletonCode()+"])");
				m_iTheNumberOfWarnings++;
				this.m_lWarnings.add("carbohydrate must have alcohol (-X in WURCS) at least 2 ("+nalcohol+" in backbone ["+a_bb.getSkeletonCode()+"])");
			}
//
// descriptor describing anomer is not over than 1
		if( (nu+nk+na)>1) {
//				m_iTheNumberOfErrors++;
//				this.m_lErrors.add("Too many anomeric descriptor in backbone ["+a_bb.getSkeletonCode()+"]");
				m_iTheNumberOfWarnings++;
				this.m_lWarnings.add("Too many anomeric descriptor in backbone ["+a_bb.getSkeletonCode()+"]");
			}
// this is not carbohydrate because no aldehyde or ketone 
		else if((nu+nk+na)==0) {
			m_iTheNumberOfWarnings++;
			this.m_lWarnings.add("this is not carbohydrate. no aldehyde or ketone in backbone ["+a_bb.getSkeletonCode()+"]");
			if(a_bb.getAnomericPosition()!=0||a_bb.getAnomericSymbol()!='o') {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("Mismatched anomeric description in backbone ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
			}
// if descriptor 'u'/'U'/'o'/'O' then no anomeric information must be included
		else if(na==0&&(nk==1||nu==1)) {
//			System.err.println("["+a_bb.getSkeletonCode()+"] non anomeric position"+"pos: "+a_bb.getAnomericPosition()+" symb: "+a_bb.getAnomericSymbol());
			if(a_bb.getAnomericPosition()!=0||a_bb.getAnomericSymbol()!='o') {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("Mismatched anomeric description in backbone ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
			int u_pos=0;
			for(BackboneCarbon bcarbon: a_bb.getBackboneCarbons()) {
				if(bcarbon.getDesctriptor().getChar()=='u') u_pos=a_bb.getBackboneCarbons().indexOf(bcarbon)+1;
				if(bcarbon.getDesctriptor().getChar()=='U') u_pos=a_bb.getBackboneCarbons().indexOf(bcarbon)+1;
				if(bcarbon.getDesctriptor().getChar()=='o') u_pos=a_bb.getBackboneCarbons().indexOf(bcarbon)+1;
				if(bcarbon.getDesctriptor().getChar()=='O') u_pos=a_bb.getBackboneCarbons().indexOf(bcarbon)+1;
				}
			boolean anomeric_ring=false;
//			System.err.print("u_pos: "+u_pos);
			for(WURCSEdge edge: a_bb.getEdges()) {
//				System.err.print(" glycosidic: "+edge.getModification().isGlycosidic()+" ring: "+edge.getModification().isRing());
				if(!edge.getModification().isGlycosidic()&&!edge.getModification().isAglycone()) for(WURCSEdge edge2:edge.getModification().getEdges()) {
					if(u_pos==edge2.getLinkages().getFirst().getBackbonePosition()) {
//						System.err.println(" position: "+u_pos+","+edge2.getLinkages().getFirst().getBackbonePosition());
						anomeric_ring=true;
						}
					}
				}
			if(anomeric_ring) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("invalid ring information with respect to constructing hemiacetal ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
			}
		else if(na==1) {
			for ( BackboneCarbon t_bc : a_bb.getBackboneCarbons() ) {
// must exist anomeric position and anomeric symbol
				char desc=t_bc.getDesctriptor().getChar();
				if(desc=='a') t_iAnomPos = a_bb.getBackboneCarbons().indexOf(t_bc)+1;
				}
			if ( a_bb.getAnomericPosition() != t_iAnomPos ) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("Anomeric positions between the anomer descriptor 'a' and anomer information are mismatched. ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
			char asymb=a_bb.getAnomericSymbol();
			if(asymb!='a'&&asymb!='b'&&asymb!='u'&&asymb!='d'&&asymb!='x') {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("Invalid anomeric symbol. allowable anomeric symbols are 'a', 'b', 'u','d', and 'x'. ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
// must exist ring constructing hemiacetal
			boolean anomeric_ring=false;
			for(WURCSEdge edge: a_bb.getEdges()) {
				if(!edge.getModification().isGlycosidic()&&!edge.getModification().isAglycone()) for(WURCSEdge edge2:edge.getModification().getEdges()) {
					if(a_bb.getAnomericPosition()==edge2.getLinkages().getFirst().getBackbonePosition()) {
						anomeric_ring=true;
						}
					}
				}
			if(!anomeric_ring) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("No ring with respect to constructing hemiacetal ["+a_bb.getSkeletonCode()+"-'"+a_bb.getAnomericPosition()+a_bb.getAnomericSymbol()+"']");
				}
			}
		}

	/**
	 * Validates stereo chemistry of backbone carbons.
	 * @param a_bb Backbone to be checked stereo chemistry
	 */
	private void validateStereo(Backbone a_bb) {
		// Analyze chiral stereo
		List<Integer> t_lH_LOSEPositions = new ArrayList<>();
		boolean t_bHasAbsolute = false;
		boolean t_bHasRelative = false;
		boolean t_bHasUnknown = false;
		int t_iLastStereo = -1;
		for ( BackboneCarbon t_bc : a_bb.getBackboneCarbons() ) {
			int t_iPos = a_bb.getBackboneCarbons().indexOf(t_bc)+1;
			// ignore terminal carbons
			if ( t_iPos == 1 || t_iPos == a_bb.getBackboneCarbons().size() )
				continue;
			char t_cd = t_bc.getDesctriptor().getChar();
			if ( t_cd == '1' || t_cd == '2' ) {
				t_bHasAbsolute = true;
			} else 
			if ( t_cd == '5' || t_cd == '6' ) {
				t_bHasAbsolute = true;
				t_lH_LOSEPositions.add(t_iPos);
			} else 
			if ( t_cd == '3' || t_cd == '4' ) {
				t_bHasRelative = true;
			} else
			if ( t_cd == '7' || t_cd == '8' ) {
				t_bHasRelative = true;
				t_lH_LOSEPositions.add(t_iPos);
			} else
			if ( t_cd == 'x' ) {
				t_bHasUnknown = true;
			} else
			if ( t_cd == 'X' ) {
				t_bHasUnknown = true;
				t_lH_LOSEPositions.add(t_iPos);
			} else
				continue;
			t_iLastStereo = t_iPos;
		}

		if ( !t_lH_LOSEPositions.isEmpty() ) {
			// Check substituent on the carbon which hydrogen lose
			for ( int t_pos : t_lH_LOSEPositions ) {
				boolean t_bHasSubstituent = false;
				for ( WURCSEdge t_edge : a_bb.getEdges() ) {
					if ( t_edge.getLinkages().size() != 1 )
						continue;
					LinkagePosition t_link = t_edge.getLinkages().getFirst();
					if ( t_pos != t_link.getBackbonePosition() )
						continue;
					t_bHasSubstituent = true;
					}
				if ( !t_bHasSubstituent ) {
					m_iTheNumberOfErrors++;
					this.m_lErrors.add("The chiral carbon without hydrogen must connect to at least one substituent.");
					}
				}
			}

		// Analyze stereo on double bond
		t_bHasAbsolute = false;
		t_bHasUnknown = false;
		boolean t_bHasNoStereo = false;
		char t_cdPrev = ' ';
		for ( BackboneCarbon t_bc : a_bb.getBackboneCarbons() ) {
			int t_iPos = a_bb.getBackboneCarbons().indexOf(t_bc)+1;
			char t_cd = t_bc.getDesctriptor().getChar();
			if ( t_cd == 'e' || t_cd == 'E' || t_cd == 'z' || t_cd == 'Z' ) {
				t_bHasAbsolute = true;
			} else
			if ( t_cd == 'f' || t_cd == 'F' ) {
				t_bHasUnknown = true;
			} else
			if ( t_cd == 'n' || t_cd == 'N' ) {
				t_bHasNoStereo = true;
			} else
				t_cd = ' ';
			// Compare to previous CarbonDescriptor
			if ( t_cdPrev != ' ' && t_cd != ' ' ) {
				t_cd = Character.toLowerCase(t_cd);
				if ( t_cd != t_cdPrev ) {
					m_iTheNumberOfErrors++;
					this.m_lErrors.add("Two carbons on a double bond must have the same stereochemistry.");
					}
				t_cd = ' ';
				}
			t_cdPrev = Character.toLowerCase(t_cd);
			}
		// TODO: To be handled 'n' and 'N'
		if ( t_bHasNoStereo ) {
//			m_iTheNumberOfErrors++;
//			this.m_lErrors.add("CarbonDescriptor \'n\' and \'N\' can not be handled for now.");
			m_iTheNumberOfWarnings++;
			this.m_lWarnings.add("CarbonDescriptor \'n\' and \'N\' could not support in GlycoCT.");
			}
		}

	private void validateBond(Backbone a_bb) {
		int ncarbon = a_bb.getBackboneCarbons().size();
//		System.err.print("backbone size: "+ncarbon);
		ArrayList<Integer> nbond = new ArrayList<Integer>();
		for (WURCSEdge t_edge: a_bb.getEdges()) {
			if(t_edge.getModification().isGlycosidic()&&t_edge.getLinkages().getFirst().getBackbonePosition()!=-1) nbond.add(t_edge.getLinkages().getFirst().getBackbonePosition());
			}
//		System.err.println(" list of bond: "+nbond);
		int icount=0;
		for (int i=1;i<=ncarbon;i++) {
			icount=0;
			for(int j:nbond) {
				if(i==j) icount++;
				}
//			if (icount>1){
//				m_iTheNumberOfErrors++;
//				if(i==1) this.m_lErrors.add(i+"st position of ["+a_bb.getSkeletonCode()+"] has too many bonds ("+icount+") for modification or glycosidic bond" );
//				if(i==2) this.m_lErrors.add(i+"nd position of ["+a_bb.getSkeletonCode()+"] has too many bonds ("+icount+") for modification or glycosidic bond" );
//				if(i==3) this.m_lErrors.add(i+"rd position of ["+a_bb.getSkeletonCode()+"] has too many bonds ("+icount+") for modification or glycosidic bond" );
//				if(i>3) this.m_lErrors.add(i+"th position of ["+a_bb.getSkeletonCode()+"] has too many bonds ("+icount+") for modification or glycosidic bond" );
//				}
			}
		}

	/**
	 * Validates that CarbonDescriptors in the Backbone are available or not for conversion.
	 * @param a_bb Backbone to be checked CarbonDescriptors
	 */
	private void validateAvailableCarbonDescriptor(Backbone a_bb) {
		for ( BackboneCarbon t_bc : a_bb.getBackboneCarbons() ) {
			int t_iPos = a_bb.getBackboneCarbons().indexOf(t_bc)+1;

			boolean t_bIsTerminal = false;
			if ( t_iPos == 1 || t_iPos == a_bb.getBackboneCarbons().size() )
				t_bIsTerminal = true;

			char t_cd = t_bc.getDesctriptor().getChar();
			if ( t_cd == 'c' || t_cd == 'C' || t_cd == 'M'
			  || t_cd == 't' || t_cd == 'T' || t_cd == 'K') {
//				m_iTheNumberOfErrors++;
//				this.m_lErrors.add("CarbonDescriptor "+t_cd+" can not be handled for now.");
				m_iTheNumberOfWarnings++;
				this.m_lWarnings.add("CarbonDescriptor "+t_cd+" could not support in GlycoCT.");
				}

			if ( !t_bIsTerminal )
				continue;

			// Stereo for terminal carbon
			if ( t_cd == '1' || t_cd == '2' || t_cd == '3' || t_cd == '4' || t_cd == 'x'
			  || t_cd == '5' || t_cd == '6' || t_cd == '7' || t_cd == '8' || t_cd == 'X') {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("Terminal carbon with stereo can not be handled for now.");
				}
			}
		}

	/**
	 * Returns true if the Backbone has an anomeric ring start with anomeric carbon.
	 * @param a_bb Backbone which may have a anomeric ring
	 * @return true if the Backbone has anomeric ring
	 */
//	private boolean hasRing(Backbone a_bb) {
//		Modification t_mod = this.getRing(a_bb);
//		return (t_mod != null);
//	}
//
//	/**
//	 * Gets an anomeric ring modification of the Backbone. Most prior one will be chosen if there are multiple ether rings start with anomeric carbon.
//	 * Prior one is close to 6-memberd ring.
//	 * @param a_bb Backbone which may have a anomeric ring
//	 * @return Modification of a ring (most prior one will be chosen if there are multiple ether rings start with anomeric carbon, null if no ring)
//	 */
//	private Modification getRing(Backbone a_bb) {
//		List<Modification> t_lRings = this.getRings(a_bb);
//		if ( t_lRings.isEmpty() )
//			return null;
//		Collections.sort(t_lRings, new Comparator<Modification>(){
//
//			@Override
//			public int compare(Modification mod1, Modification mod2) {
//				int t_iSize1 = getRingSize(mod1);
//				int t_iSize2 = getRingSize(mod2);
//				if ( t_iSize1 == t_iSize2 )
//					return 0;
//				// Non 0 comes first
//				if ( t_iSize1 != 0 || t_iSize2 == 0 ) return -1;
//				if ( t_iSize1 == 0 || t_iSize2 != 0 ) return 1;
//				// Non -1 comes first
//				if ( t_iSize1 != -1 || t_iSize2 == -1 ) return -1;
//				if ( t_iSize1 == -1 || t_iSize2 != -1 ) return 1;
//				// Prior closer size to six-membered
//				int t_iComp = Math.abs(6-t_iSize1) - Math.abs(6-t_iSize2);
//				if ( t_iComp != 0 )
//					return t_iComp;
//				// Prior smaller size
//				return t_iSize1 - t_iSize2;
//			}
//			
//		});
//		// Return first
//		return t_lRings.get(0);
//	}
//
//	/**
//	 * Gets all anomeric ring modifications of the Backbone.
//	 * @param a_bb Backbone which may have a ring modification.
//	 * @return List of anomeric ring modifications connected to the Backbone.
//	 */
//	private List<Modification> getRings(Backbone a_bb) {
//		List<Modification> t_lRings = new ArrayList<>();
//		for ( WURCSEdge t_edge : a_bb.getChildEdges() ) {
//			if ( !t_edge.getModification().isRing() )
//				continue;
//			t_lRings.add(t_edge.getModification());
//		}
//		return t_lRings;
//	}
//
//	/**
//	 * Gets ring size of the Modification.
//	 * @param a_mod Modification to be counted the ring size.
//	 * @return The ring size of the Modification
//	 */
//	private int getRingSize(Modification a_mod) {
//		if ( !a_mod.isRing() )
//			return 0;
//		WURCSEdge t_edgeStart = a_mod.getEdges().getFirst();
//		WURCSEdge t_edgeEnd   = a_mod.getEdges().getLast();
//		if ( t_edgeStart.getLinkages().size() != 1 || t_edgeEnd.getLinkages().size() != 1 )
//			return -1;
//		if ( t_edgeStart.getLinkages().getFirst().getBackbonePosition() == -1
//		  || t_edgeEnd.getLinkages().getFirst().getBackbonePosition() == -1 )
//			return -1;
//		int t_iStart = t_edgeStart.getLinkages().getFirst().getBackbonePosition();
//		int t_iEnd = t_edgeEnd.getLinkages().getFirst().getBackbonePosition();
//		return Math.abs( t_iEnd - t_iStart ) + 1;
//	}

	
	/**
	 * Validate the Modification is available for the other formats.
	 * @param a_mod Modification to be validated
	 */
	private void validateSubstituents(Modification a_mod) {
		// No error if the MAP can be omitted (empty "", "*O", "*", or "*=O")
//		if( a_mod.isGlycosidic()) {
//			System.err.println("glycosidic edge: "+a_mod.getEdges().size());
//			if(a_mod.getEdges().size()!=2) {
//				m_iTheNumberOfErrors++;
//				this.m_lErrors.add("Glycosidic bond must be only two residues. This glycosidic bond has connect "+a_mod.getEdges().size()+" residues.");
//				}
//			}
		if ( a_mod.canOmitMAP() ) return;

		String t_strMAP = a_mod.getMAPCode();
		int t_nStar = t_strMAP.length() - t_strMAP.replace("*", "").length();
//		System.err.println("nstar: "+t_nStar);
// validation of connection betwenn *1,*2,... and modificationposition
		if ( t_strMAP.startsWith("*1") ) {
//			System.err.println("map: "+t_strMAP+" t_nStar"+t_nStar);
			Pattern p=Pattern.compile("\\*[0-9]*");
			Matcher m=p.matcher(t_strMAP);
			ArrayList <Integer> star = new ArrayList<Integer>();
			int star_size=0;
			while (m.find()) {
//				System.err.println("map* "+m.group());
				if(m.group().compareTo("*")!=0) {
					star_size++;
					star.add(Integer.parseInt(m.group().substring(1)));
					}
				else {
					m_iTheNumberOfErrors++;
					this.m_lErrors.add("modification position in MAP string is invalid. "+t_strMAP);
					}
				}
//			for(int ii:star) System.err.print("star: "+ii+" ");
//			System.err.println(" edges: "+a_mod.getEdges().size());
			int imatch=0;
			for ( WURCSEdge t_edge : a_mod.getEdges() ) {
				for(LinkagePosition linkage: t_edge.getLinkages()) {
					for(int t_star:star) {
//						System.err.println("match star: "+t_star+" "+linkage.getModificationPosition());
						if(t_star!=linkage.getModificationPosition()) continue ;
						imatch++;
						}
					}
//				System.err.println("matched modification position"+imatch+" star: "+star.size());
//				if ( t_edge.getLinkages().getFirst().getModificationPosition() == 0 ) {
				}
			if ( imatch != star.size() ) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("The asymmetric crosslinking substituent in MAP, which must have linkages with substituent linkage position.");
				}
			}
		if ( t_nStar > 2 ) {
			m_iTheNumberOfErrors++;
			this.m_lErrors.add("Substituent with three or more backbone carbons can not be handled.");
			}
		if ( t_nStar == 0 ) {
			m_iTheNumberOfErrors++;
			this.m_lErrors.add("At least one backbone carbon must be in a substituent.");
			}
// these are in GlycanFormatComberter.
//		if ( t_nStar == 1 && SubstituentTemplate.forMAP(t_strMAP) == null )
//			this.m_lErrors.add("The substituent cannot be handled.");
		if ( t_nStar == 2 ) {
//			if ( CrossLinkedTemplate.forMAP(t_strMAP) == null )
//				this.m_lErrors.add("The crosslinking substituent cannot be handled.");
			if ( a_mod.getEdges().size() != 2 ) {
				m_iTheNumberOfErrors++;
				this.m_lErrors.add("The crosslinking substituent must have two linkages.");
				}
			}
		}
	}
