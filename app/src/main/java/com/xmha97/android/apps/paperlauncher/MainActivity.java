package com.xmha97.android.apps.paperlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.ResolveInfo;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void openSettings(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.settings", "com.android.settings.Settings");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void getApps() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String mylist1 = "";
        String mylist2 = "";
        for (ApplicationInfo packageInfo : packages)
        {
            if (hasLauncherIntent(this,packageInfo.packageName)) {
                mylist1 += packageInfo.packageName + "\n";
            }
            else {
                mylist2 += packageInfo.packageName + "\n";
            }
        }
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Runnable Packages:\n" + mylist1 + "\nUnrunable Packages:\n" + mylist2);
        dlgAlert.setTitle("Packages");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void buttonClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Testing")
                .setMessage("Press Yes to open the Settings app\nPress No to get a list of installed apps.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> openSettings(this))
                .setNegativeButton(android.R.string.no, (dialog, which) -> getApps())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static boolean hasLauncherIntent(Context context, String packageName) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setPackage(packageName);
        PackageManager packageManager = context.getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(launcherIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}