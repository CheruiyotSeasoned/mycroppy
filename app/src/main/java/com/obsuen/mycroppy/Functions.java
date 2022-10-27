package com.obsuen.mycroppy;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.gmail.samehadar.iosdialog.CamomileSpinner;

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

    public static void cancelLoader() {
        try {
            if (dialog != null || dialog.isShowing()) {
                dialog.cancel();
            }
        }catch (Exception e){
            Log.d("Constants.tag","Exception : "+e);
        }
    }
}
