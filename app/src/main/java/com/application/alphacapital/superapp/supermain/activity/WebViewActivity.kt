package com.application.alphacapital.superapp.supermain.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityWebViewBinding
import com.application.alphacapital.superapp.supermain.model.AdditionalLinksResponseModel
import retrofit2.Call
import retrofit2.Response

class WebViewActivity : BaseActivity()
{
    private lateinit var binding: ActivityWebViewBinding
    private var url = "";
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_web_view)
        initView()
    }

    private fun initView()
    {
        binding.toolbar.ivRefresh.visibility = View.VISIBLE

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.toolbar.ivRefresh.setOnClickListener {
            binding.webView.clearCache(true)
            binding.webView.loadUrl(url)
        }

        val settings: WebSettings = binding.webView.settings
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.displayZoomControls = false
        settings.setAppCacheEnabled(true)

        if (intent.hasExtra("isFor"))
        {
            val isFor = intent.getStringExtra("isFor")
            if (isFor == "meeting")
            {
                url = "https://alphacapital.coronation.in/calendly/index.html"
                binding.webView.loadUrl(url)
            }
            else if (isFor == "blog")
            {
                callAdditionalLinksApi("Blog")
            }
            else if (isFor == "videos")
            {
                callAdditionalLinksApi("Video")
            }
        }

        binding.webView.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?)
            {
                binding.llLoading.llLoading.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
            {
                binding.llLoading.llLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun callAdditionalLinksApi(isFrom: String)
    {
        binding.llLoading.llLoading.visibility = View.VISIBLE
        apiService.callAdditionalAPI().enqueue(object : retrofit2.Callback<AdditionalLinksResponseModel>
        {
            override fun onResponse(call: Call<AdditionalLinksResponseModel>, response: Response<AdditionalLinksResponseModel>)
            {
                if (isOnline())
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {
                            val apidata = response.body()!!

                            for (i in apidata.links.indices)
                            {
                                if (isFrom == "Blog")
                                {
                                    if (apidata.links[i].name == "blog")
                                    {
                                        url = apidata.links[i].link
                                        binding.webView.loadUrl(url)
                                    }
                                }
                                else if (isFrom == "Video")
                                {
                                    if (apidata.links[i].name == "videos")
                                    {
                                        url = apidata.links[i].link
                                        binding.webView.loadUrl(url)
                                    }
                                }
                            }
                        }
                        else
                        {
                            showToast(response.body()!!.message)
                        }
                        binding.llLoading.llLoading.visibility = View.GONE
                    }
                }
                else
                {
                }
            }

            override fun onFailure(call: Call<AdditionalLinksResponseModel>, t: Throwable)
            {
                apiFailedToast()
            }
        })
    }
}