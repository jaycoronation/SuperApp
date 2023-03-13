
package com.application.alphacapital.superapp.pms.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class NetworthTempData {

    @SerializedName("applicants_assets_details")
    public ApplicantsAssetsDetails applicantsAssetsDetails;
    @SerializedName("assets_data")
    public AssetsData assetsData;
    @SerializedName("macro_strategic")
    public MacroStrategic macroStrategic;
    @SerializedName("macro_tactical")
    public MacroTactical macroTactical;
    @SerializedName("micro_strategic")
    public MicroStrategic microStrategic;
    @SerializedName("micro_tactical")
    public MicroTactical microTactical;
    @Expose
    public Double success;

    public ApplicantsAssetsDetails getApplicantsAssetsDetails() {
        return applicantsAssetsDetails;
    }

    public void setApplicantsAssetsDetails(ApplicantsAssetsDetails applicantsAssetsDetails) {
        this.applicantsAssetsDetails = applicantsAssetsDetails;
    }

    public AssetsData getAssetsData() {
        return assetsData;
    }

    public void setAssetsData(AssetsData assetsData) {
        this.assetsData = assetsData;
    }

    public MacroStrategic getMacroStrategic() {
        return macroStrategic;
    }

    public void setMacroStrategic(MacroStrategic macroStrategic) {
        this.macroStrategic = macroStrategic;
    }

    public MacroTactical getMacroTactical() {
        return macroTactical;
    }

    public void setMacroTactical(MacroTactical macroTactical) {
        this.macroTactical = macroTactical;
    }

    public MicroStrategic getMicroStrategic() {
        return microStrategic;
    }

    public void setMicroStrategic(MicroStrategic microStrategic) {
        this.microStrategic = microStrategic;
    }

    public MicroTactical getMicroTactical() {
        return microTactical;
    }

    public void setMicroTactical(MicroTactical microTactical) {
        this.microTactical = microTactical;
    }

    public Double getSuccess() {
        return success;
    }

    public void setSuccess(Double success) {
        this.success = success;
    }

    public class ApplicantsAssetsDetails {

        @SerializedName("applicant_details")
        public List<ApplicantDetail> applicantDetails;
        @SerializedName("assets_grand_total")
        public AssetsGrandTotal assetsGrandTotal;

        public List<ApplicantDetail> getApplicantDetails() {
            return applicantDetails;
        }

        public void setApplicantDetails(List<ApplicantDetail> applicantDetails) {
            this.applicantDetails = applicantDetails;
        }

        public AssetsGrandTotal getAssetsGrandTotal() {
            return assetsGrandTotal;
        }

        public void setAssetsGrandTotal(AssetsGrandTotal assetsGrandTotal) {
            this.assetsGrandTotal = assetsGrandTotal;
        }

    }

    public class AssetsData {

        @SerializedName("assets_details")
        public AssetsDetails assetsDetails;
        @SerializedName("assets_details_grandtotal")
        public AssetsDetailsGrandtotal assetsDetailsGrandtotal;
        @SerializedName("assets_details_total")
        public AssetsDetailsTotal assetsDetailsTotal;

        public AssetsDetails getAssetsDetails() {
            return assetsDetails;
        }

        public void setAssetsDetails(AssetsDetails assetsDetails) {
            this.assetsDetails = assetsDetails;
        }

        public AssetsDetailsGrandtotal getAssetsDetailsGrandtotal() {
            return assetsDetailsGrandtotal;
        }

        public void setAssetsDetailsGrandtotal(AssetsDetailsGrandtotal assetsDetailsGrandtotal) {
            this.assetsDetailsGrandtotal = assetsDetailsGrandtotal;
        }

        public AssetsDetailsTotal getAssetsDetailsTotal() {
            return assetsDetailsTotal;
        }

        public void setAssetsDetailsTotal(AssetsDetailsTotal assetsDetailsTotal) {
            this.assetsDetailsTotal = assetsDetailsTotal;
        }

    }

    public class MacroStrategic {

        @SerializedName("macro_assets_strategic")
        public List<MacroAssetsStrategic> macroAssetsStrategic;
        @SerializedName("macro_assets_strategic_grandtotal")
        public MacroAssetsStrategicGrandtotal macroAssetsStrategicGrandtotal;

        public List<MacroAssetsStrategic> getMacroAssetsStrategic() {
            return macroAssetsStrategic;
        }

        public void setMacroAssetsStrategic(List<MacroAssetsStrategic> macroAssetsStrategic) {
            this.macroAssetsStrategic = macroAssetsStrategic;
        }

        public MacroAssetsStrategicGrandtotal getMacroAssetsStrategicGrandtotal() {
            return macroAssetsStrategicGrandtotal;
        }

        public void setMacroAssetsStrategicGrandtotal(MacroAssetsStrategicGrandtotal macroAssetsStrategicGrandtotal) {
            this.macroAssetsStrategicGrandtotal = macroAssetsStrategicGrandtotal;
        }

    }

    public class MacroTactical {

        @SerializedName("macro_assets_tactical")
        public List<MacroAssetsTactical> macroAssetsTactical;
        @SerializedName("macro_assets_tactical_grandtotal")
        public MacroAssetsTacticalGrandtotal macroAssetsTacticalGrandtotal;

        public List<MacroAssetsTactical> getMacroAssetsTactical() {
            return macroAssetsTactical;
        }

        public void setMacroAssetsTactical(List<MacroAssetsTactical> macroAssetsTactical) {
            this.macroAssetsTactical = macroAssetsTactical;
        }

        public MacroAssetsTacticalGrandtotal getMacroAssetsTacticalGrandtotal() {
            return macroAssetsTacticalGrandtotal;
        }

        public void setMacroAssetsTacticalGrandtotal(MacroAssetsTacticalGrandtotal macroAssetsTacticalGrandtotal) {
            this.macroAssetsTacticalGrandtotal = macroAssetsTacticalGrandtotal;
        }

    }

    public class MicroStrategic {

        @SerializedName("micro_assets_strategic")
        public List<MicroAssetsStrategic> microAssetsStrategic;
        @SerializedName("micro_assets_strategic_grandtotal")
        public MicroAssetsStrategicGrandtotal microAssetsStrategicGrandtotal;

        public List<MicroAssetsStrategic> getMicroAssetsStrategic() {
            return microAssetsStrategic;
        }

        public void setMicroAssetsStrategic(List<MicroAssetsStrategic> microAssetsStrategic) {
            this.microAssetsStrategic = microAssetsStrategic;
        }

        public MicroAssetsStrategicGrandtotal getMicroAssetsStrategicGrandtotal() {
            return microAssetsStrategicGrandtotal;
        }

        public void setMicroAssetsStrategicGrandtotal(MicroAssetsStrategicGrandtotal microAssetsStrategicGrandtotal) {
            this.microAssetsStrategicGrandtotal = microAssetsStrategicGrandtotal;
        }

    }

    public class MicroTactical {

        @SerializedName("micro_assets_tactical")
        public List<MicroAssetsTactical> microAssetsTactical;
        @SerializedName("micro_assets_tactical_grandtotal")
        public MicroAssetsTacticalGrandtotal microAssetsTacticalGrandtotal;

        public List<MicroAssetsTactical> getMicroAssetsTactical() {
            return microAssetsTactical;
        }

        public void setMicroAssetsTactical(List<MicroAssetsTactical> microAssetsTactical) {
            this.microAssetsTactical = microAssetsTactical;
        }

        public MicroAssetsTacticalGrandtotal getMicroAssetsTacticalGrandtotal() {
            return microAssetsTacticalGrandtotal;
        }

        public void setMicroAssetsTacticalGrandtotal(MicroAssetsTacticalGrandtotal microAssetsTacticalGrandtotal) {
            this.microAssetsTacticalGrandtotal = microAssetsTacticalGrandtotal;
        }

    }

    public class MicroAssetsTactical {

        @SerializedName("actual_amount")
        public Double actualAmount;
        @SerializedName("actual_percentage")
        public Double actualPercentage;
        @SerializedName("asset_class")
        public String assetClass;
        @Expose
        public Double policy;
        @Expose
        public Double variance;

        public Double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(Double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public Double getActualPercentage() {
            return actualPercentage;
        }

        public void setActualPercentage(Double actualPercentage) {
            this.actualPercentage = actualPercentage;
        }

        public String getAssetClass() {
            return assetClass;
        }

        public void setAssetClass(String assetClass) {
            this.assetClass = assetClass;
        }

        public Double getPolicy() {
            return policy;
        }

        public void setPolicy(Double policy) {
            this.policy = policy;
        }

        public Double getVariance() {
            return variance;
        }

        public void setVariance(Double variance) {
            this.variance = variance;
        }

    }

    public class MicroAssetsTacticalGrandtotal {

        @SerializedName("grand_percentage_amount")
        public Double grandPercentageAmount;
        @SerializedName("grand_total_amount")
        public Double grandTotalAmount;
        @SerializedName("micro_assets_tactical_policy")
        public Double microAssetsTacticalPolicy;
        @SerializedName("micro_assets_tactical_variance_total")
        public Double microAssetsTacticalVarianceTotal;

        public Double getGrandPercentageAmount() {
            return grandPercentageAmount;
        }

        public void setGrandPercentageAmount(Double grandPercentageAmount) {
            this.grandPercentageAmount = grandPercentageAmount;
        }

        public Double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(Double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

        public Double getMicroAssetsTacticalPolicy() {
            return microAssetsTacticalPolicy;
        }

        public void setMicroAssetsTacticalPolicy(Double microAssetsTacticalPolicy) {
            this.microAssetsTacticalPolicy = microAssetsTacticalPolicy;
        }

        public Double getMicroAssetsTacticalVarianceTotal() {
            return microAssetsTacticalVarianceTotal;
        }

        public void setMicroAssetsTacticalVarianceTotal(Double microAssetsTacticalVarianceTotal) {
            this.microAssetsTacticalVarianceTotal = microAssetsTacticalVarianceTotal;
        }

    }

    public class MicroAssetsStrategicGrandtotal {

        @SerializedName("grand_percentage_amount")
        public Double grandPercentageAmount;
        @SerializedName("grand_total_amount")
        public Double grandTotalAmount;
        @SerializedName("micro_assets_strategic_policy")
        public Double microAssetsStrategicPolicy;
        @SerializedName("micro_assets_strategic_variance_total")
        public Double microAssetsStrategicVarianceTotal;

        public Double getGrandPercentageAmount() {
            return grandPercentageAmount;
        }

        public void setGrandPercentageAmount(Double grandPercentageAmount) {
            this.grandPercentageAmount = grandPercentageAmount;
        }

        public Double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(Double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

        public Double getMicroAssetsStrategicPolicy() {
            return microAssetsStrategicPolicy;
        }

        public void setMicroAssetsStrategicPolicy(Double microAssetsStrategicPolicy) {
            this.microAssetsStrategicPolicy = microAssetsStrategicPolicy;
        }

        public Double getMicroAssetsStrategicVarianceTotal() {
            return microAssetsStrategicVarianceTotal;
        }

        public void setMicroAssetsStrategicVarianceTotal(Double microAssetsStrategicVarianceTotal) {
            this.microAssetsStrategicVarianceTotal = microAssetsStrategicVarianceTotal;
        }

    }

    public class MicroAssetsStrategic {

        @SerializedName("actual_amount")
        public Double actualAmount;
        @SerializedName("actual_percentage")
        public Double actualPercentage;
        @SerializedName("asset_class")
        public String assetClass;
        @Expose
        public Double policy;
        @Expose
        public Double variance;

        public Double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(Double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public Double getActualPercentage() {
            return actualPercentage;
        }

        public void setActualPercentage(Double actualPercentage) {
            this.actualPercentage = actualPercentage;
        }

        public String getAssetClass() {
            return assetClass;
        }

        public void setAssetClass(String assetClass) {
            this.assetClass = assetClass;
        }

        public Double getPolicy() {
            return policy;
        }

        public void setPolicy(Double policy) {
            this.policy = policy;
        }

        public Double getVariance() {
            return variance;
        }

        public void setVariance(Double variance) {
            this.variance = variance;
        }

    }

    public class MacroAssetsTacticalGrandtotal {

        @SerializedName("grand_percentage_amount")
        public Double grandPercentageAmount;
        @SerializedName("grand_total_amount")
        public Double grandTotalAmount;
        @SerializedName("macro_assets_tactical_policy")
        public Double macroAssetsTacticalPolicy;
        @SerializedName("macro_assets_tactical_variance")
        public Double macroAssetsTacticalVariance;

        public Double getGrandPercentageAmount() {
            return grandPercentageAmount;
        }

        public void setGrandPercentageAmount(Double grandPercentageAmount) {
            this.grandPercentageAmount = grandPercentageAmount;
        }

        public Double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(Double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

        public Double getMacroAssetsTacticalPolicy() {
            return macroAssetsTacticalPolicy;
        }

        public void setMacroAssetsTacticalPolicy(Double macroAssetsTacticalPolicy) {
            this.macroAssetsTacticalPolicy = macroAssetsTacticalPolicy;
        }

        public Double getMacroAssetsTacticalVariance() {
            return macroAssetsTacticalVariance;
        }

        public void setMacroAssetsTacticalVariance(Double macroAssetsTacticalVariance) {
            this.macroAssetsTacticalVariance = macroAssetsTacticalVariance;
        }

    }

    public class MacroAssetsTactical {

        @SerializedName("actual_amount")
        public Double actualAmount;
        @SerializedName("actual_percentage")
        public Double actualPercentage;
        @SerializedName("asset_class")
        public String assetClass;
        @Expose
        public Double policy;
        @Expose
        public Double variance;

        public Double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(Double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public Double getActualPercentage() {
            return actualPercentage;
        }

        public void setActualPercentage(Double actualPercentage) {
            this.actualPercentage = actualPercentage;
        }

        public String getAssetClass() {
            return assetClass;
        }

        public void setAssetClass(String assetClass) {
            this.assetClass = assetClass;
        }

        public Double getPolicy() {
            return policy;
        }

        public void setPolicy(Double policy) {
            this.policy = policy;
        }

        public Double getVariance() {
            return variance;
        }

        public void setVariance(Double variance) {
            this.variance = variance;
        }

    }

    public class MacroAssetsStrategicGrandtotal {

        @SerializedName("grand_percentage_amount")
        public Double grandPercentageAmount;
        @SerializedName("grand_total_amount")
        public Double grandTotalAmount;
        @SerializedName("macro_assets_strategic_policy")
        public Double macroAssetsStrategicPolicy;
        @SerializedName("macro_assets_strategic_variance")
        public Double macroAssetsStrategicVariance;

        public Double getGrandPercentageAmount() {
            return grandPercentageAmount;
        }

        public void setGrandPercentageAmount(Double grandPercentageAmount) {
            this.grandPercentageAmount = grandPercentageAmount;
        }

        public Double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(Double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

        public Double getMacroAssetsStrategicPolicy() {
            return macroAssetsStrategicPolicy;
        }

        public void setMacroAssetsStrategicPolicy(Double macroAssetsStrategicPolicy) {
            this.macroAssetsStrategicPolicy = macroAssetsStrategicPolicy;
        }

        public Double getMacroAssetsStrategicVariance() {
            return macroAssetsStrategicVariance;
        }

        public void setMacroAssetsStrategicVariance(Double macroAssetsStrategicVariance) {
            this.macroAssetsStrategicVariance = macroAssetsStrategicVariance;
        }

    }

    public class MacroAssetsStrategic {

        @SerializedName("actual_amount")
        public Double actualAmount;
        @SerializedName("actual_percentage")
        public Double actualPercentage;
        @SerializedName("asset_class")
        public String assetClass;
        @Expose
        public Double policy;
        @Expose
        public Double variance;

        public Double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(Double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public Double getActualPercentage() {
            return actualPercentage;
        }

        public void setActualPercentage(Double actualPercentage) {
            this.actualPercentage = actualPercentage;
        }

        public String getAssetClass() {
            return assetClass;
        }

        public void setAssetClass(String assetClass) {
            this.assetClass = assetClass;
        }

        public Double getPolicy() {
            return policy;
        }

        public void setPolicy(Double policy) {
            this.policy = policy;
        }

        public Double getVariance() {
            return variance;
        }

        public void setVariance(Double variance) {
            this.variance = variance;
        }

    }

    public class AssetsDetails {

        @SerializedName("Debt")
        public List<Debt> debt;
        @SerializedName("Equity")
        public List<Equity> equity;
        @SerializedName("Hybrid")
        public List<Hybrid> hybrid;

        public List<Debt> getDebt() {
            return debt;
        }

        public void setDebt(List<Debt> debt) {
            this.debt = debt;
        }

        public List<Equity> getEquity() {
            return equity;
        }

        public void setEquity(List<Equity> equity) {
            this.equity = equity;
        }

        public List<Hybrid> getHybrid() {
            return hybrid;
        }

        public void setHybrid(List<Hybrid> hybrid) {
            this.hybrid = hybrid;
        }

    }

    public class AssetsDetailsGrandtotal {

        @SerializedName("grand_percentage_amount")
        public Double grandPercentageAmount;
        @SerializedName("grand_total_amount")
        public Double grandTotalAmount;

        public Double getGrandPercentageAmount() {
            return grandPercentageAmount;
        }

        public void setGrandPercentageAmount(Double grandPercentageAmount) {
            this.grandPercentageAmount = grandPercentageAmount;
        }

        public Double getGrandTotalAmount() {
            return grandTotalAmount;
        }

        public void setGrandTotalAmount(Double grandTotalAmount) {
            this.grandTotalAmount = grandTotalAmount;
        }

    }

    public class AssetsDetailsTotal {

        @SerializedName("Debt")
        public Debt debt;
        @SerializedName("Equity")
        public Equity equity;
        @SerializedName("Hybrid")
        public Hybrid hybrid;

        public Debt getDebt() {
            return debt;
        }

        public void setDebt(Debt debt) {
            this.debt = debt;
        }

        public Equity getEquity() {
            return equity;
        }

        public void setEquity(Equity equity) {
            this.equity = equity;
        }

        public Hybrid getHybrid() {
            return hybrid;
        }

        public void setHybrid(Hybrid hybrid) {
            this.hybrid = hybrid;
        }

    }

    public class AssetsGrandTotal {

        @SerializedName("GrandAmount")
        public String grandAmount;
        @SerializedName("GrandHoldingPercentage")
        public Double grandHoldingPercentage;

        public String getGrandAmount() {
            return grandAmount;
        }

        public void setGrandAmount(String grandAmount) {
            this.grandAmount = grandAmount;
        }

        public Double getGrandHoldingPercentage() {
            return grandHoldingPercentage;
        }

        public void setGrandHoldingPercentage(Double grandHoldingPercentage) {
            this.grandHoldingPercentage = grandHoldingPercentage;
        }

    }

    public class ApplicantDetail {

        @SerializedName("apcnt_assets_details")
        public List<ApcntAssetsDetail> apcntAssetsDetails;
        @SerializedName("apcnt_assets_total")
        public ApcntAssetsTotal apcntAssetsTotal;
        @SerializedName("applicant_name")
        public String applicantName;

        public List<ApcntAssetsDetail> getApcntAssetsDetails() {
            return apcntAssetsDetails;
        }

        public void setApcntAssetsDetails(List<ApcntAssetsDetail> apcntAssetsDetails) {
            this.apcntAssetsDetails = apcntAssetsDetails;
        }

        public ApcntAssetsTotal getApcntAssetsTotal() {
            return apcntAssetsTotal;
        }

        public void setApcntAssetsTotal(ApcntAssetsTotal apcntAssetsTotal) {
            this.apcntAssetsTotal = apcntAssetsTotal;
        }

        public String getApplicantName() {
            return applicantName;
        }

        public void setApplicantName(String applicantName) {
            this.applicantName = applicantName;
        }

    }

    public class ApcntAssetsDetail {

        @Expose
        public String name;
        @Expose
        public Total total;
        @Expose
        public List<Value> values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Total getTotal() {
            return total;
        }

        public void setTotal(Total total) {
            this.total = total;
        }

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

    }

    public class ApcntAssetsTotal {

        @SerializedName("TotalAmount")
        public String totalAmount;
        @SerializedName("TotalHoldingPercentage")
        public Double totalHoldingPercentage;

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Double getTotalHoldingPercentage() {
            return totalHoldingPercentage;
        }

        public void setTotalHoldingPercentage(Double totalHoldingPercentage) {
            this.totalHoldingPercentage = totalHoldingPercentage;
        }

    }

}
