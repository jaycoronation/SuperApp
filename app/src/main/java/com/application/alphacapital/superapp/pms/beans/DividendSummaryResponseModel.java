package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class DividendSummaryResponseModel{
	private int dividend_grand_total;
	private int success;
	private ArrayList<DividendSummaryItem> dividend_summary;

	public void setDividend_grand_total(int dividend_grand_total){
		this.dividend_grand_total = dividend_grand_total;
	}

	public int getDividend_grand_total(){
		return dividend_grand_total;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setDividend_summary(ArrayList<DividendSummaryItem> dividend_summary){
		this.dividend_summary = dividend_summary;
	}

	public ArrayList<DividendSummaryItem> getDividend_summary(){
		return dividend_summary;
	}

	public class DividendSummaryItem{
		private ArrayList<DividendSummaryDetailItem> DividendSummaryDetail;
		private String GrandTotal;
		private String ApplicantName;

		public void setDividendSummaryDetail(ArrayList<DividendSummaryDetailItem> dividendSummaryDetail){
			this.DividendSummaryDetail = dividendSummaryDetail;
		}

		public ArrayList<DividendSummaryDetailItem> getDividendSummaryDetail(){
			return DividendSummaryDetail;
		}

		public void setGrandTotal(String grandTotal){
			this.GrandTotal = grandTotal;
		}

		public String getGrandTotal(){
			return GrandTotal;
		}

		public void setApplicantName(String applicantName){
			this.ApplicantName = applicantName;
		}

		public String getApplicantName(){
			return ApplicantName;
		}
	}
	public class DividendSummaryDetailItem{
		private String Total;
		private String DivReinvest;
		private String SchemeName;
		private String FolioNo;
		private String DivPayout;

		public void setTotal(String total){
			this.Total = total;
		}

		public String getTotal(){
			return Total;
		}

		public void setDivReinvest(String divReinvest){
			this.DivReinvest = divReinvest;
		}

		public String getDivReinvest(){
			return DivReinvest;
		}

		public void setSchemeName(String schemeName){
			this.SchemeName = schemeName;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public void setFolioNo(String folioNo){
			this.FolioNo = folioNo;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public void setDivPayout(String divPayout){
			this.DivPayout = divPayout;
		}

		public String getDivPayout(){
			return DivPayout;
		}
	}


}