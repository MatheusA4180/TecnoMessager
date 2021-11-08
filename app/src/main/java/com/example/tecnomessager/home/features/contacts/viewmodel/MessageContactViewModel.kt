package com.example.tecnomessager.home.features.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.Message
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.data.model.MessageSend
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.home.features.contacts.repository.ContactsRepository

class MessageContactViewModel(private val repository: ContactsRepository): ViewModel() {

    fun requestMessagesByUser(userReceiver:String): LiveData<List<Message>> {
        return repository.requestMessages(userReceiver)
    }

    fun sendMenssage(message:Message): LiveData<Resource<Boolean>> {
        return repository.sendMessage(message)
    }

}