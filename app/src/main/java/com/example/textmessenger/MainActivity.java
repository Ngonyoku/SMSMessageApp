package com.example.textmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    ArrayList<String> messageList = new ArrayList<>();
    SmsManager smsManager = SmsManager.getDefault();
    ListView messageView;
    EditText messageInput;
    private static final int READ_SMS_PERMISSION = 1;

    public static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = findViewById(R.id.message_input);
        messageView = findViewById(R.id.message_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        messageView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionSendSMS();
        } else {
            displayMessage();
        }
    }

    public void updateInbox(String smsMessage) {
        adapter.insert(smsMessage, 0);
        adapter.notifyDataSetChanged();
    }

    void displayMessage() {
        ContentResolver resolver = getContentResolver();
        Cursor smsCursor = resolver.query(Uri.parse("content://sms/inbox"),
                null, null, null, null);

        int smsBody = smsCursor.getColumnIndex("body");
        int sender = smsCursor.getColumnIndex("address");

        if (smsBody < 0 || !smsCursor.moveToFirst()) return;
        adapter.clear();

        do {
            String smsContent = "FROM: "
                    + smsCursor.getString(sender)
                    + "\n"
                    + smsCursor.getString(smsBody)
                    + "\n";

            adapter.add(smsContent);
        } while (smsCursor.moveToNext());
    }

    void sendMessage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissionSendSMS();
        } else {
            smsManager.sendTextMessage(
                    "0707670113",
                    null, messageInput.getText().toString(),
                    null, null);

            Toast.makeText(this, "Messages Sent Successfully!!", Toast.LENGTH_SHORT).show();
        }
    }

    void requestPermissionSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        }
        //If the Device Build Version is > or = Android.Marshmellow (API 23)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, READ_SMS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "We can Now Read your Messages!", Toast.LENGTH_SHORT).show();
                displayMessage();
            } else {
                Toast.makeText(this, "We don't have Permission To read your Messages!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
