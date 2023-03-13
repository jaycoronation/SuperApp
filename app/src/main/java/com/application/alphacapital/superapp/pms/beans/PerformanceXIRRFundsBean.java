package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 12-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class PerformanceXIRRFundsBean implements Parcelable,Serializable
{
    /**
     * portfolio_key : Debt
     * portfolio_value : [{"PresentAmount":"0","sub_category":"Debt: Corporate Bond","category":"Debt","SchemeName":"Aditya Birla SL - Corporate Bond Fund (G)","TranDate":"06 Apr 2018","FolioNo":"1015875385","Amount":"-548160.49","Holder":"Alpha Capital","Status":"Sell","Units":"-8220.566","Nav":"66.6816","TranTimestamp":1522953000,"holdingDays":368,"HoldingValue":-2.0172306032E8}]
     * portfolio_total : {"CurrentValue":1059696,"AmountInvested":1774904.86,"HoldingValue":1.7460979542E9,"gain":-715208.86,"total_days":983.76988736174,"Abreturn":-40.295616746466,"xirr":-14.950549210145}
     */

    private String portfolio_key = "";
    private String date = "" ;
    private String openingBalance = "" ;
    private PortfolioTotalGetSet portfolio_total;
    private ArrayList<PortfolioValueGetSet> portfolio_value = new ArrayList<>();

    public PerformanceXIRRFundsBean()
    {

    }

    protected PerformanceXIRRFundsBean(Parcel in)
    {
        portfolio_key = in.readString();
        date = in.readString();
        openingBalance = in.readString();
        portfolio_total = in.readParcelable(PortfolioTotalGetSet.class.getClassLoader());
        portfolio_value = in.createTypedArrayList(PortfolioValueGetSet.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(portfolio_key);
        dest.writeString(date);
        dest.writeString(openingBalance);
        dest.writeParcelable(portfolio_total, flags);
        dest.writeTypedList(portfolio_value);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<PerformanceXIRRFundsBean> CREATOR = new Creator<PerformanceXIRRFundsBean>()
    {
        @Override
        public PerformanceXIRRFundsBean createFromParcel(Parcel in)
        {
            return new PerformanceXIRRFundsBean(in);
        }

        @Override
        public PerformanceXIRRFundsBean[] newArray(int size)
        {
            return new PerformanceXIRRFundsBean[size];
        }
    };

    public String getPortfolio_key()
    {
        return portfolio_key;
    }

    public void setPortfolio_key(String portfolio_key)
    {
        this.portfolio_key = portfolio_key;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getOpeningBalance()
    {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance)
    {
        this.openingBalance = openingBalance;
    }

    public PortfolioTotalGetSet getPortfolio_total()
    {
        return portfolio_total;
    }

    public void setPortfolio_total(PortfolioTotalGetSet portfolio_total)
    {
        this.portfolio_total = portfolio_total;
    }

    public ArrayList<PortfolioValueGetSet> getPortfolio_value()
    {
        return portfolio_value;
    }

    public void setPortfolio_value(ArrayList<PortfolioValueGetSet> portfolio_value)
    {
        this.portfolio_value = portfolio_value;
    }

    public static class PortfolioTotalGetSet implements Parcelable,Serializable
    {
        /**
         * CurrentValue : 1059696
         * AmountInvested : 1774904.86
         * HoldingValue : 1.7460979542E9
         * gain : -715208.86
         * total_days : 983.76988736174
         * Abreturn : -40.295616746466
         * xirr : -14.950549210145
         */

        private int CurrentValue = -1;
        private double AmountInvested = 0;
        private double HoldingValue = 0;
        private double gain = 0;
        private double total_days = 0;
        private double Abreturn = 0;
        private double xirr = 0;

        public PortfolioTotalGetSet()
        {

        }

        protected PortfolioTotalGetSet(Parcel in)
        {
            CurrentValue = in.readInt();
            AmountInvested = in.readDouble();
            HoldingValue = in.readDouble();
            gain = in.readDouble();
            total_days = in.readDouble();
            Abreturn = in.readDouble();
            xirr = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(CurrentValue);
            dest.writeDouble(AmountInvested);
            dest.writeDouble(HoldingValue);
            dest.writeDouble(gain);
            dest.writeDouble(total_days);
            dest.writeDouble(Abreturn);
            dest.writeDouble(xirr);
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        public static final Creator<PortfolioTotalGetSet> CREATOR = new Creator<PortfolioTotalGetSet>()
        {
            @Override
            public PortfolioTotalGetSet createFromParcel(Parcel in)
            {
                return new PortfolioTotalGetSet(in);
            }

            @Override
            public PortfolioTotalGetSet[] newArray(int size)
            {
                return new PortfolioTotalGetSet[size];
            }
        };

        public int getCurrentValue()
        {
            return CurrentValue;
        }

        public void setCurrentValue(int CurrentValue)
        {
            this.CurrentValue = CurrentValue;
        }

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

        public double getGain()
        {
            return gain;
        }

        public void setGain(double gain)
        {
            this.gain = gain;
        }

        public double getTotal_days()
        {
            return total_days;
        }

        public void setTotal_days(double total_days)
        {
            this.total_days = total_days;
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

    public static class PortfolioValueGetSet implements Parcelable, Serializable
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

        public PortfolioValueGetSet()
        {

        }

        protected PortfolioValueGetSet(Parcel in)
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

        public static final Creator<PortfolioValueGetSet> CREATOR = new Creator<PortfolioValueGetSet>()
        {
            @Override
            public PortfolioValueGetSet createFromParcel(Parcel in)
            {
                return new PortfolioValueGetSet(in);
            }

            @Override
            public PortfolioValueGetSet[] newArray(int size)
            {
                return new PortfolioValueGetSet[size];
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
}
