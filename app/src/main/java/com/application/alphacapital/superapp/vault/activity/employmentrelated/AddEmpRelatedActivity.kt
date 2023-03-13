package com.application.alphacapital.superapp.vault.activity.employmentrelated

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddEmpRelatedGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.EmpRelatedListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddGovRelatedBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_emp_related.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEmpRelatedActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddGovRelatedBinding

    var listColumn: MutableList<String> = mutableListOf()
    var listViews: MutableList<AddEmpRelatedGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var govRelatedGetSet: EmpRelatedListResponse.EmploymentRelatedDetail

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_gov_related)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                govRelatedGetSet = Gson().fromJson(intent.getStringExtra("data"), EmpRelatedListResponse.EmploymentRelatedDetail::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Emp. Related".takeIf { !isForEdit } ?: "Update Emp. Related"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddEmpRelatedGetSet(govRelatedGetSet.holder, govRelatedGetSet.type_nature_amount, govRelatedGetSet.company, govRelatedGetSet.contact, govRelatedGetSet.address, govRelatedGetSet.phone, govRelatedGetSet.employment_related_id,govRelatedGetSet.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddEmpRelatedGetSet(holder_name, "", "", "", "", "","",""))
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
            listViews.add(AddEmpRelatedGetSet(holder_name, "", "", "", "", "","",""))
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
                                if(listViews[i].type_nature_amount.isNotEmpty() && listViews[i].company.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10)
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("type_nature_amount", listViews[i].type_nature_amount)
                                    jGroup.put("company", listViews[i].company)
                                    jGroup.put("contact", listViews[i].contact)
                                    jGroup.put("address", listViews[i].address)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("nominee_name", listViews[i].nominee_name)
                                    jGroup.put("employment_related_id", listViews[i].employment_related_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addEmpRelated(rootObject.toString())
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
                                    if(listViews[i].type_nature_amount.isNotEmpty() && listViews[i].company.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10)
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("type_nature_amount", listViews[i].type_nature_amount)
                                        jGroup.put("company", listViews[i].company)
                                        jGroup.put("contact", listViews[i].contact)
                                        jGroup.put("address", listViews[i].address)
                                        jGroup.put("nominee_name", listViews[i].nominee_name)
                                        jGroup.put("phone", listViews[i].phone)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addEmpRelated(rootObject.toString())
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
                            if (listViews[i].type_nature_amount.isEmpty() && listViews[i].company.isEmpty() && listViews[i].contact.isEmpty() && listViews[i].address.isEmpty() && listViews[i].phone.isEmpty())
                            {
                                continue
                            }
                            else if(listViews[i].type_nature_amount.isNotEmpty() && listViews[i].company.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10)
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("type_nature_amount", listViews[i].type_nature_amount)
                                jGroup.put("company", listViews[i].company)
                                jGroup.put("contact", listViews[i].contact)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("phone", listViews[i].phone)
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
                            addEmpRelated(rootObject.toString())
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
            listViews[0].type_nature_amount.isEmpty() ->
            {
                getViewHolder(0).itemView.ipTypeNatureAmount.error = "Please enter type, nature and amount."
                isValid = false
            }
            listViews[0].company.isEmpty() ->
            {
                getViewHolder(0).itemView.ipCompany.error = "Please enter Company."
                isValid = false
            }
            listViews[0].contact.isEmpty() ->
            {
                getViewHolder(0).itemView.ipContactPerson.error = "Please enter contact person."
                isValid = false
            }
            listViews[0].address.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAddress.error = "Please enter address."
                isValid = false
            }
            listViews[0].phone.isEmpty() ->
            {
                getViewHolder(0).itemView.ipMobile.error = "Please enter phone number."
                isValid = false
            }
            listViews[0].phone.length != 10 ->
            {
                getViewHolder(0).itemView.ipMobile.error = "Please enter valid phone number."
                isValid = false
            }
            listViews[0].nominee_name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNominee_name.error = "Please enter nominee name."
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
            if (listViews[i].type_nature_amount.isEmpty() && listViews[i].company.isEmpty() && listViews[i].contact.isEmpty() && listViews[i].address.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].nominee_name.isEmpty()  ||
                listViews[i].type_nature_amount.isNotEmpty() && listViews[i].company.isNotEmpty() && listViews[i].contact.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].nominee_name.isNotEmpty())
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_emp_related, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddEmpRelatedGetSet = listViews[position]

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
            holder.itemView.edtTypeNatureAmount.setText(getSet.type_nature_amount)
            holder.itemView.edtCompany.setText(getSet.company)
            holder.itemView.edtContactPerson.setText(getSet.contact)
            holder.itemView.edtAddress.setText(getSet.address)
            holder.itemView.edtMobile.setText(getSet.phone)

            holder.itemView.edtNominee_name.setText(getSet.nominee_name)

            holder.itemView.edtTypeNatureAmount.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTypeNatureAmount.isErrorEnabled = false
                    listViews[position].type_nature_amount = charSequence.toString().trim()
                }
            }

            holder.itemView.edtCompany.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipCompany.isErrorEnabled = false
                    listViews[position].company = charSequence.toString().trim()
                }
            }

            holder.itemView.edtContactPerson.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipContactPerson.isErrorEnabled = false
                    listViews[position].contact = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    listViews[position].address = charSequence.toString().trim()
                }
            }

            holder.itemView.edtMobile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipMobile.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addEmpRelated(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.empRelatedSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(EmpRelatedListActivity.handler, "null", 1, 0, 0)

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
