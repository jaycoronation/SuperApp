package com.application.alphacapital.superapp.vault.activity.accountholder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AccountHolderListResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityListCommonBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_account_holder_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountHolderListActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityListCommonBinding
    var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
    val listAdapter = ListAdapter()

    companion object
    {
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_list_common)
        initView()
        onClicks()

        handler = Handler(Handler.Callback {
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
        })
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Account Holders"
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
            val intent = Intent(activity, AddAccountHolderActivity::class.java)
            intent.putExtra("isForEdit", false)
            startActivity(intent)
            startActivityAnimation()
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
                val call = apiService.getaccountHoldersCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<AccountHolderListResponse>
                {
                    override fun onResponse(call: Call<AccountHolderListResponse>, response: Response<AccountHolderListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listHolder.clear()
                                listHolder.addAll(response.body()!!.holders)
                                sessionManager.holdersList = Gson().toJson(response.body()!!.holders)

                                if (listHolder.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                                    sessionManager.holdersList = Gson().toJson(listHolder)
                                    val intent = Intent(activity, AddAccountHolderActivity::class.java)
                                    intent.putExtra("isForEdit", false)
                                    startActivity(intent)
                                    startActivityAnimation()
                                }
                            }
                            else
                            {
                                var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                                sessionManager.holdersList = Gson().toJson(listHolder)
                                gone(binding.rvList)
                                val intent = Intent(activity, AddAccountHolderActivity::class.java)
                                intent.putExtra("isForEdit", false)
                                startActivity(intent)
                                startActivityAnimation()
                            }
                        }
                        else
                        {
                            var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                            sessionManager.holdersList = Gson().toJson(listHolder)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<AccountHolderListResponse>, t: Throwable)
                    {
                        var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                        sessionManager.holdersList = Gson().toJson(listHolder)
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

    inner class ListAdapter() : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_account_holder_list, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listHolder.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AccountHolderListResponse.Holder = listHolder[position]

            if (getSet.name.isNotEmpty())
            {
                visible(holder.itemView.txtName)
                holder.itemView.txtName.text = "Name : " + appUtils.toDisplayCaseNew(getSet.name)
            }
            else
            {
                gone(holder.itemView.txtName)
            }

            if (getSet.phone.isNotEmpty())
            {
                visible(holder.itemView.txtMobile)
                holder.itemView.txtMobile.text = "Contact : " + getSet.phone
            }
            else
            {
                gone(holder.itemView.txtMobile)
            }

            if (getSet.email.isNotEmpty())
            {
                visible(holder.itemView.txtEmail)
                holder.itemView.txtEmail.text = "Email : " + getSet.email
            }
            else
            {
                gone(holder.itemView.txtEmail)
            }

            if (getSet.address.isNotEmpty())
            {
                visible(holder.itemView.txtMobile)
                holder.itemView.txtAddress.text = "Address :\n" + getSet.address
            }
            else
            {
                gone(holder.itemView.txtAddress)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.holder_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddAccountHolderActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("data", Gson().toJson(getSet, AccountHolderListResponse.Holder::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Account Holder", "Are you sure you want to delete this details?", object : DialogActionInterface
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

    private fun deletelistItems(notificationId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteaccountHoldersCall(notificationId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                getListData()
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

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable)
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
