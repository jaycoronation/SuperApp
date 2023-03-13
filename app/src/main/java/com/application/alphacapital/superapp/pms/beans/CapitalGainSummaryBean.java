package com.application.alphacapital.superapp.pms.beans;

import java.util.ArrayList;

/**
 * Created by Maharshi Saparia on 08/05/2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class CapitalGainSummaryBean
{
    private String Applicant ="";
    private SchemeTotalGetSet SchemeTotal = new SchemeTotalGetSet();
    private ArrayList<ValueGetSet> value = new ArrayList<ValueGetSet>();

    public String getApplicant()
    {
        return Applicant;
    }

    public void setApplicant(String Applicant)
    {
        this.Applicant = Applicant;
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
        private double CapitalGainTotal= 0;
        private double LTCG_total= 0;
        private int STCL_total= -1;
        private double STCG_total= 0;
        private int LTCL_total= -1;

        public double getCapitalGainTotal()
        {
            return CapitalGainTotal;
        }

        public void setCapitalGainTotal(double CapitalGainTotal)
        {
            this.CapitalGainTotal = CapitalGainTotal;
        }

        public double getLTCG_total()
        {
            return LTCG_total;
        }

        public void setLTCG_total(double LTCG_total)
        {
            this.LTCG_total = LTCG_total;
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
    }

    public static class ValueGetSet
    {
        /**
         * category_key : Equity
         * capital_gain : 58290.89
         * STCL_total : 0
         * STCG_total : 0
         * LTCL_total : 0
         * LTCG_total : 58290.89
         */

        private String category_key ="";
        private double capital_gain =0;
        private int STCL_total= -1;
        private int STCG_total= -1;
        private int LTCL_total= -1;
        private double LTCG_total= 0;

        public String getCategory_key()
        {
            return category_key;
        }

        public void setCategory_key(String category_key)
        {
            this.category_key = category_key;
        }

        public double getCapital_gain()
        {
            return capital_gain;
        }

        public void setCapital_gain(double capital_gain)
        {
            this.capital_gain = capital_gain;
        }

        public int getSTCL_total()
        {
            return STCL_total;
        }

        public void setSTCL_total(int STCL_total)
        {
            this.STCL_total = STCL_total;
        }

        public int getSTCG_total()
        {
            return STCG_total;
        }

        public void setSTCG_total(int STCG_total)
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

        public double getLTCG_total()
        {
            return LTCG_total;
        }

        public void setLTCG_total(double LTCG_total)
        {
            this.LTCG_total = LTCG_total;
        }
    }
}
