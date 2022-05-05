package com.example.discovery.Util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.discovery.Activity.FavoritesActivity;
import com.example.discovery.Activity.MapsActivity;
import com.example.discovery.Activity.ParksListActivity;
import com.example.discovery.Activity.SettingActivity;
import com.example.discovery.Activity.VisitActivity;
import com.example.discovery.R;

public class Util {
    public static String KEY_FNAME = "firstName";
    public static String KEY_LNAME = "lastName";
    public static String KEY_PWD = "password";
    public static String KEY_USERID = "userId";
    public static String KEY_EMAIL = "email";

    public static String email = "info.discoveryproject@gmail.com";
    public static String email_pwd = "oomzwjujkdsonqee";



    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getParksUrl(String stateCode) {
        return "https://developer.nps.gov/api/v1/parks?stateCode="+stateCode+"&api_key=QjSsbGysbYPDM0KkeOaFgqwqAIgIoqhL2iG6kpSA";
    }

    public static void MenuClick(MenuItem item, Context context){
        int id = item.getItemId();
        if(id == R.id.maps_nav_btn){
            Intent intent = new Intent(context, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (id == R.id.parks_nav_btn){
            Intent intent = new Intent(context, ParksListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (id == R.id.fav_nav_btn){
            Intent intent = new Intent(context, FavoritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (id == R.id.setting_nav_btn){
            Intent intent = new Intent(context, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (id == R.id.visit_nav_btn){
            Intent intent = new Intent(context, VisitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


}
