package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class SIPSTPResponseModel{
	private int success;
	private ArrayList<SipStpDetailsItem> sip_stp_details;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setSip_stp_details(ArrayList<SipStpDetailsItem> sip_stp_details){
		this.sip_stp_details = sip_stp_details;
	}

	public ArrayList<SipStpDetailsItem> getSip_stp_details(){
		return sip_stp_details;
	}


	public static class SipStpDetailsItem{
		private SipStpTotal sip_stp_total;
		private String sip_stp_key;
		private ArrayList<SipStpValueItem> sip_stp_value;

		public void setSip_stp_total(SipStpTotal sip_stp_total){
			this.sip_stp_total = sip_stp_total;
		}

		public SipStpTotal getSip_stp_total(){
			return sip_stp_total;
		}

		public void setSip_stp_key(String sip_stp_key){
			this.sip_stp_key = sip_stp_key;
		}

		public String getSip_stp_key(){
			return sip_stp_key;
		}

		public void setSip_stp_value(ArrayList<SipStpValueItem> sip_stp_value){
			this.sip_stp_value = sip_stp_value;
		}

		public ArrayList<SipStpValueItem> getSip_stp_value(){
			return sip_stp_value;
		}
	}

	public class SipStpTotal{
		private int Nav;
		private int HoldingValue;
		private int CurrentNav;
		private int CAGR;
		private int PurchasedNav;
		private int Amount;
		private int PresentAmount;
		private int absoluteReturn;
		private int GainLoss;
		private int Units;
		private int holdingDays;


		public void setNav(int nav){
			this.Nav = nav;
		}

		public int getNav(){
			return Nav;
		}

		public void setHoldingValue(int holdingValue){
			this.HoldingValue = holdingValue;
		}

		public int getHoldingValue(){
			return HoldingValue;
		}

		public void setCurrentNav(int currentNav){
			this.CurrentNav = currentNav;
		}

		public int getCurrentNav(){
			return CurrentNav;
		}

		public void setCAGR(int cAGR){
			this.CAGR = cAGR;
		}

		public int getCAGR(){
			return CAGR;
		}

		public void setPurchasedNav(int purchasedNav){
			this.PurchasedNav = purchasedNav;
		}

		public int getPurchasedNav(){
			return PurchasedNav;
		}

		public void setAmount(int amount){
			this.Amount = amount;
		}

		public int getAmount(){
			return Amount;
		}

		public void setPresentAmount(int presentAmount){
			this.PresentAmount = presentAmount;
		}

		public int getPresentAmount(){
			return PresentAmount;
		}

		public void setAbsoluteReturn(int absoluteReturn){
			this.absoluteReturn = absoluteReturn;
		}

		public int getAbsoluteReturn(){
			return absoluteReturn;
		}

		public void setGainLoss(int gainLoss){
			this.GainLoss = gainLoss;
		}

		public int getGainLoss(){
			return GainLoss;
		}

		public void setUnits(int units){
			this.Units = units;
		}

		public int getUnits(){
			return Units;
		}

		public void setHoldingDays(int holdingDays){
			this.holdingDays = holdingDays;
		}

		public int getHoldingDays(){
			return holdingDays;
		}
	}

	public static class SipStpValueItem{
		private int TranTimestamp;
		private String Nature;
		private String SecondHolder;
		private int holdingValue;
		private int CurrentNav;
		private String TranDate;
		private String PurchasedNav;
		private int Amount;
		private String FCode;
		private String Product;
		private int PresentAmount;
		private String FolioNo;
		private int GainLoss;
		private String Units;
		private String SCode;
		private String Type;
		private int CAGR;
		private String SchemeName;
		private String FirstHolder;
		private int absoluteReturn;
		private String Applicant;
		private int holdingDays;

		public void setTranTimestamp(int tranTimestamp){
			this.TranTimestamp = tranTimestamp;
		}

		public int getTranTimestamp(){
			return TranTimestamp;
		}

		public void setNature(String nature){
			this.Nature = nature;
		}

		public String getNature(){
			return Nature;
		}

		public void setSecondHolder(String secondHolder){
			this.SecondHolder = secondHolder;
		}

		public String getSecondHolder(){
			return SecondHolder;
		}

		public void setHoldingValue(int holdingValue){
			this.holdingValue = holdingValue;
		}

		public int getHoldingValue(){
			return holdingValue;
		}

		public void setCurrentNav(int currentNav){
			this.CurrentNav = currentNav;
		}

		public int getCurrentNav(){
			return CurrentNav;
		}

		public void setTranDate(String tranDate){
			this.TranDate = tranDate;
		}

		public String getTranDate(){
			return TranDate;
		}

		public void setPurchasedNav(String purchasedNav){
			this.PurchasedNav = purchasedNav;
		}

		public String getPurchasedNav(){
			return PurchasedNav;
		}

		public void setAmount(int amount){
			this.Amount = amount;
		}

		public int getAmount(){
			return Amount;
		}

		public void setFCode(String fCode){
			this.FCode = fCode;
		}

		public String getFCode(){
			return FCode;
		}

		public void setProduct(String product){
			this.Product = product;
		}

		public String getProduct(){
			return Product;
		}

		public void setPresentAmount(int presentAmount){
			this.PresentAmount = presentAmount;
		}

		public int getPresentAmount(){
			return PresentAmount;
		}

		public void setFolioNo(String folioNo){
			this.FolioNo = folioNo;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public void setGainLoss(int gainLoss){
			this.GainLoss = gainLoss;
		}

		public int getGainLoss(){
			return GainLoss;
		}

		public void setUnits(String units){
			this.Units = units;
		}

		public String getUnits(){
			return Units;
		}

		public void setSCode(String sCode){
			this.SCode = sCode;
		}

		public String getSCode(){
			return SCode;
		}

		public void setType(String type){
			this.Type = type;
		}

		public String getType(){
			return Type;
		}

		public void setCAGR(int cAGR){
			this.CAGR = cAGR;
		}

		public int getCAGR(){
			return CAGR;
		}

		public void setSchemeName(String schemeName){
			this.SchemeName = schemeName;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public void setFirstHolder(String firstHolder){
			this.FirstHolder = firstHolder;
		}

		public String getFirstHolder(){
			return FirstHolder;
		}

		public void setAbsoluteReturn(int absoluteReturn){
			this.absoluteReturn = absoluteReturn;
		}

		public int getAbsoluteReturn(){
			return absoluteReturn;
		}

		public void setApplicant(String applicant){
			this.Applicant = applicant;
		}

		public String getApplicant(){
			return Applicant;
		}

		public void setHoldingDays(int holdingDays){
			this.holdingDays = holdingDays;
		}

		public int getHoldingDays(){
			return holdingDays;
		}
	}

}