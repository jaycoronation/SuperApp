package com.application.alphacapital.superapp.vault.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityShareBinding
import com.alphaestatevault.model.ShareDataGetSet
import com.alphaestatevault.model.ShareResponse
import kotlinx.android.synthetic.main.vault_rowview_add_share_email.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VaultShareActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityShareBinding
    var listViews: MutableList<ShareDataGetSet> = mutableListOf()
    val viewsAdapter = ViewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_share)
        setStatusBarGradiant(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Share Data"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        listViews.add(ShareDataGetSet(""))
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
            listViews.add(ShareDataGetSet(""))
            binding.rvList.adapter = viewsAdapter
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                if (isValidInputForOther())
                {
                    var emails: String = ""

                    for (i in listViews.indices)
                    {
                        if (listViews[i].email.isNotEmpty())
                        {
                            if (emails.isEmpty())
                            {
                                emails = listViews[i].email
                            }
                            else
                            {
                                emails = emails +"," + listViews[i].email
                            }
                        }
                    }

                    shareData(emails)
                    Log.e("<><> EMAILS DATA : ", emails.toString())
                }
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun isValidInputForOneHolder(pos: Int): Boolean
    {
        var isValid: Boolean = false;
        if (listViews[pos].email.isEmpty())
        {
            getViewHolder(pos).itemView.ipEmail.setError("Please enter email address.")
            isValid = false
        }
        else if (!appUtils.isEmailValid(listViews[pos].email.toString()))
        {
            getViewHolder(pos).itemView.ipEmail.setError("Please enter valid email address.")
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
            if (!isValidInputForOneHolder(i))
            {
                isValid = false
                break
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_share_email, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: ShareDataGetSet = listViews[position]

            if (position == 0)
            {
                gone(holder.itemView.llDelete)
            }
            else
            {
                visible(holder.itemView.llDelete)
            }

            holder.itemView.edtEmail.setText(getSet.email)

            holder.itemView.edtEmail.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipEmail.isErrorEnabled = false
                    listViews[position].email = charSequence.toString().trim()
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun shareData(emails: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.shareDataCall(emails,getEditTextString(binding.edtPassword), sessionManager.userId)

                call.enqueue(object : Callback<ShareResponse>
                {
                    override fun onResponse(call: Call<ShareResponse>, response: Response<ShareResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                activity.finish()
                                finishActivityAnimation()
                                if (response.body()!!.url.isNotEmpty())
                                {
                                    browse(response.body()!!.url, false)
                                }
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

                    override fun onFailure(call: Call<ShareResponse>, t: Throwable)
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
