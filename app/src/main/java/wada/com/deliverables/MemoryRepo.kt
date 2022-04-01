package wada.com.deliverables

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import wada.com.deliverables.DB.Memory
import wada.com.deliverables.DB.MemoryDao

class MemoryRepo(private val MemoryDao:MemoryDao) {
    val allMemories: Flow<List<Memory>> = MemoryDao.getAlphabetizedMemories()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: Memory){
        MemoryDao.insertMemory(entity)
    }
}