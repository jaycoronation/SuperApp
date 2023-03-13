package com.application.alphacapital.superapp.vault.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivitySplashBinding

class VaultSplashActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivitySplashBinding
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //3 seconds

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_splash)
        setStatusBarGradiant(activity)
        setAnimation()
    }

    private fun setAnimation()
    {
        try
        {
            val animation = AnimationUtils.loadAnimation(this, R.anim.vault_splash_anim)
            binding.ivLogo.visibility = View.VISIBLE
            binding.ivLogo.startAnimation(animation)
            setHandler()
        }
        catch (e: Exception)
        {
        }
    }

    private fun setHandler()
    {
        try
        {
            mDelayHandler = Handler()
            //Navigate with delay
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        }
        catch (e: Exception)
        {
        }
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing)
        {
           if(sessionManager.isLoggedIn())
           {
               val intent = Intent(applicationContext, VaultHomeActivity::class.java)
               startActivity(intent)
               startActivityAnimation()
               finish()
           }
           else
           {
               val intent = Intent(applicationContext, VaultLoginActivity::class.java)
               startActivity(intent)
               startActivityAnimation()
               finish()
           }
        }
    }

    public override fun onDestroy()
    {
        if (mDelayHandler != null)
        {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}
