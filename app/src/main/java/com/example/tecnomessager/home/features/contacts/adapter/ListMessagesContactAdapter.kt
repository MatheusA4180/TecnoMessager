package com.example.tecnomessager.home.features.contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.MessageReceiver
import com.squareup.picasso.Picasso

class ListMessagesContactAdapter(
    private val listMessagesContact: List<MessageReceiver>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = listMessagesContact.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_send, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostViewHolder) {
            holder.bind(listMessagesContact[position])
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val messageText: TextView = itemView.findViewById(R.id.textview_msg_sent)
        private val iconMessage: ImageView = itemView.findViewById(R.id.imageViewSent)
        private val hourMessage: TextView = itemView.findViewById(R.id.hour_message_send)

        fun bind(message: MessageReceiver) {
            //Picasso.get().load(message.imageUserReceiver).into(imageContact)
            if(!message.contentMessage.isNullOrEmpty()){
                messageText.text = message.contentMessage
            }
            hourMessage.text = message.hour
        }

    }

}
