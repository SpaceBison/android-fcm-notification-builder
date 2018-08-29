package com.n7mobile.fcmnotificationbuilder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FCMNotificationHelper {
    private static final AtomicInteger requestCodes = new AtomicInteger();

    public static void showFcmNotification(Context context, RemoteMessage remoteMessage) {
        Notification.Builder builder = new Notification.Builder(context);
        setFcmDefaults(context, builder, remoteMessage);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String tag = remoteMessage.getNotification().getTag();
        if (tag != null) {
            notificationManager.notify(tag.hashCode(), buildNotifcation(builder));
        }
    }

    private static Notification buildNotifcation(Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    public static void setFcmDefaults(Context context, Notification.Builder builder, RemoteMessage remoteMessage) {
        RemoteMessage.Notification remoteNotification = remoteMessage.getNotification();

        if (remoteNotification == null) {
            return;
        }

        setupTitle(context, builder, remoteNotification);
        setupContentText(builder, remoteNotification);
        setupSmallIcon(context, builder, remoteNotification);
        setupColor(context, builder, remoteNotification);
        setupSound(context, builder, remoteNotification);
        setupContentIntent(context, builder, remoteMessage);
        setupChannelId(context, builder);
        setupGroup(builder, remoteMessage);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getChannelId(Context context) {
        String channelId = getMetaData(context).getString("com.google.firebase.messaging.default_notification_channel_id");
        if (channelId != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.getNotificationChannel(channelId) == null) {
                channelId = null;
            }
        }
        return channelId;
    }

    private static Integer getColor(Context context, RemoteMessage.Notification remoteNotification) {
        String color = remoteNotification.getColor();
        Integer colorArgb = null;

        if (!TextUtils.isEmpty(color)) {
            colorArgb = Color.parseColor(color);
        }

        if (colorArgb == null) {
            int colorResource = getMetaData(context).getInt("com.google.firebase.messaging.default_notification_color");

            if (colorResource != 0) {
                colorArgb = ContextCompat.getColor(context, colorResource);
            }
        }

        return colorArgb;
    }

    private static PendingIntent getContentIntent(Context context, RemoteMessage remoteMessage) {
        return getContentIntent(context, remoteMessage.getNotification(), remoteMessage.getMessageId(), remoteMessage.getData());
    }

    private static PendingIntent getContentIntent(Context context, RemoteMessage.Notification remoteNotification, String messageId, Map<String, String> data) {
        String clickAction = remoteNotification.getClickAction();

        Intent intent = null;
        if (clickAction != null) {
            intent = new Intent(clickAction);
            intent.setPackage(context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        }

        if (intent == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        return PendingIntent.getActivity(context, messageId.hashCode(), intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private static CharSequence getContentTitle(Context context, RemoteMessage.Notification remoteNotification) {
        CharSequence title = remoteNotification.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = context.getApplicationInfo().loadLabel(context.getPackageManager());
        }
        return title;
    }

    private static Bundle getMetaData(Context context) {
        return context.getApplicationInfo().metaData;
    }

    private static int getSmallIcon(Context context, RemoteMessage.Notification remoteNotification) {
        int iconResource = 0;
        String icon = remoteNotification.getIcon();

        if (!TextUtils.isEmpty(icon)) {
            iconResource = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());

            if (iconResource == 0) {
                context.getResources().getIdentifier(icon, "mipmap", context.getPackageName());
            }
        }

        if (iconResource == 0) {
            iconResource = context.getApplicationInfo().icon;
        }

        if (iconResource == 0) {
            iconResource = getMetaData(context).getInt("com.google.firebase.messaging.default_notification_icon");
        }

        // todo: default icon

        return iconResource;
    }

    @Nullable
    private static Uri getSound(Context context, RemoteMessage.Notification remoteNotification) {
        String sound = remoteNotification.getSound();
        Uri soundUri = null;

        if (!"default".equals(sound)) {
            if (context.getResources().getIdentifier(sound, "raw", context.getPackageName()) != 0) {
                soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + sound);
            }
        }

        if (soundUri == null) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        return soundUri;
    }

    private static void setupChannelId(Context context, Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getChannelId(context);
            builder.setChannelId(channelId);
        }
    }

    private static void setupColor(Context context, Notification.Builder builder, RemoteMessage.Notification remoteNotification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Integer colorArgb = getColor(context, remoteNotification);
            if (colorArgb != null) {
                builder.setColor(colorArgb);
            }
        }
    }

    private static void setupContentIntent(Context context, Notification.Builder builder, RemoteMessage remoteMessage) {
        PendingIntent contentIntent = getContentIntent(context, remoteMessage);
        if (contentIntent != null) {
            builder.setContentIntent(contentIntent);
        }
    }

    private static void setupContentText(Notification.Builder builder, RemoteMessage.Notification remoteNotification) {
        String contentText = remoteNotification.getBody();
        if (contentText != null) {
            builder.setContentText(contentText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setStyle(new Notification.BigTextStyle().bigText(contentText));
            }
        }
    }

    private static void setupGroup(Notification.Builder builder, RemoteMessage remoteMessage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            String collapseKey = remoteMessage.getCollapseKey();
            if (collapseKey != null) {
                builder.setGroup(collapseKey);
            }
        }
    }

    private static void setupSmallIcon(Context context, Notification.Builder builder, RemoteMessage.Notification remoteNotification) {
        int smallIcon = getSmallIcon(context, remoteNotification);
        builder.setSmallIcon(smallIcon);
    }

    private static void setupSound(Context context, Notification.Builder builder, RemoteMessage.Notification remoteNotification) {
        Uri sound = getSound(context, remoteNotification);
        if (sound != null) {
            builder.setSound(sound);
        }
    }

    private static void setupTitle(Context context, Notification.Builder builder, RemoteMessage.Notification remoteNotification) {
        CharSequence contentTitle = getContentTitle(context, remoteNotification);
        builder.setContentTitle(contentTitle);
    }
}
