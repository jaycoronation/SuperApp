package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 10-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class PerformanceOverallBean implements Parcelable, Serializable
{

    /**
     * PresentAmount : 0
     * sub_category : Debt: Corporate Bond
     * category : Debt
     * SchemeName : Aditya Birla SL - Corporate Bond Fund (G)
     * TranDate : 06 Apr 2018
     * FolioNo : 1015875385
     * Amount : -548160.49
     * Holder : Alpha Capital
     * Status : Sell
     * Units : -8220.566
     * Nav : 66.6816
     * TranTimestamp : 1522953000
     * holdingDays : 368
     * HoldingValue : -2.0172306032E8
     */

    private String PresentAmount = "";
    private String sub_category = "";
    private String category = "";
    private String SchemeName = "";
    private String TranDate = "";
    private String FolioNo = "";
    private String Amount = "";
    private String Holder = "";
    private String Status = "";
    private String Units = "";
    private String Nav = "";
    private int TranTimestamp = -1;
    private int holdingDays = -1;
    private double HoldingValue = 0;

    public PerformanceOverallBean()
    {

    }

    protected PerformanceOverallBean(Parcel in)
    {
        PresentAmount = in.readString();
        sub_category = in.readString();
        category = in.readString();
        SchemeName = in.readString();
        TranDate = in.readString();
        FolioNo = in.readString();
        Amount = in.readString();
        Holder = in.readString();
        Status = in.readString();
        Units = in.readString();
        Nav = in.readString();
        TranTimestamp = in.readInt();
        holdingDays = in.readInt();
        HoldingValue = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(PresentAmount);
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

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<PerformanceOverallBean> CREATOR = new Creator<PerformanceOverallBean>()
    {
        @Override
        public PerformanceOverallBean createFromParcel(Parcel in)
        {
            return new PerformanceOverallBean(in);
        }

        @Override
        public PerformanceOverallBean[] newArray(int size)
        {
            return new PerformanceOverallBean[size];
        }
    };

    public String getPresentAmount()
    {
        return PresentAmount;
    }

    public void setPresentAmount(String PresentAmount)
    {
        this.PresentAmount = PresentAmount;
    }

    public String getSub_category()
    {
        return sub_category;
    }

    public void setSub_category(String sub_category)
    {
        this.sub_category = sub_category;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getSchemeName()
    {
        return SchemeName;
    }

    public void setSchemeName(String SchemeName)
    {
        this.SchemeName = SchemeName;
    }

    public String getTranDate()
    {
        return TranDate;
    }

    public void setTranDate(String TranDate)
    {
        this.TranDate = TranDate;
    }

    public String getFolioNo()
    {
        return FolioNo;
    }

    public void setFolioNo(String FolioNo)
    {
        this.FolioNo = FolioNo;
    }

    public String getAmount()
    {
        return Amount;
    }

    public void setAmount(String Amount)
    {
        this.Amount = Amount;
    }

    public String getHolder()
    {
        return Holder;
    }

    public void setHolder(String Holder)
    {
        this.Holder = Holder;
    }

    public String getStatus()
    {
        return Status;
    }

    public void setStatus(String Status)
    {
        this.Status = Status;
    }

    public String getUnits()
    {
        return Units;
    }

    public void setUnits(String Units)
    {
        this.Units = Units;
    }

    public String getNav()
    {
        return Nav;
    }

    public void setNav(String Nav)
    {
        this.Nav = Nav;
    }

    public int getTranTimestamp()
    {
        return TranTimestamp;
    }

    public void setTranTimestamp(int TranTimestamp)
    {
        this.TranTimestamp = TranTimestamp;
    }

    public int getHoldingDays()
    {
        return holdingDays;
    }

    public void setHoldingDays(int holdingDays)
    {
        this.holdingDays = holdingDays;
    }

    public double getHoldingValue()
    {
        return HoldingValue;
    }

    public void setHoldingValue(double HoldingValue)
    {
        this.HoldingValue = HoldingValue;
    }
}
