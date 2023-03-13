package com.application.alphacapital.superapp.vault.activity.minorchildrenadultdependents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.MinorChildrenAdultDependentsResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityMinorChildrenAdultDependentsBinding
import com.google.gson.JsonObject
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MinorChildrenAdultDependentsActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityMinorChildrenAdultDependentsBinding
    lateinit var minorChildrenAdultDependentsResponse: MinorChildrenAdultDependentsResponse.MinorChildrenAdultDependents
    var mcad_id : String =""
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_minor_children_adult_dependents)
        setStatusBarGradiant(activity)
        holder_id = intent.getStringExtra("holder_id").toString()
        holder_name = intent.getStringExtra("holder_name").toString()
        holder_userName = intent.getStringExtra("holder_userName").toString()
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Dependent Children"

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
            appUtils.removeError(binding.edtMinorChildrenAndDependents, binding.ipMinorChildrenAndDependents)
            appUtils.removeError(binding.edtNameAddressPhone, binding.ipNameAddressPhone)
            appUtils.removeError(binding.edtAgreedToAssumeResponsibility, binding.ipAgreedToAssumeResponsibility)
            appUtils.removeError(binding.edtHaveYou, binding.ipHaveYou)
            appUtils.removeError(binding.edtDocumentLocated, binding.ipDocumentLocated)
            appUtils.removeError(binding.edtInstructionsDirectionsSuggestions, binding.ipInstructionsDirectionsSuggestions)
        }
        catch (e: Exception)
        {
        }

        if (isOnline())
        {
           getDataCall()
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
                if (binding.edtMinorChildrenAndDependents.text!!.isEmpty())
                {
                    binding.ipMinorChildrenAndDependents.error = "Please list minor children and/or adult dependents in your care, including their ages."
                }
                else if (binding.edtNameAddressPhone.text!!.isEmpty())
                {
                    binding.ipNameAddressPhone.error = "Please enter Name, address and phone number of prospective guardian(s) as designated in your will."
                }
                else if (binding.edtAgreedToAssumeResponsibility.text!!.isEmpty())
                {
                    binding.ipAgreedToAssumeResponsibility.error = "Please enter Has this person (or persons) agreed to assume this responsibility?."
                }
                else if (binding.edtHaveYou.text!!.isEmpty())
                {
                    binding.ipHaveYou.error = "Please enter Have you (1) discussed with this person, or (2) documented your specific goals and aspirations for, or\n" + "suggestions regarding, continuing care of any minor children or adult dependents?."
                }
                else if (binding.edtDocumentLocated.text!!.isEmpty())
                {
                    binding.ipDocumentLocated.error = "Please enter If you have prepared a document, where is this document located?."
                }
                else if (binding.edtInstructionsDirectionsSuggestions.text!!.isEmpty())
                {
                    binding.ipInstructionsDirectionsSuggestions.error = "Please enter Would you like to include here any instructions, directions or suggestions to the prospective guardian(s)?."
                }
                else
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (binding.edtMinorChildrenAndDependents.text!!.isNotEmpty() && binding.edtNameAddressPhone.text!!.isNotEmpty() &&
                            binding.edtAgreedToAssumeResponsibility.text!!.isNotEmpty() && binding.edtHaveYou.text!!.isNotEmpty() &&
                            binding.edtDocumentLocated.text!!.isNotEmpty() && binding.edtInstructionsDirectionsSuggestions.text!!.isNotEmpty())
                        {
                            if (mcad_id.length > 0)
                            {
                                jGroup.put("mcad_id", mcad_id)
                            }
                            jGroup.put("minor_children_and_dependents", binding.edtMinorChildrenAndDependents.text.toString().trim())
                            jGroup.put("name_address_phone", binding.edtNameAddressPhone.text.toString().trim())
                            jGroup.put("agreed_to_assume_responsibility", binding.edtAgreedToAssumeResponsibility.text.toString().trim())
                            jGroup.put("have_you", binding.edtHaveYou.text.toString().trim())
                            jGroup.put("document_located", binding.edtDocumentLocated.text.toString().trim())
                            jGroup.put("instructions_directions_suggestions", binding.edtInstructionsDirectionsSuggestions.text.toString().trim())
                        }

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addData(rootObject.toString())
                    }
                    else
                    {
                        noInternetToast()
                    }
                }

            }
            else
            {
                if (binding.edtMinorChildrenAndDependents.text!!.isEmpty() && binding.edtNameAddressPhone.text!!.isEmpty() &&
                    binding.edtAgreedToAssumeResponsibility.text!!.isEmpty() && binding.edtHaveYou.text!!.isEmpty() &&
                    binding.edtDocumentLocated.text!!.isEmpty() && binding.edtInstructionsDirectionsSuggestions.text!!.isEmpty())
                {

                }
                else if (binding.edtMinorChildrenAndDependents.text!!.isNotEmpty() && binding.edtNameAddressPhone.text!!.isNotEmpty() &&
                    binding.edtAgreedToAssumeResponsibility.text!!.isNotEmpty() && binding.edtHaveYou.text!!.isNotEmpty() &&
                    binding.edtDocumentLocated.text!!.isNotEmpty() && binding.edtInstructionsDirectionsSuggestions.text!!.isNotEmpty())
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (mcad_id.length > 0)
                        {
                            jGroup.put("mcad_id", mcad_id)
                        }
                        jGroup.put("minor_children_and_dependents", binding.edtMinorChildrenAndDependents.text.toString().trim())
                        jGroup.put("name_address_phone", binding.edtNameAddressPhone.text.toString().trim())
                        jGroup.put("agreed_to_assume_responsibility", binding.edtAgreedToAssumeResponsibility.text.toString().trim())
                        jGroup.put("have_you", binding.edtHaveYou.text.toString().trim())
                        jGroup.put("document_located", binding.edtDocumentLocated.text.toString().trim())
                        jGroup.put("instructions_directions_suggestions", binding.edtInstructionsDirectionsSuggestions.text.toString().trim())

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addData(rootObject.toString())
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

    private fun getDataCall()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getMinorChildrenAdultDependentsDetail(sessionManager.userId)

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
                                    if (jsonObject.has("minor_children_adult_dependents"))
                                    {
                                        val jsonObjectminor_children_adult_dependents = jsonObject.getJSONObject("minor_children_adult_dependents")

                                        if (jsonObjectminor_children_adult_dependents.has(holder_id))
                                        {
                                            val jsonObjectData = jsonObjectminor_children_adult_dependents.getJSONObject(holder_id)
                                            minorChildrenAdultDependentsResponse = MinorChildrenAdultDependentsResponse.MinorChildrenAdultDependents()
                                            minorChildrenAdultDependentsResponse.mcad_id = jsonObjectData.getString("mcad_id")
                                            minorChildrenAdultDependentsResponse.minor_children_and_dependents = jsonObjectData.getString("minor_children_and_dependents")
                                            minorChildrenAdultDependentsResponse.name_address_phone = jsonObjectData.getString("name_address_phone")
                                            minorChildrenAdultDependentsResponse.agreed_to_assume_responsibility = jsonObjectData.getString("agreed_to_assume_responsibility")
                                            minorChildrenAdultDependentsResponse.have_you = jsonObjectData.getString("have_you")
                                            minorChildrenAdultDependentsResponse.document_located = jsonObjectData.getString("document_located")
                                            minorChildrenAdultDependentsResponse.instructions_directions_suggestions = jsonObjectData.getString("instructions_directions_suggestions")
                                            mcad_id = minorChildrenAdultDependentsResponse.mcad_id
                                            binding.edtMinorChildrenAndDependents.setText(minorChildrenAdultDependentsResponse.minor_children_and_dependents)
                                            binding.edtNameAddressPhone.setText(minorChildrenAdultDependentsResponse.name_address_phone)
                                            binding.edtAgreedToAssumeResponsibility.setText(minorChildrenAdultDependentsResponse.agreed_to_assume_responsibility)
                                            binding.edtHaveYou.setText(minorChildrenAdultDependentsResponse.have_you)
                                            binding.edtDocumentLocated.setText(minorChildrenAdultDependentsResponse.document_located)
                                            binding.edtInstructionsDirectionsSuggestions.setText(minorChildrenAdultDependentsResponse.instructions_directions_suggestions)
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

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE

                val call = apiService.minorChildrenAdultDependentSaveCall(items, sessionManager.userId)

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
