package com.fr.marcoucou.placereminder;

import android.app.Application;

import com.fr.marcoucou.placereminder.utils.Constants;
import com.fr.marcoucou.placereminder.utils.TypeFaceUtils;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by sirisak on 12/06/16.
 */
@ReportsCrashes(
        formUri = Constants.DB_URI,
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = Constants.ACRA_USER,
        formUriBasicAuthPassword = Constants.ACRA_PASS,
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash
)

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypeFaceUtils.overrideFont(getApplicationContext(), "serif", Constants.TYPEFACE_NAME);
       // ACRA.init(this);
    }

}