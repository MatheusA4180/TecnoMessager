package com.example.tecnomessager.intro.repository

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tecnomessager.data.local.SessionManager
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.User
import com.example.tecnomessager.data.model.UserApp
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class IntroRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) {

    fun auth(user: User): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            try {
                firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sessionManager.saveUidUser(firebaseAuth.uid.toString())
                            sessionManager.saveEmailUser(user.email)
                            value = Resource(true, firebaseAuth.uid.toString())
                        } else {
                            val messageError: String = returnErrorForAuth(task.exception)
                            value = Resource(false, messageError)
                        }
                    }
            } catch (e: IllegalArgumentException) {
                value = Resource(false, "E-mail ou senha não pode ser vazio")
            }
        }

    private fun returnErrorForAuth(exception: Exception?): String = when (exception) {
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
        else -> "Erro desconhecido"
    }

    fun register(user: User): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            try {
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnSuccessListener {
                        firebaseFirestore.collection(USERS).document(user.email)
                            .set(
                                mapOf(
                                    "uidUser" to firebaseAuth.uid.toString(),
                                    "email" to user.email
                                )
                            )
                            .addOnSuccessListener {
                                sessionManager.saveUidUser(firebaseAuth.uid.toString())
                                sessionManager.saveEmailUser(user.email)
                                value = Resource(true, firebaseAuth.uid.toString())
                            }
                            .addOnFailureListener {
                                value = Resource(false, "falha ao cadastrar o usuário")
                            }
                    }
                    .addOnFailureListener { exception ->
                        val messageError: String = returnErrorForRegister(exception)
                        value = Resource(false, messageError)
                    }
            } catch (e: IllegalArgumentException) {
                value = Resource(false, "E-mail ou senha não ser vazio")
            }
        }

    private fun returnErrorForRegister(exception: Exception): String = when (exception) {
        is FirebaseAuthWeakPasswordException -> "Senha precisa de pelo menos 6 dígitos"
        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"
        else -> "Erro desconhecido"
    }

    fun saveUserDataToFirebase(userApp: UserApp): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            addEmailAndRecoveryContacts(userApp)
            val referenceFile = firebaseStorage.reference.child("/images/${UUID.randomUUID()}")
            referenceFile.putFile(userApp.imageProfile!!.toUri())
                .addOnSuccessListener {
                    referenceFile.downloadUrl.addOnSuccessListener {
                        firebaseFirestore.collection(USERS)
                            .document(sessionManager.getSavedEmailUser())
                            .set(userApp)
                            .addOnSuccessListener {
                                value = Resource(true)
                            }
                            .addOnFailureListener {
                                value = Resource(false, "Falha ao salvar os dados de perfil")
                            }
                    }
                }
                .addOnFailureListener {
                    //criar um metodo para adiconar imagem generica
                    value = Resource(false, "Falha ao salvar a imagem de perfil")
                }
        }

    private fun MutableLiveData<Resource<Boolean>>.addEmailAndRecoveryContacts(
        userApp: UserApp
    ) {
        userApp.email = sessionManager.getSavedEmailUser()
        firebaseFirestore.collection(USERS).document(sessionManager.getSavedEmailUser())
            .get()
            .addOnSuccessListener {
                it.get("contacts")?.let { contact ->
                    userApp.contacts = contact as MutableList<UserApp.Contact>
                }
            }.addOnFailureListener {
                value = Resource(false, "Falha ao tentar recuperar os contatos")
            }
    }

    fun isLoggedIn(): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = sessionManager.getSavedUidUser().isNotEmpty()
    }

    fun logoutApp() {
        sessionManager.saveUidUser("")
    }

    companion object {
        const val USERS = "users"
    }

}
