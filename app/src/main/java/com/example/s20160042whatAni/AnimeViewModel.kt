package com.example.s20160042whatAni

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimeViewModel: ViewModel() { // ()가 warning 떠서 삭제
    // SERVER_URL 이라고 했을 시 underscore warning 떠 serverURL 로 변수명 변경
    private val serverURL: String = "https://port-0-backend-3yl7k2blonrx0wi.sel5.cloudtype.app/"
    private val animeApi: AnimeApi
    private val _animeList = MutableLiveData<List<Anime>>()
    val animeList: LiveData<List<Anime>>
        get() = _animeList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        animeApi = retrofit.create(AnimeApi::class.java)
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val response = animeApi.getAnimes()
                _animeList.value = response
            } catch (e: Exception) {
                Log.e("fetchData()", e.toString())
            }
        }
    }
}