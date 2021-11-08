package com.example.tecnomessager.data.model

data class UserApp (
    val uidUser: String? = null,
    var email: String? = null,
    val imageProfile: String? = null,
    val nameProfile: String? = null,
    val messageProfile:String? = null,
    var contacts: List<Contact>? = null
){
    data class Contact(
        val nameContact: String? = null,
        val idChatMessages: String? = null
    )
}
