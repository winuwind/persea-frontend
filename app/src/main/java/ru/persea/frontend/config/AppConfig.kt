package ru.persea.frontend.config

import android.content.Context
import org.json.JSONObject
import java.io.InputStream

data class ServiceConfig(
    val host: String,
    val port: Int,
    val protocol: String,
    val path: String? = null
) {
    fun getBaseUrl(): String {
        return "$protocol://$host:$port"
    }

    fun getFullUrl(): String {
        return if (path != null) {
            "$protocol://$host:$port$path"
        } else {
            getBaseUrl()
        }
    }
}

data class AppConfig(
    val auth: ServiceConfig,
    val product: ServiceConfig,
    val user: ServiceConfig,
    val recommendation: ServiceConfig
) {
    companion object {
        private var instance: AppConfig? = null

        fun getInstance(): AppConfig {
            return instance ?: throw IllegalStateException("AppConfig not initialized")
        }

        fun loadFromJson(context: Context, fileName: String = "config"): AppConfig {
            val resourceId = context.resources.getIdentifier(fileName, "raw", context.packageName)

            if (resourceId == 0) {
                throw IllegalArgumentException("Resource not found: $fileName in raw folder")
            }

            val inputStream = context.resources.openRawResource(resourceId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val jsonObject = JSONObject(jsonString)
            val services = jsonObject.getJSONObject("services")

            val auth = parseServiceConfig(services.getJSONObject("auth"))
            val product = parseServiceConfig(services.getJSONObject("product"))
            val user = parseServiceConfig(services.getJSONObject("user"))
            val recommendation = parseServiceConfig(services.getJSONObject("recommendation"))

            instance = AppConfig(auth, product, user, recommendation)
            return instance!!
        }

        private fun parseServiceConfig(json: JSONObject): ServiceConfig {
            return ServiceConfig(
                host = json.getString("host"),
                port = json.getInt("port"),
                protocol = json.getString("protocol"),
                path = if (json.has("path")) json.getString("path") else null
            )
        }
    }
}