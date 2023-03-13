package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 04-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class CapitalGainBean
{

    /**
     * category_val : [{"SchemeName":"Aditya Birla SL - Corporate Bond Fund (G)","category_name":"Debt","value":[{"FolioNo":"1015875385","PurchaseAmount":80000.29,"PurchaseDate":"19/01/2017","Units":"1295.18","SaleNav":"66.6816","PurchaseNav":"61.7677","SaleAmount":86364.67,"SaleDate":"06/04/2018","CapitalGain":6364.39,"Days":442,"AbsReturn":7.9554586926874,"AnualiseReturn":6.5695529928301,"STCG":6364.39}],"SchemeTotal":{"SellValTotal":548160.76,"PurchaseValTotal":530000.27,"CapitalGainTotal":18160.49,"STCL_total":0,"STCG_total":18160.49,"LTCL_total":0,"LTCG_total":0}}]
     * category_key : Debt
     * category_total : {"GrandCapitalGainTotal":145142.29,"GrandPurchaseValTotal":6812881.91,"GrandSellValTotal":6958024.21,"Grand_STCL_total":0,"Grand_STCG_total":145142.29,"Grand_LTCL_total":0,"Grand_LTCG_total":0}
     */

    private String category_key = "";
    private CategoryTotalGetSet category_total;
    private ArrayList<CategoryValGetSet> category_val = new ArrayList<>();

    public String getCategory_key()
    {
        return category_key;
    }

    public void setCategory_key(String category_key)
    {
        this.category_key = category_key;
    }

    public CategoryTotalGetSet getCategory_total()
    {
        return category_total;
    }

    public void setCategory_total(CategoryTotalGetSet category_total)
    {
        this.category_total = category_total;
    }

    public ArrayList<CategoryValGetSet> getCategory_val()
    {
        return category_val;
    }

    public void setCategory_val(ArrayList<CategoryValGetSet> category_val)
    {
        this.category_val = category_val;
    }

    public static class CategoryTotalGetSet
    {
        /**
         * GrandCapitalGainTotal : 145142.29
         * GrandPurchaseValTotal : 6812881.91
         * GrandSellValTotal : 6958024.21
         * Grand_STCL_total : 0
         * Grand_STCG_total : 145142.29
         * Grand_LTCL_total : 0
         * Grand_LTCG_total : 0
         */

        private double GrandCapitalGainTotal = 0;
        private double GrandPurchaseValTotal = 0;
        private double GrandSellValTotal = 0;
        private int Grand_STCL_total = -1;
        private double Grand_STCG_total = 0;
        private int Grand_LTCL_total = -1;
        private int Grand_LTCG_total = -1;

        public double getGrandCapitalGainTotal()
        {
            return GrandCapitalGainTotal;
        }

        public void setGrandCapitalGainTotal(double GrandCapitalGainTotal)
        {
            this.GrandCapitalGainTotal = GrandCapitalGainTotal;
        }

        public double getGrandPurchaseValTotal()
        {
            return GrandPurchaseValTotal;
        }

        public void setGrandPurchaseValTotal(double GrandPurchaseValTotal)
        {
            this.GrandPurchaseValTotal = GrandPurchaseValTotal;
        }

        public double getGrandSellValTotal()
        {
            return GrandSellValTotal;
        }

        public void setGrandSellValTotal(double GrandSellValTotal)
        {
            this.GrandSellValTotal = GrandSellValTotal;
        }

        public int getGrand_STCL_total()
        {
            return Grand_STCL_total;
        }

        public void setGrand_STCL_total(int Grand_STCL_total)
        {
            this.Grand_STCL_total = Grand_STCL_total;
        }

        public double getGrand_STCG_total()
        {
            return Grand_STCG_total;
        }

        public void setGrand_STCG_total(double Grand_STCG_total)
        {
            this.Grand_STCG_total = Grand_STCG_total;
        }

        public int getGrand_LTCL_total()
        {
            return Grand_LTCL_total;
        }

        public void setGrand_LTCL_total(int Grand_LTCL_total)
        {
            this.Grand_LTCL_total = Grand_LTCL_total;
        }

        public int getGrand_LTCG_total()
        {
            return Grand_LTCG_total;
        }

        public void setGrand_LTCG_total(int Grand_LTCG_total)
        {
            this.Grand_LTCG_total = Grand_LTCG_total;
        }
    }

    public static class CategoryValGetSet
    {
        /**
         * SchemeName : Aditya Birla SL - Corporate Bond Fund (G)
         * category_name : Debt
         * value : [{"FolioNo":"1015875385","PurchaseAmount":80000.29,"PurchaseDate":"19/01/2017","Units":"1295.18","SaleNav":"66.6816","PurchaseNav":"61.7677","SaleAmount":86364.67,"SaleDate":"06/04/2018","CapitalGain":6364.39,"Days":442,"AbsReturn":7.9554586926874,"AnualiseReturn":6.5695529928301,"STCG":6364.39}]
         * SchemeTotal : {"SellValTotal":548160.76,"PurchaseValTotal":530000.27,"CapitalGainTotal":18160.49,"STCL_total":0,"STCG_total":18160.49,"LTCL_total":0,"LTCG_total":0}
         */

        private String SchemeName = "";
        private String category_name = "";
        private SchemeTotalGetSet SchemeTotal;
        private ArrayList<ValueGetSet> value = new ArrayList<>();

        public String getSchemeName()
        {
            return SchemeName;
        }

        public void setSchemeName(String SchemeName)
        {
            this.SchemeName = SchemeName;
        }

        public String getCategory_name()
        {
            return category_name;
        }

        public void setCategory_name(String category_name)
        {
            this.category_name = category_name;
        }

        public SchemeTotalGetSet getSchemeTotal()
        {
            return SchemeTotal;
        }

        public void setSchemeTotal(SchemeTotalGetSet SchemeTotal)
        {
            this.SchemeTotal = SchemeTotal;
        }

        public ArrayList<ValueGetSet> getValue()
        {
            return value;
        }

        public void setValue(ArrayList<ValueGetSet> value)
        {
            this.value = value;
        }

        public static class SchemeTotalGetSet
        {
            /**
             * SellValTotal : 548160.76
             * PurchaseValTotal : 530000.27
             * CapitalGainTotal : 18160.49
             * STCL_total : 0
             * STCG_total : 18160.49
             * LTCL_total : 0
             * LTCG_total : 0
             */

            private double SellValTotal = 0;
            private double PurchaseValTotal = 0;
            private double CapitalGainTotal = 0;
            private int STCL_total = -1;
            private double STCG_total = 0;
            private int LTCL_total = -1;
            private int LTCG_total = -1;

            public double getSellValTotal()
            {
                return SellValTotal;
            }

            public void setSellValTotal(double SellValTotal)
            {
                this.SellValTotal = SellValTotal;
            }

            public double getPurchaseValTotal()
            {
                return PurchaseValTotal;
            }

            public void setPurchaseValTotal(double PurchaseValTotal)
            {
                this.PurchaseValTotal = PurchaseValTotal;
            }

            public double getCapitalGainTotal()
            {
                return CapitalGainTotal;
            }

            public void setCapitalGainTotal(double CapitalGainTotal)
            {
                this.CapitalGainTotal = CapitalGainTotal;
            }

            public int getSTCL_total()
            {
                return STCL_total;
            }

            public void setSTCL_total(int STCL_total)
            {
                this.STCL_total = STCL_total;
            }

            public double getSTCG_total()
            {
                return STCG_total;
            }

            public void setSTCG_total(double STCG_total)
            {
                this.STCG_total = STCG_total;
            }

            public int getLTCL_total()
            {
                return LTCL_total;
            }

            public void setLTCL_total(int LTCL_total)
            {
                this.LTCL_total = LTCL_total;
            }

            public int getLTCG_total()
            {
                return LTCG_total;
            }

            public void setLTCG_total(int LTCG_total)
            {
                this.LTCG_total = LTCG_total;
            }
        }

        public static class ValueGetSet
        {
            /**
             * FolioNo : 1015875385
             * PurchaseAmount : 80000.29
             * PurchaseDate : 19/01/2017
             * Units : 1295.18
             * SaleNav : 66.6816
             * PurchaseNav : 61.7677
             * SaleAmount : 86364.67
             * SaleDate : 06/04/2018
             * CapitalGain : 6364.39
             * Days : 442
             * AbsReturn : 7.9554586926874
             * AnualiseReturn : 6.5695529928301
             * STCG : 6364.39
             */

            private String FolioNo = "";
            private double PurchaseAmount = 0;
            private String PurchaseDate = "";
            private String Units = "";
            private String SaleNav = "";
            private String PurchaseNav = "";
            private double SaleAmount = 0;
            private String SaleDate = "";
            private double CapitalGain = 0;
            private int Days = -1;
            private double AbsReturn = 0;
            private double AnualiseReturn = 0;
            private double STCG = 0;
            private double LTCG = 0 ;

            public String getFolioNo()
            {
                return FolioNo;
            }

            public void setFolioNo(String FolioNo)
            {
                this.FolioNo = FolioNo;
            }

            public double getPurchaseAmount()
            {
                return PurchaseAmount;
            }

            public void setPurchaseAmount(double PurchaseAmount)
            {
                this.PurchaseAmount = PurchaseAmount;
            }

            public String getPurchaseDate()
            {
                return PurchaseDate;
            }

            public void setPurchaseDate(String PurchaseDate)
            {
                this.PurchaseDate = PurchaseDate;
            }

            public String getUnits()
            {
                return Units;
            }

            public void setUnits(String Units)
            {
                this.Units = Units;
            }

            public String getSaleNav()
            {
                return SaleNav;
            }

            public void setSaleNav(String SaleNav)
            {
                this.SaleNav = SaleNav;
            }

            public String getPurchaseNav()
            {
                return PurchaseNav;
            }

            public void setPurchaseNav(String PurchaseNav)
            {
                this.PurchaseNav = PurchaseNav;
            }

            public double getSaleAmount()
            {
                return SaleAmount;
            }

            public void setSaleAmount(double SaleAmount)
            {
                this.SaleAmount = SaleAmount;
            }

            public String getSaleDate()
            {
                return SaleDate;
            }

            public void setSaleDate(String SaleDate)
            {
                this.SaleDate = SaleDate;
            }

            public double getCapitalGain()
            {
                return CapitalGain;
            }

            public void setCapitalGain(double CapitalGain)
            {
                this.CapitalGain = CapitalGain;
            }

            public int getDays()
            {
                return Days;
            }

            public void setDays(int Days)
            {
                this.Days = Days;
            }

            public double getAbsReturn()
            {
                return AbsReturn;
            }

            public void setAbsReturn(double AbsReturn)
            {
                this.AbsReturn = AbsReturn;
            }

            public double getAnualiseReturn()
            {
                return AnualiseReturn;
            }

            public void setAnualiseReturn(double AnualiseReturn)
            {
                this.AnualiseReturn = AnualiseReturn;
            }

            public double getSTCG()
            {
                return STCG;
            }

            public void setSTCG(double STCG)
            {
                this.STCG = STCG;
            }

            public double getLTCG()
            {
                return LTCG;
            }

            public void setLTCG(double LTCG)
            {
                this.LTCG = LTCG;
            }
        }
    }
}
