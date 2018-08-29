package com.n7mobile.fcmnotificationbuilder;/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.KeyguardManager
 *  android.app.Notification
 *  android.app.Notification$BigTextStyle
 *  android.app.Notification$Builder
 *  android.app.Notification$Style
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.res.Resources
 *  android.graphics.Color
 *  android.media.RingtoneManager
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Process
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  android.util.Log
 */
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

final class aacj
implements Runnable {
    private final /* synthetic */ aaci a;
    private final /* synthetic */ Intent b;
    private final /* synthetic */ Intent c;

    aacj(aaci aaci2, Intent intent, Intent intent2) {
        this.a = aaci2;
        this.b = intent;
        this.c = intent2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public final void run() {
        block51 :
        {
            int var1_10;
            String var4_8;
            String var4_7;
            Intent var7_6 = this.b;
            Bundle var8_11;
            aaci var6_1 = this.a;
            ActivityManager.RunningAppProcessInfo var5_9;

            block52:
            {
                block50:
                {
                    if ("com.google.android.c2dm.intent.RECEIVE".equals(var7_6.getAction()))
                        break block50;
                    var4_7 = String.valueOf(var7_6.getAction());
                    var4_7 = var4_7.length() == 0 ? new String("Unknown intent action: ") : "Unknown intent action: ".concat(var4_7);
                    Log.w((String) "GcmListenerChimeraService", (String) var4_7);
                    break block51;
                }
                String var5_9 = var7_6.getStringExtra("message_type");
                var4_8 = var5_9;
                if (var5_9 == null) {
                    var4_8 = "gcm";
                }
                switch (var4_8.hashCode()) {
                    case 814800675: {
                        if (!var4_8.equals("send_event")) // **GOTO lbl26
                            var1_10 = 2;
                        break block52;
                    }
                    case 814694033: {
                        if (!var4_8.equals("send_error")) // **GOTO lbl26
                            var1_10 = 3;
                        break block52;
                    }
                    case 102161: {
                        if (!var4_8.equals("gcm")) // **GOTO lbl26
                            var1_10 = 0;
                        break block52;
                    }
                    //lbl26:
                    // 4 sources:
                    default:
                    {
                        // **GOTO lbl -1000
                    }
                    case -2062414158:
                }
                if (var4_8.equals("deleted_messages")) {
                    var1_10 = 1;
                } else //lbl - 1000: // 2 sources:
                {
                    var1_10 = -1;
                }
            }
            switch (var1_10) {
                case 1: {
                    break;
                }
                default: {
                    var4_8 = String.valueOf(var4_8);
                    var4_8 = var4_8.length() == 0 ? "Received message with unknown type: " : "Received message with unknown type: ".concat((String) var4_8);
                    Log.w((String) "GcmListenerChimeraService", (String) var4_8);
                    break;
                }
                case 3: {
                    if (var7_6.getStringExtra("google.message_id") == null) {
                        var7_6.getStringExtra("message_id");
                    }
                    var7_6.getStringExtra("error");
                    break;
                }
                case 2: {
                    var7_6.getStringExtra("google.message_id");
                    break;
                }
                case 0: {
                    var8_11 = var7_6.getExtras();
                    var8_11.remove("message_type");
                    var8_11.remove("android.support.content.wakelockid");
                    var1_10 = !"1".equals(xge.a(var8_11, "gcm.n.e")) ? (xge.a(var8_11, "gcm.n.icon") == null ? 0 : 1) : 1;
                    if (var1_10 == 0) // **GOTO lbl211
                    if (!((KeyguardManager) var6_1.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) // **
                    // GOTO lbl59
                    var1_10 = 0;
                    // **GOTO lbl71
                    lbl59:
                    // 1 sources:
                    var1_10 = Process.myPid();
                    var4_8 = ((ActivityManager) var6_1.getSystemService("activity")).getRunningAppProcesses();
                    if (var4_8 == null) // **GOTO lbl70
                    var4_8 = var4_8.iterator();
                    while (var4_8.hasNext()) {
                        var5_9 = (ActivityManager.RunningAppProcessInfo) var4_8.next();
                        if (var5_9.pid != var1_10) continue;
                        var1_10 = var5_9.importance == 100 ? 1 : 0;
                        // **GOTO lbl71
                    }
                    var1_10 = 0;
                    // **GOTO lbl71
                    lbl70:
                    // 1 sources:
                    var1_10 = 0;
                    lbl71:
                    // 4 sources:
                    if (var1_10 != 0) // **GOTO lbl192
                    var9_12 = xge.a((Context) var6_1);
                    var6_2 = var9_12.b(var8_11, "gcm.n.title");
                    if (TextUtils.isEmpty((CharSequence) var6_2)) {
                        String var6_3 = var9_12.a.getApplicationInfo().loadLabel(var9_12.a.getPackageManager());
                    }
                    String var10_14 = var9_12.b(var8_11, "gcm.n.body");
                    var4_8 = xge.a(var8_11, "gcm.n.icon");
                    if (TextUtils.isEmpty((CharSequence) var4_8)) // **GOTO lbl87
                    var5_9 = var9_12.a.getResources();
                    var1_10 = var5_9.getIdentifier((String) var4_8, "drawable", var9_12.a.getPackageName());
                    if (var1_10 != 0 || (var1_10 = var5_9.getIdentifier((String) var4_8, "mipmap", var9_12.a.getPackageName())) != 0) // **
                    //GOTO lbl89
                    var5_9 = new StringBuilder(String.valueOf(var4_8).length() + 57);
                    var5_9.append("Icon resource ");
                    var5_9.append((String) var4_8);
                    var5_9.append(" not found. Notification will use app icon.");
                    Log.w((String) "GcmNotification", (String) var5_9.toString());
                    lbl87:
                    // 2 sources:
                    if ((var1_10 = var9_12.a.getApplicationInfo().icon) == 0) {
                        var1_10 = 17301651;
                    }
                    lbl89:
                    // 4 sources:
                    var11_16 = xge.a(var8_11, "gcm.n.color");
                    var4_8 = xge.a(var8_11, "gcm.n.sound2");
                    if (!TextUtils.isEmpty((CharSequence) var4_8)) {
                        if (!"default".equals(var4_8) && var9_12.a.getResources().getIdentifier((String) var4_8, "raw", var9_12.a.getPackageName()) != 0) {
                            var5_9 = var9_12.a.getPackageName();
                            var7_6 = new StringBuilder(String.valueOf(var5_9).length() + 24 + String.valueOf(var4_8).length());
                            var7_6.append("android.resource://");
                            var7_6.append((String) var5_9);
                            var7_6.append("/raw/");
                            var7_6.append((String) var4_8);
                            var5_9 = Uri.parse((String) var7_6.toString());
                        } else {
                            var5_9 = RingtoneManager.getDefaultUri((int) 2);
                        }
                    } else {
                        var5_9 = null;
                    }
                    if (TextUtils.isEmpty((CharSequence) (var4_8 = xge.a(var8_11, "gcm.n.click_action")))) // **
                    //GOTO lbl109
                    var4_8 = new Intent((String) var4_8);
                    var4_8.setPackage(var9_12.a.getPackageName());
                    var4_8.setFlags(268435456);
                    // **GOTO lbl -1000
                    lbl109:
                    // 1 sources:
                    var4_8 = var9_12.a.getPackageManager().getLaunchIntentForPackage(var9_12.a.getPackageName());
                    if (var4_8 == null) {
                        Log.w((String) "GcmNotification", (String) "No activity found to launch app");
                        var7_6 = null;
                    } else {// lbl - 1000: // 2 sources:
                    boolean var13_18;
                    NotificationManager var12_17;
                    {
                        var7_6 = new Bundle(var8_11);
                        var12_17 = var7_6.keySet().iterator();
                        while (var12_17.hasNext()) {
                            var13_18 = (String) var12_17.next();
                            if (var13_18 == null || !var13_18.startsWith("google.c.")) continue;
                            var12_17.remove();
                        }
                        var4_8.putExtras((Bundle) var7_6);
                        for (Object var12_17 : var7_6.keySet()) {
                            if (!var12_17.startsWith("gcm.n.") && !var12_17.startsWith("gcm.notification."))
                                continue;
                            var4_8.removeExtra((String) var12_17);
                        }
                        var7_6 = PendingIntent.getActivity((Context) var9_12.a, (int) var9_12.c.getAndIncrement(), (Intent) var4_8, (int) 1073741824);
                    }
                    if (!qhn.c() || var9_12.a.getApplicationInfo().targetSdkVersion < 26) // **
                    //GOTO lbl169
                    var4_8 = xge.a(var8_11, "gcm.n.android_channel_id");
                    if (!qhn.c()) // **GOTO lbl152
                    var12_17 = (NotificationManager) var9_12.a.getSystemService(NotificationManager.class);
                    if (TextUtils.isEmpty((CharSequence) var4_8)) // **GOTO lbl138
                    if (var12_17.getNotificationChannel((String) var4_8) != null) // **GOTO lbl153
                    var13_18 = new StringBuilder(String.valueOf(var4_8).length() + 122);
                    var13_18.append("Notification Channel requested (");
                    var13_18.append((String) var4_8);
                    var13_18.append(") has not been created by the app. Manifest configuration, or default, value will be used.");
                    Log.w((String) "GcmNotification", (String) var13_18.toString());
                    lbl138:
                    // 2 sources:
                    if ((var4_8 = var9_12.b) != null) // **GOTO lbl153
                    var9_12.b = var9_12.a().getString("com.google.android.gms.gcm.default_notification_channel_id");
                    if (TextUtils.isEmpty((CharSequence) var9_12.b)) // **GOTO lbl146
                    if (var12_17.getNotificationChannel(var9_12.b) != null) // **GOTO lbl144
                    Log.w((String) "GcmNotification", (String) "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.");
                    // **GOTO lbl147
                    lbl144:
                    // 1 sources:
                    var4_8 = var9_12.b;
                    // **GOTO lbl153
                    lbl146:
                    // 1 sources:
                    Log.w((String) "GcmNotification", (String) "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.");
                    lbl147:
                    // 2 sources:
                    if (var12_17.getNotificationChannel("fcm_fallback_notification_channel") == null) {
                        var12_17.createNotificationChannel(new NotificationChannel("fcm_fallback_notification_channel", (CharSequence) var9_12.a.getString(2131953486), 3));
                    }
                    var9_12.b = "fcm_fallback_notification_channel";
                    var4_8 = var9_12.b;
                    // **GOTO lbl153
                    lbl152:
                    // 1 sources:
                    var4_8 = null;
                    lbl153:
                    // 5 sources:
                    var12_17 = new Notification.Builder(var9_12.a).setAutoCancel(true).setSmallIcon(var1_10);
                    if (!TextUtils.isEmpty((CharSequence) var6_4)) {
                        var12_17.setContentTitle((CharSequence) var6_4);
                    }
                    if (!TextUtils.isEmpty((CharSequence) var10_14)) {
                        var12_17.setContentText((CharSequence) var10_14);
                        var12_17.setStyle((Notification.Style) new Notification.BigTextStyle().bigText((CharSequence) var10_14));
                    }
                    if (!TextUtils.isEmpty((CharSequence) var11_16)) {
                        var12_17.setColor(Color.parseColor((String) var11_16));
                    }
                    if (var5_9 != null) {
                        var12_17.setSound((Uri) var5_9);
                    }
                    if (var7_6 != null) {
                        var12_17.setContentIntent((PendingIntent) var7_6);
                    }
                    if (var4_8 != null) {
                        var12_17.setChannelId((String) var4_8);
                    }
                    var4_8 = var12_17.build();
                    // **GOTO lbl181
                    lbl169:
                    // 1 sources:
                    var4_8 = new rp(var9_12.a).a(true).b(var1_10);
                    if (!TextUtils.isEmpty((CharSequence) var6_4)) {
                        var4_8.c((CharSequence) var6_4);
                    }
                    if (!TextUtils.isEmpty((CharSequence) var10_14)) {
                        var4_8.b(var10_14);
                    }
                    if (!TextUtils.isEmpty((CharSequence) var11_16)) {
                        var4_8.d = Color.parseColor((String) var11_16);
                    }
                    if (var5_9 != null) {
                        var4_8.a((Uri) var5_9);
                    }
                    if (var7_6 != null) {
                        var4_8.e = var7_6;
                    }
                    var4_8 = var4_8.a();
                    lbl181:
                    // 2 sources:
                    var6_5 = xge.a(var8_11, "gcm.n.tag");
                    var7_6 = (NotificationManager) var9_12.a.getSystemService("notification");
                    var5_9 = var6_5;
                    if (TextUtils.isEmpty((CharSequence) var6_5)) {
                        var2_19 = SystemClock.uptimeMillis();
                        var5_9 = new StringBuilder(37);
                        var5_9.append("GCM-Notification:");
                        var5_9.append(var2_19);
                        var5_9 = var5_9.toString();
                    }
                    var7_6.notify((String) var5_9, 0, (Notification) var4_8);
                    break;
                    lbl192:
                    // 1 sources:
                    var7_6 = new Bundle();
                    var9_13 = var8_11.keySet().iterator();
                    while (var9_13.hasNext()) {
                        var5_9 = (String) var9_13.next();
                        var10_15 = var8_11.getString((String) var5_9);
                        var4_8 = var5_9;
                        if (var5_9.startsWith("gcm.notification.")) {
                            var4_8 = var5_9.replace("gcm.notification.", "gcm.n.");
                        }
                        if (!var4_8.startsWith("gcm.n.")) continue;
                        if (!"gcm.n.e".equals(var4_8)) {
                            var7_6.putString(var4_8.substring(6), var10_15);
                        }
                        var9_13.remove();
                    }
                    var4_8 = var7_6.getString("sound2");
                    if (var4_8 != null) {
                        var7_6.remove("sound2");
                        var7_6.putString("sound", (String) var4_8);
                    }
                    if (!var7_6.isEmpty()) {
                        var8_11.putBundle("notification", (Bundle) var7_6);
                    }
                    lbl211:
                    // 4 sources:
                    var8_11.getString("from");
                    var8_11.remove("from");
                    var4_8 = var8_11.keySet().iterator();
                    while (var4_8.hasNext()) {
                        var5_9 = (String) var4_8.next();
                        if (var5_9 == null || !var5_9.startsWith("google.c.")) continue;
                        var4_8.remove();
                    }
                    var6_1.a(var8_11);
                }
            }
        }
        this.a.a(this.c);
    }
}
