package org.glycoinfo.WURCSFramework.wurcs.map;

import org.glycoinfo.WURCSFramework.util.property.AtomicProperties;

/**
 * Class for MAPAtom which is basic atom in MAPGraph
 * @author MasaakiMatsubara
 *
 */
public class MAPAtom extends MAPAtomAbstract {

	private String m_strSymbol;
	private MAPStereo m_enumStereo;

	public MAPAtom( String a_strSymbol ) {
		this.m_strSymbol = a_strSymbol;
		this.m_enumStereo = null;
	}

	@Override
	public String getSymbol() {
		return this.m_strSymbol;
	}

	public void setStereo( MAPStereo a_enumStereo ) {
		this.m_enumStereo = a_enumStereo;
	}

	@Override
	public MAPStereo getStereo() {
		return this.m_enumStereo;
	}

	@Override
	public MAPAtomAbstract copy() {
		MAPAtom t_oCopy = new MAPAtom(this.m_strSymbol);
		if ( this.isAromatic() ) t_oCopy.setAromatic();
		t_oCopy.setStereo(this.m_enumStereo);
		return t_oCopy;
	}

	@Override
	public int getValence() {
		AtomicProperties t_enumAP = AtomicProperties.forSymbol(this.m_strSymbol);
		// Return -1 (unknown) if element symbol is unknown string
		if ( t_enumAP == null ) return -1;

		return t_enumAP.getValence();
	}

}
