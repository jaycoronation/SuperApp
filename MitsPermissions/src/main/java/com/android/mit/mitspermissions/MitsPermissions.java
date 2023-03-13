package com.android.mit.mitspermissions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.StringRes;

import com.android.mit.mitspermissions.utils.Dlog;
import com.android.mit.mitspermissions.utils.ObjectUtils;

public class MitsPermissions
{
	private static MitsInstance instance;

	public MitsPermissions(Context context)
	{
		instance = new MitsInstance(context);
	}

	public MitsPermissions setPermissionListener(PermissionListener listener)
	{
		instance.listener = listener;

		return this;
	}

	public MitsPermissions setPermissions(String... permissions)
	{
		instance.permissions = permissions;
		return this;
	}

	public MitsPermissions setRationaleMessage(String rationaleMessage)
	{
		instance.rationaleMessage = rationaleMessage;
		return this;
	}

	@SuppressLint("ResourceType")
    public MitsPermissions setRationaleMessage(@StringRes int stringRes)
	{
		if (stringRes <= 0)
			throw new IllegalArgumentException("Invalid value for RationaleMessage");

		instance.rationaleMessage = instance.context.getString(stringRes);
		return this;
	}

	public MitsPermissions setDeniedMessage(String denyMessage)
	{
		instance.denyMessage = denyMessage;
		return this;
	}

	@SuppressLint("ResourceType")
    public MitsPermissions setDeniedMessage(@StringRes int stringRes)
	{
		if (stringRes <= 0)
			throw new IllegalArgumentException("Invalid value for DeniedMessage");

		instance.rationaleMessage = instance.context.getString(stringRes);
		return this;
	}

	public MitsPermissions setGotoSettingButton(boolean hasSettingBtn)
	{
		instance.hasSettingBtn = hasSettingBtn;
		return this;
	}

	public MitsPermissions setRationaleConfirmText(String rationaleConfirmText)
	{
		instance.rationaleConfirmText = rationaleConfirmText;
		return this;
	}

	@SuppressLint("ResourceType")
    public MitsPermissions setRationaleConfirmText(@StringRes int stringRes)
	{
		if (stringRes <= 0)
			throw new IllegalArgumentException("Invalid value for RationaleConfirmText");

		instance.rationaleConfirmText = instance.context.getString(stringRes);

		return this;
	}

	public MitsPermissions setDeniedCloseButtonText(String deniedCloseButtonText)
	{
		instance.deniedCloseButtonText = deniedCloseButtonText;
		return this;
	}

	@SuppressLint("ResourceType")
    public MitsPermissions setDeniedCloseButtonText(@StringRes int stringRes)
	{
		if (stringRes <= 0)
			throw new IllegalArgumentException("Invalid value for DeniedCloseButtonText");

		instance.deniedCloseButtonText = instance.context.getString(stringRes);

		return this;
	}

	public void check()
	{
		if (instance.listener == null)
		{
			throw new NullPointerException("You must setPermissionListener() on MitsPermission");
		}
		else if (ObjectUtils.isEmpty(instance.permissions))
		{
			throw new NullPointerException("You must setPermissions() on MitsPermission");
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
			Dlog.d("preMarshmallow");
			instance.listener.onPermissionGranted();
		}
		else
		{
			Dlog.d("Marshmallow");
			instance.checkPermissions();
		}
	}
}
