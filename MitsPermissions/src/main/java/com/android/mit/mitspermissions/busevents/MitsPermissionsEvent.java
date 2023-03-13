package com.android.mit.mitspermissions.busevents;

import java.util.ArrayList;

public class MitsPermissionsEvent {

    public boolean permission;
    public ArrayList<String> deniedPermissions;


    public MitsPermissionsEvent(boolean permission, ArrayList<String> deniedPermissions
    ) {
        this.permission = permission;
        this.deniedPermissions = deniedPermissions;
    }



    public boolean hasPermission() {
        return permission;
    }


    public ArrayList<String> getDeniedPermissions() {
        return deniedPermissions;
    }
}
