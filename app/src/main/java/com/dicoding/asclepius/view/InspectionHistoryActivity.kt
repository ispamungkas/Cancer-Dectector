package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityInspectionHistoryBinding
import com.dicoding.asclepius.model.InspectionModel
import com.dicoding.asclepius.utils.Injection
import com.dicoding.asclepius.utils.ViewModelFactory
import com.dicoding.asclepius.view.adapter.HistoryAdapter

class InspectionHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInspectionHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val viewmodel : MainViewModel by viewModels {
        ViewModelFactory(Injection.cancerRespository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspectionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryAdapter()
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@InspectionHistoryActivity)
            setHasFixedSize(true)
        }

        viewmodel.getAllHistory()?.observe(this@InspectionHistoryActivity) {
            println(it)
            setUpAdapter(it)
        }
    }

    private fun setUpAdapter(listHistory: List<InspectionModel>) {
        adapter.setValue(listHistory)
        binding.rvList.adapter = adapter
    }
}