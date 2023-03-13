package com.alphaestatevault.activity.investmenttrustaccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddInvestmentTrustAccountGetSet
import com.alphaestatevault.model.InvestTrustAccountListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddGovRelatedBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_investment_trust_account.view.*

class AddInvestTrustAccountActivity : VaultBaseActivity() {
    private lateinit var binding: VaultActivityAddGovRelatedBinding

    var listColumn: MutableList<String> = mutableListOf()
    var listViews: MutableList<AddInvestmentTrustAccountGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var govRelatedGetSet: InvestTrustAccountListResponse.InvestmentTrustAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_gov_related)

        if(intent.hasExtra("isForEdit")){
            isForEdit = intent.getBooleanExtra("isForEdit",false)
            if(isForEdit && intent.hasExtra("data")){
                govRelatedGetSet = Gson().fromJson(intent.getStringExtra("data"),
                    InvestTrustAccountListResponse.InvestmentTrustAccount::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Investment Trust Account".takeIf { !isForEdit } ?: "Update Investment Trust Account"
        binding.rvList.layoutManager = LinearLayoutManager(activity)

        if(isForEdit){
            listColumn.add(govRelatedGetSet.holder)
        }else{
            listColumn.addAll(getHolders())
        }
        setUpViews()
    }

    private fun setUpViews()
    {
        if(isForEdit){
           /* listViews.add(AddInvestmentTrustAccountGetSet(
                govRelatedGetSet.holder,
                govRelatedGetSet.type_of_account,
                govRelatedGetSet.grantor_establisher,
                govRelatedGetSet.beneficiary,
                govRelatedGetSet.location_of_account,
                govRelatedGetSet.trustee_name,
                govRelatedGetSet.trustee_phone,
                govRelatedGetSet.trustee_address,
                govRelatedGetSet.investment_trust_account_id))*/
        }else{
            for (i in listColumn.indices)
            {
                listViews.add(AddInvestmentTrustAccountGetSet(listColumn[i].toString(), "", "", "","",""))
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

        /*binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                if(isForEdit){
                    if(isValidInputForOneHolder()){
                        if (isValidInputForOther())
                        {
                            val rootObject = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("type_of_account", listViews[i].type_of_account)
                                jGroup.put("grantor_establisher", listViews[i].grantor_establisher)
                                jGroup.put("beneficiary", listViews[i].beneficiary)
                                jGroup.put("location_of_account", listViews[i].location_of_account)
                                jGroup.put("trustee_name", listViews[i].trustee_name)
                                jGroup.put("trustee_phone", listViews[i].trustee_phone)
                                jGroup.put("trustee_address", listViews[i].trustee_address)
                                jGroup.put("investment_trust_account_id", listViews[i].investment_trust_account_id)
                                dataArray.put(jGroup)
                            }

                            rootObject.put("items", dataArray)

                            addEmpRelated(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }else{
                    if (isValidInputForHolderAAndB())
                    {
                        if (isValidInputForOther())
                        {
                            val rootObject = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                if (listViews[i].type_of_account.isNotEmpty() && listViews[i].grantor_establisher.isNotEmpty() && listViews[i].beneficiary.isNotEmpty() && listViews[i].location_of_account.isNotEmpty() && listViews[i].trustee_name.isNotEmpty() && listViews[i].trustee_phone.isNotEmpty() && listViews[i].trustee_address.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("type_of_account", listViews[i].type_of_account)
                                    jGroup.put("grantor_establisher", listViews[i].grantor_establisher)
                                    jGroup.put("beneficiary", listViews[i].beneficiary)
                                    jGroup.put("location_of_account", listViews[i].location_of_account)
                                    jGroup.put("trustee_name", listViews[i].trustee_name)
                                    jGroup.put("trustee_phone", listViews[i].trustee_phone)
                                    jGroup.put("trustee_address", listViews[i].trustee_address)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObject.put("items", dataArray)

                            addEmpRelated(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }
            }
            else
            {
                noInternetToast()
            }
        }*/
    }

    /*private fun isValidInputForHolderAAndB(): Boolean
    {
        var isValid = false
        when {
            listViews[0].type_of_account.isEmpty() -> {
                getViewHolder(0).itemView.ipTypeOfAccount.error = "Please enter type of amount."
                isValid = false
            }
            listViews[0].grantor_establisher.isEmpty() -> {
                getViewHolder(0).itemView.ipGrantorEstablisher.error = "Please enter Grantor or Establisher."
                isValid = false
            }
            listViews[0].beneficiary.isEmpty() -> {
                getViewHolder(0).itemView.ipBeneficiary.error = "Please enter beneficiary."
                isValid = false
            }
            listViews[0].location_of_account.isEmpty() -> {
                getViewHolder(0).itemView.ipLocationOfAccount.error = "Please enter location Of Account."
                isValid = false
            }
            listViews[0].trustee_name.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteeName.error = "Please enter trustee name."
                isValid = false
            }
            listViews[0].trustee_phone.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteePhone.error = "Please enter trustee phone number."
                isValid = false
            }
            listViews[0].trustee_phone.length != 10 -> {
                getViewHolder(1).itemView.ipTrusteePhone.error = "Please enter valid trustee phone number."
                isValid = false
            }
            listViews[0].trustee_address.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteeAddress.error = "Please enter trustee address."
                isValid = false
            }
            listViews[1].type_of_account.isEmpty() -> {
                getViewHolder(1).itemView.ipTypeOfAccount.error = "Please enter type of amount."
                isValid = false
            }
            listViews[1].grantor_establisher.isEmpty() -> {
                getViewHolder(1).itemView.ipGrantorEstablisher.error = "Please enter Grantor or Establisher."
                isValid = false
            }
            listViews[1].beneficiary.isEmpty() -> {
                getViewHolder(1).itemView.ipBeneficiary.error = "Please enter beneficiary."
                isValid = false
            }
            listViews[1].location_of_account.isEmpty() -> {
                getViewHolder(1).itemView.ipLocationOfAccount.error = "Please enter location Of Account."
                isValid = false
            }
            listViews[1].trustee_name.isEmpty() -> {
                getViewHolder(1).itemView.ipTrusteeName.error = "Please enter trustee name."
                isValid = false
            }
            listViews[1].trustee_phone.isEmpty() -> {
                getViewHolder(1).itemView.ipTrusteePhone.error = "Please enter trustee phone number."
                isValid = false
            }
            listViews[1].trustee_phone.length != 10 -> {
                getViewHolder(1).itemView.ipTrusteePhone.error = "Please enter valid trustee phone number."
                isValid = false
            }
            listViews[1].trustee_address.isEmpty() -> {
                getViewHolder(1).itemView.ipTrusteeAddress.error = "Please enter trustee address."
                isValid = false
            }
            else -> {
                isValid = true
            }
        }

        return isValid
    }

    private fun isValidInputForOneHolder(): Boolean
    {
        var isValid = false

        when {
            listViews[0].type_of_account.isEmpty() -> {
                getViewHolder(0).itemView.ipTypeOfAccount.error = "Please enter type of amount."
                isValid = false
            }
            listViews[0].grantor_establisher.isEmpty() -> {
                getViewHolder(0).itemView.ipGrantorEstablisher.error = "Please enter Grantor or Establisher."
                isValid = false
            }
            listViews[0].beneficiary.isEmpty() -> {
                getViewHolder(0).itemView.ipBeneficiary.error = "Please enter beneficiary."
                isValid = false
            }
            listViews[0].location_of_account.isEmpty() -> {
                getViewHolder(0).itemView.ipLocationOfAccount.error = "Please enter location Of Account."
                isValid = false
            }
            listViews[0].trustee_name.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteeName.error = "Please enter trustee name."
                isValid = false
            }
            listViews[0].trustee_phone.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteePhone.error = "Please enter trustee phone number."
                isValid = false
            }
            listViews[0].trustee_phone.length != 10 -> {
                getViewHolder(1).itemView.ipTrusteePhone.error = "Please enter valid trustee phone number."
                isValid = false
            }
            listViews[0].trustee_address.isEmpty() -> {
                getViewHolder(0).itemView.ipTrusteeAddress.error = "Please enter trustee address."
                isValid = false
            }
            else -> {
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
            if (listViews[i].type_of_account.isEmpty() && listViews[i].grantor_establisher.isEmpty() && listViews[i].beneficiary.isEmpty() && listViews[i].location_of_account.isEmpty() && listViews[i].trustee_name.isEmpty() && listViews[i].trustee_phone.isEmpty() && listViews[i].trustee_address.isEmpty()||
                listViews[i].type_of_account.isNotEmpty() && listViews[i].grantor_establisher.isNotEmpty() && listViews[i].beneficiary.isNotEmpty() && listViews[i].location_of_account.isNotEmpty() && listViews[i].trustee_name.isNotEmpty() && listViews[i].trustee_phone.isNotEmpty() && listViews[i].trustee_address.isNotEmpty())
            {
                continue
            }
            else
            {
                isValid = false
                showToast("Please Enter or Clear all fields value of Holder " + listViews[i].holder + ".")
                break
            }
        }

        return isValid
    }*/

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_investment_trust_account, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddInvestmentTrustAccountGetSet = listViews[position]

            if (position == 0 || position == 1)
            {
                holder.itemView.txtMandatory.visibility = View.VISIBLE
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            /*holder.itemView.txtHolderName.text = getSet.holder
            holder.itemView.edtTypeOfA  ccount.setText(getSet.type_of_account)
            holder.itemView.edtGrantorEstablisher.setText(getSet.grantor_establisher)
            holder.itemView.edtBeneficiary.setText(getSet.beneficiary)
            holder.itemView.edtLocationOfAccount.setText(getSet.location_of_account)
            holder.itemView.edtTrusteeName.setText(getSet.trustee_name)
            holder.itemView.edtTrusteePhone.setText(getSet.trustee_phone)
            holder.itemView.edtTrusteeAddress.setText(getSet.trustee_address)

            holder.itemView.edtTypeOfAccount.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTypeOfAccount.isErrorEnabled = false
                    listViews[position].type_of_account = charSequence.toString().trim()
                }
            }
            holder.itemView.edtGrantorEstablisher.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipGrantorEstablisher.isErrorEnabled = false
                    listViews[position].grantor_establisher = charSequence.toString().trim()
                }
            }
            holder.itemView.edtBeneficiary.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBeneficiary.isErrorEnabled = false
                    listViews[position].beneficiary = charSequence.toString().trim()
                }
            }
            holder.itemView.edtLocationOfAccount.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocationOfAccount.isErrorEnabled = false
                    listViews[position].location_of_account = charSequence.toString().trim()
                }
            }
            holder.itemView.edtTrusteeName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTrusteeName.isErrorEnabled = false
                    listViews[position].trustee_name = charSequence.toString().trim()
                }
            }
            holder.itemView.edtTrusteePhone.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTrusteePhone.isErrorEnabled = false
                    listViews[position].trustee_phone = charSequence.toString().trim()
                }
            }
            holder.itemView.edtTrusteeAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTrusteeAddress.isErrorEnabled = false
                    listViews[position].trustee_address = charSequence.toString().trim()
                }
            }*/


        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    /*private fun addEmpRelated(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.investTrustAccountSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success==1)
                            {
                                appUtils.sendMessageFromHandler(
                                    InvestTrustAccountListActivity.handler,
                                    "null",
                                    1,
                                    0,
                                    0)

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
