package infinuma.android.shows.networking

import infinuma.android.shows.models.GetReviewsResponse
import infinuma.android.shows.models.GetShowsResponse
import infinuma.android.shows.models.RegisterRequest
import infinuma.android.shows.models.RegisterResponse
import infinuma.android.shows.models.SignInRequest
import infinuma.android.shows.models.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @POST("/users/sign_in")
    suspend fun loginUser(@Body request: SignInRequest): retrofit2.Response<SignInResponse>

    @GET("shows")
    @Headers("token-type: Bearer")
    suspend fun getShows(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String
    ): GetShowsResponse

    @GET("shows/{id}/reviews")
    @Headers("token-type: Bearer")
    suspend fun getReviews(
        @Path("id") showId: Int,
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String
    ): GetReviewsResponse

}

//@POST("/reviews")
//@GET("/shows/{show_id}/reviews")
//@GET("/shows/top_rated")