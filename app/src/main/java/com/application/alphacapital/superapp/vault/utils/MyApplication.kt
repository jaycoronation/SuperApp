package com.application.alphacapital.superapp.vault.utils

import androidx.multidex.MultiDexApplication
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver.ConnectivityReceiverListener
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo

open class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
            RxPaparazzo.register(this);
        mInstance = this
    }

    @Synchronized
    open fun getInstance(): MyApplication?
    {
        return mInstance
    }

    fun setConnectivityListener(listener: ConnectivityReceiverListener?)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {
        var instance: MyApplication? =null
        var mInstance: MyApplication? = null

    }
}