package com.application.alphacapital.superapp.vault.activity.deathnotifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddDeathNotificationGetSet
import com.alphaestatevault.model.DeathNotificationListResponse
import com.alphaestatevault.model.SignUpResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddDeathNotificationBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_add_death_notification.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddDeathNotificationsActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityAddDeathNotificationBinding
    var listViews: MutableList<AddDeathNotificationGetSet> = mutableListOf()
    val viewsAdapter = ViewsAdapter()
    lateinit var notificationGetSet: DeathNotificationListResponse.Notification
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_death_notification)
        //setStatusBarGradiant(activity)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()

            if (isForEdit && intent.hasExtra("data"))
            {
                notificationGetSet = Gson().fromJson(intent.getStringExtra("data"), DeathNotificationListResponse.Notification::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Death Notifications".takeIf { !isForEdit } ?: "Update Death Notification"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddDeathNotificationGetSet(notificationGetSet.name, notificationGetSet.phone, notificationGetSet.email, notificationGetSet.address, notificationGetSet.holder, notificationGetSet.notification_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddDeathNotificationGetSet("", "", "", "", holder_name, ""))
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
            listViews.add(AddDeathNotificationGetSet("", "", "", "", holder_name, ""))
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
                                if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].email.isNotEmpty() &&
                                    appUtils.isEmailValid(listViews[i].email.toString()) && listViews[i].address.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("name", listViews[i].name)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("email", listViews[i].email)
                                    jGroup.put("address", listViews[i].address)
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("notification_id", listViews[i].notification_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addNotifications(rootObject.toString())
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
                                    if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].email.isNotEmpty() &&
                                        appUtils.isEmailValid(listViews[i].email.toString()) && listViews[i].address.isNotEmpty())
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
                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addNotifications(rootObject.toString())
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
                            if (listViews[i].name.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].email.isEmpty() && listViews[i].address.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].email.isNotEmpty() &&
                                appUtils.isEmailValid(listViews[i].email.toString()) && listViews[i].address.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("email", listViews[i].email)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("holder", listViews[i].holder)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }
                        }

                        Log.e("<><> JSON DATA : ", dataArray.length().toString() + "")

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)

                        if (dataArray.length() > 0)
                        {
                            addNotifications(rootObject.toString())
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
        else if (listViews[0].address.isEmpty())
        {
            getViewHolder(0).itemView.ipAddress.setError("Please enter address.")
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
            if (listViews[i].name.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].email.isEmpty() && listViews[i].address.isEmpty() ||
                listViews[i].name.isNotEmpty() && listViews[i].phone.isNotEmpty() && listViews[i].phone.length == 10 && listViews[i].email.isNotEmpty() &&
                appUtils.isEmailValid(listViews[i].email.toString()) && listViews[i].address.isNotEmpty())
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

    inner class ViewsAdapter() : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.vault_rowview_add_death_notification, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddDeathNotificationGetSet = listViews[position]


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
            holder.itemView.edtEmail.setText(getSet.email)

            holder.itemView.edtName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipName.isErrorEnabled = false
                    getSet.name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtMobile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipMobile.isErrorEnabled = false
                    getSet.phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtEmail.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipEmail.isErrorEnabled = false
                    getSet.email = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    getSet.address = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addNotifications(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.notificationSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)

                                appUtils.sendMessageFromHandler(DeathNotificationsListActivity.handler, "null", 1, 0, 0)

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
