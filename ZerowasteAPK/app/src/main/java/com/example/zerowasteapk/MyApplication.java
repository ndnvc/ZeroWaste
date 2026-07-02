package com.example.zerowasteapk;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Inisialisasi Realm
        Realm.init(this);
        
        // Konfigurasi Realm
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("zerowaste.realm")
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        
        Realm.setDefaultConfiguration(config);
    }
}
