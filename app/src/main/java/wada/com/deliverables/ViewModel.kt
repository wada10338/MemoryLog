package wada.com.deliverables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import wada.com.deliverables.DB.Memory
import wada.com.deliverables.DB.MemoryDatabase

class ViewModel(app: Application):AndroidViewModel(app) {
    lateinit var allMemories : MutableLiveData<List<Memory>>
    init{
        allMemories= MutableLiveData()
    }
    fun getAllMemoriesObservers(): MutableLiveData<List<Memory>>{
        return allMemories
    }
    fun getAllMemories(){
        val memoryDao=MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        val list=memoryDao?.getAllMemoriesInfo()
    }
    fun insertMemory(entity: Memory){
        val memoryDao=MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        memoryDao?.insertMemory(entity)
        getAllMemories()
    }
    fun deleteMemory(entity: Memory){
        val memoryDao=MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        memoryDao?.deleteMemory(entity)
        getAllMemories()
    }
    fun updateMemory(entity: Memory){
        val memoryDao=MemoryDatabase.getMemoryDatabase((getApplication()))?.MemoryDao()
        memoryDao?.updateMemory(entity)
        getAllMemories()
    }
}