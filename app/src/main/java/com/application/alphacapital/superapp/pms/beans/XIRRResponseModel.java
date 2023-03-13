package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class XIRRResponseModel{
	private SinceInception since_inception;
	private ArrayList<PerformanceDetailsItem> performance_details;
	private int success;
	private ArrayList<PerformanceDetailsCateItem> performance_details_cate;
	private PerfomanceResponseModel.PerformanceDetailsTotal performance_details_total;
	private OpeningBalance opening_balance;

	public SinceInception getSince_inception(){
		return since_inception;
	}

	public ArrayList<PerformanceDetailsItem> getPerformance_details(){
		return performance_details;
	}

	public int getSuccess(){
		return success;
	}

	public ArrayList<PerformanceDetailsCateItem> getPerformance_details_cate(){
		return performance_details_cate;
	}

	public PerfomanceResponseModel.PerformanceDetailsTotal getPerformance_details_total(){
		return performance_details_total;
	}

	public OpeningBalance getOpening_balance(){
		return opening_balance;
	}

	public static class SinceInception{
		private PerfomanceResponseModel.GrandTotal grand_total;
		private ArrayList<SinceInceptionDetailsItem> since_inception_details;

		public PerfomanceResponseModel.GrandTotal getGrand_total(){
			return grand_total;
		}

		public ArrayList<SinceInceptionDetailsItem> getSince_inception_details(){
			return since_inception_details;
		}
	}

	public static class SinceInceptionDetailsItem implements Parcelable, Serializable {
		private double AmountInvested;
		private String asset_type;
		private double CurrentValue;
		private double xirr;
		private double Abreturn;
		private double total_days;
		private double gain;
		private double HoldingValue;

		protected SinceInceptionDetailsItem(Parcel in) {
			AmountInvested = in.readInt();
			asset_type = in.readString();
			CurrentValue = in.readInt();
			xirr = in.readInt();
			Abreturn = in.readInt();
			total_days = in.readDouble();
			gain = in.readInt();
			HoldingValue = in.readInt();
		}

		public static final Creator<SinceInceptionDetailsItem> CREATOR = new Creator<SinceInceptionDetailsItem>() {
			@Override
			public SinceInceptionDetailsItem createFromParcel(Parcel in) {
				return new SinceInceptionDetailsItem(in);
			}

			@Override
			public SinceInceptionDetailsItem[] newArray(int size) {
				return new SinceInceptionDetailsItem[size];
			}
		};

		public double getAmountInvested(){
			return AmountInvested;
		}

		public String getAsset_type(){
			return asset_type;
		}

		public double getCurrentValue(){
			return CurrentValue;
		}

		public double getXirr(){
			return xirr;
		}

		public double getAbreturn(){
			return Abreturn;
		}

		public double getTotal_days(){
			return total_days;
		}

		public double getGain(){
			return gain;
		}

		public double getHoldingValue(){
			return HoldingValue;
		}


		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeDouble(AmountInvested);
			dest.writeDouble(HoldingValue);
			dest.writeString(asset_type);
			dest.writeDouble(CurrentValue);
			dest.writeDouble(total_days);
			dest.writeDouble(gain);
			dest.writeDouble(Abreturn);
			dest.writeDouble(xirr);
		}
	}


	public static class PerformanceDetailsCateItem implements Parcelable,Serializable{
		private PortfolioTotal portfolio_total;
		private ArrayList<PortfolioValueItem> portfolio_value;
		private OpeningBalance opening_balance;
		private String portfolio_key;

		protected PerformanceDetailsCateItem(Parcel in) {
			portfolio_total = in.readParcelable(PortfolioTotal.class.getClassLoader());
			portfolio_value = in.createTypedArrayList(PortfolioValueItem.CREATOR);
			portfolio_key = in.readString();
		}

		public static final Creator<PerformanceDetailsCateItem> CREATOR = new Creator<PerformanceDetailsCateItem>() {
			@Override
			public PerformanceDetailsCateItem createFromParcel(Parcel in) {
				return new PerformanceDetailsCateItem(in);
			}

			@Override
			public PerformanceDetailsCateItem[] newArray(int size) {
				return new PerformanceDetailsCateItem[size];
			}
		};

		public PortfolioTotal getPortfolio_total(){
			return portfolio_total;
		}

		public ArrayList<PortfolioValueItem> getPortfolio_value(){
			return portfolio_value;
		}

		public OpeningBalance getOpening_balance(){
			return opening_balance;
		}

		public String getPortfolio_key(){
			return portfolio_key;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(portfolio_key);
			dest.writeParcelable(portfolio_total, flags);
			dest.writeTypedList(portfolio_value);
		}
	}

	public class PortfolioTotal implements Parcelable,Serializable{
		private double HoldingValue;
		private double AmountInvested;
		private double CurrentValue;
		private double xirr;
		private double Abreturn;
		private double gain;
		private double total_days;

		protected PortfolioTotal(Parcel in) {
			HoldingValue = in.readInt();
			AmountInvested = in.readInt();
			CurrentValue = in.readInt();
			xirr = in.readInt();
			Abreturn = in.readInt();
			gain = in.readInt();
			total_days = in.readDouble();
		}

		public final Creator<PortfolioTotal> CREATOR = new Creator<PortfolioTotal>() {
			@Override
			public PortfolioTotal createFromParcel(Parcel in) {
				return new PortfolioTotal(in);
			}

			@Override
			public PortfolioTotal[] newArray(int size) {
				return new PortfolioTotal[size];
			}
		};

		public double getHoldingValue(){
			return HoldingValue;
		}

		public double getAmountInvested(){
			return AmountInvested;
		}

		public double getCurrentValue(){
			return CurrentValue;
		}

		public double getXirr(){
			return xirr;
		}

		public double getAbreturn(){
			return Abreturn;
		}

		public double getGain(){
			return gain;
		}

		public double getTotal_days(){
			return total_days;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeDouble(CurrentValue);
			dest.writeDouble(AmountInvested);
			dest.writeDouble(HoldingValue);
			dest.writeDouble(gain);
			dest.writeDouble(total_days);
			dest.writeDouble(Abreturn);
			dest.writeDouble(xirr);
		}
	}



	public static class PortfolioValueItem  implements Parcelable,Serializable{
		private String Status;
		private double TranTimestamp;
		private String Nav;
		private double HoldingValue;
		private String TranDate;
		private String sub_category;
		private String Amount;
		private String Holder;
		private String FolioNo;
		private String Units;
		private String SchemeName;
		private String category;
		private int holdingDays;

		protected PortfolioValueItem(Parcel in) {
			Status = in.readString();
			TranTimestamp = in.readInt();
			Nav = in.readString();
			HoldingValue = in.readInt();
			TranDate = in.readString();
			sub_category = in.readString();
			Amount = in.readString();
			Holder = in.readString();
			FolioNo = in.readString();
			Units = in.readString();
			SchemeName = in.readString();
			category = in.readString();
			holdingDays = in.readInt();
		}

		public static final Creator<PortfolioValueItem> CREATOR = new Creator<PortfolioValueItem>() {
			@Override
			public PortfolioValueItem createFromParcel(Parcel in) {
				return new PortfolioValueItem(in);
			}

			@Override
			public PortfolioValueItem[] newArray(int size) {
				return new PortfolioValueItem[size];
			}
		};

		public String getStatus(){
			return Status;
		}

		public double getTranTimestamp(){
			return TranTimestamp;
		}

		public String getNav(){
			return Nav;
		}

		public double getHoldingValue(){
			return HoldingValue;
		}

		public String getTranDate(){
			return TranDate;
		}

		public String getSub_category(){
			return sub_category;
		}

		public String getAmount(){
			return Amount;
		}

		public String getHolder(){
			return Holder;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public String getUnits(){
			return Units;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public String getCategory(){
			return category;
		}

		public int getHoldingDays(){
			return holdingDays;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(sub_category);
			dest.writeString(category);
			dest.writeString(SchemeName);
			dest.writeString(TranDate);
			dest.writeString(FolioNo);
			dest.writeString(Amount);
			dest.writeString(Holder);
			dest.writeString(Status);
			dest.writeString(Units);
			dest.writeString(Nav);
			dest.writeDouble(TranTimestamp);
			dest.writeInt(holdingDays);
			dest.writeDouble(HoldingValue);
		}
	}


	public static class PerformanceDetailsItem implements Parcelable,Serializable{
		private String Status;
		private int TranTimestamp;
		private String Nav;
		private double holdingValue;
		private String TranDate;
		private String sub_category;
		private String Amount;
		private String Holder;
		private String FolioNo;
		private String Units;
		private String SchemeName;
		private String category;
		private int holdingDays;

		protected PerformanceDetailsItem(Parcel in) {
			Status = in.readString();
			TranTimestamp = in.readInt();
			Nav = in.readString();
			holdingValue = in.readInt();
			TranDate = in.readString();
			sub_category = in.readString();
			Amount = in.readString();
			Holder = in.readString();
			FolioNo = in.readString();
			Units = in.readString();
			SchemeName = in.readString();
			category = in.readString();
			holdingDays = in.readInt();
		}

		public static final Creator<PerformanceDetailsItem> CREATOR = new Creator<PerformanceDetailsItem>() {
			@Override
			public PerformanceDetailsItem createFromParcel(Parcel in) {
				return new PerformanceDetailsItem(in);
			}

			@Override
			public PerformanceDetailsItem[] newArray(int size) {
				return new PerformanceDetailsItem[size];
			}
		};

		public String getStatus(){
			return Status;
		}

		public double getTranTimestamp(){
			return TranTimestamp;
		}

		public String getNav(){
			return Nav;
		}

		public double getHoldingValue(){
			return holdingValue;
		}

		public String getTranDate(){
			return TranDate;
		}

		public String getSub_category(){
			return sub_category;
		}

		public String getAmount(){
			return Amount;
		}

		public String getHolder(){
			return Holder;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public String getUnits(){
			return Units;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public String getCategory(){
			return category;
		}

		public int getHoldingDays(){
			return holdingDays;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(sub_category);
			dest.writeString(category);
			dest.writeString(SchemeName);
			dest.writeString(TranDate);
			dest.writeString(FolioNo);
			dest.writeString(Amount);
			dest.writeString(Holder);
			dest.writeString(Status);
			dest.writeString(Units);
			dest.writeString(Nav);
			dest.writeInt(TranTimestamp);
			dest.writeInt(holdingDays);
			dest.writeDouble(holdingValue);
		}
	}

}