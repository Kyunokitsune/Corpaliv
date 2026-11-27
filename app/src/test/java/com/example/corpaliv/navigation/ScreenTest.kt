package com.example.corpaliv.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreenTest {

    @Test
    fun rutasDePantallas_tienenLosValoresEsperados() {
        assertEquals("HomeScreen", Screen.Home.route)
        assertEquals("InfoScreen", Screen.Info.route)
        assertEquals("ContactoScreen", Screen.Contact.route)
        assertEquals("RegistrosScreen", Screen.Registros.route)
        assertEquals("LoginScreen", Screen.Login.route)
        assertEquals("ApiScreen", Screen.Api.route)
    }

    @Test
    fun todasLasPantallas_tienenRutasUnicasYNoVacias() {
        val todasLasPantallas = listOf(
            Screen.Home,
            Screen.Info,
            Screen.Contact,
            Screen.Registros,
            Screen.Login,
            Screen.Api
        )

        val rutas = todasLasPantallas.map { it.route }

        // Ninguna ruta debe estar en blanco
        assertTrue(rutas.all { it.isNotBlank() })

        // La cantidad de rutas únicas debe ser igual al total de pantallas
        assertEquals(rutas.size, rutas.distinct().size)
    }
}