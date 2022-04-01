package wada.com.deliverables.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Memory::class], version = 1, exportSchema = false)
abstract class MemoryDatabase:RoomDatabase(){
    abstract fun MemoryDao(): MemoryDao

    companion object{
        private var INSTANCE: MemoryDatabase?= null

        fun getMemoryDatabase(context: Context): MemoryDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MemoryDatabase::class.java,
                    "Memory.db"
                ).build()
            }
            return INSTANCE
        }
    }

}