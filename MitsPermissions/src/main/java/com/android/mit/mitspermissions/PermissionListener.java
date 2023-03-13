package com.android.mit.mitspermissions;

import java.util.ArrayList;

public interface PermissionListener
{
     void onPermissionGranted();

     void onPermissionDenied(ArrayList<String> deniedPermissions);
}
