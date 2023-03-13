package com.application.alphacapital.superapp.vault.activity.intellectualproperty

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
import com.alphaestatevault.model.IntellectualPropertyListResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityGovRelatedListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_intellectual_property.view.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntellectualPropertyListActivity : VaultBaseActivity() {

    private lateinit var binding: VaultActivityGovRelatedListBinding
    var listNotification: MutableList<IntellectualPropertyListResponse.IntellectualProperty> = mutableListOf()

    val notificationListAdapter = NotificationListAdapter()
    
    companion object{
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_gov_related_list)

        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when(it.what){
                1 ->{
                    if (isOnline())
                    {
                        getIntellectualProperty()
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
        binding.toolbar.txtTitle.text = "Intellectual Property"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
            getIntellectualProperty()
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
                getIntellectualProperty()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getIntellectualProperty()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getIntellectualPropertyCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<IntellectualPropertyListResponse>
                {
                    override fun onResponse(call: Call<IntellectualPropertyListResponse>, response: Response<IntellectualPropertyListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listNotification.clear()
                                listNotification.addAll(response.body()!!.intellectual_properties)

                                if (listNotification.size > 0)
                                {
                                    binding.rvList.adapter = notificationListAdapter
                                  //  gone(binding.noData.llNoData)
                                    visible(binding.rvList)
                                }
                                else
                                {
                                  //  visible(binding.noData.llNoData)
                                    gone(binding.rvList)
                                    accountHolderDialog()
                                }
                            }
                            else
                            {
                               // visible(binding.noData.llNoData)
                                gone(binding.rvList)
                              //  showToast(response.body()!!.message)
                                accountHolderDialog()
                            }
                        }
                        else
                        {
                        //    visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<IntellectualPropertyListResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                      //  visible(binding.noData.llNoData)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_intellectual_property, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listNotification.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: IntellectualPropertyListResponse.IntellectualProperty = listNotification[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.lawyer_name.isNotEmpty())
            {
                visible(holder.itemView.txtCounselName)
                holder.itemView.txtCounselName.text =getSet.lawyer_name
            }
            else
            {
                gone(holder.itemView.txtCounselName)
            }

            if (getSet.nominee_name.isNotEmpty())
            {
                visible(holder.itemView.txtnominee_name)
                holder.itemView.txtnominee_name.text = getSet.nominee_name
            }
            else
            {
                gone(holder.itemView.txtnominee_name)
            }

            if (getSet.firm.isNotEmpty())
            {
                visible(holder.itemView.txtFirm)
                holder.itemView.txtFirm.text = getSet.firm
            }
            else
            {
                gone(holder.itemView.txtFirm)
            }

            if (getSet.phone.isNotEmpty())
            {
                visible(holder.itemView.txtPhone)
                holder.itemView.txtPhone.text =  getSet.phone
            }
            else
            {
                gone(holder.itemView.txtPhone)
            }

            if (getSet.created_on.isNotEmpty())
            {
                visible(holder.itemView.txtCreatedon)
                holder.itemView.txtCreatedon.text = getSet.created_on
            }
            else
            {
                gone(holder.itemView.txtCreatedon)
            }

            if (getSet.location_of_document.isNotEmpty())
            {
                visible(holder.itemView.txtLocationofdocument)
                holder.itemView.txtLocationofdocument.text = getSet.location_of_document
            }
            else
            {
                gone(holder.itemView.txtLocationofdocument)
            }

            if (getSet.description.isNotEmpty())
            {
                visible(holder.itemView.txtDescription)
                holder.itemView.txtDescription.text = getSet.description
            }
            else
            {
                gone(holder.itemView.txtDescription)
            }

            if (getSet.notes.isNotEmpty())
            {
                visible(holder.itemView.txtNotes)
                holder.itemView.txtNotes.text = getSet.notes
            }
            else
            {
                gone(holder.itemView.txtNotes)
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
                    deleteDialog(getSet.intellectual_property_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if(isOnline()){
                    val intent = Intent(activity, AddIntellectualPropertyActivity::class.java)
                    intent.putExtra("isForEdit",true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet,IntellectualPropertyListResponse.IntellectualProperty::class.java))
                    startActivity(intent)
                    startActivityAnimation()
                }
            }

            if (getSet.upload_doc.isNotEmpty())
            {
                visible(holder.itemView.ivDownload)
            }
            else
            {
                gone(holder.itemView.ivDownload)
            }


            holder.itemView.ivDownload.setOnClickListener {
                if(getSet.upload_doc.isNotEmpty()){
                    browse(getSet.upload_doc,false)
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun deleteDialog(id: String, position: Int)
    {
        UniversalAlertDialog(activity,
                             "Intellectual Property",
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

    private fun deletelistItems(govRelatedId: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteInvestTrustAccountCall(govRelatedId)

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
                                   // gone(binding.noData.llNoData)
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

    private fun accountHolderDialog()
    {
        if(getHoldersFullList().size >0)
        {
            val intent = Intent(activity, AddIntellectualPropertyActivity::class.java)
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
