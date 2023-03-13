package com.application.alphacapital.superapp.acpital.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.acpital.CapitalLocateUsActivity;
import com.application.alphacapital.superapp.acpital.CapitalSplashActivity;
import com.application.alphacapital.superapp.acpital.classes.RangeTimePickerDialog;
import com.application.alphacapital.superapp.acpital.textutils.CapitalSnackbarHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")
public class AppUtils
{
    /**
     * check if string contains characters only. Use for name validation
     *
     * @param name
     * @return false if contains non character number or special character, true if valid
     */
    public static boolean isAlpha(String name)
    {
        return name.matches("[a-zA-Z]+");
    }

    /**
     * check if entered email address is valid
     *
     * @param email email address
     * @return true if valid else false
     */
    public static boolean validateEmail(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * use to replace null value with blank value
     */
    public static String getValidAPIStringResponse(String value)
    {
        if (value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("<null>"))
        {
            value = "";
        }
        return value.trim();
    }

    public static String removeDiacriticalMarks(String string)
    {
        return string;
		/*try {
			return getValidAPIStringResponse(Normalizer.normalize(string, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return getValidAPIStringResponse(string);
		}*/
    }

    /**
     * use to get valid boolean from string
     */
    public static boolean getValidAPIBooleanResponse(String value)
    {
        boolean flag = false;
        try
        {
            value = getValidAPIStringResponse(value);

            if (value.equalsIgnoreCase("true"))
            {
                flag = true;
            }
        }
        catch (Exception e)
        {
        }

        return flag;
    }

    /**
     * use to get valid integer from string
     *
     * @param value
     * @return
     */
    public static int getValidAPIIntegerResponse(String value)
    {
        int val = 0;
        value = getValidAPIStringResponse(value);

        if (value.contains("."))
        {
            try
            {
                float f = Float.parseFloat(value);
                val = (int) f;
            }
            catch (NumberFormatException e)
            {
                val = 0;
            }
        }
        else
        {
            try
            {
                val = Integer.parseInt(value);
            }
            catch (NumberFormatException e)
            {
                val = 0;
            }
        }

        return val;
    }

    /**
     * use to get valid double from string
     *
     * @param value
     * @return
     */
    public static double getValidAPIDoubleResponse(String value)
    {
        double val = 0.0;
        value = getValidAPIStringResponse(value);

        try
        {
            val = Double.parseDouble(value);
        }
        catch (NumberFormatException e1)
        {
            val = 0.0;
        }

        return val;
    }

    /**
     * use to get valid long from string
     *
     * @param value
     * @return
     */
    public static long getValidAPILongResponse(String value)
    {
        long l = 0;
        try
        {
            value = getValidAPIStringResponse(value);
            if (value.length() == 0)
            {
                l = 0;
            }
            else
            {
                l = Long.parseLong(value);
            }
        }
        catch (Exception e)
        {
        }
        return l;
    }

    public static boolean isOnline(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("LOG_TAG", "Error checking internet connection", e);
            }
        } else {
            Log.d("LOG_TAG", "No network available!");
        }
        return false;
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getValidAPIStringResponseHas(JSONObject jsonObject, String value)
    {
        if (jsonObject.has(value))
        {
            try
            {
                value = getValidAPIStringResponse(jsonObject.getString(value));

            }
            catch (JSONException e)
            {
                value = "";
                e.printStackTrace();
            }

        }
        else
        {
            value = "";
        }

        return value.trim();
    }

