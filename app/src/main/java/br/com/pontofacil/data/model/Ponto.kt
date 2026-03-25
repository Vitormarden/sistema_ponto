package br.com.pontofacil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pontos")
data class Ponto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val data: String,
    val hora: String,
    val caminhoFoto: String,
    val latitude: Double,
    val longitude: Double,
    val tipo: String // Entrada, Intervalo, Saída
)
