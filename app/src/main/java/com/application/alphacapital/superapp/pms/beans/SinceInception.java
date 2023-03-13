package com.application.alphacapital.superapp.pms.beans;

import java.util.List;

public class SinceInception{
	private GrandTotal grandTotal;
	private List<SinceInceptionDetailsItem> sinceInceptionDetails;

	public void setGrandTotal(GrandTotal grandTotal){
		this.grandTotal = grandTotal;
	}

	public GrandTotal getGrandTotal(){
		return grandTotal;
	}

	public void setSinceInceptionDetails(List<SinceInceptionDetailsItem> sinceInceptionDetails){
		this.sinceInceptionDetails = sinceInceptionDetails;
	}

	public List<SinceInceptionDetailsItem> getSinceInceptionDetails(){
		return sinceInceptionDetails;
	}
}