package com.haiqili.msgads;

/**
 * Created by qili on 5/22/16.
 */
public class Constant {
    public static class Event {
        public static final String SENT = "SENT";
        public static final String SENT_SUCCESS = "SENT_SUCCESS";
        public static final String SENT_FAILURE = "SENT_FAILURE";
        public static final String DELIVERED_SUCCESS = "DELIVERED_SUCCESS";
        public static final String DELIVERED_FAILURE = "DELIVERED_FAILURED";
        public static final String DELIVERED = "DELIVERED";
        public static final String STATUS = "STATUS";
        public static final String ERROR = "ERROR";
    }
    public static class Rate {
        public static final int SMS_QPS_LIMIT = 5;
    }
}
