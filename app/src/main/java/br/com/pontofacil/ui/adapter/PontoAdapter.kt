package br.com.pontofacil.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pontofacil.data.model.Ponto
import br.com.pontofacil.databinding.ItemPontoBinding

class PontoAdapter : ListAdapter<Ponto, PontoAdapter.PontoViewHolder>(PontoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontoViewHolder {
        val binding = ItemPontoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PontoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PontoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PontoViewHolder(private val binding: ItemPontoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ponto: Ponto) {
            binding.tvTipoPonto.text = ponto.tipo
            binding.tvDataHora.text = "${ponto.data} às ${ponto.hora}"
            binding.tvLocalizacao.text = "Lat: ${ponto.latitude}, Lon: ${ponto.longitude}"
            
            if (ponto.caminhoFoto.isNotEmpty()) {
                binding.ivFotoPonto.setImageURI(Uri.parse(ponto.caminhoFoto))
            }
        }
    }

    class PontoDiffCallback : DiffUtil.ItemCallback<Ponto>() {
        override fun areItemsTheSame(oldItem: Ponto, newItem: Ponto): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Ponto, newItem: Ponto): Boolean = oldItem == newItem
    }
}
