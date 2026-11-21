package com.example.corpaliv.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioApiDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val telefono: String,
    @SerializedName("website")
    val sitioWeb: String
)
