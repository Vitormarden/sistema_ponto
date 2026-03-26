package br.com.pontofacil

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pontofacil.data.local.AppDatabase
import br.com.pontofacil.data.repository.PontoRepository
import br.com.pontofacil.databinding.ActivityMainBinding
import br.com.pontofacil.ui.adapter.PontoAdapter
import br.com.pontofacil.ui.viewmodel.PontoViewModel
import br.com.pontofacil.ui.viewmodel.PontoViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PontoViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = PontoRepository(database.pontoDao())
        PontoViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        
        binding.fabNovoPonto.setOnClickListener {
            startActivity(Intent(this, RegistroPontoActivity::class.java))
        }

        viewModel.todosOsPontos.observe(this) { pontos ->
            (binding.rvPontos.adapter as PontoAdapter).submitList(pontos)
        }
    }

    private fun setupRecyclerView() {
        binding.rvPontos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PontoAdapter()
        }
    }
}
