package wada.com.deliverables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory(
    @PrimaryKey(autoGenerate = true)
    var id:Int,val title:String,val Contents:String,val  latitude:Double ,val longitude:Double,val Date:String) {
}