package uz.mobiler.lesson58_2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Group

class GroupAdapter(
    var groupList: ArrayList<Group>,
    val listener: OnItemClickListener,
    val context: Context
) : RecyclerView.Adapter<GroupAdapter.Vh>() {
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(context)
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var groupName: TextView = itemView.findViewById(R.id.groupName)
        var studentCount: TextView = itemView.findViewById(R.id.studentCount)
        var edit: CardView = itemView.findViewById(R.id.edit)
        var delete: CardView = itemView.findViewById(R.id.delete)
        var view: CardView = itemView.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.group_item, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val list = groupList[position]
        val studentList = appDatabase.studentDao().getAllStudent(groupList[position].id)
        holder.studentCount.text = "O’quvchilar soni: " + studentList.size.toString()
        holder.groupName.text = list.name
        holder.edit.setOnClickListener {
            listener.onItemEdit(
                groupList[position],
                position
            )
        }
        holder.delete.setOnClickListener {
            listener.onItemDelete(
                groupList[position],
                position
            )
        }
        holder.view.setOnClickListener {
            listener.onItemClick(
                groupList[position],
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    interface OnItemClickListener {
        fun onItemClick(group: Group, position: Int)
        fun onItemEdit(group: Group, position: Int)
        fun onItemDelete(group: Group, position: Int)
    }
}