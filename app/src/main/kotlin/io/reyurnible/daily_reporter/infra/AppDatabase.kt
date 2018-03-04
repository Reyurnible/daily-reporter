package io.reyurnible.daily_reporter.infra

import io.ktor.application.Application


class Database(application: Application) {

    init {
        
        Database.connect("jdbc:mysql://localhost/sample", "com.mysql.jdbc.Driver","user","password")
    }

}