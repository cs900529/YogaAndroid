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
    val poseNames: Array<String>,
    private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    private val buttonReferences = mutableListOf<Button>()
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
        // 確保按鈕引用是唯一的
        if (!buttonReferences.contains(holder.button)) {
            buttonReferences.add(holder.button)
        }
    }

    override fun getItemCount(): Int {
        return poseNames.size
    }
    // Function 1: 根據索引獲取按鈕的參考
    fun getButtonByIndex(index: Int): Button {
        return buttonReferences[index]
    }
    // Function 2: 根據按鈕的參考獲取它的索引
    fun getIndexByButton(button: Button): Int {
        return buttonReferences.indexOf(button)
    }
}