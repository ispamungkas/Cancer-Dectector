package com.dicoding.asclepius.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.model.InspectionModel
import com.dicoding.asclepius.model.ResponseHealty
import com.dicoding.asclepius.repo.CancerRepository
import com.dicoding.asclepius.utils.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val repo: CancerRepository
) : ViewModel() {

    private val _allArticle: MutableLiveData<Resources<ResponseHealty>> = MutableLiveData()
    val allArticle = _allArticle

    init {
        setAllArticle()
    }

    fun getAllHistory() = repo.getAllHistory()

    fun insertHistory(inspectionModel: InspectionModel) = viewModelScope.launch {
        repo.insertDataHistoryDao(inspectionModel)
    }

    fun setAllArticle() = viewModelScope.launch {
        _allArticle.postValue(Resources.Loading())
        try {
            val response = repo.getArticle()
            _allArticle.postValue(handlerArticleResponse(response))
        } catch (e: Exception) {
            _allArticle.postValue(Resources.Failed(e.message.toString()))
        }
    }

    private fun handlerArticleResponse(response: Response<ResponseHealty>): Resources<ResponseHealty> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resources.Success(it)
            }
        } else {
            return Resources.Failed("Response Unsuccessfully")
        }

        return Resources.Failed("Handler failed")
    }


}