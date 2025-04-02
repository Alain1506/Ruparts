package com.ruparts.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ruparts.AuthorizationActivity;
import com.ruparts.main.exception.api.ApiCallException;
import com.ruparts.main.exception.api.NotAuthorizedException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class CrashHandler implements Thread.UncaughtExceptionHandler{

    private String newLine = "\n";
    private StringBuilder errorMessage = new StringBuilder();
    private StringBuilder softwareInfo = new StringBuilder();
    private StringBuilder dateInfo = new StringBuilder();
    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable exception) {
        if (exception.getCause() instanceof NotAuthorizedException) {
            Intent intent = new Intent(context, AuthorizationActivity.class);
            context.startActivity(intent);
            Looper.loop();
            return;
        }
        if (exception instanceof ApiCallException) {
            errorMessage.append("API call exception!" + newLine);
            errorMessage.append("Message: " + exception.getMessage() + newLine);
            errorMessage.append("Details: " + ((ApiCallException) exception).details + newLine);
            errorMessage.append(newLine);
        }

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace( new PrintWriter(stackTrace));
        errorMessage.append("Stacktrace: ");
        errorMessage.append(stackTrace);
        errorMessage.append(newLine);

        softwareInfo.append("SDK: ");
        softwareInfo.append(Build.VERSION.SDK_INT);
        softwareInfo.append(newLine);
        softwareInfo.append("Release: ");
        softwareInfo.append(Build.VERSION.RELEASE);
        softwareInfo.append(newLine);
        softwareInfo.append("Incremental: ");
        softwareInfo.append(Build.VERSION.INCREMENTAL);
        softwareInfo.append(newLine);

        dateInfo.append(Calendar.getInstance());
        dateInfo.append(newLine);

        Log.d("Error" , errorMessage.toString());
        Log.d("Software" , softwareInfo.toString());
        Log.d("Date" , dateInfo.toString());

        Intent intent = new Intent(context , CrashActivity.class);
        intent.putExtra("Error" , errorMessage.toString());
        intent.putExtra("Software" , softwareInfo.toString());
        intent.putExtra("Date" , dateInfo.toString());

        context.startActivity(intent);
//        Looper.loop();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
