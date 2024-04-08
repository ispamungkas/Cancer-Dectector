package com.dicoding.asclepius.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.databinding.ItemHistoryInspectionBinding
import com.dicoding.asclepius.model.InspectionModel

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    inner class HistoryHolder(val binding: ItemHistoryInspectionBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val ayncList = AsyncListDiffer(this, diff)

    fun setValue(listInspection: List<InspectionModel>) {
        ayncList.submitList(listInspection)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryHolder {
        val view =
            ItemHistoryInspectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryHolder, position: Int) {
        val data = ayncList.currentList[position]
        val image = Uri.parse(data.imageUri)
        data.apply {
            holder.binding.tvDate.text = this.timesTime
            holder.binding.tvScore.text = this.confidenceScore
            holder.binding.tvSuspect.text = this.name
            holder.binding.inspectionImage.setImageURI(image)
        }
    }

    override fun getItemCount() = ayncList.currentList.size

    companion object {
        val diff = object : DiffUtil.ItemCallback<InspectionModel>() {
            override fun areItemsTheSame(
                oldItem: InspectionModel,
                newItem: InspectionModel
            ): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(
                oldItem: InspectionModel,
                newItem: InspectionModel
            ): Boolean {
                return newItem.imageUri == oldItem.imageUri
            }

        }
    }
}