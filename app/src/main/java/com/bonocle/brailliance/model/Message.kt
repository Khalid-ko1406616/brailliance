package com.bonocle.brailliance.model

import android.icu.util.Calendar
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


@kotlinx.serialization.Serializable
data class Message (
    @DocumentId
    val id: String = "",
    var content: String = "",
    val studentID: String = "",
    val instructorID: String = "",
    var isSSender: Boolean = true,
    @ServerTimestamp
    val dateSent: Date = Calendar.getInstance().time
){

    constructor(): this("","","","",true)

    constructor(content: String,studentID: String,instructorID: String,isSSender: Boolean): this("",content,studentID,instructorID,isSSender)

    override fun toString()
            = "\n Message $id :" +
              "\n From : ${ if(isSSender) "$studentID" else "$instructorID" } " +
              "\n To : ${ if(isSSender) "$instructorID" else "$studentID"}" +
              "\n Body : \n\t $content"


}