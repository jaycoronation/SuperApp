package com.application.alphacapital.superapp.supermain.network

import com.alphaestatevault.model.CommonResponse
import com.application.alphacapital.superapp.acpital.model.LoginResponseModel
import com.application.alphacapital.superapp.supermain.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface
{
    @POST("loginForSuperApp")
    @FormUrlEncoded
    fun superappLogin(@Field("username") username: String, @Field("email") email: String, @Field("password") password: String): Call<SuperLoginResponseModel>

    @POST("http://m.investwell.in/hc/logincheck.jsp?bid=10250&")
    fun inveswellLogin(@QueryMap param: MutableMap<String, String>): Call<LoginResponseModel>

    @POST("inquiries/save")
    @FormUrlEncoded
    fun callContactUsAPI(@Field("name") name: String, @Field("email") email: String, @Field("contact") contact: String, @Field("comments") comments: String, @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("additionallinks")
    fun callAdditionalAPI(): Call<AdditionalLinksResponseModel>

    @POST("updateProfileSuperApp")
    @FormUrlEncoded
    fun updateProfileAPI(
        @Field("username") username: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("dob") dob: String,
    ) : Call<CommonResponse>

    @GET("https://alphacapital.in/blog/feed/json?")
    fun getFeedJsonData(@Query("paged") paged : String) : Call<FeedResponseModel>

    @GET("https://www.alphacapital.in/youtube.php")
    fun getVideos() : Call<VideoResponseModel>

    @GET("https://alphacapital.in/blog/filter.json")
    fun getFilter() : Call<FilterResponseModel>

    @GET("https://www.alphacapital.in/blog/category/financial-planning/feed/json")
    fun getFinPlanJSON() : Call<FeedResponseModel>

    @GET("https://www.alphacapital.in/blog/category/general/feed/json")
    fun getGeneralJSON() : Call<FeedResponseModel>

    @GET("https://www.alphacapital.in/blog/category/investmentideas/feed/json")
    fun getInvestmentIdeaJSON() : Call<FeedResponseModel>

    @GET("https://www.alphacapital.in/blog/category/taxplanning/feed/json")
    fun getTaxPlanningJSON() : Call<FeedResponseModel>
}