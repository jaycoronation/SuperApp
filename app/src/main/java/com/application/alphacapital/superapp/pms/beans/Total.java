
package com.application.alphacapital.superapp.pms.beans;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Total {

    @SerializedName("amount_total")
    private Long amountTotal;
    @SerializedName("percentage_total")
    private Double percentageTotal;

    public Long getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(Long amountTotal) {
        this.amountTotal = amountTotal;
    }

    public Double getPercentageTotal() {
        return percentageTotal;
    }

    public void setPercentageTotal(Double percentageTotal) {
        this.percentageTotal = percentageTotal;
    }

}
