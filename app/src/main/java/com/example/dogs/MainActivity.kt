package com.example.dogs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

private const val TAG = "MainActivity"
private lateinit var viewModel: MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.loadDogImage()
        viewModel.isError.observe(this, Observer<Boolean> { err ->
            if (err) {
                Toast.makeText(
                    this,
                    R.string.error_with_internet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.isLoading.observe(this, Observer<Boolean> { loading ->
            if (loading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
        viewModel.dogImage.observe(this, Observer<DogImage> { image ->
            Glide.with(this)
                .load(image.message)
                .into(imageView)
        })
        button.setOnClickListener {
            viewModel.loadDogImage()
        }
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        button = findViewById(R.id.button)
    }
}