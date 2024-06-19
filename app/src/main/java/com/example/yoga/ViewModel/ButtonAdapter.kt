package com.example.yoga.ViewModel

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.yoga.R

class ButtonAdapter(
    private val context: Context,
    private val poseNames: Array<String>,
    private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.buttonItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.button_item, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val poseName = poseNames[position]
        holder.button.text = poseName
        holder.button.setBackgroundColor(Color.BLUE)
        holder.button.setOnClickListener {
            onButtonClick(poseName)
        }
    }

    override fun getItemCount(): Int {
        return poseNames.size
    }
}