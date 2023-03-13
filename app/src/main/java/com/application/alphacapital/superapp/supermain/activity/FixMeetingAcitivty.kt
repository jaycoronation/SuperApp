package com.application.alphacapital.superapp.supermain.activity

import android.graphics.Color
import android.text.format.DateFormat;
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityFixMeetingAcitivtyBinding
import com.application.alphacapital.superapp.supermain.model.MeetingModel
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.bumptech.glide.Glide
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.rowview_meeting_slots.view.*
import org.jetbrains.anko.startActivity
import org.joda.time.DateTime
import java.util.*

class FixMeetingAcitivty : VaultBaseActivity(), DatePickerListener
{
    private lateinit var binding: ActivityFixMeetingAcitivtyBinding
    private var dateList: MutableList<MeetingModel> = mutableListOf()
    private val meetingAdapter = FixMeetingAdapter()
    var isValid: Boolean = false
    var selectedSlot: MeetingModel = MeetingModel()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_fix_meeting_acitivty)
        initView()
        onClick()
    }

    private fun onClick()
    {
        binding.toolbar.llBack.setOnClickListener { finish() }

        binding.tvSubmit.setOnClickListener {
            if (dateList.isNotEmpty())
            {
                for (i in dateList.indices)
                {
                    if (dateList[i].isSelected)
                    {
                        isValid = true
                        selectedSlot = dateList[i]
                    }
                }
            }

            if (!isValid)
            {
                showToast("Please select your slot")
                return@setOnClickListener
            }
            val gson = Gson()
            val listData = gson.toJson(selectedSlot)
            startActivity<ConfirmMeetingActivity>("slot" to listData)
        }

    }

    private fun initView()
    {
        binding.datePicker.setListener(this).setDays(30).setOffset(2)
            .setDateSelectedColor(ContextCompat.getColor(activity, R.color.blue_new))
            .setDateSelectedTextColor(Color.WHITE)
            .setTodayButtonTextColor(ContextCompat.getColor(activity, R.color.light_blue_new))
            .setTodayDateTextColor(ContextCompat.getColor(activity, R.color.black))
            .setTodayDateBackgroundColor(ContextCompat.getColor(activity, R.color.light_blue_new))
            .setUnselectedDayTextColor(ContextCompat.getColor(activity, R.color.black))
            .setUnselectedDayTextColor(ContextCompat.getColor(activity, R.color.black))
            .showTodayButton(false).initialize()

        binding.datePicker.setDate(DateTime().minusDays(-1))

        Glide.with(activity).load(R.drawable.ic_back_nav).into(binding.toolbar.ivBack)
        binding.toolbar.txtTitle.text = "Request Meeting"
        gone(binding.tvSubmit)
        binding.rvTimeSlots.adapter = meetingAdapter
    }

    inner class FixMeetingAdapter : RecyclerView.Adapter<FixMeetingAdapter.ViewHolder>()
    {
        inner class ViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView.rootView)
        {
            var tvTime: AppCompatTextView = itemView.findViewById(R.id.tvTime)
            var tvDays: AppCompatTextView = itemView.findViewById(R.id.tvDays)
            var tvDate: AppCompatTextView = itemView.findViewById(R.id.tvDate)
            var ivStart: AppCompatImageView = itemView.findViewById(R.id.ivStart)
            var llMain: LinearLayout = itemView.findViewById(R.id.llMain)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.rowview_meeting_slots, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet = dateList[position]

            val splitDate = getSet.fullDate.split(" ")

            holder.tvTime.text = getSet.time
            holder.tvDate.text = splitDate[0] + " " + getSet.day
            holder.tvDays.text = splitDate[1] + " " + splitDate[2]
            if (getSet.isSelected)
            {
                holder.llMain.background =
                    ContextCompat.getDrawable(activity, R.drawable.vault_bg_button_fill)
                holder.tvTime.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.tvDays.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.tvDate.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.ivStart.setColorFilter(
                    ContextCompat.getColor(activity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            else
            {
                holder.llMain.background =
                    ContextCompat.getDrawable(activity, R.drawable.vault_bg_button_fill_light)
                holder.tvTime.setTextColor(ContextCompat.getColor(activity, R.color.black))
                holder.tvDays.setTextColor(ContextCompat.getColor(activity, R.color.black))
                holder.tvDate.setTextColor(ContextCompat.getColor(activity, R.color.black))
                holder.ivStart.setColorFilter(
                    ContextCompat.getColor(activity, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            holder.llMain.setOnClickListener {
                visible(binding.tvSubmit)
                for (i in dateList.indices)
                {
                    if (i == position)
                    {
                        if (dateList[i].isSelected)
                        {
                            dateList[i].isSelected = false
                        }
                        else
                        {
                            dateList[i].isSelected = true
                        }
                    }
                    else
                    {
                        dateList[i].isSelected = false
                    }
                }
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int
        {
            return dateList.size
        }

    }

    override fun onDateSelected(dateSelected: DateTime)
    {
        Log.e("DATE", dateSelected.toString())
        visible(binding.rvTimeSlots)

        val date = appUtils.universalDateConvert(
            dateSelected.toString(),
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "dd MMM"
        )
        val dateFull = appUtils.universalDateConvert(
            dateSelected.toString(),
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "dd MMM yyyy"
        )
        val dayOfTheWeek = DateFormat.format("EEE", dateSelected?.toLocalDate()?.toDate())

        Log.e("DATE", dayOfTheWeek.toString())

        dateList.clear()
        dateList.add(MeetingModel(dateFull, date, dayOfTheWeek.toString(), "1:00 PM to 2:00 PM"))
        dateList.add(MeetingModel(dateFull, date, dayOfTheWeek.toString(), "2:00 PM to 3:00 PM"))
        dateList.add(MeetingModel(dateFull, date, dayOfTheWeek.toString(), "3:00 PM to 4:00 PM"))
        dateList.add(MeetingModel(dateFull, date, dayOfTheWeek.toString(), "4:00 PM to 5:00 PM"))
        meetingAdapter.notifyDataSetChanged()
    }
}