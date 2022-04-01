package wada.com.deliverables

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import wada.com.deliverables.DB.Memory

class MemoryViewModel(private val repo: MemoryRepo):ViewModel() {
    val allMemories:LiveData<List<Memory>> = repo.allMemories.asLiveData()

    fun insert(entity: Memory) = viewModelScope.launch {
        repo.insert(entity)
    }
}
class MemoryViewModelFactory(private val repo: MemoryRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MemoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MemoryViewModel(repo)as T
        }
        throw  IllegalAccessException("Unknown ViewModel class")
    }
}