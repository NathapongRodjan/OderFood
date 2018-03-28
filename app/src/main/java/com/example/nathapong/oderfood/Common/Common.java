package com.example.nathapong.oderfood.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.nathapong.oderfood.Model.User;

public class Common {



    public static User currentUser;

    public static String DELETE = "ลบรายการ";


    // Check the Internet Connection
    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }





}
