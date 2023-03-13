package com.alphaestatevault.utils

import android.app.Activity
import android.os.Build

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.application.alphacapital.superapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class UniversalAlertDialog(private val activity: Activity,
                           val title: String,
                           val description: String,
                           private val dialogActionInterface: DialogActionInterface)
{
    fun showAlert()
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.vault_dialog_logout, null)
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

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val txtNo = view.findViewById<TextView>(R.id.txtNo)
        val txtYes = view.findViewById<TextView>(R.id.txtYes)

        tvTitle.text = title
        tvMessage.text = description

        txtNo.setOnClickListener {
            dialog.dismiss()
            dialogActionInterface.cancelClick()
        }

        txtYes.setOnClickListener {
            dialog.dismiss()
            dialogActionInterface.okClick()
        }

        dialog.setOnDismissListener {
            //dialogActionInterface.onDismiss()
        }

        dialog.show()
    }
}
