package com.application.alphacapital.superapp.vault.activity.otherassets

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
import com.application.alphacapital.superapp.databinding.VaultActivityAddOtherAssetsBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_other_asset.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOtherAssetsActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddOtherAssetsBinding
    var listViews: MutableList<AddOtherAssetGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var otherAssetDetails: OtherAssetsResponse.OtherAsset

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_other_assets)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                otherAssetDetails = Gson().fromJson(intent.getStringExtra("data"), OtherAssetsResponse.OtherAsset::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Other Asset".takeIf { !isForEdit } ?: "Update Other Asset"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddOtherAssetGetSet(otherAssetDetails.holder, otherAssetDetails.description, otherAssetDetails.encumbrances, otherAssetDetails.approximate_value, otherAssetDetails.notes, otherAssetDetails.other_asset_id,otherAssetDetails.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddOtherAssetGetSet(holder_name, "", "", "", "", "",""))
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
            listViews.add(AddOtherAssetGetSet(holder_name, "", "", "", "", "",""))
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
                                if (listViews[i].description.isNotEmpty() && listViews[i].encumbrances.isNotEmpty() && listViews[i].approximateValue.isNotEmpty() && listViews[i].notes.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("description", listViews[i].description)
                                    jGroup.put("encumbrances", listViews[i].encumbrances)
                                    jGroup.put("approximate_value", listViews[i].approximateValue)
                                    jGroup.put("notes", listViews[i].notes)
                                    jGroup.put("nominee_name", listViews[i].nominee_name)
                                    jGroup.put("other_asset_id", listViews[i].other_asset_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addOtherAssets(rootObject.toString())
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
                                    if (listViews[i].description.isNotEmpty() && listViews[i].encumbrances.isNotEmpty() && listViews[i].approximateValue.isNotEmpty() && listViews[i].notes.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("description", listViews[i].description)
                                        jGroup.put("encumbrances", listViews[i].encumbrances)
                                        jGroup.put("approximate_value", listViews[i].approximateValue)
                                        jGroup.put("nominee_name", listViews[i].nominee_name)
                                        jGroup.put("notes", listViews[i].notes)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addOtherAssets(rootObject.toString())
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
                            if (listViews[i].description.isEmpty() && listViews[i].encumbrances.isEmpty() && listViews[i].approximateValue.isEmpty() && listViews[i].notes.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].description.isNotEmpty() && listViews[i].encumbrances.isNotEmpty() && listViews[i].approximateValue.isNotEmpty() && listViews[i].notes.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("description", listViews[i].description)
                                jGroup.put("encumbrances", listViews[i].encumbrances)
                                jGroup.put("approximate_value", listViews[i].approximateValue)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("notes", listViews[i].notes)
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

                        if (dataArray.length() > 0)
                        {
                            addOtherAssets(rootObject.toString())
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
            listViews[0].description.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDescription.error = "Please enter description."
                isValid = false
            }
            listViews[0].encumbrances.isEmpty() ->
            {
                getViewHolder(0).itemView.ipEncumbrances.error = "Please enter encumbrances."
                isValid = false
            }
            listViews[0].approximateValue.isEmpty() ->
            {
                getViewHolder(0).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNotes.error = "Please enter notes."
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
            if (listViews[i].description.isEmpty() && listViews[i].encumbrances.isEmpty() && listViews[i].approximateValue.isEmpty() && listViews[i].notes.isEmpty()
                && listViews[i].nominee_name.isEmpty()|| listViews[i].description.isNotEmpty() && listViews[i].encumbrances.isNotEmpty() && listViews[i].approximateValue.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].nominee_name.isNotEmpty())
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_other_asset, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddOtherAssetGetSet = listViews[position]

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
            holder.itemView.edtDescription.setText(getSet.description)
            holder.itemView.edtEncumbrances.setText(getSet.encumbrances)
            holder.itemView.edtApproximateValue.setText(getSet.approximateValue)
            holder.itemView.edtNotes.setText(getSet.notes)

            holder.itemView.edtNominee_name.setText(getSet.nominee_name)

            holder.itemView.edtDescription.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipDescription.isErrorEnabled = false
                    listViews[position].description = charSequence.toString().trim()
                }
            }

            holder.itemView.edtEncumbrances.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipEncumbrances.isErrorEnabled = false
                    listViews[position].encumbrances = charSequence.toString().trim()
                }
            }

            holder.itemView.edtApproximateValue.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipApproximateValue.isErrorEnabled = false
                    listViews[position].approximateValue = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNotes.isErrorEnabled = false
                    listViews[position].notes = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addOtherAssets(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.otherAssetsSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(OtherAssetsListActivity.handler, "null", 1, 0, 0)
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
