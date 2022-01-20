package com.android.attendme.Helpers;

import static android.widget.Toast.makeText;
import static android.widget.Toast.LENGTH_LONG;
import android.content.Context;
import android.net.ConnectivityManager;

import java.sql.Connection;


public final class Helpers {
    //Method to show toast message
    public static void makeToast(Context context, String message){
        makeText(context, message, LENGTH_LONG).show();
    }

    //Method to validate the email
    public static Boolean emailValidator(String email){
        String emailPatter = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+(\\.+[a-z]+)?";
        return email.matches(emailPatter);
    }

    //Method to validate the password
    public static Boolean passwordValidator(String password){
        return password.length() >= 8;
    }

    public static String removeSpecialCharacters(String email){
        char[] emailArray = email.toCharArray();
        char[] resArray = new char[emailArray.length];
        int i = 0;
        for(char ch : emailArray){
            if(ch != '@' && ch != '.'){
                resArray[i++] = ch;
            }
        }
        return new String(resArray).trim();
    }

    public static Boolean isConnectedToInternet(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isAvailable() &&
                manager.getActiveNetworkInfo().isConnected();
    }
}
