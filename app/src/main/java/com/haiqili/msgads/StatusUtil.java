package com.haiqili.msgads;

import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by qili on 5/22/16.
 */
public class StatusUtil {
    /**
     * Response code detail explanation
     * http://stackoverflow.com/questions/12987909/whats-meant-by-result-errors-of-smsmanager
     * @param code
     * @return
     */
    public static String code2String(int code) {
        String ret = null;
        switch (code)
        {
            case Activity.RESULT_OK:
                ret = "OK";
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                ret = "GENERIC_FAILURE";
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                ret = "NO_SERVICE";
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                ret = "NULL_PDU";
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                ret = "RADIO_OFF";
                break;
            case Activity.RESULT_CANCELED:
                ret = "CANCELED";
                break;
        }
        return ret;
    }
}
