package com.application.alphacapital.superapp.vault.utils

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphaestatevault.model.AccountHolderListResponse
import com.alphaestatevault.utils.AccountHolderInterface
import com.application.alphacapital.superapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.vault_rowview_account_holder_list.view.txtName
import kotlinx.android.synthetic.main.vault_rowview_select_account_holder.view.*

class AccountHolderSelectionDialog(private val activity: Activity,
                                   val listItems: MutableList<AccountHolderListResponse.Holder>,
                                   private val accountHolderInterface: AccountHolderInterface)
{
    fun showAlert()
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.vault_dialog_account_holder_selection, null)
        val dialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)

        try
        {
            val window = dialog.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
                dialog.window!!.statusBarColor = activity.resources.getColor(R.color.vault_transparent)
            }
        }
        catch (e: Exception)
        {
        }

        val rvList = view.findViewById<RecyclerView>(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(activity)

        val listAdapter = ListAdapter(dialog)
        rvList.adapter = listAdapter

        dialog.setOnDismissListener {
            //dialogActionInterface.onDismiss()
        }

        dialog.show()
    }

    inner class ListAdapter(val dialog: BottomSheetDialog) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_select_account_holder, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listItems.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AccountHolderListResponse.Holder = listItems[position]

            holder.itemView.txtName.text = getSet.holder + ". " +getSet.name

            if (position == listItems.size -1)
            {
                holder.itemView.viewLine.visibility = View.GONE
            }
            else
            {
                holder.itemView.viewLine.visibility = View.VISIBLE
            }

            holder.itemView.setOnClickListener {
                dialog.dismiss()
                accountHolderInterface.okClick(getSet)
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
