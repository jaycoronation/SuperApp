package com.application.alphacapital.superapp.pms.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.android.mit.mitsutils.MitsUtils;
import com.application.alphacapital.superapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")
public class AppUtils
{

    public static String regularFonts = "proxima-nova-soft-regular.otf";
    public static String mediumFonts = "proxima-nova-soft-medium.otf";
    public static String semiBoldFonts = "proxima-nova-soft-semibold.otf";
    public static String boldFonts = "proxima-nova-soft-bold.otf";

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

    public static void removeError(EditText edt, final TextInputLayout layout)
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
            e.printStackTrace();
        }

        return flag;
    }

    public static boolean getValidAPIBooleanResponseFromInteger(String value)
    {
        boolean flag = false;
        try
        {
            value = getValidAPIStringResponse(value);

            if (value.equalsIgnoreCase("1"))
            {
                flag = true;
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


    public static void setTabsForBoldRegular(TabLayout tabs, final ViewPager viewPager, final Activity activity)
    {
        for (int i = 0; i < tabs.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (tab != null)
            {
                TextView tabTextView = new TextView(activity);
                tabTextView.setTextColor(activity.getResources().getColor(R.color.portfolio_white));
                tabTextView.setTextSize(14);
                tabTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.regularFonts));
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.setGravity(Gravity.CENTER);
                tabTextView.setText(tab.getText());

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0)
                {
                    tabTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.boldFonts));
                }
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());

                TextView text = (TextView) tab.getCustomView();
                //text.setTypeface(null, Typeface.BOLD);
                text.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.boldFonts));

                text.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                text.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                text.setGravity(Gravity.CENTER);
                text.setTextSize(14);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                TextView text = (TextView) tab.getCustomView();

                //text.setTypeface(null, Typeface.NORMAL);
                text.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.regularFonts));

                text.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                text.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                text.setGravity(Gravity.CENTER);
                text.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }

        });
    }

    public static float getValidAPIFloatResponse(String value)
    {
        float val = 0.0f;
        value = getValidAPIStringResponse(value);

        try
        {
            val = Float.parseFloat(value);
        }
        catch (NumberFormatException e)
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
            e1.printStackTrace();
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
            e.printStackTrace();
        }
        return l;
    }

    public static String getValidAPIJsonStringResponse(JSONObject jsonObject, String key)
    {
        String value = "";
        try
        {
            if (jsonObject.has(key))
            {
                value = getValidAPIStringResponse(jsonObject.getString(key));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("<null>"))
        {
            value = "";
        }
        return value.trim();
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

    public static SpannableString createLink(String completeString, String partToClick, ClickableSpan clickableAction)
    {
        SpannableString spannableString = new SpannableString(completeString);

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        int startPosition = completeString.indexOf(partToClick);
        int endPosition = completeString.lastIndexOf(partToClick) + partToClick.length();

        spannableString.setSpan(clickableAction, startPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return spannableString;
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
            @SuppressWarnings("deprecation")
            Date gmtTime = new Date(sdf.format(localTime));
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date date = new Date(timestamp);
                datetime = sdf.format(date).replace(".", "");

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return datetime;
    }

    public static String getDate(long time)
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy", cal).toString();
        return date;
    }

    public static long convertStringToTimestamp(String str_date, String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try
        {
            date = formatter.parse(str_date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        System.out.println("Today is " + date.getTime());
        return date.getTime();
    }

    public static boolean validateTwoDates(String fromDate,String toDate,String pattern)
    {
        boolean isValid = true ;

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date1 = null;
        Date date2 = null;
        try
        {
            date1 = formatter.parse(fromDate);
            date2 = formatter.parse(toDate);

            if(date1.after(date2))
            {
                isValid = false ;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return true ;
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
                Pattern.compile("[!@#\\$%^&*()~`\\-=_+\\[\\]{}|:\\\";',\\./<>?]"),
                // small character
                Pattern.compile("[a-z]"),
                // capital character
                Pattern.compile("[A-Z]"),
                // numeric character
                Pattern.compile("\\d"),
                // 6 to 40 character length
                Pattern.compile("^.{8,40}$")};
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
     * @param editText edittext
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
            s = getValidAPIStringResponse(s);
            if (s.length() == 0)
            {
                return "";
            }
            StringBuffer res = new StringBuffer();
            String[] strArr = s.split(" ");
            for (String str : strArr)
            {
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);

                res.append(str).append(" ");
            }
            strToReturn = res.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            final String ACTIONABLE_DELIMITERS = " -/"; // these cause the character following to be capitalized
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
        return strToReturn;
    }

    public static CharSequence removeLineSpace(CharSequence s, int start, int end)
    {
        while (start < end && Character.isWhitespace(s.charAt(start)))
        {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1)))
        {
            end--;
        }

        return s.subSequence(start, end);
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

    public static Typeface getEdittextTypeface(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), regularFonts);
    }

    public static int[] getDeviceWidthHeight(final Activity activity)
    {
        int[] arr = {0, 0};
        try
        {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            arr[0] = width;
            arr[1] = height;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            arr[0] = 0;
            arr[1] = 0;
        }
        return arr;
    }

    public static String getTotalMemoryUsedByApp()
    {
        double roundOff = 0.0;

        try
        {
            long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double memoryMb = (double) memory / (1000000);
            roundOff = (double) Math.round(memoryMb * 100.0) / 100.0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return String.valueOf(roundOff);
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

    public static void showSnackBar(View v, String message, Activity activity)
    {
        Snackbar bar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        View view = bar.getView();
        view.setBackground(ContextCompat.getDrawable(activity, R.drawable.portfolio_snackbar_drawable));
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        textView.setTypeface(Typeface.createFromAsset(activity.getAssets(), AppUtils.regularFonts));
        textView.setTextColor(ContextCompat.getColor(activity, R.color.portfolio_white));
        bar.show();
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
        //activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void finishActivityAnimation(Activity activity)
    {
        MitsUtils.hideKeyboard(activity);
        //activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void printLog(String title, String message)
    {
        Log.e(title,message);
    }

    public static String getLastBitFromUrl(final String url)
    {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    public static HashMap<String, String> getEmailListFromDB(final Context context)
    {
        HashMap<String, String> hashmapEmail = new HashMap<>();

        try
        {
            final String[] PROJECTIONEMAIL = new String[]{ContactsContract.CommonDataKinds.Email.CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Email.DATA};

            ContentResolver cr = context.getContentResolver();
            Cursor cursorEmail = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTIONEMAIL, null, null, null);
            if (cursorEmail != null)
            {
                try
                {
                    final int contactIdIndex = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                    final int emailIndex = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                    long contactId;
                    String address;
                    while (cursorEmail.moveToNext())
                    {
                        contactId = cursorEmail.getLong(contactIdIndex);
                        address = cursorEmail.getString(emailIndex);
                        hashmapEmail.put(String.valueOf(contactId), address);
                        /*Log.v("CONTACTS DETAIL", contactId + " ** " + displayName + " ** " + address);*/
                    }
                }
                finally
                {
                    cursorEmail.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return hashmapEmail;
    }

    public static String getValidDateString(String birthdate)
    {
        try
        {
            if (birthdate.contains("-"))
            {
                if (birthdate.startsWith("--"))
                {
                    birthdate = "1991" + birthdate.substring(1, birthdate.length());
                }

                String[] arrdate = birthdate.split("-");
                String yearStr = arrdate[0];
                String monthStr = arrdate[1];
                String dateStr = arrdate[2];

                birthdate = dateStr + "/" + monthStr + "/" + yearStr;
            }
            else
            {
                birthdate = "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return birthdate;
    }

    public static HashMap<String, String> getBirthdateListFromDB(final Context context)
    {
        HashMap<String, String> hashmapBirthdate = new HashMap<>();
        try
        {
            final String[] selectionArgs = new String[]{ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};

            ContentResolver cr = context.getContentResolver();
            String whereBirthdate = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
            Cursor cursorBirthdate = cr.query(ContactsContract.Data.CONTENT_URI, null, whereBirthdate, selectionArgs, null);
            if (cursorBirthdate != null)
            {
                try
                {
                    final int contactIdIndex = cursorBirthdate.getColumnIndex(ContactsContract.CommonDataKinds.Event.CONTACT_ID);
                    while (cursorBirthdate.moveToNext())
                    {

                        int bDayColumn = cursorBirthdate.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                        String birthdate = AppUtils.getValidAPIStringResponse(cursorBirthdate.getString(bDayColumn));
                        birthdate = AppUtils.getValidDateString(birthdate);

                        long contactId = cursorBirthdate.getLong(contactIdIndex);
                        hashmapBirthdate.put(String.valueOf(contactId), birthdate);
                        /*Log.v("CONTACTS DETAIL", contactId + " ** " + birthdate);*/
                    }
                }
                finally
                {
                    cursorBirthdate.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return hashmapBirthdate;
    }

    public static long getDateAsUpcoming(String birthdate)
    {
        long millis = 0;

        try
        {
            Date dateObjNow = new Date(Calendar.getInstance().getTimeInMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String[] strarrNow = dateFormat.format(dateObjNow).split("/");
            int dateNow = Integer.parseInt(strarrNow[0]);
            int monthNow = Integer.parseInt(strarrNow[1]);
            int yearNow = Integer.parseInt(strarrNow[2]);

            String[] strarr = birthdate.split("/");
            int date = Integer.parseInt(strarr[0]);
            int month = Integer.parseInt(strarr[1]);
            int year = yearNow;

            if (monthNow == month)
            {
                if (dateNow > date)
                {
                    year = yearNow + 1;
                }
            }
            else if (monthNow > month)
            {
                year = yearNow + 1;
            }

            String newdatestr = String.valueOf(date) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFinal = simpleDateFormat.parse(newdatestr);
            millis = dateFinal.getTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return millis;
    }

    public static void expand(final View v, ImageView img)
    {
        img.setImageResource(R.drawable.portfolio_ic_down_arrow_blue);
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                v.getLayoutParams().height = interpolatedTime == 1 ? WindowManager.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expandDark(final View v, ImageView img)
    {
        img.setImageResource(R.drawable.portfolio_ic_down_arrow_white);
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                v.getLayoutParams().height = interpolatedTime == 1 ? WindowManager.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v, ImageView img)
    {
        img.setImageResource(R.drawable.portfolio_ic_right_arrow_blue);
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                if (interpolatedTime == 1)
                {
                    v.setVisibility(View.GONE);
                }
                else
                {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapseDark(final View v, ImageView img)
    {
        img.setImageResource(R.drawable.portfolio_ic_right_arrow_white);
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                if (interpolatedTime == 1)
                {
                    v.setVisibility(View.GONE);
                }
                else
                {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v, int duration, int targetHeight)
    {
        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight)
    {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void shareScreen(Activity activity)
    {
        try
        {
            File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/");

            if (!dirtostoreFile.exists())
            {
                dirtostoreFile.mkdirs();
            }

            String timestamp = new SimpleDateFormat("ddMMMyyyy-HHmmss").format(new Date());

            String name = "screenshot" + timestamp + ".jpg";

            File file = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/" + name);

            ScreenShotUtils.savePic(ScreenShotUtils.takeScreenShotOfActivity(activity), file.toString());

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Uri contentUri = FileProvider.getUriForFile(activity, "com.vadilalmarket.provider", file);
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList)
                {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            }
            else
            {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }

            shareIntent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(shareIntent, "Share Via VadilalMarket"));

        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
    }

    public static void shareChartScreen(Activity activity,View v,int width,int height)
    {
        try
        {
            File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/");

            if (!dirtostoreFile.exists())
            {
                dirtostoreFile.mkdirs();
            }

            String timestamp = new SimpleDateFormat("ddMMMyyyy-HHmmss").format(new Date());

            String name = "screenshot" + timestamp + ".jpg";

            File file = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/" + name);

            Bitmap bitmap = loadBitmapFromView(v,width,height);

            ScreenShotUtils.savePic(bitmap, file.toString());

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Uri contentUri = FileProvider.getUriForFile(activity, "com.vadilalmarket.provider", file);
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList)
                {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            }
            else
            {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }

            shareIntent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(shareIntent, "Share Via VadilalMarket"));
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height)
    {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static void shareNewsScreen(Activity activity, String newsUrl)
    {
        try
        {
            File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/");

            if (!dirtostoreFile.exists())
            {
                dirtostoreFile.mkdirs();
            }

            String timestamp = new SimpleDateFormat("ddMMMyyyy-HHmmss").format(new Date());

            String name = "screenshot" + timestamp + ".jpg";

            File file = new File(Environment.getExternalStorageDirectory() + "/VadilalMarket/ShareData/" + name);

            ScreenShotUtils.savePic(ScreenShotUtils.takeScreenShotOfActivity(activity), file.toString());

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
            shareIntent.setType("text/plain");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Uri contentUri = FileProvider.getUriForFile(activity, "com.vadilalmarket.provider", file);
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList)
                {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            }
            else
            {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }

            shareIntent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(shareIntent, "Share Via VadilalMarket"));

        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
    }

    public static String getRandomCartToken()
    {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++)
        {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    public static Bitmap getFontBitmap(Context context, String text)
    {
		/*int fontSizePX = convertDipToPix(context, fontSizeSP);
		int pad = (fontSizePX / 9);
		Paint paint = new Paint();
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), AppUtils.boldFonts);
		paint.setAntiAlias(true);
		paint.setTypeface(typeface);
		paint.setColor(color);
		paint.setTextSize(fontSizePX);

		int textWidth = (int) (paint.measureText(text) + pad * 2);
		int height = (int) (fontSizePX / 0.75);
		Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		float xOriginal = pad;
		canvas.drawText(text, xOriginal, fontSizePX, paint);
		return bitmap;*/

        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(), AppUtils.boldFonts);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(text, 80, 60, paint);
        return myBitmap;
    }

    public static int convertDipToPix(Context context, float dip)
    {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return value;
    }

    public static String convertToCommaSeperatedValue(String value)
    {
        String str = "";
        try
        {
            DecimalFormat formatter = new DecimalFormat("#,##,###");
            str = "";
            str = formatter.format(Double.parseDouble(value));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertToOneDecimalValue(String value)
    {
        String str = "";
        try
        {
            DecimalFormat formatter = new DecimalFormat("#,##,##0.0");
            str = "";
            str = formatter.format(Double.parseDouble(value));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertToTwoDecimalValue(String value)
    {
        String str = "";
        try
        {
            DecimalFormat formatter = new DecimalFormat("#,##,###");
            str = "";

            if(value.trim().length() > 0 && !value.equals("NA"))
            {
                str = formatter.format(Double.parseDouble(value));
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertToFourDecimalValue(String value)
    {
        String str = "";
        try
        {
            DecimalFormat formatter = new DecimalFormat("#,##,##0.0000");
            str = "";
            str = formatter.format(Double.parseDouble(value));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static Typeface getTypefaceRegular(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), regularFonts);
    }

    public static Typeface getTypefaceMedium(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), mediumFonts);
    }

    public static Typeface getTypefaceSemiBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), semiBoldFonts);
    }

    public static Typeface getTypefaceBold(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), boldFonts);
    }

    public static Typeface getTypefaceButton(final Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), boldFonts);
    }

    public static ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < stringArray.size(); i++)
        {
            try
            {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringArray.get(i)));
            }
            catch (NumberFormatException nfe)
            {
                //System.out.println("Could not parse " + nfe);
                Log.w("NumberFormat", "Parsing failed! " + stringArray.get(i) + " can not be an integer");
            }
        }

        return result;
    }

    public static void loadCircleAnimation(Activity activity, View orangeCircle, View skyblueCircle)
    {

        ObjectAnimator orangeAnimator = ObjectAnimator.ofFloat(orangeCircle, "rotationY", 0.0f, 360f);
        orangeAnimator.setDuration(3000);
        orangeAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        orangeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        orangeAnimator.start();

        ObjectAnimator skyblueAnimator = ObjectAnimator.ofFloat(skyblueCircle, "rotationY", 360f, 0.0f);
        skyblueAnimator.setDuration(3000);
        skyblueAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        skyblueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        skyblueAnimator.start();
    }

    public static void toolbarCircleAnimation(View orangeCircle, View skyblueCircle)
    {
        /*Set Animation via TranslateAnimation moving two circle around.*/

        TranslateAnimation orangeCircleAnimation = new TranslateAnimation(0.0f, 50.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
        orangeCircleAnimation.setDuration(800); // orangeCircleAnimation duration
        orangeCircleAnimation.setRepeatCount(Animation.INFINITE); // orangeCircleAnimation repeat count
        orangeCircleAnimation.setRepeatMode(2); // repeat orangeCircleAnimation (left to right, right to left)
        orangeCircleAnimation.setFillAfter(true);
        orangeCircle.startAnimation(orangeCircleAnimation);//your_view for mine is imageView

        TranslateAnimation skyblueCircleAnimation = new TranslateAnimation(0.0f, -50.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
        skyblueCircleAnimation.setDuration(800); // skyblueCircleAnimation duration
        skyblueCircleAnimation.setRepeatCount(Animation.INFINITE); // skyblueCircleAnimation repeat count
        skyblueCircleAnimation.setRepeatMode(2); // repeat skyblueCircleAnimation (left to right, right to left)
        skyblueCircleAnimation.setFillAfter(true);
        skyblueCircle.startAnimation(skyblueCircleAnimation);//your_view for mine is imageView
    }

    private String convertNumberToUpDown(double input, boolean isUp, boolean isDefault)
    {
        String value = "";
        DecimalFormat df2 = new DecimalFormat(".##");

        if (isDefault)
        {
            value = df2.format(input);
        }
        else
        {
            if (isUp)
            {
                df2.setRoundingMode(RoundingMode.UP);
                value = df2.format(input);
            }
            else
            {
                df2.setRoundingMode(RoundingMode.DOWN);
                value = df2.format(input);
            }
        }
        System.out.println("double () : " + df2.format(input));

        return value;
    }

    //Check internet is in working state or not
    public static boolean isOnline()
    {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public static String toFirstCapitalString(String s)
    {
        String returnString = "";
        try
        {
            returnString = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
            return returnString;
        }
        catch (Exception e)
        {
            return s;
        }
    }

    public static void shareTextViaAllApps(final String text, final String link, final Activity activity)
    {
        try
        {
            String appname = activity.getResources().getString(R.string.app_name);
            String shareText = text + "\n" + link + "\n-- shared via " + appname;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, appname);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText.trim());
            activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isWhole(double d)
    {
        return (d % 1) == 0;
    }

    public static boolean checkNumberLessThanOrGreaterThan(double newValue, double oldValue)
    {
        boolean isGreater = false;

        if (newValue < oldValue)
        {
            isGreater = false;
        }
        else
        {
            isGreater = true;
        }

        return isGreater;
    }

    public static String convertDecimalValue(String value)
    {
        String str = "";
        try
        {
            DecimalFormat formatter = new DecimalFormat("#,##,##0");
            str = "";
            str = formatter.format(Double.parseDouble(value));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static void showVersionMismatchDialog(final Activity activity)
    {
        try
        {
            String titleText = "Upgrade";
            String messageText = "A new version of VadilalMarket is ready for installation. Please upgrade to continue.";

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(titleText);
            alertDialogBuilder.setMessage(messageText);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Upgrade",new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog,int id)
                {
                    dialog.dismiss();
                    dialog.cancel();

                    final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                    try
                    {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                    catch (android.content.ActivityNotFoundException anfe)
                    {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                    startActivityAnimation(activity);
                    activity.finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setBackgrounColor(Activity activity , View view , int color)
    {
        view.setBackgroundColor(ContextCompat.getColor(activity,color));
    }

    public static void setTextColor(Activity activity , TextView view , int color)
    {
        view.setTextColor(ContextCompat.getColor(activity,color));
    }
}
