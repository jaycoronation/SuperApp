package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

public class AppicantiListResponseModel{
	private int success;
	private ArrayList<ApplicantsListItem> applicants_list =  new ArrayList<ApplicantsListItem>();

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setApplicants_list(ArrayList<ApplicantsListItem> applicants_list){
		this.applicants_list = applicants_list;
	}

	public ArrayList<ApplicantsListItem> getApplicants_list(){
		return applicants_list;
	}

	public static class ApplicantsListItem{
		private String InitialVal;
		private String DividendValue;
		private String CurrentVal;
		private String AbsoluteReturn;
		private String GroupLeader;
		private String CAGR;
		private String WeightedDays;
		private String ApplicantName;
		private String Gain;
		private String Cid;

		public void setInitialVal(String initialVal){
			this.InitialVal = initialVal;
		}

		public String getInitialVal(){
			return InitialVal;
		}

		public void setDividendValue(String dividendValue){
			this.DividendValue = dividendValue;
		}

		public String getDividendValue(){
			return DividendValue;
		}

		public void setCurrentVal(String currentVal){
			this.CurrentVal = currentVal;
		}

		public String getCurrentVal(){
			return CurrentVal;
		}

		public void setAbsoluteReturn(String absoluteReturn){
			this.AbsoluteReturn = absoluteReturn;
		}

		public String getAbsoluteReturn(){
			return AbsoluteReturn;
		}

		public void setGroupLeader(String groupLeader){
			this.GroupLeader = groupLeader;
		}

		public String getGroupLeader(){
			return GroupLeader;
		}

		public void setCAGR(String cAGR){
			this.CAGR = cAGR;
		}

		public String getCAGR(){
			return CAGR;
		}

		public void setWeightedDays(String weightedDays){
			this.WeightedDays = weightedDays;
		}

		public String getWeightedDays(){
			return WeightedDays;
		}

		public void setApplicantName(String applicantName){
			this.ApplicantName = applicantName;
		}

		public String getApplicantName(){
			return ApplicantName;
		}

		public void setGain(String gain){
			this.Gain = gain;
		}

		public String getGain(){
			return Gain;
		}

		public void setCid(String cid){
			this.Cid = cid;
		}

		public String getCid(){
			return Cid;
		}
	}

}