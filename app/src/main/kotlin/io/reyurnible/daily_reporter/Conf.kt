package io.reyurnible.daily_reporter

import io.ktor.application.Application
import io.ktor.config.ApplicationConfig

sealed class Conf(private val application: Application) {
    val applicationConfig: ApplicationConfig = application.environment.config

    class Database(application: Application) : Conf(application) {
        private object Keys {
            const val url = "database.url"
            const val driver = "database.driver"
            const val user = "database.user"
            const val password = "database.password"
        }

        val url: String
            get() = applicationConfig.stringProperty(Keys.url)

        val driver: String
            get() = applicationConfig.stringProperty(Keys.driver)

        val user: String
            get() = applicationConfig.stringProperty(Keys.user)

        val password: String
            get() = applicationConfig.stringProperty(Keys.password)
    }
}

private fun ApplicationConfig.stringProperty(name: String): String = property(name).getString()
private fun ApplicationConfig.stringPropertyOrNull(name: String): String? = propertyOrNull(name)?.getString()