package com.example.tecnomessager.home.features.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.data.model.UserApp
import com.example.tecnomessager.home.features.contacts.repository.ContactsRepository

class ListContactsViewModel(private val repository: ContactsRepository):ViewModel() {

//    fun requestMessagesByContacts(): LiveData<List<MessageReceiver>>{
//        val listContacts = requestContacts().value
//        val listMessages = requestMessages().value
//        return MutableLiveData<List<MessageReceiver>>().apply {
//            val listMessagesByContacts: MutableList<MessageReceiver> = mutableListOf()
//            listContacts?.forEach {
//                listMessagesByContacts.add(MessageReceiver(userReceiver = it.uidUser))
//            }
//            listContacts?.forEach { contact ->
//                listMessages?.forEach { message ->
//                    if(contact.uidUser == message.userReceiver){
//                        listMessagesByContacts.add(message)
//                    }
//                }
//            }
//            value = listMessagesByContacts
//            val teste1 = 0
//        }
//    }

    fun requestContacts(): LiveData<List<UserApp>> {
        return repository.requestContacts()
    }

    fun requestMessages(): LiveData<List<MessageReceiver>> {
        return repository.requestMessages()
    }

}