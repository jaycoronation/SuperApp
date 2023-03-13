package com.application.alphacapital.superapp.pms.beans;

public class PortfolioTotal{
	private long holdingValue;
	private int amountInvested;
	private int currentValue;
	private double xirr;
	private double abreturn;
	private int gain;
	private double totalDays;

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

	public void setTotalDays(double totalDays){
		this.totalDays = totalDays;
	}

	public double getTotalDays(){
		return totalDays;
	}
}
