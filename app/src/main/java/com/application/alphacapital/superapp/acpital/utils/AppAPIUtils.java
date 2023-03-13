package com.application.alphacapital.superapp.acpital.utils;

public class AppAPIUtils
{
    public static String TOKEN_ID = "@alpha-capital-123-4$";

    public static String MAIN_DOMAIN = "http://demo1.coronation.in/alphacapitalapp/";
    public static String API_DOMAIN = MAIN_DOMAIN + "webservice/AlphaCapitalAppService.asmx/";


    public static String GET_APP_VERSION = API_DOMAIN + "GetAppVersionInfo";
    public static String GET_MENU_TAB_BY_ID = API_DOMAIN + "GetMenuTabById";
    public static String GET_DETAILS_BY_MENU_ID = API_DOMAIN + "GetDetailsByMenuId";
    public static String GET_ALL_AWARDS = API_DOMAIN + "GetAllAwardDetailsByMenuId";
    public static String GET_ALL_RESEARCH = API_DOMAIN + "GetAllResearchDetailsByMenuId";
    public static String GET_ALL_LOCATIONS = API_DOMAIN + "GetAllContactDetailsByMenuId";
    public static String GET_OUR_VISION = API_DOMAIN + "GetAllOurVision";
    public static String GET_MANAGEMENT_TEAM = API_DOMAIN + "GetAllManagementTeam";
    public static String GET_MULTI_FAMILY_FAQ = API_DOMAIN + "GetAllMultiFamilyFAQ";
    public static String CONTACT_SUBMIT = API_DOMAIN + "SendContactMail";
}
