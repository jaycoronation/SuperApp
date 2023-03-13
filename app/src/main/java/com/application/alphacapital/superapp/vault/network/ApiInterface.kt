package com.application.alphacapital.superapp.vault.network

import com.alphaestatevault.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface
{

    @POST("user/signin")
    @FormUrlEncoded
    fun signInCall(@Field("username") username: String,
                    @Field("password") password: String): Call<LoginResponse>

    @POST("user/forgot_password")
    @FormUrlEncoded
    fun forgotPassword(@Field("email") email: String): Call<SignUpResponse>

    @POST("user/signup")
    @FormUrlEncoded
    fun signup(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("mobile") mobile: String,
                    @Field("password") password: String,
                    @Field("country") country: String,
                    @Field("state") state: String,
                    @Field("city") city: String): Call<SignUpResponse>

    @POST("user/social_signin")
    @FormUrlEncoded
    fun socialLogin(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("mobile") mobile: String,
                    @Field("login_type") login_type: String): Call<LoginResponse>



    @POST("user/userProfile")
    @FormUrlEncoded
    fun getUserDetails(@Field("user_id") user_id: String): Call<UserProfileDetailsResponse>

    @POST("user/updateProfile")
    @FormUrlEncoded
    fun updateProfileCall(
                    @Field("user_id") user_id : String,
                    @Field("name") name: String,
                    @Field("email") email: String,
                    @Field("mobile") mobile: String,
                    @Field("country") country: String,
                    @Field("state") state: String,
                    @Field("city") city: String,
                    @Field("from_app") from_app :String): Call<SignUpResponse>

    @POST("user/uploadProfilePic")
    @Multipart
    fun updateProfileImage(@Part user_id: MultipartBody.Part?,
                           @Part from_app: MultipartBody.Part?,
                           @Part profile_pic: MultipartBody.Part?): Call<UpdateProfileResponse>

    @POST("user/changePassword")
    @FormUrlEncoded
    fun changePasswordCall(
        @Field("current_password") current_password : String,
        @Field("password") password: String,
        @Field("user_id") user_id: String): Call<SignUpResponse>

    @POST("holders")
    fun getHolders(): Call<List<String>>

    @POST("get_countries")
    fun getCountriesList(): Call<CountriesResponse>

    @POST("get_states")
    @FormUrlEncoded
    fun getStateList(@Field("country_id") country_id: String): Call<StateResponse>

    @POST("get_cities")
    @FormUrlEncoded
    fun getCityList(@Field("state_id") state_id: String): Call<CityResponse>


    @POST("generate_pdf")
    @FormUrlEncoded
    fun shareDataCall(
        @Field("email_addresses") email_addresses : String,
        @Field("password") password : String,
        @Field("user_id") user_id: String): Call<ShareResponse>

    @POST("constitution_values/list")
    @FormUrlEncoded
    fun getConstitutionValuesCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<ConstitutionListResponse>

    @POST("constitution_values/delete")
    @FormUrlEncoded
    fun deleteconstitution_valuesCall(
        @Field("id") id : String): Call<SignUpResponse>


    @POST("constitution_values/save")
    @FormUrlEncoded
    fun constitution_valuesSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<SignUpResponse>

    @POST("account_holders/list")
    @FormUrlEncoded
    fun getaccountHoldersCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<AccountHolderListResponse>


    @POST("account_holders/delete")
    @FormUrlEncoded
    fun deleteaccountHoldersCall(
        @Field("holder_id") holder_id : String): Call<SignUpResponse>

    @POST("account_holders/save")
    @FormUrlEncoded
    fun accountHoldersSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<SignUpResponse>


    @POST("notification/list")
    @FormUrlEncoded
    fun getDeathnotificationCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<DeathNotificationListResponse>


    @POST("notification/delete")
    @FormUrlEncoded
    fun deleteDeathnotificationCall(
        @Field("notification_id") notification_id : String): Call<SignUpResponse>

    @POST("notification/save")
    @FormUrlEncoded
    fun notificationSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<SignUpResponse>

    /*Advisor*/
    @POST("advisers/save")
    @FormUrlEncoded
    fun advisorSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("advisers/list")
    @FormUrlEncoded
    fun getAdviserCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<AdviserListResponse>

    @POST("advisers/delete")
    @FormUrlEncoded
    fun deleteAdviserCall(
        @Field("adviser_id") notification_id : String): Call<SignUpResponse>

    /*Keys to residence*/
    @POST("keys_to_residences/save")
    @FormUrlEncoded
    fun residenceSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("keys_to_residences/list")
    @FormUrlEncoded
    fun getResidenceCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<ResidenceListResponse>

    @POST("keys_to_residences/delete")
    @FormUrlEncoded
    fun deleteResidenceCall(
        @Field("keys_to_residences_id") notification_id : String): Call<SignUpResponse>

    /*SafeDepoit*/
    @POST("safe_deposit_boxes/save")
    @FormUrlEncoded
    fun safeDepositSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("safe_deposit_boxes/list")
    @FormUrlEncoded
    fun getSafeDepositCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<SafeDepositListResponse>

    @POST("safe_deposit_boxes/delete")
    @FormUrlEncoded
    fun deleteSafeDepositCall(
        @Field("safe_deposit_box_id") safe_deposit_box_id : String): Call<SignUpResponse>

    /*Important Document*/
    @POST("important_documents/save")
    @Multipart
    fun impDocSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>): Call<CommonResponse>

    @POST("important_documents/list")
    @FormUrlEncoded
    fun getImpDocCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<ImpDocListResponse>

    @POST("important_documents/delete")
    @FormUrlEncoded
    fun deleteImpDocCall(
        @Field("document_id") safe_deposit_box_id : String): Call<SignUpResponse>



    /*Generally*/
    @POST("generally/detail")
    @FormUrlEncoded
    fun getGenerallyDetail(
        @Field("user_id") user_id : String): Call<JsonObject>

    @POST("generally/save")
    @FormUrlEncoded
    fun generallySaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>

    /*Minor Children Adult Dependents*/
    @POST("minor_children_adult_dependents/detail")
    @FormUrlEncoded
    fun getMinorChildrenAdultDependentsDetail(
        @Field("user_id") user_id : String): Call<JsonObject>

    @POST("minor_children_adult_dependents/save")
    @FormUrlEncoded
    fun minorChildrenAdultDependentSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>


    /*Will*/
    @POST("will/detail")
    @FormUrlEncoded
    fun getWillDetail(
        @Field("user_id") user_id : String): Call<JsonObject>

    @POST("will/save")
    @Multipart
    fun willDSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part profile_pic: MultipartBody.Part?): Call<CommonResponse>


    @POST("will/save")
    @Multipart
    fun willDSaveCallNew(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part): Call<CommonResponse>


    /*Business(es)*/
    @POST("businesses/detail")
    @FormUrlEncoded
    fun getBusinessesDetail(
        @Field("user_id") user_id : String): Call<BusinessDetailResponse>

    @POST("businesses/save")
    @FormUrlEncoded
    fun businessesSaveCall(
        @Field("own_or_jointly_business") own_or_jointly_business : String,
        @Field("document_stating_your_wishes") document_stating_your_wishes : String,
        @Field("document_instructions") document_instruction : String,
        @Field("items") items : String,
        @Field("user_id") user_id: String): Call<CommonResponse>

    /*Domestic Employees*/
    @POST("domestic_employees/detail")
    @FormUrlEncoded
    fun getDomesticEmployeesDetail(
        @Field("user_id") user_id : String): Call<JsonObject>

    @POST("domestic_employees/save")
    @FormUrlEncoded
    fun domesticEmployeesSaveCall(@Field("items") items : String,
                                  @Field("user_id") user_id: String): Call<CommonResponse>

    @POST("domestic_employees/save")
    @FormUrlEncoded
    fun domesticEmployeesUpdateCall(
        @Field("is_domestic_employee") is_domestic_employee : String,
        @Field("employee_instruction") employee_instruction : String,
        @Field("domestic_employees_id") domestic_employees_id : String,
        @Field("user_id") user_id: String): Call<CommonResponse>


    /*Government Related*/
    @POST("government_related/save")
    @Multipart
    fun govRelatedSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("government_related/list")
    @FormUrlEncoded
    fun getGovRelatedCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<GovRelatedListResponse>

    @POST("government_related/delete")
    @FormUrlEncoded
    fun deleteGovRelatedCall(
        @Field("government_related_id") government_related_id : String): Call<CommonResponse>




    /*Insurance Policies*/
    @POST("insurance_policies/save")
    @Multipart
    fun insurancePolicySaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("insurance_policies/list")
    @FormUrlEncoded
    fun getInsurancePoliciesCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<InsurancePoliciesListResponse>

    @POST("insurance_policies/delete")
    @FormUrlEncoded
    fun deleteInsurancePoliciesCall(
        @Field("insurance_policies_id") insurance_policies_id : String): Call<CommonResponse>




    /*Financial Institution Accounts*/
    @POST("financial_institution_accounts/save")
    @Multipart
    fun financialInstitutionAccountsSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("financial_institution_accounts/list")
    @FormUrlEncoded
    fun getFinancialInstitutionAccountCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<FinancialInstitutionAccountsResponse>

    @POST("financial_institution_accounts/delete")
    @FormUrlEncoded
    fun deleteFinancialInstitutionAccountCall(
        @Field("financial_institution_account_id") financial_institution_account_id : String): Call<CommonResponse>



    /*Real Property*/
    @POST("real_property/save")
    @Multipart
    fun realPropertySaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("real_property/list")
    @FormUrlEncoded
    fun getRealPropertyCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<RealPropertyResponse>

    @POST("real_property/delete")
    @FormUrlEncoded
    fun deleteRealPropertyCall(
        @Field("real_property_id") real_property_id : String): Call<CommonResponse>


    /*Other Assets*/
    @POST("other_assets/save")
    @FormUrlEncoded
    fun otherAssetsSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>


    @POST("other_assets/list")
    @FormUrlEncoded
    fun getOtherAssetsCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<OtherAssetsResponse>

    @POST("other_assets/delete")
    @FormUrlEncoded
    fun deleteOtherAssetsCall(
        @Field("other_asset_id") real_property_id : String): Call<CommonResponse>


    /*Employment Related*/
    @POST("employment_related/save")
    @FormUrlEncoded
    fun empRelatedSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>

    @POST("employment_related/list")
    @FormUrlEncoded
    fun getEmpRelatedCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<EmpRelatedListResponse>

    @POST("employment_related/delete")
    @FormUrlEncoded
    fun deleteEmpRelatedCall(
        @Field("employment_related_id") employment_related_id : String): Call<CommonResponse>

    /*Investment Trust Accounts*/
    @POST("investment_trust_accounts/save")
    @Multipart
    fun investTrustAccountSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>

    @POST("investment_trust_accounts/list")
    @FormUrlEncoded
    fun getInvestTrustAccountCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<InvestTrustAccountListResponse>

    @POST("investment_trust_accounts/delete")
    @FormUrlEncoded
    fun deleteInvestTrustAccountCall(
        @Field("investment_trust_accounts") investment_trust_accounts : String): Call<CommonResponse>


    //mutual_funds
    @POST("mutual_funds/list")
    @FormUrlEncoded
    fun getmutual_fundsCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<MutualFundsListResponse>

    @POST("mutual_funds/delete")
    @FormUrlEncoded
    fun deletemutual_fundsCall(
        @Field("mutual_funds_id") mutual_funds_id : String): Call<CommonResponse>

    @POST("mutual_funds/save")
    @Multipart
    fun mutual_fundsSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    //Share Bonds
    @POST("shares_bonds/list")
    @FormUrlEncoded
    fun getshares_bondsCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<ShareBondsListResponse>

    @POST("shares_bonds/delete")
    @FormUrlEncoded
    fun deleteshares_bondsCall(
        @Field("shares_bonds_id") shares_bonds_id : String): Call<CommonResponse>

    @POST("shares_bonds/save")
    @Multipart
    fun shares_bondsSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    /*Intellectual Property*/
    @POST("intellectual_property/save")
    @Multipart
    fun intellectualPropertySaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>

    @POST("intellectual_property/list")
    @FormUrlEncoded
    fun getIntellectualPropertyCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<IntellectualPropertyListResponse>

    @POST("intellectual_property/delete")
    @FormUrlEncoded
    fun deleteIntellectualPropertyCall(
        @Field("intellectual_property_id") intellectual_property_id : String): Call<CommonResponse>

    /*Assets not in possession*/
    @POST("assets_not_in_possession/save")
    @FormUrlEncoded
    fun assetsNotPossessionSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>

    @POST("assets_not_in_possession/list")
    @FormUrlEncoded
    fun getAssetsNotPossessionCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<AssetsNotPossessionListResponse>

    @POST("assets_not_in_possession/delete")
    @FormUrlEncoded
    fun deleteAssetsNotPossessionCall(
        @Field("possession_id") possession_id : String): Call<CommonResponse>


    /* Credit Cards, Personal Loan, Home Loan*/
    @POST("credit_cards_and_loans/save")
    @Multipart
    fun creditCardsAndLoansSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("credit_cards_and_loans/list")
    @FormUrlEncoded
    fun getCreditCardsAndLoansCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<CreditCardsAndLoansResponse>

    @POST("credit_cards_and_loans/delete")
    @FormUrlEncoded
    fun deletecreditCardsAndLoansCall(
        @Field("credit_cards_and_loan_id") credit_cards_and_loan_id : String): Call<CommonResponse>



    /*Rformer_spouse_of_former_marriages*/
    @POST("former_spouse_of_former_marriages/save")
    @FormUrlEncoded
    fun formerSpouseOfFormerMarriagesSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>


    @POST("former_spouse_of_former_marriages/list")
    @FormUrlEncoded
    fun getformerSpouseOfFormerMarriagesCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<FormerSpouseOfFormerMarriagesResponse>

    @POST("former_spouse_of_former_marriages/delete")
    @FormUrlEncoded
    fun deleteformerSpouseOfFormerMarriagesCall(
        @Field("former_spouse_id") former_spouse_id : String): Call<CommonResponse>


    /*Charity-Related Obligations*/
    @POST("charity_related_obligation/save")
    @FormUrlEncoded
    fun charity_related_obligationSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>


    @POST("charity_related_obligation/list")
    @FormUrlEncoded
    fun getcharity_related_obligationCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<CharityRelatedObligationsResponse>

    @POST("charity_related_obligation/delete")
    @FormUrlEncoded
    fun deletecharity_related_obligationCall(
        @Field("charity_related_obligation_id") charity_related_obligation_id : String): Call<CommonResponse>

    /* Fiduciary Oobligations*/
    @POST("fiduciary_obligations/save")
    @FormUrlEncoded
    fun fiduciary_obligationsSaveCall(
        @Field("items") items : String,
        @Field("user_id") user_id : String) : Call<CommonResponse>


    @POST("fiduciary_obligations/list")
    @FormUrlEncoded
    fun getfiduciary_obligationsCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<FiduciaryObligationsResponse>

    @POST("fiduciary_obligations/delete")
    @FormUrlEncoded
    fun deletefiduciary_obligationsCall(
        @Field("fiduciary_obligation_id") fiduciary_obligation_id : String): Call<CommonResponse>


    /*  Other Debts*/
    @POST("other_debts/save")
    @Multipart
    fun other_debtsSaveCall(
        @Part items : MultipartBody.Part,
        @Part user_id : MultipartBody.Part,
        @Part from_app : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Call<CommonResponse>


    @POST("other_debts/list")
    @FormUrlEncoded
    fun getother_debtsCall(
        @Field("user_id") user_id : String,
        @Field("search_string") search_string: String,
        @Field("order_by") order_by: String,
        @Field("order") order: String,
        @Field("limit") limit: String,
        @Field("page") page: String): Call<OtherDebtsResponse>

    @POST("other_debts/delete")
    @FormUrlEncoded
    fun deleteother_debtsCall(
        @Field("other_debt_id") other_debt_id : String): Call<CommonResponse>
}
