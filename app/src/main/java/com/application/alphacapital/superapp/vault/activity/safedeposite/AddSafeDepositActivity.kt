package com.application.alphacapital.superapp.vault.activity.safedeposite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddSafeDepositGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.SafeDepositListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddSafeDepositBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_safe_depositebox.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSafeDepositActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddSafeDepositBinding
    var listViews: MutableList<AddSafeDepositGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var adviserGetSet: SafeDepositListResponse.SafeDepositBoxe
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_safe_deposit)
        //setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                adviserGetSet = Gson().fromJson(intent.getStringExtra("data"), SafeDepositListResponse.SafeDepositBoxe::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Safe Deposit Box".takeIf { !isForEdit } ?: "Update Safe Deposit Box"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddSafeDepositGetSet(adviserGetSet.location, adviserGetSet.box_number, adviserGetSet.person_authorized_to_sign, adviserGetSet.person_with_keys, adviserGetSet.general_inventory, holder_name, adviserGetSet.safe_deposit_box_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddSafeDepositGetSet("", "", "", "", "", holder_name, ""))
        }
        
        binding.rvList.adapter = viewsAdapter
    }

    private fun onClicks()
    {

        binding.txtAddMore.setOnClickListener {
            listViews.add(AddSafeDepositGetSet("", "", "", "", "", holder_name, ""))
            viewsAdapter.notifyDataSetChanged()
        }

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
                            val rootObjectItems = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("box_number", listViews[i].box_number)
                                jGroup.put("person_authorized_to_sign", listViews[i].person_authorized_to_sign)
                                jGroup.put("person_with_keys", listViews[i].person_with_keys)
                                jGroup.put("general_inventory", listViews[i].notes)
                                jGroup.put("safe_deposit_box_id", listViews[i].safe_deposit_box_id)
                                dataArray.put(jGroup)
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addSafeDeposit(rootObject.toString())
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
                                    if (listViews[i].location.isNotEmpty() && listViews[i].box_number.isNotEmpty() && listViews[i].person_authorized_to_sign.isNotEmpty() && listViews[i].person_with_keys.isNotEmpty() && listViews[i].notes.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("location", listViews[i].location)
                                        jGroup.put("box_number", listViews[i].box_number)
                                        jGroup.put("person_authorized_to_sign", listViews[i].person_authorized_to_sign)
                                        jGroup.put("person_with_keys", listViews[i].person_with_keys)
                                        jGroup.put("general_inventory", listViews[i].notes)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addSafeDeposit(rootObject.toString())
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
                            if (listViews[i].location.isEmpty() && listViews[i].box_number.isEmpty() && listViews[i].person_authorized_to_sign.isEmpty() && listViews[i].person_with_keys.isEmpty() && listViews[i].notes.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].location.isNotEmpty() && listViews[i].box_number.isNotEmpty() && listViews[i].person_authorized_to_sign.isNotEmpty() && listViews[i].person_with_keys.isNotEmpty() && listViews[i].notes.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("box_number", listViews[i].box_number)
                                jGroup.put("person_authorized_to_sign", listViews[i].person_authorized_to_sign)
                                jGroup.put("person_with_keys", listViews[i].person_with_keys)
                                jGroup.put("general_inventory", listViews[i].notes)
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
                            addSafeDeposit(rootObject.toString())
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
            listViews[0].location.isEmpty() ->
            {
                getViewHolder(0).itemView.ipLocation.error = "Please enter location."
                isValid = false
            }
            listViews[0].box_number.isEmpty() ->
            {
                getViewHolder(0).itemView.ipBoxnumber.error = "Please enter box number."
                isValid = false
            }
            listViews[0].person_authorized_to_sign.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAuthPersonSign.error =
                    "Please enter person authorised to sign."
                isValid = false
            }
            listViews[0].person_with_keys.isEmpty() ->
            {
                getViewHolder(0).itemView.ipPersonKey.error = "Please enter person with key."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipGenInventory.error = "Please enter notes."
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
            if (listViews[i].location.isEmpty() && listViews[i].box_number.isEmpty() && listViews[i].person_authorized_to_sign.isEmpty() && listViews[i].person_with_keys.isEmpty() && listViews[i].notes.isEmpty() || listViews[i].location.isNotEmpty() && listViews[i].box_number.isNotEmpty() && listViews[i].person_authorized_to_sign.isNotEmpty() && listViews[i].person_with_keys.isNotEmpty() && listViews[i].notes.isNotEmpty())
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
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.vault_rowview_add_safe_depositebox, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddSafeDepositGetSet = listViews[position]

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
            holder.itemView.edtLocation.setText(getSet.location)
            holder.itemView.edtBoxnumber.setText(getSet.box_number)
            holder.itemView.edtAuthPersonSign.setText(getSet.person_authorized_to_sign)
            holder.itemView.edtPersonKey.setText(getSet.person_with_keys)
            holder.itemView.edtGenInventory.setText(getSet.notes)

            holder.itemView.edtLocation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocation.isErrorEnabled = false
                    listViews[position].location = charSequence.toString().trim()
                }
            }

            holder.itemView.edtBoxnumber.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBoxnumber.isErrorEnabled = false
                    listViews[position].box_number = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAuthPersonSign.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAuthPersonSign.isErrorEnabled = false
                    listViews[position].person_authorized_to_sign = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPersonKey.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPersonKey.isErrorEnabled = false
                    listViews[position].person_with_keys = charSequence.toString().trim()
                }
            }

            holder.itemView.edtGenInventory.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipGenInventory.isErrorEnabled = false
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

    private fun addSafeDeposit(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.safeDepositSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(SafeDepositListActivity.handler, "null", 1, 0, 0)

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
