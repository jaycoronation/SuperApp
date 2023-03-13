package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class CapitalGainResponseModel{
	private ArrayList<SaleValuesItem> sale_values;
	private int success;
	private GrandTotal grand_total;
	private String message;

	public void setSale_values(ArrayList<SaleValuesItem> sale_values){
		this.sale_values = sale_values;
	}

	public ArrayList<SaleValuesItem> getSale_values(){
		return sale_values;
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

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public static class GrandTotal{
		private double STCL_GrandTotal;
		private double STCG_GrandTotal;
		private double LTCL_GrandTotal;
		private double CapitalGain_GrandTotal;
		private double LTCG_GrandTotal;

		public void setSTCLGrandTotal(double sTCLGrandTotal){
			this.STCL_GrandTotal = sTCLGrandTotal;
		}

		public double getSTCLGrandTotal(){
			return STCL_GrandTotal;
		}

		public void setSTCGGrandTotal(double sTCGGrandTotal){
			this.STCG_GrandTotal = sTCGGrandTotal;
		}

		public double getSTCGGrandTotal(){
			return STCG_GrandTotal;
		}

		public void setLTCLGrandTotal(double lTCLGrandTotal){
			this.LTCL_GrandTotal = lTCLGrandTotal;
		}

		public double getLTCLGrandTotal(){
			return LTCL_GrandTotal;
		}

		public void setCapitalGain_GrandTotal(double capitalGain_GrandTotal){
			this.CapitalGain_GrandTotal = capitalGain_GrandTotal;
		}

		public double getCapitalGain_GrandTotal(){
			return CapitalGain_GrandTotal;
		}

		public void setLTCGGrandTotal(double lTCGGrandTotal){
			this.LTCG_GrandTotal = lTCGGrandTotal;
		}

		public double getLTCGGrandTotal(){
			return LTCG_GrandTotal;
		}
	}


	public static class SaleValuesItem{
		private SchemeTotal SchemeTotal;
		private String Applicant;
		private ArrayList<ValueItem> value;

		public void setSchemeTotal(SchemeTotal schemeTotal){
			this.SchemeTotal = schemeTotal;
		}

		public SchemeTotal getSchemeTotal(){
			return SchemeTotal;
		}

		public void setApplicant(String applicant){
			this.Applicant = applicant;
		}

		public String getApplicant(){
			return Applicant;
		}

		public void setValue(ArrayList<ValueItem> value){
			this.value = value;
		}

		public ArrayList<ValueItem> getValue(){
			return value;
		}
	}

	public static class ValueItem{
		private double STCL_total;
		private String category_key;
		private double STCG_total;
		private double LTCL_total;
		private double capital_gain;
		private double LTCG_total;

		public void setSTCLTotal(double sTCLTotal){
			this.STCL_total = sTCLTotal;
		}

		public double getSTCLTotal(){
			return STCL_total;
		}

		public void setCategory_key(String category_key){
			this.category_key = category_key;
		}

		public String getCategory_key(){
			return category_key;
		}

		public void setSTCGTotal(double sTCGTotal){
			this.STCG_total = sTCGTotal;
		}

		public double getSTCGTotal(){
			return STCG_total;
		}

		public void setLTCLTotal(double lTCLTotal){
			this.LTCL_total = lTCLTotal;
		}

		public double getLTCLTotal(){
			return LTCL_total;
		}

		public void setCapital_gain(double capital_gain){
			this.capital_gain = capital_gain;
		}

		public double getCapital_gain(){
			return capital_gain;
		}

		public void setLTCGTotal(double lTCGTotal){
			this.LTCG_total = lTCGTotal;
		}

		public double getLTCGTotal(){
			return LTCG_total;
		}
	}

}