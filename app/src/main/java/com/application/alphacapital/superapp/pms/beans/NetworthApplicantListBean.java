package com.application.alphacapital.superapp.pms.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Maharshi Saparia on 27-05-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class NetworthApplicantListBean implements Parcelable,Serializable
{

    /**
     * applicants : [{"ApplicantName":"Abhimanyu Bansal","Cid":"01C00042","GroupLeader":"01C00039","InitialVal":"2331966.33","CurrentVal":"2485720.77","DividendValue":"15739.00","Gain":"169493.44","AbsoluteReturn":"7.26","WeightedDays":"445","CAGR":"5.92"}]
     * total : {"InitialVal":1.0593736934E8,"CurrentVal":1.1084370677E8,"Gain":5297700.61,"DividendValue":391363.2,"AbsoluteReturn":"-","CAGR":"-","WeightedDays":3967}
     */

    private String ApplicantName = "";
    private String Cid = "";
    private String GroupLeader = "";
    private String InitialVal = "";
    private String CurrentVal = "";
    private String DividendValue = "";
    private String Gain = "";
    private String AbsoluteReturn = "";
    private String WeightedDays = "";
    private String CAGR = "";

    public NetworthApplicantListBean()
    {

    }

    protected NetworthApplicantListBean(Parcel in)
    {
        ApplicantName = in.readString();
        Cid = in.readString();
        GroupLeader = in.readString();
        InitialVal = in.readString();
        CurrentVal = in.readString();
        DividendValue = in.readString();
        Gain = in.readString();
        AbsoluteReturn = in.readString();
        WeightedDays = in.readString();
        CAGR = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(ApplicantName);
        dest.writeString(Cid);
        dest.writeString(GroupLeader);
        dest.writeString(InitialVal);
        dest.writeString(CurrentVal);
        dest.writeString(DividendValue);
        dest.writeString(Gain);
        dest.writeString(AbsoluteReturn);
        dest.writeString(WeightedDays);
        dest.writeString(CAGR);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<NetworthApplicantListBean> CREATOR = new Creator<NetworthApplicantListBean>()
    {
        @Override
        public NetworthApplicantListBean createFromParcel(Parcel in)
        {
            return new NetworthApplicantListBean(in);
        }

        @Override
        public NetworthApplicantListBean[] newArray(int size)
        {
            return new NetworthApplicantListBean[size];
        }
    };

    public String getApplicantName()
    {
        return ApplicantName;
    }

    public void setApplicantName(String ApplicantName)
    {
        this.ApplicantName = ApplicantName;
    }

    public String getCid()
    {
        return Cid;
    }

    public void setCid(String Cid)
    {
        this.Cid = Cid;
    }

    public String getGroupLeader()
    {
        return GroupLeader;
    }

    public void setGroupLeader(String GroupLeader)
    {
        this.GroupLeader = GroupLeader;
    }

    public String getInitialVal()
    {
        return InitialVal;
    }

    public void setInitialVal(String InitialVal)
    {
        this.InitialVal = InitialVal;
    }

    public String getCurrentVal()
    {
        return CurrentVal;
    }

    public void setCurrentVal(String CurrentVal)
    {
        this.CurrentVal = CurrentVal;
    }

    public String getDividendValue()
    {
        return DividendValue;
    }

    public void setDividendValue(String DividendValue)
    {
        this.DividendValue = DividendValue;
    }

    public String getGain()
    {
        return Gain;
    }

    public void setGain(String Gain)
    {
        this.Gain = Gain;
    }

    public String getAbsoluteReturn()
    {
        return AbsoluteReturn;
    }

    public void setAbsoluteReturn(String AbsoluteReturn)
    {
        this.AbsoluteReturn = AbsoluteReturn;
    }

    public String getWeightedDays()
    {
        return WeightedDays;
    }

    public void setWeightedDays(String WeightedDays)
    {
        this.WeightedDays = WeightedDays;
    }

    public String getCAGR()
    {
        return CAGR;
    }

    public void setCAGR(String CAGR)
    {
        this.CAGR = CAGR;
    }
}
