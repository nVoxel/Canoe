package com.voxeldev.canoe.utils.providers.string

import android.content.Context
import com.voxeldev.canoe.utils.R

/**
 * @author nvoxel
 */
internal class ContextStringResourceProvider(context: Context) : StringResourceProvider {

    private val resources = context.resources

    override fun getWakaTimeApiBaseUrl(): String = resources.getString(R.string.wakatime_api_base_url)
    override fun getWakaTimeOAuthBaseUrl(): String = resources.getString(R.string.wakatime_oauth_base_url)
    override fun getWakaTimePhotoBaseUrl(): String = resources.getString(R.string.wakatime_photo_base_url)
    override fun getWakaTimeProfileBaseUrl(): String = resources.getString(R.string.wakatime_profile_base_url)
    override fun getOAuthAuthorizeUrl(): String = "${getWakaTimeOAuthBaseUrl()}authorize?client_id=${getOAuthClientId()}" +
            "&scope=email,read_logged_time,read_stats&response_type=code&redirect_uri=${getOAuthRedirectUrl()}"

    override fun getOAuthClientId(): String = resources.getString(R.string.oauth_client_id)
    override fun getOAuthClientSecret(): String = resources.getString(R.string.oauth_client_secret)
    override fun getOAuthRedirectUrl(): String = resources.getString(R.string.oauth_redirect_url)

    override fun getJavascriptString(): String = resources.getString(R.string.language_javascript)

    override fun getPythonString(): String = resources.getString(R.string.language_python)

    override fun getGoString(): String = resources.getString(R.string.language_go)

    override fun getJavaString(): String = resources.getString(R.string.language_java)

    override fun getKotlinString(): String = resources.getString(R.string.language_kotlin)

    override fun getPhpString(): String = resources.getString(R.string.language_php)

    override fun getCSharpString(): String = resources.getString(R.string.language_csharp)

    override fun getIndiaString(): String = resources.getString(R.string.country_india)

    override fun getChinaString(): String = resources.getString(R.string.country_china)

    override fun getUsString(): String = resources.getString(R.string.country_us)

    override fun getBrazilString(): String = resources.getString(R.string.country_brazil)

    override fun getRussiaString(): String = resources.getString(R.string.country_russia)

    override fun getJapanString(): String = resources.getString(R.string.country_japan)

    override fun getGermanyString(): String = resources.getString(R.string.country_germany)
}
