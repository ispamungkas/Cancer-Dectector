package com.dicoding.asclepius.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    val context: Context,
    val treshold: Float = 0.1F,
    val maxResult: Int = 3,
    val contentResolver: ContentResolver,
    private val modelName: String = "cancer_classification.tflite",
    private val imageClassifierResult: ImageClassifierResult
) {

    private lateinit var imageClassifier: ImageClassifier
    private lateinit var bitmapImage : Bitmap

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(treshold)
            .setMaxResults(maxResult)
        val baseOption = BaseOptions.builder()
            .setNumThreads(4)
        optionBuilder.setBaseOptions(baseOption.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context, modelName, optionBuilder.build()
            )
        } catch (e: IllegalStateException) {
            imageClassifierResult.onError(e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if (imageClassifier != null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .build()

        uriToBitmap(imageUri)

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmapImage))
        var inference = SystemClock.uptimeMillis()
        val results = imageClassifier.classify(tensorImage)
        inference = SystemClock.uptimeMillis() - inference
        results?.let {
            imageClassifierResult.onSuccess(
                it,
                inference
            )
        }
    }

    private fun uriToBitmap(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                contentResolver,
                uri
            )
            bitmapImage = ImageDecoder.decodeBitmap(source)
            bitmapImage = bitmapImage.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(
                contentResolver,
                uri
            ).copy(Bitmap.Config.ARGB_8888, true)?.let {
                bitmapImage = it
            }
        }
    }

    interface ImageClassifierResult {
        fun onError(message: String)
        fun onSuccess(
            result: List<Classifications>?,
            inference: Long
        )
    }


}