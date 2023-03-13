
package com.application.alphacapital.superapp.pms.beans;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Hybrid {

    @SerializedName("Amount")
    private String amount;
    @SerializedName("HoldingPercentage")
    private String holdingPercentage;
    @SerializedName("Objective")
    private String objective;
    @SerializedName("percentage_amount")
    private Double percentageAmount;
    @SerializedName("total_amount")
    private Long totalAmount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHoldingPercentage() {
        return holdingPercentage;
    }

    public void setHoldingPercentage(String holdingPercentage) {
        this.holdingPercentage = holdingPercentage;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Double getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(Double percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

}
