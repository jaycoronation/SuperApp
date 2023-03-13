package com.application.alphacapital.superapp.vault.activity.charityrelatedobligations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.*
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddCommonBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_charity_related_obligation.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCharityRelatedObligationsActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddCommonBinding
    var listViews: MutableList<AddCharityRelatedObligationsGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var detailGetSet: CharityRelatedObligationsResponse.CharityRelatedObligation

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_common)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                detailGetSet = Gson().fromJson(intent.getStringExtra("data"), CharityRelatedObligationsResponse.CharityRelatedObligation::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {

        if (intent.hasExtra("from"))
        {
            binding.tvTitleNew.text = "Enter the details"
        }

            binding.toolbar.txtTitle.text = "Add Charity Related".takeIf { !isForEdit } ?: "Update Charity Related"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddCharityRelatedObligationsGetSet(detailGetSet.holder, detailGetSet.organization, detailGetSet.nature_of_obligation, detailGetSet.instructions, detailGetSet.contact, detailGetSet.phone, detailGetSet.address, detailGetSet.charity_related_obligation_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddCharityRelatedObligationsGetSet(holder_name, "", "", "", "", "", "", ""))
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


        binding.txtAddMore.setOnClickListener {
            listViews.add(AddCharityRelatedObligationsGetSet(holder_name, "", "", "", "", "", "", ""))
            viewsAdapter.notifyDataSetChanged()
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
                            val rootObjectItems = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                if (listViews[i].organization.isNotEmpty() && listViews[i].nature_of_obligation.isNotEmpty() &&
                                    listViews[i].instructions.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].address.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("organization", listViews[i].organization)
                                    jGroup.put("nature_of_obligation", listViews[i].nature_of_obligation)
                                    jGroup.put("instructions", listViews[i].instructions)
                                    jGroup.put("contact", listViews[i].contact)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("address", listViews[i].address)
                                    jGroup.put("charity_related_obligation_id", listViews[i].charity_related_obligation_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)

                            addFinancialInstitutionAccount(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }
                else
                {
                    if (holder_name.equals("A"))
                    {
                        if (isValidInputForOneHolder())
                        {
                            if (isValidInputForOther())
                            {
                                val rootObject = JSONObject()
                                val rootObjectItems = JSONObject()
                                val dataArray = JSONArray()

                                for (i in listViews.indices)
                                {
                                    if (listViews[i].organization.isNotEmpty() && listViews[i].nature_of_obligation.isNotEmpty()
                                        && listViews[i].instructions.isNotEmpty() && listViews[i].contact.isNotEmpty() &&
                                        listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].address.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("organization", listViews[i].organization)
                                        jGroup.put("nature_of_obligation", listViews[i].nature_of_obligation)
                                        jGroup.put("instructions", listViews[i].instructions)
                                        jGroup.put("contact", listViews[i].contact)
                                        jGroup.put("phone", listViews[i].phone)
                                        jGroup.put("address", listViews[i].address)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)

                                addFinancialInstitutionAccount(rootObject.toString())

                                Log.e("<><> JSON DATA : ", rootObject.toString())
                            }
                        }
                    }
                    else
                    {
                        val rootObject = JSONObject()
                        val rootObjectItems = JSONObject()
                        val dataArray = JSONArray()

                        for (i in listViews.indices)
                        {
                            if(listViews[i].organization.isEmpty() && listViews[i].nature_of_obligation.isEmpty() && listViews[i].instructions.isEmpty() && listViews[i].contact.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].address.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].organization.isNotEmpty() && listViews[i].nature_of_obligation.isNotEmpty()
                                && listViews[i].instructions.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].address.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("organization", listViews[i].organization)
                                jGroup.put("nature_of_obligation", listViews[i].nature_of_obligation)
                                jGroup.put("instructions", listViews[i].instructions)
                                jGroup.put("contact", listViews[i].contact)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("address", listViews[i].address)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }


                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)

                        if(dataArray.length() >0)
                        {
                            addFinancialInstitutionAccount(rootObject.toString())
                        }
                        Log.e("<><> JSON DATA : ", rootObject.toString())
                    }
                }
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun isValidInputForOneHolder(): Boolean
    {
        var isValid = false

        when
        {
            listViews[0].organization.isEmpty() ->
            {
                getViewHolder(0).itemView.iporganization.error = "Please enter organization."
                isValid = false
            }
            listViews[0].nature_of_obligation.isEmpty() ->
            {
                getViewHolder(0).itemView.ipnature_of_obligation.error = "Please enter nature of obligation."
                isValid = false
            }
            listViews[0].instructions.isEmpty() ->
            {
                getViewHolder(0).itemView.ipinstructions.error = "Please enter instructions."
                isValid = false
            }
            listViews[0].contact.isEmpty() ->
            {
                getViewHolder(0).itemView.ipcontact.error = "Please enter contact Person."
                isValid = false
            }
            listViews[0].phone.isEmpty() ->
            {
                getViewHolder(0).itemView.ipPhone.error = "Please enter phone."
                isValid = false
            }
            listViews[0].phone.length != 10 ->
            {
                getViewHolder(0).itemView.ipPhone.error = "Please enter valid phone number."
                isValid = false
            }
            listViews[0].address.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAddress.error = "Please enter address."
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
            if (listViews[i].organization.isEmpty() && listViews[i].nature_of_obligation.isEmpty() &&
                listViews[i].instructions.isEmpty() && listViews[i].contact.isEmpty() && listViews[i].phone.isEmpty() &&
                listViews[i].address.isEmpty() || listViews[i].organization.isNotEmpty() && listViews[i].nature_of_obligation.isNotEmpty()
                && listViews[i].instructions.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].address.isNotEmpty())
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
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_charity_related_obligation, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddCharityRelatedObligationsGetSet = listViews[position]

            if (holder_name == "A")
            {
                if (position == 0)
                {
                    holder.itemView.txtMandatory.visibility = View.VISIBLE
                }
                else
                {
                    holder.itemView.txtMandatory.visibility = View.GONE
                }
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            if (isForEdit)
            {
                gone(holder.itemView.llDelete)
            }
            else
            {
                if (position == 0)
                {
                    gone(holder.itemView.llDelete)
                }
                else
                {
                    visible(holder.itemView.llDelete)
                }
            }

            holder.itemView.txtHolderName.text = holder_userName
            holder.itemView.edtorganization.setText(getSet.organization)
            holder.itemView.edtnature_of_obligation.setText(getSet.nature_of_obligation)
            holder.itemView.edtinstructions.setText(getSet.instructions)
            holder.itemView.edtcontact.setText(getSet.contact)
            holder.itemView.edtPhone.setText(getSet.phone)
            holder.itemView.edtAddress.setText(getSet.address)

            holder.itemView.edtorganization.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iporganization.isErrorEnabled = false
                    listViews[position].organization = charSequence.toString().trim()
                }
            }


            holder.itemView.edtnature_of_obligation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipnature_of_obligation.isErrorEnabled = false
                    listViews[position].nature_of_obligation = charSequence.toString().trim()
                }
            }

            holder.itemView.edtinstructions.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipinstructions.isErrorEnabled = false
                    listViews[position].instructions = charSequence.toString().trim()
                }
            }



            holder.itemView.edtcontact.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipcontact.isErrorEnabled = false
                    listViews[position].contact = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPhone.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPhone.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    listViews[position].address = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addFinancialInstitutionAccount(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.charity_related_obligationSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(
                                    CharityRelatedObligationsListActivity.handler, "null", 1, 0, 0)

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
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
