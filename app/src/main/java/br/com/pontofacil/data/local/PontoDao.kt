package br.com.pontofacil.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.pontofacil.data.model.Ponto
import kotlinx.coroutines.flow.Flow

@Dao
interface PontoDao {
    @Insert
    suspend fun inserirPonto(ponto: Ponto): Long

    @Query("SELECT * FROM pontos ORDER BY id DESC")
    fun listarTodos(): Flow<List<Ponto>>
}
