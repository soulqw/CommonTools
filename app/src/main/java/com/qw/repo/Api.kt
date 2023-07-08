package com.qw.repo

import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<Repo>
}