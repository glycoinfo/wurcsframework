package org.glycoinfo.WURCSFramework.wurcs.graph;

/**
 * Class for modification at repeating linkage
 * @author MasaakiMatsubara
 *
 */
public class ModificationRepeat extends Modification implements InterfaceRepeat {

	private Repeat m_objRepeat;

	public ModificationRepeat(String MAPCode) {
		super(MAPCode);
		this.m_objRepeat = new Repeat();
	}

	@Override
	public void setMinRepeatCount(int a_nRepMin) {
		this.m_objRepeat.setMinRepeatCount(a_nRepMin);
	}

	@Override
	public void setMaxRepeatCount(int a_nRepMax) {
		this.m_objRepeat.setMaxRepeatCount(a_nRepMax);
	}

	@Override
	public int getMinRepeatCount() {
		return this.m_objRepeat.getMinRepeatCount();
	}

	@Override
	public int getMaxRepeatCount() {
		return this.m_objRepeat.getMaxRepeatCount();
	}

	public ModificationRepeat copy() {
		ModificationRepeat t_oCopy = new ModificationRepeat(this.getMAPCode());
		t_oCopy.m_objRepeat = this.m_objRepeat;
		return t_oCopy;
	}
}
