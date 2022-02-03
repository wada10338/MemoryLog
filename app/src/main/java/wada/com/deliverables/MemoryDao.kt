package wada.com.deliverables

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface MemoryDao {
    @Insert
    fun insert(memory:Memory)

    @Query("select * from memory")
    fun getAll():LiveData<List<Memory>>
}