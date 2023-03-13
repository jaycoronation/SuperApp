package com.application.alphacapital.superapp.pms.beans;

public class JsonMember9{
	private double allocation;
	private String fundName;
	private int currentValue;

	public void setAllocation(double allocation){
		this.allocation = allocation;
	}

	public double getAllocation(){
		return allocation;
	}

	public void setFundName(String fundName){
		this.fundName = fundName;
	}

	public String getFundName(){
		return fundName;
	}

	public void setCurrentValue(int currentValue){
		this.currentValue = currentValue;
	}

	public int getCurrentValue(){
		return currentValue;
	}
}
