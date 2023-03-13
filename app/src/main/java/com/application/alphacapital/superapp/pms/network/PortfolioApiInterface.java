package com.application.alphacapital.superapp.pms.network;


import com.application.alphacapital.superapp.pms.beans.AppicantiListResponseModel;
import com.application.alphacapital.superapp.pms.beans.ApplicantListResponseModel;
import com.application.alphacapital.superapp.pms.beans.ApplicationFundSchemesResponseModel;
import com.application.alphacapital.superapp.pms.beans.BSMovementResponseModel;
import com.application.alphacapital.superapp.pms.beans.BsYearsResponseModel;
import com.application.alphacapital.superapp.pms.beans.CapitalGainReportResponseModel;
import com.application.alphacapital.superapp.pms.beans.CapitalGainResponseModel;
import com.application.alphacapital.superapp.pms.beans.DividendSummaryResponseModel;
import com.application.alphacapital.superapp.pms.beans.FolioNumberResponseModel;
import com.application.alphacapital.superapp.pms.beans.Last30DaysDataResponse;
import com.application.alphacapital.superapp.pms.beans.LoginResponseModel;
import com.application.alphacapital.superapp.pms.beans.MyJourneyResponseModel;
import com.application.alphacapital.superapp.pms.beans.NetworthResponseModel;
import com.application.alphacapital.superapp.pms.beans.NetworthTempData;
import com.application.alphacapital.superapp.pms.beans.OneDayChangeResponseModel;
import com.application.alphacapital.superapp.pms.beans.PerfomanceResponseModel;
import com.application.alphacapital.superapp.pms.beans.PortfoliaResponseModel;
import com.application.alphacapital.superapp.pms.beans.PortfolioDetailsResponseModel;
import com.application.alphacapital.superapp.pms.beans.SIPSTPResponseModel;
import com.application.alphacapital.superapp.pms.beans.UserListResponseModel;
import com.application.alphacapital.superapp.pms.beans.XIRRResponseModel;
import com.application.alphacapital.superapp.vault.model.GetPercentageResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PortfolioApiInterface
{
    @POST("user/login")
    @FormUrlEncoded
    Call<LoginResponseModel> loginAPI(@Field("email") String email, @Field("password") String password);

    @POST("user/list")
    @FormUrlEncoded
    Call<UserListResponseModel> getAllUserAPI(@Field("search_string") String search_string, @Field("admin_id") String admin_id);

    @POST("OneDayChange")
    @FormUrlEncoded
    Call<OneDayChangeResponseModel> getOneDayChangeAPI(@Field("user_id") String user_id);

    @POST("FolioNosDetails")
    @FormUrlEncoded
    Call<FolioNumberResponseModel> getFolioNumberAPI(@Field("user_id") String user_id);

    @POST("networth_new")
    @FormUrlEncoded
    Call<NetworthResponseModel> getNetworthAPI(@Field("user_id") String user_id);

    @POST("networth_new")
    @FormUrlEncoded
    Call<NetworthTempData> getNetworthNewAPIData(@Field("user_id") String user_id);

    @GET("settings/get_percentage")
    Call<GetPercentageResponse> getPercentage();

    @POST("dividendSummary")
    @FormUrlEncoded
    Call<DividendSummaryResponseModel> getDividendAPI(@Field("user_id") String user_id);

    @POST("cg_years")
    Call<BsYearsResponseModel> getYearsAPI();

    @POST("common_years")
    Call<BsYearsResponseModel> getCommonYearAPI();

    @POST("bs_movement")
    @FormUrlEncoded
    Call<BSMovementResponseModel> getBSMovementAPI(@Field("user_id") String user_id, @Field("range_time") String range_time);

    @POST("capital_gain_summury")
    @FormUrlEncoded
    Call<CapitalGainResponseModel> getCapitalGainAPI(@Field("user_id") String user_id, @Field("cr_yr") String cr_yr);

    @POST("capital_gain")
    @FormUrlEncoded
    Call<CapitalGainReportResponseModel> getCapitalGainSingle(@Field("user_id") String user_id, @Field("cr_yr") String cr_yr);

    @POST("xirr")
    @FormUrlEncoded
    Call<XIRRResponseModel> getXIRRAPI(@Field("user_id") String user_id);

    @POST("xirrPrevious")
    @FormUrlEncoded
    Call<XIRRResponseModel> getxirrPreviousApi(@Field("user_id") String user_id);

    @POST("performance")
    @FormUrlEncoded
    Call<PerfomanceResponseModel> getPerfomaceAPI(@Field("user_id") String user_id);

    @POST("last_30_days_transaction_consolidated_networth")
    @FormUrlEncoded
    Call<Last30DaysDataResponse> getLast30DaysData(@Field("user_id") String user_id);

    @POST("sip_stp")
    @FormUrlEncoded
    Call<SIPSTPResponseModel> getSIPSTPAPI(@Field("user_id") String user_id, @Field("folio_type") String folio_type);

    @POST("get_applicants/list")
    @FormUrlEncoded
    Call<AppicantiListResponseModel> getAppicantsAPI(@Field("user_id") String user_id);

    @POST("portfolio")
    @FormUrlEncoded
    Call<PortfoliaResponseModel> getPortFolioAPI(@Field("user_id") String user_id, @Field("Cid") String Cid);

    @POST("portfolioDetails")
    @FormUrlEncoded
    Call<PortfolioDetailsResponseModel> getPortfolioDetailsAPI(@Field("FolioNo") String FolioNo, @Field("SCode") String SCode, @Field("SchemeName") String SchemeName, @Field("user_id") String user_id);

    @POST("get_applicants_list/list")
    @FormUrlEncoded
    Call<ApplicantListResponseModel> getAppicantListAPI(@Field("user_id") String user_id);

    @POST("AllocationFundSchemes")
    @FormUrlEncoded
    Call<ApplicationFundSchemesResponseModel> getFundSchemes(@Field("user_id") String user_id);

    @POST("MyJourneyDetail")
    @FormUrlEncoded
    Call<MyJourneyResponseModel> getJournerAPI(@Field("user_id") String user_id);

    @POST("get_applicants_list/list")
    @FormUrlEncoded
    Call<ApplicantListResponseModel> getApplicantListAPI(@Field("user_id") String user_id);
}
