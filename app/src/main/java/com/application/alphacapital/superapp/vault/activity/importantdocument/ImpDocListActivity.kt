package com.application.alphacapital.superapp.vault.activity.importantdocument

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
import com.alphaestatevault.model.ImpDocListResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityImpDocListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_imp_doc.view.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImpDocListActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityImpDocListBinding

    var listNotification: MutableList<ImpDocListResponse.Document> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_imp_doc_list)

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
        binding.toolbar.txtTitle.text = "Important Documents"
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
                val call = apiService.getImpDocCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<ImpDocListResponse>
                {
                    override fun onResponse(
                        call: Call<ImpDocListResponse>,
                        response: Response<ImpDocListResponse>
                    )
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.documents)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter =
                                        notificationListAdapter //  gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                { // visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                    finish()
                                }
                            }
                            else
                            { // visible(binding.noData.llNoData)
                                gone(binding.rvList) //showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
                            }
                        }
                        else
                        { //   visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<ImpDocListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast() // visible(binding.noData.llNoData)
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
                LayoutInflater.from(activity).inflate(R.layout.vault_rowview_imp_doc, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: ImpDocListResponse.Document = listNotification[position]


            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text = getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.document.isNotEmpty())
            {
                visible(holder.itemView.txtDocumentName)
                holder.itemView.txtDocumentName.text =  getSet.document
            }
            else
            {
                gone(holder.itemView.txtDocumentName)
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

            if (getSet.softcopy.isNotEmpty())
            {
                visible(holder.itemView.ivDownload)
            }
            else
            {
                gone(holder.itemView.ivDownload)
            }

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.document_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddImpDocActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra(
                        "data",
                        Gson().toJson(getSet, ImpDocListResponse.Document::class.java)
                    )
                    startActivity(intent)
                    startActivityAnimation()
                }
            }

            holder.itemView.ivDownload.setOnClickListener {
                if (getSet.softcopy.isNotEmpty())
                {
                    browse(getSet.softcopy, false)
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity,
            "Important Documents",
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
                val call = apiService.deleteImpDocCall(notificationId)

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
                                    notificationListAdapter.notifyDataSetChanged() // gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    visible(binding.noData.llNoData) // gone(binding.rvList)
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
            val intent = Intent(activity, AddImpDocActivity::class.java)
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
