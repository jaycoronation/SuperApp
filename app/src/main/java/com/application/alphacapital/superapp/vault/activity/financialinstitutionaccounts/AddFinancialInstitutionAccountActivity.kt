package com.application.alphacapital.superapp.vault.activity.financialinstitutionaccounts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddFinancialInstitutionAccountGetSet
import com.alphaestatevault.model.FinancialInstitutionAccountsResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddFinancialInstitutionAccountsBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_financial_institution_account.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.json.JSONArray
import org.json.JSONObject

class AddFinancialInstitutionAccountActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddFinancialInstitutionAccountsBinding
    var listColumn: MutableList<String> = mutableListOf()
    var listViews: MutableList<AddFinancialInstitutionAccountGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var financialInstitutionAccountsDetails : FinancialInstitutionAccountsResponse.FinancialInstitutionAccount

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_financial_institution_accounts)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit && intent.hasExtra("data"))
            {
                financialInstitutionAccountsDetails = Gson().fromJson(
                    intent.getStringExtra("data"),
                    FinancialInstitutionAccountsResponse.FinancialInstitutionAccount::class.java
                )
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Financial Institution Account".takeIf { !isForEdit } ?: "Update Financial Institution Account"
        binding.rvList.layoutManager = LinearLayoutManager(activity)

        if (isForEdit)
        {
            listColumn.add(financialInstitutionAccountsDetails.holder)
        }
        else
        {
            listColumn.addAll(getHolders())
        }
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            listViews.add(
                AddFinancialInstitutionAccountGetSet(
                    financialInstitutionAccountsDetails.holder,
                    financialInstitutionAccountsDetails.bank_name,
                    financialInstitutionAccountsDetails.branch,
                    financialInstitutionAccountsDetails.account_number_and_type,
                    financialInstitutionAccountsDetails.other_than_own_name,
                    financialInstitutionAccountsDetails.approximate_value,
                    financialInstitutionAccountsDetails.financial_institution_account_id
                )
            )
        }
        else
        {
            for (i in listColumn.indices)
            {
                listViews.add(AddFinancialInstitutionAccountGetSet(listColumn[i].toString(), "", "", "", "", "",""))
            }
        }
        binding.rvList.adapter = viewsAdapter
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
            if (isOnline())
            {
                if (isForEdit)
                {
                    if (isValidInputForOneHolder())
                    {
                        if (isValidInputForOther())
                        {
                            val rootObject = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("bank_name", listViews[i].bankName)
                                jGroup.put("branch", listViews[i].branch)
                                jGroup.put("account_number_and_type", listViews[i].accountNumberAndType)
                                jGroup.put("other_than_own_name", listViews[i].nominee_name)
                                jGroup.put("approximate_value", listViews[i].approximateValue)
                                jGroup.put("financial_institution_account_id", listViews[i].financialInstitutionAccountId)
                                dataArray.put(jGroup)
                            }

                            rootObject.put("items", dataArray)

                            //addFinancialInstitutionAccount(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }
                else
                {
                    if (isValidInputForHolderAAndB())
                    {
                        if (isValidInputForOther())
                        {
                            val rootObject = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                if ( listViews[i].bankName.isNotEmpty() && listViews[i].branch.isNotEmpty() && listViews[i].accountNumberAndType.isNotEmpty() && listViews[i].nominee_name.isNotEmpty() && listViews[i].approximateValue.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("bank_name", listViews[i].bankName)
                                    jGroup.put("branch", listViews[i].branch)
                                    jGroup.put("account_number_and_type", listViews[i].accountNumberAndType)
                                    jGroup.put("other_than_own_name", listViews[i].nominee_name)
                                    jGroup.put("approximate_value", listViews[i].approximateValue)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObject.put("items", dataArray)

                            //addFinancialInstitutionAccount(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun isValidInputForHolderAAndB(): Boolean
    {
        var isValid = false

        when
        {
            listViews[0].bankName.isEmpty() ->
            {
                getViewHolder(0).itemView.ipBankName.error =
                    "Please enter bank name."
                isValid = false
            }
            listViews[0].branch.isEmpty() ->
            {
                getViewHolder(0).itemView.ipBranch.error = "Please enter branch."
                isValid = false
            }
            listViews[0].accountNumberAndType.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAccountNumberAndType.error = "Please enter account number & type."
                isValid = false
            }
            listViews[0].nominee_name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNominee_name.error = "Please enter Name(s) (other than own)."
                isValid = false
            }
            listViews[0].approximateValue.isEmpty() ->
            {
                getViewHolder(0).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            else ->
            {
                isValid = true
            }
        }

        return isValid
    }

    private fun isValidInputForOneHolder(): Boolean
    {
        var isValid = false

        when
        {
            listViews[0].bankName.isEmpty() ->
            {
                getViewHolder(0).itemView.ipBankName.error =
                    "Please enter bank name."
                isValid = false
            }
            listViews[0].branch.isEmpty() ->
            {
                getViewHolder(0).itemView.ipBranch.error = "Please enter branch."
                isValid = false
            }
            listViews[0].accountNumberAndType.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAccountNumberAndType.error = "Please enter account number & type."
                isValid = false
            }
            listViews[0].nominee_name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNominee_name.error = "Please enter Name(s) (other than own)."
                isValid = false
            }
            listViews[0].approximateValue.isEmpty() ->
            {
                getViewHolder(0).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            else ->
            {
                isValid = true
            }
        }

        return isValid
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid = true

        for (i in listViews.indices)
        {
            if (listViews[i].bankName.isEmpty() &&
                listViews[i].branch.isEmpty() &&
                listViews[i].accountNumberAndType.isEmpty() &&
                listViews[i].nominee_name.isEmpty() &&
                listViews[i].approximateValue.isEmpty() ||
                listViews[i].bankName.isNotEmpty() &&
                listViews[i].branch.isNotEmpty() &&
                listViews[i].accountNumberAndType.isNotEmpty() &&
                listViews[i].nominee_name.isNotEmpty() &&
                listViews[i].approximateValue.isNotEmpty())
            {
                continue
            }
            else
            {
                isValid = false
                //howToast("Please Enter or Clear all fields value of Holder " + listViews[i].holder + ".")
                break
            }
        }

        return isValid
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.vault_rowview_add_financial_institution_account, parent, false
                )
            )
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddFinancialInstitutionAccountGetSet = listViews[position]

            if (position == 0)
            {
                holder.itemView.txtMandatory.visibility = View.VISIBLE
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            holder.itemView.txtHolderName.text = getSet.holder
            holder.itemView.edtBankName.setText(getSet.bankName)
            holder.itemView.edtBranch.setText(getSet.branch)
            holder.itemView.edtAccountNumberAndType.setText(getSet.accountNumberAndType)
            holder.itemView.edtNominee_name.setText(getSet.nominee_name)
            holder.itemView.edtApproximateValue.setText(getSet.approximateValue)

            holder.itemView.edtBankName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBankName.isErrorEnabled = false
                    listViews[position].bankName = charSequence.toString().trim()
                }
            }


            holder.itemView.edtBranch.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBranch.isErrorEnabled = false
                    listViews[position].branch = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAccountNumberAndType.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAccountNumberAndType.isErrorEnabled = false
                    listViews[position].accountNumberAndType = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtApproximateValue.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipApproximateValue.isErrorEnabled = false
                    listViews[position].approximateValue = charSequence.toString().trim()
                }
            }

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    /*private fun addFinancialInstitutionAccount(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.financialInstitutionAccountsSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(
                        call: Call<CommonResponse>, response: Response<CommonResponse>
                    )
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(
                                    FinancialInstitutionAccountsListActivity.handler, "null", 1, 0, 0
                                )

                                activity.finish()
                                finishActivityAnimation()
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
    }*/

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
