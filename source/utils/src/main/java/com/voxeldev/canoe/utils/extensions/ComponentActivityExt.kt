package com.voxeldev.canoe.utils.extensions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.voxeldev.canoe.utils.R
import android.R as AndroidR

/**
 * @author nvoxel
 */
fun ComponentActivity.registerNotificationsPermissionLauncher(): ActivityResultLauncher<String> =
    registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (!isGranted) {
            Snackbar.make(
                findViewById(AndroidR.id.content),
                R.string.notifications_denied_warning,
                Snackbar.LENGTH_LONG,
            ).setAction(
                R.string.notifications_denied_warning_fix,
            ) {
                with(Intent()) {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(
                        Settings.EXTRA_APP_PACKAGE,
                        applicationContext.packageName,
                    )
                    startActivity(this)
                }
            }.show()
        }
    }

fun ComponentActivity.checkNotificationsPermission(launcher: ActivityResultLauncher<String>) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    if (ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
        Toast.makeText(
            applicationContext,
            R.string.notifications_rationale,
            Toast.LENGTH_LONG,
        ).show()
    }

    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
}
