package com.application.alphacapital.superapp.vault.activity.insurancepolicies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.InsurancePoliciesListResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityInsurancePoliciesListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_insurance_policies.view.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsurancePoliciesListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityInsurancePoliciesListBinding
    var listData: MutableList<InsurancePoliciesListResponse.InsurancePoliciesDetail> = mutableListOf()
    val listAdapter = ListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_insurance_policies_list)

        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when (it.what)
            {
                1 ->
                {
                    if (isOnline())
                    {
                        getListData()
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
            }
            false
        }
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Insurance Policies"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getListData()
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

        binding.ivAdd.setOnClickListener {
            accountHolderDialog()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getListData()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getListData()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getInsurancePoliciesCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<InsurancePoliciesListResponse>
                {
                    override fun onResponse(call: Call<InsurancePoliciesListResponse>, response: Response<InsurancePoliciesListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.insurance_policies_details)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                                   // gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                   // visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                }
                            }
                            else
                            {
                               // visible(binding.noData.llNoData)
                                gone(binding.rvList)
                               // showToast(response.body()!!.message)
                                accountHolderDialog()
                            }
                        }
                        else
                        {
                           // visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<InsurancePoliciesListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                       // visible(binding.noData.llNoData)
                        gone(binding.rvList)
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

    inner class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_insurance_policies, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: InsurancePoliciesListResponse.InsurancePoliciesDetail = listData[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.insurance_company.isNotEmpty())
            {
                visible(holder.itemView.txtInsuranceCompany)
                holder.itemView.txtInsuranceCompany.text = getSet.insurance_company
            }
            else
            {
                gone(holder.itemView.txtInsuranceCompany)
            }

            if (getSet.type_of_policy.isNotEmpty())
            {
                visible(holder.itemView.txtTypeOfPolicy)
                holder.itemView.txtTypeOfPolicy.text = getSet.type_of_policy
            }
            else
            {
                gone(holder.itemView.txtTypeOfPolicy)
            }

            if (getSet.nominee_name.isNotEmpty())
            {
                visible(holder.itemView.txtnominee_name)
                holder.itemView.txtnominee_name.text = getSet.nominee_name
            }
            else
            {
                gone(holder.itemView.txtnominee_name)
            }

            if (getSet.policy_number.isNotEmpty())
            {
                visible(holder.itemView.txtPolicyNumber)
                holder.itemView.txtPolicyNumber.text = getSet.policy_number
            }
            else
            {
                gone(holder.itemView.txtPolicyNumber)
            }

            if (getSet.person_thing_insured.isNotEmpty())
            {
                visible(holder.itemView.txtPersonThingInsured)
                holder.itemView.txtPersonThingInsured.text = getSet.person_thing_insured
            }
            else
            {
                gone(holder.itemView.txtPersonThingInsured)
            }

            if (getSet.sum_assured.isNotEmpty())
            {
                visible(holder.itemView.txtSumAssured)
                holder.itemView.txtSumAssured.text = getSet.sum_assured
            }
            else
            {
                gone(holder.itemView.txtSumAssured)
            }

            if (getSet.current_value.isNotEmpty())
            {
                visible(holder.itemView.txtCurrentValue)
                holder.itemView.txtCurrentValue.text = getSet.current_value
            }
            else
            {
                gone(holder.itemView.txtCurrentValue)
            }

            if (getSet.purchased_on.isNotEmpty())
            {
                visible(holder.itemView.txtPurchasedon)
                holder.itemView.txtPurchasedon.text = getSet.purchased_on
            }
            else
            {
                gone(holder.itemView.txtPurchasedon)
            }

            if (getSet.location_of_document.isNotEmpty())
            {
                visible(holder.itemView.txtLocationofdocument)
                holder.itemView.txtLocationofdocument.text = getSet.location_of_document
            }
            else
            {
                gone(holder.itemView.txtLocationofdocument)
            }

            if (getSet.notes.isNotEmpty())
            {
                visible(holder.itemView.txtNotes)
                holder.itemView.txtNotes.text = getSet.notes
            }
            else
            {
                gone(holder.itemView.txtNotes)
            }

            if (getSet.agent_name.isNotEmpty())
            {
                visible(holder.itemView.txtName)
                holder.itemView.txtName.text = getSet.agent_name
            }
            else
            {
                gone(holder.itemView.txtName)
            }

            if (getSet.agent_phone.isNotEmpty())
            {
                visible(holder.itemView.txtPhone)
                holder.itemView.txtPhone.text = getSet.agent_phone
            }
            else
            {
                gone(holder.itemView.txtPhone)
            }

            if (getSet.agent_address.isNotEmpty())
            {
                visible(holder.itemView.txtAddress)
                holder.itemView.txtAddress.text = getSet.agent_address
            }
            else
            {
                gone(holder.itemView.txtAddress)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.insurance_policies_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddInsurancePoliciesActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, InsurancePoliciesListResponse.InsurancePoliciesDetail::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }

            if (getSet.upload_doc.isNotEmpty())
            {
                visible(holder.itemView.ivDownload)
            }
            else
            {
                gone(holder.itemView.ivDownload)
            }

            holder.itemView.ivDownload.setOnClickListener {
                if(getSet.upload_doc.isNotEmpty()){
                    browse(getSet.upload_doc,false)
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Insurance Policies", "Are you sure you want to delete this details?", object : DialogActionInterface
        {
            override fun okClick()
            {
                deletelistItems(id, position)
            }

            override fun cancelClick()
            {
            }

            override fun onDismiss()
            {
            }

        }).showAlert()
    }

    private fun deletelistItems(insurance_policies_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteInsurancePoliciesCall(insurance_policies_id)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.removeAt(pos)

                                if (listData.size > 0)
                                {
                                    listAdapter.notifyDataSetChanged()
                                    //gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                   // visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                }
                            }
                            else
                            {
                                showToast(response.body()!!.message)
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

    private fun accountHolderDialog()
    {
        if(getHoldersFullList().size >0)
        {
            val intent = Intent(activity, AddInsurancePoliciesActivity::class.java)
            intent.putExtra("isForEdit", false)
            intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
            intent.putExtra("holder_name", getHoldersFullList()[0].holder)
            intent.putExtra("holder_userName", getHoldersFullList()[0].name)
            startActivity(intent)
            startActivityAnimation()
        }
        else
        {
            showToast("Holder Data Not Found.")
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
