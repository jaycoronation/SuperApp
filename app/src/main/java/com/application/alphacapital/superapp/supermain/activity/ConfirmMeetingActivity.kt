package com.application.alphacapital.superapp.supermain.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityConfirmMeetingBinding
import com.application.alphacapital.superapp.supermain.model.MeetingModel
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.application.alphacapital.superapp.vault.activity.VaultChangePasswordActivity
import com.application.alphacapital.superapp.vault.activity.VaultUpdateProfileActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConfirmMeetingActivity : VaultBaseActivity()
{
    private lateinit var binding : ActivityConfirmMeetingBinding
    var selectedSlot : MeetingModel = MeetingModel()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_confirm_meeting)
        initView()
        onClick()
    }

    private fun onClick()
    {
        binding.toolbar.llBack.setOnClickListener { finish() }

        binding.edtReason.setOnClickListener {
            openReasonBottomSheet()
        }

        binding.tvSubmit.setOnClickListener {
            if (binding.edtName.getValue().isEmpty())
            {
                binding.ipName.error = "Please enter your name"
                return@setOnClickListener
            }
            if (binding.edtEmail.getValue().isEmpty())
            {
                binding.ipEmail.error = "Please enter email"
                return@setOnClickListener
            }
            if (!appUtils.isEmailValid(binding.edtEmail.getValue()))
            {
                binding.ipEmail.error = "Please enter valid email"
                return@setOnClickListener
            }
            if (binding.edtNumber.getValue().isEmpty())
            {
                binding.ipMobile.error = "Please enter mobile number"
                return@setOnClickListener
            }
            if (binding.edtNumber.getValue().length != 10)
            {
                binding.ipMobile.error = "Please enter valid mobile number"
                return@setOnClickListener
            }
            if (binding.edtReason.getValue().isNotEmpty())
            {
                binding.ipReason.error = "Please select reason for meeting"
                return@setOnClickListener
            }
            showToast("Your meeting schedule is fixed our office person will call you.")
            finish()
        }
    }

    @SuppressLint("InflateParams")
    private fun openReasonBottomSheet()
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_meeting_reason, null)
        val dialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)

        dialog.setCanceledOnTouchOutside(true)

        val tvReason1 = view.findViewById<AppCompatTextView>(R.id.tvReason1)
        val tvReason2 = view.findViewById<AppCompatTextView>(R.id.tvReason2)
        val tvReason3 = view.findViewById<AppCompatTextView>(R.id.tvReason3)

        tvReason1.setOnClickListener {
            dialog.dismiss()
            binding.edtReason.setText("To know more about Alpha Capital")
        }

        tvReason2.setOnClickListener {
            dialog.dismiss()
            binding.edtReason.setText("To discuss about my portfolio")
        }

        tvReason3.setOnClickListener {
            dialog.dismiss()
            binding.edtReason.setText("To invest in new scheme")
        }

        dialog.show()
    }

    private fun initView()
    {
        try
        {
            removeError(binding.edtName,binding.ipName)
            removeError(binding.edtEmail,binding.ipEmail)
            removeError(binding.edtNumber,binding.ipMobile)
            removeError(binding.edtMessage,binding.ipMessage)
            removeError(binding.edtReason,binding.ipReason)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        Glide.with(activity).load(R.drawable.ic_back_nav).load(binding.toolbar.ivBack)
        binding.toolbar.txtTitle.text = "Request Meeting"

        if (intent.hasExtra("slot"))
        {
            val gson = Gson()
            val json = intent.getStringExtra("slot")
            val type = object : TypeToken<MeetingModel>() {}.type
            selectedSlot = gson.fromJson(json, type)

            binding.tvTime.text = "Meeting : ${selectedSlot.date}(${selectedSlot.day}) - ${selectedSlot.time}"

        }
    }
}