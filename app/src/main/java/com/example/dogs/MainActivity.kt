package com.example.dogs

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

private const val BASE_URL = "https://dog.ceo/api/breeds/image/random"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDogImage()
    }

    private fun loadDogImage() {
        thread {
            try {
                val url: URL = URL(BASE_URL)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.inputStream
                val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                val data = StringBuilder()
                var result: String
                do {
                    result = bufferedReader.readLine()
                    if (result != null) data.append(result)
                } while (result != null)

                val jsonObject = JSONObject(data.toString())
                val message = jsonObject.getString("message")
                val status = jsonObject.getString("status")
                val dogImage = DogImage(message, status)

                Log.d("MainActivity", dogImage.toString())
            } catch (e: Exception) {
                Log.d("MainActivity", e.toString())
            }
        }
    }
}