package wada.com.deliverables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import wada.com.deliverables.DB.Memory
import wada.com.deliverables.DB.MemoryDatabase

class ViewModel(app: Application):AndroidViewModel(app) {
    var allMemories : MutableLiveData<List<Memory>> = MutableLiveData()
    init{
        getAllMemories()
    }

    fun getAllUsersObservers(): MutableLiveData<List<Memory>> {
        return allMemories
    }

    fun getAllMemories() {
        val userDao = MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        val list = userDao?.getAllMemoriesInfo()

        allMemories.postValue(list)
    }

    fun insertUserInfo(entity: Memory){
        val userDao = MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        userDao?.insertMemory(entity)
        getAllMemories()
    }

    fun updateUserInfo(entity: Memory){
        val userDao = MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        userDao?.updateMemory(entity)
        getAllMemories()
    }

    fun deleteUserInfo(entity: Memory){
        val userDao = MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        userDao?.deleteMemory(entity)
        getAllMemories()
    }
}