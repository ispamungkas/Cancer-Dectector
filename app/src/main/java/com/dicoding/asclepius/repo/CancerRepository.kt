package com.dicoding.asclepius.repo

import com.dicoding.asclepius.datasouce.network.ApIConfiguration
import com.dicoding.asclepius.datasouce.room.HistoryDao
import com.dicoding.asclepius.model.InspectionModel
import com.dicoding.asclepius.model.ResponseHealty
import retrofit2.Response

class CancerRepository(
    val service: HistoryDao?
) {

    suspend fun insertDataHistoryDao(inspectionModel: InspectionModel) = service?.service()?.insertHistory(inspectionModel)

    fun getAllHistory() = service?.service()?.queryAllHistory()

    suspend fun getArticle(): Response<ResponseHealty> {
        return ApIConfiguration.service().getArticle()
    }

}