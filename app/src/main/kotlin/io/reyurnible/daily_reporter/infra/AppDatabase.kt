package io.reyurnible.daily_reporter.infra

import io.ktor.application.Application
import io.reyurnible.daily_reporter.Conf
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class AppDatabase(application: Application) {

    object Report : Table("report") {
        val id = integer("id").autoIncrement().primaryKey()
        val date = date("date")
    }

    init {
        val config = Conf.Database(application)
        Database.connect(config.url, config.driver, config.user, config.password)
        transaction {
            create(Report)
        }
    }
}