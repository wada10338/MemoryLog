package wada.com.deliverables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import wada.com.deliverables.R

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        var backAcButton=findViewById<Button>(R.id.backActivityButton)
        backAcButton.setOnClickListener { finish() }
    }
}