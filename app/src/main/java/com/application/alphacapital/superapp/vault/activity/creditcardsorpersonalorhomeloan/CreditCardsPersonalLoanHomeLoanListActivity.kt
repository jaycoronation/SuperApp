package com.application.alphacapital.superapp.vault.activity.creditcardsorpersonalorhomeloan

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
import com.alphaestatevault.model.CreditCardsAndLoansResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_credit_cards_and_loans.view.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreditCardsPersonalLoanHomeLoanListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityListBinding
    var listData: MutableList<CreditCardsAndLoansResponse.CreditCardsAndLoan> = mutableListOf()
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
        binding.toolbar.txtTitle.text = "Credit Cards and Loans"
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
                val call = apiService.getCreditCardsAndLoansCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<CreditCardsAndLoansResponse>
                {
                    override fun onResponse(call: Call<CreditCardsAndLoansResponse>, response: Response<CreditCardsAndLoansResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.credit_cards_and_loans)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
                                 //   gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                   // visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            {
                                //visible(binding.noData.llNoData)
                                gone(binding.rvList)
                               // showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
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

                    override fun onFailure(call: Call<CreditCardsAndLoansResponse>, t: Throwable)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_credit_cards_and_loans, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: CreditCardsAndLoansResponse.CreditCardsAndLoan = listData[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.type.isNotEmpty())
            {
                visible(holder.itemView.txtType)
                holder.itemView.txtType.setText(getSet.type)
            }
            else
            {
                gone(holder.itemView.txtType)
            }

            if (getSet.account_number.isNotEmpty())
            {
                visible(holder.itemView.txtAccountNumber)
                holder.itemView.txtAccountNumber.setText(getSet.account_number)
            }
            else
            {
                gone(holder.itemView.txtAccountNumber)
            }

            if (getSet.institution.isNotEmpty())
            {
                visible(holder.itemView.txtinstitution)
                holder.itemView.txtinstitution.text = getSet.institution
            }
            else
            {
                gone(holder.itemView.txtinstitution)
            }


            if (getSet.amount.isNotEmpty())
            {
                visible(holder.itemView.txtamount)
                holder.itemView.txtamount.text = getSet.amount
            }
            else
            {
                gone(holder.itemView.txtamount)
            }

            if (getSet.created_on.isNotEmpty())
            {
                visible(holder.itemView.txtCreatedont)
                holder.itemView.txtCreatedont.text = getSet.created_on
            }
            else
            {
                gone(holder.itemView.txtCreatedont)
            }

            if (getSet.contact_person.isNotEmpty())
            {
                visible(holder.itemView.txtContactPerson)
                holder.itemView.txtContactPerson.text =getSet.contact_person
            }
            else
            {
                gone(holder.itemView.txtContactPerson)
            }

            if (getSet.location_of_document.isNotEmpty())
            {
                visible(holder.itemView.txtlocation_of_document)
                holder.itemView.txtlocation_of_document.text = getSet.location_of_document
            }
            else
            {
                gone(holder.itemView.txtlocation_of_document)
            }

            if (getSet.notes.isNotEmpty())
            {
                visible(holder.itemView.txtnotes)
                holder.itemView.txtnotes.text = getSet.notes
            }
            else
            {
                gone(holder.itemView.txtnotes)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.credit_cards_and_loan_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddCreditCardsPersonalLoanHomeLoanActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, CreditCardsAndLoansResponse.CreditCardsAndLoan::class.java))
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
                if (getSet.upload_doc.isNotEmpty())
                {
                    browse(getSet.upload_doc, false)
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Credit Cards and Loans.", "Are you sure you want to delete this details?", object : DialogActionInterface
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

    private fun deletelistItems(credit_cards_and_loan_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deletecreditCardsAndLoansCall(credit_cards_and_loan_id)

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
                                 //   gone(binding.noData.llNoData)
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
            val intent = Intent(activity, AddCreditCardsPersonalLoanHomeLoanActivity::class.java)
            intent.putExtra("isForEdit", false)
            intent.putExtra("from", "CreditCard")
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
