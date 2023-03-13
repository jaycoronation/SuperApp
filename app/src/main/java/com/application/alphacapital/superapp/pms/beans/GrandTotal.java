package com.application.alphacapital.superapp.pms.beans;

public class GrandTotal{
	private int amountInvested;
	private int currentValue;
	private double xirr;
	private double abreturn;
	private int gain;
	private long holdingAmount;
	private double totalDays;

	public void setAmountInvested(int amountInvested){
		this.amountInvested = amountInvested;
	}

	public int getAmountInvested(){
		return amountInvested;
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

	public void setGain(int gain){
		this.gain = gain;
	}

	public int getGain(){
		return gain;
	}

	public void setHoldingAmount(long holdingAmount){
		this.holdingAmount = holdingAmount;
	}

	public long getHoldingAmount(){
		return holdingAmount;
	}

	public void setTotalDays(double totalDays){
		this.totalDays = totalDays;
	}

	public double getTotalDays(){
		return totalDays;
	}
}
