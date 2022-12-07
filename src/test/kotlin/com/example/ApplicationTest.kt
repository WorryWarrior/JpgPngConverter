package com.example

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplicationTest {

    @Test
    fun testNotFound() = testApplication {
        application {
            configureRouting()
        }

        val response = client.get("/convert-not-found")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testNotAllowed() = testApplication {
        application {
            configureRouting()
        }

        val response = client.get("/convert")
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }
}