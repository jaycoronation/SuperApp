package com.application.alphacapital.superapp.acpital;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.model.AppVersionResponseModel;
import com.application.alphacapital.superapp.acpital.network.CapitalApiClient;
import com.application.alphacapital.superapp.acpital.network.CapitalApiInterface;
import com.application.alphacapital.superapp.acpital.utils.AppAPIUtils;
import com.application.alphacapital.superapp.acpital.utils.AppUtils;
import com.application.alphacapital.superapp.acpital.utils.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapitalSplashActivity extends Activity
{
	private Activity activity;
	private SessionManager sessionManager;
	CapitalApiInterface apiService;

	private View ivLogo;

	private CountDownTimer timer;
	
	//Forecefull update
	public static boolean isVersionMismatch = false, isForcefulUpdateDialogOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				Window w = getWindow(); // in Activity's onCreate() for instance
				w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);

		isVersionMismatch = false;
		isForcefulUpdateDialogOpen = false;

		setContentView(R.layout.capital_activity_splash);
		
		activity = CapitalSplashActivity.this;
		sessionManager = new SessionManager(activity);
		apiService = Objects.requireNonNull(CapitalApiClient.INSTANCE.getClient()).create(CapitalApiInterface.class);
		AppUtils.saveDeviceInfo(activity);

		setupViews();

		setupData();

		/*printKeyHash();*/

		goThrough();

		if(sessionManager.isNetworkAvailable())
		{
			validateAppVersionAsync();
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void setupViews()
	{
		ivLogo = findViewById(R.id.ivLogo);
	}

	private void setupData()
	{
		try {
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.capital_splash_anim);
			ivLogo.setAnimation(animation);
			ivLogo.startAnimation(animation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goThrough()
	{
		timer = new CountDownTimer(2500, 1000)
		{
			@Override
			public void onTick(long millisUntilFinished)
			{

			}

			@Override
			public void onFinish()
			{
				try
				{
                    Intent intent = new Intent(activity, CapitalMainActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.capital_activity_open_translate,R.anim.capital_activity_close_scale);
                    activity.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.start();
	}

	private void printKeyHash()
	{
		try
		{
			PackageInfo info = getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	boolean isSuccess = false;

	private void validateAppVersionAsync()
	{
		try
		{
			apiService.getAppVersion(AppAPIUtils.TOKEN_ID,"true","false").enqueue(new Callback<AppVersionResponseModel>() {
				@Override
				public void onResponse(Call<AppVersionResponseModel> call, Response<AppVersionResponseModel> response) {
					if (response.isSuccessful())
					{
						String version = "";
						try
						{
							version = sessionManager.getDeviceAppVersion();
							Log.v("app version", version + "**");
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						assert response.body() != null;
						if (response.body().getStatus().equals("success"))
						{
							isSuccess = true;

							String[] arrVersionApp = version.split("\\.");
							String[] arrVersionLive = response.body().getAppVersion().split("\\.");

							int verApp = Integer.parseInt(arrVersionApp[0]+arrVersionApp[1]);
							int verLive = Integer.parseInt(arrVersionLive[0]+arrVersionLive[1]);

							if(verApp < verLive)
							{
								System.out.println("app needs to be upgraded");
								isVersionMismatch = true;
							}
							else
							{
								isVersionMismatch = false;
								System.out.println("app is upgraded");
							}
						}
						else
						{
							isSuccess = false;
						}
						if(!isSuccess)
						{
							isVersionMismatch = false;
						}
					}
					else
					{
						isSuccess = false;
						if(!isSuccess)
						{
							isVersionMismatch = false;
						}
					}
				}

				@Override
				public void onFailure(Call<AppVersionResponseModel> call, Throwable t) {
					AppUtils.apiFailedToast(activity);
				}
			});

			/*new AsyncTask<Void, Void, Void>()
			{
				private boolean isSuccess = false;

				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(Void... params)
				{
					checkAppVersion();

					return null;
				}

				private void checkAppVersion()
				{
					try
					{
						String version = "";

						try
						{
							version = sessionManager.getDeviceAppVersion();
							Log.v("app version", version + "**");
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						String URL = AppAPIUtils.GET_APP_VERSION;
						HashMap<String, String> hashMap = new HashMap<>();
						hashMap.put("ApiTokenId", AppAPIUtils.TOKEN_ID);
						hashMap.put("IsAdnroid", "true");
						hashMap.put("IsIOS", "false");

						String serverResponse = MitsUtils.readJSONServiceUsingPOST(URL, hashMap);

						try
						{
							JSONObject jsonObject = new JSONObject(serverResponse);
							String status = AppUtils.getValidAPIStringResponse(jsonObject.getString("status"));
							if(status.equalsIgnoreCase("success"))
							{
								isSuccess = true;

								final String appVersion = AppUtils.getValidAPIStringResponse(jsonObject.getString("appVersion"));
								String[] arrVersionApp = version.split("\\.");
								String[] arrVersionLive = appVersion.split("\\.");

								int verApp = Integer.parseInt(arrVersionApp[0]+arrVersionApp[1]);
								int verLive = Integer.parseInt(arrVersionLive[0]+arrVersionLive[1]);

								if(verApp < verLive)
								{
									System.out.println("app needs to be upgraded");
									isVersionMismatch = true;
								}
								else
								{
									isVersionMismatch = false;
									System.out.println("app is upgraded");
								}
							}
							else
							{
								isVersionMismatch = false;
								isSuccess = false;
							}
						}
						catch (Exception ex)
						{
							isSuccess = false;
							ex.printStackTrace();
						}
					}
					catch(Exception jse)
					{
						isSuccess = false;
						jse.printStackTrace();
					}
				}

				@Override
				protected void onPostExecute(Void result)
				{
					super.onPostExecute(result);
					if(!isSuccess)
					{
						isVersionMismatch = false;
					}
				}
			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
