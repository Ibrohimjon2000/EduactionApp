package uz.mobiler.lesson58_2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.entity.Course

class CourseAdapter(
    var courseList: ArrayList<Course>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourseAdapter.Vh>() {

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.course_item, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val list = courseList[position]
        holder.name.text = list.name
        holder.itemView.setOnClickListener { listener.onItemClick(list, position) }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    interface OnItemClickListener {
        fun onItemClick(course: Course, position: Int)
    }
}