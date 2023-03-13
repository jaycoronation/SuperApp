package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class PortfoliaResponseModel{
	private ArrayList<PortfolioDetailsItem> portfolio_details;
	private int success;
	private GrandTotal grand_total;

	public void setPortfolio_details(ArrayList<PortfolioDetailsItem> portfolio_details){
		this.portfolio_details = portfolio_details;
	}

	public ArrayList<PortfolioDetailsItem> getPortfolio_details(){
		return portfolio_details;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setGrand_total(GrandTotal grand_total){
		this.grand_total = grand_total;
	}

	public GrandTotal getGrand_total(){
		return grand_total;
	}

	public class GrandTotal{
		private int InitialValue;
		private int CurrentValue;
		private int Gain;

		public void setInitialValue(int initialValue){
			this.InitialValue = initialValue;
		}

		public int getInitialValue(){
			return InitialValue;
		}

		public void setCurrentValue(int currentValue){
			this.CurrentValue = currentValue;
		}

		public int getCurrentValue(){
			return CurrentValue;
		}

		public void setGain(int gain){
			this.Gain = gain;
		}

		public int getGain(){
			return Gain;
		}
	}

	public static class PortfolioDetailsItem{
		private TotalValue total_value;
		private ArrayList<PortfolioValueItem> portfolio_value;
		private String portfolio_key;

		public void setTotal_value(TotalValue total_value){
			this.total_value = total_value;
		}

		public TotalValue getTotal_value(){
			return total_value;
		}

		public void setPortfolio_value(ArrayList<PortfolioValueItem> portfolio_value){
			this.portfolio_value = portfolio_value;
		}

		public ArrayList<PortfolioValueItem> getPortfolio_value(){
			return portfolio_value;
		}

		public void setPortfolio_key(String portfolio_key){
			this.portfolio_key = portfolio_key;
		}

		public String getPortfolio_key(){
			return portfolio_key;
		}
	}

	public static class PortfolioValueItem{
		private ArrayList<SubCatItem> sub_cat;
		private PortfolioTotal portfolio_total;
		private String sub_cat_key;

		public void setSub_cat(ArrayList<SubCatItem> sub_cat){
			this.sub_cat = sub_cat;
		}

		public ArrayList<SubCatItem> getSub_cat(){
			return sub_cat;
		}

		public void setPortfolio_total(PortfolioTotal portfolio_total){
			this.portfolio_total = portfolio_total;
		}

		public PortfolioTotal getPortfolio_total(){
			return portfolio_total;
		}

		public void setSub_cat_key(String sub_cat_key){
			this.sub_cat_key = sub_cat_key;
		}

		public String getSub_cat_key(){
			return sub_cat_key;
		}

		public static class PortfolioTotal{
			private int InitialValue;
			private int CurrentValue;
			private int Gain;

			public void setInitialValue(int initialValue){
				this.InitialValue = initialValue;
			}

			public int getInitialValue(){
				return InitialValue;
			}

			public void setCurrentValue(int currentValue){
				this.CurrentValue = currentValue;
			}

			public int getCurrentValue(){
				return CurrentValue;
			}

			public void setGain(int gain){
				this.Gain = gain;
			}

			public int getGain(){
				return Gain;
			}
		}

		public class SubCatItem{
			private String BankName;
			private String RemainingUnits;
			private String sub_category;
			private int Holdingdays;
			private int CurrentValue;
			private String ModeOfHolding;
			private String Joint1Name;
			private int Unit;
			private String SCode;
			private String Exlcode;
			private int CAGR;
			private int CurrentNav;
			private int AbsoluteReturn;
			private int PurchasedNav;
			private String FCode;
			private String Change;
			private String FolioNo;
			private String MaturityDate;
			private String Nominee;
			private String Objective;
			private String HoldingPercentage;
			private String UCC;
			private String Joint2Name;
			private String InitialValue;
			private String PurchaseDate;
			private String ServiceProvider;
			private String SchemeName;
			private int Gain;

			public void setBankName(String bankName){
				this.BankName = bankName;
			}

			public String getBankName(){
				return BankName;
			}

			public void setRemainingUnits(String remainingUnits){
				this.RemainingUnits = remainingUnits;
			}

			public String getRemainingUnits(){
				return RemainingUnits;
			}

			public void setSub_category(String sub_category){
				this.sub_category = sub_category;
			}

			public String getSub_category(){
				return sub_category;
			}

			public void setHoldingdays(int holdingdays){
				this.Holdingdays = holdingdays;
			}

			public int getHoldingdays(){
				return Holdingdays;
			}

			public void setCurrentValue(int currentValue){
				this.CurrentValue = currentValue;
			}

			public int getCurrentValue(){
				return CurrentValue;
			}

			public void setModeOfHolding(String modeOfHolding){
				this.ModeOfHolding = modeOfHolding;
			}

			public String getModeOfHolding(){
				return ModeOfHolding;
			}

			public void setJoint1Name(String joint1Name){
				this.Joint1Name = joint1Name;
			}

			public String getJoint1Name(){
				return Joint1Name;
			}

			public void setUnit(int unit){
				this.Unit = unit;
			}

			public int getUnit(){
				return Unit;
			}

			public void setSCode(String sCode){
				this.SCode = sCode;
			}

			public String getSCode(){
				return SCode;
			}

			public void setExlcode(String exlcode){
				this.Exlcode = exlcode;
			}

			public String getExlcode(){
				return Exlcode;
			}

			public void setCAGR(int cAGR){
				this.CAGR = cAGR;
			}

			public int getCAGR(){
				return CAGR;
			}

			public void setCurrentNav(int currentNav){
				this.CurrentNav = currentNav;
			}

			public int getCurrentNav(){
				return CurrentNav;
			}

			public void setAbsoluteReturn(int absoluteReturn){
				this.AbsoluteReturn = absoluteReturn;
			}

			public int getAbsoluteReturn(){
				return AbsoluteReturn;
			}

			public void setPurchasedNav(int purchasedNav){
				this.PurchasedNav = purchasedNav;
			}

			public int getPurchasedNav(){
				return PurchasedNav;
			}

			public void setFCode(String fCode){
				this.FCode = fCode;
			}

			public String getFCode(){
				return FCode;
			}

			public void setChange(String change){
				this.Change = change;
			}

			public String getChange(){
				return Change;
			}

			public void setFolioNo(String folioNo){
				this.FolioNo = folioNo;
			}

			public String getFolioNo(){
				return FolioNo;
			}

			public void setMaturityDate(String maturityDate){
				this.MaturityDate = maturityDate;
			}

			public String getMaturityDate(){
				return MaturityDate;
			}

			public void setNominee(String nominee){
				this.Nominee = nominee;
			}

			public String getNominee(){
				return Nominee;
			}

			public void setObjective(String objective){
				this.Objective = objective;
			}

			public String getObjective(){
				return Objective;
			}

			public void setHoldingPercentage(String holdingPercentage){
				this.HoldingPercentage = holdingPercentage;
			}

			public String getHoldingPercentage(){
				return HoldingPercentage;
			}

			public void setUCC(String uCC){
				this.UCC = uCC;
			}

			public String getUCC(){
				return UCC;
			}

			public void setJoint2Name(String joint2Name){
				this.Joint2Name = joint2Name;
			}

			public String getJoint2Name(){
				return Joint2Name;
			}

			public void setInitialValue(String initialValue){
				this.InitialValue = initialValue;
			}

			public String getInitialValue(){
				return InitialValue;
			}

			public void setPurchaseDate(String purchaseDate){
				this.PurchaseDate = purchaseDate;
			}

			public String getPurchaseDate(){
				return PurchaseDate;
			}

			public void setServiceProvider(String serviceProvider){
				this.ServiceProvider = serviceProvider;
			}

			public String getServiceProvider(){
				return ServiceProvider;
			}

			public void setSchemeName(String schemeName){
				this.SchemeName = schemeName;
			}

			public String getSchemeName(){
				return SchemeName;
			}

			public void setGain(int gain){
				this.Gain = gain;
			}

			public int getGain(){
				return Gain;
			}
		}


	}

}