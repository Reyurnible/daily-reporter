package io.reyurnible.daily_reporter.infra

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object ApiClient {
    var client =
        OkHttpClient.Builder()
            /*.addInterceptor {

            }
            .addNetworkInterceptor {

            }*/
            .build()
    val gson: Gson =
            GsonBuilder().create()

    inline fun <reified T> get(url: String): Deferred<T> = async() {
        val request: Request = Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        return@async gson.fromJson<T>(response.body()?.string(), T::class.java)
    }
}