package com.application.alphacapital.superapp.pms.beans;

public class PerformanceTemp {
	private String assetName;
	private String invested;
	private String current;
	private String gain;
	private String XIRR;

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getInvested() {
		return invested;
	}

	public void setInvested(String invested) {
		this.invested = invested;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getGain() {
		return gain;
	}

	public void setGain(String gain) {
		this.gain = gain;
	}

	public String getXIRR() {
		return XIRR;
	}

	public void setXIRR(String XIRR) {
		this.XIRR = XIRR;
	}
}
