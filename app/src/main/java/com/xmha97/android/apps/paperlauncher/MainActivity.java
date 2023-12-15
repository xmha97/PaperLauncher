package com.xmha97.android.apps.paperlauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView clockTextView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockTextView = findViewById(R.id.clockTextView);
        handler = new Handler();

        handler.postDelayed(updateClockRunnable, 1000);
    }

    private Runnable updateClockRunnable = new Runnable() {
        @Override
        public void run() {
            updateClock();
            // دوباره اجرای متد updateClock() هر یک ثانیه یکبار
            handler.postDelayed(this, 1000);
        }
    };

    private void updateClock() {
        // دریافت تاریخ و زمان فعلی
        Date currentTime = new Date();
        // تعیین قالب زمان برای نمایش به صورت ۲۴ ساعته
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        // تبدیل تاریخ به رشته بر اساس قالب مورد نظر
        String formattedTime = dateFormat.format(currentTime);
        // نمایش زمان در TextView
        clockTextView.setText(formattedTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // حذف تمام Callback ها و اجرایی های انجام نشده از Handler
        handler.removeCallbacksAndMessages(null);
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
        String MyList1 = "";
        String MyList2 = "";
        for (ApplicationInfo packageInfo : packages)
        {
            if (hasLauncherIntent(this,packageInfo.packageName)) {
                MyList1 = MyList1 + packageInfo.packageName + "\n";
            }
            else {
                MyList2 = MyList2 + packageInfo.packageName + "\n";
            }
        }
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Applicable Packages:" + "\n" + MyList1 + "\n" + "Inapplicable Packages:\n" + MyList2);
        dlgAlert.setTitle("Packages");
        dlgAlert.setPositiveButton(android.R.string.ok, null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void buttonClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Testing")
                .setMessage("Press Yes to open the Settings app\nPress cancel to get a list of installed apps.")
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