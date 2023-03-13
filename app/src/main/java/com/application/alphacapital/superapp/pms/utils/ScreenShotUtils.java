package com.application.alphacapital.superapp.pms.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtils
{
    public static Bitmap takeScreenShotOfActivity(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Find the screen dimensions to create bitmap in the same size. 
        int width = size.x;
        int height = size.y;

        //To remove status bar height uncomment below code.
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        //Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height);
        view.destroyDrawingCache();
        return b;
    }

    public static void savePic(Bitmap b, String strFileName)
    {
        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveChartPic(Bitmap b, String strFileName)
    {
        FileOutputStream fos;
        if (b != null)
        {
            ByteArrayOutputStream mByteArrayOS = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 90, mByteArrayOS);
            try
            {
                fos = new FileOutputStream(strFileName);
                fos.write(mByteArrayOS.toByteArray());
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void shareView(Activity activity, View view, String path)
    {
        try
        {
            //Get the bitmap from the view
            if (view != null)
            {
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bm = view.getDrawingCache();
                //End
                //Pass the generated bitmap instead.
                ScreenShotUtils.savePic(bm, path);
            }
        }
        catch (NullPointerException ignored)
        {
            ignored.printStackTrace();
        }


    }
}