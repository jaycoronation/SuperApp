package com.application.alphacapital.superapp.vault.activity.accountholder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AccountHolderListResponse
import com.alphaestatevault.model.AddAccountHolder
import com.alphaestatevault.model.SignUpResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddCommonBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_account_holder.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddAccountHolderActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityAddCommonBinding
    var listColumn: MutableList<String> = mutableListOf()
    var listViews: MutableList<AddAccountHolder> = mutableListOf()
    val viewsAdapter = ViewsAdapter()

    var isForEdit = false
    lateinit var editGetSet: AccountHolderListResponse.Holder

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_common)
        setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            if (isForEdit && intent.hasExtra("data"))
            {
                editGetSet = Gson().fromJson(
                    intent.getStringExtra("data"),
                    AccountHolderListResponse.Holder::class.java
                )
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Account Holder".takeIf { !isForEdit } ?: "Update Account Holder"
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
            listViews.add(AddAccountHolder(editGetSet.name, editGetSet.phone, editGetSet.email, editGetSet.address, editGetSet.holder, editGetSet.holder_id))
        }
        else
        {
            listViews.add(AddAccountHolder("", "", "", "", listColumn[0].toString(), ""))
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

            var pos: Int = 0
            pos = listViews.size
            listViews.add(AddAccountHolder("", "", "", "", listColumn[pos].toString(), ""))
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
                            if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].email.isNotEmpty() && listViews[i].address.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("email", listViews[i].email)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("holder_id", listViews[i].account_holder_id)
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
                            if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty()
                                && listViews[i].email.isNotEmpty() && listViews[i].address.isNotEmpty()
                            )
                            {
                                val jGroup = JSONObject()
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("email", listViews[i].email)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("holder", listViews[i].holder)
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

    private fun isValidInputForPositionOne(pos : Int): Boolean
    {
        var isValid: Boolean = false;
        if (listViews[pos].name.isEmpty())
        {
            getViewHolder(pos).itemView.ipName.setError("Please enter name.")
            isValid = false
        }
        else if (listViews[pos].phone.isEmpty())
        {
            getViewHolder(pos).itemView.ipMobile.setError("Please enter mobile number.")
            isValid = false
        }
        else if (listViews[pos].phone.length != 10)
        {
            getViewHolder(pos).itemView.ipMobile.setError("Please enter valid mobile number.")
            isValid = false
        }
        else if (listViews[pos].email.isEmpty())
        {
            getViewHolder(pos).itemView.ipEmail.setError("Please enter email address.")
            isValid = false
        }
        else if (!appUtils.isEmailValid(listViews[pos].email.toString()))
        {
            getViewHolder(pos).itemView.ipEmail.setError("Please enter valid email address.")
            isValid = false
        }
        else if (listViews[pos].address.isEmpty())
        {
            getViewHolder(pos).itemView.ipAddress.setError("Please enter address.")
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
        var isValid: Boolean = true;

        for (i in listViews.indices)
        {
            if(listViews[i].holder.equals("A"))
            {
                isValid = isValidInputForPositionOne(i)
            }
            else
            {
                if (listViews[i].name.isEmpty() &&
                    listViews[i].phone.isEmpty() &&
                    listViews[i].email.isEmpty() &&
                    listViews[i].address.isEmpty() ||
                    listViews[i].name.isNotEmpty() &&
                    listViews[i].phone.isNotEmpty() &&
                    listViews[i].phone.length == 10 &&
                    listViews[i].email.isNotEmpty() &&
                    appUtils.isEmailValid(listViews[i].email.toString()) &&
                    listViews[i].address.isNotEmpty()
                )
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
        }

        return isValid
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter() : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.vault_rowview_add_account_holder,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddAccountHolder = listViews[position]

            if (getSet.holder.equals("A"))
            {
                holder.itemView.txtMandatory.visibility = View.VISIBLE
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            holder.itemView.txtHolderName.text = getSet.holder
            holder.itemView.edtName.setText(getSet.name)
            holder.itemView.edtMobile.setText(getSet.phone)
            holder.itemView.edtEmail.setText(getSet.email)
            holder.itemView.edtAddress.setText(getSet.address)

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

            holder.itemView.edtName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipName.isErrorEnabled = false
                    listViews.get(position).name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtMobile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipMobile.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtEmail.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipEmail.isErrorEnabled = false
                    listViews[position].email = charSequence.toString().trim()
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

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.accountHoldersSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    )
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)

                                appUtils.sendMessageFromHandler(
                                    AccountHolderListActivity.handler,
                                    "null",
                                    1,
                                    0,
                                    0
                                )

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
