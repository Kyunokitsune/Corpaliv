package com.example.corpaliv.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationEventTest {

    @Test
    fun navigateTo_soloConRuta_tieneValoresPorDefectoEsperados() {
        val evento = NavigationEvent.NavigateTo(route = Screen.Home)

        assertEquals(Screen.Home, evento.route)
        assertNull(evento.popUpToRoute)
        assertFalse(evento.inclusive)
        assertFalse(evento.singleTop)
    }

    @Test
    fun navigateTo_conParametrosPersonalizados_asignaPropiedadesCorrectamente() {
        val evento = NavigationEvent.NavigateTo(
            route = Screen.Home,
            popUpToRoute = Screen.Login,
            inclusive = true,
            singleTop = true
        )

        assertEquals(Screen.Home, evento.route)
        assertEquals(Screen.Login, evento.popUpToRoute)
        assertTrue(evento.inclusive)
        assertTrue(evento.singleTop)
    }

    @Test
    fun navigateTo_conMismosParametros_sonIguales() {
        val evento1 = NavigationEvent.NavigateTo(route = Screen.Info, singleTop = true)
        val evento2 = NavigationEvent.NavigateTo(route = Screen.Info, singleTop = true)

        assertEquals(evento1, evento2)
        assertEquals(evento1.hashCode(), evento2.hashCode())
    }
}