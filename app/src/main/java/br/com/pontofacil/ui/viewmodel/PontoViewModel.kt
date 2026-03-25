package br.com.pontofacil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.pontofacil.data.model.Ponto
import br.com.pontofacil.data.repository.PontoRepository
import kotlinx.coroutines.launch

class PontoViewModel(private val repository: PontoRepository) : ViewModel() {

    // Expondo a lista de pontos como LiveData para a Activity observar
    val todosOsPontos = repository.todosOsPontos.asLiveData()

    fun salvarPonto(ponto: Ponto) {
        viewModelScope.launch {
            repository.salvarPonto(ponto)
        }
    }
}

class PontoViewModelFactory(private val repository: PontoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PontoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PontoViewModel(repository) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
