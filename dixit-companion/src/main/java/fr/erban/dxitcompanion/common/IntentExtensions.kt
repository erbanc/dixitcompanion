package fr.erban.dxitcompanion.common

import android.content.Intent
import android.os.Build
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.parcelableExtra(key: String): T? =
    if (Build.VERSION.SDK_INT >= 33)
        getParcelableExtra(key, T::class.java)
    else
        @Suppress("DEPRECATION")
        getParcelableExtra(key)
