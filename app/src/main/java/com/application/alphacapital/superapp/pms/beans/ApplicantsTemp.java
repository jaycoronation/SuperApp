package com.application.alphacapital.superapp.pms.beans;

public class ApplicantsTemp
{
	private String name;
	private String amount_invested;
	private String current_amount;
	private String gain;
	private String abs_return;
	private String cagr;
	private double percentage;

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAmount_invested()
	{
		return amount_invested;
	}

	public void setAmount_invested(String amount_invested)
	{
		this.amount_invested = amount_invested;
	}

	public String getCurrent_amount()
	{
		return current_amount;
	}

	public void setCurrent_amount(String current_amount)
	{
		this.current_amount = current_amount;
	}

	public String getGain()
	{
		return gain;
	}

	public void setGain(String gain)
	{
		this.gain = gain;
	}

	public String getAbs_return()
	{
		return abs_return;
	}

	public void setAbs_return(String abs_return)
	{
		this.abs_return = abs_return;
	}

	public String getCagr()
	{
		return cagr;
	}

	public void setCagr(String cagr)
	{
		this.cagr = cagr;
	}
}
