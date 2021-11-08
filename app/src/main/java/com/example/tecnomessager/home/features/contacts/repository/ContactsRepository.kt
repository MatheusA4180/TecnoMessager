package com.example.tecnomessager.home.features.contacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tecnomessager.data.local.SessionManager
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.data.model.MessageSend
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.UserApp
import com.example.tecnomessager.intro.repository.IntroRepository.Companion.USERS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ContactsRepository(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) {

    fun addContacts(contact: String): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            var listContacts: MutableList<String> = mutableListOf()
            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<String>
                    }
                    if (listContacts.none { ElementListContact -> ElementListContact == contact }) {
                        listContacts.add(contact)
                        addContactToFirebase(listContacts)
                        duplicateContactToFirebase(contact)
                    } else {
                        value = Resource(false, "Esse contato já existe")
                    }
                }.addOnFailureListener {
                    value = Resource(false, "Falha ao salvar o contato")
                }
        }

    private fun MutableLiveData<Resource<Boolean>>.addContactToFirebase(
        listContacts: MutableList<String>
    ) {
        firebaseFirestore.collection(USERS)
            .document(sessionManager.getSavedEmailUser())
            .update("contacts", listContacts)
            .addOnSuccessListener {
                value = Resource(true, "contato salvo com sucesso")
            }
            .addOnFailureListener {
                value = Resource(false, "Falha ao salvar o contato")
            }
    }

    private fun MutableLiveData<Resource<Boolean>>.duplicateContactToFirebase(contact: String) {
        var listContacts: MutableList<String> = mutableListOf()
        firebaseFirestore.collection(USERS).document(contact)
            .get()
            .addOnSuccessListener {
                it.get("contacts")?.let { contact ->
                    listContacts = contact as MutableList<String>
                }
                listContacts.add(sessionManager.getSavedEmailUser())
                firebaseFirestore.collection(USERS)
                    .document(contact)
                    .update("contacts", listContacts)
                    .addOnSuccessListener {
                        value = Resource(true, "contato salvo com sucesso")
                    }
                    .addOnFailureListener {
                        value = Resource(false, "Falha ao salvar o contato")
                    }
            }.addOnFailureListener {
                value = Resource(false, "Falha ao salvar o contato")
            }
    }

    fun requestContacts(): LiveData<List<UserApp>> =
        MutableLiveData<List<UserApp>>().apply {
            var listContacts: MutableList<String> = mutableListOf()
            val listContactsWithData: MutableList<UserApp> = mutableListOf()

            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<String>
                    }
                    listContacts.forEach { contact ->
                        firebaseFirestore.collection(USERS).document(contact).get()
                            .addOnSuccessListener { snapshot ->
                                try {
                                    listContactsWithData.add(snapshot.toObject<UserApp>()!!)
                                } catch (e: Exception) {
                                }
                                value = listContactsWithData
                            }
                            .addOnFailureListener { }
                    }
                }
        }

    fun requestMessages(): LiveData<List<MessageReceiver>> =
        MutableLiveData<List<MessageReceiver>>().apply {
            firebaseFirestore.collection(sessionManager.getSavedUidUser())
                .addSnapshotListener { valueSnapshot, _ ->
                    valueSnapshot?.let { snapshot ->
                        val listMessages: List<MessageSend> = snapshot.documents
                            .mapNotNull { documento ->
                                documento.toObject<MessageSend>()
                            }
                        value = listMessages.map { message ->
                            MessageReceiver(
                                date = message.date,
                                hour = message.hour,
                                contentMessage = message.contentMessage,
                                file = message.file,
                                userSend = message.userSend,
                                userReceiver = message.userReceiver,
                                imageUserReceiver = ""
                            )
                        }
                    }
                }
        }

    fun requestMessagesByUser(userReceiver: String): LiveData<List<MessageSend>> =
        MutableLiveData<List<MessageSend>>().apply {
            firebaseFirestore.collection(sessionManager.getSavedUidUser())
                .addSnapshotListener { valueSnapshot, _ ->
                    valueSnapshot?.let { snapshot ->
                        val listMessages: List<MessageSend> = snapshot.documents
                            .mapNotNull { documento ->
                                documento.toObject<MessageSend>()
                            }
                        value = listMessages.filter { it.userReceiver == userReceiver }
                    }
                }
        }

    fun sendMessage(message: MessageSend): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            val messageTreatmented = MessageSend(
                date = message.date,
                hour = message.hour,
                contentMessage = message.contentMessage,
                file = message.file,
                userSend = sessionManager.getSavedUidUser(),
                userReceiver = message.userReceiver,
            )
            firebaseFirestore.collection(sessionManager.getSavedUidUser())
                .document(MESSAGE + "${UUID.randomUUID()}")
                .set(messageTreatmented)
                .addOnSuccessListener {
                    value = Resource(true, "Mensagem enviada com sucesso")
                }
                .addOnFailureListener {
                    value = Resource(false, "Falha ao enviar a mensagem")
                }
        }

    fun removeMessage(message: MessageSend): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            firebaseFirestore.collection(sessionManager.getSavedUidUser()).document(MESSAGE)
                .delete()
                .addOnSuccessListener {
                    value = Resource(true, "Mensagem removida com sucesso")
                }
                .addOnFailureListener {
                    value = Resource(false, "Falha ao remover a mensagem")
                }
        }

    companion object {
        const val MESSAGE = "message"
    }

}
