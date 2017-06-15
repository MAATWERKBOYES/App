package docentengo.fontys.nl.docentengo;

import android.app.Activity;

import com.tapadoo.alerter.Alerter;

/**
 * Created by Jeroe on 15-6-2017.
 */

public class AlertHandler {

    public static void showErrorDialog(Activity activity, Throwable cause, String message) {
        showErrorDialog(activity, cause, null, message);
    }

    public static void showErrorDialog(Activity activity, Throwable cause,
                                       String title, String message) {
        cause.printStackTrace();

        showAlertDialog(activity, title, message);
    }

    public static void showAlertDialog(Activity activity, String message) {
        showAlertDialog(activity, null, message);
    }

    public static void showAlertDialog(Activity activity, String title, String message) {
        showMessage(activity, title, message);
    }

    private static void showMessage(Activity activity, String title, String message) {
        Alerter.create(activity)
                .setTitle(title)
                .setText(message)
                .setBackgroundColor(R.color.alert_fontys_purple)
                .show();
    }

}
