package io.reyurnible.daily_reporter.controller

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

// Indexの実装
fun Route.index() = route("/") {
    get {
        call.respondText("""

            """, ContentType.Text.Html)

    }
}