package com.application.alphacapital.superapp.vault.activity.deathnotifications

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.DeathNotificationListResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityDeathNotificationListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_death_notification_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeathNotificationsListActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityDeathNotificationListBinding
    var listNotification: MutableList<DeathNotificationListResponse.Notification> = mutableListOf()
    val notificationListAdapter = NotificationListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_death_notification_list)
        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when (it.what)
            {
                1 ->
                {
                    if (isOnline())
                    {
                        getNotifications()
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
            }
            false
        }
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Death Notifications"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getNotifications()
        }
        else
        {
            noInternetToast()
        }
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.ivAddNotification.setOnClickListener {
            accountHolderDialog()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getNotifications()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getNotifications()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getDeathnotificationCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<DeathNotificationListResponse>
                {
                    override fun onResponse(call: Call<DeathNotificationListResponse>, response: Response<DeathNotificationListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.notifications)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter = notificationListAdapter
                                    //  gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    // visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            {
                                // visible(binding.noData.llNoData)
                                gone(binding.rvList)
                                // showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
                            }
                        }
                        else
                        {
                            //   visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<DeathNotificationListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                        // visible(binding.noData.llNoData)
                        gone(binding.rvList)
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

    inner class NotificationListAdapter() : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_death_notification_list, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: DeathNotificationListResponse.Notification = listNotification[position]

            if (getSet.name.isNotEmpty())
            {
                visible(holder.itemView.txtName)
                holder.itemView.txtName.text = getSet.name
            }
            else
            {
                gone(holder.itemView.txtName)
            }

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text = getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.phone.isNotEmpty())
            {
                visible(holder.itemView.txtMobile)
                holder.itemView.txtMobile.text =  getSet.phone
            }
            else
            {
                gone(holder.itemView.txtMobile)
            }

            if (getSet.email.isNotEmpty())
            {
                visible(holder.itemView.txtEmail)
                holder.itemView.txtEmail.text =  getSet.email
            }
            else
            {
                gone(holder.itemView.txtEmail)
            }


            if (getSet.address.isNotEmpty())
            {
                visible(holder.itemView.txtMobile)
                holder.itemView.txtAddress.text = getSet.address
            }
            else
            {
                gone(holder.itemView.txtAddress)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.notification_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddDeathNotificationsActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, DeathNotificationListResponse.Notification::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Death Notifications", "Are you sure you want to delete this details?", object : DialogActionInterface
        {
            override fun okClick()
            {
                deletelistItems(id, position)
            }

            override fun cancelClick()
            {
            }

            override fun onDismiss()
            {
            }

        }).showAlert()
    }

    private fun deletelistItems(notificationId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteDeathnotificationCall(notificationId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.removeAt(pos)

                                if (listNotification.size > 0)
                                {
                                    notificationListAdapter.notifyDataSetChanged()
                                  //  gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                  //  visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                }
                            }
                            else
                            {
                                showToast(response.body()!!.message)
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

    private fun accountHolderDialog()
    {
        /*AccountHolderSelectionDialog(activity, getHoldersFullList(), object : AccountHolderInterface
        {
            override fun okClick(getset: AccountHolderListResponse.Holder)
            {
                val intent = Intent(activity, AddDeathNotificationsActivity::class.java)
                intent.putExtra("isForEdit", false)
                intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
                intent.putExtra("holder_name", getHoldersFullList()[0].holder)
                intent.putExtra("holder_userName", getHoldersFullList()[0].name)
                startActivity(intent)
                startActivityAnimation()
            }

            override fun onDismiss()
            {
            }

        }).showAlert()*/

        val intent = Intent(activity, AddDeathNotificationsActivity::class.java)
        intent.putExtra("isForEdit", false)
        intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
        intent.putExtra("holder_name", getHoldersFullList()[0].holder)
        intent.putExtra("holder_userName", getHoldersFullList()[0].name)
        startActivity(intent)
        startActivityAnimation()
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
