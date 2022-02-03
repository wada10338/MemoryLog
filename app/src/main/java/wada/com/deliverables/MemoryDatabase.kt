package wada.com.deliverables

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Memory::class], version = 1, exportSchema = false)
abstract class MemoryDatabase:RoomDatabase(){
    abstract fun MemoryDao():MemoryDao
}