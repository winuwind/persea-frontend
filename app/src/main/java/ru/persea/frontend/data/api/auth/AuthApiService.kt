package ru.persea.frontend.data.api.auth

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.persea.frontend.data.model.users.LoginResponse
import ru.persea.frontend.data.model.users.TokenRefreshResponse

interface AuthApiService {
    @FormUrlEncoded
    @POST(".")
    suspend fun getToken(
        @Field("client_id") clientId: String = "android-app",
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("redirect_uri") redirectUri: String
    ): LoginResponse

    @FormUrlEncoded
    @POST(".")
    suspend fun refreshToken(
        @Field("client_id") clientId: String = "android-app",
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): TokenRefreshResponse
}