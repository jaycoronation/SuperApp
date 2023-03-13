package com.application.alphacapital.superapp.vault.activity.assetsnotpossession

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddAssetsNotPossessionGetSet
import com.alphaestatevault.model.AssetsNotPossessionListResponse
import com.alphaestatevault.model.CommonResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddGovRelatedBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_assets_not_possession.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAssetsNotPossessionActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddGovRelatedBinding

    var listColumn: MutableList<String> = mutableListOf()
    var listViews: MutableList<AddAssetsNotPossessionGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var govRelatedGetSet: AssetsNotPossessionListResponse.AssetsNotInPossession

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_gov_related)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit && intent.hasExtra("data"))
            {
                govRelatedGetSet = Gson().fromJson(intent.getStringExtra("data"), AssetsNotPossessionListResponse.AssetsNotInPossession::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Assets Not in Possession".takeIf { !isForEdit } ?: "Update Assets Not in Possession"
        binding.rvList.layoutManager = LinearLayoutManager(activity)

        if (isForEdit)
        {
            listColumn.add(govRelatedGetSet.holder)
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
            listViews.add(AddAssetsNotPossessionGetSet(govRelatedGetSet.holder, govRelatedGetSet.title, govRelatedGetSet.description, govRelatedGetSet.approximate_value, govRelatedGetSet.documentation, govRelatedGetSet.location_of_documentation, govRelatedGetSet.notes, govRelatedGetSet.possession_id))
        }
        else
        {
            for (i in listColumn.indices)
            {
                listViews.add(AddAssetsNotPossessionGetSet(listColumn[i].toString(), "", "", "", "", "", "", ""))
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
                                jGroup.put("description", listViews[i].description)
                                jGroup.put("title", listViews[i].title)
                                jGroup.put("approximate_value", listViews[i].approximate_value)
                                jGroup.put("documentation", listViews[i].documentation)
                                jGroup.put("location_of_documentation", listViews[i].location_of_documentation)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("possession_id", listViews[i].possession_id)
                                dataArray.put(jGroup)
                            }

                            rootObject.put("items", dataArray)

                            addIntellectualProperty(rootObject.toString())

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
                                if (listViews[i].description.isNotEmpty() && listViews[i].description.isNotEmpty() && listViews[i].title.isNotEmpty() && listViews[i].approximate_value.isNotEmpty() && listViews[i].documentation.isNotEmpty() && listViews[i].location_of_documentation.isNotEmpty() && listViews[i].notes.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("description", listViews[i].description)
                                    jGroup.put("title", listViews[i].title)
                                    jGroup.put("approximate_value", listViews[i].approximate_value)
                                    jGroup.put("documentation", listViews[i].documentation)
                                    jGroup.put("location_of_documentation", listViews[i].location_of_documentation)
                                    jGroup.put("notes", listViews[i].notes)
                                    dataArray.put(jGroup)
                                }

                            }

                            rootObject.put("items", dataArray)

                            addIntellectualProperty(rootObject.toString())

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
            listViews[0].title.isEmpty() ->
            {
                getViewHolder(0).itemView.ipTitle.error = "Please enter title."
                isValid = false
            }
            listViews[0].description.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDescription.error = "Please enter description."
                isValid = false
            }
            listViews[0].approximate_value.isEmpty() ->
            {
                getViewHolder(0).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            listViews[0].documentation.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDocumentation.error = "Please enter document name."
                isValid = false
            }
            listViews[0].location_of_documentation.isEmpty() ->
            {
                getViewHolder(0).itemView.ipLocationOfDocument.error = "Please enter location of document."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNotes.error = "Please enter notes."
                isValid = false
            }
            listViews[1].title.isEmpty() ->
            {
                getViewHolder(1).itemView.ipTitle.error = "Please enter title."
                isValid = false
            }
            listViews[1].description.isEmpty() ->
            {
                getViewHolder(1).itemView.ipDescription.error = "Please enter description."
                isValid = false
            }
            listViews[1].approximate_value.isEmpty() ->
            {
                getViewHolder(1).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            listViews[1].documentation.isEmpty() ->
            {
                getViewHolder(1).itemView.ipDocumentation.error = "Please enter document name."
                isValid = false
            }
            listViews[1].location_of_documentation.isEmpty() ->
            {
                getViewHolder(1).itemView.ipLocationOfDocument.error = "Please enter location of document."
                isValid = false
            }
            listViews[1].notes.isEmpty() ->
            {
                getViewHolder(1).itemView.ipNotes.error = "Please enter notes."
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
            listViews[0].title.isEmpty() ->
            {
                getViewHolder(0).itemView.ipTitle.error = "Please enter title."
                isValid = false
            }
            listViews[0].description.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDescription.error = "Please enter description."
                isValid = false
            }
            listViews[0].approximate_value.isEmpty() ->
            {
                getViewHolder(0).itemView.ipApproximateValue.error = "Please enter Approximate Value."
                isValid = false
            }
            listViews[0].documentation.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDocumentation.error = "Please enter document name."
                isValid = false
            }
            listViews[0].location_of_documentation.isEmpty() ->
            {
                getViewHolder(0).itemView.ipLocationOfDocument.error = "Please enter location of document."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNotes.error = "Please enter notes."
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
            if (listViews[i].title.isEmpty() && listViews[i].description.isEmpty() && listViews[i].approximate_value.isEmpty() && listViews[i].documentation.isEmpty() && listViews[i].location_of_documentation.isEmpty() && listViews[i].notes.isEmpty() || listViews[i].title.isNotEmpty() && listViews[i].description.isNotEmpty() && listViews[i].approximate_value.isNotEmpty() && listViews[i].documentation.isNotEmpty() && listViews[i].location_of_documentation.isNotEmpty() && listViews[i].notes.isNotEmpty())
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_assets_not_possession, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddAssetsNotPossessionGetSet = listViews[position]

            if (position == 0 || position == 1)
            {
                holder.itemView.txtMandatory.visibility = View.VISIBLE
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            holder.itemView.txtHolderName.text = getSet.holder
            holder.itemView.edtDescription.setText(getSet.description)
            holder.itemView.edtTitle.setText(getSet.title)
            holder.itemView.edtApproximateValue.setText(getSet.approximate_value)
            holder.itemView.edtDocumentation.setText(getSet.documentation)
            holder.itemView.edtLocationOfDocument.setText(getSet.location_of_documentation)
            holder.itemView.edtNotes.setText(getSet.notes)

            holder.itemView.edtDescription.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipDescription.isErrorEnabled = false
                    listViews[position].description = charSequence.toString().trim()
                }
            }
            holder.itemView.edtNotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNotes.isErrorEnabled = false
                    listViews[position].notes = charSequence.toString().trim()
                }
            }
            holder.itemView.edtTitle.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTitle.isErrorEnabled = false
                    listViews[position].title = charSequence.toString().trim()
                }
            }
            holder.itemView.edtApproximateValue.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipApproximateValue.isErrorEnabled = false
                    listViews[position].approximate_value = charSequence.toString().trim()
                }
            }
            holder.itemView.edtDocumentation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipDocumentation.isErrorEnabled = false
                    listViews[position].documentation = charSequence.toString().trim()
                }
            }
            holder.itemView.edtLocationOfDocument.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocationOfDocument.isErrorEnabled = false
                    listViews[position].location_of_documentation = charSequence.toString().trim()
                }
            }


        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addIntellectualProperty(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.assetsNotPossessionSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(AssetsNotPossessionListActivity.handler, "null", 1, 0, 0)

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
