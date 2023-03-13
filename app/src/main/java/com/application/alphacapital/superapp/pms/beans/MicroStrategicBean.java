package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 02-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class MicroStrategicBean implements Parcelable, Serializable
{

    /**
     * asset_class : Debt
     * actual_amount : 2124010.13
     * actual_percentage : 48.49
     * policy : 50
     * variance : 2
     */

    private String asset_class = "";
    private double actual_amount = 0;
    private double actual_percentage = 0;
    private String policy = "";
    private int variance = -1;
    private String totalAmount = "" ;
    private String totalAmountPercentage = "" ;
    private String totalPolicyPercentage = "" ;
    private String totalVariance = "" ;
    private boolean isTotal = false ;

    public MicroStrategicBean()
    {

    }

    protected MicroStrategicBean(Parcel in)
    {
        asset_class = in.readString();
        actual_amount = in.readDouble();
        actual_percentage = in.readDouble();
        policy = in.readString();
        variance = in.readInt();
        totalAmount = in.readString();
        totalAmountPercentage = in.readString();
        totalPolicyPercentage = in.readString();
        totalVariance = in.readString();
        isTotal = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(asset_class);
        dest.writeDouble(actual_amount);
        dest.writeDouble(actual_percentage);
        dest.writeString(policy);
        dest.writeInt(variance);
        dest.writeString(totalAmount);
        dest.writeString(totalAmountPercentage);
        dest.writeString(totalPolicyPercentage);
        dest.writeString(totalVariance);
        dest.writeByte((byte) (isTotal ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<MicroStrategicBean> CREATOR = new Creator<MicroStrategicBean>()
    {
        @Override
        public MicroStrategicBean createFromParcel(Parcel in)
        {
            return new MicroStrategicBean(in);
        }

        @Override
        public MicroStrategicBean[] newArray(int size)
        {
            return new MicroStrategicBean[size];
        }
    };

    public String getAsset_class()
    {
        return asset_class;
    }

    public void setAsset_class(String asset_class)
    {
        this.asset_class = asset_class;
    }

    public double getActual_amount()
    {
        return actual_amount;
    }

    public void setActual_amount(double actual_amount)
    {
        this.actual_amount = actual_amount;
    }

    public double getActual_percentage()
    {
        return actual_percentage;
    }

    public void setActual_percentage(double actual_percentage)
    {
        this.actual_percentage = actual_percentage;
    }

    public String getPolicy()
    {
        return policy;
    }

    public void setPolicy(String policy)
    {
        this.policy = policy;
    }

    public int getVariance()
    {
        return variance;
    }

    public void setVariance(int variance)
    {
        this.variance = variance;
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
}
