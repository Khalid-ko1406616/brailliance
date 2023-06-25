package com.bonocle.brailliance.model

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import androidx.lifecycle.liveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.tasks.await

class MainRepository(private val context: Context) {

    //region SESSION DATABASE
    private val sessionCollectionRef by lazy {
        Firebase.firestore.collection("sessions")
    }

    fun getUserSessions() : Query? {
        val uid = Firebase.auth.currentUser?.uid?: return null
        return sessionCollectionRef.whereEqualTo("uid",uid)
    }

    private suspend fun getSession(uid: String): Session? {
        val queryResult = sessionCollectionRef
            .whereEqualTo("InstructorID",uid)
            .get().await()

        return queryResult.firstOrNull()?.toObject(Session::class.java)
    }

    suspend fun addSession(session: Session) : String? {
        val uid = Firebase.auth.currentUser?.uid?: return null
        println(">> Debug: Firebase.auth.currentUser?.displayName -> ${Firebase.auth.currentUser?.displayName}")

        val dbItem = getSession(uid)
        return if (dbItem == null) {
            session.instructorID = uid
            val queryResult = sessionCollectionRef.add(session).await()
            queryResult.id
        } else {
            dbItem.id
        }

    }

    suspend fun updateSession(session: Session) {
        sessionCollectionRef.document(session.id).set(session).await()
    }

    suspend fun updateSessionStudent(sessionID: String, studentID:String) {
        sessionCollectionRef.document(sessionID).update("studentID",studentID).await()
    }

    suspend fun deleteSession(session: Session){
        sessionCollectionRef.document(session.id).delete().await()
    }

    fun getSessions() = liveData<List<Session>> {
        val queryResult = sessionCollectionRef.orderBy("subject",Query.Direction.DESCENDING).get().await()
        val sessions = queryResult.toObjects(Session::class.java)
        emit(sessions)
    }

    private suspend fun isThereSessionCollection() : Boolean {
        val queryResult = sessionCollectionRef.limit(1).get().await()
        return queryResult.isEmpty
    }

    //endregion

    //region MESSAGE REGION

    private val messageCollectionRef by lazy {
        Firebase.firestore.collection("messages")
    }

    fun getStudentMessages() : Query? {
        val uid = Firebase.auth.currentUser?.uid?: return null
        return messageCollectionRef.whereEqualTo("studentID",uid)
    }

    fun getInstructorMessages() : Query? {
        val uid = Firebase.auth.currentUser?.uid?: return null
        return messageCollectionRef.whereEqualTo("instructorID",uid)
    }

    private suspend fun getStudentMessage(uid: String): Message? {
        val queryResult = messageCollectionRef
            .whereEqualTo("studentID",uid)
            .get().await()

        return queryResult.firstOrNull()?.toObject(Message::class.java)
    }

    suspend fun addStudentMessage(message: Message) : String? {
        val uid = Firebase.auth.currentUser?.uid?: return null
        println(">> Debug: Firebase.auth.currentUser?.displayName -> ${Firebase.auth.currentUser?.displayName}")

        val dbItem = getStudentMessage(uid)
        return if (dbItem == null) {
            var m = Message(message.content,Firebase.auth.currentUser!!.uid,message.instructorID)
            val queryResult = messageCollectionRef.add(m).await()
            queryResult.id
        } else {
            dbItem.id
        }

    }

    suspend fun addInstructorMessage(message: Message) : String? {

        val uid = Firebase.auth.currentUser?.uid?: return null
        println(">> Debug: Firebase.auth.currentUser?.displayName -> ${Firebase.auth.currentUser?.displayName}")

        val dbItem = getStudentMessage(uid)
        return if (dbItem == null) {
            var m = Message(message.content,message.studentID,Firebase.auth.currentUser!!.uid)
            val queryResult = messageCollectionRef.add(m).await()
            queryResult.id
        } else {
            dbItem.id
        }

    }

    suspend fun updateMessage(message: Message) {
        messageCollectionRef.document(message.id).set(message).await()
    }


    suspend fun deleteMessage(message: Message){
        messageCollectionRef.document(message.id).delete().await()
    }

    private suspend fun isThereMessageCollection() : Boolean {
        val queryResult = messageCollectionRef.limit(1).get().await()
        return queryResult.isEmpty
    }

    //endregion

    suspend fun initDB() : String {

        if(!isThereSessionCollection() || !isThereMessageCollection()) {
            return "Firebase database is already initialized"
        }

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        var data = context.assets.open("sessions.json")
            .bufferedReader().use { it.readText() }

        val sessions = json.decodeFromString<List<Session>>(data)

        data = context.assets.open("messages.json")
            .bufferedReader().use {it.readText()}

        val messages = json.decodeFromString<List<Message>>(data)

        println(">> Debug: initDB sessions $sessions")
        println(">> Debug: initDB messages $messages")

        for (session in sessions) {
            val sessionID = addSession(session)
            println(">> Debug: addSession(${sessionID})")

        }

        for (message in messages) {
            val messageID = addStudentMessage(message)
            println(">> Debug: addMessage(${messageID})")
        }

        return "Firebase database initialized with ${sessions.size} sessions & ${messages.size}."

    }

}

