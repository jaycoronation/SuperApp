package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class MyJourneyResponseModel{
	private ArrayList<JourneyDetailsItem> journey_details;
	private int success;

	public void setJourney_details(ArrayList<JourneyDetailsItem> journey_details){
		this.journey_details = journey_details;
	}

	public ArrayList<JourneyDetailsItem> getJourney_details(){
		return journey_details;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public static class JourneyDetailsItem{
		private String DivAmt;
		private String TotalGain;
		private String CurrentVal;
		private String Purchase;
		private String XIIR;
		private String Sell;
		private String DivAmtReinvest;
		private String Switchout;
		private String SwitchIn;
		private String TotalPurchase;
		private String NetInvestment;
		private String Dividend;

		public void setDivAmt(String divAmt){
			this.DivAmt = divAmt;
		}

		public String getDivAmt(){
			return DivAmt;
		}

		public void setTotalGain(String totalGain){
			this.TotalGain = totalGain;
		}

		public String getTotalGain(){
			return TotalGain;
		}

		public void setCurrentVal(String currentVal){
			this.CurrentVal = currentVal;
		}

		public String getCurrentVal(){
			return CurrentVal;
		}

		public void setPurchase(String purchase){
			this.Purchase = purchase;
		}

		public String getPurchase(){
			return Purchase;
		}

		public void setXIIR(String xIIR){
			this.XIIR = xIIR;
		}

		public String getXIIR(){
			return XIIR;
		}

		public void setSell(String sell){
			this.Sell = sell;
		}

		public String getSell(){
			return Sell;
		}

		public void setDivAmtReinvest(String divAmtReinvest){
			this.DivAmtReinvest = divAmtReinvest;
		}

		public String getDivAmtReinvest(){
			return DivAmtReinvest;
		}

		public void setSwitchout(String switchout){
			this.Switchout = switchout;
		}

		public String getSwitchout(){
			return Switchout;
		}

		public void setSwitchIn(String switchIn){
			this.SwitchIn = switchIn;
		}

		public String getSwitchIn(){
			return SwitchIn;
		}

		public void setTotalPurchase(String totalPurchase){
			this.TotalPurchase = totalPurchase;
		}

		public String getTotalPurchase(){
			return TotalPurchase;
		}

		public void setNetInvestment(String netInvestment){
			this.NetInvestment = netInvestment;
		}

		public String getNetInvestment(){
			return NetInvestment;
		}

		public void setDividend(String dividend){
			this.Dividend = dividend;
		}

		public String getDividend(){
			return Dividend;
		}
	}

}