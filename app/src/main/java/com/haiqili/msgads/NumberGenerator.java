package com.haiqili.msgads;

import android.util.Log;

import com.amplitude.api.Amplitude;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qili on 5/21/16.
 * V1 number generator:
 * TODO replace it with producer and consumer model when receivers are large
 */
public class NumberGenerator {
    private static final String NUMBER_START_CHINA = "+86";
    private static final int NUMBER_LENGTH_CHINA = 11;
    /**
     * 1. Verify input number
     * 2. Generator complete numbers
     * @param text
     * @return
     */
    List<String> generateNumber(String text) {
        List<String> numbers = new ArrayList<>();
        if (text != null && text.startsWith(NUMBER_START_CHINA)) {
            String numberText = text.substring(3);
            int digitsMissing = NUMBER_LENGTH_CHINA - numberText.length();
            if (digitsMissing > 0) {
                for (int i = 0; i < Math.pow(10, digitsMissing); i++) {
                    String number = padNumber(i, digitsMissing);
                    String fullNumber = NUMBER_START_CHINA + numberText + number;
                    numbers.add(fullNumber);
                }
            } else {
                numbers.add(text);
            }
        }
        return numbers;
    }

    /**
     * Pad leading 0 to the number and make it length of len String
     * @param num
     * @param len
     * @return
     */
    String padNumber(int num, int len) {
        String retString = String.valueOf(num);
        if (retString.length() < len) {
            StringUtils.leftPad("0", (len - retString.length()));
        }
        return retString;
    }

    public static void main(String[] args) {
        NumberGenerator numberGenerator = new NumberGenerator();
        List<String> numbers = numberGenerator.generateNumber("+861381076874");
        System.out.println(numbers);
    }
}
