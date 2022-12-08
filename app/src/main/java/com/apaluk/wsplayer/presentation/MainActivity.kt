package com.apaluk.wsplayer.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.apaluk.wsplayer.core.hashing.Md5Crypt
import com.apaluk.wsplayer.core.utils.md5Crypt
import com.apaluk.wsplayer.core.utils.sha1
import com.apaluk.wsplayer.data.remote.WebShareApi
import com.apaluk.wsplayer.data.remote.dto.SaltDto
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme
import com.google.common.hash.Hashing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WsPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            val username = ""
            val password = ""

            val retrofit = Retrofit.Builder()
                .baseUrl("https://webshare.cz")
                .build()
                .create(WebShareApi::class.java)

            val saltResponse = retrofit.salt(username).body()?.string() ?: ""
            val salt = Persister().read(SaltDto::class.java, saltResponse).salt

            Log.d("TAG", "xxx salt=$salt")
            val md5 = password.md5Crypt(salt = salt)
            Log.d("TAG", "xxx md5=${md5}")
            val sha1 = md5.sha1()
            Log.d("TAG", "xxx sha1=$sha1")

            val response = retrofit.login(
                    username,
                    sha1,
                    1
                )

            Log.d("TAG", "xxx code=${response.code()} body=${response.body()?.string()} errorBody=${response.errorBody()}")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WsPlayerTheme {
        Greeting("Android")
    }
}