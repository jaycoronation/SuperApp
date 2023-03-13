package com.application.alphacapital.superapp.finplan.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_splash.*
import org.jetbrains.anko.startActivity

class FinPlanSplashActivity : FinPlanBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_splash)
        setAnimation()
    }

    val timer = object: CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            if(sessionManager.isLoggedIn()){
                finish()
                startActivity<FinPlanMainActivity>()
                startActivityAnimation()
            }else{
                finish()
                startActivity<FinPlanLoginActivity>()
                startActivityAnimation()
            }
        }
    }

    private fun setAnimation()
    {
        try
        {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fin_plan_splash_anim)
            ivLogo.visibility = View.VISIBLE
            ivLogo.startAnimation(animation)
            timer.start()
        }
        catch (e: Exception)
        {
        }
    }
}
