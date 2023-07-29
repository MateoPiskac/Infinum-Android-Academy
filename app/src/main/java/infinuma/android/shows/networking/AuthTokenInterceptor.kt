package infinuma.android.shows.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val accessToken: String,
    private val client: String,
    private val tokenType: String,
    private val uid: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("access-token", accessToken)
            .addHeader("client", client)
            .addHeader("token-type", tokenType)
            .addHeader("uid", uid)
            .build()
        return chain.proceed(request)
    }
}
