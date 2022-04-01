package wada.com.deliverables.DB

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface MemoryDao {
    //データの挿入
    @Insert
    fun insertMemory(memory:Memory)

    //取得
    @Query("select * from memory")
    fun getAllMemoriesInfo():List<Memory>
    //削除
    @Delete
    fun deleteMemory(memory:Memory)
    //アップデート
    @Update
    fun updateMemory(memory:Memory)

    @Query("SELECT * FROM memory ORDER BY id ASC")
    fun getAlphabetizedMemories():Flow<List<Memory>>


}