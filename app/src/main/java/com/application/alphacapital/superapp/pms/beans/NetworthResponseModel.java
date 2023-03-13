package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.application.alphacapital.superapp.pms.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NetworthResponseModel{
	private MacroStrategic macro_strategic;
	private MicroTactical micro_tactical;
	private MacroTactical macro_tactical;
	private ApplicantsAssetsDetails applicants_assets_details;
	private AssetsData assets_data;
	private int success;
	private MicroStrategic micro_strategic;

	public void setMacro_strategic(MacroStrategic macro_strategic){
		this.macro_strategic = macro_strategic;
	}

	public MacroStrategic getMacro_strategic(){
		return macro_strategic;
	}

	public void setMicro_tactical(MicroTactical micro_tactical){
		this.micro_tactical = micro_tactical;
	}

	public MicroTactical getMicro_tactical(){
		return micro_tactical;
	}

	public void setMacro_tactical(MacroTactical macro_tactical){
		this.macro_tactical = macro_tactical;
	}

	public MacroTactical getMacro_tactical(){
		return macro_tactical;
	}

	public void setApplicants_assets_details(ApplicantsAssetsDetails applicants_assets_details){
		this.applicants_assets_details = applicants_assets_details;
	}

	public ApplicantsAssetsDetails getApplicants_assets_details(){
		return applicants_assets_details;
	}

	public void setAssets_data(AssetsData assets_data){
		this.assets_data = assets_data;
	}

	public AssetsData getAssets_data(){
		return assets_data;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setMicro_strategic(MicroStrategic micro_strategic){
		this.micro_strategic = micro_strategic;
	}

	public MicroStrategic getMicro_strategic(){
		return micro_strategic;
	}


	public static class MacroStrategic{
		private ArrayList<MacroAssetsStrategicItem> macro_assets_strategic;
		private MacroAssetsStrategicItem macro_assets_strategic_grandtotal;

		public void setMacro_assets_strategic(ArrayList<MacroAssetsStrategicItem> macro_assets_strategic){
			this.macro_assets_strategic = macro_assets_strategic;
		}

		public ArrayList<MacroAssetsStrategicItem> getMacro_assets_strategic(){
			return macro_assets_strategic;
		}

		public void setMacro_assets_strategic_grandtotal(MacroAssetsStrategicItem macro_assets_strategic_grandtotal){
			this.macro_assets_strategic_grandtotal = macro_assets_strategic_grandtotal;
		}

		public MacroAssetsStrategicItem getMacro_assets_strategic_grandtotal(){
			return macro_assets_strategic_grandtotal;
		}
	}

	public static class MacroAssetsStrategicGrandtotal{
		private int micro_assets_strategic_variance_total;
		private int grand_percentage_amount;
		private int grand_total_amount;
		private int micro_assets_strategic_policy;

		public void setMicro_assets_strategic_variance_total(int micro_assets_strategic_variance_total){
			this.micro_assets_strategic_variance_total = micro_assets_strategic_variance_total;
		}

		public int getMicro_assets_strategic_variance_total(){
			return micro_assets_strategic_variance_total;
		}

		public void setGrand_percentage_amount(int grand_percentage_amount){
			this.grand_percentage_amount = grand_percentage_amount;
		}

		public int getGrand_percentage_amount(){
			return grand_percentage_amount;
		}

		public void setGrand_total_amount(int grand_total_amount){
			this.grand_total_amount = grand_total_amount;
		}

		public int getGrand_total_amount(){
			return grand_total_amount;
		}

		public void setMicro_assets_strategic_policy(int micro_assets_strategic_policy){
			this.micro_assets_strategic_policy = micro_assets_strategic_policy;
		}

		public int getMicro_assets_strategic_policy(){
			return micro_assets_strategic_policy;
		}
	}

	public static class MacroAssetsStrategicItem implements Parcelable, Serializable{
		private double variance;
		private String asset_class;
		private double actual_amount;
		private double actual_percentage;
		private double policy;
		private int micro_assets_strategic_variance_total;
		private int grand_percentage_amount;
		private int grand_total_amount;
		private int micro_assets_strategic_policy;
		private String totalAmount = "" ;
		private String totalAmountPercentage = "" ;
		private String totalPolicyPercentage = "" ;
		private String totalVariance = "" ;
		private boolean isTotal = false ;

		protected MacroAssetsStrategicItem(Parcel in) {
			variance = in.readDouble();
			asset_class = in.readString();
			actual_amount = in.readDouble();
			actual_percentage = in.readDouble();
			policy = in.readDouble();
			micro_assets_strategic_variance_total = in.readInt();
			grand_percentage_amount = in.readInt();
			grand_total_amount = in.readInt();
			micro_assets_strategic_policy = in.readInt();
			totalAmount = in.readString();
			totalAmountPercentage = in.readString();
			totalPolicyPercentage = in.readString();
			totalVariance = in.readString();
			isTotal = in.readByte() != 0;

		}

		public static final Creator<MacroAssetsStrategicItem> CREATOR = new Creator<MacroAssetsStrategicItem>() {
			@Override
			public MacroAssetsStrategicItem createFromParcel(Parcel in) {
				return new MacroAssetsStrategicItem(in);
			}

			@Override
			public MacroAssetsStrategicItem[] newArray(int size) {
				return new MacroAssetsStrategicItem[size];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(asset_class);
			dest.writeDouble(actual_amount);
			dest.writeDouble(actual_percentage);
			dest.writeDouble(policy);
			dest.writeDouble(variance);
			dest.writeString(totalAmount);
			dest.writeString(totalAmountPercentage);
			dest.writeString(totalPolicyPercentage);
			dest.writeString(totalVariance);
			dest.writeByte((byte) (isTotal ? 1 : 0));
		}

		public String getTotalAmount()
		{
			return totalAmount;
		}

		public void setTotalAmount(String totalAmount)
		{
			this.totalAmount = totalAmount;
		}

		public String getTotalAmountPercentage()
		{
			return totalAmountPercentage;
		}

		public void setTotalAmountPercentage(String totalAmountPercentage)
		{
			this.totalAmountPercentage = totalAmountPercentage;
		}

		public String getTotalPolicyPercentage()
		{
			return totalPolicyPercentage;
		}

		public void setTotalPolicyPercentage(String policyPercentage)
		{
			this.totalPolicyPercentage = policyPercentage;
		}

		public String getTotalVariance()
		{
			return totalVariance;
		}

		public void setTotalVariance(String totalVariance)
		{
			this.totalVariance = totalVariance;
		}

		public boolean isTotal()
		{
			return isTotal;
		}

		public void setTotal(boolean total)
		{
			isTotal = total;
		}

		public void setMicro_assets_strategic_variance_total(int micro_assets_strategic_variance_total){
			this.micro_assets_strategic_variance_total = micro_assets_strategic_variance_total;
		}

		public int getMicro_assets_strategic_variance_total(){
			return micro_assets_strategic_variance_total;
		}

		public void setGrand_percentage_amount(int grand_percentage_amount){
			this.grand_percentage_amount = grand_percentage_amount;
		}

		public int getGrand_percentage_amount(){
			return grand_percentage_amount;
		}

		public void setGrand_total_amount(int grand_total_amount){
			this.grand_total_amount = grand_total_amount;
		}

		public int getGrand_total_amount(){
			return grand_total_amount;
		}

		public void setMicro_assets_strategic_policy(int micro_assets_strategic_policy){
			this.micro_assets_strategic_policy = micro_assets_strategic_policy;
		}

		public int getMicro_assets_strategic_policy(){
			return micro_assets_strategic_policy;
		}

		public void setVariance(double variance){
			this.variance = variance;
		}

		public double getVariance(){
			return variance;
		}

		public void setAsset_class(String asset_class){
			this.asset_class = asset_class;
		}

		public String getAsset_class(){
			return asset_class;
		}

		public void setActual_amount(double actual_amount){
			this.actual_amount = actual_amount;
		}

		public double getActual_amount(){
			return actual_amount;
		}

		public void setActual_percentage(double actual_percentage){
			this.actual_percentage = actual_percentage;
		}

		public double getActual_percentage(){
			return actual_percentage;
		}

		public void setPolicy(double policy){
			this.policy = policy;
		}

		public double getPolicy(){
			return policy;
		}


	}

	public static class MicroStrategic{
		private MicroAssetsStrategicItem micro_assets_strategic_grandtotal;
		private ArrayList<MicroAssetsStrategicItem> micro_assets_strategic;

		public void setMicro_assets_strategic_grandtotal(MicroAssetsStrategicItem micro_assets_strategic_grandtotal){
			this.micro_assets_strategic_grandtotal = micro_assets_strategic_grandtotal;
		}

		public MicroAssetsStrategicItem getMicro_assets_strategic_grandtotal(){
			return micro_assets_strategic_grandtotal;
		}

		public void setMicro_assets_strategic(ArrayList<MicroAssetsStrategicItem> micro_assets_strategic){
			this.micro_assets_strategic = micro_assets_strategic;
		}

		public ArrayList<MicroAssetsStrategicItem> getMicro_assets_strategic(){
			return micro_assets_strategic;
		}
	}

	public static class MicroAssetsStrategicItem implements Parcelable, Serializable{
		private double variance;
		private String asset_class;
		private int actual_amount;
		private double actual_percentage;
		private String policy;
		private int macro_assets_strategic_variance;
		private int grand_percentage_amount;
		private int grand_total_amount;
		private int macro_assets_strategic_policy;
		private boolean isTotal = false ;
		private String totalAmount = "" ;
		private String totalAmountPercentage = "" ;
		private String totalPolicyPercentage = "" ;
		private String totalVariance = "" ;

		protected MicroAssetsStrategicItem(Parcel in) {
			variance = in.readDouble();
			asset_class = in.readString();
			actual_amount = in.readInt();
			actual_percentage = in.readDouble();
			policy = in.readString();
			totalAmount = in.readString();
			totalAmountPercentage = in.readString();
			totalPolicyPercentage = in.readString();
			totalVariance = in.readString();
			macro_assets_strategic_variance = in.readInt();
			grand_percentage_amount = in.readInt();
			grand_total_amount = in.readInt();
			macro_assets_strategic_policy = in.readInt();
			isTotal = in.readByte() != 0;
		}

		public static final Creator<MicroAssetsStrategicItem> CREATOR = new Creator<MicroAssetsStrategicItem>() {
			@Override
			public MicroAssetsStrategicItem createFromParcel(Parcel in) {
				return new MicroAssetsStrategicItem(in);
			}

			@Override
			public MicroAssetsStrategicItem[] newArray(int size) {
				return new MicroAssetsStrategicItem[size];
			}
		};

		public void setMacro_assets_strategic_variance(int macro_assets_strategic_variance){
			this.macro_assets_strategic_variance = macro_assets_strategic_variance;
		}

		public int getMacro_assets_strategic_variance(){
			return macro_assets_strategic_variance;
		}

		public void setGrand_percentage_amount(int grand_percentage_amount){
			this.grand_percentage_amount = grand_percentage_amount;
		}

		public int getGrand_percentage_amount(){
			return grand_percentage_amount;
		}

		public void setGrand_total_amount(int grand_total_amount){
			this.grand_total_amount = grand_total_amount;
		}

		public int getGrand_total_amount(){
			return grand_total_amount;
		}

		public void setMacro_assets_strategic_policy(int macro_assets_strategic_policy){
			this.macro_assets_strategic_policy = macro_assets_strategic_policy;
		}

		public int getMacro_assets_strategic_policy(){
			return macro_assets_strategic_policy;
		}

		public void setVariance(double variance){
			this.variance = variance;
		}

		public double getVariance(){
			return variance;
		}

		public void setAsset_class(String asset_class){
			this.asset_class = asset_class;
		}

		public String getAsset_class(){
			return asset_class;
		}

		public void setActual_amount(int actual_amount){
			this.actual_amount = actual_amount;
		}

		public int getActual_amount(){
			return actual_amount;
		}

		public void setActual_percentage(double actual_percentage){
			this.actual_percentage = actual_percentage;
		}

		public double getActual_percentage(){
			return actual_percentage;
		}

		public void setPolicy(String policy){
			this.policy = AppUtils.getValidAPIStringResponse(policy);
		}

		public String getPolicy(){
			return policy;
		}

		public boolean isTotal()
		{
			return isTotal;
		}

		public void setTotal(boolean total)
		{
			isTotal = total;
		}

		public String getTotalAmount()
		{
			return totalAmount;
		}

		public void setTotalAmount(String totalAmount)
		{
			this.totalAmount = totalAmount;
		}

		public String getTotalAmountPercentage()
		{
			return totalAmountPercentage;
		}

		public void setTotalAmountPercentage(String totalAmountPercentage)
		{
			this.totalAmountPercentage = totalAmountPercentage;
		}

		public String getTotalPolicyPercentage()
		{
			return totalPolicyPercentage;
		}

		public void setTotalPolicyPercentage(String policyPercentage)
		{
			this.totalPolicyPercentage = policyPercentage;
		}

		public String getTotalVariance()
		{
			return totalVariance;
		}

		public void setTotalVariance(String totalVariance)
		{
			this.totalVariance = totalVariance;
		}


		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(asset_class);
			dest.writeDouble(actual_amount);
			dest.writeDouble(actual_percentage);
			dest.writeString(policy);
			dest.writeDouble(variance);
			dest.writeString(totalAmount);
			dest.writeString(totalAmountPercentage);
			dest.writeString(totalPolicyPercentage);
			dest.writeString(totalVariance);
			dest.writeByte((byte) (isTotal ? 1 : 0));
		}
	}

	public static class MicroAssetsStrategicGrandtotal{
		private int macro_assets_strategic_variance;
		private int grand_percentage_amount;
		private int grand_total_amount;
		private int macro_assets_strategic_policy;

		public void setMacro_assets_strategic_variance(int macro_assets_strategic_variance){
			this.macro_assets_strategic_variance = macro_assets_strategic_variance;
		}

		public int getMacro_assets_strategic_variance(){
			return macro_assets_strategic_variance;
		}

		public void setGrand_percentage_amount(int grand_percentage_amount){
			this.grand_percentage_amount = grand_percentage_amount;
		}

		public int getGrand_percentage_amount(){
			return grand_percentage_amount;
		}

		public void setGrand_total_amount(int grand_total_amount){
			this.grand_total_amount = grand_total_amount;
		}

		public int getGrand_total_amount(){
			return grand_total_amount;
		}

		public void setMacro_assets_strategic_policy(int macro_assets_strategic_policy){
			this.macro_assets_strategic_policy = macro_assets_strategic_policy;
		}

		public int getMacro_assets_strategic_policy(){
			return macro_assets_strategic_policy;
		}
	}

	public static class ApplicantsAssetsDetails{
		private ArrayList<ApplicantDetailsItem> applicant_details;
		private AssetsGrandTotal assets_grand_total;

		public void setApplicant_details(ArrayList<ApplicantDetailsItem> applicant_details){
			this.applicant_details = applicant_details;
		}

		public ArrayList<ApplicantDetailsItem> getApplicant_details(){
			return applicant_details;
		}

		public void setAssets_grand_total(AssetsGrandTotal assets_grand_total){
			this.assets_grand_total = assets_grand_total;
		}

		public AssetsGrandTotal getAssets_grand_total(){
			return assets_grand_total;
		}
	}

	public class AssetsGrandTotal {

		@SerializedName("GrandAmount")
		public String grandAmount;
		@SerializedName("GrandHoldingPercentage")
		public Long grandHoldingPercentage;

		public String getGrandAmount() {
			return grandAmount;
		}

		public void setGrandAmount(String grandAmount) {
			this.grandAmount = grandAmount;
		}

		public Long getGrandHoldingPercentage() {
			return grandHoldingPercentage;
		}

		public void setGrandHoldingPercentage(Long grandHoldingPercentage) {
			this.grandHoldingPercentage = grandHoldingPercentage;
		}

	}

	public static class ApplicantDetailsItem implements Parcelable, Serializable {
		private ApcntAssetsTotal apcnt_assets_total;
		private String applicant_name;
		private ArrayList<ApcntAssetsDetailsItem.ValuesItem> apcnt_assets_details;

		protected ApplicantDetailsItem(Parcel in) {
			apcnt_assets_total = in.readParcelable(ApcntAssetsTotal.class.getClassLoader());
			applicant_name = in.readString();
		}

		public static final Creator<ApplicantDetailsItem> CREATOR = new Creator<ApplicantDetailsItem>() {
			@Override
			public ApplicantDetailsItem createFromParcel(Parcel in) {
				return new ApplicantDetailsItem(in);
			}

			@Override
			public ApplicantDetailsItem[] newArray(int size) {
				return new ApplicantDetailsItem[size];
			}
		};

		public void setApcnt_assets_total(ApcntAssetsTotal apcnt_assets_total){
			this.apcnt_assets_total = apcnt_assets_total;
		}

		public ApcntAssetsTotal getApcnt_assets_total(){
			return apcnt_assets_total;
		}

		public void setApplicant_name(String applicant_name){
			this.applicant_name = applicant_name;
		}

		public String getApplicant_name(){
			return applicant_name;
		}

		public void setApcnt_assets_details(ArrayList<ApcntAssetsDetailsItem.ValuesItem> apcnt_assets_details){
			this.apcnt_assets_details = apcnt_assets_details;
		}

		public ArrayList<ApcntAssetsDetailsItem.ValuesItem> getApcnt_assets_details(){
			return apcnt_assets_details;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(applicant_name);
			dest.writeParcelable(apcnt_assets_total, flags);
			dest.writeTypedList(apcnt_assets_details);
		}

		public static class ApcntAssetsTotal implements Parcelable, Serializable{
			private String TotalAmount;
			private double TotalHoldingPercentage;

			protected ApcntAssetsTotal(Parcel in) {
				TotalAmount = in.readString();
				TotalHoldingPercentage = in.readDouble();
			}

			public static final Creator<ApcntAssetsTotal> CREATOR = new Creator<ApcntAssetsTotal>() {
				@Override
				public ApcntAssetsTotal createFromParcel(Parcel in) {
					return new ApcntAssetsTotal(in);
				}

				@Override
				public ApcntAssetsTotal[] newArray(int size) {
					return new ApcntAssetsTotal[size];
				}
			};

			public void setTotalAmount(String totalAmount){
				this.TotalAmount = totalAmount;
			}

			public String getTotalAmount(){
				return TotalAmount;
			}

			public void setTotalHoldingPercentage(double totalHoldingPercentage){
				this.TotalHoldingPercentage = totalHoldingPercentage;
			}

			public double getTotalHoldingPercentage(){
				return TotalHoldingPercentage;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(TotalAmount);
				dest.writeDouble(TotalHoldingPercentage);
			}
		}

		public static class ApcntAssetsDetailsItem implements Parcelable, Serializable{
			private ApplicantListResponseModel.Total total;
			private ArrayList<ValuesItem> values;
			private String name;

			protected ApcntAssetsDetailsItem(Parcel in) {
				values = in.createTypedArrayList(ValuesItem.CREATOR);
				name = in.readString();
			}

			public static final Creator<ApcntAssetsDetailsItem> CREATOR = new Creator<ApcntAssetsDetailsItem>() {
				@Override
				public ApcntAssetsDetailsItem createFromParcel(Parcel in) {
					return new ApcntAssetsDetailsItem(in);
				}

				@Override
				public ApcntAssetsDetailsItem[] newArray(int size) {
					return new ApcntAssetsDetailsItem[size];
				}
			};

			public void setTotal(ApplicantListResponseModel.Total total){
				this.total = total;
			}

			public ApplicantListResponseModel.Total getTotal(){
				return total;
			}

			public void setValues(ArrayList<ValuesItem> values){
				this.values = values;
			}

			public ArrayList<ValuesItem> getValues(){
				return values;
			}

			public void setName(String name){
				this.name = name;
			}

			public String getName(){
				return name;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeTypedList(values);
				dest.writeString(name);
			}

			public static class ValuesItem implements Parcelable, Serializable{
				public static Creator<ValuesItem> CREATOR;
				private String Objective;
				private double HoldingPercentage;
				private int Amount;

				public void setObjective(String objective){
					this.Objective = AppUtils.getValidAPIStringResponse(objective);
				}

				public String getObjective(){
					return Objective;
				}

				public void setHoldingPercentage(double holdingPercentage){
					this.HoldingPercentage = AppUtils.getValidAPIDoubleResponse(String.valueOf(holdingPercentage));
				}

				public double getHoldingPercentage(){
					return HoldingPercentage;
				}

				public void setAmount(int amount){
					this.Amount = AppUtils.getValidAPIIntegerResponse(String.valueOf(amount));
				}

				public int getAmount(){
					return Amount;
				}

				@Override
				public int describeContents() {
					return 0;
				}

				@Override
				public void writeToParcel(Parcel dest, int flags) {
					dest.writeString(Objective);
					dest.writeInt(Amount);
					dest.writeDouble(HoldingPercentage);
				}
			}
		}
	}

	public static class AssetsData{
		private AssetsDetailsTotal assets_details_total;
		private AssetsDetails assets_details;
		private AssetsDetailsGrandtotal assets_details_grandtotal;

		public void setAssets_details_total(AssetsDetailsTotal assets_details_total){
			this.assets_details_total = assets_details_total;
		}

		public AssetsDetailsTotal getAssets_details_total(){
			return assets_details_total;
		}

		public void setAssets_details(AssetsDetails assets_details){
			this.assets_details = assets_details;
		}

		public AssetsDetails getAssets_details(){
			return assets_details;
		}

		public void setAssets_details_grandtotal(AssetsDetailsGrandtotal assets_details_grandtotal){
			this.assets_details_grandtotal = assets_details_grandtotal;
		}

		public AssetsDetailsGrandtotal getAssets_details_grandtotal(){
			return assets_details_grandtotal;
		}

		public static class AssetsDetailsGrandtotal{
			private int grand_percentage_amount;
			private int grand_total_amount;

			public void setGrand_percentage_amount(int grand_percentage_amount){
				this.grand_percentage_amount = grand_percentage_amount;
			}

			public int getGrand_percentage_amount(){
				return grand_percentage_amount;
			}

			public void setGrand_total_amount(int grand_total_amount){
				this.grand_total_amount = grand_total_amount;
			}

			public int getGrand_total_amount(){
				return grand_total_amount;
			}
		}

		public static class AssetsDetailsTotal{
			private AssetsDetails.DebtItem Debt;
			private AssetsDetails.DebtItem Equity;
			private AssetsDetails.DebtItem Hybrid;

			public void setDebt(AssetsDetails.DebtItem debt){
				this.Debt = debt;
			}

			public AssetsDetails.DebtItem getDebt(){
				return Debt;
			}

			public void setEquity(AssetsDetails.DebtItem equity){
				this.Equity = equity;
			}

			public AssetsDetails.DebtItem getEquity(){
				return Equity;
			}

			public void setHybrid(AssetsDetails.DebtItem hybrid){
				this.Hybrid = hybrid;
			}

			public AssetsDetails.DebtItem getHybrid(){
				return Hybrid;
			}

			public static class Hybrid{
				private double percentage_amount;
				private int total_amount;

				public void setPercentage_amount(double percentage_amount){
					this.percentage_amount = percentage_amount;
				}

				public double getPercentage_amount(){
					return percentage_amount;
				}

				public void setTotal_amount(int total_amount){
					this.total_amount = total_amount;
				}

				public int getTotal_amount(){
					return total_amount;
				}
			}

			public static class Debt{
				private double percentage_amount;
				private int total_amount;

				public void setPercentage_amount(double percentage_amount){
					this.percentage_amount = percentage_amount;
				}

				public double getPercentage_amount(){
					return percentage_amount;
				}

				public void setTotal_amount(int total_amount){
					this.total_amount = total_amount;
				}

				public int getTotal_amount(){
					return total_amount;
				}
			}

			public static class Equity{
				private double percentage_amount;
				private int total_amount;

				public void setPercentage_amount(double percentage_amount){
					this.percentage_amount = percentage_amount;
				}

				public double getPercentage_amount(){
					return percentage_amount;
				}

				public void setTotal_amount(int total_amount){
					this.total_amount = total_amount;
				}

				public int getTotal_amount(){
					return total_amount;
				}
			}
		}

		public static class AssetsDetails{
			private ArrayList<DebtItem> Debt;
			private ArrayList<DebtItem> Equity;
			private ArrayList<DebtItem> Hybrid;

			public void setDebt(ArrayList<DebtItem> debt){
				this.Debt = debt;
			}

			public ArrayList<DebtItem> getDebt(){
				return Debt;
			}

			public void setEquity(ArrayList<DebtItem> equity){
				this.Equity = equity;
			}

			public ArrayList<DebtItem> getEquity(){
				return Equity;
			}

			public void setHybrid(ArrayList<DebtItem> hybrid){
				this.Hybrid = hybrid;
			}

			public ArrayList<DebtItem> getHybrid(){
				return Hybrid;
			}

			public static class DebtItem implements Parcelable, Serializable{
				private String Objective = "";
				private String HoldingPercentage = "";
				private String Amount = "";
				private double percentage_amount  =0;
				private int total_amount = 0;
				private String AssetsTitle = "" ;
				private String TotalAmount = "" ;
				private String TotalAllocation = "" ;
				private boolean isTotal = false ;

				public DebtItem(Parcel in) {
					Objective = in.readString();
					HoldingPercentage = in.readString();
					Amount = in.readString();
					percentage_amount = in.readDouble();
					total_amount = in.readInt();
					AssetsTitle = in.readString();
					TotalAmount = in.readString();
					TotalAllocation = in.readString();
					isTotal = in.readByte() != 0;
				}

				public static final Creator<DebtItem> CREATOR = new Creator<DebtItem>() {
					@Override
					public DebtItem createFromParcel(Parcel in) {
						return new DebtItem(in);
					}

					@Override
					public DebtItem[] newArray(int size) {
						return new DebtItem[size];
					}
				};

				public DebtItem()
				{

				}

				public int getTotal_amount()
				{
					return total_amount;
				}

				public void setTotal_amount(int total_amount)
				{
					this.total_amount = total_amount;
				}

				public double getPercentage_amount()
				{
					return percentage_amount;
				}

				public void setPercentage_amount(double percentage_amount)
				{
					this.percentage_amount = percentage_amount;
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

				public void setAmount(String amount){
					this.Amount = AppUtils.getValidAPIStringResponse(amount);
				}

				public String getAmount(){
					return Amount;
				}

				public String getAssetsTitle()
				{
					return AssetsTitle;
				}

				public void setAssetsTitle(String assetsTitle)
				{
					AssetsTitle = AppUtils.getValidAPIStringResponse(assetsTitle);
				}

				public String getTotalAmount()
				{
					return TotalAmount;
				}

				public void setTotalAmount(String totalAmount)
				{
					TotalAmount = totalAmount;
				}

				public String getTotalAllocation()
				{
					return TotalAllocation;
				}

				public void setTotalAllocation(String totalAllocation)
				{
					TotalAllocation = totalAllocation;
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
					dest.writeString(Objective);
					dest.writeString(Amount);
					dest.writeString(HoldingPercentage);
					dest.writeInt(total_amount);
					dest.writeString(AssetsTitle);
					dest.writeString(TotalAmount);
					dest.writeString(TotalAllocation);
					dest.writeByte((byte) (isTotal ? 1 : 0));
				}
			}

			public static class HybridItem{
				private String Objective;
				private String HoldingPercentage;
				private String Amount;

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

				public void setAmount(String amount){
					this.Amount = amount;
				}

				public String getAmount(){
					return Amount;
				}
			}

			public static class EquityItem{
				private String Objective;
				private String HoldingPercentage;
				private String Amount;

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

				public void setAmount(String amount){
					this.Amount = amount;
				}

				public String getAmount(){
					return Amount;
				}
			}
		}
	}

	public static class MicroTactical{
		private MicroAssetsTacticalItem micro_assets_tactical_grandtotal;
		private ArrayList<MicroAssetsTacticalItem> micro_assets_tactical;

		public void setMicro_assets_tactical_grandtotal(MicroAssetsTacticalItem micro_assets_tactical_grandtotal){
			this.micro_assets_tactical_grandtotal = micro_assets_tactical_grandtotal;
		}

		public MicroAssetsTacticalItem getMicro_assets_tactical_grandtotal(){
			return micro_assets_tactical_grandtotal;
		}

		public void setMicro_assets_tactical(ArrayList<MicroAssetsTacticalItem> micro_assets_tactical){
			this.micro_assets_tactical = micro_assets_tactical;
		}

		public ArrayList<MicroAssetsTacticalItem> getMicro_assets_tactical(){
			return micro_assets_tactical;
		}

		public static class MicroAssetsTacticalItem implements Parcelable, Serializable{
			private double variance;
			private String asset_class;
			private int actual_amount;
			private double actual_percentage;
			private double policy;
			private int micro_assets_tactical_policy;
			private int grand_percentage_amount;
			private int grand_total_amount;
			private int micro_assets_tactical_variance_total;
			private String totalPercentage = "" ;
			private String totalPolicyPercentage = "" ;
			private String toalVariance = "" ;
			private boolean isTotal = false ;

			protected MicroAssetsTacticalItem(Parcel in) {
				variance = in.readDouble();
				asset_class = in.readString();
				actual_amount = in.readInt();
				actual_percentage = in.readDouble();
				policy = in.readInt();
				micro_assets_tactical_policy = in.readInt();
				grand_percentage_amount = in.readInt();
				grand_total_amount = in.readInt();
				micro_assets_tactical_variance_total = in.readInt();
				totalPercentage = in.readString();
				totalPolicyPercentage = in.readString();
				toalVariance = in.readString();
				isTotal = in.readByte() != 0;
			}

			public static final Creator<MicroAssetsTacticalItem> CREATOR = new Creator<MicroAssetsTacticalItem>() {
				@Override
				public MicroAssetsTacticalItem createFromParcel(Parcel in) {
					return new MicroAssetsTacticalItem(in);
				}

				@Override
				public MicroAssetsTacticalItem[] newArray(int size) {
					return new MicroAssetsTacticalItem[size];
				}
			};

			public void setMicro_assets_tactical_policy(int micro_assets_tactical_policy){
				this.micro_assets_tactical_policy = micro_assets_tactical_policy;
			}

			public int getMicro_assets_tactical_policy(){
				return micro_assets_tactical_policy;
			}

			public void setGrand_percentage_amount(int grand_percentage_amount){
				this.grand_percentage_amount = grand_percentage_amount;
			}

			public int getGrand_percentage_amount(){
				return grand_percentage_amount;
			}

			public void setGrand_total_amount(int grand_total_amount){
				this.grand_total_amount = grand_total_amount;
			}

			public int getGrand_total_amount(){
				return grand_total_amount;
			}

			public void setMicro_assets_tactical_variance_total(int micro_assets_tactical_variance_total){
				this.micro_assets_tactical_variance_total = micro_assets_tactical_variance_total;
			}

			public int getMicro_assets_tactical_variance_total(){
				return micro_assets_tactical_variance_total;
			}

			public String getTotalPercentage()
			{
				return totalPercentage;
			}

			public void setTotalPercentage(String totalPercentage)
			{
				this.totalPercentage = totalPercentage;
			}

			public String getTotalPolicyPercentage()
			{
				return totalPolicyPercentage;
			}

			public void setTotalPolicyPercentage(String totalPolicyPercentage)
			{
				this.totalPolicyPercentage = totalPolicyPercentage;
			}

			public String getToalVariance()
			{
				return toalVariance;
			}

			public void setToalVariance(String toalVariance)
			{
				this.toalVariance = toalVariance;
			}

			public boolean isTotal()
			{
				return isTotal;
			}

			public void setTotal(boolean total)
			{
				isTotal = total;
			}

			public void setVariance(double variance){
				this.variance = variance;
			}

			public double getVariance(){
				return variance;
			}

			public void setAsset_class(String asset_class){
				this.asset_class = asset_class;
			}

			public String getAsset_class(){
				return asset_class;
			}

			public void setActual_amount(int actual_amount){
				this.actual_amount = actual_amount;
			}

			public int getActual_amount(){
				return actual_amount;
			}

			public void setActual_percentage(double actual_percentage){
				this.actual_percentage = actual_percentage;
			}

			public double getActual_percentage(){
				return actual_percentage;
			}

			public void setPolicy(double policy){
				this.policy = policy;
			}

			public double getPolicy(){
				return policy;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(asset_class);
				dest.writeDouble(actual_percentage);
				dest.writeDouble(policy);
				dest.writeDouble(variance);
				dest.writeString(totalPercentage);
				dest.writeString(totalPolicyPercentage);
				dest.writeString(toalVariance);
				dest.writeByte((byte) (isTotal ? 1 : 0));
			}
		}

		public static class MicroAssetsTacticalGrandtotal{
			private int micro_assets_tactical_policy;
			private int grand_percentage_amount;
			private int grand_total_amount;
			private int micro_assets_tactical_variance_total;

			public void setMicro_assets_tactical_policy(int micro_assets_tactical_policy){
				this.micro_assets_tactical_policy = micro_assets_tactical_policy;
			}

			public int getMicro_assets_tactical_policy(){
				return micro_assets_tactical_policy;
			}

			public void setGrand_percentage_amount(int grand_percentage_amount){
				this.grand_percentage_amount = grand_percentage_amount;
			}

			public int getGrand_percentage_amount(){
				return grand_percentage_amount;
			}

			public void setGrand_total_amount(int grand_total_amount){
				this.grand_total_amount = grand_total_amount;
			}

			public int getGrand_total_amount(){
				return grand_total_amount;
			}

			public void setMicro_assets_tactical_variance_total(int micro_assets_tactical_variance_total){
				this.micro_assets_tactical_variance_total = micro_assets_tactical_variance_total;
			}

			public int getMicro_assets_tactical_variance_total(){
				return micro_assets_tactical_variance_total;
			}
		}

	}

	public static class MacroTactical{
		private ArrayList<MacroAssetsTacticalItem> macro_assets_tactical;
		private MacroAssetsTacticalItem macro_assets_tactical_grandtotal;

		public void setMacro_assets_tactical(ArrayList<MacroAssetsTacticalItem> macro_assets_tactical){
			this.macro_assets_tactical = macro_assets_tactical;
		}

		public ArrayList<MacroAssetsTacticalItem> getMacro_assets_tactical(){
			return macro_assets_tactical;
		}

		public void setMacro_assets_tactical_grandtotal(MacroAssetsTacticalItem macro_assets_tactical_grandtotal){
			this.macro_assets_tactical_grandtotal = macro_assets_tactical_grandtotal;
		}

		public MacroAssetsTacticalItem getMacro_assets_tactical_grandtotal(){
			return macro_assets_tactical_grandtotal;
		}

		public static class MacroAssetsTacticalGrandtotal{
			private int grand_percentage_amount;
			private int macro_assets_tactical_policy;
			private int macro_assets_tactical_variance;
			private int grand_total_amount;

			public void setGrand_percentage_amount(int grand_percentage_amount){
				this.grand_percentage_amount = grand_percentage_amount;
			}

			public int getGrand_percentage_amount(){
				return grand_percentage_amount;
			}

			public void setMacro_assets_tactical_policy(int macro_assets_tactical_policy){
				this.macro_assets_tactical_policy = macro_assets_tactical_policy;
			}

			public int getMacro_assets_tactical_policy(){
				return macro_assets_tactical_policy;
			}

			public void setMacro_assets_tactical_variance(int macro_assets_tactical_variance){
				this.macro_assets_tactical_variance = macro_assets_tactical_variance;
			}

			public int getMacro_assets_tactical_variance(){
				return macro_assets_tactical_variance;
			}

			public void setGrand_total_amount(int grand_total_amount){
				this.grand_total_amount = grand_total_amount;
			}

			public int getGrand_total_amount(){
				return grand_total_amount;
			}
		}

		public static class MacroAssetsTacticalItem implements Parcelable, Serializable{
			private double variance;
			private String asset_class;
			private int actual_amount;
			private double actual_percentage;
			private double policy;
			private int grand_percentage_amount;
			private int macro_assets_tactical_policy;
			private int macro_assets_tactical_variance;
			private int grand_total_amount;
			private String totalPercentage = "" ;
			private String totalPolicyPercentage = "" ;
			private String toalVariance = "" ;
			private boolean isTotal = false ;

			protected MacroAssetsTacticalItem(Parcel in) {
				variance = in.readDouble();
				asset_class = in.readString();
				actual_amount = in.readInt();
				actual_percentage = in.readDouble();
				policy = in.readDouble();
				grand_percentage_amount = in.readInt();
				macro_assets_tactical_policy = in.readInt();
				macro_assets_tactical_variance = in.readInt();
				grand_total_amount = in.readInt();
				totalPercentage = in.readString();
				totalPolicyPercentage = in.readString();
				toalVariance = in.readString();
				isTotal = in.readByte() != 0;
			}

			public static final Creator<MacroAssetsTacticalItem> CREATOR = new Creator<MacroAssetsTacticalItem>() {
				@Override
				public MacroAssetsTacticalItem createFromParcel(Parcel in) {
					return new MacroAssetsTacticalItem(in);
				}

				@Override
				public MacroAssetsTacticalItem[] newArray(int size) {
					return new MacroAssetsTacticalItem[size];
				}
			};

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(asset_class);
				dest.writeDouble(actual_percentage);
				dest.writeDouble(policy);
				dest.writeDouble(variance);
				dest.writeInt(actual_amount);
				dest.writeString(totalPercentage);
				dest.writeString(totalPolicyPercentage);
				dest.writeString(toalVariance);
				dest.writeByte((byte) (isTotal ? 1 : 0));
			}

			public String getTotalPercentage()
			{
				return totalPercentage;
			}

			public void setTotalPercentage(String totalPercentage)
			{
				this.totalPercentage = totalPercentage;
			}

			public String getTotalPolicyPercentage()
			{
				return totalPolicyPercentage;
			}

			public void setTotalPolicyPercentage(String totalPolicyPercentage)
			{
				this.totalPolicyPercentage = totalPolicyPercentage;
			}

			public String getToalVariance()
			{
				return toalVariance;
			}

			public void setToalVariance(String toalVariance)
			{
				this.toalVariance = toalVariance;
			}

			public boolean isTotal()
			{
				return isTotal;
			}

			public void setTotal(boolean total)
			{
				isTotal = total;
			}

			public void setGrand_percentage_amount(int grand_percentage_amount){
				this.grand_percentage_amount = grand_percentage_amount;
			}

			public int getGrand_percentage_amount(){
				return grand_percentage_amount;
			}

			public void setMacro_assets_tactical_policy(int macro_assets_tactical_policy){
				this.macro_assets_tactical_policy = macro_assets_tactical_policy;
			}

			public int getMacro_assets_tactical_policy(){
				return macro_assets_tactical_policy;
			}

			public void setMacro_assets_tactical_variance(int macro_assets_tactical_variance){
				this.macro_assets_tactical_variance = macro_assets_tactical_variance;
			}

			public int getMacro_assets_tactical_variance(){
				return macro_assets_tactical_variance;
			}

			public void setGrand_total_amount(int grand_total_amount){
				this.grand_total_amount = grand_total_amount;
			}

			public int getGrand_total_amount(){
				return grand_total_amount;
			}

			public void setVariance(double variance){
				this.variance = variance;
			}

			public double getVariance(){
				return variance;
			}

			public void setAsset_class(String asset_class){
				this.asset_class = asset_class;
			}

			public String getAsset_class(){
				return asset_class;
			}

			public void setActual_amount(int actual_amount){
				this.actual_amount = actual_amount;
			}

			public int getActual_amount(){
				return actual_amount;
			}

			public void setActual_percentage(double actual_percentage){
				this.actual_percentage = actual_percentage;
			}

			public double getActual_percentage(){
				return actual_percentage;
			}

			public void setPolicy(double policy){
				this.policy = policy;
			}

			public double getPolicy(){
				return policy;
			}


		}
	}
}
