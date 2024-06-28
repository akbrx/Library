package com.example.library.API

import com.example.library.Respon.ApiResponse

import com.example.library.Respon.BukuResponse
import com.example.library.Respon.LoanResponse
import com.example.library.Respon.MemberResponse
import com.example.library.Respon.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api-regist.php")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("api-login.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("get-buku.php")
    fun getBuku(): Call<BukuResponse>

    @GET("get-loan.php") // Replace with your actual endpoint
    fun getLoans(): Call<LoanResponse>

    @GET("get-member.php")
    fun getMemberData(): Call<MemberResponse>

}