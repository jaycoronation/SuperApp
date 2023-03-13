package com.application.alphacapital.superapp.supermain.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityFeedDetailsBinding
import com.application.alphacapital.superapp.supermain.utils.PicassoImageGetter

class FeedDetailsActivity : BaseActivity()
{
    private lateinit var binding : ActivityFeedDetailsBinding
    private var htmlText = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_feed_details)
        initView()
        onClick()
    }

    private fun initView()
    {
        if (intent.hasExtra("title"))
        {
            val title = intent.getStringExtra("title")
            binding.toolbar.tvTitle.text = title
            visible(binding.toolbar.tvTitle)
        }

        if (intent.hasExtra("data"))
        {
            htmlText = intent.getStringExtra("data").toString()
            htmlText.replace("'\'","")
            Log.e("TEXT HTML",htmlText)
        }

        val imageGetter = PicassoImageGetter(binding.tvHtml)
        val html: Spannable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY, imageGetter, null) as Spannable
        }
        else
        {
            Html.fromHtml(htmlText, imageGetter, null) as Spannable
        }
        binding.tvHtml.text = html

    }

    private fun onClick()
    {
        binding.toolbar.ivBack.backNav()
    }


}