package com.application.alphacapital.superapp.vault.activity.assetsnotpossession

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
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.AssetsNotPossessionListResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityGovRelatedListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_assets_not_possession.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssetsNotPossessionListActivity : VaultBaseActivity()
{

    private lateinit var binding: VaultActivityGovRelatedListBinding
    var listNotification: MutableList<AssetsNotPossessionListResponse.AssetsNotInPossession> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_gov_related_list)

        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when (it.what)
            {
                1 ->
                {
                    if (isOnline())
                    {
                        getAssetsNotPossession()
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
        binding.toolbar.txtTitle.text = "Assets Not in Possession"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getAssetsNotPossession()
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

            startActivity<AddAssetsNotPossessionActivity>("isForEdit" to false
            )
            startActivityAnimation()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getAssetsNotPossession()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getAssetsNotPossession()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getAssetsNotPossessionCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<AssetsNotPossessionListResponse>
                {
                    override fun onResponse(call: Call<AssetsNotPossessionListResponse>, response: Response<AssetsNotPossessionListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.assets_not_in_possessions)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter = notificationListAdapter
                                    gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                }
                            }
                            else
                            {
                                visible(binding.noData.llNoData)
                                gone(binding.rvList)
                                showToast(response.body()!!.message)
                            }
                        }
                        else
                        {
                            visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<AssetsNotPossessionListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                        visible(binding.noData.llNoData)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_assets_not_possession, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AssetsNotPossessionListResponse.AssetsNotInPossession = listNotification[position]

            if (getSet.description.isNotEmpty())
            {
                visible(holder.itemView.txtDescription)
                holder.itemView.txtDescription.text = "Description: ${getSet.description}"
            }
            else
            {
                gone(holder.itemView.txtDescription)
            }

            if (getSet.title.isNotEmpty())
            {
                visible(holder.itemView.txtTitle)
                holder.itemView.txtTitle.text = getSet.title
            }
            else
            {
                gone(holder.itemView.txtTitle)
            }

            if (getSet.approximate_value.isNotEmpty())
            {
                visible(holder.itemView.txtApproximateValue)
                holder.itemView.txtApproximateValue.text = "Approximate Value: ${activity.getString(R.string.rs) + " " + getSet.approximate_value}"
            }
            else
            {
                gone(holder.itemView.txtApproximateValue)
            }

            if (getSet.documentation.isNotEmpty())
            {
                visible(holder.itemView.txtDocument)
                holder.itemView.txtDocument.text = "Document: ${getSet.documentation}"
            }
            else
            {
                gone(holder.itemView.txtDocument)
            }

            if (getSet.location_of_documentation.isNotEmpty())
            {
                visible(holder.itemView.txtDocumentLocation)
                holder.itemView.txtDocumentLocation.text = "Document Location: ${getSet.location_of_documentation}"
            }
            else
            {
                gone(holder.itemView.txtDocumentLocation)
            }

            if (getSet.notes.isNotEmpty())
            {
                visible(holder.itemView.txtNotes)
                holder.itemView.txtNotes.text = "Notes: ${getSet.notes}"
            }
            else
            {
                gone(holder.itemView.txtNotes)
            }




            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.possession_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if (isOnline())
                {
                    val intent = Intent(activity, AddAssetsNotPossessionActivity::class.java)
                    intent.putExtra("isForEdit", true)
                    intent.putExtra("data", Gson().toJson(getSet, AssetsNotPossessionListResponse.AssetsNotInPossession::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity, "Assets Not in Possession", "Are you sure you want to delete this details?", object : DialogActionInterface
        {
            override fun okClick()
            {
                deleteAssetsNotInPossession(id, position)
            }

            override fun cancelClick()
            {
            }

            override fun onDismiss()
            {
            }

        }).showAlert()
    }

    private fun deleteAssetsNotInPossession(govRelatedId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteAssetsNotPossessionCall(govRelatedId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.removeAt(pos)

                                if (listNotification.size > 0)
                                {
                                    notificationListAdapter.notifyDataSetChanged()
                                    gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                    visible(binding.noData.llNoData)
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
