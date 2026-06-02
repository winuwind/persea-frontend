package ru.persea.frontend

import android.app.Application
import ru.persea.frontend.config.AppConfig
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.api.recommendation.RecommendationRetrofitClient
import ru.persea.frontend.data.api.users.UserRetrofitClient

class PerseaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.loadFromJson(this)
        UserRetrofitClient.init(this)
        ProductRetrofitClient.init(this)
        RecommendationRetrofitClient.init(this)
    }
}