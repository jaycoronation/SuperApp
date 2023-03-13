package com.application.alphacapital.superapp.vault.activity.keystoresidence

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
import com.alphaestatevault.model.ResidenceListResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityResidenceListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_residence_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResidenceListActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityResidenceListBinding
    var listNotification: MutableList<ResidenceListResponse.KeysToResidence> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_residence_list)

        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when (it.what)
            {
                1 ->
                {
                    if (isOnline())
                    {
                        getResidence()
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
        binding.toolbar.txtTitle.text = "Key to Residence"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getResidence()
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

        binding.ivAddAdvisor.setOnClickListener {
            accountHolderDialog()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getResidence()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getResidence()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getResidenceCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<ResidenceListResponse>
                {
                    override fun onResponse(
                        call: Call<ResidenceListResponse>,
                        response: Response<ResidenceListResponse>
                    )
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.keys_to_residences)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter =
                                        notificationListAdapter // gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                { //  visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            { //  visible(binding.noData.llNoData)
                                gone(binding.rvList) //  showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
                            }
                        }
                        else
                        { // visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<ResidenceListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast() //  visible(binding.noData.llNoData)
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

    inner class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(
                LayoutInflater.from(activity)
                    .inflate(R.layout.vault_rowview_residence_list, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: ResidenceListResponse.KeysToResidence = listNotification[position]

            if (getSet.name.isNotEmpty())
            {
                visible(holder.itemView.txtName)
                holder.itemView.txtName.text =  getSet.name
            }
            else
            {
                gone(holder.itemView.txtName)
            }

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
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

            if (getSet.phone.isNotEmpty())
            {
                visible(holder.itemView.txtMobile)
                holder.itemView.txtMobile.text = getSet.phone
            }
            else
            {
                gone(holder.itemView.txtMobile)
            }

            if (getSet.location.isNotEmpty())
            {
                visible(holder.itemView.txtLocation)
                holder.itemView.txtLocation.text =  getSet.location
            }
            else
            {
                gone(holder.itemView.txtLocation)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.keys_to_residences_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddResidenceActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.name)
                    intent.putExtra(
                        "data",
                        Gson().toJson(getSet, ResidenceListResponse.KeysToResidence::class.java)
                    )
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity,
            "Key to Residence",
            "Are you sure you want to delete this details?",
            object : DialogActionInterface
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
                val call = apiService.deleteResidenceCall(notificationId)

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
                                listNotification.removeAt(pos)

                                if (listNotification.size > 0)
                                {
                                    notificationListAdapter.notifyDataSetChanged() //   gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                { //   visible(binding.noData.llNoData)
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
        if (getHoldersFullList().size > 0)
        {
            val intent = Intent(activity, AddResidenceActivity::class.java)
            intent.putExtra("isForEdit", false)
            intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
            intent.putExtra("holder_name", getHoldersFullList()[0].holder)
            intent.putExtra("holder_userName", getHoldersFullList()[0].name)
            startActivity(intent)
            startActivityAnimation()
        }
        else
        {
            showToast("Holder Data Not Found.")
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
