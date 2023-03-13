package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class CapitalGainReportResponseModel{
	private SaleValues sale_values;
	private int success;
	private String message;

	public void setSale_values(SaleValues sale_values){
		this.sale_values = sale_values;
	}

	public SaleValues getSale_values(){
		return sale_values;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public static class SaleValues{
		private TotalAmount total_amount;
		private ArrayList<PortfolioListItem> portfolio_list;
		private ArrayList<String> objective;

		public void setTotal_amount(TotalAmount total_amount){
			this.total_amount = total_amount;
		}

		public TotalAmount getTotal_amount(){
			return total_amount;
		}

		public void setPortfolio_list(ArrayList<PortfolioListItem> portfolio_list){
			this.portfolio_list = portfolio_list;
		}

		public ArrayList<PortfolioListItem> getPortfolio_list(){
			return portfolio_list;
		}

		public void setObjective(ArrayList<String> objective){
			this.objective = objective;
		}

		public ArrayList<String> getObjective(){
			return objective;
		}
	}


	public class PortfolioListItem{
		private String category_key;
		private CategoryTotal category_total;
		private ArrayList<CategoryValItem> category_val;

		public void setCategory_key(String category_key){
			this.category_key = category_key;
		}

		public String getCategory_key(){
			return category_key;
		}

		public void setCategory_total(CategoryTotal category_total){
			this.category_total = category_total;
		}

		public CategoryTotal getCategory_total(){
			return category_total;
		}

		public void setCategory_val(ArrayList<CategoryValItem> category_val){
			this.category_val = category_val;
		}

		public ArrayList<CategoryValItem> getCategory_val(){
			return category_val;
		}


		public class CategoryValItem{
			private String category_name;
			private String SchemeName;
			private SchemeTotal SchemeTotal;
			private ArrayList<ValueItem> value;

			public void setCategory_name(String category_name){
				this.category_name = category_name;
			}

			public String getCategory_name(){
				return category_name;
			}

			public void setSchemeName(String schemeName){
				this.SchemeName = schemeName;
			}

			public String getSchemeName(){
				return SchemeName;
			}

			public void setSchemeTotal(SchemeTotal schemeTotal){
				this.SchemeTotal = schemeTotal;
			}

			public SchemeTotal getSchemeTotal(){
				return SchemeTotal;
			}

			public void setValue(ArrayList<ValueItem> value){
				this.value = value;
			}

			public ArrayList<ValueItem> getValue(){
				return value;
			}
		}
	}

	public static class ValueItem{
		private double CapitalGain;
		private double STCG;
		private double PurchaseAmount;
		private String PurchaseNav;
		private double AnualiseReturn;
		private String FolioNo;
		private String SaleDate;
		private double STCL;
		private String Units;
		private double SaleAmount;
		private double LTCG;
		private String SaleNav;
		private double AbsReturn;
		private double LTCL;
		private String PurchaseDate;
		private int Days;
		private String Applicant;
	/*{
		"FolioNo": "1015875385",
			"Applicant": "Alpha Capital",
			"PurchaseAmount": 1.5873,
			"PurchaseDate": "21/03/2017",
			"Units": "0.005",
			"SaleNav": "421.942",
			"PurchaseNav": "317.459",
			"SaleAmount": 2.1097,
			"SaleDate": "23/03/2021",
			"CapitalGain": 0.5224,
			"Days": 1463,
			"AbsReturn": 32.911336582047,
			"AnualiseReturn": 8.2109623051586,
			"LTCG": 0.5224,
			"LTCL": 0,
			"STCG": 0,
			"STCL": 0
	}*/


		public void setCapitalGain(double capitalGain){
			this.CapitalGain = capitalGain;
		}

		public double getCapitalGain(){
			return CapitalGain;
		}

		public void setSTCG(double sTCG){
			this.STCG = sTCG;
		}

		public double getSTCG(){
			return STCG;
		}

		public void setPurchaseAmount(double purchaseAmount){
			this.PurchaseAmount = purchaseAmount;
		}

		public double getPurchaseAmount(){
			return PurchaseAmount;
		}

		public void setPurchaseNav(String purchaseNav){
			this.PurchaseNav = purchaseNav;
		}

		public String getPurchaseNav(){
			return PurchaseNav;
		}

		public void setAnualiseReturn(double anualiseReturn){
			this.AnualiseReturn = anualiseReturn;
		}

		public double getAnualiseReturn(){
			return AnualiseReturn;
		}

		public void setFolioNo(String folioNo){
			this.FolioNo = folioNo;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public void setSaleDate(String saleDate){
			this.SaleDate = saleDate;
		}

		public String getSaleDate(){
			return SaleDate;
		}

		public void setSTCL(double sTCL){
			this.STCL = sTCL;
		}

		public double getSTCL(){
			return STCL;
		}

		public void setUnits(String units){
			this.Units = units;
		}

		public String getUnits(){
			return Units;
		}

		public void setSaleAmount(double saleAmount){
			this.SaleAmount = saleAmount;
		}

		public double getSaleAmount(){
			return SaleAmount;
		}

		public void setLTCG(double lTCG){
			this.LTCG = lTCG;
		}

		public double getLTCG(){
			return LTCG;
		}

		public void setSaleNav(String saleNav){
			this.SaleNav = saleNav;
		}

		public String getSaleNav(){
			return SaleNav;
		}

		public void setAbsReturn(double absReturn){
			this.AbsReturn = absReturn;
		}

		public double getAbsReturn(){
			return AbsReturn;
		}

		public void setLTCL(double lTCL){
			this.LTCL = lTCL;
		}

		public double getLTCL(){
			return LTCL;
		}

		public void setPurchaseDate(String purchaseDate){
			this.PurchaseDate = purchaseDate;
		}

		public String getPurchaseDate(){
			return PurchaseDate;
		}

		public void setDays(int days){
			this.Days = days;
		}

		public int getDays(){
			return Days;
		}

		public void setApplicant(String applicant){
			this.Applicant = applicant;
		}

		public String getApplicant(){
			return Applicant;
		}
	}

}
