package io.reyurnible.daily_reporter.infra

import org.jetbrains.squash.definition.*

val schemes
    get() = listOf(UserTable, PostTable, CommentTable)

// Create Table for
object UserTable : TableDefinition() {
    val id = integer("id").autoIncrement().primaryKey()
    val accountName = varchar("account_name", 50)
    val passHash = varchar("passhash", 65535)
    val authority = integer("authority")
    val delFlg = bool("del_flg")
    val createdAt = datetime("created_at")
}

object PostTable : TableDefinition() {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("user_id")
    val imageData = binary("imgdata", 65535)
    val body = varchar("body", 50)
    val mime = varchar("mime", 50)
    val createdAt = datetime("created_at")
}

object CommentTable : TableDefinition() {
    val id = integer("id").autoIncrement().primaryKey()
    val postId = integer("post_id")
    val userId = integer("user_id")
    val comment = varchar("comment", 50)
    val createdAt = datetime("created_at")
}

/*
type User struct {
    ID          int       `db:"id"`
    AccountName string    `db:"account_name"`
    Passhash    string    `db:"passhash"`
    Authority   int       `db:"authority"`
    DelFlg      int       `db:"del_flg"`
    CreatedAt   time.Time `db:"created_at"`
}

type Post struct {
    ID           int       `db:"id"`
    UserID       int       `db:"user_id"`
    Imgdata      []byte    `db:"imgdata"`
    Body         string    `db:"body"`
    Mime         string    `db:"mime"`
    CreatedAt    time.Time `db:"created_at"`
    CommentCount int
            Comments     []Comment
            User         User
            CSRFToken    string
}

type Comment struct {
    ID        int       `db:"id"`
    PostID    int       `db:"post_id"`
    UserID    int       `db:"user_id"`
    Comment   string    `db:"comment"`
    CreatedAt time.Time `db:"created_at"`
    User      User
}*/
