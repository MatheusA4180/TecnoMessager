package com.example.tecnomessager.home.features.contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.Message
import com.example.tecnomessager.data.model.MessageReceiver
import com.squareup.picasso.Picasso
import java.lang.Exception

class ListMessagesContactAdapter(
    private val listMessagesContact: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = listMessagesContact.size

    override fun getItemViewType(position: Int): Int {
        if (listMessagesContact[position].userSend == "1") {
            return MESSAGE_SEND
        } else if (listMessagesContact[position].userSend == "2") {
            return MESSAGE_RECEIVER
        }else{
            return 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == MESSAGE_SEND){
            return MessageSendViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_send, parent, false)
            )
        }else {
            return MessageReceiverViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_receiver, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageSendViewHolder) {
            holder.bind(listMessagesContact[position])
        }else if(holder is MessageReceiverViewHolder){
            holder.bind(listMessagesContact[position])
        }
    }

    class MessageSendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val messageText: TextView = itemView.findViewById(R.id.textview_msg_sent)
        private val iconMessage: ImageView = itemView.findViewById(R.id.imageViewSent)
        private val hourMessage: TextView = itemView.findViewById(R.id.hour_message_send)

        fun bind(message: Message) {
            //Picasso.get().load(message.imageUserReceiver).into(imageContact)
            if(!message.contentMessage.isNullOrEmpty()){
                messageText.text = message.contentMessage
            }
            hourMessage.text = message.hour
        }

    }

    class MessageReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val messageText: TextView = itemView.findViewById(R.id.textview_msg_receiver)
        private val iconMessage: ImageView = itemView.findViewById(R.id.imageViewReceiver)
        private val hourMessage: TextView = itemView.findViewById(R.id.hour_message_receiver)

        fun bind(message: Message) {
            //Picasso.get().load(message.imageUserReceiver).into(imageContact)
            if(!message.contentMessage.isNullOrEmpty()){
                messageText.text = message.contentMessage
            }
            hourMessage.text = message.hour
        }

    }

    companion object{
        const val MESSAGE_SEND = 1
        const val MESSAGE_RECEIVER = 2
    }

}
