package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.application.alphacapital.superapp.pms.utils.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplicationFundSchemesResponseModel{
	//private FundSchemesTemp fund_schemes_temp;
	private int success;
	private FundSchemesItem fund_schemes_total;
	private ArrayList<FundSchemesItem> fund_schemes;
	private ArrayList<FundSchemeListItem> fund_scheme_list;
	private FundSchemeListItem fund_scheme_list_total;

	/*public void setFund_schemes_temp(FundSchemesTemp fund_schemes_temp){
		this.fund_schemes_temp = fund_schemes_temp;
	}

	public FundSchemesTemp getFund_schemes_temp(){
		return fund_schemes_temp;
	}*/

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setFund_schemes_total(FundSchemesItem fund_schemes_total){
		this.fund_schemes_total = fund_schemes_total;
	}

	public FundSchemesItem getFund_schemes_total(){
		return fund_schemes_total;
	}

	public void setFund_schemes(ArrayList<FundSchemesItem> fund_schemes){
		this.fund_schemes = fund_schemes;
	}

	public ArrayList<FundSchemesItem> getFund_schemes(){
		return fund_schemes;
	}

	public void setFund_scheme_list(ArrayList<FundSchemeListItem> fund_scheme_list){
		this.fund_scheme_list = fund_scheme_list;
	}

	public ArrayList<FundSchemeListItem> getFund_scheme_list(){
		return fund_scheme_list;
	}

	public void setFund_scheme_list_total(FundSchemeListItem fund_scheme_list_total){
		this.fund_scheme_list_total = fund_scheme_list_total;
	}

	public FundSchemeListItem getFund_scheme_list_total(){
		return fund_scheme_list_total;
	}

	public static class FundSchemesItem implements Parcelable, Serializable {
		private double allocation;
		private String FundName;
		private int CurrentValue;
		private String PresentAmountTotal;
		private int AllocationTotal;
		private boolean isTotal = false ;

		public FundSchemesItem(Parcel in) {
			allocation = in.readDouble();
			FundName = in.readString();
			CurrentValue = in.readInt();
			PresentAmountTotal = in.readString();
			AllocationTotal = in.readInt();
			isTotal = in.readByte() != 0;
		}

		public static final Creator<FundSchemesItem> CREATOR = new Creator<FundSchemesItem>() {
			@Override
			public FundSchemesItem createFromParcel(Parcel in) {
				return new FundSchemesItem(in);
			}

			@Override
			public FundSchemesItem[] newArray(int size) {
				return new FundSchemesItem[size];
			}
		};

		public FundSchemesItem()
		{

		}

		public void setPresentAmountTotal(String presentAmountTotal){
			this.PresentAmountTotal = presentAmountTotal;
		}

		public String getPresentAmountTotal(){
			return PresentAmountTotal;
		}

		public void setAllocationTotal(int allocationTotal){
			this.AllocationTotal = allocationTotal;
		}

		public int getAllocationTotal(){
			return AllocationTotal;
		}

		public void setAllocation(double allocation){
			this.allocation = allocation;
		}

		public double getAllocation(){
			return allocation;
		}

		public void setFundName(String fundName){
			this.FundName = fundName;
		}

		public String getFundName(){
			return FundName;
		}

		public void setCurrentValue(int currentValue){
			this.CurrentValue = currentValue;
		}

		public int getCurrentValue(){
			return CurrentValue;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(FundName);
			dest.writeDouble(CurrentValue);
			dest.writeByte((byte) (isTotal ? 1 : 0));
		}
		public boolean isTotal()
		{
			return isTotal;
		}

		public void setTotal(boolean total)
		{
			isTotal = total;
		}
	}

	public static class FundSchemesTotal{
		private String PresentAmountTotal;
		private int AllocationTotal;

		public void setPresentAmountTotal(String presentAmountTotal){
			this.PresentAmountTotal = presentAmountTotal;
		}

		public String getPresentAmountTotal(){
			return PresentAmountTotal;
		}

		public void setAllocationTotal(int allocationTotal){
			this.AllocationTotal = allocationTotal;
		}

		public int getAllocationTotal(){
			return AllocationTotal;
		}
	}

	public static class FundSchemeListItem implements Parcelable, Serializable{
		private double allocation = 0;
		private String FundName = "";
		private String CurrentValue = "";
		private String SchemeName = "";
		private String category = "";
		private int PresentAmountTotal = 0;
		private int AllocationTotal = 0;
		private boolean isTotal = false ;

		public FundSchemeListItem(Parcel in) {
			allocation = in.readDouble();
			FundName = in.readString();
			CurrentValue = in.readString();
			SchemeName = in.readString();
			category = in.readString();
			PresentAmountTotal = in.readInt();
			AllocationTotal = in.readInt();
			isTotal = in.readByte() != 0;
		}

		public static final Creator<FundSchemeListItem> CREATOR = new Creator<FundSchemeListItem>() {
			@Override
			public FundSchemeListItem createFromParcel(Parcel in) {
				return new FundSchemeListItem(in);
			}

			@Override
			public FundSchemeListItem[] newArray(int size) {
				return new FundSchemeListItem[size];
			}
		};

		public FundSchemeListItem()
		{

		}

		public void setPresentAmountTotal(int presentAmountTotal){
			this.PresentAmountTotal = presentAmountTotal;
		}

		public int getPresentAmountTotal(){
			return PresentAmountTotal;
		}

		public void setAllocationTotal(int allocationTotal){
			this.AllocationTotal = allocationTotal;
		}

		public int getAllocationTotal(){
			return AllocationTotal;
		}

		public void setAllocation(double allocation){
			this.allocation = allocation;
		}

		public double getAllocation(){
			return allocation;
		}

		public void setFundName(String fundName){
			this.FundName = fundName;
		}

		public String getFundName(){
			return FundName;
		}

		public void setCurrentValue(String currentValue){
			this.CurrentValue = AppUtils.getValidAPIStringResponse(currentValue);
		}

		public String getCurrentValue(){
			return CurrentValue;
		}

		public void setSchemeName(String schemeName){
			this.SchemeName = schemeName;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public void setCategory(String category){
			this.category = category;
		}

		public String getCategory(){
			return category;
		}

		public boolean isTotal()
		{
			return isTotal;
		}

		public void setTotal(boolean total)
		{
			isTotal = total;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(FundName);
			dest.writeString(SchemeName);
			dest.writeString(CurrentValue);
			dest.writeString(category);
			dest.writeDouble(allocation);
			dest.writeByte((byte) (isTotal ? 1 : 0));
		}
	}
	public static class FundSchemeListTotal{
		private int PresentAmountTotal;
		private int AllocationTotal;

		public void setPresentAmountTotal(int presentAmountTotal){
			this.PresentAmountTotal = presentAmountTotal;
		}

		public int getPresentAmountTotal(){
			return PresentAmountTotal;
		}

		public void setAllocationTotal(int allocationTotal){
			this.AllocationTotal = allocationTotal;
		}

		public int getAllocationTotal(){
			return AllocationTotal;
		}
	}


}