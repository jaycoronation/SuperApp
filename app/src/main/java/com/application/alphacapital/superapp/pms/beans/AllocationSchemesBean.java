package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 02-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class AllocationSchemesBean implements Parcelable, Serializable
{

    /**
     * FundName : Aditya Birla Sun Life Mutual Fund
     * SchemeName : Aditya Birla SL - Equity Hybrid 95 Fund Reg (G)
     * CurrentValue : 95989.35
     * category : Hybrid: Aggressive
     * allocation : 2.1916033943164
     */

    private String FundName = "";
    private String SchemeName = "";
    private String CurrentValue = "";
    private String category = "";
    private double allocation = 0;
    private String totalCurrentValue = "" ;
    private String totalAllocation = "" ;
    private boolean isTotal = false ;

    public AllocationSchemesBean()
    {

    }

    protected AllocationSchemesBean(Parcel in)
    {
        FundName = in.readString();
        SchemeName = in.readString();
        CurrentValue = in.readString();
        category = in.readString();
        allocation = in.readDouble();
        totalCurrentValue = in.readString();
        totalAllocation = in.readString();
        isTotal = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(FundName);
        dest.writeString(SchemeName);
        dest.writeString(CurrentValue);
        dest.writeString(category);
        dest.writeDouble(allocation);
        dest.writeString(totalCurrentValue);
        dest.writeString(totalAllocation);
        dest.writeByte((byte) (isTotal ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<AllocationSchemesBean> CREATOR = new Creator<AllocationSchemesBean>()
    {
        @Override
        public AllocationSchemesBean createFromParcel(Parcel in)
        {
            return new AllocationSchemesBean(in);
        }

        @Override
        public AllocationSchemesBean[] newArray(int size)
        {
            return new AllocationSchemesBean[size];
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

    public String getSchemeName()
    {
        return SchemeName;
    }

    public void setSchemeName(String SchemeName)
    {
        this.SchemeName = SchemeName;
    }

    public String getCurrentValue()
    {
        return CurrentValue;
    }

    public void setCurrentValue(String CurrentValue)
    {
        this.CurrentValue = CurrentValue;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public double getAllocation()
    {
        return allocation;
    }

    public void setAllocation(double allocation)
    {
        this.allocation = allocation;
    }

    public String getTotalCurrentValue()
    {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(String totalCurrentValue)
    {
        this.totalCurrentValue = totalCurrentValue;
    }

    public String getTotalAllocation()
    {
        return totalAllocation;
    }

    public void setTotalAllocation(String totalAllocation)
    {
        this.totalAllocation = totalAllocation;
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
