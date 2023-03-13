package com.application.alphacapital.superapp.acpital.network

import com.application.alphacapital.superapp.acpital.model.AppVersionResponseModel
import com.application.alphacapital.superapp.acpital.model.LoginResponseModel
import com.application.alphacapital.superapp.acpital.model.MenuTabResponseModel
import retrofit2.Call
import retrofit2.http.*

interface CapitalApiInterface
{

    @POST("http://m.investwell.in/hc/logincheck.jsp?bid=10250")
    fun login(@QueryMap param: MutableMap<String,String>): Call<LoginResponseModel>

    @POST("http://m.investwell.in/hc/setuser.jsp?bid=10250")
    fun signup(@Query("&name=") name: String, @Query("&mobile=") mobile : String,@Query("&email=") email: String): Call<LoginResponseModel>

    @POST("GetAppVersionInfo")
    @FormUrlEncoded
    fun getAppVersion(@Field("ApiTokenId") ApiTokenId: String,
                      @Field("IsAdnroid") IsAdnroid: String,
                      @Field("IsIOS") IsIOS: String): Call<AppVersionResponseModel>

    @POST("GetMenuTabById")
    @FormUrlEncoded
    fun getMenuTabs(@Field("ApiTokenId") ApiTokenId: String,
                    @Field("Id") Id: String):Call<MenuTabResponseModel>

}
