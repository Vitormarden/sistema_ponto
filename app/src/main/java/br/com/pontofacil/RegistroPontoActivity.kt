package br.com.pontofacil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import br.com.pontofacil.data.local.AppDatabase
import br.com.pontofacil.data.model.Ponto
import br.com.pontofacil.data.repository.PontoRepository
import br.com.pontofacil.databinding.ActivityRegistroPontoBinding
import br.com.pontofacil.ui.viewmodel.PontoViewModel
import br.com.pontofacil.ui.viewmodel.PontoViewModelFactory
import br.com.pontofacil.util.ImageHelper
import br.com.pontofacil.util.LocationHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RegistroPontoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroPontoBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var locationHelper: LocationHelper

    private val viewModel: PontoViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = PontoRepository(database.pontoDao())
        PontoViewModelFactory(repository)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true &&
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            startCamera()
        } else {
            Toast.makeText(this, "Permissões necessárias para bater o ponto.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroPontoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHelper = LocationHelper(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        checkPermissions()

        binding.btnBaterPonto.setOnClickListener { baterPonto() }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Erro ao iniciar câmera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun baterPonto() {
        val imageCapture = imageCapture ?: return
        binding.loading.visibility = View.VISIBLE
        binding.btnBaterPonto.isEnabled = false

        // 1. Capturar Localização
        locationHelper.getCurrentLocation { location ->
            if (location == null) {
                runOnUiThread {
                    Toast.makeText(this, "Erro ao capturar GPS. Verifique se está ligado.", Toast.LENGTH_LONG).show()
                    binding.loading.visibility = View.GONE
                    binding.btnBaterPonto.isEnabled = true
                }
                return@getCurrentLocation
            }

            // 2. Tirar Foto
            val photoFile = ImageHelper.createTempImageFile(this)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: photoFile.toURI()
                    
                    // 3. Salvar no Banco de Dados
                    val tipoPonto = when (binding.rgTipoPonto.checkedRadioButtonId) {
                        binding.rbEntrada.id -> "Entrada"
                        binding.rbIntervalo.id -> "Intervalo"
                        else -> "Saída"
                    }

                    val ponto = Ponto(
                        data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                        hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
                        caminhoFoto = savedUri.toString(),
                        latitude = location.latitude,
                        longitude = location.longitude,
                        tipo = tipoPonto
                    )

                    viewModel.salvarPonto(ponto)
                    Toast.makeText(baseContext, "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(baseContext, "Erro ao capturar foto", Toast.LENGTH_SHORT).show()
                    binding.loading.visibility = View.GONE
                    binding.btnBaterPonto.isEnabled = true
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
