package com.obsuen.mycroppy.helpers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.obsuen.mycroppy.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Functions {
    public static Dialog dialog;

    public static void showLoader(Context context, boolean outside_touch, boolean cancleable) {
        try {

            if (dialog != null)
            {
                cancelLoader();
                dialog=null;
            }
            {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_dialog_loading_view);
                dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_white_background));


                CamomileSpinner loader = dialog.findViewById(R.id.loader);
                loader.start();


                if (!outside_touch)
                    dialog.setCanceledOnTouchOutside(false);

                if (!cancleable)
                    dialog.setCancelable(false);

                dialog.show();
            }

        }
        catch (Exception e)
        {
            Log.d("Constants.tag","Exception : "+e);
        }
    }
    public static boolean emptyOrNull(String str) {
        return str == null || str.length() == 0;
    }/*  ww  w  .  jav  a 2 s .co m*/

    public static boolean emptyOrNull(String... arrStr) {
        for (String str : arrStr) {
            if (emptyOrNull(str)) {
                return true;
            }
        }
        return false;
    }
    public static String decimalToPercent(String decimal) {
        float value = -1;
        if (!emptyOrNull(decimal)) {
            try {
                value = Float.parseFloat(decimal);
            } catch (NumberFormatException e) {
            }
        }
        if (value == -1) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("0.#%");
            return format.format(value);
        }
    }


    public static void cancelLoader() {
        try {
            if (dialog != null || dialog.isShowing()) {
                dialog.cancel();
            }
        }catch (Exception e){
            Log.d("Constants.tag","Exception : "+e);
        }
    }
    public static String getDate(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy ");
        return sdf.format(d);
    }

}
