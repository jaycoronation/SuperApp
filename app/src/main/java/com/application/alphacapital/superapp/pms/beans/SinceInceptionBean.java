package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 10-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class SinceInceptionBean implements Parcelable, Serializable
{

    /**
     * AmountInvested : 1774904.86
     * HoldingValue : 1.7460979542E9
     * asset_type : Debt
     * CurrentValue : 1059696
     * total_days : 983.76988736174
     * gain : -715208.86
     * Abreturn : -40.295616746466
     * xirr : -14.950549210145
     */

    private double AmountInvested = 0;
    private double HoldingValue = 0;
    private String asset_type = "";
    private int CurrentValue = -1;
    private double total_days = 0;
    private double gain = 0;
    private double Abreturn = 0;
    private double xirr = 0;

    public SinceInceptionBean()
    {

    }

    protected SinceInceptionBean(Parcel in)
    {
        AmountInvested = in.readDouble();
        HoldingValue = in.readDouble();
        asset_type = in.readString();
        CurrentValue = in.readInt();
        total_days = in.readDouble();
        gain = in.readDouble();
        Abreturn = in.readDouble();
        xirr = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeDouble(AmountInvested);
        dest.writeDouble(HoldingValue);
        dest.writeString(asset_type);
        dest.writeInt(CurrentValue);
        dest.writeDouble(total_days);
        dest.writeDouble(gain);
        dest.writeDouble(Abreturn);
        dest.writeDouble(xirr);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<SinceInceptionBean> CREATOR = new Creator<SinceInceptionBean>()
    {
        @Override
        public SinceInceptionBean createFromParcel(Parcel in)
        {
            return new SinceInceptionBean(in);
        }

        @Override
        public SinceInceptionBean[] newArray(int size)
        {
            return new SinceInceptionBean[size];
        }
    };

    public double getAmountInvested()
    {
        return AmountInvested;
    }

    public void setAmountInvested(double AmountInvested)
    {
        this.AmountInvested = AmountInvested;
    }

    public double getHoldingValue()
    {
        return HoldingValue;
    }

    public void setHoldingValue(double HoldingValue)
    {
        this.HoldingValue = HoldingValue;
    }

    public String getAsset_type()
    {
        return asset_type;
    }

    public void setAsset_type(String asset_type)
    {
        this.asset_type = asset_type;
    }

    public int getCurrentValue()
    {
        return CurrentValue;
    }

    public void setCurrentValue(int CurrentValue)
    {
        this.CurrentValue = CurrentValue;
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

    public double getAbreturn()
    {
        return Abreturn;
    }

    public void setAbreturn(double Abreturn)
    {
        this.Abreturn = Abreturn;
    }

    public double getXirr()
    {
        return xirr;
    }

    public void setXirr(double xirr)
    {
        this.xirr = xirr;
    }
}
