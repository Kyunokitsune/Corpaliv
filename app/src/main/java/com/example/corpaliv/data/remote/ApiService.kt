package com.example.corpaliv.data.remote

import com.example.corpaliv.data.model.UsuarioApiDto
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun obtenerUsuarios(): List<UsuarioApiDto>
}
