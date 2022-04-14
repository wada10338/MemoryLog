package wada.com.deliverables

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_row.view.*
import wada.com.deliverables.DB.Memory
import wada.com.deliverables.databinding.ActivityMainBinding


class RecyclerViewAdapter(val listener: RowClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    var items = ArrayList<Memory>()

    fun setListData(data: ArrayList<Memory>) {
        this.items = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return MyViewHolder(inflater, listener)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])

    }
    class MyViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view) {

        val title = view.title
        val Contents = view.Contents
        val deleteMemoryID=view.deleteMemoryID

        fun bind(data: Memory) {
            title.text=data.title
            Contents.text=data.Contents

            deleteMemoryID.setOnClickListener {
                listener.onDeleteUserClickListener(data)
            }
        }
    }
    interface RowClickListener{
        fun onDeleteUserClickListener(user: Memory)
        fun onItemClickListener(user: Memory)
    }
}