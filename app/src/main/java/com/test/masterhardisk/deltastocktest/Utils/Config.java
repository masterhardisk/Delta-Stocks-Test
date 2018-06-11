package com.test.masterhardisk.deltastocktest.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MasterHardisk on 11/6/18.
 */

public class Config {

    private Context mContext;

    public Config(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public void setUserCity(String userCity){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(Constants.KEY_USER_CITY, userCity);
        editor.apply();
    }

    public String getUserCity(){
        return getSettings().getString(Constants.KEY_USER_CITY, null);
    }

    public void setUserCP(String cp){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(Constants.KEY_POSTAL_CODE, cp);
        editor.apply();
    }

    public String getUserCP(){
        return getSettings().getString(Constants.KEY_POSTAL_CODE, null);
    }

    public String getUserLatitude(){
        return getSettings().getString(Constants.KEY_LATITUDE, null);
    }

    public void setUserLatitude(String latitude){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(Constants.KEY_LATITUDE, latitude);
        editor.apply();
    }

    public String getUserLongitude(){
        return getSettings().getString(Constants.KEY_LONGITUDE, null);
    }

    public void setUserLongitude(String longitude){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(Constants.KEY_LONGITUDE, longitude);
        editor.apply();
    }
}
