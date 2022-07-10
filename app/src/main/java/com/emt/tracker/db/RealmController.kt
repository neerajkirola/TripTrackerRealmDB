package com.emt.tracker.db

import io.realm.Realm
import io.realm.RealmConfiguration


object RealmController {
    private const val tracker_db_name = "trackerDb"
    fun initReamDb() {
        val realmConfig = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .name(tracker_db_name)
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}