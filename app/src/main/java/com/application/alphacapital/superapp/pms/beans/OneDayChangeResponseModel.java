package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class OneDayChangeResponseModel{
	private int success;
	private ArrayList<OneDayChangeDetailItem> OneDayChangeDetail;
	private String Cid;

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setOneDayChangeDetail(ArrayList<OneDayChangeDetailItem> oneDayChangeDetail){
		this.OneDayChangeDetail = oneDayChangeDetail;
	}

	public ArrayList<OneDayChangeDetailItem> getOneDayChangeDetail(){
		return OneDayChangeDetail;
	}

	public void setCid(String cid){
		this.Cid = cid;
	}

	public String getCid(){
		return Cid;
	}

	public static class OneDayChangeDetailItem{
		private String NAV;
		private int SrNo;
		private String Schemename;
		private String FolioNo;
		private String Change;
		private String NAVDate;
		private String Code;

		public void setNAV(String nAV){
			this.NAV = nAV;
		}

		public String getNAV(){
			return NAV;
		}

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

		public void setChange(String change){
			this.Change = change;
		}

		public String getChange(){
			return Change;
		}

		public void setNAVDate(String nAVDate){
			this.NAVDate = nAVDate;
		}

		public String getNAVDate(){
			return NAVDate;
		}

		public void setCode(String code){
			this.Code = code;
		}

		public String getCode(){
			return Code;
		}
	}

}