package com.example.tecnomessager.home.features.contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.MessageReceiver

class ListContactsAdapter(
    private val listMessage: List<MessageReceiver>,
    private val contactClickListener: ContactClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = listMessage.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostViewHolder) {
            if (listMessage.isEmpty()) {
                holder.bind(MessageReceiver())
            } else {
                holder.bind(listMessage[position])
            }
            holder.itemView.setOnClickListener {
                if (listMessage.isEmpty()) {
                    contactClickListener.clickListener(
                        MessageReceiver(
                            userReceiver = "",
                            imageUserReceiver = ""
                        )
                    )
                } else {
                    contactClickListener.clickListener(listMessage[position])
                }
            }
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageContact: ImageView = itemView.findViewById(R.id.imageview_contact)
        private val nameContact: TextView = itemView.findViewById(R.id.textview_username)
        private val messageRecent: TextView = itemView.findViewById(R.id.message_recent)
        private val hourMessageRecent: TextView = itemView.findViewById(R.id.hour_message_recent)

        fun bind(message: MessageReceiver) {
            if (message.imageUserReceiver.isNullOrEmpty()) {
                imageContact.setImageResource(R.drawable.icon_app)
            }
//            else{
//                Picasso.get().load(message.imageUserReceiver).into(imageContact)
//            }
            nameContact.text = if (message.userReceiver.isNullOrEmpty()) {
                ""
            } else {
                message.userReceiver
            }
            messageRecent.text = if (message.contentMessage.isNullOrEmpty()) {
                ""
            } else {
                message.contentMessage
            }
            hourMessageRecent.text = if (message.hour.isNullOrEmpty()) {
                ""
            } else {
                message.hour
            }
        }

    }

    interface ContactClickListener {
        fun clickListener(message: MessageReceiver)
    }

}
