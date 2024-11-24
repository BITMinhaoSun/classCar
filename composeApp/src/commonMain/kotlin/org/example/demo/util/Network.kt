package org.example.demo.util

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest {
        url("http://0.0.0.0:8080")
    }
}