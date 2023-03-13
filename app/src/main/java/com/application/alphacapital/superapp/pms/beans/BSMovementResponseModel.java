package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class BSMovementResponseModel implements Parcelable,Serializable{
	private int equityFlag = 0;
	private int success = 0;
	private int goldFlag = 0;
	private int debtFdFlag = 0;
	private int debtFlag = 0;
	private int othersFlag = 0;
	private int realEstateFlag = 0;
	private ArrayList<GraphDataItem> graph_data = new ArrayList<GraphDataItem>();
	private ArrayList<SheetDataItem> sheet_data = new ArrayList<SheetDataItem>();
	private int solutionFlag = 0;
	private int hybridFlag = 0;

	protected BSMovementResponseModel(Parcel in)
	{
		equityFlag = in.readInt();
		success = in.readInt();
		goldFlag = in.readInt();
		debtFdFlag = in.readInt();
		debtFlag = in.readInt();
		othersFlag = in.readInt();
		realEstateFlag = in.readInt();
		graph_data = in.createTypedArrayList(GraphDataItem.CREATOR);
		sheet_data = in.createTypedArrayList(SheetDataItem.CREATOR);
		solutionFlag = in.readInt();
		hybridFlag = in.readInt();
	}

	@Override public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(equityFlag);
		dest.writeInt(success);
		dest.writeInt(goldFlag);
		dest.writeInt(debtFdFlag);
		dest.writeInt(debtFlag);
		dest.writeInt(othersFlag);
		dest.writeInt(realEstateFlag);
		dest.writeTypedList(graph_data);
		dest.writeTypedList(sheet_data);
		dest.writeInt(solutionFlag);
		dest.writeInt(hybridFlag);
	}

	@Override public int describeContents()
	{
		return 0;
	}

	public static final Creator<BSMovementResponseModel> CREATOR = new Creator<BSMovementResponseModel>()
	{
		@Override public BSMovementResponseModel createFromParcel(Parcel in)
		{
			return new BSMovementResponseModel(in);
		}

		@Override public BSMovementResponseModel[] newArray(int size)
		{
			return new BSMovementResponseModel[size];
		}
	};

	public void setEquityFlag(int equityFlag){
		this.equityFlag = equityFlag;
	}

	public int getEquityFlag(){
		return equityFlag;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setGoldFlag(int goldFlag){
		this.goldFlag = goldFlag;
	}

	public int getGoldFlag(){
		return goldFlag;
	}

	public void setDebtFdFlag(int debtFdFlag){
		this.debtFdFlag = debtFdFlag;
	}

	public int getDebtFdFlag(){
		return debtFdFlag;
	}

	public void setDebtFlag(int debtFlag){
		this.debtFlag = debtFlag;
	}

	public int getDebtFlag(){
		return debtFlag;
	}

	public void setOthersFlag(int othersFlag){
		this.othersFlag = othersFlag;
	}

	public int getOthersFlag(){
		return othersFlag;
	}

	public void setRealEstateFlag(int realEstateFlag){
		this.realEstateFlag = realEstateFlag;
	}

	public int getRealEstateFlag(){
		return realEstateFlag;
	}

	public void setGraph_data(ArrayList<GraphDataItem> graph_data){
		this.graph_data = graph_data;
	}

	public ArrayList<GraphDataItem> getGraph_data(){
		return graph_data;
	}

	public void setSheet_data(ArrayList<SheetDataItem> sheet_data){
		this.sheet_data = sheet_data;
	}

	public ArrayList<SheetDataItem> getSheet_data(){
		return sheet_data;
	}

	public void setSolutionFlag(int solutionFlag){
		this.solutionFlag = solutionFlag;
	}

	public int getSolutionFlag(){
		return solutionFlag;
	}

	public void setHybridFlag(int hybridFlag){
		this.hybridFlag = hybridFlag;
	}

	public int getHybridFlag(){
		return hybridFlag;
	}


	public static class SheetDataItem implements Parcelable,Serializable{
		private int in_out_flow = 0;
		private int total = 0;
		private String Debt = "";
		private String Hybrid = "";
		private String Equity = "";
		private int movement = 0;
		private String timestamp = "";
		private int profite = 0;

		protected SheetDataItem(Parcel in)
		{
			in_out_flow = in.readInt();
			total = in.readInt();
			Debt = in.readString();
			Hybrid = in.readString();
			Equity = in.readString();
			movement = in.readInt();
			timestamp = in.readString();
			profite = in.readInt();
		}

		public static final Creator<SheetDataItem> CREATOR = new Creator<SheetDataItem>()
		{
			@Override public SheetDataItem createFromParcel(Parcel in)
			{
				return new SheetDataItem(in);
			}

			@Override public SheetDataItem[] newArray(int size)
			{
				return new SheetDataItem[size];
			}
		};

		public void setIn_out_flow(int in_out_flow){
			this.in_out_flow = in_out_flow;
		}

		public int getIn_out_flow(){
			return in_out_flow;
		}

		public void setTotal(int total){
			this.total = total;
		}

		public int getTotal(){
			return total;
		}

		public void setDebt(String debt){
			this.Debt = debt;
		}

		public String getDebt(){
			return Debt;
		}

		public void setHybrid(String hybrid){
			this.Hybrid = hybrid;
		}

		public String getHybrid(){
			return Hybrid;
		}

		public void setEquity(String equity){
			this.Equity = equity;
		}

		public String getEquity(){
			return Equity;
		}

		public void setMovement(int movement){
			this.movement = movement;
		}

		public int getMovement(){
			return movement;
		}

		public void setTimestamp(String timestamp){
			this.timestamp = timestamp;
		}

		public String getTimestamp(){
			return timestamp;
		}

		public void setProfite(int profite){
			this.profite = profite;
		}

		public int getProfite(){
			return profite;
		}

		@Override public int describeContents()
		{
			return 0;
		}

		@Override public void writeToParcel(Parcel parcel, int i)
		{
			parcel.writeInt(in_out_flow);
			parcel.writeInt(total);
			parcel.writeString(Debt);
			parcel.writeString(Hybrid);
			parcel.writeString(Equity);
			parcel.writeInt(movement);
			parcel.writeString(timestamp);
			parcel.writeInt(profite);
		}
	}


	public static class GraphDataItem  implements Parcelable, Serializable{
		private int total = 0;
		private String Debt = "";
		private String Hybrid = "";
		private String Equity = "";
		private String timestamp = "";

		protected GraphDataItem(Parcel in) {
			total = in.readInt();
			Debt = in.readString();
			Hybrid = in.readString();
			Equity = in.readString();
			timestamp = in.readString();
		}

		public static final Creator<GraphDataItem> CREATOR = new Creator<GraphDataItem>() {
			@Override
			public GraphDataItem createFromParcel(Parcel in) {
				return new GraphDataItem(in);
			}

			@Override
			public GraphDataItem[] newArray(int size) {
				return new GraphDataItem[size];
			}
		};

		public void setTotal(int total){
			this.total = total;
		}

		public int getTotal(){
			return total;
		}

		public void setDebt(String debt){
			this.Debt = debt;
		}

		public String getDebt(){
			return Debt;
		}

		public void setHybrid(String hybrid){
			this.Hybrid = hybrid;
		}

		public String getHybrid(){
			return Hybrid;
		}

		public void setEquity(String equity){
			this.Equity = equity;
		}

		public String getEquity(){
			return Equity;
		}

		public void setTimestamp(String timestamp){
			this.timestamp = timestamp;
		}

		public String getTimestamp(){
			return timestamp;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(Hybrid);
			dest.writeString(Equity);
			dest.writeString(Debt);
			dest.writeString(timestamp);
			dest.writeInt(total);
		}
	}

}