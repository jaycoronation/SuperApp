package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class PerfomanceResponseModel{
	private SinceInception since_inception = new SinceInception();
	private ArrayList<PerformanceDetailsItem> performance_details = new ArrayList<>();
	private int success = 0;
	private ArrayList<PerformanceDetailsCateItem> performance_details_cate = new ArrayList<>();
	private PerformanceDetailsTotal performance_details_total = new PerformanceDetailsTotal();

	public void setSince_inception(SinceInception since_inception){
		this.since_inception = since_inception;
	}

	public SinceInception getSince_inception(){
		return since_inception;
	}

	public void setPerformance_details(ArrayList<PerformanceDetailsItem> performance_details){
		this.performance_details = performance_details;
	}

	public ArrayList<PerformanceDetailsItem> getPerformance_details(){
		return performance_details;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setPerformance_details_cate(ArrayList<PerformanceDetailsCateItem> performance_details_cate){
		this.performance_details_cate = performance_details_cate;
	}

	public ArrayList<PerformanceDetailsCateItem> getPerformance_details_cate(){
		return performance_details_cate;
	}

	public void setPerformance_details_total(PerformanceDetailsTotal performance_details_total){
		this.performance_details_total = performance_details_total;
	}

	public PerformanceDetailsTotal getPerformance_details_total(){
		return performance_details_total;
	}

	public static class SinceInception{
		private GrandTotal grand_total = new GrandTotal();
		private ArrayList<SinceInceptionDetailsItem> since_inception_details = new ArrayList<>();

		public void setGrand_total(GrandTotal grand_total){
			this.grand_total = grand_total;
		}

		public GrandTotal getGrand_total(){
			return grand_total;
		}

		public void setSince_inception_details(ArrayList<SinceInceptionDetailsItem> since_inception_details){
			this.since_inception_details = since_inception_details;
		}

		public ArrayList<SinceInceptionDetailsItem> getSince_inception_details(){
			return since_inception_details;
		}
	}

	public static class GrandTotal{
		private double AmountInvested = 0;
		private long CurrentValue = 0;
		private double xirr = 0;
		private double Abreturn = 0;
		private double gain = 0;
		private long holding_amount = 0;
		private double total_days = 0;

		public double getAmountInvested()
		{
			return AmountInvested;
		}

		public void setAmountInvested(double amountInvested)
		{
			AmountInvested = amountInvested;
		}

		public long getCurrentValue()
		{
			return CurrentValue;
		}

		public void setCurrentValue(long currentValue)
		{
			CurrentValue = currentValue;
		}

		public double getXirr()
		{
			return xirr;
		}

		public void setXirr(double xirr)
		{
			this.xirr = xirr;
		}

		public double getGain()
		{
			return gain;
		}

		public void setGain(double gain)
		{
			this.gain = gain;
		}

		public double getAbreturn()
		{
			return Abreturn;
		}

		public void setAbreturn(double abreturn)
		{
			Abreturn = abreturn;
		}


		public long getHolding_amount()
		{
			return holding_amount;
		}

		public void setHolding_amount(long holding_amount)
		{
			this.holding_amount = holding_amount;
		}

		public double getTotal_days()
		{
			return total_days;
		}

		public void setTotal_days(double total_days)
		{
			this.total_days = total_days;
		}
	}

	public static class SinceInceptionDetailsItem implements Parcelable, Serializable {
		private long HoldingValue = 0;
		private long AmountInvested = 0;
		private String asset_type = "";
		private long CurrentValue = 0;
		private double xirr = 0;
		private double Abreturn = 0;
		private double total_days = 0;
		private double gain = 0;

		protected SinceInceptionDetailsItem(Parcel in) {
			HoldingValue = in.readLong();
			AmountInvested = in.readInt();
			asset_type = in.readString();
			CurrentValue = in.readInt();
			xirr = in.readInt();
			Abreturn = in.readInt();
			total_days = in.readDouble();
			gain = in.readDouble();
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

		public long getHoldingValue()
		{
			return HoldingValue;
		}

		public void setHoldingValue(long holdingValue)
		{
			HoldingValue = holdingValue;
		}

		public long getAmountInvested()
		{
			return AmountInvested;
		}

		public void setAmountInvested(long amountInvested)
		{
			AmountInvested = amountInvested;
		}

		public String getAsset_type()
		{
			return asset_type;
		}

		public void setAsset_type(String asset_type)
		{
			this.asset_type = asset_type;
		}

		public long getCurrentValue()
		{
			return CurrentValue;
		}

		public void setCurrentValue(long currentValue)
		{
			CurrentValue = currentValue;
		}

		public double getXirr()
		{
			return xirr;
		}

		public void setXirr(double xirr)
		{
			this.xirr = xirr;
		}

		public double getAbreturn()
		{
			return Abreturn;
		}

		public void setAbreturn(double abreturn)
		{
			Abreturn = abreturn;
		}

		public double getTotal_days()
		{
			return total_days;
		}

		public void setTotal_days(double total_days)
		{
			this.total_days = total_days;
		}

		public double getGain()
		{
			return gain;
		}

		public void setGain(double gain)
		{
			this.gain = gain;
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

	public static class PerformanceDetailsTotal{
		private double AmountInvested = 0;
		private double CurrentValue  = 0;
		private double xirr = 0;
		private double Abreturn = 0;
		private double gain = 0;
		private long holding_amount = 0;
		private double total_days = 0;

		public void setAmountInvested(double amountInvested){
			this.AmountInvested = amountInvested;
		}

		public double getAmountInvested(){
			return AmountInvested;
		}

		public void setCurrentValue(int currentValue){
			this.CurrentValue = currentValue;
		}

		public double getCurrentValue(){
			return CurrentValue;
		}

		public void setXirr(double xirr){
			this.xirr = xirr;
		}

		public double getXirr(){
			return xirr;
		}

		public void setAbreturn(double abreturn){
			this.Abreturn = abreturn;
		}

		public double getAbreturn(){
			return Abreturn;
		}

		public void setGain(int gain){
			this.gain = gain;
		}

		public double getGain(){
			return gain;
		}

		public void setHolding_amount(long holding_amount){
			this.holding_amount = holding_amount;
		}

		public long getHolding_amount(){
			return holding_amount;
		}

		public void setTotal_days(double total_days){
			this.total_days = total_days;
		}

		public double getTotal_days(){
			return total_days;
		}
	}

	public static class PerformanceDetailsCateItem implements Parcelable,Serializable{
		private ArrayList<PortfolioValueItem> portfolio_value = new ArrayList<>();
		private PortfolioTotal portfolio_total =  new PortfolioTotal();
		private String portfolio_key = "";

		protected PerformanceDetailsCateItem(Parcel in) {
			portfolio_value = in.createTypedArrayList(PortfolioValueItem.CREATOR);
			portfolio_total = in.readParcelable(PortfolioTotal.class.getClassLoader());
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

		public void setPortfolio_value(ArrayList<PortfolioValueItem> portfolio_value){
			this.portfolio_value = portfolio_value;
		}

		public ArrayList<PortfolioValueItem> getPortfolio_value(){
			return portfolio_value;
		}

		public void setPortfolio_total(PortfolioTotal portfolio_total){
			this.portfolio_total = portfolio_total;
		}

		public PortfolioTotal getPortfolio_total(){
			return portfolio_total;
		}

		public void setPortfolio_key(String portfolio_key){
			this.portfolio_key = portfolio_key;
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

		public static class PortfolioValueItem implements Parcelable, Serializable{
			private String Status = "";
			private int TranTimestamp = 0;
			private String Nav = "";
			private long HoldingValue = 0;
			private String TranDate = "";
			private String sub_category = "";
			private String Amount = "";
			private String Holder = "";
			private String FolioNo = "";
			private String Units = "";
			private String SchemeName = "";
			private String category = "";
			private int holdingDays = 0;

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

			public void setStatus(String status){
				this.Status = status;
			}

			public String getStatus(){
				return Status;
			}

			public void setTranTimestamp(int tranTimestamp){
				this.TranTimestamp = tranTimestamp;
			}

			public int getTranTimestamp(){
				return TranTimestamp;
			}

			public void setNav(String nav){
				this.Nav = nav;
			}

			public String getNav(){
				return Nav;
			}

			public void setHoldingValue(long holdingValue){
				this.HoldingValue = holdingValue;
			}

			public long getHoldingValue(){
				return HoldingValue;
			}

			public void setTranDate(String tranDate){
				this.TranDate = tranDate;
			}

			public String getTranDate(){
				return TranDate;
			}

			public void setSub_category(String sub_category){
				this.sub_category = sub_category;
			}

			public String getSub_category(){
				return sub_category;
			}

			public void setAmount(String amount){
				this.Amount = amount;
			}

			public String getAmount(){
				return Amount;
			}

			public void setHolder(String holder){
				this.Holder = holder;
			}

			public String getHolder(){
				return Holder;
			}

			public void setFolioNo(String folioNo){
				this.FolioNo = folioNo;
			}

			public String getFolioNo(){
				return FolioNo;
			}

			public void setUnits(String units){
				this.Units = units;
			}

			public String getUnits(){
				return Units;
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

			public void setHoldingDays(int holdingDays){
				this.holdingDays = holdingDays;
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
				dest.writeDouble(HoldingValue);
			}
		}


		public static class PortfolioTotal implements Parcelable,Serializable{
			private long HoldingValue =0;
			private int AmountInvested = 0;
			private int CurrentValue = 0;
			private double xirr = 0;
			private double Abreturn = 0;
			private int gain = 0;
			private double total_days = 0;

			protected PortfolioTotal(Parcel in) {
				HoldingValue = in.readLong();
				AmountInvested = in.readInt();
				CurrentValue = in.readInt();
				xirr = in.readInt();
				Abreturn = in.readInt();
				gain = in.readInt();
				total_days = in.readDouble();
			}

			public static final Creator<PortfolioTotal> CREATOR = new Creator<PortfolioTotal>() {
				@Override
				public PortfolioTotal createFromParcel(Parcel in) {
					return new PortfolioTotal(in);
				}

				@Override
				public PortfolioTotal[] newArray(int size) {
					return new PortfolioTotal[size];
				}
			};

			public PortfolioTotal()
			{

			}

			public void setHoldingValue(long holdingValue){
				this.HoldingValue = holdingValue;
			}

			public long getHoldingValue(){
				return HoldingValue;
			}

			public void setAmountInvested(int amountInvested){
				this.AmountInvested = amountInvested;
			}

			public int getAmountInvested(){
				return AmountInvested;
			}

			public void setCurrentValue(int currentValue){
				this.CurrentValue = currentValue;
			}

			public int getCurrentValue(){
				return CurrentValue;
			}

			public void setXirr(double xirr){
				this.xirr = xirr;
			}

			public double getXirr(){
				return xirr;
			}

			public void setAbreturn(double abreturn){
				this.Abreturn = abreturn;
			}

			public double getAbreturn(){
				return Abreturn;
			}

			public void setGain(int gain){
				this.gain = gain;
			}

			public int getGain(){
				return gain;
			}

			public void setTotal_days(double total_days){
				this.total_days = total_days;
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
				dest.writeInt(CurrentValue);
				dest.writeDouble(AmountInvested);
				dest.writeDouble(HoldingValue);
				dest.writeDouble(gain);
				dest.writeDouble(total_days);
				dest.writeDouble(Abreturn);
				dest.writeDouble(xirr);
			}
		}

	}

	public static class PerformanceDetailsItem implements Parcelable, Serializable{
		private String Status = "";
		private int TranTimestamp = 0;
		private String Nav = "";
		private long HoldingValue = 0;
		private String TranDate = "";
		private String sub_category = "";
		private String Amount = "";
		private String Holder = "";
		private String FolioNo = "";
		private String Units = "";
		private String SchemeName = "";
		private String category = "";
		private int holdingDays = 0;

		protected PerformanceDetailsItem(Parcel in) {
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

		public void setStatus(String status){
			this.Status = status;
		}

		public String getStatus(){
			return Status;
		}

		public void setTranTimestamp(int tranTimestamp){
			this.TranTimestamp = tranTimestamp;
		}

		public int getTranTimestamp(){
			return TranTimestamp;
		}

		public void setNav(String nav){
			this.Nav = nav;
		}

		public String getNav(){
			return Nav;
		}

		public void setHoldingValue(long holdingValue){
			this.HoldingValue = holdingValue;
		}

		public long getHoldingValue(){
			return HoldingValue;
		}

		public void setTranDate(String tranDate){
			this.TranDate = tranDate;
		}

		public String getTranDate(){
			return TranDate;
		}

		public void setSub_category(String sub_category){
			this.sub_category = sub_category;
		}

		public String getSub_category(){
			return sub_category;
		}

		public void setAmount(String amount){
			this.Amount = amount;
		}

		public String getAmount(){
			return Amount;
		}

		public void setHolder(String holder){
			this.Holder = holder;
		}

		public String getHolder(){
			return Holder;
		}

		public void setFolioNo(String folioNo){
			this.FolioNo = folioNo;
		}

		public String getFolioNo(){
			return FolioNo;
		}

		public void setUnits(String units){
			this.Units = units;
		}

		public String getUnits(){
			return Units;
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

		public void setHoldingDays(int holdingDays){
			this.holdingDays = holdingDays;
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
			dest.writeDouble(HoldingValue);
		}
	}

}