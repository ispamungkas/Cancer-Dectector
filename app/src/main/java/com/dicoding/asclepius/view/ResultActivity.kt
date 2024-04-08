package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.model.ArticlesItem
import com.dicoding.asclepius.model.InspectionModel
import com.dicoding.asclepius.utils.Injection
import com.dicoding.asclepius.utils.Resources
import com.dicoding.asclepius.utils.ViewModelFactory
import com.dicoding.asclepius.view.adapter.ArticleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(Injection.cancerRespository(applicationContext))
    }
    private lateinit var adapter: ArticleAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var uri: Uri? = null
    private var label: String? = null
    private var score: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ArticleAdapter()
        intent?.let {
            uri = Uri.parse(it.getStringExtra(FORURIINTENT).toString())
            label = it.getStringExtra(FORLABELINTENT).toString()
            score = it.getStringExtra(FORSCOREINTENT).toString()
        }

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        binding.resultImage.setImageURI(uri)
        binding.resultText.text = "Inspection result : $label $score"

        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        binding.rvArticle.setHasFixedSize(true)

        viewModel.allArticle.observe(this@ResultActivity) {
            when (it) {
                is Resources.Loading -> binding.progressIndicator.visibility = View.VISIBLE
                is Resources.Failed -> Toast.makeText(
                    this@ResultActivity,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

                is Resources.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    val data =
                        it.data?.articles?.filter { it?.author != null && it.title != null && it.description != null }
                    data?.let {
                        attachAdapterToRecyclerView(it)
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            coroutineScope.launch {
                saveAction()
            }
            Toast.makeText(this@ResultActivity, "Save Successful", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun saveAction() = coroutineScope {
        val data = InspectionModel(
            id = null,
            imageUri = uri.toString(),
            name = label!!,
            confidenceScore = score!!,
            timesTime = Date().toString()
        )
        println(data)
        viewModel.insertHistory(data)
    }

    private fun attachAdapterToRecyclerView(listArticle: List<ArticlesItem?>) {
        adapter.saveData(listArticle)
        binding.rvArticle.adapter = adapter
    }

    companion object {
        const val FORLABELINTENT = "label result"
        const val FORSCOREINTENT = "score result"
        const val FORURIINTENT = "uri"
        const val FORORIGINALSCORE = "original Score"
    }

}