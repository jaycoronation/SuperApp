package com.application.alphacapital.superapp.acpital;

import androidx.multidex.MultiDexApplication;
import com.application.alphacapital.superapp.acpital.service.ConnectivityReceiver;

public class CapitalMyApplication extends MultiDexApplication {
 
    private static CapitalMyApplication mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        mInstance = this;
    }
 
    public static synchronized CapitalMyApplication getInstance() {
        return mInstance;
    }
 
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}