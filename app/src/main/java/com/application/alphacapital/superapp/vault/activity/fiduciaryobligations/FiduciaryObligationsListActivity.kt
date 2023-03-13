package com.application.alphacapital.superapp.vault.activity.fiduciaryobligations

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
import com.alphaestatevault.model.FiduciaryObligationsResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_fiduciary_obligations.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FiduciaryObligationsListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityListBinding
    var listData: MutableList<FiduciaryObligationsResponse.FiduciaryObligation> = mutableListOf()
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
        binding.toolbar.txtTitle.text = "Fiduciary Obligations"
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
                val call = apiService.getfiduciary_obligationsCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<FiduciaryObligationsResponse>
                {
                    override fun onResponse(call: Call<FiduciaryObligationsResponse>, response: Response<FiduciaryObligationsResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.fiduciary_obligations)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                               //     gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                  //  visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            {
                               // visible(binding.noData.llNoData)
                                gone(binding.rvList)
                               // showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
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

                    override fun onFailure(call: Call<FiduciaryObligationsResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                      //  visible(binding.noData.llNoData)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_fiduciary_obligations, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: FiduciaryObligationsResponse.FiduciaryObligation = listData[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.name.isNotEmpty())
            {
                visible(holder.itemView.txtname)
                holder.itemView.txtname.text = getSet.name
            }
            else
            {
                gone(holder.itemView.txtname)
            }

            if (getSet.relationship.isNotEmpty())
            {
                visible(holder.itemView.txtrelationship)
                holder.itemView.txtrelationship.text =  getSet.relationship
            }
            else
            {
                gone(holder.itemView.txtrelationship)
            }

            if (getSet.types_of_records.isNotEmpty())
            {
                visible(holder.itemView.txttypes_of_records)
                holder.itemView.txttypes_of_records.text = getSet.types_of_records
            }
            else
            {
                gone(holder.itemView.txttypes_of_records)
            }

            if (getSet.instructions.isNotEmpty())
            {
                visible(holder.itemView.txtlocation_of_records)
                holder.itemView.txtlocation_of_records.text =  getSet.instructions
            }
            else
            {
                gone(holder.itemView.txtlocation_of_records)
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

            if (getSet.created_on.isNotEmpty())
            {
                visible(holder.itemView.txtCreatedon)
                holder.itemView.txtCreatedon.text = getSet.created_on
            }
            else
            {
                gone(holder.itemView.txtCreatedon)
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



            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.fiduciary_obligation_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddFiduciaryObligationsActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, FiduciaryObligationsResponse.FiduciaryObligation::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Fiduciary Obligations", "Are you sure you want to delete this details?", object : DialogActionInterface
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

    private fun deletelistItems(fiduciary_obligation_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deletefiduciary_obligationsCall(fiduciary_obligation_id)

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
                                   // gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                 //   visible(binding.noData.llNoData)
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
            val intent = Intent(activity, AddFiduciaryObligationsActivity::class.java)
            intent.putExtra("isForEdit", false)
            intent.putExtra("from", false)
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
