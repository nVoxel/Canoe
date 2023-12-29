package com.voxeldev.canoe.utils.di

import com.voxeldev.canoe.utils.platform.NetworkHandler
import com.voxeldev.canoe.utils.providers.string.ContextStringResourceProvider
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import com.voxeldev.canoe.utils.providers.token.SharedPrefsTokenProvider
import com.voxeldev.canoe.utils.providers.token.TokenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val utilsModule = module {

    single { NetworkHandler(context = androidContext()) }

    single<TokenProvider> { SharedPrefsTokenProvider(androidContext()) }

    single<StringResourceProvider> { ContextStringResourceProvider(androidContext()) }
}
