package com.example.uasproject_zerowaste;

import android.app.Application;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;

public class ZeroWasteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("zerowaste.realm")
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                // MENJAMIN TABEL TERBENTUK SAAT DATABASE PERTAMA KALI DIBUAT
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // Menggunakan dynamic schema di dalam transaksi awal
                        RealmSchema schema = realm.getSchema();
                        if (!schema.contains("User")) {
                            schema.create("User")
                                    .addPrimaryKey("userId")
                                    .addField("name", String.class)
                                    .addField("email", String.class)
                                    .addField("password", String.class)
                                    .addField("phoneNumber", String.class);
                        }
                    }
                })
                .build();

        Realm.setDefaultConfiguration(config);
    }
}