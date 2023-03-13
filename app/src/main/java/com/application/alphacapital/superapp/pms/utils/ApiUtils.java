package com.application.alphacapital.superapp.pms.utils;

public class ApiUtils
{
    //Main Live Server
    public static String API_DOMAIN = "http://alphacapital.coronation.in/api/services/";

    public static String API_TOKEN_ID = "V3A0-D7I7-L3A0-L7M7" ;
    public static String END_USER_ID = "" ;
    public static String CID = "" ;

    //This Project
    public static String LOGIN = API_DOMAIN + "user/login";
    public static String GET_ALL_USERS = API_DOMAIN + "user/list";
    public static String GET_USER_DETAILS_BY_USERID = API_DOMAIN + "user/userById" ;
    public static String UPDATE_PROFILE = API_DOMAIN + "user/update";
    public static String GET_ONE_DAY_CHANGE = API_DOMAIN + "OneDayChange";
    public static String GET_NETWORTH = API_DOMAIN + "networth";
    public static String GET_ALLOCATION_FUND_SCHEMES = API_DOMAIN + "AllocationFundSchemes";
    public static String GET_MY_JOURNEY = API_DOMAIN + "MyJourneyDetail";
    public static String GET_DIVIDEND_SUMMARY = API_DOMAIN + "dividendSummary";
    public static String GET_FOLIO_NUMBER = API_DOMAIN + "FolioNosDetails";
    public static String GET_PORTFOLIO = API_DOMAIN + "portfolio";
    public static String GET_PORTFOLIO_DETAILS = API_DOMAIN + "portfolioDetails";
    public static String GET_CAPITAL_GAIN = API_DOMAIN + "capital_gain";
    public static String CAPITAL_GAIN_SUMMURY = API_DOMAIN + "capital_gain_summury";
    public static String GET_YEARS = API_DOMAIN + "common_years";
    public static String GET_BS_YEARS = API_DOMAIN + "cg_years";
    public static String GET_BS_MOVEMENT = API_DOMAIN + "bs_movement";
    public static String GET_SIP_STP = API_DOMAIN + "sip_stp";
    public static String GET_PERFORMANCE = API_DOMAIN + "performance";
    public static String GET_XIRR = API_DOMAIN + "xirr";
    public static String GET_APPLICANTS_LIST = API_DOMAIN + "get_applicants/list";
    public static String GET_NETWORTH_APPLICANTS_LIST = API_DOMAIN + "get_applicants_list/list";

    public static String APPVERSION_GETDETAIL = API_DOMAIN + "AppVersion_GetDetail";
}
