package org.glycoinfo.WURCSFramework.wurcs.graph;


public class ModificationRepeatAlternative extends ModificationAlternative implements InterfaceRepeat {

	private Repeat m_objRepeat;
	public ModificationRepeatAlternative(String MAPCode) {
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

}
