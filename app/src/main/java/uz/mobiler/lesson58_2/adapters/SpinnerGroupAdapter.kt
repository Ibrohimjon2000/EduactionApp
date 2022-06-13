package uz.mobiler.lesson58_2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.entity.Group

class SpinnerGroupAdapter(context: Context, mentorList: ArrayList<Group>) :
    ArrayAdapter<Group>(context, 0, mentorList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val group = getItem(position)

        val view = LayoutInflater.from(context).inflate(R.layout.spinner_group_item, parent, false)
        view.findViewById<TextView>(R.id.groupNameSpinner).text = group?.name
        return view
    }
}