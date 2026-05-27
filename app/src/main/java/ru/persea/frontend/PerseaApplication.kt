package ru.persea.frontend

import android.app.Application
import ru.persea.frontend.config.AppConfig

class PerseaApplication : Application() {
    override fun onCreate() {
        AppConfig.loadFromJson(this)
        super.onCreate()
    }
}