package hr.bagy94.android.common.sharedpref

import hr.bagy94.android.base.shared_pref.DefaultSharedPreference

interface AppSharedPreference : DefaultSharedPreference{
    var authToken : String?
}