package com.application.alphacapital.superapp.vault.activity.business

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddBusinessDetailsGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.BusinessDetailResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityBusinessBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.vault_rowview_add_business_details.view.*
import kotlinx.android.synthetic.main.vault_rowview_list_dialog_selection.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BusinessActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityBusinessBinding
    lateinit var businessDetailResponse: BusinessDetailResponse.Business
    var listViews: MutableList<AddBusinessDetailsGetSet> = mutableListOf()
    var listSelection: MutableList<String> = mutableListOf()
    val viewsAdapter = ViewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_business)
        setStatusBarGradiant(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Business(es)"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        listSelection.add("Yes")
        listSelection.add("No")
        setUpViews()
        if (isOnline())
        {
            getDataCall()
        }
        else
        {
            noInternetToast()
        }
    }

    private fun setUpViews()
    {
        for (i in getHoldersFullList().indices)
        {
            listViews.add(AddBusinessDetailsGetSet("", "", "", getHoldersFullList()[i].holder, getHoldersFullList()[i].holder_id,getHoldersFullList()[i].name))
        }

        binding.edtOwnOrJointlyBusiness.setText(listSelection[0].toString())
        binding.edtDocumentStatingYourWishes.setText(listSelection[0].toString())
        binding.rvList.adapter = viewsAdapter
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.edtOwnOrJointlyBusiness.setOnClickListener {
            showListDialog(1)
        }

        binding.edtDocumentStatingYourWishes.setOnClickListener {
            showListDialog(2)
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (binding.rvList.visibility == View.VISIBLE)
            {
                if (isValidInputForOther())
                {
                    val rootObject = JSONObject()
                    val rootObjectItems = JSONObject()

                    var isData: Boolean = false;
                    for (i in listViews.indices)
                    {
                        if (listViews[i].name.isNotEmpty() && listViews[i].lineorArea.isNotEmpty() && listViews[i].type.isNotEmpty())
                        {
                            isData = true;
                            val jGroup = JSONObject()
                            jGroup.put("name", listViews[i].name)
                            jGroup.put("area_of_business", listViews[i].lineorArea)
                            jGroup.put("type_of_organization", listViews[i].type)
                            rootObjectItems.put(listViews[i].holder_id, jGroup)
                        }
                    }

                    rootObject.put("items", rootObjectItems)
                    Log.e("<><> JSON : ", rootObject.toString())

                    if (isOnline())
                    {
                        if (isData)
                        {
                            addData(rootObject.toString())
                        }
                        else
                        {
                            addData("")
                        }
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
            }
            else
            {
                if (isOnline())
                {
                    addData("")
                }
                else
                {
                    noInternetToast()
                }
            }
        }
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid: Boolean = true;

        for (i in listViews.indices)
        {
            if (listViews[i].name.isEmpty() && listViews[i].lineorArea.isEmpty() && listViews[i].type.isEmpty() || listViews[i].name.isNotEmpty() && listViews[i].lineorArea.isNotEmpty() && listViews[i].lineorArea.isNotEmpty())
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

    inner class ViewsAdapter() : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_business_details, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddBusinessDetailsGetSet = listViews[position]

            holder.itemView.txtMandatory.visibility = View.GONE

            holder.itemView.txtHolderName.text = getSet.holder_userName
            holder.itemView.edtName.setText(getSet.name)
            holder.itemView.edtLineAreaofBusiness.setText(getSet.lineorArea)
            holder.itemView.edtTypeOfOrganization.setText(getSet.type)

            holder.itemView.edtName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipName.isErrorEnabled = false
                    listViews.get(position).name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtLineAreaofBusiness.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLineAreaofBusiness.isErrorEnabled = false
                    listViews[position].lineorArea = charSequence.toString().trim()
                }
            }

            holder.itemView.edtTypeOfOrganization.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTypeOfOrganization.isErrorEnabled = false
                    listViews[position].type = charSequence.toString().trim()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun getDataCall()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getBusinessesDetail(sessionManager.userId)

                call.enqueue(object : Callback<BusinessDetailResponse>
                {
                    override fun onResponse(call: Call<BusinessDetailResponse>, response: Response<BusinessDetailResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                businessDetailResponse = response.body()!!.business
                                binding.edtOwnOrJointlyBusiness.setText(appUtils.toDisplayCaseNew(businessDetailResponse.own_or_jointly_business))
                                binding.edtDocumentStatingYourWishes.setText(appUtils.toDisplayCaseNew(businessDetailResponse.document_stating_your_wishes))
                                binding.edtDocumentInstructions.setText(businessDetailResponse.document_instructions)

                                if (businessDetailResponse.own_or_jointly_business.length > 0)
                                {
                                    if (binding.edtOwnOrJointlyBusiness.text!!.toString().equals("Yes"))
                                    {
                                        visible(binding.rvList)
                                    }
                                    else
                                    {
                                        gone(binding.rvList)
                                    }
                                }

                                if (businessDetailResponse.businesses_details.size > 0)
                                {
                                    for(j in listViews.indices)
                                    {
                                        for (i in businessDetailResponse.businesses_details.indices)
                                        {
                                            if(listViews[j].holder.toString().equals(businessDetailResponse.businesses_details[i].holder))
                                            {
                                                val addBusinessDetailsGetSet = AddBusinessDetailsGetSet()
                                                addBusinessDetailsGetSet.name = businessDetailResponse.businesses_details[i].name
                                                addBusinessDetailsGetSet.lineorArea = businessDetailResponse.businesses_details[i].area_of_business
                                                addBusinessDetailsGetSet.type = businessDetailResponse.businesses_details[i].type_of_organization
                                                addBusinessDetailsGetSet.holder = businessDetailResponse.businesses_details[i].holder
                                                addBusinessDetailsGetSet.holder_id = businessDetailResponse.businesses_details[i].holder_id
                                                addBusinessDetailsGetSet.holder_userName = businessDetailResponse.businesses_details[i].holder_name
                                                listViews.set(j, addBusinessDetailsGetSet)
                                            }
                                        }
                                    }

                                    viewsAdapter.notifyDataSetChanged()
                                }
                            }
                            else
                            {
                                //toast(response.body()!!.message)
                            }
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<BusinessDetailResponse>, t: Throwable)
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

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.businessesSaveCall(getEditTextString(binding.edtOwnOrJointlyBusiness), getEditTextString(binding.edtDocumentStatingYourWishes), getEditTextString(binding.edtDocumentInstructions), items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                finish()
                                finishActivityAnimation()
                            }
                            else
                            {
                                toast(response.body()!!.message)
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

    fun showListDialog(isFor: Int)
    {
        val view = layoutInflater.inflate(R.layout.vault_dialog_list, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)
        //appUtils.setStatusBarForDialog(dialog, activity)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val edtSearch = view.findViewById<EditText>(R.id.edtSearch)
        val rvDialog = view.findViewById<RecyclerView>(R.id.rvDialog)
        rvDialog.layoutManager = LinearLayoutManager(activity)
        tvTitle.text = "Select Option"
        edtSearch.visibility = View.GONE
        val listAdapter = ListAdapter(dialog, isFor)
        rvDialog.adapter = listAdapter
        dialog.show()
    }

    inner class ListAdapter(val dialog: Dialog, val isFor: Int) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_list_dialog_selection, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listSelection.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            try
            {
                holder.itemView.tvItems.text = listSelection[position].toString()
            }
            catch (e: Exception)
            {
            }


            holder.itemView.setOnClickListener {
                dialog.dismiss()
                if (isFor == 1)
                {
                    binding.edtOwnOrJointlyBusiness.setText(listSelection[position].toString())
                    if (listSelection[position].toString().equals("Yes"))
                    {
                        visible(binding.rvList)
                    }
                    else
                    {
                        gone(binding.rvList)
                    }
                }
                else
                {
                    binding.edtDocumentStatingYourWishes.setText(listSelection[position].toString())
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
