package com.application.alphacapital.superapp.finplan.network

import com.alphafinancialplanning.model.*
import com.application.alphacapital.superapp.finplan.model.*
import retrofit2.Call
import retrofit2.http.*

interface FinPlanApiInterface
{

    @POST("user/signin")
    @FormUrlEncoded
    fun signIn(@Field("username") username: String, @Field("password") password: String): Call<LoginResponse>

    @POST("user/signup")
    @FormUrlEncoded
    fun signup(@Field("first_name") first_name: String, @Field("last_name") last_name: String, @Field("email") email: String, @Field("mobile") mobile: String, @Field("dob") dob: String, @Field("password") password: String): Call<CommonResponse>

    @POST("user/changePassword")
    @FormUrlEncoded
    fun changePassword(@Field("current_password") current_password: String, @Field("password") password: String, @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("user/userProfile")
    @FormUrlEncoded
    fun getUserProfile(@Field("user_id") user_id: String): Call<UserProfileResponse>

    @POST("user/updateProfile")
    @FormUrlEncoded
    fun updatePRofile(@Field("user_id") user_id: String, @Field("first_name") first_name: String, @Field("last_name") last_name: String, @Field("email") email: String, @Field("mobile") mobile: String, @Field("dob") dob: String, @Field("retirement_age") retirement_age: String, @Field("life_expectancy") life_expectancy: String, @Field("tax_slab") tax_slab: String, @Field("risk_profile") risk_profile: String, @Field("time_horizon") time_horizon: String, @Field("amount_invested") amount_invested: String): Call<UserProfileResponse>

    @POST("user/uploadProfilePic")
    @FormUrlEncoded
    fun uploadProfilePic(@Field("username") username: String, @Field("password") password: String): Call<Any>

    @POST("user/forgot_password")
    @FormUrlEncoded
    fun forgotPassword(@Field("email") email: String): Call<CommonResponse>

    @POST("risk_profiler")
    fun getRiskProfileQueAns(): Call<RiskProfileQueAndResponse>

    @POST("get_risk_profile")
    @FormUrlEncoded
    fun getRiskProfile(@Field("answer_ids") answer_ids: String, @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("investment_types")
    fun getInvestmentTypes(): Call<InvestmentTypeResponse>

    @POST("liabilities")
    fun getLiabilitiesType(): Call<LiabilitiesTypeResponse>

    @POST("aspiration_types")
    fun getAspirationTypes(): Call<AspirationTypeResponse>

    /*Existing Assets*/
    @POST("existing_assets/save")
    @FormUrlEncoded
    fun saveExistingAssets(@Field("user_id") user_id: String, @Field("investment_type") investment_type: String, @Field("asset_type") asset_type: String, @Field("current_value") current_value: String, @Field("existing_assets_id") existing_assets_id: String): Call<CommonResponse>

    @POST("existing_assets/list")
    @FormUrlEncoded
    fun getExistingAssets(@Field("user_id") user_id: String): Call<ExistingAssetsListResponse>

    @POST("existing_assets/delete")
    @FormUrlEncoded
    fun deleteExistingAssets(@Field("existing_assets_id") existing_assets_id: String): Call<CommonResponse>

    /*Existing Liabilities*/
    @POST("existing_liabilities/save")
    @FormUrlEncoded
    fun saveExistingLiabilities(@Field("user_id") user_id: String, @Field("liability_type") liability_type: String, @Field("asset_type") asset_type: String, @Field("current_value") current_value: String, @Field("existing_liability_id") existing_assets_id: String): Call<CommonResponse>

    @POST("existing_liabilities/list")
    @FormUrlEncoded
    fun getExistingLiabilities(@Field("user_id") user_id: String): Call<ExistingLiabilitiesListResponse>

    @POST("existing_liabilities/delete")
    @FormUrlEncoded
    fun deleteExistingLiabilities(@Field("existing_liability_id") existing_assets_id: String): Call<CommonResponse>

    /*Future In Flow*/
    @POST("future_inflow/save")
    @FormUrlEncoded
    fun saveFutureInFlow(@Field("user_id") user_id: String, @Field("source") source: String, @Field("start_year") start_year: String, @Field("end_year") end_year: String, @Field("expected_growth") expected_growth: String, @Field("amount") amount: String, @Field("future_inflow_id") future_inflow_id: String): Call<CommonResponse>

    @POST("future_inflow/list")
    @FormUrlEncoded
    fun getFutureInFlow(@Field("user_id") user_id: String): Call<FutureInFlowListResponse>

    @POST("future_inflow/delete")
    @FormUrlEncoded
    fun deleteFutureInFlow(@Field("future_inflow_id") future_inflow_id: String): Call<CommonResponse>

    @POST("future_inflow_list")
    @FormUrlEncoded
    fun getFutureInflowListForReport(@Field("user_id") user_id: String): Call<FutureInflowListReportResponse>

    /*Aspiration Future Expense*/
    @POST("aspiration_future_expense/save")
    @FormUrlEncoded
    fun saveAspirationFutureExpense(@Field("user_id") user_id: String, @Field("aspiration_type") aspiration_type: String, @Field("start_year") start_year: String, @Field("end_year") end_year: String, @Field("periodicity") periodicity: String, @Field("amount") amount: String, @Field("aspiration_id") aspiration_id: String): Call<CommonResponse>

    @POST("aspiration_future_expense/list")
    @FormUrlEncoded
    fun getAspirationFutureExpense(@Field("user_id") user_id: String): Call<AspirationFutureExpenseListResponse>

    @POST("aspiration_future_expense/delete")
    @FormUrlEncoded
    fun deleteAspireFutureExpense(@Field("aspiration_id") aspiration_id: String): Call<CommonResponse>


    /*Aspiration list for report*/
    @POST("aspirations")
    @FormUrlEncoded
    fun getAspirationList(@Field("user_id") user_id: String): Call<AspirationListReportResponse>

    @POST("networth")
    @FormUrlEncoded
    fun getNetWorth(@Field("user_id") user_id: String): Call<NetWorthListResponse>

    @POST("asset_allocation_micro")
    @FormUrlEncoded
    fun getAssetAllocationMicro(@Field("user_id") user_id: String): Call<AssetAllocationMicroListResponse>

    @POST("asset_allocation_macro")
    @FormUrlEncoded
    fun getAssetAllocationMacro(@Field("user_id") user_id: String): Call<AssetAllocationMacroListResponse>

    @POST("recommended_asset_allocation/list")
    @FormUrlEncoded
    fun getRecommendedAssetAllocation(@Field("user_id") user_id: String): Call<RecommendedAssetAllocationListResponse>

    @POST("recommended_asset_allocation/save")
    @FormUrlEncoded
    fun saveRecommendedAssetAllocation(@Field("user_id") user_id: String, @Field("items") items: String): Call<CommonResponse>

    @POST("assets_classes")
    fun getAssetClassesList(): Call<AssetsClassesListResponse>

    @POST("variance_analysis_micro_strategic")
    @FormUrlEncoded
    fun getVarianceAnalysisMicroStrategic(@Field("user_id") user_id: String): Call<VarianceAnalysisMicroListResponse>

    @POST("variance_analysis_micro_tactical")
    @FormUrlEncoded
    fun getVarianceAnalysisMicroTactical(@Field("user_id") user_id: String): Call<VarianceAnalysisMicroListResponse>

    @POST("variance_analysis_macro_strategic")
    @FormUrlEncoded
    fun getVarianceAnalysisMacroStrategic(@Field("user_id") user_id: String): Call<VarianceAnalysisMacroListResponse>

    @POST("variance_analysis_macro_tactical")
    @FormUrlEncoded
    fun getVarianceAnalysisMacroTactical(@Field("user_id") user_id: String): Call<VarianceAnalysisMacroListResponse>

    @POST("balance_sheet_movement")
    @FormUrlEncoded
    fun getBalanceSheetMovement(@Field("user_id") user_id: String): Call<BalanceSheetMovementListResponse>

    @POST("wealth_required")
    @FormUrlEncoded
    fun getWealthRequiredList(@Field("user_id") user_id: String): Call<WealthRequiredListResponse>

    @POST("return_of_risk")
    @FormUrlEncoded
    fun getReturnOfRiskList(@Field("user_id") user_id: String): Call<ReturnOfRiskListResponse>

    @POST("risk_profile_allocation")
    @FormUrlEncoded
    fun getRiskProfileAllocationList(@Field("user_id") user_id: String): Call<RiskProfileAllocationResponse>

    @POST("risk_profiler")
    fun getRiskProfileQuestionnaire(): Call<RiskProfileQuestionsModel>

}
