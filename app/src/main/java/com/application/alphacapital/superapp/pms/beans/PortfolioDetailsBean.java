package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 04-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class PortfolioDetailsBean
{

    /**
     * total_value : {"CurrentValue":2128267,"Gain":158336,"InitialValue":1969930.56}
     * portfolio_key : Debt
     * portfolio_value : [{"sub_cat_key":"Debt: Low Duration","sub_cat":[{"sub_category":"Debt: Low Duration","PurchaseDate":"NA","SchemeName":"Franklin - India Low Duration (G)","InitialValue":"601017.92","CurrentValue":656621,"Gain":55603,"AbsoluteReturn":9,"CAGR":9,"Change":"0.00","FolioNo":"4009910320428","FCode":"F0032","SCode":"FS017","Exlcode":"639","Unit":30123,"Holdingdays":360,"Objective":"Debt: Low Duration","UCC":"5011156275","HoldingPercentage":"15.04","PurchasedNav":20,"CurrentNav":22,"ModeOfHolding":"NA","BankName":"NA","MaturityDate":"NA","ServiceProvider":"AlphaCapital","Joint1Name":"-","Joint2Name":"-","Nominee":"-","RemainingUnits":"-"},{"sub_category":"Debt: Low Duration","PurchaseDate":"NA","SchemeName":"L&T - Low Duration Fund (G)","InitialValue":"399999.99","CurrentValue":436149,"Gain":36149,"AbsoluteReturn":9,"CAGR":7,"Change":"0.00","FolioNo":"3726531/45","FCode":"F0006","SCode":"FS397","Exlcode":"30857","Unit":21742,"Holdingdays":457,"Objective":"Debt: Low Duration","UCC":"5011156275","HoldingPercentage":"9.99","PurchasedNav":18,"CurrentNav":20,"ModeOfHolding":"NA","BankName":"NA","MaturityDate":"NA","ServiceProvider":"AlphaCapital","Joint1Name":"-","Joint2Name":"-","Nominee":"-","RemainingUnits":"-"}],"portfolio_total":{"CurrentValue":1092770,"Gain":91752,"InitialValue":1001017.91}},{"sub_cat_key":"Debt: Ultra Short Duration","sub_cat":[{"sub_category":"Debt: Ultra Short Duration","PurchaseDate":"NA","SchemeName":"Franklin - India Ultra Short Bond Super Ins (G)","InitialValue":"968912.65","CurrentValue":1035497,"Gain":66584,"AbsoluteReturn":7,"CAGR":10,"Change":"0.01","FolioNo":"3099910320428","FCode":"F0032","SCode":"FS264","Exlcode":"16134","Unit":39352,"Holdingdays":254,"Objective":"Debt: Ultra Short Duration","UCC":"5011156275","HoldingPercentage":"23.72","PurchasedNav":25,"CurrentNav":26,"ModeOfHolding":"NA","BankName":"NA","MaturityDate":"NA","ServiceProvider":"AlphaCapital","Joint1Name":"-","Joint2Name":"-","Nominee":"-","RemainingUnits":"-"}],"portfolio_total":{"CurrentValue":1035497,"Gain":66584,"InitialValue":968912.65}}]
     */

    private TotalValueGetSet total_value;
    private String portfolio_key = "";
    private ArrayList<PortfolioValueGetSet> portfolio_value = new ArrayList<>();

    public TotalValueGetSet getTotal_value()
    {
        return total_value;
    }

    public void setTotal_value(TotalValueGetSet total_value)
    {
        this.total_value = total_value;
    }

    public String getPortfolio_key()
    {
        return portfolio_key;
    }

    public void setPortfolio_key(String portfolio_key)
    {
        this.portfolio_key = portfolio_key;
    }

    public ArrayList<PortfolioValueGetSet> getPortfolio_value()
    {
        return portfolio_value;
    }

    public void setPortfolio_value(ArrayList<PortfolioValueGetSet> portfolio_value)
    {
        this.portfolio_value = portfolio_value;
    }

    public static class TotalValueGetSet
    {
        /**
         * CurrentValue : 2128267
         * Gain : 158336
         * InitialValue : 1969930.56
         */

        private int CurrentValue = -1;
        private int Gain = -1;
        private double InitialValue = 0;

        public int getCurrentValue()
        {
            return CurrentValue;
        }

        public void setCurrentValue(int CurrentValue)
        {
            this.CurrentValue = CurrentValue;
        }

        public int getGain()
        {
            return Gain;
        }

        public void setGain(int Gain)
        {
            this.Gain = Gain;
        }

        public double getInitialValue()
        {
            return InitialValue;
        }

        public void setInitialValue(double InitialValue)
        {
            this.InitialValue = InitialValue;
        }
    }

    public static class PortfolioValueGetSet
    {
        /**
         * sub_cat_key : Debt: Low Duration
         * sub_cat : [{"sub_category":"Debt: Low Duration","PurchaseDate":"NA","SchemeName":"Franklin - India Low Duration (G)","InitialValue":"601017.92","CurrentValue":656621,"Gain":55603,"AbsoluteReturn":9,"CAGR":9,"Change":"0.00","FolioNo":"4009910320428","FCode":"F0032","SCode":"FS017","Exlcode":"639","Unit":30123,"Holdingdays":360,"Objective":"Debt: Low Duration","UCC":"5011156275","HoldingPercentage":"15.04","PurchasedNav":20,"CurrentNav":22,"ModeOfHolding":"NA","BankName":"NA","MaturityDate":"NA","ServiceProvider":"AlphaCapital","Joint1Name":"-","Joint2Name":"-","Nominee":"-","RemainingUnits":"-"},{"sub_category":"Debt: Low Duration","PurchaseDate":"NA","SchemeName":"L&T - Low Duration Fund (G)","InitialValue":"399999.99","CurrentValue":436149,"Gain":36149,"AbsoluteReturn":9,"CAGR":7,"Change":"0.00","FolioNo":"3726531/45","FCode":"F0006","SCode":"FS397","Exlcode":"30857","Unit":21742,"Holdingdays":457,"Objective":"Debt: Low Duration","UCC":"5011156275","HoldingPercentage":"9.99","PurchasedNav":18,"CurrentNav":20,"ModeOfHolding":"NA","BankName":"NA","MaturityDate":"NA","ServiceProvider":"AlphaCapital","Joint1Name":"-","Joint2Name":"-","Nominee":"-","RemainingUnits":"-"}]
         * portfolio_total : {"CurrentValue":1092770,"Gain":91752,"InitialValue":1001017.91}
         */

        private String sub_cat_key = "";
        private PortfolioTotalGetSet portfolio_total;
        private ArrayList<SubCatGetSet> sub_cat = new ArrayList<>();

        public String getSub_cat_key()
        {
            return sub_cat_key;
        }

        public void setSub_cat_key(String sub_cat_key)
        {
            this.sub_cat_key = sub_cat_key;
        }

        public PortfolioTotalGetSet getPortfolio_total()
        {
            return portfolio_total;
        }

        public void setPortfolio_total(PortfolioTotalGetSet portfolio_total)
        {
            this.portfolio_total = portfolio_total;
        }

        public ArrayList<SubCatGetSet> getSub_cat()
        {
            return sub_cat;
        }

        public void setSub_cat(ArrayList<SubCatGetSet> sub_cat)
        {
            this.sub_cat = sub_cat;
        }

        public static class PortfolioTotalGetSet
        {
            /**
             * CurrentValue : 1092770
             * Gain : 91752
             * InitialValue : 1001017.91
             */

            private int CurrentValue = -1;
            private int Gain = -1;
            private double InitialValue = 0;

            public int getCurrentValue()
            {
                return CurrentValue;
            }

            public void setCurrentValue(int CurrentValue)
            {
                this.CurrentValue = CurrentValue;
            }

            public int getGain()
            {
                return Gain;
            }

            public void setGain(int Gain)
            {
                this.Gain = Gain;
            }

            public double getInitialValue()
            {
                return InitialValue;
            }

            public void setInitialValue(double InitialValue)
            {
                this.InitialValue = InitialValue;
            }
        }

        public static class SubCatGetSet
        {
            /**
             * sub_category : Debt: Low Duration
             * PurchaseDate : NA
             * SchemeName : Franklin - India Low Duration (G)
             * InitialValue : 601017.92
             * CurrentValue : 656621
             * Gain : 55603
             * AbsoluteReturn : 9
             * CAGR : 9
             * Change : 0.00
             * FolioNo : 4009910320428
             * FCode : F0032
             * SCode : FS017
             * Exlcode : 639
             * Unit : 30123
             * Holdingdays : 360
             * Objective : Debt: Low Duration
             * UCC : 5011156275
             * HoldingPercentage : 15.04
             * PurchasedNav : 20
             * CurrentNav : 22
             * ModeOfHolding : NA
             * BankName : NA
             * MaturityDate : NA
             * ServiceProvider : AlphaCapital
             * Joint1Name : -
             * Joint2Name : -
             * Nominee : -
             * RemainingUnits : -
             */

            private String sub_category = "";
            private String PurchaseDate = "";
            private String SchemeName = "";
            private String InitialValue = "";
            private int CurrentValue = -1;
            private int Gain = -1;
            private int AbsoluteReturn = -1;
            private int CAGR = -1;
            private String Change = "";
            private String FolioNo = "";
            private String FCode = "";
            private String SCode = "";
            private String Exlcode = "";
            private int Unit = -1;
            private int Holdingdays = -1;
            private String Objective = "";
            private String UCC = "";
            private String HoldingPercentage = "";
            private int PurchasedNav = -1;
            private int CurrentNav = -1;
            private String ModeOfHolding = "";
            private String BankName = "";
            private String MaturityDate = "";
            private String ServiceProvider = "";
            private String Joint1Name = "";
            private String Joint2Name = "";
            private String Nominee = "";
            private String RemainingUnits = "";

            public String getSub_category()
            {
                return sub_category;
            }

            public void setSub_category(String sub_category)
            {
                this.sub_category = sub_category;
            }

            public String getPurchaseDate()
            {
                return PurchaseDate;
            }

            public void setPurchaseDate(String PurchaseDate)
            {
                this.PurchaseDate = PurchaseDate;
            }

            public String getSchemeName()
            {
                return SchemeName;
            }

            public void setSchemeName(String SchemeName)
            {
                this.SchemeName = SchemeName;
            }

            public String getInitialValue()
            {
                return InitialValue;
            }

            public void setInitialValue(String InitialValue)
            {
                this.InitialValue = InitialValue;
            }

            public int getCurrentValue()
            {
                return CurrentValue;
            }

            public void setCurrentValue(int CurrentValue)
            {
                this.CurrentValue = CurrentValue;
            }

            public int getGain()
            {
                return Gain;
            }

            public void setGain(int Gain)
            {
                this.Gain = Gain;
            }

            public int getAbsoluteReturn()
            {
                return AbsoluteReturn;
            }

            public void setAbsoluteReturn(int AbsoluteReturn)
            {
                this.AbsoluteReturn = AbsoluteReturn;
            }

            public int getCAGR()
            {
                return CAGR;
            }

            public void setCAGR(int CAGR)
            {
                this.CAGR = CAGR;
            }

            public String getChange()
            {
                return Change;
            }

            public void setChange(String Change)
            {
                this.Change = Change;
            }

            public String getFolioNo()
            {
                return FolioNo;
            }

            public void setFolioNo(String FolioNo)
            {
                this.FolioNo = FolioNo;
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

            public String getExlcode()
            {
                return Exlcode;
            }

            public void setExlcode(String Exlcode)
            {
                this.Exlcode = Exlcode;
            }

            public int getUnit()
            {
                return Unit;
            }

            public void setUnit(int Unit)
            {
                this.Unit = Unit;
            }

            public int getHoldingdays()
            {
                return Holdingdays;
            }

            public void setHoldingdays(int Holdingdays)
            {
                this.Holdingdays = Holdingdays;
            }

            public String getObjective()
            {
                return Objective;
            }

            public void setObjective(String Objective)
            {
                this.Objective = Objective;
            }

            public String getUCC()
            {
                return UCC;
            }

            public void setUCC(String UCC)
            {
                this.UCC = UCC;
            }

            public String getHoldingPercentage()
            {
                return HoldingPercentage;
            }

            public void setHoldingPercentage(String HoldingPercentage)
            {
                this.HoldingPercentage = HoldingPercentage;
            }

            public int getPurchasedNav()
            {
                return PurchasedNav;
            }

            public void setPurchasedNav(int PurchasedNav)
            {
                this.PurchasedNav = PurchasedNav;
            }

            public int getCurrentNav()
            {
                return CurrentNav;
            }

            public void setCurrentNav(int CurrentNav)
            {
                this.CurrentNav = CurrentNav;
            }

            public String getModeOfHolding()
            {
                return ModeOfHolding;
            }

            public void setModeOfHolding(String ModeOfHolding)
            {
                this.ModeOfHolding = ModeOfHolding;
            }

            public String getBankName()
            {
                return BankName;
            }

            public void setBankName(String BankName)
            {
                this.BankName = BankName;
            }

            public String getMaturityDate()
            {
                return MaturityDate;
            }

            public void setMaturityDate(String MaturityDate)
            {
                this.MaturityDate = MaturityDate;
            }

            public String getServiceProvider()
            {
                return ServiceProvider;
            }

            public void setServiceProvider(String ServiceProvider)
            {
                this.ServiceProvider = ServiceProvider;
            }

            public String getJoint1Name()
            {
                return Joint1Name;
            }

            public void setJoint1Name(String Joint1Name)
            {
                this.Joint1Name = Joint1Name;
            }

            public String getJoint2Name()
            {
                return Joint2Name;
            }

            public void setJoint2Name(String Joint2Name)
            {
                this.Joint2Name = Joint2Name;
            }

            public String getNominee()
            {
                return Nominee;
            }

            public void setNominee(String Nominee)
            {
                this.Nominee = Nominee;
            }

            public String getRemainingUnits()
            {
                return RemainingUnits;
            }

            public void setRemainingUnits(String RemainingUnits)
            {
                this.RemainingUnits = RemainingUnits;
            }
        }
    }
}
