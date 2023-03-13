package com.alphaestatevault.services

import com.application.alphacapital.superapp.vault.utils.MyApplication
import com.application.alphacapital.superapp.vault.utils.VaultSessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService()
{
    private val ADMIN_CHANNEL_ID = "admin_channel"
    val GROUP_KEY_BUNDLED = "group_key_bundled"
    val sessionManager = VaultSessionManager(MyApplication.instance!!)

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        if(!sessionManager.isLoggedIn())
        {
            return
        }
    }
}
