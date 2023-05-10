package com.bonocle.brailliance.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Session (
    @DocumentId
    val id: String = "",
    val subject: String,
    val meetingTime: String,
    var instructorID: String,
    var studentID: String? = null,
    @ServerTimestamp
    val updatedDate: Date = Calendar.getInstance().time
) {
    constructor(): this("","","","","")

    constructor(subject: String, meetingTime: String, instructorID: String): this("",subject,meetingTime,instructorID,"")

    override fun toString()
            = "${subject.trim()} - ${meetingTime.trim()}"

}

