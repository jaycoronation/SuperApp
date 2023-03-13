package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 02-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class MicroTacticalBean implements Parcelable, Serializable
{

    /**
     * asset_class : Debt
     * actual_percentage : 48
     * policy : 50
     * variance : 2
     */

    private String asset_class = "";
    private String actual_percentage = "";
    private String policy = "";
    private int variance = -1;
    private String totalPercentage = "" ;
    private String totalPolicyPercentage = "" ;
    private String toalVariance = "" ;
    private boolean isTotal = false ;

    public MicroTacticalBean()
    {

    }

    protected MicroTacticalBean(Parcel in)
    {
        asset_class = in.readString();
        actual_percentage = in.readString();
        policy = in.readString();
        variance = in.readInt();
        totalPercentage = in.readString();
        totalPolicyPercentage = in.readString();
        toalVariance = in.readString();
        isTotal = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(asset_class);
        dest.writeString(actual_percentage);
        dest.writeString(policy);
        dest.writeInt(variance);
        dest.writeString(totalPercentage);
        dest.writeString(totalPolicyPercentage);
        dest.writeString(toalVariance);
        dest.writeByte((byte) (isTotal ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<MicroTacticalBean> CREATOR = new Creator<MicroTacticalBean>()
    {
        @Override
        public MicroTacticalBean createFromParcel(Parcel in)
        {
            return new MicroTacticalBean(in);
        }

        @Override
        public MicroTacticalBean[] newArray(int size)
        {
            return new MicroTacticalBean[size];
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

    public String getActual_percentage()
    {
        return actual_percentage;
    }

    public void setActual_percentage(String actual_percentage)
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
}
