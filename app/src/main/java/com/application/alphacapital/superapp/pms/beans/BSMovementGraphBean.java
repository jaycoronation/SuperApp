package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 05-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class BSMovementGraphBean implements Parcelable, Serializable
{

    /**
     * Hybrid : 472840.57
     * Equity : 1760868.91
     * Debt : 2121069.13
     * timestamp : 29.03.2019
     * total : 4354778.61
     */

    private String Hybrid = "";
    private String Equity = "";
    private String Debt = "";
    private String timestamp = "";
    private double total = 0;

    public BSMovementGraphBean()
    {

    }

    public BSMovementGraphBean(Parcel in)
    {
        Hybrid = in.readString();
        Equity = in.readString();
        Debt = in.readString();
        timestamp = in.readString();
        total = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Hybrid);
        dest.writeString(Equity);
        dest.writeString(Debt);
        dest.writeString(timestamp);
        dest.writeDouble(total);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<BSMovementGraphBean> CREATOR = new Creator<BSMovementGraphBean>()
    {
        @Override
        public BSMovementGraphBean createFromParcel(Parcel in)
        {
            return new BSMovementGraphBean(in);
        }

        @Override
        public BSMovementGraphBean[] newArray(int size)
        {
            return new BSMovementGraphBean[size];
        }
    };

    public String getHybrid()
    {
        return Hybrid;
    }

    public void setHybrid(String Hybrid)
    {
        this.Hybrid = Hybrid;
    }

    public String getEquity()
    {
        return Equity;
    }

    public void setEquity(String Equity)
    {
        this.Equity = Equity;
    }

    public String getDebt()
    {
        return Debt;
    }

    public void setDebt(String Debt)
    {
        this.Debt = Debt;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }
}
