
package com.application.alphacapital.superapp.pms.beans;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Value {

    @SerializedName("Amount")
    private Long amount;
    @SerializedName("HoldingPercentage")
    private Double holdingPercentage;
    @SerializedName("Objective")
    private String objective;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getHoldingPercentage() {
        return holdingPercentage;
    }

    public void setHoldingPercentage(Double holdingPercentage) {
        this.holdingPercentage = holdingPercentage;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

}
