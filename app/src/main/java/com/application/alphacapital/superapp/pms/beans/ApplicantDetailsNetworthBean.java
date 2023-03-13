package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 19/06/2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class ApplicantDetailsNetworthBean implements Parcelable,Serializable
{
    /**
     * applicant_name : Abhimanyu Bansal
     * apcnt_assets_details : [{"Objective":"Debt: Low Duration","Amount":537972,"HoldingPercentage":0.55},{"Objective":"Debt: Money Market","Amount":401411,"HoldingPercentage":0.41},{"Objective":"Debt: Ultra Short Duration","Amount":406009,"HoldingPercentage":0.41},{"Objective":"Equity: ELSS","Amount":357445,"HoldingPercentage":0.36},{"Objective":"Equity: Mid Cap","Amount":45014,"HoldingPercentage":0.05},{"Objective":"Equity: Multi Cap","Amount":225100,"HoldingPercentage":0.23},{"Objective":"Equity: Small Cap","Amount":527714,"HoldingPercentage":0.54},{"Objective":"Others: FoFs Overseas","Amount":471975,"HoldingPercentage":0.48}]
     * apcnt_assets_total : {"TotalAmount":"2972640","TotalHoldingPercentage":3.03}
     */

    private String applicant_name;
    private ApcntAssetsTotalGetSet apcnt_assets_total;
    private ArrayList<ApcntAssetsDetailsGetSet> apcnt_assets_details;

    public ApplicantDetailsNetworthBean()
    {

    }

    protected ApplicantDetailsNetworthBean(Parcel in)
    {
        applicant_name = in.readString();
        apcnt_assets_total = in.readParcelable(ApcntAssetsTotalGetSet.class.getClassLoader());
        apcnt_assets_details = in.createTypedArrayList(ApcntAssetsDetailsGetSet.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(applicant_name);
        dest.writeParcelable(apcnt_assets_total, flags);
        dest.writeTypedList(apcnt_assets_details);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<ApplicantDetailsNetworthBean> CREATOR = new Creator<ApplicantDetailsNetworthBean>()
    {
        @Override
        public ApplicantDetailsNetworthBean createFromParcel(Parcel in)
        {
            return new ApplicantDetailsNetworthBean(in);
        }

        @Override
        public ApplicantDetailsNetworthBean[] newArray(int size)
        {
            return new ApplicantDetailsNetworthBean[size];
        }
    };

    public String getApplicant_name()
    {
        return applicant_name;
    }

    public void setApplicant_name(String applicant_name)
    {
        this.applicant_name = applicant_name;
    }

    public ApcntAssetsTotalGetSet getApcnt_assets_total()
    {
        return apcnt_assets_total;
    }

    public void setApcnt_assets_total(ApcntAssetsTotalGetSet apcnt_assets_total)
    {
        this.apcnt_assets_total = apcnt_assets_total;
    }

    public ArrayList<ApcntAssetsDetailsGetSet> getApcnt_assets_details()
    {
        return apcnt_assets_details;
    }

    public void setApcnt_assets_details(ArrayList<ApcntAssetsDetailsGetSet> apcnt_assets_details)
    {
        this.apcnt_assets_details = apcnt_assets_details;
    }

    public static class ApcntAssetsTotalGetSet implements Parcelable, Serializable
    {
        /**
         * TotalAmount : 2972640
         * TotalHoldingPercentage : 3.03
         */

        private String TotalAmount;
        private double TotalHoldingPercentage;

        public ApcntAssetsTotalGetSet()
        {

        }

        protected ApcntAssetsTotalGetSet(Parcel in)
        {
            TotalAmount = in.readString();
            TotalHoldingPercentage = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(TotalAmount);
            dest.writeDouble(TotalHoldingPercentage);
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        public static final Creator<ApcntAssetsTotalGetSet> CREATOR = new Creator<ApcntAssetsTotalGetSet>()
        {
            @Override
            public ApcntAssetsTotalGetSet createFromParcel(Parcel in)
            {
                return new ApcntAssetsTotalGetSet(in);
            }

            @Override
            public ApcntAssetsTotalGetSet[] newArray(int size)
            {
                return new ApcntAssetsTotalGetSet[size];
            }
        };

        public String getTotalAmount()
        {
            return TotalAmount;
        }

        public void setTotalAmount(String TotalAmount)
        {
            this.TotalAmount = TotalAmount;
        }

        public double getTotalHoldingPercentage()
        {
            return TotalHoldingPercentage;
        }

        public void setTotalHoldingPercentage(double TotalHoldingPercentage)
        {
            this.TotalHoldingPercentage = TotalHoldingPercentage;
        }
    }

    public static class ApcntAssetsDetailsGetSet implements Parcelable, Serializable
    {
        /**
         * Objective : Debt: Low Duration
         * Amount : 537972
         * HoldingPercentage : 0.55
         */

        private String Objective;
        private int Amount;
        private double HoldingPercentage;

        public ApcntAssetsDetailsGetSet()
        {

        }

        protected ApcntAssetsDetailsGetSet(Parcel in)
        {
            Objective = in.readString();
            Amount = in.readInt();
            HoldingPercentage = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(Objective);
            dest.writeInt(Amount);
            dest.writeDouble(HoldingPercentage);
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        public static final Creator<ApcntAssetsDetailsGetSet> CREATOR = new Creator<ApcntAssetsDetailsGetSet>()
        {
            @Override
            public ApcntAssetsDetailsGetSet createFromParcel(Parcel in)
            {
                return new ApcntAssetsDetailsGetSet(in);
            }

            @Override
            public ApcntAssetsDetailsGetSet[] newArray(int size)
            {
                return new ApcntAssetsDetailsGetSet[size];
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

        public int getAmount()
        {
            return Amount;
        }

        public void setAmount(int Amount)
        {
            this.Amount = Amount;
        }

        public double getHoldingPercentage()
        {
            return HoldingPercentage;
        }

        public void setHoldingPercentage(double HoldingPercentage)
        {
            this.HoldingPercentage = HoldingPercentage;
        }
    }
}
