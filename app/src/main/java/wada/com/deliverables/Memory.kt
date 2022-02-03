package wada.com.deliverables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory(
    @PrimaryKey(autoGenerate = true)
    var id:Int,val title:String,val Contents:String,val longitude:Double,val  latitude:Double ,val Date:String) {
}