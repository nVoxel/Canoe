package com.voxeldev.canoe.utils.providers.string

/**
 * @author nvoxel
 */
interface StringResourceProvider {
    fun getWakaTimeApiBaseUrl(): String
    fun getWakaTimeOAuthBaseUrl(): String
    fun getWakaTimePhotoBaseUrl(): String
    fun getWakaTimeProfileBaseUrl(): String
    fun getOAuthAuthorizeUrl(): String

    fun getOAuthClientId(): String
    fun getOAuthClientSecret(): String
    fun getOAuthRedirectUrl(): String

    fun getJavascriptString(): String
    fun getPythonString(): String
    fun getGoString(): String
    fun getJavaString(): String
    fun getKotlinString(): String
    fun getPhpString(): String
    fun getCSharpString(): String
    fun getIndiaString(): String
    fun getChinaString(): String
    fun getUsString(): String
    fun getBrazilString(): String
    fun getRussiaString(): String
    fun getJapanString(): String
    fun getGermanyString(): String
}
