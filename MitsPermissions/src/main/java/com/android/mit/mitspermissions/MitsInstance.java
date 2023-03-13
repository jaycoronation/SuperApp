package com.android.mit.mitspermissions;

import android.content.Context;
import android.content.Intent;

import com.android.mit.mitspermissions.busevents.MitsBusProvider;
import com.android.mit.mitspermissions.busevents.MitsPermissionsEvent;
import com.android.mit.mitspermissions.utils.Dlog;
import com.squareup.otto.Subscribe;

public class MitsInstance
{
	public PermissionListener listener;
	public String[] permissions;
	public String rationaleMessage;
	public String denyMessage;
	public boolean hasSettingBtn = true;

	public String deniedCloseButtonText;
	public String rationaleConfirmText;
	Context context;

	public MitsInstance(Context context)
	{
		this.context = context;

		MitsBusProvider.getInstance().register(this);

		deniedCloseButtonText = "CLOSE";
		rationaleConfirmText = "CONFIRM";
	}

	public void checkPermissions()
	{
		Intent intent = new Intent(context, MitsPermissionsActivity.class);
		intent.putExtra(MitsPermissionsActivity.EXTRA_PERMISSIONS, permissions);

		intent.putExtra(MitsPermissionsActivity.EXTRA_RATIONALE_MESSAGE, rationaleMessage);
		intent.putExtra(MitsPermissionsActivity.EXTRA_DENY_MESSAGE, denyMessage);
		intent.putExtra(MitsPermissionsActivity.EXTRA_PACKAGE_NAME, context.getPackageName());
		intent.putExtra(MitsPermissionsActivity.EXTRA_SETTING_BUTTON, hasSettingBtn);
		intent.putExtra(MitsPermissionsActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, deniedCloseButtonText);
		intent.putExtra(MitsPermissionsActivity.EXTRA_RATIONALE_CONFIRM_TEXT, rationaleConfirmText);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Subscribe
	public void onPermissionResult(MitsPermissionsEvent event)
	{
		Dlog.d("");
		if (event.hasPermission())
		{
			listener.onPermissionGranted();
		}
		else
		{
			listener.onPermissionDenied(event.getDeniedPermissions());
		}
		MitsBusProvider.getInstance().unregister(this);
	}
}
