package io.reyurnible.daily_reporter.infra

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.log
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.run
import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.dialects.h2.H2Connection
import org.jetbrains.squash.query.select
import org.jetbrains.squash.results.get
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import kotlin.coroutines.experimental.CoroutineContext
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.query.from
import org.jetbrains.squash.query.where
import java.io.InputStreamReader
import java.io.BufferedReader


class Database(application: Application) {
    private val dispatcher: CoroutineContext
    private val connectionPool: HikariDataSource
    private val connection: DatabaseConnection

    init {
        val config = application.environment.config.config("database")
        val url = config.property("connection").getString()
        val poolSize = config.property("poolSize").getString().toInt()
        application.log.info("Connecting to database at '$url'")

        dispatcher = newFixedThreadPoolContext(poolSize, "database-pool")
        val cfg = HikariConfig()
        cfg.jdbcUrl = url
        cfg.maximumPoolSize = poolSize
        cfg.validate()

        connectionPool = HikariDataSource(cfg)

        connection = H2Connection { connectionPool.connection }
        connection.transaction {
            databaseSchema().create(schemes)
        }
    }

    suspend fun createUser(accountName: String, password: String): Int = run(dispatcher) {
        connection.transaction {
            insertInto(UserTable).values {
                it[UserTable.accountName] = accountName
                it[passHash] = calculatePashash(accountName, password)
            }.execute()
            from(UserTable).select(UserTable.id).execute().last().let { it[UserTable.id] }
        }
    }

    suspend fun existUserByName(accountName: String): Boolean = run(dispatcher) {
        connection.transaction {
            from(UserTable)
                    .where { UserTable.accountName eq accountName }
                    .select()
                    .execute()
                    .any()
        }
    }

    // Coroutines function
    suspend fun getPostList(): List<Post> = run(dispatcher) {
        connection.transaction {
            PostTable.select().execute().map { dao ->
                Post(
                        id = dao[PostTable.id],
                        userId = dao[PostTable.userId],
                        imageData = dao[PostTable.imageData],
                        body = dao[PostTable.body],
                        mime = dao[PostTable.mime],
                        createdAt = dao[PostTable.createdAt]
                )
            }.toList()
        }
    }

    private fun digest(src: String): String {
        // opensslのバージョンによっては(stdin) = というのがつくので取る
        val process = Runtime.getRuntime().exec(
                """
                `printf "%s" #{Shellwords.shellescape(${src})} | openssl dgst -sha512 | sed 's/^.*= //'`
                """
        )
        val stdInput = BufferedReader(InputStreamReader(process.inputStream))
        val stdError = BufferedReader(InputStreamReader(process.errorStream))
        // read the output from the command
        return stdInput.readLines().joinToString("").trimStart().trimEnd()
    }

    private fun calculatePashash(accountName: String, password: String): String =
            digest("${password}:${calculateSalt(accountName)}")

    private fun calculateSalt(accountName: String): String =
            digest(accountName)
}