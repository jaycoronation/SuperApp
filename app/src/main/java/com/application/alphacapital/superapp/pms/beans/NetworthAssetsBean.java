package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 01-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class NetworthAssetsBean implements Parcelable, Serializable
{

    /**
     * Objective : Debt: Low Duration
     * Amount : 1090272.44
     * HoldingPercentage : 24.92
     */

    private String Objective = "";
    private String Amount = "";
    private String HoldingPercentage = "";
    private String AssetsTitle = "" ;
    private String TotalAmount = "" ;
    private String TotalAllocation = "" ;
    private boolean isTotal = false ;

    public NetworthAssetsBean()
    {

    }

    protected NetworthAssetsBean(Parcel in)
    {
        Objective = in.readString();
        Amount = in.readString();
        HoldingPercentage = in.readString();
        AssetsTitle = in.readString();
        TotalAmount = in.readString();
        TotalAllocation = in.readString();
        isTotal = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Objective);
        dest.writeString(Amount);
        dest.writeString(HoldingPercentage);
        dest.writeString(AssetsTitle);
        dest.writeString(TotalAmount);
        dest.writeString(TotalAllocation);
        dest.writeByte((byte) (isTotal ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<NetworthAssetsBean> CREATOR = new Creator<NetworthAssetsBean>()
    {
        @Override
        public NetworthAssetsBean createFromParcel(Parcel in)
        {
            return new NetworthAssetsBean(in);
        }

        @Override
        public NetworthAssetsBean[] newArray(int size)
        {
            return new NetworthAssetsBean[size];
        }
    };

    public String getObjective()
    {
        return Objective;
    }

    public void setObjective(String Objective)
    {
        this.Objective = Objective;
    }

    public String getAmount()
    {
        return Amount;
    }

    public void setAmount(String Amount)
    {
        this.Amount = Amount;
    }

    public String getHoldingPercentage()
    {
        return HoldingPercentage;
    }

    public void setHoldingPercentage(String HoldingPercentage)
    {
        this.HoldingPercentage = HoldingPercentage;
    }

    public String getAssetsTitle()
    {
        return AssetsTitle;
    }

    public void setAssetsTitle(String assetsTitle)
    {
        AssetsTitle = assetsTitle;
    }

    public String getTotalAmount()
    {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount)
    {
        TotalAmount = totalAmount;
    }

    public String getTotalAllocation()
    {
        return TotalAllocation;
    }

    public void setTotalAllocation(String totalAllocation)
    {
        TotalAllocation = totalAllocation;
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
