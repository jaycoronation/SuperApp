package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 08-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class DividendSummaryBean
{

    /**
     * ApplicantName : Alpha Capital
     * GrandTotal : 19021.39
     * DividendSummaryDetail : [{"FolioNo":"1015875385","SchemeName":"Aditya Birla SL - Savings Fund Reg (D) (D)","Total":"8682.54","DivPayout":"0.00","DivReinvest":"8682.54"},{"FolioNo":"1015875385","SchemeName":"Aditya Birla SL - Savings Fund Ret (D) (D)(Susp)","Total":"10338.85","DivPayout":"0.00","DivReinvest":"10338.85"}]
     */

    private String ApplicantName;
    private String GrandTotal;
    private ArrayList<DividendSummaryDetailGetSet> DividendSummaryDetail;

    public String getApplicantName()
    {
        return ApplicantName;
    }

    public void setApplicantName(String ApplicantName)
    {
        this.ApplicantName = ApplicantName;
    }

    public String getGrandTotal()
    {
        return GrandTotal;
    }

    public void setGrandTotal(String GrandTotal)
    {
        this.GrandTotal = GrandTotal;
    }

    public ArrayList<DividendSummaryDetailGetSet> getDividendSummaryDetail()
    {
        return DividendSummaryDetail;
    }

    public void setDividendSummaryDetail(ArrayList<DividendSummaryDetailGetSet> DividendSummaryDetail)
    {
        this.DividendSummaryDetail = DividendSummaryDetail;
    }

    public static class DividendSummaryDetailGetSet
    {
        /**
         * FolioNo : 1015875385
         * SchemeName : Aditya Birla SL - Savings Fund Reg (D) (D)
         * Total : 8682.54
         * DivPayout : 0.00
         * DivReinvest : 8682.54
         */

        private String FolioNo;
        private String SchemeName;
        private String Total;
        private String DivPayout;
        private String DivReinvest;

        public String getFolioNo()
        {
            return FolioNo;
        }

        public void setFolioNo(String FolioNo)
        {
            this.FolioNo = FolioNo;
        }

        public String getSchemeName()
        {
            return SchemeName;
        }

        public void setSchemeName(String SchemeName)
        {
            this.SchemeName = SchemeName;
        }

        public String getTotal()
        {
            return Total;
        }

        public void setTotal(String Total)
        {
            this.Total = Total;
        }

        public String getDivPayout()
        {
            return DivPayout;
        }

        public void setDivPayout(String DivPayout)
        {
            this.DivPayout = DivPayout;
        }

        public String getDivReinvest()
        {
            return DivReinvest;
        }

        public void setDivReinvest(String DivReinvest)
        {
            this.DivReinvest = DivReinvest;
        }
    }
}
