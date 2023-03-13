package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 02-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class AllocationFundBean implements Parcelable, Serializable
{

    /**
     * FundName : Aditya Birla Sun Life Mutual Fund
     * CurrentValue : 95989.35
     */

    private String FundName = "";
    private double CurrentValue = 0;
    private String totalCurrentValue = "" ;
    private boolean isTotal = false ;

    public AllocationFundBean()
    {

    }

    protected AllocationFundBean(Parcel in)
    {
        FundName = in.readString();
        CurrentValue = in.readDouble();
        totalCurrentValue = in.readString();
        isTotal = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(FundName);
        dest.writeDouble(CurrentValue);
        dest.writeString(totalCurrentValue);
        dest.writeByte((byte) (isTotal ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<AllocationFundBean> CREATOR = new Creator<AllocationFundBean>()
    {
        @Override
        public AllocationFundBean createFromParcel(Parcel in)
        {
            return new AllocationFundBean(in);
        }

        @Override
        public AllocationFundBean[] newArray(int size)
        {
            return new AllocationFundBean[size];
        }
    };

    public String getFundName()
    {
        return FundName;
    }

    public void setFundName(String FundName)
    {
        this.FundName = FundName;
    }

    public double getCurrentValue()
    {
        return CurrentValue;
    }

    public void setCurrentValue(double CurrentValue)
    {
        this.CurrentValue = CurrentValue;
    }

    public String getTotalCurrentValue()
    {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(String totalCurrentValue)
    {
        this.totalCurrentValue = totalCurrentValue;
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
