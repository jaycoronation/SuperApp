package com.application.alphacapital.superapp.vault.activity.charityrelatedobligations

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
import com.alphaestatevault.model.CharityRelatedObligationsResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_charity_related_obligations.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharityRelatedObligationsListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityListBinding
    var listData: MutableList<CharityRelatedObligationsResponse.CharityRelatedObligation> = mutableListOf()
    val listAdapter = ListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_list)

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
        binding.toolbar.txtTitle.text = "Charity Related"
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
                val call = apiService.getcharity_related_obligationCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<CharityRelatedObligationsResponse>
                {
                    override fun onResponse(call: Call<CharityRelatedObligationsResponse>, response: Response<CharityRelatedObligationsResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.charity_related_obligations)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                                   // gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                  //  visible(binding.noData.llNoData)
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
                          //  visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<CharityRelatedObligationsResponse>, t: Throwable)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_charity_related_obligations, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: CharityRelatedObligationsResponse.CharityRelatedObligation = listData[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.organization.isNotEmpty())
            {
                visible(holder.itemView.txtorganization)
                holder.itemView.txtorganization.text = getSet.organization
            }
            else
            {
                gone(holder.itemView.txtorganization)
            }

            if (getSet.nature_of_obligation.isNotEmpty())
            {
                visible(holder.itemView.txtnature_of_obligation)
                holder.itemView.txtnature_of_obligation.text = getSet.nature_of_obligation
            }
            else
            {
                gone(holder.itemView.txtnature_of_obligation)
            }

            if (getSet.instructions.isNotEmpty())
            {
                visible(holder.itemView.txtinstructions)
                holder.itemView.txtinstructions.text = getSet.instructions
            }
            else
            {
                gone(holder.itemView.txtinstructions)
            }

            if (getSet.contact.isNotEmpty())
            {
                visible(holder.itemView.txtcontact)
                holder.itemView.txtcontact.text = getSet.contact
            }
            else
            {
                gone(holder.itemView.txtcontact)
            }

            if (getSet.phone.isNotEmpty())
            {
                visible(holder.itemView.txtPhone)
                holder.itemView.txtPhone.text = getSet.phone
            }
            else
            {
                gone(holder.itemView.txtPhone)
            }

            if (getSet.address.isNotEmpty())
            {
                visible(holder.itemView.txtAddress)
                holder.itemView.txtAddress.text = getSet.address
            }
            else
            {
                gone(holder.itemView.txtAddress)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.charity_related_obligation_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddCharityRelatedObligationsActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, CharityRelatedObligationsResponse.CharityRelatedObligation::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Charity Related", "Are you sure you want to delete this details?", object : DialogActionInterface
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

    private fun deletelistItems(charity_related_obligation_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deletecharity_related_obligationCall(charity_related_obligation_id)

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
                                  //  gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                  //  visible(binding.noData.llNoData)
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
            val intent = Intent(activity, AddCharityRelatedObligationsActivity::class.java)
            intent.putExtra("isForEdit", false)
            intent.putExtra("from", false)
            intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
            intent.putExtra("holder_name",  getHoldersFullList()[0].holder)
            intent.putExtra("holder_userName",  getHoldersFullList()[0].name)
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
