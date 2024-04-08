package com.dicoding.asclepius.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            contentResolver = contentResolver,
            imageClassifierResult = object : ImageClassifierHelper.ImageClassifierResult {
                override fun onError(message: String) {
                    showToast(message)
                }

                override fun onSuccess(result: List<Classifications>?, inference: Long) {
                    result?.let {
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            binding.progressIndicator.visibility = View.GONE

                            val sortedDatas = it[0].categories.sortedBy { item -> item.score }
                            val resultLabel = sortedDatas[0].label
                            val score =
                                NumberFormat.getPercentInstance().format(sortedDatas[0].score)
                            moveToResult(resultLabel, score)

                        }
                    }
                }

            }
        )

        binding.galleryButton.setOnClickListener { startGallery() }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        binding.containerHistory.setOnClickListener {
            moveToHistory()
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val showGallery = Intent(Intent.ACTION_VIEW)
        showGallery.setType("image/*")
        resultGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val resultGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            it.let { dataUri ->
                val name = Random.nextInt(1, 1000)
                val list = arrayListOf(dataUri, File(filesDir, "${name}_ae.jpg").toUri())
                resultUCropContract.launch(list)
            }
        } else {
            Log.w(MAINACTIVITYLOG, "Result Null")
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private val cropContract = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val initial = input[0]
            val output = input[1]

            val ucrop = UCrop.of(initial, output)
                .withAspectRatio(8f, 8f)
                .withMaxResultSize(800, 800)

            return ucrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }

    }

    private val resultUCropContract = registerForActivityResult(cropContract) {
        currentImageUri = it
        showImage()
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        binding.progressIndicator.visibility = View.VISIBLE
        currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(it)
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun moveToResult(label: String, score: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.FORURIINTENT, currentImageUri.toString())
        intent.putExtra(ResultActivity.FORLABELINTENT, label)
        intent.putExtra(ResultActivity.FORSCOREINTENT, score)
        startActivity(intent)
    }

    private fun moveToHistory() {
        val intent = Intent(this, InspectionHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val MAINACTIVITYLOG = "MAINLOG"
    }
}