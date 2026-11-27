package com.example.corpaliv.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun retrofitClient_apiService_retornaMismaInstanciaSingleton() {
        val instancia1 = RetrofitClient.apiService
        val instancia2 = RetrofitClient.apiService

        assertNotNull(instancia1)
        assertSame(instancia1, instancia2)
    }

    @Test
    fun obtenerUsuarios_respuestaHttp200_enviaGetAUsersYMapeaListaDto() = runTest {
        val jsonSimulado = """
            [
                {
                    "id": 1,
                    "name": "Leanne Graham",
                    "email": "Sincere@april.biz",
                    "phone": "1-770-736-8031 x56442",
                    "website": "hildegard.org"
                },
                {
                    "id": 2,
                    "name": "Ervin Howell",
                    "email": "Shanna@melissa.tv",
                    "phone": "010-692-6593 x09125",
                    "website": "anastasia.net"
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonSimulado)
        )

        val usuarios = apiService.obtenerUsuarios()

        // Verificar que la petición HTTP real se hizo con método GET y ruta /users
        val requestRegistrada = mockWebServer.takeRequest()
        assertEquals("GET", requestRegistrada.method)
        assertEquals("/users", requestRegistrada.path)

        // Verificar los datos deserializados
        assertEquals(2, usuarios.size)
        assertEquals("Leanne Graham", usuarios[0].nombre)
        assertEquals("Ervin Howell", usuarios[1].nombre)
    }

    @Test
    fun obtenerUsuarios_respuestaHttp404_lanzaHttpException() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("{\"error\": \"Not Found\"}")
        )

        val excepcion = assertThrows(HttpException::class.java) {
            kotlinx.coroutines.runBlocking {
                apiService.obtenerUsuarios()
            }
        }

        assertEquals(404, excepcion.code())
    }
}