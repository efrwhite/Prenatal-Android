package com.elizabethwhitebaker.prenatal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import com.elizabethwhitebaker.prenatal.session.Session
import com.elizabethwhitebaker.prenatal.ui.theme.PrenatalTheme
import com.elizabethwhitebaker.prenatal.views.SessionView
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SingleSessionDemo() }
    }
}

@Composable
fun SingleSessionDemo() {
    PrenatalTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            //val navController = rememberNavController()

            val resources = LocalResources.current
            val articleStream = resources.openRawResource(R.raw.session_1)
            val articleJSON = articleStream.bufferedReader().use { it.readText() }
            var session: Session? = null

            try {
                val json = Json { ignoreUnknownKeys = true }
                session = json.decodeFromString<Session>(articleJSON)
                print("a")
            } catch(ex: Exception) {
                ex.printStackTrace()
            }

            if (session != null) SessionView(session, Modifier.padding(innerPadding))
            // Not a perfect padding... but we shouldn't be seeing this
            else Text("Failed to load article!", Modifier.padding(innerPadding))
        }
    }
}