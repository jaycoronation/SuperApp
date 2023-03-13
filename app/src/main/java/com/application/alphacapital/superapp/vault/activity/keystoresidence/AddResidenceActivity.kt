package com.application.alphacapital.superapp.vault.activity.keystoresidence

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddResidenceGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.ResidenceListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddResidenceBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_key_residence.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddResidenceActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddResidenceBinding
    var listViews: MutableList<AddResidenceGetSet> = mutableListOf()
    val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var residenceGetSet: ResidenceListResponse.KeysToResidence

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_residence)
        //setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()

            if (isForEdit && intent.hasExtra("data"))
            {
                residenceGetSet =
                    Gson().fromJson(intent.getStringExtra("data"), ResidenceListResponse.KeysToResidence::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Key to Residence".takeIf { !isForEdit } ?: "Update Key to Residence"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddResidenceGetSet(residenceGetSet.name, residenceGetSet.email, residenceGetSet.phone,residenceGetSet.location, residenceGetSet.holder, residenceGetSet.keys_to_residences_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddResidenceGetSet("", "", "", "", holder_name, ""))
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
            listViews.add(AddResidenceGetSet("", "", "", "", holder_name, ""))
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
                                if (listViews[i].name.isNotEmpty() && listViews[i].email.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].location.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("name", listViews[i].name)
                                    jGroup.put("email", listViews[i].email)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("location", listViews[i].location)
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("keys_to_residences_id", listViews[i].keys_to_residences_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)

                            addResidence(rootObject.toString())

                            Log.e("<><> JSON DATA : ", rootObject.toString());
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
                                    if (listViews[i].name.isNotEmpty() && listViews[i].email.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].location.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("name", listViews[i].name)
                                        jGroup.put("email", listViews[i].email)
                                        jGroup.put("phone", listViews[i].phone)
                                        jGroup.put("location", listViews[i].location)
                                        jGroup.put("holder", listViews[i].holder)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addResidence(rootObject.toString())
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
                            if (listViews[i].name.isEmpty() && listViews[i].email.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].location.isEmpty())
                            {
                               continue
                            }
                            else if (listViews[i].name.isNotEmpty() && listViews[i].email.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].location.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("email", listViews[i].email)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("holder", listViews[i].holder)
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
                            addResidence(rootObject.toString())
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
        var isValid: Boolean = false;

        if (listViews[0].name.isEmpty())
        {
            getViewHolder(0).itemView.ipName.setError("Please enter name.")
            isValid = false
        }
        else if (listViews[0].email.isEmpty())
        {
            getViewHolder(0).itemView.ipEmail.setError("Please enter email address.")
            isValid = false
        }
        else if (!appUtils.isEmailValid(listViews[0].email.toString()))
        {
            getViewHolder(0).itemView.ipEmail.setError("Please enter valid email address.")
            isValid = false
        }
        else if (listViews[0].phone.isEmpty())
        {
            getViewHolder(0).itemView.ipMobile.setError("Please enter mobile number.")
            isValid = false
        }
        else if (listViews[0].phone.length != 10)
        {
            getViewHolder(0).itemView.ipMobile.setError("Please enter valid mobile number.")
            isValid = false
        }
        else if (listViews[0].location.isEmpty())
        {
            getViewHolder(0).itemView.ipLocation.setError("Please enter location.")
            isValid = false
        }
        else
        {
            isValid = true
        }

        return isValid
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid: Boolean = true

        for (i in listViews.indices)
        {
            if (listViews[i].name.isEmpty() && listViews[i].email.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].location.isEmpty() || listViews[i].name.isNotEmpty() && listViews[i].email.isNotEmpty() && appUtils.isEmailValid(listViews[i].email.toString()) && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].location.isNotEmpty())
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

        return isValid
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        val viewHolder: ViewsAdapter.ViewHolder =
            binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
        return viewHolder
    }

    inner class ViewsAdapter() : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.vault_rowview_add_key_residence, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddResidenceGetSet = listViews[position]

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
            holder.itemView.edtEmail.setText(getSet.email)
            holder.itemView.edtMobile.setText(getSet.phone)
            holder.itemView.edtLocation.setText(getSet.location)

            holder.itemView.edtName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipName.isErrorEnabled = false
                    listViews[position].name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtEmail.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipEmail.isErrorEnabled = false
                    listViews[position].email = charSequence.toString().trim()
                }
            }
            holder.itemView.edtMobile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipMobile.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtLocation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocation.isErrorEnabled = false
                    listViews[position].location = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addResidence(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.residenceSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)

                                appUtils.sendMessageFromHandler(ResidenceListActivity.handler, "null", 1, 0, 0)

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
