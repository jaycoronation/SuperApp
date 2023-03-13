package com.application.alphacapital.superapp.pms.beans;

public class SinceInceptionDetailsItem{
	private long holdingValue;
	private int amountInvested;
	private String assetType;
	private int currentValue;
	private double xirr;
	private double abreturn;
	private double totalDays;
	private int gain;

	public void setHoldingValue(long holdingValue){
		this.holdingValue = holdingValue;
	}

	public long getHoldingValue(){
		return holdingValue;
	}

	public void setAmountInvested(int amountInvested){
		this.amountInvested = amountInvested;
	}

	public int getAmountInvested(){
		return amountInvested;
	}

	public void setAssetType(String assetType){
		this.assetType = assetType;
	}

	public String getAssetType(){
		return assetType;
	}

	public void setCurrentValue(int currentValue){
		this.currentValue = currentValue;
	}

	public int getCurrentValue(){
		return currentValue;
	}

	public void setXirr(double xirr){
		this.xirr = xirr;
	}

	public double getXirr(){
		return xirr;
	}

	public void setAbreturn(double abreturn){
		this.abreturn = abreturn;
	}

	public double getAbreturn(){
		return abreturn;
	}

	public void setTotalDays(double totalDays){
		this.totalDays = totalDays;
	}

	public double getTotalDays(){
		return totalDays;
	}

	public void setGain(int gain){
		this.gain = gain;
	}

	public int getGain(){
		return gain;
	}
}
