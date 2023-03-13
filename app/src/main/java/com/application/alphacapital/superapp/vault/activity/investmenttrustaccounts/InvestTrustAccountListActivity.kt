package com.alphaestatevault.activity.investmenttrustaccounts

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
import com.alphaestatevault.model.InvestTrustAccountListResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityGovRelatedListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_invest_trust_account.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvestTrustAccountListActivity : VaultBaseActivity() {

    private lateinit var binding: VaultActivityGovRelatedListBinding
    var listNotification: MutableList<InvestTrustAccountListResponse.InvestmentTrustAccount> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()
    
    companion object{
        var handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_gov_related_list)

        initView()
        onClicks()

        handler = Handler(Handler.Callback {
            when(it.what){
                1 ->{
                    if (isOnline())
                    {
                        getInvestTrustAccount()
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
        binding.toolbar.txtTitle.text = "Investment Trust Accounts"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getInvestTrustAccount()
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

        binding.ivAddAdvisor.setOnClickListener {

            startActivity<AddInvestTrustAccountActivity>(
                "isForEdit" to false
            )
            startActivityAnimation()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getInvestTrustAccount()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getInvestTrustAccount()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getInvestTrustAccountCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<InvestTrustAccountListResponse>
                {
                    override fun onResponse(call: Call<InvestTrustAccountListResponse>, response: Response<InvestTrustAccountListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.investment_trust_accounts)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter = notificationListAdapter
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

                    override fun onFailure(call: Call<InvestTrustAccountListResponse>, t: Throwable)
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

    inner class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_invest_trust_account, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: InvestTrustAccountListResponse.InvestmentTrustAccount = listNotification[position]

           /* if (getSet.type_of_account.isNotEmpty())
            {
                visible(holder.itemView.txtTypeOfAccount)
                holder.itemView.txtTypeOfAccount.text = getSet.type_of_account
            }
            else
            {
                gone(holder.itemView.txtTypeOfAccount)
            }

            if (getSet.grantor_establisher.isNotEmpty())
            {
                visible(holder.itemView.txtGrantor)
                holder.itemView.txtGrantor.text = "Grantor/ Establisher: ${getSet.grantor_establisher}"
            }
            else
            {
                gone(holder.itemView.txtGrantor)
            }

            if (getSet.beneficiary.isNotEmpty())
            {
                visible(holder.itemView.txtBeneficiary)
                holder.itemView.txtBeneficiary.text = "Beneficiary: ${getSet.beneficiary}"
            }
            else
            {
                gone(holder.itemView.txtBeneficiary)
            }

            if (getSet.location_of_account.isNotEmpty())
            {
                visible(holder.itemView.txtLocationOfAccount)
                holder.itemView.txtLocationOfAccount.text = "Location of Account: ${getSet.location_of_account}"
            }
            else
            {
                gone(holder.itemView.txtLocationOfAccount)
            }

            if (getSet.trustee_name.isNotEmpty())
            {
                visible(holder.itemView.txtTrusteeName)
                holder.itemView.txtTrusteeName.text = "Name: ${getSet.trustee_name}"
            }
            else
            {
                gone(holder.itemView.txtTrusteeName)
            }

            if (getSet.trustee_phone.isNotEmpty())
            {
                visible(holder.itemView.txtTrusteePhone)
                holder.itemView.txtTrusteePhone.text = "Phone: ${getSet.trustee_phone}"
            }
            else
            {
                gone(holder.itemView.txtTrusteePhone)
            }

            if (getSet.trustee_address.isNotEmpty())
            {
                visible(holder.itemView.txtTrusteeAddress)
                holder.itemView.txtTrusteeAddress.text = "Address: ${getSet.trustee_address}"
            }
            else
            {
                gone(holder.itemView.txtTrusteeAddress)
            }
*/



            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.investment_trust_account_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if(isOnline()){
                    val intent = Intent(activity, AddInvestTrustAccountActivity::class.java)
                    intent.putExtra("isForEdit",true)
                    intent.putExtra("data", Gson().toJson(getSet,InvestTrustAccountListResponse.InvestmentTrustAccount::class.java))
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
                             "Investment Trust Accounts",
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

    private fun deletelistItems(govRelatedId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteInvestTrustAccountCall(govRelatedId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.removeAt(pos)

                                if (listNotification.size > 0)
                                {
                                    notificationListAdapter.notifyDataSetChanged()
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
