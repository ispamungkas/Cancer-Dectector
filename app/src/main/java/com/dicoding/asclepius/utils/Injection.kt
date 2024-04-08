package com.dicoding.asclepius.utils

import android.content.Context
import com.dicoding.asclepius.datasouce.room.HistoryDao
import com.dicoding.asclepius.repo.CancerRepository

object Injection {

    fun cancerRespository(
        context: Context
    ): CancerRepository {
        val service = HistoryDao.getInstance(context)
        return CancerRepository(service)
    }

}