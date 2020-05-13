package com.example.textmessenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Objects;

public class SmsBroadCastReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Objects[]) intentExtras.get(SMS_BUNDLE);
            String messageStr = "";

            for (int i = 0; i < sms.length; ++i) {
                String format = intentExtras.getString("format");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);

                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();

                    messageStr += "SMS FROM: " + address + "\n";
                    messageStr += smsBody + "\n";
                }
            }

            MainActivity inst = MainActivity.instance();
            inst.updateInbox(messageStr);
        }
    }
}
