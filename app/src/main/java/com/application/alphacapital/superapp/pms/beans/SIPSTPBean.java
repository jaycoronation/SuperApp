package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 08-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class SIPSTPBean
{

    /**
     * sip_stp_key : Aditya Birla SL - Savings Fund Ret (D) (D)(Susp)
     * sip_stp_value : [{"TranDate":"29 Sep 2011","TranTimestamp":1317234600,"Applicant":"Alpha Capital","SchemeName":"Aditya Birla SL - Savings Fund Ret (D) (D)(Susp)","Type":"Purchase","Nature":"Purchase","FolioNo":"1015875385","PurchasedNav":"100.0680","Units":"699.524","Amount":70000,"FCode":"F0003","SCode":"FS447","Product":"F","FirstHolder":"-","SecondHolder":"-","holdingDays":2747,"HoldingValue":192290000,"GainLoss":-70000,"absoluteReturn":-100,"CAGR":-13}]
     * sip_stp_total : {"Amount":-102,"Units":-5,"Nav":0,"GainLoss":102,"CAGR":0,"PresentAmount":0,"HoldingValue":77083345,"CurrentNav":0,"holdingDays":-755719,"absoluteReturn":-100,"PurchasedNav":20}
     */

    private String sip_stp_key = "";
    private SipStpTotalGetSet sip_stp_total;
    private ArrayList<SipStpValueGetSet> sip_stp_value = new ArrayList<>();

    public String getSip_stp_key()
    {
        return sip_stp_key;
    }

    public void setSip_stp_key(String sip_stp_key)
    {
        this.sip_stp_key = sip_stp_key;
    }

    public SipStpTotalGetSet getSip_stp_total()
    {
        return sip_stp_total;
    }

    public void setSip_stp_total(SipStpTotalGetSet sip_stp_total)
    {
        this.sip_stp_total = sip_stp_total;
    }

    public ArrayList<SipStpValueGetSet> getSip_stp_value()
    {
        return sip_stp_value;
    }

    public void setSip_stp_value(ArrayList<SipStpValueGetSet> sip_stp_value)
    {
        this.sip_stp_value = sip_stp_value;
    }

    public static class SipStpTotalGetSet
    {
        /**
         * Amount : -102
         * Units : -5
         * Nav : 0
         * GainLoss : 102
         * CAGR : 0
         * PresentAmount : 0
         * HoldingValue : 77083345
         * CurrentNav : 0
         * holdingDays : -755719
         * absoluteReturn : -100
         * PurchasedNav : 20
         */

        private int Amount = -1;
        private int Units = -1;
        private int Nav = -1;
        private int GainLoss = -1;
        private int CAGR = -1;
        private int PresentAmount = -1;
        private int HoldingValue = -1;
        private int CurrentNav = -1;
        private int holdingDays = -1;
        private int absoluteReturn = -1;
        private int PurchasedNav = -1;

        public int getAmount()
        {
            return Amount;
        }

        public void setAmount(int Amount)
        {
            this.Amount = Amount;
        }

        public int getUnits()
        {
            return Units;
        }

        public void setUnits(int Units)
        {
            this.Units = Units;
        }

        public int getNav()
        {
            return Nav;
        }

        public void setNav(int Nav)
        {
            this.Nav = Nav;
        }

        public int getGainLoss()
        {
            return GainLoss;
        }

        public void setGainLoss(int GainLoss)
        {
            this.GainLoss = GainLoss;
        }

        public int getCAGR()
        {
            return CAGR;
        }

        public void setCAGR(int CAGR)
        {
            this.CAGR = CAGR;
        }

        public int getPresentAmount()
        {
            return PresentAmount;
        }

        public void setPresentAmount(int PresentAmount)
        {
            this.PresentAmount = PresentAmount;
        }

        public int getHoldingValue()
        {
            return HoldingValue;
        }

        public void setHoldingValue(int HoldingValue)
        {
            this.HoldingValue = HoldingValue;
        }

        public int getCurrentNav()
        {
            return CurrentNav;
        }

        public void setCurrentNav(int CurrentNav)
        {
            this.CurrentNav = CurrentNav;
        }

        public int getHoldingDays()
        {
            return holdingDays;
        }

        public void setHoldingDays(int holdingDays)
        {
            this.holdingDays = holdingDays;
        }

        public int getAbsoluteReturn()
        {
            return absoluteReturn;
        }

        public void setAbsoluteReturn(int absoluteReturn)
        {
            this.absoluteReturn = absoluteReturn;
        }

        public int getPurchasedNav()
        {
            return PurchasedNav;
        }

        public void setPurchasedNav(int PurchasedNav)
        {
            this.PurchasedNav = PurchasedNav;
        }
    }

    public static class SipStpValueGetSet
    {
        /**
         * TranDate : 29 Sep 2011
         * TranTimestamp : 1317234600
         * Applicant : Alpha Capital
         * SchemeName : Aditya Birla SL - Savings Fund Ret (D) (D)(Susp)
         * Type : Purchase
         * Nature : Purchase
         * FolioNo : 1015875385
         * PurchasedNav : 100.0680
         * Units : 699.524
         * Amount : 70000
         * FCode : F0003
         * SCode : FS447
         * Product : F
         * FirstHolder : -
         * SecondHolder : -
         * holdingDays : 2747
         * HoldingValue : 192290000
         * GainLoss : -70000
         * absoluteReturn : -100
         * CAGR : -13
         */

        private String CurrentNav = "" ;
        private String PresentAmount = "" ;
        private String TranDate = "";
        private int TranTimestamp = -1;
        private String Applicant = "";
        private String SchemeName = "";
        private String Type = "";
        private String Nature = "";
        private String FolioNo = "";
        private String PurchasedNav = "";
        private String Units = "";
        private int Amount = -1;
        private String FCode = "";
        private String SCode = "";
        private String Product = "";
        private String FirstHolder = "";
        private String SecondHolder = "";
        private int holdingDays = -1;
        private int HoldingValue = -1;
        private int GainLoss = -1;
        private int absoluteReturn = -1;
        private int CAGR = -1;

        public String getCurrentNav()
        {
            return CurrentNav;
        }

        public void setCurrentNav(String currentNav)
        {
            CurrentNav = currentNav;
        }

        public String getPresentAmount()
        {
            return PresentAmount;
        }

        public void setPresentAmount(String presentAmount)
        {
            PresentAmount = presentAmount;
        }

        public String getTranDate()
        {
            return TranDate;
        }

        public void setTranDate(String TranDate)
        {
            this.TranDate = TranDate;
        }

        public int getTranTimestamp()
        {
            return TranTimestamp;
        }

        public void setTranTimestamp(int TranTimestamp)
        {
            this.TranTimestamp = TranTimestamp;
        }

        public String getApplicant()
        {
            return Applicant;
        }

        public void setApplicant(String Applicant)
        {
            this.Applicant = Applicant;
        }

        public String getSchemeName()
        {
            return SchemeName;
        }

        public void setSchemeName(String SchemeName)
        {
            this.SchemeName = SchemeName;
        }

        public String getType()
        {
            return Type;
        }

        public void setType(String Type)
        {
            this.Type = Type;
        }

        public String getNature()
        {
            return Nature;
        }

        public void setNature(String Nature)
        {
            this.Nature = Nature;
        }

        public String getFolioNo()
        {
            return FolioNo;
        }

        public void setFolioNo(String FolioNo)
        {
            this.FolioNo = FolioNo;
        }

        public String getPurchasedNav()
        {
            return PurchasedNav;
        }

        public void setPurchasedNav(String PurchasedNav)
        {
            this.PurchasedNav = PurchasedNav;
        }

        public String getUnits()
        {
            return Units;
        }

        public void setUnits(String Units)
        {
            this.Units = Units;
        }

        public int getAmount()
        {
            return Amount;
        }

        public void setAmount(int Amount)
        {
            this.Amount = Amount;
        }

        public String getFCode()
        {
            return FCode;
        }

        public void setFCode(String FCode)
        {
            this.FCode = FCode;
        }

        public String getSCode()
        {
            return SCode;
        }

        public void setSCode(String SCode)
        {
            this.SCode = SCode;
        }

        public String getProduct()
        {
            return Product;
        }

        public void setProduct(String Product)
        {
            this.Product = Product;
        }

        public String getFirstHolder()
        {
            return FirstHolder;
        }

        public void setFirstHolder(String FirstHolder)
        {
            this.FirstHolder = FirstHolder;
        }

        public String getSecondHolder()
        {
            return SecondHolder;
        }

        public void setSecondHolder(String SecondHolder)
        {
            this.SecondHolder = SecondHolder;
        }

        public int getHoldingDays()
        {
            return holdingDays;
        }

        public void setHoldingDays(int holdingDays)
        {
            this.holdingDays = holdingDays;
        }

        public int getHoldingValue()
        {
            return HoldingValue;
        }

        public void setHoldingValue(int HoldingValue)
        {
            this.HoldingValue = HoldingValue;
        }

        public int getGainLoss()
        {
            return GainLoss;
        }

        public void setGainLoss(int GainLoss)
        {
            this.GainLoss = GainLoss;
        }

        public int getAbsoluteReturn()
        {
            return absoluteReturn;
        }

        public void setAbsoluteReturn(int absoluteReturn)
        {
            this.absoluteReturn = absoluteReturn;
        }

        public int getCAGR()
        {
            return CAGR;
        }

        public void setCAGR(int CAGR)
        {
            this.CAGR = CAGR;
        }
    }
}
