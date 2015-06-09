package org.glycoinfo.WURCSFramework.wurcs.graph;

public class BackboneCarbon_TBD {
	/** Backbone which contain this*/
	private Backbone_TBD m_objBackbone;
	/** Descriptor for this carbon */
	private CarbonDescriptor_TBD m_objCarbonDescriptor;


	public BackboneCarbon_TBD( Backbone_TBD a_oBackbone, CarbonDescriptor_TBD a_enumCarbonDescriptor) {
		this.m_objBackbone = a_oBackbone;
		this.m_objCarbonDescriptor = a_enumCarbonDescriptor;
	}

	public Backbone_TBD getBackbone() {
		return this.m_objBackbone;
	}

	public CarbonDescriptor_TBD getDesctriptor() {
		return this.m_objCarbonDescriptor;
	}

	public boolean isChiral() {
		return ( this.m_objCarbonDescriptor.getHybridOrbital().equals("sp3")
				&& this.m_objCarbonDescriptor.getStereo() != null );
	}

	/**
	 * Clone
	 * @param backbone
	 * @return clone backbone carbon
	 */
	public BackboneCarbon_TBD copy(Backbone_TBD backbone) {
		return new BackboneCarbon_TBD(backbone, this.m_objCarbonDescriptor);
	}

	/**
	 * Invert
	 * @param backbone
	 * @return inverted backbone carbon
	 */
	public BackboneCarbon_TBD invert(Backbone_TBD backbone) {
		char symbolInv = this.m_objCarbonDescriptor.getChar();
		symbolInv = ( symbolInv == '1' )? '2' :
					( symbolInv == '2' )? '1' :
					( symbolInv == '3' )? '4' :
					( symbolInv == '4' )? '3' :
					( symbolInv == '5' )? '6' :
					( symbolInv == '6' )? '5' :
					( symbolInv == '7' )? '8' :
					( symbolInv == '8' )? '7' : symbolInv;
		CarbonDescriptor_TBD cdInv = CarbonDescriptor_TBD.forCharacter( symbolInv, this.m_objCarbonDescriptor.isTerminal() );
		return new BackboneCarbon_TBD(backbone, cdInv);
	}

}
