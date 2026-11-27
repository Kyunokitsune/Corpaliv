package com.example.corpaliv.data.model

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsuarioApiDtoTest {

    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = Gson()
    }

    @Test
    fun deserializacionGson_jsonValidoDeApi_mapeaCamposCorrectamente() {
        val jsonRespuestaApi = """
            {
                "id": 1,
                "name": "Leanne Graham",
                "email": "Sincere@april.biz",
                "phone": "1-770-736-8031 x56442",
                "website": "hildegard.org"
            }
        """.trimIndent()

        val usuario = gson.fromJson(jsonRespuestaApi, UsuarioApiDto::class.java)

        assertEquals(1, usuario.id)
        assertEquals("Leanne Graham", usuario.nombre)
        assertEquals("Sincere@april.biz", usuario.email)
        assertEquals("1-770-736-8031 x56442", usuario.telefono)
        assertEquals("hildegard.org", usuario.sitioWeb)
    }

    @Test
    fun deserializacionGson_listaDeUsuarios_mapeaTodosLosElementos() {
        val jsonLista = """
            [
                {
                    "id": 1,
                    "name": "Usuario Uno",
                    "email": "uno@correo.com",
                    "phone": "111-111",
                    "website": "uno.cl"
                },
                {
                    "id": 2,
                    "name": "Usuario Dos",
                    "email": "dos@correo.com",
                    "phone": "222-222",
                    "website": "dos.cl"
                }
            ]
        """.trimIndent()

        val listaUsuarios = gson.fromJson(jsonLista, Array<UsuarioApiDto>::class.java).toList()

        assertEquals(2, listaUsuarios.size)
        assertEquals("Usuario Uno", listaUsuarios[0].nombre)
        assertEquals("Usuario Dos", listaUsuarios[1].nombre)
    }

    @Test
    fun serializacionGson_desdeObjetoDto_usaClavesSerializedNameEnIngles() {
        val dto = UsuarioApiDto(
            id = 10,
            nombre = "Clementina DuBuque",
            email = "Rey.Padberg@karina.biz",
            telefono = "024-648-3804",
            sitioWeb = "ambrose.net"
        )

        val jsonGenerado = gson.toJson(dto)

        assertTrue(jsonGenerado.contains("\"id\":10"))
        assertTrue(jsonGenerado.contains("\"name\":\"Clementina DuBuque\""))
        assertTrue(jsonGenerado.contains("\"email\":\"Rey.Padberg@karina.biz\""))
        assertTrue(jsonGenerado.contains("\"phone\":\"024-648-3804\""))
        assertTrue(jsonGenerado.contains("\"website\":\"ambrose.net\""))
    }
}