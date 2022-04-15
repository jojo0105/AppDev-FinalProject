package com.example.discovery.Util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {
    public static String KEY_FNAME = "firstName";
    public static String KEY_LNAME = "lastName";
    public static String KEY_PWD = "password";
    public static String KEY_USERID = "userId";
    public static String KEY_EMAIL = "email";



    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getParksUrl(String stateCode) {
        return "https://developer.nps.gov/api/v1/parks?stateCode="+stateCode+"&api_key=QjSsbGysbYPDM0KkeOaFgqwqAIgIoqhL2iG6kpSA";
    }


}
