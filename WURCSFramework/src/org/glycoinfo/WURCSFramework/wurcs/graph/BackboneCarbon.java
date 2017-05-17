package org.glycoinfo.WURCSFramework.wurcs.graph;

/**
 * Class for a carbon in backbone
 * @author MasaakiMatsubara
 *
 */
public class BackboneCarbon {
	/** Backbone which contain this*/
	private Backbone m_objBackbone;
	/** Descriptor for this carbon */
	private CarbonDescriptorInterface m_objCarbonDescriptor;
	/** Whether or not this is anomeric like */
	private boolean m_bIsAnomericLike = false;
	/** Whether or not the carbon has unkown length */
	private boolean m_bHasUnknownLength = false;

	/**
	 * Private constructor
	 * @param a_oBackbone Backbone which contain this
	 * @param a_enumCarbonDescriptor CarbonDescriptor
	 * @param a_bIsAnomeric Whether this is anomeric like or not
	 * @param a_bIsUnknown Whether the length is unknown or not
	 */
	public BackboneCarbon( Backbone a_oBackbone, CarbonDescriptor a_enumCarbonDescriptor, boolean a_bIsAnomeric, boolean a_bIsUnknown ) {
		this.m_objBackbone         = a_oBackbone;
		this.m_objCarbonDescriptor = a_enumCarbonDescriptor;
		this.m_bIsAnomericLike     = a_bIsAnomeric;
		this.m_bHasUnknownLength   = a_bIsUnknown;
	}

	/**
	 * Constructor for not unknown length
	 * @param a_oBackbone Backbone which contain this
	 * @param a_enumCarbonDescriptor CarbonDescriptor
	 * @param a_bIsAnomeric Whether or not this is anomeric like
	 * @param a_bIsUnknown Whether or not have unknown length
	 */
	public BackboneCarbon( Backbone a_oBackbone, CarbonDescriptor a_enumCarbonDescriptor, boolean a_bIsAnomeric ) {
		this(a_oBackbone, a_enumCarbonDescriptor, a_bIsAnomeric, false);
	}

	/**
	 * Costructor for new CarbonDescriptor
	 * @param a_oBackbone
	 * @param a_enumCarbonDescriptor
	 */
	public BackboneCarbon( Backbone a_oBackbone, CarbonDescriptor_TBD a_enumCarbonDescriptor) {
		this.m_objBackbone = a_oBackbone;
		this.m_objCarbonDescriptor = a_enumCarbonDescriptor;
	}


	public Backbone getBackbone() {
		return this.m_objBackbone;
	}

	public CarbonDescriptorInterface getDesctriptor() {
		return this.m_objCarbonDescriptor;
	}

	public boolean isAnomeric() {
		return this.m_bIsAnomericLike;
	}

	public boolean hasUnknownLength() {
		return this.m_bHasUnknownLength;
	}

	public boolean isChiral() {
		if ( this.m_objCarbonDescriptor.getHybridOrbital() == null ) return false;
		return ( this.m_objCarbonDescriptor.getHybridOrbital().equals("sp3")
				&& this.m_objCarbonDescriptor.getStereo() != null );
	}

	/**
	 * Clone
	 * @param backbone
	 * @return clone backbone carbon
	 */
	public BackboneCarbon copy(Backbone backbone) {
		if ( this.m_objCarbonDescriptor instanceof CarbonDescriptor_TBD )
			return new BackboneCarbon(backbone, (CarbonDescriptor_TBD)this.m_objCarbonDescriptor );
		return new BackboneCarbon(backbone, (CarbonDescriptor)this.m_objCarbonDescriptor, this.m_bIsAnomericLike, this.m_bHasUnknownLength);
	}

	/**
	 * Invert
	 * @param backbone
	 * @return inverted backbone carbon
	 */
	public BackboneCarbon invert(Backbone backbone) {
		char symbolInv = this.m_objCarbonDescriptor.getChar();
		symbolInv = ( symbolInv == '1' )? '2' :
					( symbolInv == '2' )? '1' :
					( symbolInv == '3' )? '4' :
					( symbolInv == '4' )? '3' :
					( symbolInv == '5' )? '6' :
					( symbolInv == '6' )? '5' :
					( symbolInv == '7' )? '8' :
					( symbolInv == '8' )? '7' :
					( symbolInv == 's' )? 'r' :
					( symbolInv == 'r' )? 's' :
					( symbolInv == 'S' )? 'R' :
					( symbolInv == 'R' )? 'S' : symbolInv;
		if ( this.m_objCarbonDescriptor instanceof CarbonDescriptor_TBD ) {
			CarbonDescriptor_TBD cdInv = CarbonDescriptor_TBD.forCharacter( symbolInv, this.m_objCarbonDescriptor.isTerminal() );
			return new BackboneCarbon(backbone, cdInv );
		}
		CarbonDescriptor cdInv = CarbonDescriptor.forCharacter( symbolInv, this.m_objCarbonDescriptor.isTerminal() );
		return new BackboneCarbon(backbone, cdInv, this.m_bIsAnomericLike, this.m_bHasUnknownLength);
	}
}
