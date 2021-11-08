package com.example.tecnomessager.home.features.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.Message
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.UserApp
import com.example.tecnomessager.home.features.contacts.repository.ContactsRepository

class ListContactsViewModel(private val repository: ContactsRepository) : ViewModel() {

    fun addContact(contact: String): LiveData<Resource<Boolean>> {
        val validResponse = validFields(contact)
        return if (validResponse.value!!.dado) {
            repository.addContacts(contact)
        } else {
            validResponse
        }
    }

    fun requestContacts(): LiveData<List<UserApp>> {
        return repository.requestContacts()
    }

    private fun validFields(contact: String): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            value = if (contact.isEmpty()) {
                Resource(false, "Email do contato não pode ser vazio")
            }else{
                Resource(true)
            }
        }

}