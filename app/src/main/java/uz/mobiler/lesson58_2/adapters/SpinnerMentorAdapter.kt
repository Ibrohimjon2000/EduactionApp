package uz.mobiler.lesson58_2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.entity.Mentor

class SpinnerMentorAdapter(context: Context, mentorList: ArrayList<Mentor>) :
    ArrayAdapter<Mentor>(context, 0, mentorList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mentor = getItem(position)

        val view = LayoutInflater.from(context).inflate(R.layout.spinner_mentor_item, parent, false)
        view.findViewById<TextView>(R.id.firstNameSpinner).text = mentor?.firstName
        view.findViewById<TextView>(R.id.lastNameSpinner).text = mentor?.lastName
        return view
    }
}