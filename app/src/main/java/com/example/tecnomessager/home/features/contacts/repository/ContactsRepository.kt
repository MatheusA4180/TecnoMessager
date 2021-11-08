package com.example.tecnomessager.home.features.contacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tecnomessager.data.local.SessionManager
import com.example.tecnomessager.data.model.Message
import com.example.tecnomessager.data.model.MessageReceiver
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
            var listContacts: MutableList<HashMap<String, String>> = mutableListOf()
            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<HashMap<String, String>>
                    }
                    if (listContacts.none { ElementListContact -> ElementListContact["nameContact"] == contact }) {
                        val idChatMessages = "${UUID.randomUUID()}"
                        firebaseFirestore.collection(idChatMessages)
                            .add(mapOf("idChat" to idChatMessages)).addOnSuccessListener {
                                listContacts.add(
                                    hashMapOf(
                                        "nameContact" to contact,
                                        "idChatMessages" to idChatMessages
                                    )
                                )
                                addContactToFirebase(listContacts)
                                duplicateContactToFirebase(contact, idChatMessages)
                            }
                            .addOnFailureListener {
                                value = Resource(false, "Falha ao salvar o contato")
                            }
                    } else {
                        value = Resource(false, "Esse contato já existe")
                    }
                }.addOnFailureListener {
                    value = Resource(false, "Falha ao salvar o contato")
                }
        }

    private fun MutableLiveData<Resource<Boolean>>.addContactToFirebase(
        listContacts: MutableList<HashMap<String, String>>
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

    private fun MutableLiveData<Resource<Boolean>>.duplicateContactToFirebase(
        contact: String,
        idChatMessages: String
    ) {
        var listContacts: MutableList<UserApp.Contact> = mutableListOf()
        firebaseFirestore.collection(USERS).document(contact)
            .get()
            .addOnSuccessListener {
                it.get("contacts")?.let { contact ->
                    listContacts = contact as MutableList<UserApp.Contact>
                }
                listContacts.add(
                    UserApp.Contact(
                        nameContact = sessionManager.getSavedEmailUser(),
                        idChatMessages = idChatMessages
                    )
                )
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

    fun requestContacts(): LiveData<List<MessageReceiver>> =
        MutableLiveData<List<MessageReceiver>>().apply {
            var listContacts: MutableList<HashMap<String, String>> = mutableListOf()
            val listContactsWithData: MutableList<UserApp> = mutableListOf()

            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<HashMap<String, String>>
                    }
                    listContacts.forEach { contact ->
                        firebaseFirestore.collection(USERS).document(contact["nameContact"]!!).get()
                            .addOnSuccessListener { snapshot ->
                                try {
                                    listContactsWithData.add(snapshot.toObject<UserApp>()!!)
                                } catch (e: Exception) {
                                }
                                val listMessagesByContacts: MutableList<MessageReceiver> =
                                    mutableListOf()
                                listContactsWithData.forEach {
                                    listMessagesByContacts.add(
                                        MessageReceiver(
                                            emailUserReceiver = it.email,
                                            userReceiver = it.nameProfile,
                                            imageUserReceiver = it.imageProfile
                                        )
                                    )
                                }
                                value = listMessagesByContacts
                            }
                            .addOnFailureListener { }
                    }
                }
        }

    fun requestMessagesByUser(userReceiver: String): LiveData<List<Message>> =
        MutableLiveData<List<Message>>().apply {
            var listContacts: MutableList<HashMap<String, String>> = mutableListOf()
            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<HashMap<String, String>>
                    }
                    listContacts.forEach { contact ->
                        try {
                            if (contact["nameContact"] == userReceiver) {
                                var idChatMessages = ""
                                idChatMessages = contact["idChatMessages"].toString()
                                firebaseFirestore.collection(idChatMessages)
                                    .addSnapshotListener { valueSnapshot, _ ->
                                        valueSnapshot?.let { snapshot ->
                                            var userSend = sessionManager.getSavedEmailUser()
                                            var listMessages: MutableList<Message> = mutableListOf()
                                            listMessages = snapshot.documents
                                                .mapNotNull { documento ->
                                                    documento.toObject<Message>()
                                                }
                                                .filter { message -> !(message.contentMessage.isNullOrEmpty()) }
                                                .toMutableList()
                                            listMessages.forEach { messageFiltered ->
                                                if (messageFiltered.userSend == userSend) {
                                                    messageFiltered.userSend = "1"
                                                } else {
                                                    messageFiltered.userSend = "2"
                                                }
                                            }
                                            value = listMessages
                                        }
                                    }
                            }
                        } catch (e: Exception) {
                        }
                    }
                }.addOnFailureListener { }
        }

//    fun requestMessagesByUser(userReceiver: String): LiveData<List<MessageSend>> =
//        MutableLiveData<List<MessageSend>>().apply {
//            firebaseFirestore.collection(sessionManager.getSavedUidUser())
//                .addSnapshotListener { valueSnapshot, _ ->
//                    valueSnapshot?.let { snapshot ->
//                        val listMessages: List<MessageSend> = snapshot.documents
//                            .mapNotNull { documento ->
//                                documento.toObject<MessageSend>()
//                            }
//                        value = listMessages.filter { it.userReceiver == userReceiver }
//                    }
//                }
//        }

    fun sendMessage(message: Message): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            var listContacts: MutableList<HashMap<String, String>> = mutableListOf()
            firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
                .get()
                .addOnSuccessListener {
                    it.get("contacts")?.let { contact ->
                        listContacts = contact as MutableList<HashMap<String, String>>
                    }
                    listContacts.forEach { contact ->
                        try {
                            if (contact["nameContact"] == message.userReceiver) {
                                var idChatMessages = ""
                                idChatMessages = contact["idChatMessages"].toString()
                                val messageTreatmented = Message(
                                    date = message.date,
                                    hour = message.hour,
                                    contentMessage = message.contentMessage,
                                    file = message.file,
                                    userSend = sessionManager.getSavedEmailUser(),
                                    userReceiver = message.userReceiver,
                                )
                                firebaseFirestore.collection(idChatMessages)
                                    .document(MESSAGE + "${UUID.randomUUID()}")
                                    .set(messageTreatmented)
                                    .addOnSuccessListener {
                                        value = Resource(
                                            true,
                                            "Mensagem enviada com sucesso"
                                        )
                                    }
                                    .addOnFailureListener {
                                        value = Resource(
                                            false,
                                            "Falha ao enviar a mensagem"
                                        )
                                    }
                            }
                        } catch (e: Exception) {
                        }
                    }
                }.addOnFailureListener { }
        }


//    fun removeMessage(message: MessageSend): LiveData<Resource<Boolean>> =
//        MutableLiveData<Resource<Boolean>>().apply {
//            firebaseFirestore.collection(sessionManager.getSavedUidUser()).document(MESSAGE)
//                .delete()
//                .addOnSuccessListener {
//                    value = Resource(true, "Mensagem removida com sucesso")
//                }
//                .addOnFailureListener {
//                    value = Resource(false, "Falha ao remover a mensagem")
//                }
//        }


    companion object {
        const val MESSAGE = "message"
    }

}
