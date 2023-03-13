package com.application.alphacapital.superapp.vault.model;

public class GetPercentageResponse
{
    private int success = 0;
    private String master_market_percentage = "";

    public int getSuccess()
    {
        return success;
    }

    public void setSuccess(int success)
    {
        this.success = success;
    }

    public String getMaster_market_percentage()
    {
        return master_market_percentage;
    }

    public void setMaster_market_percentage(String master_market_percentage)
    {
        this.master_market_percentage = master_market_percentage;
    }
}
