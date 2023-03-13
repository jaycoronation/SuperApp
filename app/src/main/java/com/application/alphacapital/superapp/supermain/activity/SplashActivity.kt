package com.application.alphacapital.superapp.supermain.activity

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivitySplashBinding
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.application.alphacapital.superapp.pms.utils.SessionManager
import com.application.alphacapital.superapp.vault.services.ServiceUtils
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity()
{
    private lateinit var binding: ActivitySplashBinding
    private lateinit var pmsSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_splash)
        pmsSession = SessionManager(activity)
        setStatusBarGradiant(activity)
        setupData()
        goThrough()
    }


    private fun setupData()
    {
        try
        {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.capital_splash_anim)
            binding.tvLogo.animation = animation
            binding.tvLogo.startAnimation(animation)

            if(pmsSession.isLoggedIn)
            {
                ApiUtils.END_USER_ID = pmsSession.userId
                val serviceUtils = ServiceUtils()
                serviceUtils.scheduleJob(activity)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun goThrough()
    {
        val timer = object : CountDownTimer(2500, 1000)
        {
            override fun onTick(millisUntilFinished: Long)
            {
            }

            override fun onFinish()
            {
                try
                {
                    if (pmsSession.isLoggedIn)
                    {
                        startActivity<MainActivity>()
                        startActivityAnimation()
                        activity.finish()
                    }
                    else
                    {
                        startActivity<MainLoginActivity>()
                        startActivityAnimation()
                        activity.finish()
                    }

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        timer.start()
    }
}