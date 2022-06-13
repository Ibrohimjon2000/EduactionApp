package uz.mobiler.lesson58_2.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.SpinnerGroupAdapter
import uz.mobiler.lesson58_2.adapters.SpinnerMentorAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Group
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.database.entity.Student
import uz.mobiler.lesson58_2.databinding.FragmentAddStudentBinding
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "id"

class AddStudentFragment : Fragment() {
    private var param1: Long = -1
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentAddStudentBinding
    private lateinit var mentorList: ArrayList<Mentor>
    private lateinit var groupList: ArrayList<Group>
    private lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        mentorList = ArrayList(appDatabase.mentorDao().getAllMentors(param1))
        list = ArrayList(emptyList())
        for (mentor in mentorList) {
            list.add(mentor.firstName + " " + mentor.lastName)
        }
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val calendar = Calendar.getInstance()
            val dp = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                val format = "dd/MM/yyyy"
                val simpleDateFormat = SimpleDateFormat(format, Locale.US)
                date.setText(simpleDateFormat.format(calendar.time))
            }
            date.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dp,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            var groupIds: Long = -1
            var mentorIds: Long = -1
            val adapter1 = SpinnerMentorAdapter(requireContext(), mentorList)
            mentorId.adapter = adapter1
            mentorId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    mentorIds = mentorList[position].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
            groupList = ArrayList(appDatabase.groupDao().getAllIdGroup(param1))

            val adapter2 = SpinnerGroupAdapter(requireContext(), groupList)
            groupId.adapter = adapter2
            groupId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    groupIds = groupList[position].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            addMentor.setOnClickListener {
                val firstName = firstName.text
                val lastName = lastName.text
                val patron = patron.text
                val day = date.text.toString()
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && patron.isNotEmpty() && day.isNotEmpty() && groupIds != -1L && mentorIds != -1L) {
                    val student = Student(
                        firstName = firstName.toString(),
                        lastName = lastName.toString(),
                        patron = patron.toString(),
                        registerDate = day,
                        groupId = groupIds,
                        mentorId = mentorIds
                    )
                    appDatabase.studentDao().addStudent(student)
                    Navigation.findNavController(binding.root).popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            AddStudentFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Navigation.findNavController(binding.root).popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}