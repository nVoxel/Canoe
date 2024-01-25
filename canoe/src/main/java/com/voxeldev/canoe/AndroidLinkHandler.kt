package com.voxeldev.canoe

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.voxeldev.canoe.root.integration.LinkHandler

/**
 * @author nvoxel
 */
class AndroidLinkHandler(private val context: Context) : LinkHandler {

    private var isCustomTabsSupported: Boolean? = null

    override fun openLink(url: String) {
        if (checkCustomTabsSupported()) {
            openUsingCustomTab(url = url)
        } else {
            openUsingImplicitIntent(url = url)
        }
    }

    private fun checkCustomTabsSupported(): Boolean =
        isCustomTabsSupported ?: run {
            val serviceIntent = Intent(SERVICE_ACTION)
            serviceIntent.setPackage(CHROME_PACKAGE)
            val packageName = context.packageManager.queryIntentServices(serviceIntent, 0)
            return packageName.isNotEmpty().also { isCustomTabsSupported = it }
        }

    private fun openUsingImplicitIntent(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)),
        )
    }

    private fun openUsingCustomTab(url: String) {
        CustomTabsIntent.Builder().build().apply {
            intent.data = Uri.parse(url)
            // Otherwise app activity may close after returning from the Custom Tab
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, startAnimationBundle)
        }
    }

    private companion object {
        const val SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService"
        const val CHROME_PACKAGE = "com.android.chrome"
    }
}
