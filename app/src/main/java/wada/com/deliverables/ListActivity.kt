package wada.com.deliverables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_list.*
import wada.com.deliverables.DB.Memory

class ListActivity : AppCompatActivity(),RecyclerViewAdapter.RowClickListener {
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        var backAcButton=findViewById<Button>(R.id.backActivityButton)
        backAcButton.setOnClickListener { finish() }


        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@ListActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        viewModel.getAllUsersObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

    }
    override fun onDeleteUserClickListener(memory: Memory) {
        viewModel.deleteUserInfo(memory)
    }

    override fun onItemClickListener(memory: Memory) {

    }
}