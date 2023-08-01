package infinuma.android.shows.networking

import infinuma.android.shows.models.AddReviewRequest
import infinuma.android.shows.models.GetReviewsResponse
import infinuma.android.shows.models.GetShowsResponse
import infinuma.android.shows.models.RegisterRequest
import infinuma.android.shows.models.RegisterResponse
import infinuma.android.shows.models.SignInRequest
import infinuma.android.shows.models.SignInResponse
import infinuma.android.shows.models.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("/reviews")
    @Headers("token-type: Bearer")
    suspend fun addReview(
        @Body request: AddReviewRequest,
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String
    ): GetReviewsResponse

    @Multipart
    @PUT("/users")
    @Headers("Cookie: BetterErrors-2.9.1-CSRF-Token=e089e6a6-7556-40c0-8194-25899188c2b2")
    suspend fun uploadImage(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String,
        @Part image: MultipartBody.Part
    ) : UploadImageResponse
}

//@GET("/shows/top_rated")