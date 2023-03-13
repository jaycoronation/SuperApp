package com.application.alphacapital.superapp.pms.beans;

/**
 * Created by Maharshi Saparia on 05-Apr-2019.
 * for Coronation Solutions Pvt Ltd.
 */
public class BSMovementDataBean
{

    /**
     * Hybrid : 472840.57
     * Equity : 1760868.91
     * Debt : 2121069.13
     * timestamp : 29.03.2019
     * total : 4354778.61
     * profite : NA
     * movement : NA
     */

    private String Hybrid = "";
    private String Equity = "";
    private String Debt = "";
    private String timestamp = "";
    private double total = 0;
    private String profite = "";
    private String movement = "";

    public String getHybrid()
    {
        return Hybrid;
    }

    public void setHybrid(String Hybrid)
    {
        this.Hybrid = Hybrid;
    }

    public String getEquity()
    {
        return Equity;
    }

    public void setEquity(String Equity)
    {
        this.Equity = Equity;
    }

    public String getDebt()
    {
        return Debt;
    }

    public void setDebt(String Debt)
    {
        this.Debt = Debt;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public String getProfite()
    {
        return profite;
    }

    public void setProfite(String profite)
    {
        this.profite = profite;
    }

    public String getMovement()
    {
        return movement;
    }

    public void setMovement(String movement)
    {
        this.movement = movement;
    }
}
