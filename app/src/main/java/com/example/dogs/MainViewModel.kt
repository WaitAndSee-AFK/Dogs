package com.example.dogs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

private const val BASE_URL = "https://dog.ceo/api/breeds/image/random"
private const val KEY_MESSAGE = "message"
private const val KEY_STATUS = "status"
private const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    private val _dogImage: MutableLiveData<DogImage> = MutableLiveData<DogImage>()
    val dogImage: LiveData<DogImage>
        get() = _dogImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun loadDogImage() {
        val disposable = loadDogImageRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _isLoading.value = true
                _isError.value = false
            }
            .doAfterTerminate { _isLoading.value = false }
            .doOnError { _isError.value = true }
            .subscribe({ image ->
                _dogImage.value = image
            }, { err ->
                Log.d(TAG, err.toString())
            })
        compositeDisposable.add(disposable)
    }

    private fun loadDogImageRx(): Single<DogImage> {
        return ApiFactory.getApiService().loadDogImage()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}