package com.application.alphacapital.superapp.vault.activity.advisor

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
import com.alphaestatevault.model.AdviserListResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAdvisorListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_adviser_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvisorListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityAdvisorListBinding
    var listNotification: MutableList<AdviserListResponse.Adviser> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_advisor_list)
        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when (it.what)
            {
                1 ->
                {
                    if (isOnline())
                    {
                        getAdviser()
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
        binding.toolbar.txtTitle.text = "Advisors"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getAdviser()
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
                getAdviser()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getAdviser()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getAdviserCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<AdviserListResponse>
                {
                    override fun onResponse(call: Call<AdviserListResponse>, response: Response<AdviserListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.advisers)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter = notificationListAdapter
                                    //   gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    //  visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            {
                                //visible(binding.noData.llNoData)
                                gone(binding.rvList)
                                //showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
                            }
                        }
                        else
                        {
                            //  visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<AdviserListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                        //   visible(binding.noData.llNoData)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_adviser_list, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AdviserListResponse.Adviser = listNotification[position]

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

            if (getSet.relationship.isNotEmpty())
            {
                visible(holder.itemView.txtRelationship)
                holder.itemView.txtRelationship.text = getSet.relationship
            }
            else
            {
                gone(holder.itemView.txtRelationship)
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

            if (getSet.company.isNotEmpty())
            {
                visible(holder.itemView.txtCompany)
                holder.itemView.txtCompany.text = getSet.company
            }
            else
            {
                gone(holder.itemView.txtCompany)
            }

            if (getSet.address.isNotEmpty())
            {
                visible(holder.itemView.txtAddress)
                holder.itemView.txtAddress.text = getSet.address
            }
            else
            {
                gone(holder.itemView.txtAddress)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.adviser_id, position)

                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddAdvisorActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet, AdviserListResponse.Adviser::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(adviserId: String, position: Int)
    {
        UniversalAlertDialog(activity, "Advisors", "Are you sure you want to delete this details?", object : DialogActionInterface
        {
            override fun okClick()
            {
                deleteAdviser(adviserId, position)
            }

            override fun cancelClick()
            {
            }

            override fun onDismiss()
            {
            }

        }).showAlert()
    }

    private fun deleteAdviser(adviserId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteAdviserCall(adviserId)

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
                                    //   gone(binding.noData.llNoData)
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
        /*AccountHolderSelectionDialog(activity,
            getHoldersFullList(),
            object : AccountHolderInterface
            {
                override fun okClick(getset: AccountHolderListResponse.Holder)
                {
                    Log.e("<><> NAME : ", getset.name + " ")
                    Log.e("<><> EMAIL : ", getset.email + " ")
                    Log.e("<><> MOBILE : ", getset.phone + " ")
                    Log.e("<><> ID : ", getset.holder_id + " ")

                    val intent = Intent(activity, AddAdvisorActivity::class.java)
                    intent.putExtra("isForEdit", false)
                    intent.putExtra("holder_id", getset.holder_id)
                    intent.putExtra("holder_name", getset.holder)
                    intent.putExtra("holder_userName", getset.name)
                    startActivity(intent)
                    startActivityAnimation()
                }

                override fun onDismiss()
                {
                }

            }).showAlert()*/

        if (getHoldersFullList().size > 0)
        {
            val intent = Intent(activity, AddAdvisorActivity::class.java)
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
