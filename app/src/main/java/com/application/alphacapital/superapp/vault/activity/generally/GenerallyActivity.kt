package com.application.alphacapital.superapp.vault.activity.generally

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.GenerallyDetailsResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityGenerallyBinding
import com.google.gson.JsonObject
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GenerallyActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityGenerallyBinding
    lateinit var generallyDetailsResponse: GenerallyDetailsResponse.Generally
    var generally_id: String = ""
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_generally)
        setStatusBarGradiant(activity)
        holder_id = intent.getStringExtra("holder_id").toString()
        holder_name = intent.getStringExtra("holder_name").toString()
        holder_userName = intent.getStringExtra("holder_userName").toString()
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Medical & Funeral"

        binding.txtHolderName.text = holder_userName

        if (holder_name == "A")
        {
            binding.txtMandatory.visibility = View.VISIBLE
        }
        else
        {
            binding.txtMandatory.visibility = View.GONE
        }

        try
        {
            appUtils.removeError(binding.edtDirectionsMedicalNursing, binding.ipDirectionsMedicalNursing)
            appUtils.removeError(binding.edtOrganDonorTransplantationWishes, binding.ipOrganDonorTransplantationWishes)
            appUtils.removeError(binding.edtInstructionsAboutFuneral, binding.ipInstructionsAboutFuneral)
        }
        catch (e: Exception)
        {
        }

        if (isOnline())
        {
            getGenerallyDetailData()
        }
        else
        {
            noInternetToast()
        }
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (holder_name == "A")
            {
                if (binding.edtDirectionsMedicalNursing.text!!.isEmpty())
                {
                    binding.ipDirectionsMedicalNursing.error = "Please enter Directions about Medical or Nursing Home Care."
                }
                else if (binding.edtOrganDonorTransplantationWishes.text!!.isEmpty())
                {
                    binding.ipOrganDonorTransplantationWishes.error = "Please enter Your wishes about Organ Donation."
                }
                else if (binding.edtInstructionsAboutFuneral.text!!.isEmpty())
                {
                    binding.ipInstructionsAboutFuneral.error = "Please enter Instructions regarding your Funeral, Memorial Services, Disposition of Remains, etc."
                }
                else
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (binding.edtDirectionsMedicalNursing.text!!.isNotEmpty() && binding.edtOrganDonorTransplantationWishes.text!!.isNotEmpty() && binding.edtInstructionsAboutFuneral.text!!.isNotEmpty())
                        {
                            if (generally_id.length > 0)
                            {
                                jGroup.put("generally_id", generally_id)
                            }
                            jGroup.put("directions_about_medical_and_nursing", binding.edtDirectionsMedicalNursing.text.toString().trim())
                            jGroup.put("organ_donor_transplantation_wishes", binding.edtOrganDonorTransplantationWishes.text.toString().trim())
                            jGroup.put("instructions_about_funeral", binding.edtInstructionsAboutFuneral.text.toString().trim())
                        }

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addGenerallyData(rootObject.toString())
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
            }
            else
            {
                if (binding.edtDirectionsMedicalNursing.text!!.isEmpty() && binding.edtOrganDonorTransplantationWishes.text!!.isEmpty() && binding.edtInstructionsAboutFuneral.text!!.isEmpty())
                {

                }
                else if (binding.edtDirectionsMedicalNursing.text!!.isNotEmpty() && binding.edtOrganDonorTransplantationWishes.text!!.isNotEmpty() && binding.edtInstructionsAboutFuneral.text!!.isNotEmpty())
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (generally_id.length > 0)
                        {
                            jGroup.put("generally_id", generally_id)
                        }
                        jGroup.put("directions_about_medical_and_nursing", binding.edtDirectionsMedicalNursing.text.toString().trim())
                        jGroup.put("organ_donor_transplantation_wishes", binding.edtOrganDonorTransplantationWishes.text.toString().trim())
                        jGroup.put("instructions_about_funeral", binding.edtInstructionsAboutFuneral.text.toString().trim())

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addGenerallyData(rootObject.toString())
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
                else
                {
                    showToast("Please Enter or Clear all fields value.")
                }
            }
        }
    }

    private fun getGenerallyDetailData()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getGenerallyDetail(sessionManager.userId)

                call.enqueue(object : Callback<JsonObject>
                {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>)
                    {
                        if (response.isSuccessful)
                        {
                            Log.e("<><> RES : ", response.body().toString() + "")
                            val jsonObject: JSONObject? = JSONObject(response.body().toString())
                            var message: String = ""
                            if (jsonObject!!.has("success"))
                            {
                                if (jsonObject.has("message"))
                                {
                                    message = jsonObject.getString("message")
                                }

                                if (jsonObject!!.getInt("success") == 1)
                                {
                                    if (jsonObject.has("generally"))
                                    {
                                        val jsonObjectgenerally = jsonObject.getJSONObject("generally")

                                        if (jsonObjectgenerally.has(holder_id))
                                        {
                                            val jsonObjectData = jsonObjectgenerally.getJSONObject(holder_id)
                                            generallyDetailsResponse = GenerallyDetailsResponse.Generally()
                                            generallyDetailsResponse.generally_id = jsonObjectData.getString("generally_id")
                                            generallyDetailsResponse.directions_about_medical_and_nursing = jsonObjectData.getString("directions_about_medical_and_nursing")
                                            generallyDetailsResponse.organ_donor_transplantation_wishes = jsonObjectData.getString("organ_donor_transplantation_wishes")
                                            generallyDetailsResponse.instructions_about_funeral = jsonObjectData.getString("instructions_about_funeral")
                                            generally_id = generallyDetailsResponse.generally_id
                                            binding.edtDirectionsMedicalNursing.setText(generallyDetailsResponse.directions_about_medical_and_nursing)
                                            binding.edtOrganDonorTransplantationWishes.setText(generallyDetailsResponse.organ_donor_transplantation_wishes)
                                            binding.edtInstructionsAboutFuneral.setText(generallyDetailsResponse.instructions_about_funeral)
                                        }
                                    }
                                }
                                else
                                {
                                    if(message.length >0)
                                    {
                                       // showToast(message)
                                    }
                                    else
                                    {
                                        apiFailedToast()
                                    }
                                }
                            }
                            else
                            {
                                apiFailedToast()
                            }
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                    }
                })
            }
            else
            {
                noInternetToast()
            }
        }
        catch (e: Exception)
        {
        }
    }

    private fun addGenerallyData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE

                val call = apiService.generallySaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                finish()
                                finishActivityAnimation()
                            }
                            else
                            {
                                toast(response.body()!!.message)
                            }
                        }
                        else
                        {
                            apiFailedToast()

                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                    }
                })
            }
            else
            {
                noInternetToast()
            }
        }
        catch (e: Exception)
        {
        }
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
