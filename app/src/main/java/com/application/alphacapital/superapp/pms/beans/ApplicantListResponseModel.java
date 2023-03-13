package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.application.alphacapital.superapp.pms.utils.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplicantListResponseModel{
	private int success = 0;
	private ApplicantsList applicants_list =  new ApplicantsList();

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setApplicants_list(ApplicantsList applicants_list){
		this.applicants_list = applicants_list;
	}

	public ApplicantsList getApplicants_list(){
		return applicants_list;
	}

	public static class ApplicantsList implements Parcelable, Serializable {
		private Total total = new Total();
		private ArrayList<ApplicantsItem> applicants =  new ArrayList<>();

		public ApplicantsList(Parcel in) {
			total = in.readParcelable(Total.class.getClassLoader());
			applicants = in.createTypedArrayList(ApplicantsItem.CREATOR);
		}

		public static final Creator<ApplicantsList> CREATOR = new Creator<ApplicantsList>() {
			@Override
			public ApplicantsList createFromParcel(Parcel in) {
				return new ApplicantsList(in);
			}

			@Override
			public ApplicantsList[] newArray(int size) {
				return new ApplicantsList[size];
			}
		};

		public ApplicantsList()
		{

		}

		public void setTotal(Total total){
			this.total = total;
		}

		public Total getTotal(){
			return total;
		}

		public void setApplicants(ArrayList<ApplicantsItem> applicants){
			this.applicants = applicants;
		}

		public ArrayList<ApplicantsItem> getApplicants(){
			return applicants;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeParcelable(total,flags);
			dest.writeTypedList(applicants);
		}
	}

	public static class Total  implements Parcelable, Serializable{
		private int InitialVal = 0;
		private int DividendValue = 0;
		private int CurrentVal = 0;
		private String AbsoluteReturn = "";
		private String CAGR  = "";
		private int WeightedDays = 0;
		private int Gain = 0;

	/*"CurrentVal": 11264542,
      "Gain": 1945777,
      "DividendValue": 0,
      "AbsoluteReturn": "",
      "CAGR": "",
      "WeightedDays": 0*/

		protected Total(Parcel in) {
			InitialVal = in.readInt();
			DividendValue = in.readInt();
			CurrentVal = in.readInt();
			AbsoluteReturn = in.readString();
			CAGR = in.readString();
			WeightedDays = in.readInt();
			Gain = in.readInt();
		}

		public static final Creator<Total> CREATOR = new Creator<Total>() {
			@Override
			public Total createFromParcel(Parcel in) {
				return new Total(in);
			}

			@Override
			public Total[] newArray(int size) {
				return new Total[size];
			}
		};

		public Total()
		{

		}

		public void setInitialVal(int initialVal){
			this.InitialVal = initialVal;
		}

		public int getInitialVal(){
			return InitialVal;
		}

		public void setDividendValue(int dividendValue){
			this.DividendValue = dividendValue;
		}

		public int getDividendValue(){
			return DividendValue;
		}

		public void setCurrentVal(int currentVal){
			this.CurrentVal = currentVal;
		}

		public int getCurrentVal(){
			return CurrentVal;
		}

		public void setAbsoluteReturn(String absoluteReturn){
			this.AbsoluteReturn = AppUtils.getValidAPIStringResponse(absoluteReturn);
		}

		public String getAbsoluteReturn(){
			return AbsoluteReturn;
		}

		public void setCAGR(String CAGR){
			this.CAGR = AppUtils.getValidAPIStringResponse(CAGR);
		}

		public String getCAGR(){
			return CAGR;
		}

		public void setWeightedDays(int weightedDays){
			this.WeightedDays = weightedDays;
		}

		public int getWeightedDays(){
			return WeightedDays;
		}

		public void setGain(int gain){
			this.Gain = gain;
		}

		public int getGain(){
			return Gain;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(InitialVal);
			dest.writeInt(DividendValue);
			dest.writeInt(CurrentVal);
			dest.writeInt(WeightedDays);
			dest.writeInt(Gain);
			dest.writeString(AbsoluteReturn);
			dest.writeString(CAGR);
		}
	}

	public static class ApplicantsItem implements Parcelable, Serializable {
		private String DividendValue = "";
		private String InitialVal = "";
		private String CurrentVal = "";
		private String AbsoluteReturn = "";
		private String GroupLeader = "";
		private String CAGR = "";
		private String WeightedDays = "";
		private String ApplicantName = "";
		private String Gain = "";
		private String Cid = "";

		public Double getPercentage() {
			return percentage;
		}

		public void setPercentage(Double percentage) {
			this.percentage = percentage;
		}

		private Double percentage = 0.0;

	 /*"ApplicantName": "Alpha Capital",
			 "Cid": "01C00276",
			 "GroupLeader": "01C00276",
			 "DividendValue": "0",
			 "InitialVal": "9318766",
			 "CurrentVal": "11264542",
			 "Gain": "1945777",
			 "AbsoluteReturn": "20.88",
			 "WeightedDays": "0",
			 "CAGR": "15.34"*/

		protected ApplicantsItem(Parcel in) {
			DividendValue = in.readString();
			InitialVal = in.readString();
			CurrentVal = in.readString();
			AbsoluteReturn = in.readString();
			GroupLeader = in.readString();
			CAGR = in.readString();
			WeightedDays = in.readString();
			ApplicantName = in.readString();
			Gain = in.readString();
			Cid = in.readString();
		}

		public static final Creator<ApplicantsItem> CREATOR = new Creator<ApplicantsItem>() {
			@Override
			public ApplicantsItem createFromParcel(Parcel in) {
				return new ApplicantsItem(in);
			}

			@Override
			public ApplicantsItem[] newArray(int size) {
				return new ApplicantsItem[size];
			}
		};

		public void setDividendValue(String dividendValue){
			this.DividendValue = dividendValue;
		}

		public String getDividendValue(){
			return DividendValue;
		}

		public void setInitialVal(String initialVal){
			this.InitialVal = initialVal;
		}

		public String getInitialVal(){
			return InitialVal;
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

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(DividendValue);
			dest.writeString(InitialVal);
			dest.writeString(CurrentVal);
			dest.writeString(AbsoluteReturn);
			dest.writeString(GroupLeader);
			dest.writeString(CAGR);
			dest.writeString(WeightedDays);
			dest.writeString(ApplicantName);
			dest.writeString(Gain);
			dest.writeString(Cid);
		}
	}

}
