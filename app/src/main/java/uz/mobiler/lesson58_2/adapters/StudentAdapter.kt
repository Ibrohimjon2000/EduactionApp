package uz.mobiler.lesson58_2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.entity.Student

class StudentAdapter(
    var studentList: ArrayList<Student>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<StudentAdapter.Vh>() {

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstName: TextView = itemView.findViewById(R.id.firstName)
        var lastName: TextView = itemView.findViewById(R.id.lastName)
        var patron: TextView = itemView.findViewById(R.id.patron)
        var edit: CardView = itemView.findViewById(R.id.edit)
        var delete: CardView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.mentor_item, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val list = studentList[position]
        holder.firstName.text = list.firstName
        holder.lastName.text = list.lastName
        holder.patron.text = list.patron
        holder.edit.setOnClickListener {
            listener.onItemEdit(
                studentList[position],
                position
            )
        }
        holder.delete.setOnClickListener {
            listener.onItemDelete(
                studentList[position],
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    interface OnItemClickListener {
        fun onItemEdit(student: Student, position: Int)
        fun onItemDelete(student: Student, position: Int)
    }
}