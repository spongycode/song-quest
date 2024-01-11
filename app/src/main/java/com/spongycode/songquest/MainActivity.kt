package com.spongycode.songquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spongycode.songquest.navigation.NavContainer
import com.spongycode.songquest.ui.theme.SongQuestTheme
import com.spongycode.songquest.util.datastore
import com.spongycode.songquest.util.getAccessToken
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SongQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (runBlocking {
                            val accessToken = applicationContext.datastore.getAccessToken()
                            accessToken.isEmpty()
                        }) {
                        NavContainer(startDestination = "register")
                    } else {
                        NavContainer(startDestination = "home")
                    }
                }
            }
        }
    }
}

@OptIn(InternalAPI::class)
fun main () {
    val BASE_URL = "https://songquest.vercel.app/"
    val client = HttpClient {
        install(ContentNegotiation) {
            json()

        }
    }

    @Serializable
    data class Register(
        val fullName: String,
        val email: String,
        val username: String,
        val password: String
    )


    runBlocking {
        val res = client.post {
            url("${BASE_URL}api/mobile/users/register")
            contentType(ContentType.Application.Json)
            setBody(
                Register(
                    "Shake Kumar", "20je0039@ese.iitism.ac.in",
                    "khdfkyjgbcode", "dsf"
                )
            )
        }
        val body = res.body<String>()
        println(body)
    }
}
