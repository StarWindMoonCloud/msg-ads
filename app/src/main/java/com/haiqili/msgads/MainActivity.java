package com.haiqili.msgads;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.amplitude.api.Amplitude;
import com.google.common.util.concurrent.RateLimiter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    private RateLimiter rateLimiter;
    private EditText phoneNumber;
    private EditText smsBody;
    private Button smsManagerBtn;
    private NumberGenerator numberGenerator;
    private PendingIntent sentPI;
    private PendingIntent deliveredPI;
    private BroadcastReceiver sentBroadcastReceiver;
    private BroadcastReceiver receiveBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        smsBody = (EditText) findViewById(R.id.smsBody);
        smsManagerBtn = (Button) findViewById(R.id.smsManager);

        numberGenerator = new NumberGenerator();
        Amplitude.getInstance().initialize(this, "98b0a23cf36cfbd882d8d00391b799b5").enableForegroundTracking(getApplication());
        sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(Constant.Event.SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(Constant.Event.DELIVERED), 0);
        rateLimiter = RateLimiter.create(Constant.Rate.SMS_QPS_LIMIT);

        smsManagerBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List<String> numbers = numberGenerator.generateNumber(phoneNumber.getText().toString());
                for (String number : numbers) {
                    sendSMS(number, smsBody.getText().toString());
                    rateLimiter.acquire();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        registerStatusIntent();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterStatusIntent();
        super.onPause();
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    private void sendToAmplitude(String type) {
       Amplitude.getInstance().logEvent(type);
    }
    private void sendToAmplitude(String type, String key, String value) {
        try {
            JSONObject eventProperties = new JSONObject();
            eventProperties.put(key, value);
            Amplitude.getInstance().logEvent(type, eventProperties);
        } catch (JSONException e) {
            Amplitude.getInstance().logEvent(Constant.Event.ERROR);
        }
    }
    private void registerStatusIntent() {
        //---when the SMS has been sent---
        sentBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String statusCode = StatusUtil.code2String(getResultCode());
                if (Activity.RESULT_OK == getResultCode()) {
                    sendToAmplitude(Constant.Event.SENT_SUCCESS);
                }
                sendToAmplitude(Constant.Event.SENT, Constant.Event.STATUS, statusCode);
            }};
        registerReceiver(sentBroadcastReceiver, new IntentFilter(Constant.Event.SENT));

        //---when the SMS has been delivered---
        receiveBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String statusCode = StatusUtil.code2String(getResultCode());
                if (Activity.RESULT_OK == getResultCode()) {
                    sendToAmplitude(Constant.Event.DELIVERED_SUCCESS);
                }
                sendToAmplitude(Constant.Event.DELIVERED, Constant.Event.STATUS, statusCode);
            }
        };
        registerReceiver(receiveBroadcastReceiver, new IntentFilter(Constant.Event.DELIVERED));
    }

    private void unregisterStatusIntent() {
        unregisterReceiver(sentBroadcastReceiver);
        unregisterReceiver(receiveBroadcastReceiver);
    }
}

