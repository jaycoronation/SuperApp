package com.application.alphacapital.superapp.pms.beans;

/**
 * Created by Maharshi Saparia on 01-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class OneDayChangeBean
{

    /**
     * FolioNo : 1015875385
     * Code : F0003FS163
     * Schemename : Aditya Birla SL - Equity Hybrid 95 Fund Reg (G)
     * Change : 0.59
     * NAV : 760.3899
     * NAVDate : 29 Mar 2019
     */

    private String FolioNo = "";
    private String Code = "";
    private String Schemename = "";
    private String Change = "";
    private String NAV = "";
    private String NAVDate = "";

    public String getFolioNo()
    {
        return FolioNo;
    }

    public void setFolioNo(String FolioNo)
    {
        this.FolioNo = FolioNo;
    }

    public String getCode()
    {
        return Code;
    }

    public void setCode(String Code)
    {
        this.Code = Code;
    }

    public String getSchemename()
    {
        return Schemename;
    }

    public void setSchemename(String Schemename)
    {
        this.Schemename = Schemename;
    }

    public String getChange()
    {
        return Change;
    }

    public void setChange(String Change)
    {
        this.Change = Change;
    }

    public String getNAV()
    {
        return NAV;
    }

    public void setNAV(String NAV)
    {
        this.NAV = NAV;
    }

    public String getNAVDate()
    {
        return NAVDate;
    }

    public void setNAVDate(String NAVDate)
    {
        this.NAVDate = NAVDate;
    }
}
