package br.com.pontofacil.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object ImageHelper {
    fun createTempImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile("PONTO_${timeStamp}_", ".jpg", storageDir)
    }

    fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, "PontoFacil").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }
}
