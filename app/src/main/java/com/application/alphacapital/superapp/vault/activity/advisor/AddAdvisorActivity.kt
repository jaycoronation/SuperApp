package com.application.alphacapital.superapp.vault.activity.advisor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddAdvisorGetSet
import com.alphaestatevault.model.AdviserListResponse
import com.alphaestatevault.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddAdvisorBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_advisor.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAdvisorActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddAdvisorBinding
    var listViews: MutableList<AddAdvisorGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    lateinit var adviserGetSet: AdviserListResponse.Adviser
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_advisor)
        //setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()

            if (isForEdit && intent.hasExtra("data"))
            {
                adviserGetSet = Gson().fromJson(intent.getStringExtra("data"), AdviserListResponse.Adviser::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Advisors".takeIf { !isForEdit } ?: "Update Advisors"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddAdvisorGetSet(adviserGetSet.name, adviserGetSet.phone, adviserGetSet.address, adviserGetSet.relationship, adviserGetSet.company, adviserGetSet.holder, adviserGetSet.adviser_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddAdvisorGetSet("", "", "", "", "", holder_name, ""))
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
            listViews.add(AddAdvisorGetSet("", "", "", "", "", holder_name, ""))
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
                                if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].relationship.isNotEmpty() && listViews[i].company.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("name", listViews[i].name)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("address", listViews[i].address)
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("relationship", listViews[i].relationship)
                                    jGroup.put("company", listViews[i].company)
                                    jGroup.put("adviser_id", listViews[i].adviser_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items",rootObjectItems)

                            addAdviser(rootObject.toString())

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
                                    if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].relationship.isNotEmpty() && listViews[i].company.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("name", listViews[i].name)
                                        jGroup.put("phone", listViews[i].phone)
                                        jGroup.put("address", listViews[i].address)
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("relationship", listViews[i].relationship)
                                        jGroup.put("company", listViews[i].company)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items",rootObjectItems)
                                addAdviser(rootObject.toString())
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
                            if (listViews[i].name.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].address.isEmpty()  && listViews[i].relationship.isEmpty() && listViews[i].company.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].address.isNotEmpty() && listViews[i].relationship.isNotEmpty() && listViews[i].company.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("relationship", listViews[i].relationship)
                                jGroup.put("company", listViews[i].company)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }
                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items",rootObjectItems)

                        if (dataArray.length() > 0)
                        {
                            addAdviser(rootObject.toString())
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
            listViews[0].name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipName.error = "Please enter name."
                isValid = false
            }
            listViews[0].phone.isEmpty() ->
            {
                getViewHolder(0).itemView.ipMobile.error = "Please enter mobile number."
                isValid = false
            }
            listViews[0].phone.length != 10 ->
            {
                getViewHolder(0).itemView.ipMobile.error = "Please enter valid mobile number."
                isValid = false
            }
            listViews[0].address.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAddress.error = "Please enter address."
                isValid = false
            }
            listViews[0].relationship.isEmpty() ->
            {
                getViewHolder(0).itemView.ipRelationship.error = "Please enter relationship."
                isValid = false
            }
            listViews[0].company.isEmpty() ->
            {
                getViewHolder(0).itemView.ipCompany.error = "Please enter company."
                isValid = false
            }

            else ->
            {
                isValid = true
            }
        }

        return true
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid = true

        for (i in listViews.indices)
        {
            if (listViews[i].name.isEmpty() &&
                listViews[i].phone.isEmpty() &&
                listViews[i].address.isEmpty() &&
                listViews[i].relationship.isEmpty() &&
                listViews[i].company.isEmpty() ||
                listViews[i].name.isNotEmpty() &&
                listViews[i].phone.isNotEmpty() &&
                listViews[i].phone.length == 10 &&
                listViews[i].address.isNotEmpty() &&
                listViews[i].relationship.isNotEmpty() &&
                listViews[i].company.isNotEmpty())
            {
                continue
            }
            else
            {
                isValid = false
                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                break
            }
        }

        return true
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_advisor, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddAdvisorGetSet = listViews[position]


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
            holder.itemView.edtName.setText(getSet.name)
            holder.itemView.edtMobile.setText(getSet.phone)
            holder.itemView.edtAddress.setText(getSet.address)
            holder.itemView.edtRelationship.setText(getSet.relationship)
            holder.itemView.edtCompany.setText(getSet.company)

            holder.itemView.edtName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipName.isErrorEnabled = false
                    listViews[position].name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtMobile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipMobile.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    listViews[position].address = charSequence.toString().trim()
                }
            }

            holder.itemView.edtRelationship.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipRelationship.isErrorEnabled = false
                    listViews[position].relationship = charSequence.toString().trim()
                }
            }

            holder.itemView.edtCompany.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipCompany.isErrorEnabled = false
                    listViews[position].company = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addAdviser(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.advisorSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(AdvisorListActivity.handler, "null", 1, 0, 0)
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
