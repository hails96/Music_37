package lsh.framgia.com.isoundcloud.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import lsh.framgia.com.isoundcloud.R;

public class DialogUtils {

    private static ProgressDialog sProgress;

    public static void showProgressDialog(final Activity activity) {
        dismissProgressDialog();

        if (sProgress != null && sProgress.isShowing()) {
            sProgress.dismiss();
        } else {
            sProgress = new ProgressDialog(activity);
        }

        sProgress.setMessage(activity.getString(R.string.label_loading));
        Drawable drawable = new ProgressBar(activity).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(
                activity, R.color.color_primary), PorterDuff.Mode.SRC_IN);
        sProgress.setIndeterminateDrawable(drawable);
        sProgress.show();
    }

    public static void dismissProgressDialog() {
        if (sProgress != null && sProgress.isShowing()) {
            sProgress.dismiss();
        }
    }
}
