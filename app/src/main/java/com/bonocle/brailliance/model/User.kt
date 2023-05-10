package com.bonocle.brailliance.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class User (
    @DocumentId
    var uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    // Marks property as excluded from saving to Firestore
    @get:Exclude val password: String,
    val role: String){

    constructor(): this("", "", "", "",  "", "")

    constructor(uid: String, displayName: String, email: String): this(uid, displayName, "", email,  "", "")

    override fun toString()
            = "${firstName.trim()} - ${email.trim()}" //${lastName.trim()}
}
