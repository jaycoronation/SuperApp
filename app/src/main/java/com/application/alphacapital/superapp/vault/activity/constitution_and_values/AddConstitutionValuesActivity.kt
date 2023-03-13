package com.application.alphacapital.superapp.vault.activity.constitution_and_values

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddConstitutionAndValues
import com.alphaestatevault.model.ConstitutionListResponse
import com.alphaestatevault.model.SignUpResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddCommonBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_constitution_values.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddConstitutionValuesActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityAddCommonBinding
    var listViews: MutableList<AddConstitutionAndValues> = mutableListOf()
    val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var editGetSet: ConstitutionListResponse.Data

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_common)
        //setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit && intent.hasExtra("data"))
            {
                editGetSet = Gson().fromJson(intent.getStringExtra("data"), ConstitutionListResponse.Data::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Constitution And Values".takeIf { !isForEdit } ?: "Update Constitution And Values"
        binding.rvList.layoutManager = LinearLayoutManager(activity)

        if (isForEdit)
        {
            gone(binding.txtAddMore)
        }
        else
        {
            visible(binding.txtAddMore)
        }
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            listViews.add(AddConstitutionAndValues(editGetSet.notes, editGetSet.id))
        }
        else
        {
            listViews.add(AddConstitutionAndValues("", ""))
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
            listViews.add(AddConstitutionAndValues("", ""))
            viewsAdapter.notifyDataSetChanged()
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                if (isForEdit)
                {
                    if (isValidInputForOther())
                    {
                        val rootObject = JSONObject()
                        val dataArray = JSONArray()

                        for (i in listViews.indices)
                        {
                            if (listViews[i].notes.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("id", listViews[i].constitution_and_values_id)
                                dataArray.put(jGroup)
                            }
                        }
                        rootObject.put("items", dataArray)
                        addData(rootObject.toString())
                        Log.e("<><> JSON DATA : ", rootObject.toString());
                    }
                }
                else
                {
                    if (isValidInputForOther())
                    {
                        val rootObject = JSONObject()
                        val dataArray = JSONArray()

                        for (i in listViews.indices)
                        {
                            if (listViews[i].notes.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("notes", listViews[i].notes)
                                dataArray.put(jGroup)
                            }
                        }

                        rootObject.put("items", dataArray)
                        addData(rootObject.toString())
                        Log.e("<><> JSON DATA : ", rootObject.toString());
                    }
                }
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun isValidInputForPositionOne(pos: Int): Boolean
    {
        var isValid: Boolean = false;
        if (listViews[pos].notes.isEmpty())
        {
            getViewHolder(pos).itemView.ipNotes.setError("Please enter your notes.")
            isValid = false
        }
        else
        {
            isValid = true
        }
        return true
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid: Boolean = true;

        for (i in listViews.indices)
        {
            isValid = isValidInputForPositionOne(i)
            if (!isValid)
            {
                break
            }
        }

        return true
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter() : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_constitution_values, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddConstitutionAndValues = listViews[position]

            holder.itemView.edtNotes.setText(getSet.notes)

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

            holder.itemView.edtNotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNotes.isErrorEnabled = false
                    listViews.get(position).notes = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.constitution_valuesSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)

                                appUtils.sendMessageFromHandler(ConstitutionAndValuesListActivity.handler, "null", 1, 0, 0)

                                activity.finish()
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

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable)
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
