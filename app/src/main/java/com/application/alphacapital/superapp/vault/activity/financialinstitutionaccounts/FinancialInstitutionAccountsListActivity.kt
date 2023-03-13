package com.alphaestatevault.activity.financialinstitutionaccounts

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
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.FinancialInstitutionAccountsResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityFinancialInstitutionAccountsListBinding
import com.application.alphacapital.superapp.vault.activity.financialinstitutionaccounts.AddFinancialInstitutionAccountActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_financial_institution_accounts.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinancialInstitutionAccountsListActivity : VaultBaseActivity() {

    private lateinit var binding: VaultActivityFinancialInstitutionAccountsListBinding
    var listData: MutableList<FinancialInstitutionAccountsResponse.FinancialInstitutionAccount> = mutableListOf()
    val listAdapter = ListAdapter()

    companion object{
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_financial_institution_accounts_list)

        initView()
        onClicks()

        handler = Handler(Handler.Callback {
            when(it.what){
                1 ->{
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
        binding.toolbar.txtTitle.text = "Financial Institution Accounts"
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

            startActivity<AddFinancialInstitutionAccountActivity>(
                "isForEdit" to false
            )
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
                val call = apiService.getFinancialInstitutionAccountCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<FinancialInstitutionAccountsResponse>
                {
                    override fun onResponse(call: Call<FinancialInstitutionAccountsResponse>, response: Response<FinancialInstitutionAccountsResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.financial_institution_accounts)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                                    gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                }
                            }
                            else
                            {
                                visible(binding.noData.llNoData)
                                gone(binding.rvList)
                                showToast(response.body()!!.message)
                            }
                        }
                        else
                        {
                            visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<FinancialInstitutionAccountsResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                        visible(binding.noData.llNoData)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_financial_institution_accounts, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: FinancialInstitutionAccountsResponse.FinancialInstitutionAccount = listData[position]

            if (getSet.bank_name.isNotEmpty())
            {
                visible(holder.itemView.txtBankName)
                holder.itemView.txtBankName.setText(getSet.bank_name)
            }
            else
            {
                gone(holder.itemView.txtBankName)
            }

            if (getSet.branch.isNotEmpty())
            {
                visible(holder.itemView.txtBranch)
                holder.itemView.txtBranch.setText("Branch : "+getSet.branch)
            }
            else
            {
                gone(holder.itemView.txtBranch)
            }

            if (getSet.account_number_and_type.isNotEmpty())
            {
                visible(holder.itemView.txtAccountNumberAndType)
                holder.itemView.txtAccountNumberAndType.setText("Account No. & Type : "+ getSet.account_number_and_type)
            }
            else
            {
                gone(holder.itemView.txtAccountNumberAndType)
            }

            if (getSet.other_than_own_name.isNotEmpty())
            {
                visible(holder.itemView.txtNames)
                holder.itemView.txtNames.setText(getSet.other_than_own_name)
            }
            else
            {
                gone(holder.itemView.txtNames)
            }

            if (getSet.approximate_value.isNotEmpty())
            {
                visible(holder.itemView.txtApproximateValue)
                holder.itemView.txtApproximateValue.setText("Approximate Value : "+ getSet.approximate_value)
            }
            else
            {
                gone(holder.itemView.txtApproximateValue)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.financial_institution_account_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if(isOnline()){
                    val intent = Intent(activity, AddFinancialInstitutionAccountActivity::class.java)
                    intent.putExtra("isForEdit",true)
                    intent.putExtra("data", Gson().toJson(getSet,FinancialInstitutionAccountsResponse.FinancialInstitutionAccount::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity,
                             "Financial Institution Accounts",
                             "Are you sure you want to delete this details?",
                             object : DialogActionInterface
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

    private fun deletelistItems(financial_institution_account_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteFinancialInstitutionAccountCall(financial_institution_account_id)

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
                                    gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    visible(binding.noData.llNoData)
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

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
