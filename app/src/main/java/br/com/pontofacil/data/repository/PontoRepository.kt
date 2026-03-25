package br.com.pontofacil.data.repository

import br.com.pontofacil.data.local.PontoDao
import br.com.pontofacil.data.model.Ponto
import kotlinx.coroutines.flow.Flow

class PontoRepository(private val pontoDao: PontoDao) {
    val todosOsPontos: Flow<List<Ponto>> = pontoDao.listarTodos()

    suspend fun salvarPonto(ponto: Ponto) {
        pontoDao.inserirPonto(ponto)
    }
}
