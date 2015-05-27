package org.glycoinfo.WURCSFramework.wurcs.array;

public class LIP {

	private int  m_iBackbonePosition;
	private char m_cBackboneDirection;
	private int  m_iModificationPosition;
	/** Upper probability on Backbone side */
	private double m_dBackboneProbabilityUpper     = 1.0;
	/** Lower probability on Backbone side */
	private double m_dBackboneProbabilityLower     = 1.0;
	/** Upper probability on Modification side */
	private double m_dModificationProbabilityUpper = 1.0;
	/** Lower probability on Modification side */
	private double m_dModificationProbabilityLower = 1.0;


	public LIP(int a_iBackbonePosition, char a_iBackboneDirection, int a_iModificationPosition) {
		this.m_iBackbonePosition = a_iBackbonePosition;
		this.m_cBackboneDirection = a_iBackboneDirection;
		this.m_iModificationPosition = a_iModificationPosition;
	}

	public void setBackboneProbabilityUpper(double a_dBProbUp) {
		this.m_dBackboneProbabilityUpper = a_dBProbUp;
	}

	public void setBackboneProbabilityLower(double a_dBProbLow) {
		this.m_dBackboneProbabilityLower = a_dBProbLow;
	}

	public void setModificationProbabilityUpper(double a_dMProbUp) {
		this.m_dModificationProbabilityUpper = a_dMProbUp;
	}

	public void setModificationProbabilityLower(double a_dMProbLow) {
		this.m_dModificationProbabilityLower = a_dMProbLow;
	}

	public int getBackbonePosition() {
		return this.m_iBackbonePosition;
	}

	public char getBackboneDirection() {
		return this.m_cBackboneDirection;
	}

	public int getModificationPosition() {
		return this.m_iModificationPosition;
	}

	public double getBackboneProbabilityUpper() {
		return this.m_dBackboneProbabilityUpper;
	}

	public double getBackboneProbabilityLower() {
		return this.m_dBackboneProbabilityLower;
	}

	public double getModificationProbabilityUpper() {
		return this.m_dModificationProbabilityUpper;
	}

	public double getModificationProbabilityLower() {
		return this.m_dModificationProbabilityLower;
	}
}
