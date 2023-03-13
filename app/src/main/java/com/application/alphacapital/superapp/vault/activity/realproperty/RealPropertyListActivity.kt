package com.application.alphacapital.superapp.vault.activity.realproperty

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
import com.alphaestatevault.model.RealPropertyResponse
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityRealPropertyListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_real_property.view.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RealPropertyListActivity : VaultBaseActivity() {

    private lateinit var binding: VaultActivityRealPropertyListBinding
    var listData: MutableList<RealPropertyResponse.RealProperty> = mutableListOf()
    val listAdapter = ListAdapter()

    companion object{
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_real_property_list)

        initView()
        onClicks()

        handler = Handler(Looper.getMainLooper()) {
            when(it.what){
                1 ->{
                    if (isOnline())
                    {
                       getListData()
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
        binding.toolbar.txtTitle.text = "Real Estate"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        if (isOnline())
        {
           getListData()
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

        binding.ivAdd.setOnClickListener {
            accountHolderDialog()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
              getListData()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getListData()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getRealPropertyCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<RealPropertyResponse>
                {
                    override fun onResponse(call: Call<RealPropertyResponse>, response: Response<RealPropertyResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.clear()
                                listData.addAll(response.body()!!.real_properties)

                                if (listData.size > 0)
                                {
                                    binding.rvList.adapter = listAdapter
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
                                //visible(binding.noData.llNoData)
                                gone(binding.rvList)
                               // showToast(response.body()!!.message)
                                accountHolderDialog()
                                finish()
                            }
                        }
                        else
                        {
                            //visible(binding.noData.llNoData)
                            gone(binding.rvList)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<RealPropertyResponse>, t: Throwable)
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

    inner class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_real_property, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: RealPropertyResponse.RealProperty = listData[position]

            if (getSet.holder_name.isNotEmpty())
            {
                visible(holder.itemView.txtHolderName)
                holder.itemView.txtHolderName.text =  getSet.holder_name
            }
            else
            {
                gone(holder.itemView.txtHolderName)
            }

            if (getSet.name.isNotEmpty())
            {
                visible(holder.itemView.txtname)
                holder.itemView.txtname.text =  getSet.name
            }
            else
            {
                gone(holder.itemView.txtname)
            }

            if (getSet.type_of_property.isNotEmpty())
            {
                visible(holder.itemView.txttype_of_property)
                holder.itemView.txttype_of_property.text = getSet.type_of_property
            }
            else
            {
                gone(holder.itemView.txttype_of_property)
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

            if (getSet.encumbrances.isNotEmpty())
            {
                visible(holder.itemView.txtEncumbrances)
                holder.itemView.txtEncumbrances.text =  getSet.encumbrances
            }
            else
            {
                gone(holder.itemView.txtEncumbrances)
            }

            if (getSet.approximate_value.isNotEmpty())
            {
                visible(holder.itemView.txtApproximateValue)
                holder.itemView.txtApproximateValue.text =  getSet.approximate_value
            }
            else
            {
                gone(holder.itemView.txtApproximateValue)
            }

            if (getSet.location.isNotEmpty())
            {
                visible(holder.itemView.txtLocation)
                holder.itemView.txtLocation.text = getSet.location
            }
            else
            {
                gone(holder.itemView.txtLocation)
            }

            if (getSet.loan.isNotEmpty())
            {
                visible(holder.itemView.txtLoan)
                holder.itemView.txtLoan.text = getSet.loan
            }
            else
            {
                gone(holder.itemView.txtLoan)
            }

            if (getSet.rent.isNotEmpty())
            {
                visible(holder.itemView.txtRent)
                holder.itemView.txtRent.text = getSet.rent
            }
            else
            {
                gone(holder.itemView.txtRent)
            }

            if (getSet.purchased_on.isNotEmpty())
            {
                visible(holder.itemView.txtPurchasedon)
                holder.itemView.txtPurchasedon.text = getSet.purchased_on
            }
            else
            {
                gone(holder.itemView.txtPurchasedon)
            }

            if (getSet.location_of_document.isNotEmpty())
            {
                visible(holder.itemView.txtLocationofDocument)
                holder.itemView.txtLocationofDocument.text = getSet.location_of_document
            }
            else
            {
                gone(holder.itemView.txtLocationofDocument)
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

            holder.itemView.ivDelete.setOnClickListener {
                if (isOnline())
                {
                    deleteDialog(getSet.real_property_id, position)
                }
                else
                {
                    noInternetToast()
                }
            }

            holder.itemView.ivEdit.setOnClickListener {
                if(isOnline()){
                    val intent = Intent(activity, AddRealPropertyActivity::class.java)
                    intent.putExtra("isForEdit",true)
                    intent.putExtra("holder_id", getSet.holder_id)
                    intent.putExtra("holder_name", getSet.holder)
                    intent.putExtra("holder_userName", getSet.holder_name)
                    intent.putExtra("data", Gson().toJson(getSet,RealPropertyResponse.RealProperty::class.java))
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
                             "Real Estate",
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

    private fun deletelistItems(real_property_id: String, pos: Int)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.deleteRealPropertyCall(real_property_id)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listData.removeAt(pos)

                                if (listData.size > 0)
                                {
                                    listAdapter.notifyDataSetChanged()
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
            val intent = Intent(activity, AddRealPropertyActivity::class.java)
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
