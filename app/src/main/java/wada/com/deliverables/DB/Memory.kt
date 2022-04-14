package wada.com.deliverables.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")var id:Int,
    @ColumnInfo(name = "title")val title:String,
    @ColumnInfo(name = "Contents")val Contents:String,
    @ColumnInfo(name = "latitude")val  latitude:Double,
    @ColumnInfo(name = "longitude")val longitude:Double,
    @ColumnInfo(name = "date")val Date:String) {
}