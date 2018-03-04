package io.reyurnible.daily_reporter

import com.google.gson.GsonBuilder
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.PartialContentSupport
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.routing.Routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.reyurnible.daily_reporter.infra.AppDatabase

val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()

class App {

    // Call from application.conf setting
    fun Application.install() {
        val database = AppDatabase(this)

        install(DefaultHeaders)
        install(CallLogging)
        install(ConditionalHeaders)
        install(PartialContentSupport)
        install(Sessions) {
            cookie<MySession>("SESSION")
        }
        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter(gson))
        }
        install(Routing) {
            static("../public") {

            }
        }
    }
}