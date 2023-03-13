package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class FolioNumberResponseModel{
	private ArrayList<FolioNosDetailsItem> FolioNosDetails;
	private int success;

	public void setFolioNosDetails(ArrayList<FolioNosDetailsItem> folioNosDetails){
		this.FolioNosDetails = folioNosDetails;
	}

	public ArrayList<FolioNosDetailsItem> getFolioNosDetails(){
		return FolioNosDetails;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public static class FolioNosDetailsItem{
		private int SrNo;
		private String Schemename;
		private String FolioNo;

		public void setSrNo(int srNo){
			this.SrNo = srNo;
		}

		public int getSrNo(){
			return SrNo;
		}

		public void setSchemename(String schemename){
			this.Schemename = schemename;
		}

		public String getSchemename(){
			return Schemename;
		}

		public void setFolioNo(String folioNo){
			this.FolioNo = folioNo;
		}

		public String getFolioNo(){
			return FolioNo;
		}
	}

}