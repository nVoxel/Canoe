package com.voxeldev.canoe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.firebase.FirebaseApp
import com.voxeldev.canoe.compose.ui.root.RootContent
import com.voxeldev.canoe.root.integration.LinkHandler
import com.voxeldev.canoe.root.integration.RootComponent
import com.voxeldev.canoe.utils.extensions.checkNotificationsPermission
import com.voxeldev.canoe.utils.extensions.registerNotificationsPermissionLauncher
import org.koin.android.ext.android.get

internal class MainActivity : ComponentActivity() {

    private val launcher = registerNotificationsPermissionLauncher()

    private val androidLinkHandler = LinkHandler { url ->
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)

        checkNotificationsPermission(launcher)

        val root =
            RootComponent(
                componentContext = defaultComponentContext(),
                storeFactory = DefaultStoreFactory(),
                linkHandler = androidLinkHandler,
                tokenRepository = get(),
                stringResourceProvider = get(),
                deepLink = intent.data,
            )

        setContent {
            RootContent(component = root)
        }
    }
}
