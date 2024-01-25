package com.voxeldev.canoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.firebase.FirebaseApp
import com.voxeldev.canoe.compose.ui.content.root.RootContent
import com.voxeldev.canoe.root.integration.RootComponent
import com.voxeldev.canoe.utils.extensions.checkNotificationsPermission
import com.voxeldev.canoe.utils.extensions.lazyUnsafe
import com.voxeldev.canoe.utils.extensions.registerNotificationsPermissionLauncher
import org.koin.android.ext.android.get

internal class MainActivity : ComponentActivity() {

    private val androidLinkHandler by lazyUnsafe { AndroidLinkHandler(context = this) }

    private val launcher = registerNotificationsPermissionLauncher()

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