    /**
     * use to get valid boolean from string
     */
    public static boolean getValidAPIBooleanResponseHas(JSONObject jsonObject, String value)
    {
        boolean flag = false;
        try
        {
            if (jsonObject.has(value))
            {
                value = getValidAPIStringResponse(jsonObject.getString(value));

                if (value.equalsIgnoreCase("true"))
                {
                    flag = true;
                }
            }
            else
            {
                flag = false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * use to get valid integer from string
     *
     * @param value
     * @return
     */
    public static int getValidAPIIntegerResponseHas(JSONObject jsonObject, String value)
    {
        int val = 0;

        if (jsonObject.has(value))
        {
            try
            {
                value = getValidAPIStringResponse(jsonObject.getString(value));

                if (value.contains("."))
                {
                    try
                    {
                        float f = Float.parseFloat(value);
                        val = (int) f;
                    }
                    catch (NumberFormatException e)
                    {
                        val = 0;
                    }
                }
                else
                {
                    try
                    {
                        val = Integer.parseInt(value);
                    }
                    catch (NumberFormatException e)
                    {
                        val = 0;
                    }
                }
            }
            catch (JSONException e)
            {
                val = 0;
                e.printStackTrace();
            }
        }
        else
        {
            val = 0;
        }

        return val;
    }

    public static float getValidAPIFloatResponseHas(JSONObject jsonObject, String value)
    {
        float val = 0.0f;
        if (jsonObject.has(value))
        {
            try
            {
                value = getValidAPIStringResponse(jsonObject.getString(value));
                val = Float.parseFloat(value);
            }
            catch (Exception e)
            {
                val = 0.0f;
            }
        }
        else
        {
            val = 0.0f;
        }


        return val;
    }

    /**
     * use to get valid double from string
     *
     * @param value
     * @return
     */
    public static double getValidAPIDoubleResponseHas(JSONObject jsonObject, String value)
    {
        double val = 0.0;

        if (jsonObject.has(value))
        {
            try
            {
                value = getValidAPIStringResponse(jsonObject.getString(value));
                val = Double.parseDouble(value);
            }
            catch (Exception e1)
            {
                val = 0.0;
                e1.printStackTrace();
            }
        }
        else
        {
            val = 0.0;
        }


        return val;
    }

    /**
     * use to get valid long from string
     *
     * @param value
     * @return
     */
    public static long getValidAPILongResponseHas(JSONObject jsonObject, String value)
    {
        long l = 0;

        if (jsonObject.has(value))
        {
            try
            {
                l = getValidAPILongResponse(jsonObject.getString(value));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            l = 0;
        }

        return l;
    }

    /**
     * get possibility in boolean from 0 or 1
     *
     * @param number 0 or 1
     * @return true if number is 1 else false
     */
    public static boolean getFlagFromInt(int number)
    {
        boolean flag = (number == 1) ? true : false;
        return flag;
    }

    /**
     * get current date in dd MMM, yyyy format
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateString()
    {
        String str = "";
        try
        {
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            str = dateFormat.format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * get string in dd MMM, yyyy format from millis
     *
     * @param l millis
     * @return date string
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertDateToString(long l)
    {
        String str = "";
        try
        {
            Date date = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            str = dateFormat.format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * get local time timestamp
     *
     * @param time timestamp to convert
     * @return local timestamp
     */
    public static long getLocalTimestamp(final long time)
    {
        long timestamp = 0;
        try
        {
            Date localTime = new Date(time);
            String format = "dd MMM, yyyy HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            // Convert Local Time to UTC (Works Fine)
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            @SuppressWarnings("deprecation") Date gmtTime = new Date(sdf.format(localTime));
            // Convert UTC to Local Time
//			Date fromGmt = new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
//			System.out.println("LOCAL TIME : " + fromGmt.toString());
            timestamp = gmtTime.getTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * get relative date time like Yesterday, 4:10 PM or 2 hours ago
     *
     * @param activity
     * @param timestamp timestamp to convert
     * @return relative datetime
     */
    public static String getRelativeDateTime(final Activity activity, final long timestamp)
    {
        String datetime = "";
        try
        {
            datetime = DateUtils.getRelativeDateTimeString(activity, timestamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();

            if (datetime.contains("/"))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, h:mm a");
                Date date = new Date(timestamp);
                datetime = sdf.format(date);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return datetime;
    }

    /**
     * check if url is valid or not
     *
     * @param url
     * @return true if valid else false
     */
    public static boolean validateWebUrl(CharSequence url)
    {
        try
        {
            return android.util.Patterns.WEB_URL.matcher(url).matches();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * check if password is valid or not
     *
     * @param password
     * @return true if valid else false
     */
    public static boolean validPassword(String password)
    {
        Pattern[] checks = {
                // special character
//	        Pattern.compile("[!@#\\$%^&*()~`\\-=_+\\[\\]{}|:\\\";',\\./<>?]"),
                // small character
                Pattern.compile("[a-z]"),
                // capital character
                Pattern.compile("[A-Z]"),
                // numeric character
                Pattern.compile("\\d"),
                // 6 to 40 character length
                Pattern.compile("^.{6,40}$")};
        boolean ok = true;
        for (Pattern check : checks)
        {
            ok = ok && check.matcher(password).find();
        }

        return ok;
    }

    /**
     * hide virtual keyboard
     *
     * @param view     edittext
     * @param activity context
     */
    public static void hideKeyboard(View view, Activity activity)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * show virtual keyboard
     *
     * @param activity context
     */
    public static void showKeyboard(EditText editText, Activity activity)
    {
        try
        {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * get string with every first letter of word in capital
     *
     * @param s
     * @return string
     */
    public static String toDisplayCase(String s)
    {
        String strToReturn = "";
        try
        {
            final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following to be capitalized

            StringBuilder sb = new StringBuilder();
            boolean capNext = true;

            for (char c : s.toCharArray())
            {
                c = (capNext) ? Character.toUpperCase(c) : Character.toLowerCase(c);
                sb.append(c);
                capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
            }
            strToReturn = sb.toString();
        }
        catch (Exception e)
        {
            strToReturn = s;
            e.printStackTrace();
        }
        return strToReturn;
    }

    public static ArrayList<String> sortStringList(ArrayList<String> result)
    {
        try
        {
            Collections.sort(result, new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return (o1.toLowerCase(Locale.getDefault()).compareTo(o2.toLowerCase(Locale.getDefault())));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get dp value from pixels
     *
     * @param context activity context
     * @param px      pixels
     * @return dp
     */
    public static float dpFromPx(final Context context, final float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * get pixels value from dp
     *
     * @param context activity context
     * @param dp      dp
     * @return pixels
     */
    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * get text in capital style
     *
     * @param text
     * @return capital text
     */
    public static String getCapitalText(String text)
    {
        String desiredText = "";
        try
        {
            desiredText = text.toUpperCase(Locale.ENGLISH);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return desiredText;
    }

    /**
     * get encoded string in md5 format
     *
     * @param input text to encode
     * @return encoded string
     */
    public static String getMD5(String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Typeface getTypefaceRegular(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_regular));
    }

    public static Typeface getTypefaceMedium(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_medium));
    }

    public static Typeface getTypefaceSemiBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_semibold));
    }

    public static Typeface getTypefaceBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.font_bold));
    }

    private static final int WIDTH_INDEX = 0;
    private static final int HEIGHT_INDEX = 1;

    public static int[] getScreenSize(Context context)
    {
        int[] widthHeight = new int[2];
        widthHeight[WIDTH_INDEX] = 0;
        widthHeight[HEIGHT_INDEX] = 0;

        try
        {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();

            Point size = new Point();
            display.getSize(size);
            widthHeight[WIDTH_INDEX] = size.x;
            widthHeight[HEIGHT_INDEX] = size.y;

            if (!isScreenSizeRetrieved(widthHeight))
            {
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                widthHeight[0] = metrics.widthPixels;
                widthHeight[1] = metrics.heightPixels;
            }

            // Last defense. Use deprecated API that was introduced in lower than API 13
            if (!isScreenSizeRetrieved(widthHeight))
            {
                widthHeight[0] = display.getWidth(); // deprecated
                widthHeight[1] = display.getHeight(); // deprecated
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return widthHeight;
    }

    private static boolean isScreenSizeRetrieved(int[] widthHeight)
    {
        return widthHeight[WIDTH_INDEX] != 0 && widthHeight[HEIGHT_INDEX] != 0;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;
        try
        {
            if (drawable instanceof BitmapDrawable)
            {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable.getBitmap() != null)
                {
                    return bitmapDrawable.getBitmap();
                }
            }
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            }
            else
            {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void shareCodeSpecificApp(final String title, final String message, final Activity activity)
    {
        try
        {
            Intent emailIntent = new Intent();
            emailIntent.setAction(Intent.ACTION_SEND);
            // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            emailIntent.setType("message/rfc822");

            PackageManager pm = activity.getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");

            Intent openInChooser = Intent.createChooser(emailIntent, "Share via");

            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<>();
            for (int i = 0; i < resInfo.size(); i++)
            {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                if (packageName.contains("android.email"))
                {
                    emailIntent.setPackage(packageName);
                }
                else if (packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("whatsapp") || packageName.contains("com.jio.join") || packageName.contains("com.google.android.apps.messaging") || packageName.contains("com.google.android.talk"))
                {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, message);

                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            activity.startActivity(openInChooser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void ShowSnackBar(Activity activity, View v, String message, String primarycolor)
    {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTypeface(AppUtils.getTypefaceRegular(activity));
        sbView.setBackgroundColor(Color.parseColor(primarycolor));

        snackbar.show();
    }

    public static String getEncodedText(String text)
    {
        String encodedText = "";

        try
        {
            byte[] data = text.getBytes("UTF-8");
            encodedText = Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            encodedText = text;
        }

        return encodedText;
    }

    public static String getDecodedText(String text)
    {
        String decodedText = "";

        try
        {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            decodedText = new String(data, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            decodedText = text;
        }

        return decodedText;
    }

    // TODO : show version mismatch alert
    public static void showVersionMismatchDialog(final Activity activity)
    {
        try
        {
            String titleText = "Upgrade";
            String appname = activity.getResources().getString(R.string.app_name);
            String boldappname = "<b>" + appname + "</b>";
            String messageText = "A new version of " + boldappname + " is ready for installation. Please upgrade to continue.";

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(titleText);
            alertDialogBuilder.setMessage(Html.fromHtml(messageText));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Upgrade", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    try
                    {
                        dialog.dismiss();
                        dialog.cancel();

                        CapitalSplashActivity.isForcefulUpdateDialogOpen = false;

                        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                        try
                        {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                        catch (android.content.ActivityNotFoundException anfe)
                        {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                        activity.finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
            textView.setTypeface(AppUtils.getTypefaceRegular(activity));

            Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            button.setTypeface(AppUtils.getTypefaceMedium(activity));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveDeviceInfo(final Activity activity)
    {
        try
        {
            final SessionManager sessionManager = new SessionManager(activity);

            try
            {
                PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                String currentAppVersion = pInfo.versionName;
                sessionManager.setDeviceAppVersion(currentAppVersion);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            String android_id = AppUtils.getValidAPIStringResponse(sessionManager.getUserDeviceId());
            if (android_id.length() == 0)
            {
                android_id = AppUtils.getValidAPIStringResponse(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                sessionManager.setUserDeviceId(android_id);
            }

            String phoneModel = AppUtils.getValidAPIStringResponse(sessionManager.getDeviceModelName());
            if (phoneModel.length() == 0)
            {
                // Device model
                phoneModel = Build.MODEL;
                sessionManager.setDeviceModelName(phoneModel);
            }

            String androidVersion = AppUtils.getValidAPIStringResponse(sessionManager.getDeviceOS());
            if (androidVersion.length() == 0)
            {
                // Android version
                androidVersion = Build.VERSION.RELEASE;
                sessionManager.setDeviceOS(androidVersion);
            }
            /*Toast.makeText(activity, "Model : " + phoneModel + " & OS : " + androidVersion, Toast.LENGTH_LONG).show();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void writeToCustomFileHashmap(final String data, final String filename)
    {
        try
        {
            File myFileDir = new File(Environment.getExternalStorageDirectory() + "/Alpha Capital/text/");
            if (!myFileDir.exists())
            {
                myFileDir.mkdirs();
            }

            File myFile = new File(Environment.getExternalStorageDirectory() + "/Alpha Capital/text/" + filename);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            // Toast.makeText(getApplicationContext(), "File saved on " + myFile.getAbsolutePath() + " ", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
        }
    }

    public static void animateHide(final Activity activity, final View view)
    {
        try
        {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.capital_activity_fade_out);
            view.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    try
                    {
                        view.setVisibility(View.GONE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void animateShow(final Activity activity, final View view)
    {
        try
        {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.capital_activity_fade_in);
            animation.setDuration(500);
            view.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                    try
                    {
                        view.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void removeError(final EditText edt, final TextInputLayout layout)
    {
        edt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().length() > 0)
                {
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    public static void gotoContactUs(final Activity activity, View v)
    {
        v.setVisibility(View.VISIBLE);
        v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SessionManager sessionManager = new SessionManager(activity);

                Intent intent = new Intent(activity, CapitalLocateUsActivity.class);
                intent.putExtra("menuId", sessionManager.getContactMenuId());
                intent.putExtra("title", sessionManager.getContactMenuTitle());
                intent.putExtra("menuIcon", sessionManager.getContactMenuImage());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.capital_activity_open_translate, R.anim.capital_activity_close_scale);
            }
        });
    }

    public static String readJSONServiceUsingPOST(String urlString, HashMap<String, String> params)
    {
        StringBuilder builder = new StringBuilder();

        try
        {
            StrictMode.ThreadPolicy policy = (new StrictMode.ThreadPolicy.Builder()).permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic QWxwaGFjYXBpdGFsOkFwcC00U1RTQTY0TzYyV1FXWlIwUVNJSw==");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            boolean isError = connection.getResponseCode() >= 400;
            InputStream inputStream;
            BufferedReader rd;
            String line;
            if (isError)
            {
                inputStream = connection.getErrorStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while ((line = rd.readLine()) != null)
                {
                    builder.append(line);
                }

                Log.e("MitsUtils r", builder.toString());
            }
            else
            {
                inputStream = connection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                line = "";

                while ((line = rd.readLine()) != null)
                {
                    builder.append(line);
                }
            }
        }
        catch (ConnectException var12)
        {
            var12.printStackTrace();
        }
        catch (IOException var13)
        {
            var13.printStackTrace();
        }

        return builder.toString();
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();

        try
        {
            boolean first = true;
            Iterator var4 = params.entrySet().iterator();

            while (var4.hasNext())
            {
                Map.Entry<String, String> entry = (Map.Entry) var4.next();
                if (first)
                {
                    first = false;
                }
                else
                {
                    result.append("&");
                }
                result.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
            }
        }
        catch (Exception var5)
        {
            var5.printStackTrace();
        }

        return result.toString();
    }

    public static void printLog(Activity activity, String param, String value)
    {
        try
        {
            Log.e(param, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        TextView textView;

        public SelectDateFragment(TextView textView)
        {
            this.textView = textView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            DatePickerDialog datepicker = null;

            if (textView.getText().length() > 0 && !textView.getText().equals("Select Date"))
            {
                try
                {
                    String[] datearr = textView.getText().toString().split("/");
                    int dateint = Integer.parseInt(datearr[0]);
                    int monthint = Integer.parseInt(datearr[1]);
                    int yearint = Integer.parseInt(datearr[2]);
                    datepicker = new DatePickerDialog(getActivity(), this, yearint, monthint - 1, dateint);
                    datepicker.getDatePicker().setMinDate(new Date().getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    final Calendar calendar = Calendar.getInstance();
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    datepicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);
                    datepicker.getDatePicker().setMinDate(new Date().getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            return datepicker;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm + 1, dd, textView);
        }

    }

    public static void populateSetDate(int year, int month, int day, TextView textView)
    {
        String Selected_date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate2 = new Date();
        try
        {
            convertedDate2 = dateFormat.parse(Selected_date);
            String showDate = df.format(convertedDate2);
            Log.e("showDate", showDate.toString());
            textView.setText(showDate);
            textView.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showTimePickerDialog(final TextView textView, final Activity activity)
    {
        try
        {

            final Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            if (!textView.getText().toString().equalsIgnoreCase("Select Time") && !textView.getText().toString().equals(""))
            {
                try
                {
                    String time = textView.getText().toString().trim();
                    boolean isPm = false;

                    if (time.contains("AM"))
                    {
                        time = time.replace("AM", "").trim();
                        isPm = false;
                    }
                    else if (time.contains("PM"))
                    {
                        time = time.replace("PM", "").trim();
                        isPm = true;
                    }

                    String[] splitedTime = time.split(":");

                    hour = Integer.parseInt(splitedTime[0]);
                    minute = Integer.parseInt(splitedTime[1]);

                    Log.e("selectedHour before ", hour + "");

                    if (isPm)
                    {
                        if (hour != 12 && hour < 12)
                        {
                            hour = hour + 12;
                        }
                        else
                        {
                            hour = hour;
                        }
                    }
                    else
                    {
                        hour = hour;
                    }

                    Log.e("selectedHour after ", hour + "");
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                }
            }
            else
            {
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
            }

            final RangeTimePickerDialog mTimePicker;

            mTimePicker = new RangeTimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                {

                    String AM_PM;

                    Log.e("selectedHour select", selectedHour + "");

                    if (selectedHour < 12)
                    {
                        AM_PM = "AM";
                    }
                    else
                    {
                        AM_PM = "PM";
                        selectedHour = selectedHour - 12;
                        if (selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                    }

                    String newHour = "", newMinute = "";

                    if (selectedHour <= 9)
                    {
                        newHour = "0" + selectedHour;
                    }
                    else
                    {
                        newHour = String.valueOf(selectedHour);
                    }

                    if (selectedMinute <= 9)
                    {
                        newMinute = "0" + selectedMinute;
                    }
                    else
                    {
                        newMinute = String.valueOf(selectedMinute);
                    }

                    String selectedTime = newHour + ":" + newMinute + " " + AM_PM;

                    textView.setText(selectedTime);
                }
            }, hour, minute, false);//true = 24 hour time

            mTimePicker.setTitle("Select Time");

            mTimePicker.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String universalDateConvert(String str, String FromFormat, String ToFormat)
    {
        String newString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(FromFormat);
        Date d;
        try
        {
            d = sdf.parse(str);
            sdf.applyPattern(ToFormat);
            newString = sdf.format(d);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return newString;
    }

    public static void startActivityAnimation(Activity activity)
    {
        MitsUtils.hideKeyboard(activity);
        //activity.overridePendingTransition(R.anim.capital_push_left_in, R.anim.capital_push_left_out);
    }

    public static void finishActivityAnimation(Activity activity)
    {
        MitsUtils.hideKeyboard(activity);
        //activity.overridePendingTransition(R.anim.capital_push_right_in, R.anim.capital_push_right_out);
    }

    /*public static void setLightStatusBar(Activity activity)
    {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparent));
            activity.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.img_portfolio));
        }
    }*/

    public static void setLightStatusBar(Activity activity)
    {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_VISIBLE;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparent));
            activity.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.capital_img_portfolio));
        }
    }

    public static void setLightStatusBarDialog(Dialog dialog, Activity activity)
    {
        Window window = dialog.getWindow();
        View view = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            dialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.tool_bar_bg));
        }
    }

    public static void setLightStatusBarBottomDialog(Dialog dialog, Activity activity)
    {
        Window window = dialog.getWindow();

        View view = window.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            dialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparent));
        }
    }

    public static void showLogoutDialog(final Activity activity, final SessionManager sessionManager)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.capital_dialog_logout, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final TextView txtMsg;
        final LinearLayout llCancel;
        final LinearLayout llLogout;

        txtMsg = (TextView) sheetView.findViewById(R.id.txtMsg);
        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llLogout = (LinearLayout) sheetView.findViewById(R.id.llLogout);


        llCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottomSheetDialog.dismiss();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottomSheetDialog.dismiss();
                sessionManager.logoutUser();
            }
        });


        bottomSheetDialog.show();
    }

    public static String formatDoubleIntoString(double doubleValue)
    {
        if (doubleValue == (int) doubleValue) return String.format("%d", (int) doubleValue);
        else return String.format("%s", doubleValue);
    }

    public static String getCurrentMonth()
    {
        return String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    public static String getCurrentYear()
    {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getFormattedAmount(Activity activity, long number)
    {
        return activity.getString(R.string.ruppe) + NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String getEmployeeIdForAdmin(Activity activity)
    {
        SessionManager sessionManager = new SessionManager(activity);
        if (sessionManager.isAdmin())
        {
            return sessionManager.getEmpIdForAdmin();
        }
        else
        {
            return sessionManager.getUserId();
        }
    }

    public static String getHashtagFromString(final String str, final int start)
    {
        String hashtag = "";

        int startIndexOfNextHashSign = 0;

        int index = 0;
        try
        {
            while (index < start)
            {
                char sign = str.charAt(index);
                int nextNotLetterDigitCharIndex = index + 1; // we assume it is next. if if was not changed by findNextValidHashTagChar then index will be incremented by 1
                // if (sign == '#')
                if (sign == '@')
                {
                    startIndexOfNextHashSign = index;

                    nextNotLetterDigitCharIndex = findNextValidHashTagChar(str, startIndexOfNextHashSign);

                    Log.v("indexes", "index = " + nextNotLetterDigitCharIndex + " && start = " + start + "");

                    if (nextNotLetterDigitCharIndex - 1 == start)
                    {
                        hashtag = str.toString().substring(startIndexOfNextHashSign, nextNotLetterDigitCharIndex);
                        Log.d("hashtag fetch", hashtag + "");
                        break;
                    }
                }
                index = nextNotLetterDigitCharIndex;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return hashtag;
    }

    public static int findNextValidHashTagChar(CharSequence text, int start)
    {

        int nonLetterDigitCharIndex = -1; // skip first sign '#"
        for (int index = start + 1; index < text.length(); index++)
        {

            char sign = text.charAt(index);

            boolean isValidSign = Character.isLetterOrDigit(sign);
            if (!isValidSign)
            {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1)
        {
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        }

        return nonLetterDigitCharIndex;
    }

    public static String getSortName(String text)
    {
        String sortname = "";
        try
        {
            String[] splited = text.split("\\s+");

            if (splited.length > 0)
            {
                if (splited.length == 1)
                {
                    char f = splited[0].charAt(0);
                    sortname = String.valueOf(f);
                }
                else
                {
                    char f = splited[0].charAt(0);
                    char l = splited[1].charAt(0);
                    sortname = String.valueOf(f) + String.valueOf(l);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sortname;
    }

    public static void configureBottomSheetBehavior(View contentView)
    {
        final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (mBottomSheetBehavior != null)
        {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
            {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState)
                {
                    //showing the different states
                    switch (newState)
                    {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });

                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            bottomSheet.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset)
                {

                }
            });
        }
    }

    public static String applyColor(String itemStr)
    {
        String finalString = "";

        try
        {
            String[] splited = itemStr.split("\\s+");

            for (int i = 0; i < splited.length; i++)
            {

                String str = splited[i].toString() + " ";

                if (str.startsWith("@"))
                {
                    finalString = finalString + getColoredSpanned(str, "#771D75");
                }
                else
                {
                    finalString = finalString + getColoredSpanned(str, "#000000");

                }

            }
        }
        catch (Exception e)
        {
            finalString = "<font color=" + "#000000" + ">" + itemStr + "</font>";
            e.printStackTrace();
        }

        return finalString;
    }

    public static String getColoredSpanned(String text, String color)
    {
        String input = "";
        try
        {
            input = "<font color=" + color + ">" + text + "</font>";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }

    public static void openDrawerLeft(DrawerLayout drawerLayout)
    {
        try
        {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
            else
            {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getRelativeDateTimeForChat(final String datetimeFromList)
    {
        String relativeDateTime = "";

        try
        {

            SimpleDateFormat df = new SimpleDateFormat("h:mm a");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm a");

            Date dateget = sdf.parse(datetimeFromList);

            long date = dateget.getTime();

            SimpleDateFormat dateCheck = new SimpleDateFormat("MM-dd-yyyy");

            Calendar calendar = Calendar.getInstance();

            String currentYear = String.valueOf(calendar.get(Calendar.YEAR));

            long today = calendar.getTimeInMillis();

            calendar.add(Calendar.DAY_OF_MONTH, -1);

            long yesterday = calendar.getTimeInMillis();

            if (dateCheck.format(date).equals(dateCheck.format(today)))
            {
                relativeDateTime = df.format(date);
            }
            else if (dateCheck.format(date).equals(dateCheck.format(yesterday)))
            {
                relativeDateTime = "Yesterday";
            }
            else
            {
                SimpleDateFormat newdateformat = new SimpleDateFormat("dd MMM, yyyy");

                relativeDateTime = dateCheck.format(date);

                relativeDateTime = newdateformat.format(dateCheck.parse(relativeDateTime));

                try
                {
                    if (relativeDateTime.contains(currentYear))
                    {
                        String year = ", " + currentYear;

                        relativeDateTime = relativeDateTime.replace(year, "");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            relativeDateTime = datetimeFromList;
            e.printStackTrace();
        }

        return relativeDateTime;
    }

    public static Bitmap rotateBitmap(String src)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(src);
        try
        {
            ExifInterface exif = new ExifInterface(src);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                case ExifInterface.ORIENTATION_UNDEFINED:
                default:
                    return bitmap;
            }

            try
            {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            }
            catch (OutOfMemoryError e)
            {
                e.printStackTrace();
                return bitmap;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static void showToast(Activity activity, String msg)
    {
        try
        {
            if (activity != null)
            {
                LayoutInflater inflater = activity.getLayoutInflater();
                View layout = inflater.inflate(R.layout.capital_custom_toast, null);

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(msg);

                Toast toast = new Toast(activity);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);

                if (toast.getView().isShown())
                {
                    toast.cancel();
                }
                else
                {
                    toast.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void noInternetSnackBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                @SuppressLint("WrongConstant") Snackbar snack = Snackbar.make(view, activity.getString(R.string.network_failed_message), Toast.LENGTH_SHORT);
                CapitalSnackbarHelper.configSnackbar(activity, snack);
                snack.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                Snackbar snackbar = Snackbar.make(view, activity.getString(R.string.network_failed_message), Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTypeface(AppUtils.getTypefaceRegular(activity));
                sbView.setBackgroundColor(color);
                snackbar.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void apiFailedToast(Activity activity)
    {
        Toast.makeText(activity, activity.getString(R.string.api_failed), Toast.LENGTH_LONG).show();
    }

    public static void apiFailedSnackBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                Snackbar snack = Snackbar.make(view, activity.getString(R.string.api_failed), Snackbar.LENGTH_SHORT);
                CapitalSnackbarHelper.configSnackbar(activity, snack);
                snack.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                Snackbar snackbar = Snackbar.make(view, activity.getString(R.string.api_failed), Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTypeface(AppUtils.getTypefaceRegular(activity));
                sbView.setBackgroundColor(color);
                snackbar.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public static String getMonthName(int month)
    {
        String name = "";

        if (month == 1)
        {
            name = "January";
        }
        else if (month == 2)
        {
            name = "February";
        }
        else if (month == 3)
        {
            name = "March";
        }
        else if (month == 4)
        {
            name = "April";
        }
        else if (month == 5)
        {
            name = "May";
        }
        else if (month == 6)
        {
            name = "June";
        }
        else if (month == 7)
        {
            name = "July";
        }
        else if (month == 8)
        {
            name = "August";
        }
        else if (month == 9)
        {
            name = "September";
        }
        else if (month == 10)
        {
            name = "October";
        }
        else if (month == 11)
        {
            name = "November";
        }
        else if (month == 12)
        {
            name = "December";
        }

        return name;

    }

}



