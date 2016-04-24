package cn.edu.nju.software.tongbaoshipper.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class ReceiverService extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.d(this.getClass().getName(), String.format("onReceive: %s, extras: %s", intent.getAction(), bundle.toString()));

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(this.getClass().getName(), "message received");
            receiveMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(this.getClass().getName(), "notification received");
            receiveNotification(bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(this.getClass().getName(), "notification opened");
            openNotification(bundle);
        } else {
            Log.d(this.getClass().getName(), "Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * received message
     * @param bundle    Bundle
     */
    private void receiveMessage(Context context, Bundle bundle) {
        Toast.makeText(context, String.format("message: %s", bundle.getString(JPushInterface.EXTRA_MESSAGE)),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * receive notification
     * @param bundle    Bundle
     */
    private void receiveNotification(Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(this.getClass().getName(), " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(this.getClass().getName(), "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(this.getClass().getName(), "extras : " + extras);
    }

    /**
     * open notification
     * @param bundle    Bundle
     */
    private void openNotification(Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            Log.w(this.getClass().getName(), "Unexpected: extras is not a valid json", e);
            return;
        }
//        if (TYPE_THIS.equals(myValue)) {
//            Intent mIntent = new Intent(context, ThisActivity.class);
//            mIntent.putExtras(bundle);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mIntent);
//        } else if (TYPE_ANOTHER.equals(myValue)) {
//            Intent mIntent = new Intent(context, AnotherActivity.class);
//            mIntent.putExtras(bundle);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mIntent);
//        }
    }

}
