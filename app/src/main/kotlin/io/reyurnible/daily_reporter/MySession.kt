package io.reyurnible.daily_reporter

import io.reyurnible.daily_reporter.model.SessionUser

class MySession {
    var user: SessionUser? = null
    var csrfToken: String? = null
}