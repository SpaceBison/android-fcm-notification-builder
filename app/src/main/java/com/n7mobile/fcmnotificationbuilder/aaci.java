package com.n7mobile.fcmnotificationbuilder;/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 */
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.chimera.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Deprecated
public class aaci
extends Service {
    private Binder a;
    private final ExecutorService b = Executors.newSingleThreadExecutor();
    private int c;
    private final Object d = new Object();
    private int e = 0;

    public aaci() {
    }

    public aaci(byte by) {
        this();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    final void a(Intent object) {
        if (object != null) {
            tk.a(object);
        }
        object = this.d;
        synchronized (object) {
            --this.e;
            if (this.e == 0) {
                this.stopSelfResult(this.c);
            }
            return;
        }
    }

    public void a(Bundle bundle) {
    }

    @Override
    public final IBinder onBind(Intent intent) {
        synchronized (this) {
            if (this.a == null) {
                this.a = new aack();
            }
            intent = this.a;
            return intent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public final int onStartCommand(Intent intent, int n, int n2) {
        Object object = this.d;
        // MONITORENTER : object
        this.c = n2;
        ++this.e;
        // MONITOREXIT : object
        if (intent == null) {
            this.a(intent);
            return 2;
        }
        this.b.execute(new aacj(this, intent, intent));
        return 3;
    }
}
