package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Last30DaysDataResponse{

	@SerializedName("success")
	public int success;

	@SerializedName("sip_stp_details")
	public ArrayList<SipStpDetailsItem> sipStpDetails;

	@SerializedName("transaction_details")
	public ArrayList<TransactionDetailsItem> sipTransactionDetails;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setSipStpDetails(ArrayList<SipStpDetailsItem> sipStpDetails){
		this.sipStpDetails = sipStpDetails;
	}

	public ArrayList<SipStpDetailsItem> getSipStpDetails(){
		return sipStpDetails;
	}

	public ArrayList<TransactionDetailsItem> getSipTransactionDetails()
	{
		return sipTransactionDetails;
	}

	public void setSipTransactionDetails(ArrayList<TransactionDetailsItem> sipTransactionDetails)
	{
		this.sipTransactionDetails = sipTransactionDetails;
	}

	public static class SipStpDetailsItem{

		@SerializedName("Nature")
		public String nature;

		@SerializedName("Type")
		public String type;

		@SerializedName("TranDate")
		public String tranDate;

		@SerializedName("Amount")
		public String amount;

		@SerializedName("Product")
		public String product;

		@SerializedName("SchemeName")
		public String schemeName;

		@SerializedName("FolioNo")
		public String folioNo;

		@SerializedName("Applicant")
		public String applicant;


		public void setNature(String nature){
			this.nature = nature;
		}

		public String getNature(){
			return nature;
		}


		public void setType(String type){
			this.type = type;
		}

		public String getType(){
			return type;
		}



		public void setTranDate(String tranDate){
			this.tranDate = tranDate;
		}

		public String getTranDate(){
			return tranDate;
		}

		public void setAmount(String amount){
			this.amount = amount;
		}

		public String getAmount(){
			return amount;
		}


		public void setProduct(String product){
			this.product = product;
		}

		public String getProduct(){
			return product;
		}

		public void setSchemeName(String schemeName){
			this.schemeName = schemeName;
		}

		public String getSchemeName(){
			return schemeName;
		}

		public void setFolioNo(String folioNo){
			this.folioNo = folioNo;
		}

		public String getFolioNo(){
			return folioNo;
		}

		public void setApplicant(String applicant){
			this.applicant = applicant;
		}

		public String getApplicant(){
			return applicant;
		}

	}

	public static class TransactionDetailsItem{
		@SerializedName("TranDate")
		public String tranDate;

		@SerializedName("Applicant")
		public String applicant;

		@SerializedName("SchemeName")
		public String schemeName;

		@SerializedName("Amount")
		public String amount;

		@SerializedName("Type")
		public String type;

		@SerializedName("Nature")
		public String nature;

		@SerializedName("FolioNo")
		public String folioNo;

		@SerializedName("Nav")
		public String nav;

		@SerializedName("Units")
		public String units;

		@SerializedName("FCode")
		public String fCode;

		@SerializedName("SCode")
		public String sCode;

		@SerializedName("Product")
		public String product;

		public String getTranDate()
		{
			return tranDate;
		}

		public void setTranDate(String tranDate)
		{
			this.tranDate = tranDate;
		}

		public String getApplicant()
		{
			return applicant;
		}

		public void setApplicant(String applicant)
		{
			this.applicant = applicant;
		}

		public String getSchemeName()
		{
			return schemeName;
		}

		public void setSchemeName(String schemeName)
		{
			this.schemeName = schemeName;
		}

		public String getAmount()
		{
			return amount;
		}

		public void setAmount(String amount)
		{
			this.amount = amount;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getNature()
		{
			return nature;
		}

		public void setNature(String nature)
		{
			this.nature = nature;
		}

		public String getFolioNo()
		{
			return folioNo;
		}

		public void setFolioNo(String folioNo)
		{
			this.folioNo = folioNo;
		}

		public String getNav()
		{
			return nav;
		}

		public void setNav(String nav)
		{
			this.nav = nav;
		}

		public String getUnits()
		{
			return units;
		}

		public void setUnits(String units)
		{
			this.units = units;
		}

		public String getfCode()
		{
			return fCode;
		}

		public void setfCode(String fCode)
		{
			this.fCode = fCode;
		}

		public String getsCode()
		{
			return sCode;
		}

		public void setsCode(String sCode)
		{
			this.sCode = sCode;
		}

		public String getProduct()
		{
			return product;
		}

		public void setProduct(String product)
		{
			this.product = product;
		}
	}

}