package com.bonocle.brailliance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bonocle.brailliance.model.MainRepository
import com.bonocle.brailliance.model.Session
import com.bonocle.brailliance.model.User
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionViewModel (appContext: Application) :AndroidViewModel(appContext) {

    private val sessionsRepo = MainRepository(appContext)

    var selectedSession : Session? = null
    private var sessionsUpdateListener : ListenerRegistration? = null

    val sessionsList = mutableListOf<Session>()

    init {
        getSessionsList()
    }

    private fun getSessionsList() {
        sessionsUpdateListener?.remove()

        val query = sessionsRepo.getUserSessions()
        if(query == null) {
            sessionsList.clear()
            return
        }
        sessionsUpdateListener = query.addSnapshotListener {snapshot, e ->
            if (e != null) {
                println(">> Debug: Sessions List Update Listener failed. ${e.message}")
                return@addSnapshotListener
            }
            val results = snapshot?.toObjects(Session::class.java)
            sessionsList.clear()
            results?.let {
                sessionsList.addAll( it )
            }
        }
    }

    fun addSession(session: Session) = viewModelScope.launch(Dispatchers.IO){
        sessionsRepo.addSession(session)
    }

    fun updateSession(session: Session) = viewModelScope.launch(Dispatchers.IO){
        sessionsRepo.updateSession(session)
    }

    fun updateSessionStudent(session: Session, student: User) = viewModelScope.launch(Dispatchers.IO){
        sessionsRepo.updateSessionStudent(session.id, student.uid)
    }

    fun deleteSession(session: Session) = viewModelScope.launch(Dispatchers.IO){
        sessionsRepo.deleteSession(session)
    }

    fun initDB() = liveData<String> {
        val result = sessionsRepo.initDB()
        emit(result)
    }

    override fun onCleared() {
        sessionsUpdateListener?.remove()
        super.onCleared()
    }

}